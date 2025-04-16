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
    private final Lock lock = new ReentrantLock();
    private final Condition stop = lock.newCondition();

    public void check() 
    {
        lock.lock();
        try 
        {
            while(paused) 
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

    public void pause() 
    {
        lock.lock();
        try 
        {
            paused = true;
        } 
        finally 
        {
            lock.unlock();
        }
    }

    public void resume() 
    {
        lock.lock();
        try 
        {
            paused = false;
            stop.signalAll();
        } 
        finally 
        {
            lock.unlock();
        }
    }
}

