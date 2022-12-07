package keeper.entity;

import java.io.Serializable;
import java.util.Arrays;

public class Account implements Serializable {
    private final String customerUUID;
    private final byte[] encryptedPassKey;

    public Account(String customerUUID, byte[] encryptedPassKey) {
        this.customerUUID = customerUUID;
        this.encryptedPassKey = encryptedPassKey;
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public byte[] getEncryptedPassKey() {
        return encryptedPassKey;
    }

    @Override
    public String toString() {
        return "Account{" +
                "customerUUID='" + customerUUID + '\'' +
                ", encryptedPassKey=" + Arrays.toString(encryptedPassKey) +
                '}';
    }
}
