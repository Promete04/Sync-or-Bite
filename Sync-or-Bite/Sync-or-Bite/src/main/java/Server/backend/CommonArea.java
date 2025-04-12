/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author guill
 */
public class CommonArea 
{
    private List<Human> commonList = new ArrayList<Human>();
    Semaphore mutex = new Semaphore(1,true);
    
    
    public void CommonArea ()
    {
    }
    
    public void wander(Human h) throws InterruptedException
    {
        mutex.acquire();
        commonList.add(h);
        mutex.release();
        
        Thread.sleep((int) (1000 + Math.random() * 1000));
        
        mutex.acquire();
        commonList.remove(h);
        mutex.release();
    }
    
    // Methods for monitoring
    public synchronized int getCommon() throws InterruptedException
    {
        mutex.acquire();
        try 
        {
            return commonList.size();
        } 
        finally 
        {
           mutex.release();
        }
    }
    public synchronized List<String> getCommonIds() throws InterruptedException 
    {
        List<String> ids = new ArrayList<>();
        
        mutex.acquire();
       
        for (Human h : commonList) 
        {
            ids.add(h.getHumanId());
        }
        
         mutex.release();
         return ids;
    }
    
}
