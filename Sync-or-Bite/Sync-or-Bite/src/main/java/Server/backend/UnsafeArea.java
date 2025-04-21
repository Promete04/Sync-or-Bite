/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.ServerApp;
import Server.frontend.MapPage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Lopex
 */
public class UnsafeArea 
{
    private List<Zombie> zombiesInside = new ArrayList<>();  
    private List<Human> possibleTargets = new ArrayList<>();  
    private AtomicInteger humansInside = new AtomicInteger(0);
    private Map<Human,Zombie> attacks = new HashMap<>();
    private RiskZone riskZone;
    private int area;
    private FoodGenerator fgenerator;
    private Logger logger;
    private MapPage mapPage = ServerApp.getMapPage();
    
    public UnsafeArea(int area, Logger logger, FoodGenerator fgenerator, RiskZone riskZone)
    {
        this.riskZone = riskZone;
        this.fgenerator = fgenerator;
        this.area = area;
        this.logger = logger;
    }
    
    public void wander(Zombie z, PauseManager pm) throws InterruptedException
    {
        pm.check();
        Human attackedHuman = null;
        synchronized(possibleTargets)
        {
            if(!possibleTargets.isEmpty())
            {
                int attackedHumanIndex = (int) (Math.random()*possibleTargets.size());
                attackedHuman = possibleTargets.get(attackedHumanIndex);
                possibleTargets.remove(attackedHuman);       // So it can't be attacked by other zombie.
            }
        }
        pm.check();
        
        if(attackedHuman != null)
        {
            logger.log("Zombie " + z.getZombieId() + " in unsafe area " + area + " attacks human " + attackedHuman.getHumanId());
            mapPage.setLabelColorInPanel("RZ"+String.valueOf(area+1),z.getZombieId(), utils.ColorManager.ATACKING_COLOR);
            
            synchronized(attacks)
            {
                attacks.put(attackedHuman, z);
            }

            attackedHuman.interrupt();                    // Attack starts

//            Thread.sleep(500 + (int) (Math.random()*1000));
            Thread.sleep(250 + (int) (Math.random()*500));
            pm.check();
            Thread.sleep(125 + (int) (Math.random()*250));
            pm.check();
            Thread.sleep(125 + (int) (Math.random()*250));
            pm.check();
            
            attackedHuman.interrupt();                    // Attack ends

            mapPage.setLabelColorInPanel("RZ"+String.valueOf(area+1),z.getZombieId(), utils.ColorManager.ZOMBIE_COLOR);
        }
        
        pm.check();
//        Thread.sleep(2000 + (int) (Math.random()*1000));
        Thread.sleep(500 + (int) (Math.random()*250));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*250));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*250));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*125));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*125));
        pm.check();
    }
    
    public void enter(Zombie z, PauseManager pm) throws InterruptedException
    {
        pm.check();
        synchronized(zombiesInside)
        {
            zombiesInside.add(z);
            logger.log("Zombie " + z.getZombieId() + " entered unsafe area " + area + ".");
            mapPage.setCounter("Z"+String.valueOf(area+1),String.valueOf(zombiesInside.size()));
            mapPage.addLabelToPanel("RZ"+String.valueOf(area+1), z.getZombieId());
            mapPage.setLabelColorInPanel("RZ"+String.valueOf(area+1), z.getZombieId(), utils.ColorManager.ZOMBIE_COLOR);
        }
        pm.check();
    }
    
    public void exit(Zombie z, PauseManager pm) throws InterruptedException
    {
        pm.check();
        synchronized(zombiesInside) 
        {
            zombiesInside.remove(z);
            logger.log("Zombie " + z.getZombieId() + " left unsafe area " + area + ".");
            mapPage.setCounter("Z"+String.valueOf(area+1),String.valueOf(zombiesInside.size()));
            mapPage.removeLabelFromPanel("RZ"+String.valueOf(area+1), z.getZombieId());
        }
        pm.check();
    }
    
    public void enter(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        synchronized (possibleTargets)
        {
            possibleTargets.add(h);
            logger.log("Human " + h.getHumanId() + " entered unsafe area " + area + ".");
            mapPage.setCounter("H"+String.valueOf(area+1),String.valueOf(humansInside.incrementAndGet()));
            mapPage.addLabelToPanel("RH"+String.valueOf(area+1), h.getHumanId());
        }
        pm.check();
    }
    
    public void exit(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        synchronized (possibleTargets) 
        {
            possibleTargets.remove(h);
            logger.log("Human " + h.getHumanId() + " left unsafe area " + area + ".");
            mapPage.setCounter("H"+String.valueOf(area+1),String.valueOf(humansInside.decrementAndGet()));
            mapPage.removeLabelFromPanel("RH"+String.valueOf(area+1), h.getHumanId());
        }
        pm.check();
    }
    
    public void wander(Human h, PauseManager pm)
    {
        pm.check();
        try
        {
            logger.log("Human " + h.getHumanId() + " is exploring unsafe area " + area + ".");
//        Thread.sleep(3000 + (int) (Math.random()*2000));

            Thread.sleep(500 + (int) (Math.random() * 333));
            pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333));
            pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333));
            pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333));
            pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 334));
            pm.underAttackCheck();
            Thread.sleep(250 + (int) (Math.random() * 167));
            pm.underAttackCheck();
            Thread.sleep(250 + (int) (Math.random() * 167));
            pm.underAttackCheck();
            
            h.collectFood(fgenerator.gatherFood());
            h.collectFood(fgenerator.gatherFood());
            logger.log("Human " + h.getHumanId() + " gathered 2 units of food in unsafe area " + area + ".");
        }
        catch(InterruptedException ie)
        {
            try
            {
                pm.underAttackCheck();
                Thread.sleep(10000);       // Under attack (time governed by the zombie, when the zombie ends it interrupts again)
            }
            catch(InterruptedException ie2)
            {
                pm.check();
                mapPage.setLabelColorInPanel("RH"+String.valueOf(area+1),h.getHumanId(),utils.ColorManager.ATACKED_COLOR);
                int defense = (int) (Math.random() * 3);
                pm.check();
                if (defense < 2) 
                {
                    logger.log("Human " + h.getHumanId() + " successfully defended itself from the attack.");
                    mapPage.setLabelColorInPanel("RH"+String.valueOf(area+1),h.getHumanId(),utils.ColorManager.INJURED_COLOR);
                    h.toggleMarked();
                    pm.check();
                    synchronized (attacks) 
                    { 
                        attacks.remove(h);
                    }
                    pm.check();
                } 
                else 
                {
                    logger.log("Human " + h.getHumanId() + " failed to defend itself from the attack.");
                    mapPage.removeLabelFromPanel("RH"+String.valueOf(area+1), h.getHumanId()); 
                    mapPage.setCounter("H"+String.valueOf(area+1),String.valueOf(humansInside.decrementAndGet()));
                    String zombieId = h.getHumanId().replaceFirst("H", "Z");
                    Zombie killer;
                    
                    pm.check();
                    synchronized(attacks)
                    {  
                        killer = attacks.get(h);
                        attacks.remove(h);
                    }
                    pm.check(); 
                    killer.increaseKillCount();
                    logger.log("Zombie " + killer.getZombieId() + " killed human " + h.getHumanId() + " (Kill count: " + killer.getKillCount() + ")");
                    riskZone.reportKill(killer);
                    pm.check();
                    
                    Zombie killed = new Zombie(zombieId,this,logger,pm);
                    pm.check();
                    synchronized(zombiesInside)
                    {
                        zombiesInside.add(killed);
                        mapPage.setCounter("Z"+String.valueOf(area+1),String.valueOf(zombiesInside.size()));
                        mapPage.addLabelToPanel("RZ"+String.valueOf(area+1), killed.getZombieId());
                        
                    }
                    pm.check();
                    logger.log("Human " + h.getHumanId() + " was reborn as " + "Zombie " + killed.getZombieId() + " in area " + area + ".");
                    killed.start();
                    pm.check();
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
    
    
    public AtomicInteger getHumansInside()
    {
        return humansInside;
    }
    
    public int getZombiesInside()
    {
        int count;
        synchronized(zombiesInside)
        {
            count = zombiesInside.size();
            return count;
        }
    }
}
