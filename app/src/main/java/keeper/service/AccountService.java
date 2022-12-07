package keeper.service;

import keeper.entity.Account;
import keeper.storage.repository.sqlite.AccountRepository;

import java.nio.charset.StandardCharsets;

public class AccountService {
    private final AccountRepository accountRepository;
    private final EncryptionService<byte[], byte[], byte[]> encryptionService;

    private static void validatePassKeyLength(int passKeyLength) {
        if (passKeyLength < 8 || passKeyLength > 32) {
            throw new IllegalArgumentException("PassKey should be between 8 and 32 bytes");
        }
    }

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.encryptionService = new TwoFishEncryptionService<>();
    }

    public Account createAccount(String customerUUID, String utf8PassKey) {
        byte[] passKeyBytes = utf8PassKey.getBytes(StandardCharsets.UTF_8);
        validatePassKeyLength(passKeyBytes.length);
        byte[] selfEncryptedPassKey = encryptionService.encrypt(passKeyBytes, passKeyBytes);
        return accountRepository.createAccount(customerUUID, selfEncryptedPassKey);
    }

    public Account findAccount(String customerUUID) {
        return accountRepository.findAccount(customerUUID);
    }

    public boolean updateAccount(String customerUUID, String newUtf8PassKey) {
        byte[] newPassKeyBytes = newUtf8PassKey.getBytes(StandardCharsets.UTF_8);
        validatePassKeyLength(newPassKeyBytes.length);
        byte[] selfEncryptedNewPassKey = encryptionService.encrypt(newPassKeyBytes, newPassKeyBytes);
        return accountRepository.updateAccount(customerUUID, selfEncryptedNewPassKey);
    }

    public boolean deleteAccount(String customerUUID) {
        return accountRepository.deleteAccount(customerUUID);
    }
}
