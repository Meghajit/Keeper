package keeper.entity;

import java.io.Serializable;

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
}
