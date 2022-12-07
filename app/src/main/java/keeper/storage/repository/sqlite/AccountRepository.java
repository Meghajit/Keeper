package keeper.storage.repository.sqlite;

import keeper.entity.Account;

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
                    "(CUSTOMER_UUID TEXT NOT NULL," +
                    " ENCRYPTED_PASSKEY BLOB NOT NULL, " +
                    " PRIMARY KEY (CUSTOMER_UUID, ENCRYPTED_PASSKEY))";
            statement.executeUpdate(sql);
            System.out.println("Account table successfully initialized.");
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account createAccount(String customerUUID, byte[] passKey) {
        try {
            String sql = "INSERT INTO ACCOUNT(CUSTOMER_UUID, ENCRYPTED_PASSKEY) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerUUID);
            statement.setBytes(2, passKey);
            System.out.println(statement.executeUpdate() + " records inserted");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Account successfully created.");
        return new Account(customerUUID, passKey);
    }

    public Account findAccount(String customerUUID, byte[] passKey) {
        try {
            String sql = "SELECT * FROM ACCOUNT WHERE CUSTOMER_UUID = ? AND ENCRYPTED_PASSKEY = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerUUID);
            statement.setBytes(2, passKey);
            ResultSet resultSet = statement.executeQuery();
            Account account;

            if (resultSet.next()) {
                account = new Account(resultSet.getString("CUSTOMER_UUID"), resultSet.getBytes("ENCRYPTED_PASSKEY"));
                if (resultSet.next()) {
                    throw new RuntimeException("Multiple accounts found with the same credentials !");
                }
            } else {
                account = null;
            }
            resultSet.close();
            statement.close();
            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAccount(String customerUUID, byte[] oldPassKey, byte[] newPassKey) {
        if (findAccount(customerUUID, oldPassKey) != null) {
            try {
                String sql = "UPDATE ACCOUNT SET ENCRYPTED_PASSKEY = ? WHERE CUSTOMER_UUID = ? AND ENCRYPTED_PASSKEY = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setBytes(1, newPassKey);
                statement.setString(2, customerUUID);
                statement.setBytes(3, oldPassKey);
                int recordsUpdated = statement.executeUpdate();
                if (recordsUpdated == 0) {
                    return false;
                } else {
                    System.out.println("Account successfully updated.");
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return false;
    }

    public boolean deleteAccount(String customerUUID, byte[] passKey) {
        if (findAccount(customerUUID, passKey) != null) {
            try {
                String sql = "DELETE FROM ACCOUNT WHERE CUSTOMER_UUID = ? AND ENCRYPTED_PASSKEY = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                statement.setBytes(2, passKey);
                int recordsDeleted = statement.executeUpdate();
                if (recordsDeleted == 0) {
                    return false;
                } else {
                    System.out.println("Account successfully deleted.");
                    return true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }
}
