/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lopex
 */

public class PauseManager 
{
    private boolean paused = false;
    
    private List<Runnable> pauseStateListener = new ArrayList<>();


    public synchronized void check() 
    {
        try 
        {
            while (paused) 
            {
                wait();
            }
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
    }
    
    public synchronized void underAttackCheck() throws InterruptedException
    {

        while (paused) 
        {
            wait();
        }

    }

    public synchronized void togglePause() 
    {

        paused = !paused;
        
        for(Runnable runnable: pauseStateListener)
        {
            runnable.run();
        }
        
        if(!paused)
        {
            notifyAll();
        }

    }

 
    public void setPauseStateListener(Runnable listener) 
    {
        pauseStateListener.add(listener);
    }
    
    public boolean isPaused()
    {
        return paused;
    }
}

