package keeper.service;

public interface EncryptionService<E, D, P> {

    E encrypt(D plainText, P passKey);

    D decrypt(E cipherText, P passKey);
}
