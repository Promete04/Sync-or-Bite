/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PauseManager 
{
    private boolean paused = false;
    
    private Runnable pauseStateListener;
    
    private final Lock lock = new ReentrantLock();
    private final Condition stop = lock.newCondition();

    public void check() 
    {
        lock.lock();
        try 
        {
            while (paused) 
            {
                stop.await();
            }
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            lock.unlock();
        }
    }
    
    public void underAttackCheck() throws InterruptedException
    {
        lock.lock();
        try 
        {
            while (paused) 
            {
                stop.await();
            }
        
        } 
        finally 
        {
            lock.unlock();
        }
    }

    public void togglePause() 
    {
        lock.lock();
        try 
        {
            paused = !paused;
            pauseStateListener.run();
            if(!paused)
            {
                stop.signalAll();
            }
        } 
        finally 
        {
            lock.unlock();
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

