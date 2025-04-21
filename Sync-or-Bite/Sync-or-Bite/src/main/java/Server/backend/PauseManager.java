/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */

public class PauseManager 
{
    private boolean paused = false;
    
    private Runnable pauseStateListener;

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
        pauseStateListener.run();
        if(!paused)
        {
            notifyAll();
        }

    }

 
    public void setPauseStateListener(Runnable listener) 
    {
        this.pauseStateListener = listener;
    }
    
    public boolean isPaused()
    {
        return paused;
    }
}

