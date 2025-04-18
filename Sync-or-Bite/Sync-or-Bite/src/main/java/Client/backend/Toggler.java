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
    private Runnable pauseStateListener;
    
    private boolean paused = false;
    
    public Toggler()
    {
    }
    
     public void setPauseStateListener(Runnable listener) 
    {
    this.pauseStateListener = listener;
    }
    
    public void togglePause() 
    {
        
        try 
        {
            Socket socket = new Socket(InetAddress.getLocalHost(), 1);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            output.writeUTF("togglePause");

            paused = !paused;
            pauseStateListener.run();
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

    //methods to allow synchronization
    public void pause()
    {
        if(!paused)
        {
        paused=true;
        pauseStateListener.run();
        }
    }
    public void resume()
    {
        if(paused)
        {
        paused=false;
        pauseStateListener.run();
        }
    }
    
}
