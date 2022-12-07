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

    public Account findAccount(String customerUUID, String utf8PassKey) {
        byte[] passKeyBytes = utf8PassKey.getBytes(StandardCharsets.UTF_8);
        validatePassKeyLength(passKeyBytes.length);
        byte[] selfEncryptedPassKey = encryptionService.encrypt(passKeyBytes, passKeyBytes);
        return accountRepository.findAccount(customerUUID, selfEncryptedPassKey);
    }

    public boolean updateAccount(String customerUUID, String oldUtf8PassKey, String newUtf8PassKey) {
        byte[] oldPassKeyBytes = oldUtf8PassKey.getBytes(StandardCharsets.UTF_8);
        byte[] newPassKeyBytes = newUtf8PassKey.getBytes(StandardCharsets.UTF_8);
        validatePassKeyLength(oldPassKeyBytes.length);
        validatePassKeyLength(newPassKeyBytes.length);
        byte[] selfEncryptedOldPassKey = encryptionService.encrypt(oldPassKeyBytes, oldPassKeyBytes);
        byte[] selfEncryptedNewPassKey = encryptionService.encrypt(newPassKeyBytes, newPassKeyBytes);
        return accountRepository.updateAccount(customerUUID, selfEncryptedOldPassKey, selfEncryptedNewPassKey);
    }

    public boolean deleteAccount(String customerUUID, String utf8PassKey) {
        byte[] passKeyBytes = utf8PassKey.getBytes(StandardCharsets.UTF_8);
        validatePassKeyLength(passKeyBytes.length);
        byte[] selfEncryptedPassKey = encryptionService.encrypt(passKeyBytes, passKeyBytes);
        return accountRepository.deleteAccount(customerUUID, selfEncryptedPassKey);
    }
}
