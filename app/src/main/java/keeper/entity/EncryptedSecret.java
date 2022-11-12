package keeper.entity;

import java.io.Serializable;

public class EncryptedSecret<T extends Serializable> {
    private final String name;
    private final String customerUUID;
    private final T cipherText;

    public EncryptedSecret(String name, String customerUUID, T cipherText) {
        this.name = name;
        this.customerUUID = customerUUID;
        this.cipherText = cipherText;
    }

    public String getName() {
        return name;
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public T getCipherText() {
        return cipherText;
    }
}
