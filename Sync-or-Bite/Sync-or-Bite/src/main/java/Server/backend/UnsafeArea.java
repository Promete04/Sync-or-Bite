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
 * @author Lopex
 */
public class UnsafeArea 
{
    private final List<Zombie> zombiesInside = new ArrayList<Zombie>();  
    private final List<Human> humansInside = new ArrayList<Human>();  
    private Semaphore mutex = new Semaphore(1,true);
    private int area;
    
    public UnsafeArea(int area)
    {
        this.area = area;
    }
    
    public void wander(Zombie z) throws InterruptedException
    {
        try
        {
            mutex.acquire();
            zombiesInside.add(z); 
        }
        finally
        {
            mutex.release();
        }
        
        synchronized(humansInside)
        {
            if(!humansInside.isEmpty())
            {
                int atackedHuman = (int) (Math.random()*humansInside.size());
            }
        }
        
        Thread.sleep(2000 + (int) (Math.random()*1000));
        
        try
        {
            mutex.acquire();
            zombiesInside.remove(z); 
        }
        finally
        {
            mutex.release();
        }
        
    }
    
    public void wander(Human h)
    {
        
    }
}
