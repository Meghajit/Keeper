/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package keeper;

import keeper.service.TwoFishEncryptionService;
import keeper.template.WebsiteTemplate;

import java.nio.charset.StandardCharsets;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        TwoFishEncryptionService<WebsiteTemplate> enc = new TwoFishEncryptionService<>();
        WebsiteTemplate ws = new WebsiteTemplate("Some".getBytes(StandardCharsets.UTF_8), "Password".getBytes(StandardCharsets.UTF_8), "no metadata".getBytes(StandardCharsets.UTF_8));
        byte[] passkey = ("This is my passkey").getBytes(StandardCharsets.UTF_8);

        // Encrypting the secret
        byte[] cipherText = enc.encrypt(ws, passkey);
        System.out.println("Cipher text is: " + new String(cipherText, StandardCharsets.UTF_8));

        // Decrypting the secret
        WebsiteTemplate decryptedSecret = enc.decrypt(cipherText, passkey);
        System.out.println("Plain text is: " + decryptedSecret.print());
    }
}
