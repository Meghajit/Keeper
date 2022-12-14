package keeper.mashing.twofish;

import keeper.exception.MasherException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TwoFishMasherTest {

    @Test
    public void shouldThrowExceptionDuringEncryptionIfPlainTextIsNull() {
        TwoFishMasher masher = new TwoFishMasher();
        NullPointerException exception = assertThrows(NullPointerException.class, () -> masher.encrypt(null, new byte[]{9, 122}));
        assertEquals("Plain text is null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionDuringEncryptionIfPassKeyIsNull() {
        TwoFishMasher masher = new TwoFishMasher();
        NullPointerException exception = assertThrows(NullPointerException.class, () -> masher.encrypt(new byte[]{9, 122}, null));
        assertEquals("Pass key is null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionDuringEncryptionIfPassKeyIsNotOfCorrectSize() {
        TwoFishMasher masher = new TwoFishMasher();
        byte[] plainText = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] passKey = new byte[40];
        Arrays.fill(passKey, (byte) 'x');

        MasherException exception = assertThrows(MasherException.class, () -> masher.encrypt(plainText, passKey));
        assertEquals("PassKey should be between 8 and 32 bytes", exception.getMessage());
    }

    @Test
    public void shouldSupportEncryptionWithPassKeyOfAnySizeBetween8And32Bytes() {
        TwoFishMasher masher = new TwoFishMasher();
        byte[] inputPlainText = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] passKey = new byte[]{99, 99, 99, 99, 99, 99, 99, 99, 99, 121, 11, 13};

        byte[] cipherText = masher.encrypt(inputPlainText, passKey);
        byte[] expectedDecryptedValue = masher.decrypt(cipherText, passKey);

        assertArrayEquals(inputPlainText, expectedDecryptedValue);
    }

    @Test
    public void shouldSupportEncryptionOfPlainTextLessThanBlockSize() {
        TwoFishMasher masher = new TwoFishMasher();
        byte[] inputPlainText = new byte[]{1, 2, 3, 4};
        byte[] passKey = new byte[]{99, 99, 99, 99, 99, 99, 99, 99};

        byte[] cipherText = masher.encrypt(inputPlainText, passKey);
        byte[] expectedDecryptedValue = masher.decrypt(cipherText, passKey);

        assertArrayEquals(inputPlainText, expectedDecryptedValue);
    }

    @Test
    public void shouldSupportEncryptionOfPlainTextMoreThanBlockSize() {
        TwoFishMasher masher = new TwoFishMasher();
        byte[] inputPlainText = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
        byte[] passKey = new byte[]{99, 99, 99, 99, 99, 99, 99, 99};

        byte[] cipherText = masher.encrypt(inputPlainText, passKey);
        byte[] expectedDecryptedValue = masher.decrypt(cipherText, passKey);

        assertArrayEquals(inputPlainText, expectedDecryptedValue);
    }

    @Test
    public void shouldThrowExceptionDuringDecryptionIfCipherTextIsNull() {
        TwoFishMasher masher = new TwoFishMasher();
        NullPointerException exception = assertThrows(NullPointerException.class, () -> masher.decrypt(null, new byte[]{9, 122}));
        assertEquals("Cipher text is null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionDuringDecryptionIfPassKeyIsNull() {
        TwoFishMasher masher = new TwoFishMasher();
        NullPointerException exception = assertThrows(NullPointerException.class, () -> masher.decrypt(new byte[]{9, 122}, null));
        assertEquals("Pass key is null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionDuringDecryptionIfPassKeyIsNotOfCorrectSize() {
        TwoFishMasher masher = new TwoFishMasher();
        byte[] plainText = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        byte[] passKey = new byte[]{1, 2, 3, 4, 5, 6};

        MasherException exception = assertThrows(MasherException.class, () -> masher.decrypt(plainText, passKey));
        assertEquals("PassKey should be between 8 and 32 bytes", exception.getMessage());
    }
}
