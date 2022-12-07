package keeper.service;

import keeper.entity.EncryptedSecret;
import keeper.storage.repository.sqlite.TwoFishSecretRepository;

import java.util.List;

public class TwoFishSecretService {
    private final TwoFishSecretRepository secretRepository;

    public TwoFishSecretService() {
        this.secretRepository = new TwoFishSecretRepository();
    }

    public EncryptedSecret<byte[]> createSecret(String secretName, String customerUUID, byte[] cipherText) {
        return secretRepository.createSecret(secretName, customerUUID, cipherText);
    }

    public EncryptedSecret<byte[]> findSecretByName(String secretName, String customerUUID) {
        return secretRepository.findSecretByName(secretName, customerUUID);
    }

    public List<EncryptedSecret<byte[]>> findAllCustomerSecrets(String customerUUID) {
        return secretRepository.findAllCustomerSecrets(customerUUID);
    }

    public boolean updateSecretName(String oldSecretName, String newSecretName, String customerUUID) {
        return secretRepository.updateSecretName(oldSecretName, newSecretName, customerUUID);
    }

    public boolean updateSecretCipherText(byte[] newCipherText, String secretName, String customerUUID) {
        return secretRepository.updateSecretCipherText(newCipherText, secretName, customerUUID);
    }

    public boolean deleteSecretByName(String secretName, String customerUUID) {
        return secretRepository.deleteSecretByName(secretName, customerUUID);
    }

    public boolean deleteAllCustomerSecrets(String customerUUID) {
        return secretRepository.deleteAllCustomerSecrets(customerUUID);
    }
}
