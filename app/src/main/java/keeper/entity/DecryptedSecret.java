package keeper.entity;

import java.io.Serializable;

public class DecryptedSecret<T extends Serializable> {
    private final String name;
    private final String customerUUID;
    private final PlainText<T> plainText;

    public DecryptedSecret(String name, String customerUUID, PlainText<T> plainText) {
        this.name = name;
        this.customerUUID = customerUUID;
        this.plainText = plainText;
    }

    public String getName() {
        return name;
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public PlainText<T> getPlainText() {
        return plainText;
    }
}
