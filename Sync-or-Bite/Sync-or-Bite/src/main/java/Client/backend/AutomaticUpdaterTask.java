/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

/**
 *
 * @author Lopex
 */
public class AutomaticUpdaterTask implements Callable<String[]>
{
    public String[] call()
    {
        String[] individualData = null;
        try 
        {
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            output.writeUTF("get");
            String data = input.readUTF();
            individualData = data.split("|");

            socket.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        return individualData;
        
    }
}