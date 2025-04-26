/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the pause/resume functionality.
 * Blocks threads when the simulation is paused, and resumes them when unpaused.
 * Notifies a listener on state change for GUI updates.
 * 
 * Methods are synchronized using the monitor to allow thread safe access.
 */
public class PauseManager 
{
    private boolean paused = false;

    // ArrayList of listeners to allow multiple pauseResume buttons being sync
    private List<Runnable> pauseStateListener = new ArrayList<>(); 

    /**
     * Blocks the calling thread if the system is paused.
     */
    public synchronized void check() 
    {
        try 
        {
            while(paused) 
            {
                wait();  // Wait using the monitor
            }
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
    }
    
    /**
     * Same as check(), but declares InterruptedException explicitly.
     * Meant to be used when the human is under attack, to ensure that the zombie attack (interrupt) works correctly
     *
     * @throws InterruptedException if the waiting thread is interrupted
     */
    public synchronized void underAttackCheck() throws InterruptedException
    {
        while(paused) 
        {
            wait();  // Wait using the monitor
        }
    }
    
    /**
     * Toggles the simulation pause state and notifies waiting threads if resuming.
     * It also triggers the listeners 
     */
    public synchronized void togglePause() 
    {
        paused = !paused;
        
        for(Runnable runnable: pauseStateListener)
        {
            runnable.run();
        }
        if(!paused)
        {
            notifyAll(); // Notify all threads using monitor
        }
    }

    /**
     * Adds a listener to an array list of runnables to be executed.
     *
     * @param listener a Runnable to be executed when pause is toggled
     */
    public void setPauseStateListener(Runnable listener) 
    {
        pauseStateListener.add(listener);
    }
    
    /**
     * Returns whether the system is currently paused.
     *
     * @return true if paused, false otherwise
     */
    public boolean isPaused()
    {
        return paused;
    }
}

