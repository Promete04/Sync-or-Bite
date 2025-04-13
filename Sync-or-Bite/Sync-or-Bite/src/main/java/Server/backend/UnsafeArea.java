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
    private List<Zombie> zombiesInside = new ArrayList<Zombie>();  
    private List<Human> humansInside = new ArrayList<Human>();  
    private int area;
    private FoodGenerator fgenerator;
    private Logger logger;
    
    public UnsafeArea(int area, Logger logger, FoodGenerator fgenerator)
    {
        this.fgenerator = fgenerator;
        this.area = area;
        this.logger = logger;
    }
    
    public void wander(Zombie z) throws InterruptedException
    {
        synchronized(humansInside)
        {
            if(!humansInside.isEmpty())
            {
                int attackedHuman = (int) (Math.random()*humansInside.size());
                logger.log("Zombie " + z.getZombieId() + " in unsafe area " + area + " attacks human " + humansInside.get(attackedHuman).getHumanId());
                humansInside.get(attackedHuman).looseAll();
            }
        }
        
        
        Thread.sleep(2000 + (int) (Math.random()*1000));
    }
    
    public void enter(Zombie z) throws InterruptedException
    {
        synchronized(zombiesInside)
        {
            zombiesInside.add(z);
        }
    }
    
    public void exit(Zombie z) throws InterruptedException
    {
        synchronized (zombiesInside) 
        {
            zombiesInside.remove(z);
        }
    }
    
    public void enter(Human h) throws InterruptedException
    {
        synchronized (humansInside)
        {
            humansInside.add(h);
        }
    }
    
    public void exit(Human h) throws InterruptedException
    {
        synchronized (humansInside) 
        {
            humansInside.remove(h);
        }
    }
    
    public void wander(Human h) throws InterruptedException
    {
        Thread.sleep(3000 + (int) (Math.random()*2000));
        h.collectFood(fgenerator.gatherFood());
        h.collectFood(fgenerator.gatherFood());
    }
    
    public int getArea()
    {
        return area;
    }
}
