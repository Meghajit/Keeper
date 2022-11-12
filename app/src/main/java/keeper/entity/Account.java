package keeper.entity;

import java.io.Serializable;

public class Account<T extends Serializable> {
    private final String customerUUID;
    private final T encryptedPassKey;

    public Account(String customerUUID, T encryptedPassKey) {
        this.customerUUID = customerUUID;
        this.encryptedPassKey = encryptedPassKey;
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public T getEncryptedPassKey() {
        return encryptedPassKey;
    }
}
