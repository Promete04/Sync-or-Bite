/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import utils.ClientCryptoUtils;

public class Toggler {
    private boolean paused = false;

    public void togglePause() {
        try {
            // Step 1: Fetch server public key
            PublicKey serverKey = ClientCryptoUtils.fetchServerPublicKey();

            // Step 2: Generate new AES key for session
            SecretKey aesKey = ClientCryptoUtils.generateAESKey();

            // Step 3: Connect to server
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            // Step 4: Send encrypted AES key
            output.writeUTF(ClientCryptoUtils.encryptRSA(aesKey.getEncoded(), serverKey));

            // Step 5: Encrypt and send pause/resume command
            String command = paused ? "resume" : "pause";
            output.writeUTF(ClientCryptoUtils.encryptAES(command, aesKey));

            // Step 6: Decrypt server's response
            String response = ClientCryptoUtils.decryptAES(input.readUTF(), aesKey);
            System.out.println("Server response: " + response);

            paused = !paused;
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPaused() {
        return paused;
    }
}

