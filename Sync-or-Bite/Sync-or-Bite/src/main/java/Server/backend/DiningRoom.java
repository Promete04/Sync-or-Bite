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
    
    public synchronized void storeFood(Food f)
    {
        foodList.offer(f);
        foodCount.release();
    }
    
    
    public  void eatFood(Human h) throws InterruptedException  
    {                                    //Used semaphores to ensure fairness
        mutex.acquire();       //Collections.synchronizedList(new ArrayList<>())
        diningList.add(h);     //could be used, but do NOT ensure fairness
        mutex.release();
        
        foodCount.acquire();
        foodList.poll();
        Thread.sleep(3000+(int) Math.random()*2000);
        
        mutex.acquire();
        diningList.remove(h);
        mutex.release();
        
    }
}
