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
public class UnsafeArea 
{
    private final List<Zombie> zombiesInside = new ArrayList<Zombie>();  
    private final List<Zombie> humansInside = new ArrayList<Human>();  
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
            
        }
        finally
        {
            
        }
    }
    
    public void wander(Human h)
    {
        
    }
}
