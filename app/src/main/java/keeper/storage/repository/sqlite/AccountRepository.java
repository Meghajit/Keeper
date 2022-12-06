package keeper.storage.repository.sqlite;

import keeper.entity.Account;

import java.sql.Connection;
import java.sql.DriverManager;

public class AccountRepository {
    private final Connection connection;

    public AccountRepository() {
        try {
            String dbPath = "src/main/resources/keeper.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("AccountRepository DB connection established");

    }

    public Account createAccount(String customerUUID, byte[] passKey) {
        return null;
    }

    public Account findAccount(String customerUUID, byte[] passKey) {
        return null;
    }

    public void updateAccount(String customerUUID, byte[] oldPassKey, byte[] newPassKey) {

    }

    public void deleteAccount(String customerUUID, byte[] passKey) {

    }
}
