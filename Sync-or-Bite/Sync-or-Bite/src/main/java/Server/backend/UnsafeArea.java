/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an unsafe area within a RiskZone where zombies and humans interact.
 */
public class UnsafeArea 
{
    // List of zombies currently inside this unsafe area
    private List<Zombie> zombiesInside = new ArrayList<>();  
    // List of humans that can be targeted by zombies
    private List<Human> possibleTargets = new ArrayList<>();  
    // Thread safe counter for the number of humans inside
    private AtomicInteger humansInsideCount = new AtomicInteger(0);
    private List<Human> humansInside = new ArrayList<>();  
    // Records attacked humans and the zombies attacking them
    private Map<Human,Zombie> attacks = new HashMap<>();
    private RiskZone riskZone;
    private int area;
    // Food generator for humans wandering inside
    private FoodGenerator fgenerator;
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    /**
     * Constructor for UnsafeArea.
     */
    public UnsafeArea(int area, Logger logger, FoodGenerator fgenerator, RiskZone riskZone, PauseManager pm)
    {
        this.pm = pm;
        this.riskZone = riskZone;
        this.fgenerator = fgenerator;
        this.area = area;
        this.logger = logger;
    }
    
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
    public void removeChangeListener(ChangeListener l) 
    {
        listeners.remove(l);
    }
    
