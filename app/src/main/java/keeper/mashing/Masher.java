package keeper.mashing;

public interface Masher {
    Object encrypt(Object plainText, Object passKey);

    Object decrypt(Object cipherText, Object passKey);
}
