/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author guill
 */
public class DiningRoom 
{
    private final List<Human> diningList = new ArrayList<>();
    private final Queue<Food> foodList = new ConcurrentLinkedQueue<>();
    private final Semaphore mutex = new Semaphore(1,true);
    private final Semaphore foodCount = new Semaphore(0,true);
    
    public DiningRoom()
    {
    }
    
    public synchronized void storeFood(Food f) throws InterruptedException  
    {
        foodList.offer(f);
        foodCount.release();
    }
    
    //Used semaphores to ensure fairness
    public  void eatFood(Human h) throws InterruptedException  
    {                                    
        foodCount.acquire();
        foodList.poll();
        Thread.sleep(3000+(int) Math.random()*2000);
        
    }
    
    public void enter(Human h) throws InterruptedException
    {
        try
        {
            mutex.acquire();
            diningList.add(h);
        }
        finally
        {
            mutex.release();
        }
        
    }
    
    public void exit(Human h) throws InterruptedException
    {
        try 
        {
            mutex.acquire();
            diningList.remove(h);
        } 
        finally 
        {
            mutex.release();
        }
    }
}
