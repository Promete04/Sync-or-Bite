/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.backend;

import java.io.*;
import java.net.*;

/**
 * The Toggler class is responsible for managing the pause/resume state 
 * of the server from the client side. It sends a "togglePause" command to the server, 
 * which updates the server's state accordingly.
 *
 * This class also keeps track of the local pause state and notifies a listener 
 * whenever a change occurs. Used to synchronize the client GUI with the server’s execution state.
 */
public class Toggler 
{
    private Runnable pauseStateListener;
    private boolean paused = false;
    
    /**
     * Default constructor.
     */
    public Toggler()
    {
    }
    
    /**
     * Registers a listener that will be called whenever the pause state changes.
     *
     * @param listener a Runnable to execute pause/resume toggle
     */
    public void setPauseStateListener(Runnable listener) 
    {
        this.pauseStateListener = listener;
    }
    
    /**
     * Sends a "togglePause" command to the server to pause or resume its execution.
     * Locally updates the pause state and notifies the listener.
     *
     */
    public void togglePause() 
    {
        try 
        {
            // Stablish connection with socket on port 1024
            Socket socket = new Socket(InetAddress.getLocalHost(), 1024);
            
            // Create output stream used for the connection
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Send request
            output.writeUTF("togglePause");
            paused = !paused;  // Change local pause state
            
            // Notify the listener that the state changed
            pauseStateListener.run();
            
            // Close output stream
            output.close();
                
            // Close connection after receiving the response
            socket.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the current local pause state.
     *
     * @return true if paused, false otherwise
     */
    public boolean isPaused() 
    {
        return paused;
    }

    /**
     * Sets the local state to paused and notifies the listener, 
     * if the application is not already paused.
     */
    public void pause()
    {
        if(!paused)
        {
            paused=true;
            pauseStateListener.run();
        }
    }
    
    /**
     * Resumes local execution and notifies the listener, 
     * if the application is currently paused.
     */
    public void resume()
    {
        if(paused)
        {
            paused=false;
            pauseStateListener.run();
        }
    }
}
