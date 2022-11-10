package keeper.mashing;

import org.junit.Test;

import java.security.InvalidKeyException;

import static org.junit.Assert.assertArrayEquals;

public class EncryptorTest {

    @Test
    public void shouldEncryptASerializableObject() throws InvalidKeyException {
        Encryptor encryptor = new Encryptor();
        byte[] input = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] passKey = new byte[]{99, 99, 99, 99, 99, 99, 99, 99};
        byte[] cipherText = encryptor.encryptObject(input, passKey);
        byte[] plainText = encryptor.decryptObject(cipherText, passKey);
        assertArrayEquals(input, plainText);
    }
}