package keeper.mashing;

import gnu.crypto.cipher.Twofish;

import java.security.InvalidKeyException;
import java.util.Arrays;

public class Encryptor {
    private final Twofish twoFish;
    private static final byte PADDING_BYTE = 113;
    private static final int BLOCK_SIZE_IN_BYTES = 16;

    public Encryptor() {
        this.twoFish = new Twofish();
    }

    public byte[] decryptObject(byte[] cipherText, byte[] passKey) throws InvalidKeyException {
        Object sessionKey = twoFish.makeKey(passKey, 16);
        int potentialPaddingStartIndex = cipherText.length - 16;
        int paddingLengthIndicatorIndex = 15; // last byte of the block
        byte[] paddingBlock = new byte[16];
        byte[] plainText;
        twoFish.decrypt(cipherText, potentialPaddingStartIndex, paddingBlock, 0, sessionKey, BLOCK_SIZE_IN_BYTES);
        if (paddingBlock[paddingLengthIndicatorIndex] == 15) {
            // then I can ignore the last block as it is fully padding
            plainText = new byte[cipherText.length - 16];
            int numberOfInputBlocks = (cipherText.length / 16) - 1;
            for (int i = 1; i <= numberOfInputBlocks; i++) {
                int startOffset = (i - 1) * 16;
                twoFish.decrypt(cipherText, startOffset, plainText, startOffset, sessionKey, BLOCK_SIZE_IN_BYTES);
            }
        } else {
            plainText = new byte[0];
        }
        return plainText;
    }

    public byte[] encryptObject(byte[] input, byte[] passKey) throws InvalidKeyException {
        Object sessionKey = twoFish.makeKey(passKey, 16);
        byte[] cipherText;
        if (input.length % 16 == 0) {
            cipherText = new byte[input.length + 16];
            int numberOfInputBlocks = input.length / 16;
            for (int i = 1; i <= numberOfInputBlocks; i++) {
                int startOffset = (i - 1) * 16;
                twoFish.encrypt(input, startOffset, cipherText, startOffset, sessionKey, BLOCK_SIZE_IN_BYTES);
            }
            byte[] padding = new byte[16];
            Arrays.fill(padding, 0, 15, PADDING_BYTE);
            padding[15] = 15;
            twoFish.encrypt(padding, 0, cipherText, input.length, sessionKey, BLOCK_SIZE_IN_BYTES);
        } else {
            cipherText = new byte[0];
        }
        return cipherText;
    }
}