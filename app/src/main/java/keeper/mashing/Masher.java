package keeper.mashing;

import java.io.Serializable;

/**
 * @param <E> Class type of cipher text
 * @param <D> Class type of plain text
 * @param <K> Class type of pass key
 */
public interface Masher<E extends Serializable, D extends Serializable, K extends Serializable> {
    E encrypt(D plainText, K passKey);

    D decrypt(E cipherText, K passKey);
}
