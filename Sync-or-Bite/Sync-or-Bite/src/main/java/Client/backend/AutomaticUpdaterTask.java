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
 * This class is used by a SingleThreadExecutor to periodically retrieve updates via Future from the server.
 */
public class AutomaticUpdaterTask implements Callable<String[]>
{
    /**
     * Executes the task of connecting to the server, sending a "get" command, 
     * and receiving the response data.
     *
     * @return an array of strings containing the data returned from the server,
     * or null if an exception occurs
     * 
     * If an IOException is thrown, the method shows an error dialog and exits the program.
     */
    public String[] call()
    {
        String[] individualData = null;
        try 
        {
            // Stablish connection with socket on port 1024
            Socket socket = new Socket(InetAddress.getLocalHost(), 1024);
            
            // Create the input and output streams used for the connection
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            
            // Send request
            output.writeUTF("get");
            
            // Receive response
            String data = input.readUTF();
            individualData = data.split("\\|");  // The \\ is for using | as a character not with the logic meaning
            
            // Close input and output streams
            output.close();
            input.close();
                
            // Close connection after receiving the response
            socket.close();
        } 
        catch (IOException e) 
        {
            // Show error dialog to the user and terminate the application
            JOptionPane.showMessageDialog(null,"Connection lost.","Connection Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return individualData;
    }
}