/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import utils.ClientCryptoUtils;

public class AutomaticUpdaterTask implements Callable<String[]>
{
    @Override
    public String[] call() 
    {
        String[] individualData = null;
        try 
        {
            // Step 1: Fetch RSA public key from server
            PublicKey serverKey = ClientCryptoUtils.fetchServerPublicKey();

            // Step 2: Generate AES session key
            SecretKey aesKey = ClientCryptoUtils.generateAESKey();

            // Step 3: Open socket to server
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            // Step 4: Send AES key encrypted with RSA
            output.writeUTF(ClientCryptoUtils.encryptRSA(aesKey.getEncoded(), serverKey));

            // Step 5: Encrypt command and send with AES
            output.writeUTF(ClientCryptoUtils.encryptAES("get", aesKey));

            // Step 6: Receive and decrypt response with AES
            String response = ClientCryptoUtils.decryptAES(input.readUTF(), aesKey);
            individualData = response.split("\\|");

            socket.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return individualData;
    }
}
