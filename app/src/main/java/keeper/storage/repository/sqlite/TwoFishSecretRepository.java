package keeper.storage.repository.sqlite;

import keeper.entity.EncryptedSecret;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TwoFishSecretRepository {
    private final Connection connection;

    public TwoFishSecretRepository() {
        try {
            String dbPath = "app/src/main/resources/keeper.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            System.out.println("SecretRepository DB connection established");
            String sql = "CREATE TABLE IF NOT EXISTS SECRET " +
                    "(CUSTOMER_UUID TEXT NOT NULL," +
                    " SECRET_NAME TEXT NOT NULL," +
                    " CIPHER_TEXT TEXT NOT NULL," +
                    " PRIMARY KEY (CUSTOMER_UUID,SECRET_NAME))";
            statement.executeUpdate(sql);
            System.out.println("Secret table successfully initialized.");
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EncryptedSecret<byte[]> createSecret(String secretName, String customerUUID, byte[] cipherText) {
        if (findSecretByName(secretName, customerUUID) == null) {
            try {
                String base64EncodedCipherText = Base64.getEncoder().encodeToString(cipherText);
                String sql = "INSERT INTO SECRET(CUSTOMER_UUID, SECRET_NAME, CIPHER_TEXT) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                statement.setString(2, secretName);
                statement.setString(3, base64EncodedCipherText);
                int recordsUpdated = statement.executeUpdate();
                if (recordsUpdated == 0) {
                    System.out.println("Secret could not be created.");
                    return null;
                }
                statement.close();
                System.out.println("Secret added to account.");
                return findSecretByName(secretName, customerUUID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Secret with the given name already exists.");
            return null;
        }
    }

    public EncryptedSecret<byte[]> findSecretByName(String secretName, String customerUUID) {
        try {
            String sql = "SELECT * FROM SECRET WHERE CUSTOMER_UUID = ? AND SECRET_NAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerUUID);
            statement.setString(2, secretName);
            ResultSet resultSet = statement.executeQuery();
            EncryptedSecret<byte[]> encryptedSecret = null;

            if (resultSet.next()) {
                String foundSecretName = resultSet.getString("SECRET_NAME");
                String foundCustomerUUID = resultSet.getString("CUSTOMER_UUID");
                byte[] foundCipherText = Base64.getDecoder().decode(resultSet.getString("CIPHER_TEXT"));
                encryptedSecret = new EncryptedSecret<>(foundSecretName, foundCustomerUUID, foundCipherText);
                if (resultSet.next()) {
                    throw new RuntimeException("Multiple secrets found with the same name !");
                }
            }
            resultSet.close();
            statement.close();
            return encryptedSecret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EncryptedSecret<byte[]>> findAllCustomerSecrets(String customerUUID) {
        try {
            String sql = "SELECT * FROM SECRET WHERE CUSTOMER_UUID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customerUUID);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<EncryptedSecret<byte[]>> allEncryptedSecrets = new ArrayList<>();

            while (resultSet.next()) {
                String foundSecretName = resultSet.getString("SECRET_NAME");
                String foundCustomerUUID = resultSet.getString("CUSTOMER_UUID");
                byte[] foundCipherText = Base64.getDecoder().decode(resultSet.getString("CIPHER_TEXT"));
                allEncryptedSecrets.add(new EncryptedSecret<>(foundSecretName, foundCustomerUUID, foundCipherText));
            }
            resultSet.close();
            statement.close();
            return allEncryptedSecrets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateSecretName(String oldSecretName, String newSecretName, String customerUUID) {
        if (findSecretByName(oldSecretName, customerUUID) != null) {
            if (findSecretByName(newSecretName, customerUUID) == null) {
                try {
                    String sql = "UPDATE SECRET SET SECRET_NAME = ? WHERE CUSTOMER_UUID = ? AND SECRET_NAME = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, newSecretName);
                    statement.setString(2, customerUUID);
                    statement.setString(3, oldSecretName);
                    int recordsUpdated = statement.executeUpdate();
                    statement.close();
                    if (recordsUpdated == 1) {
                        System.out.println("Secret name successfully updated.");
                        return true;
                    } else {
                        System.out.println("Issue in updating secret name.");
                        return false;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Secret with the new name already exists.");
                return false;
            }
        } else {
            System.out.println("Secret with the old name does not exist.");
            return false;
        }
    }

    public boolean updateSecretCipherText(byte[] newCipherText, String secretName, String customerUUID) {
        if (findSecretByName(secretName, customerUUID) != null) {
            try {
                String base64EncodedNewCipherText = Base64.getEncoder().encodeToString(newCipherText);
                String sql = "UPDATE SECRET SET CIPHER_TEXT = ? WHERE CUSTOMER_UUID = ? AND SECRET_NAME = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, base64EncodedNewCipherText);
                statement.setString(2, customerUUID);
                statement.setString(3, secretName);
                int recordsUpdated = statement.executeUpdate();
                statement.close();
                if (recordsUpdated == 1) {
                    System.out.println("Secret value was successfully updated.");
                    return true;
                } else {
                    System.out.println("Issue in updating secret value.");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Secret with the name provided does not exist.");
            return false;
        }
    }

    public boolean deleteSecretByName(String secretName, String customerUUID) {
        if (findSecretByName(secretName, customerUUID) != null) {
            try {
                String sql = "DELETE FROM SECRET WHERE CUSTOMER_UUID = ? AND SECRET_NAME = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                statement.setString(2, secretName);
                int recordsDeleted = statement.executeUpdate();
                statement.close();
                if (recordsDeleted == 1) {
                    System.out.println("Secret successfully deleted.");
                    return true;
                } else {
                    System.out.println("Issue in deleting secret.");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Secret with the name provided does not exist.");
            return false;
        }
    }

    public boolean deleteAllCustomerSecrets(String customerUUID) {
        List<EncryptedSecret<byte[]>> allSecrets = findAllCustomerSecrets(customerUUID);
        if (allSecrets.size() != 0) {
            try {
                String sql = "DELETE FROM SECRET WHERE CUSTOMER_UUID = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, customerUUID);
                int recordsDeleted = statement.executeUpdate();
                statement.close();
                if (recordsDeleted == allSecrets.size()) {
                    System.out.println("All secrets successfully deleted.");
                    return true;
                } else {
                    System.out.println("Issue in deleting secrets.");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No secrets exist for the customer.");
            return false;
        }
    }
}
