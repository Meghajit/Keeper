package keeper.mashing.twofish;

import com.google.common.base.Preconditions;
import gnu.crypto.cipher.Twofish;
import keeper.exception.MasherException;
import keeper.mashing.Masher;

import java.security.InvalidKeyException;
import java.util.Arrays;

public class TwoFishMasher implements Masher<byte[], byte[], byte[]> {
    private final Twofish twoFish;
    private static final byte PADDING_BYTE = 113;
    private static final int BLOCK_SIZE_IN_BYTES = 16;

    public TwoFishMasher() {
        this.twoFish = new Twofish();
    }

    private byte[] decryptObject(byte[] cipherText, byte[] passKey) throws InvalidKeyException {
        Object sessionKey = twoFish.makeKey(passKey, 16);
        byte[] plainText = new byte[cipherText.length];
        int numberOfInputBlocks = cipherText.length / 16;
        for (int i = 1; i <= numberOfInputBlocks; i++) {
            int startOffset = (i - 1) * 16;
            twoFish.decrypt(cipherText, startOffset, plainText, startOffset, sessionKey, BLOCK_SIZE_IN_BYTES);
        }
        int numberOfPaddingBytesPresent = plainText[plainText.length - 1];
        int actualDataLength = plainText.length - (numberOfPaddingBytesPresent + 1);
        return Arrays.copyOfRange(plainText, 0, actualDataLength);
    }

    private byte[] encryptObject(byte[] input, byte[] passKey) throws InvalidKeyException {
        Object sessionKey = twoFish.makeKey(passKey, 16);
        int numberOfPaddingBytesRequired = (16 * ((int) Math.floor(input.length / 16)) + 16) - input.length;
        if (numberOfPaddingBytesRequired == 0) {
            numberOfPaddingBytesRequired = 16;
        }
        int totalLength = input.length + numberOfPaddingBytesRequired;
        byte[] updatedInputWithPadding = new byte[totalLength];
        System.arraycopy(input, 0, updatedInputWithPadding, 0, input.length);
        Arrays.fill(updatedInputWithPadding, input.length, totalLength - 1, PADDING_BYTE);
        updatedInputWithPadding[totalLength - 1] = (byte) (numberOfPaddingBytesRequired - 1);
        byte[] cipherText = new byte[totalLength];
        int numberOfInputBlocks = totalLength / 16;
        for (int i = 1; i <= numberOfInputBlocks; i++) {
            int startOffset = (i - 1) * 16;
            twoFish.encrypt(updatedInputWithPadding, startOffset, cipherText, startOffset, sessionKey, BLOCK_SIZE_IN_BYTES);
        }
        return cipherText;
    }

    @Override
    public byte[] encrypt(byte[] plainText, byte[] passKey) {
        Preconditions.checkNotNull(plainText, "Plain text is null");
        Preconditions.checkNotNull(passKey, "Pass key is null");
        try {
            return encryptObject(plainText, passKey);
        } catch (InvalidKeyException ex) {
            throw new MasherException("Could not create session key from passkey", ex);
        }
    }

    @Override
    public byte[] decrypt(byte[] cipherText, byte[] passKey) {
        Preconditions.checkNotNull(cipherText, "Cipher text is null");
        Preconditions.checkNotNull(passKey, "Pass key is null");
        try {
            return decryptObject(cipherText, passKey);
        } catch (InvalidKeyException ex) {
            throw new MasherException("Could not create session key from passkey", ex);
        }
    }
}
