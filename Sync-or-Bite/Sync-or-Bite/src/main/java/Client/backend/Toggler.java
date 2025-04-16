/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;

/**
 *
 * @author Lopex
 */
public class Toggler 
{
    private boolean paused = false;
    
    public Toggler()
    {
    }
    
    public void togglePause() 
    {
        
        try 
        {
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            if(paused) 
            {
                output.writeUTF("resume");
            } 
            else 
            {
                output.writeUTF("pause");
            }
            
            paused = !paused;
            socket.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public boolean isPaused() 
    {
        return paused;
    }
    
    
}
