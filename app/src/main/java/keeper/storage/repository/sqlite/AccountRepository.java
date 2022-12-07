package keeper.storage.repository.sqlite;

import keeper.entity.Account;

import java.util.Base64;

import java.sql.*;

public class AccountRepository {
    private final Connection connection;

    public AccountRepository() {
        try {
            String dbPath = "app/src/main/resources/keeper.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            System.out.println("AccountRepository DB connection established");
            String sql = "CREATE TABLE IF NOT EXISTS ACCOUNT " +
                    "(CUSTOMER_UUID TEXT NOT NULL PRIMARY KEY," +
                    " ENCRYPTED_PASSKEY TEXT NOT NULL)";
            statement.executeUpdate(sql);
            System.out.println("Account table successfully initialized.");
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account createAccount(String customerUUID, byte[] encryptedPassKey) {
        if (findAccount(customerUUID) == null) {
            try {
                String base64EncodedEncryptedPassKey = Base64.getEncoder().encodeToString(encryptedPassKey);
                String sql = "INSERT INTO ACCOUNT(CUSTOMER_UUID, ENCRYPTED_PASSKEY) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                statement.setString(2, base64EncodedEncryptedPassKey);
                int recordsUpdated = statement.executeUpdate();
                statement.close();
                if (recordsUpdated == 0) {
                    System.out.println("Account could not be created.");
                    return null;
                }
                System.out.println("Account successfully created.");
                return findAccount(customerUUID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Account already exists.");
            return null;
        }
    }

    public Account findAccount(String customerUUID) {
        try {
            String sql = "SELECT * FROM ACCOUNT WHERE CUSTOMER_UUID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerUUID);
            ResultSet resultSet = statement.executeQuery();
            Account account = null;

            if (resultSet.next()) {
                String foundUUID = resultSet.getString("CUSTOMER_UUID");
                byte[] foundEncryptedPassKey = Base64.getDecoder().decode(resultSet.getString("ENCRYPTED_PASSKEY"));
                account = new Account(foundUUID, foundEncryptedPassKey);
                if (resultSet.next()) {
                    throw new RuntimeException("Multiple accounts found with the same customer uuid !");
                }
            }
            resultSet.close();
            statement.close();
            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAccount(String customerUUID, byte[] newEncryptedPassKey) {
        if (findAccount(customerUUID) != null) {
            try {
                String base64EncodedNewPassKey = Base64.getEncoder().encodeToString(newEncryptedPassKey);
                String sql = "UPDATE ACCOUNT SET ENCRYPTED_PASSKEY = ? WHERE CUSTOMER_UUID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, base64EncodedNewPassKey);
                statement.setString(2, customerUUID);
                int recordsUpdated = statement.executeUpdate();
                statement.close();
                if (recordsUpdated == 1) {
                    System.out.println("Account successfully updated.");
                    return true;
                } else {
                    System.out.println("Account could not be updated.");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Account does not exist.");
            return false;
        }
    }

    public boolean deleteAccount(String customerUUID) {
        if (findAccount(customerUUID) != null) {
            try {
                String sql = "DELETE FROM ACCOUNT WHERE CUSTOMER_UUID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                int recordsDeleted = statement.executeUpdate();
                statement.close();
                if (recordsDeleted == 0) {
                    System.out.println("Account could not be deleted.");
                    return false;
                } else {
                    System.out.println("Account successfully deleted.");
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Account does not exist.");
            return false;
        }
    }
}
