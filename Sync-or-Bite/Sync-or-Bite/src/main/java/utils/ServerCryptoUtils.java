/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author guill
 */
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class ServerCryptoUtils 
{
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    // Generate RSA key pair at server startup
    public ServerCryptoUtils() throws Exception 
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    // Get the Base64-encoded public RSA key to send to the client
    public String getEncodedPublicKey() 
    {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    // Decrypt AES key received from client using the private RSA key
    public SecretKey decryptAESKey(String encryptedAESKey) throws Exception 
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey));
        return new SecretKeySpec(aesKeyBytes, "AES");
    }

    // Decrypt an AES-encrypted message
    public String decryptAESMessage(String encryptedMessage, SecretKey aesKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decoded));
    }

    // Encrypt a message with AES to send back to client
    public String encryptAESMessage(String message, SecretKey aesKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
