/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.io.*;
import java.net.*;
import javax.crypto.SecretKey;
import utils.ServerCryptoUtils;

public class ServerData extends Thread
{
    private PauseManager pm;
    private Refuge r;
    private Tunnels t;
    private RiskZone rk;
    private ServerCryptoUtils crypto;

    public ServerData(PauseManager pm, Tunnels t, Refuge r, RiskZone rk) 
    {
        this.pm = pm;
        this.t = t;
        this.r = r;
        this.rk = rk;
        try 
        {
            this.crypto = new ServerCryptoUtils(); // Create RSA key pair once
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Return the data snapshot to the client
    private String getData()
    {
        return r.getCount().get()
                + "|" + t.obtainTunnel(0).getInTunnel() + "|" + t.obtainTunnel(1).getInTunnel() + "|" + t.obtainTunnel(2).getInTunnel() + "|" + t.obtainTunnel(3).getInTunnel()
                + "|" + rk.accessUnsafeArea(0).getHumansInside() + "|" + rk.accessUnsafeArea(1).getHumansInside() + "|" + rk.accessUnsafeArea(2).getHumansInside() + "|" + rk.accessUnsafeArea(3).getHumansInside()
                + "|" + rk.accessUnsafeArea(0).getZombiesInside() + "|" + rk.accessUnsafeArea(1).getZombiesInside() + "|" + rk.accessUnsafeArea(2).getZombiesInside() + "|" + rk.accessUnsafeArea(3).getZombiesInside()
                + "|" + rk.obtainTopKillers();
    }

    public void run() 
    {
        try 
        {
            ServerSocket server = new ServerSocket(1);
            while (true)
            {
                Socket connection = server.accept();
                DataInputStream input = new DataInputStream(connection.getInputStream());
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());

                String request = input.readUTF();

                // Step 1: Send public key to client
                if (request.equals("public_key")) 
                {
                    output.writeUTF(crypto.getEncodedPublicKey());
                } 
                else 
                {
                    // Step 2: Decrypt AES key from client
                    SecretKey aesKey = crypto.decryptAESKey(request);

                    // Step 3: Decrypt command from client using AES
                    String encryptedCommand = input.readUTF();
                    String command = crypto.decryptAESMessage(encryptedCommand, aesKey);

                    // Step 4: Handle command and prepare response
                    String response;
                    if (command.equals("get")) 
                    {
                        response = getData();
                    }
                    else if (command.equals("pause")) 
                    {
                        pm.pause();
                        response = "Paused";
                    }
                    else 
                    {
                        pm.resume();
                        response = "Resumed";
                    }

                    // Step 5: Encrypt response with AES and send to client
                    output.writeUTF(crypto.encryptAESMessage(response, aesKey));
                }

                connection.close();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}