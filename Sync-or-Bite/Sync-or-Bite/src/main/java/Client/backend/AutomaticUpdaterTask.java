/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import javax.swing.JOptionPane;

/**
 * The AutomaticUpdaterTask class defines a task to fetch data from the server. 
 * It sends a "get" command and processes the received response, splitting it into an array.
 * 
 * This class is used by a executor to periodically retrieve 
 * updates from the server.
 */
public class AutomaticUpdaterTask implements Callable<String[]>
{
    /**
     * Executes the task of connecting to the server, sending a "get" command, 
     * and receiving the response data.
     *
     * @return An array of strings containing the data returned from the server,
     * or null if an exception occurs
     */
    public String[] call()
    {
        String[] individualData = null;
        try 
        {
            // Stablish connection with socket on port 1
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            // Create the input and output streams used for the connection
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            
            // Send request
            output.writeUTF("get");
            // Receive response
            String data = input.readUTF();
            individualData = data.split("\\|");

            // Close connection after receiving the response
            socket.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Connection lost.","Connection Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        return individualData;
        
    }
}