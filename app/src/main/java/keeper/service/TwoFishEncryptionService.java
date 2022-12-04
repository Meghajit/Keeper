package keeper.service;

import keeper.mashing.twofish.TwoFishMasher;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * @param <T> The serializable template class
 */
public class TwoFishEncryptionService<T extends Serializable> implements EncryptionService<byte[], T, byte[]> {
    private final TwoFishMasher masher;

    public TwoFishEncryptionService() {
        this.masher = new TwoFishMasher();
    }

    @Override
    public byte[] encrypt(T plainText, byte[] passKey) {
        byte[] plainTextBytes = SerializationUtils.serialize(plainText);
        return masher.encrypt(plainTextBytes, passKey);
    }

    @Override
    public T decrypt(byte[] cipherText, byte[] passKey) {
        byte[] plainTextBytes = masher.decrypt(cipherText, passKey);
        return SerializationUtils.deserialize(plainTextBytes);
    }
}
