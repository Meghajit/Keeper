package keeper.entity;

import java.io.Serializable;

public class EncryptedSecret<T extends Serializable> implements Serializable {
    private final String secretName;
    private final String customerUUID;
    private final T cipherText;

    public EncryptedSecret(String name, String customerUUID, T cipherText) {
        this.secretName = name;
        this.customerUUID = customerUUID;
        this.cipherText = cipherText;
    }

    public String getSecretName() {
        return secretName;
    }

    public String getCustomerUUID() {
        return customerUUID;
    }

    public T getCipherText() {
        return cipherText;
    }

    @Override
    public String toString() {
        return "EncryptedSecret{" +
                "secretName='" + secretName + '\'' +
                ", customerUUID='" + customerUUID + '\'' +
                ", cipherText=" + cipherText +
                '}';
    }
}