    private void notifyChange() 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this);
        }
    }
    
    /**
     * Called by a zombie to perform its wandering behavior. If a human
     * is inside the area, it attacks that human.
     * @param z the Zombie that is wandering.
     * @throws InterruptedException if the thread is interrupted
     */
    public void wander(Zombie z) throws InterruptedException
    {
        pm.check();                     // Check if system is paused 
        Human attackedHuman = null;
        // Attempt to pick a human target
        synchronized(possibleTargets)   // Use the monitor of the object to ensure synchronization
        {
            if(!possibleTargets.isEmpty())
            {
                
                int attackedHumanIndex = (int) (Math.random()*possibleTargets.size());
                attackedHuman = possibleTargets.get(attackedHumanIndex);
                possibleTargets.remove(attackedHuman);       // Remove so it can't be attacked by other zombies
            }
        }
        pm.check();                    // Check if system is paused 
        
        // If the zombie successfully picked a human to attack
        if(attackedHuman != null)
        {
            z.toggleAttacking();
            logger.log("Zombie " + z.getZombieId() + " in unsafe area " + area + " attacks human " + attackedHuman.getHumanId());
            notifyChange();
            
            // Record the target and itself in the attacks hashmap (using it's monitor).
            synchronized(attacks)
            {
                attacks.put(attackedHuman, z);
            }
            
            // Start of attack simulated via interrupt
            attackedHuman.interrupt();                    
            
            // Simulate attack time with periodic pause checks
//            Thread.sleep(500 + (int) (Math.random()*1000));
            Thread.sleep(250 + (int) (Math.random()*500));
            pm.check();
            Thread.sleep(125 + (int) (Math.random()*250));
            pm.check();
            Thread.sleep(125 + (int) (Math.random()*250));
            
            attackedHuman.interrupt();      // End of attack using another interrupt
            z.toggleAttacking();

            notifyChange();
        }
        
        // Simulate wandering time with periodic pause checks
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
    
    /**
     * Registers a zombie entering the unsafe area.
     *
     * @param z the zombie entering
     * @throws InterruptedException if interrupted
     */
    public void enter(Zombie z) throws InterruptedException
    {
        pm.check();
        synchronized(zombiesInside)  // Uses the list's monitor for synchronization
        {
            zombiesInside.add(z);
            logger.log("Zombie " + z.getZombieId() + " entered unsafe area " + area + ".");
            // Update GUI
            notifyChange();
        }
        pm.check();
    }
    
    /**
     * Registers a zombie leaving the unsafe area.
     * 
     * @param z the zombie leaving
     * @throws InterruptedException if interrupted
     */
    public void exit(Zombie z) throws InterruptedException
    {
        pm.check();
        synchronized(zombiesInside) // Uses the list's monitor for synchronization
        {
            zombiesInside.remove(z);
            logger.log("Zombie " + z.getZombieId() + " left unsafe area " + area + ".");
            // Update GUI
            notifyChange();
        }
        pm.check();
    }
    
    /**
     * Registers a human entering the unsafe area.
     * 
     * @param h the human entering
     * @throws InterruptedException if interrupted
     */
    public void enter(Human h) throws InterruptedException
    {
        pm.check();
        synchronized(possibleTargets) // Uses the list's monitor for synchronization
        {
            possibleTargets.add(h);
            humansInside.add(h);
            logger.log("Human " + h.getHumanId() + " entered unsafe area " + area + ".");
            humansInsideCount.incrementAndGet();
            // Update GUI
            notifyChange();
        }
        pm.check();
    }
    
    /**
     * Registers a human leaving the unsafe area.
     * 
     * @param h the human leaving
     * @throws InterruptedException if interrupted
     */
    public void exit(Human h) throws InterruptedException
    {
        pm.check();
        synchronized (possibleTargets)  // Uses the list's monitor for synchronization
        {
            possibleTargets.remove(h);
            humansInside.remove(h);
            logger.log("Human " + h.getHumanId() + " left unsafe area " + area + ".");
            humansInsideCount.decrementAndGet();
            // Update GUI
            notifyChange();
        }
        pm.check();
    }
    
    /**
     * Simulates human wandering through the unsafe area: explores, and if not attacked collects food.
     * If attacked, may defend or be turned into a zombie.
     * 
     * @param h the human
     */
    public void wander(Human h)
    {
        pm.check();
        try
        {
            logger.log("Human " + h.getHumanId() + " is exploring unsafe area " + area + ".");
//        Thread.sleep(3000 + (int) (Math.random()*2000));

            // Exploration simulation with periodic pause checks
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
            
            // Collect food 
            h.collectFood(fgenerator.gatherFood());
            h.collectFood(fgenerator.gatherFood());
            logger.log("Human " + h.getHumanId() + " gathered 2 units of food in unsafe area " + area + ".");
        }
        catch(InterruptedException ie)
        {
            try
            {
                // Under attack (time governed by the zombie, when the zombie ends it interrupts again)
                Thread.sleep(10000);       
            }
            catch(InterruptedException ie2)
            {
                h.toggleAttacked();
                notifyChange();
                int defense = (int) (Math.random() * 3);  // 2/3 chance to survive
                pm.check();
                if (defense < 2) 
                {
                    logger.log("Human " + h.getHumanId() + " successfully defended itself from the attack.");
                    notifyChange();
                    
                    // If survived it's marked
                    h.toggleMarked();
                    pm.check();
                    
                    // Remove the attack from the hashmap using its monitor for synchronization
                    synchronized(attacks) 
                    { 
                        attacks.remove(h);
                        h.toggleAttacked();
                    }
                    pm.check();
                } 
                else 
                {
                    logger.log("Human " + h.getHumanId() + " failed to defend itself from the attack.");
                    notifyChange();
                    String zombieId = h.getHumanId().replaceFirst("H", "Z");
                    Zombie killer;
                    
                    pm.check();
                    // If the human failed to defend itself then get its killer and remove the entry from the hashmap using its monitor for synchronization
                    synchronized(attacks)
                    {  
                        killer = attacks.get(h);
                        attacks.remove(h);
                        humansInside.remove(h);
                        humansInsideCount.decrementAndGet();
                        notifyChange();
                        
                    }
                    pm.check(); 
                    
                    // Increase the killer's killcount
                    killer.increaseKillCount();
                    logger.log("Zombie " + killer.getZombieId() + " killed human " + h.getHumanId() + " (Kill count: " + killer.getKillCount() + ")");
                    
                    // Report the kill in case the top 3 most lethal zombies need to be updated
                    riskZone.reportKill(killer);
                    pm.check();
                    
                    // The human converted to zombie
                    Zombie killed = new Zombie(zombieId,this);
                    pm.check();
                    synchronized(zombiesInside) // Add the new zombie to zombiesInside list using its monitor for synchronization
                    {
                        zombiesInside.add(killed);
                        notifyChange();
                    }
                    pm.check();
                    logger.log("Human " + h.getHumanId() + " was reborn as " + "Zombie " + killed.getZombieId() + " in area " + area + ".");
                    killed.start();   // Begin zombie behaviour
                    pm.check();
                    h.interrupt();    // Termination of human (dies)
                }
            }
        }
    }
    
    /**
     * @return the RiskZone associated 
     */
    public RiskZone getRiskZone() 
    {
        return riskZone;
    }
    
    /**
     * @return the number which represents this area
     */
    public int getArea()
    {
        return area;
    }
    
    /**
     * @return atomic counter tracking humans inside
     */
    public AtomicInteger getHumansInsideCount()
    {
        return humansInsideCount;
    }
    
    public List<Human> getHumansInside()
    {
        synchronized(humansInside)
        {
            return humansInside;
        }
    }
   
    
    /**
     * Gets the current number of zombies in this area.
     * 
     * @return number of zombies protected by the list's monitor
     */
    public int getZombiesInsideCount()
    {
        int count;
        synchronized(zombiesInside)
        {
            count = zombiesInside.size();
            return count;
        }
    }
    
    public List<Zombie> getZombiesInside()
    {
        synchronized(zombiesInside)
        {
            return zombiesInside;
        }
    }
}
