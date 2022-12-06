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
        return new Account(customerUUID, passKey);
    }

    public Account findAccount(String customerUUID, byte[] passKey) {
        return null;
    }

    public void updateAccount(String customerUUID, byte[] oldPassKey, byte[] newPassKey) {

    }

    public void deleteAccount(String customerUUID, byte[] passKey) {

    }
}
