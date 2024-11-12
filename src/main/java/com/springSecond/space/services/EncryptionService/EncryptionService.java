package com.springSecond.space.services.EncryptionService;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Getter
@Service
public class EncryptionService {

    private final SecretKey secretKey;

    // Constructor to initialize the key. The key should be securely stored or fetched.
    public EncryptionService() throws Exception {
        String key = "shaileshattri83";  // Ensure this key is 16 characters for AES-128
        this.secretKey = new SecretKeySpec(padKey(key), "AES");
    }

    // Encrypt the given string
    public String encrypt(String strToEncrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  // Use AES with PKCS5 padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
    }

    // Decrypt the given string
    public String decrypt(String strToDecrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  // Use AES with PKCS5 padding
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    // Ensure the key is exactly 16 bytes for AES-128, pad or truncate as needed
    private byte[] padKey(String key) {
        if (key.length() < 16) {
            // Pad with spaces if the key is too short
            while (key.length() < 16) {
                key += " ";
            }
        } else if (key.length() > 16) {
            // Truncate to 16 characters if the key is too long
            key = key.substring(0, 16);
        }
        return key.getBytes();
    }
}
