/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lopex
 */
public class UnsafeArea 
{
    private List<Zombie> zombiesInside = new ArrayList<>();  
    private List<Human> humansInside = new ArrayList<>();  
    private Map<Human,Zombie> attacks = new HashMap<>();
    private RiskZone riskZone;
    private int area;
    private FoodGenerator fgenerator;
    private Logger logger;
    
    public UnsafeArea(int area, Logger logger, FoodGenerator fgenerator, RiskZone riskZone)
    {
        this.riskZone = riskZone;
        this.fgenerator = fgenerator;
        this.area = area;
        this.logger = logger;
    }
    
    public void wander(Zombie z) throws InterruptedException
    {
        Human attackedHuman = null;
        synchronized(humansInside)
        {
            if(!humansInside.isEmpty())
            {
                int attackedHumanIndex = (int) (Math.random()*humansInside.size());
                attackedHuman = humansInside.get(attackedHumanIndex);
            }
        }
        
        if(attackedHuman != null)
        {
            logger.log("Zombie " + z.getZombieId() + " in unsafe area " + area + " attacks human " + attackedHuman.getHumanId());
            synchronized(attacks)
            {
                attacks.put(attackedHuman, z);
            }
            
            synchronized(humansInside)
            {
                humansInside.remove(attackedHuman);       // So it can't be attacked by other zombie.
            }
            
            attackedHuman.interrupt();                    // Attack starts
            Thread.sleep(500 + (int) (Math.random()*1000));
            attackedHuman.interrupt();                    // Attack ends
        }
        
        Thread.sleep(2000 + (int) (Math.random()*1000));
    }
    
    public void enter(Zombie z) throws InterruptedException
    {
        synchronized(zombiesInside)
        {
            zombiesInside.add(z);
            logger.log("Zombie " + z.getZombieId() + " entered unsafe area " + area + ".");
        }
    }
    
    public void exit(Zombie z) throws InterruptedException
    {
        synchronized (zombiesInside) 
        {
            zombiesInside.remove(z);
            logger.log("Zombie " + z.getZombieId() + " left unsafe area " + area + ".");
        }
    }
    
    public void enter(Human h) throws InterruptedException
    {
        synchronized (humansInside)
        {
            humansInside.add(h);
            logger.log("Human " + h.getHumanId() + " entered unsafe area " + area + ".");
        }
    }
    
    public void exit(Human h) throws InterruptedException
    {
        synchronized (humansInside) 
        {
            humansInside.remove(h);
            logger.log("Human " + h.getHumanId() + " left unsafe area " + area + ".");
        }
    }
    
    public void wander(Human h, PauseManager pm)
    {
        try
        {
            logger.log("Human " + h.getHumanId() + " is exploring unsafe area " + area + ".");
            Thread.sleep(3000 + (int) (Math.random() * 2000));
            h.collectFood(fgenerator.gatherFood());
            h.collectFood(fgenerator.gatherFood());
            logger.log("Human " + h.getHumanId() + " gathered 2 units of food in unsafe area " + area + ".");
        }
        catch(InterruptedException ie)
        {
            try
            {
                Thread.sleep(10000);       //Under attack (time governed by the zombie, when the zombie ends it interrupts again)
            }
            catch(InterruptedException ie2)
            {
                int defense = (int) (Math.random() * 3);
                if (defense < 2) 
                {
                    logger.log("Human " + h.getHumanId() + " successfully defended itself from the attack.");
                    h.toggleMarked();
                    synchronized (attacks) 
                    { 
                        attacks.remove(h);
                    }
                    
                    synchronized (humansInside)
                    {
                        humansInside.add(h);             //Add the human back 
                    }
                } 
                else 
                {
                    logger.log("Human " + h.getHumanId() + " failed to defend itself from the attack.");
                    String zombieId = h.getHumanId().replaceFirst("H", "Z");
                    Zombie killer;
                    
                    synchronized(attacks)
                    {  
                        killer = attacks.get(h);
                        attacks.remove(h);
                    }
                    
                    killer.increaseKillCount();
                    riskZone.getTkm().reportKill(killer);
                    logger.log("Zombie " + killer.getZombieId() + " killed human " + h.getHumanId() + " (Kill count: " + killer.getKillCount() + ")");
                    
                    Zombie killed = new Zombie(zombieId,this,logger,pm);
 
                    synchronized(zombiesInside)
                    {
                        zombiesInside.add(killed);
                    }
                    logger.log("Human " + h.getHumanId() + " was reborn as " + "Zombie " + killed.getZombieId() + " in area " + area + ".");
                    killed.start();
                    
                    h.interrupt();
                }
            }
        }
    }

    public RiskZone getRiskZone() 
    {
        return riskZone;
    }
    
    public int getArea()
    {
        return area;
    }
    
    
    public int getHumansInside()
    {
        int count;
        synchronized(humansInside)
        {
            count = humansInside.size();
        }
        return count;
    }
    
    public int getZombiesInside()
    {
        int count;
        synchronized(zombiesInside)
        {
            count = zombiesInside.size();
        }
        return count;
    }
}
