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
 * 
 * Shared variables like zombiesInside, humansInside, possibleTargets and attacks are
 * accessed within synchronized using the respective object's monitor to ensure safety.
 * 
 * Each human that enters will be visualized in the GUI and logged.
 * 
 * The pause manager is periodically checked for pausing/resuming the system.
 * 
 * Observers are registered to receive updates when the state changes (for the GUI).
 */
public class UnsafeArea 
{
    // List of zombies currently inside this unsafe area
    private List<Zombie> zombiesInside = new ArrayList<>();  
    // List of humans currently inside 
    private List<Human> humansInside = new ArrayList<>();  
    // List of humans that can be targeted by zombies
    private List<Human> possibleTargets = new ArrayList<>();  
    // Thread safe counter for the number of humans inside
    private AtomicInteger humansInsideCount = new AtomicInteger(0);
    // Records attacked humans and the zombies attacking them
    private Map<Human,Zombie> attacks = new HashMap<>();
    // Food generator for humans wandering inside
    private FoodGenerator fgenerator;
    // The logger to log events
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    private RiskZone riskZone;
    private int area;
    
    /**
     * Constructor for UnsafeArea.
     * @param area the identifier of the unsafe area
     * @param logger the logger 
     * @param fgenerator the food generator for human food collection
     * @param riskZone the RiskZone 
     * @param pm the pause manager 
     */
    public UnsafeArea(int area, Logger logger, FoodGenerator fgenerator, RiskZone riskZone, PauseManager pm)
    {
        this.pm = pm;
        this.riskZone = riskZone;
        this.fgenerator = fgenerator;
        this.area = area;
        this.logger = logger;
    }
    
    /**
     * Registers a new change listener to be notified when state update occurs.
     *
     * @param l the listener to register
     */
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
    
    /**
     * Notifies all registered listeners about a change in the state.
     * 
     * @param isRepainting whether a repaint in the GUI is needed
     */
    private void notifyChange(boolean isRepainting) 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this,isRepainting);
        }
    }
    
    /**
     * Registers a zombie entering the unsafe area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using zombiesInside's monitor.
     *
     * @param z the zombie entering
     */
    public void enter(Zombie z) 
    {
        pm.check();
        synchronized(zombiesInside)  // Uses the list's monitor for synchronization
        {
            zombiesInside.add(z);
            logger.log("Zombie " + z.getZombieId() + " entered unsafe area " + area + ".");
            // Update GUI
            notifyChange(false);
        }
        pm.check();
    }
    
    /**
     * Registers a zombie leaving the unsafe area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using zombiesInside's monitor.
     * 
     * @param z the zombie leaving
     */
    public void exit(Zombie z) 
    {
        pm.check();
        synchronized(zombiesInside) // Uses the list's monitor for synchronization
        {
            zombiesInside.remove(z);
            logger.log("Zombie " + z.getZombieId() + " left unsafe area " + area + ".");
            // Update GUI
            notifyChange(false);
        }
        pm.check();
    }
    
    /**
     * Called by a zombie to perform its wandering behavior. 
     * If a human is inside the area, it attacks that human.
     * 
     * Uses possibleTargets and attacks monitor to ensure thread safe access when
     * selecting targets and recording attacks.
     * 
     * @param z the Zombie that is wandering.
     */
    public void wander(Zombie z) 
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
            notifyChange(true);
            
            // Record the target and itself in the attacks hashmap (using it's monitor).
            synchronized(attacks)
            {
                attacks.put(attackedHuman, z);
            }
            
            // Start of attack simulated via interrupt
            attackedHuman.interrupt();        

            try
            {
                // Simulate attack time with periodic pause checks
                
//            Thread.sleep(500 + (int) (Math.random()*1000));
                Thread.sleep(250 + (int) (Math.random() * 500)); pm.check();
                Thread.sleep(125 + (int) (Math.random() * 250)); pm.check();
                Thread.sleep(125 + (int) (Math.random() * 250)); pm.check();
            }
            catch(InterruptedException ie)
            {
                ie.printStackTrace();
            }
            
            attackedHuman.interrupt();      // End of attack using another interrupt
            
            pm.check();
            z.toggleAttacking();
            notifyChange(true);  // Notify listeners
        }
        
        try
        {
            // Simulate wandering time with periodic pause checks

//        Thread.sleep(2000 + (int) (Math.random()*1000));
            Thread.sleep(500 + (int) (Math.random() * 250)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 250)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 250)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 125)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 125)); pm.check();
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }
    
    /**
     * Registers a human entering the unsafe area.
     * Adds the human to both possibleTargets and humansInside lists, notifies 
     * listeners, updates the human counter and shows the action in the log file.
     * 
     * Protected using possibleTargets's monitor.
     * 
     * @param h the human entering
     */
    public void enter(Human h) 
    {
        pm.check();
        synchronized(possibleTargets) // Uses the list's monitor for synchronization
        {
            possibleTargets.add(h);
            humansInside.add(h);
            logger.log("Human " + h.getHumanId() + " entered unsafe area " + area + ".");
            humansInsideCount.incrementAndGet();
            // Update GUI
            notifyChange(false);
        }
        pm.check();
    }
    
    /**
     * Registers a human leaving the unsafe area.
     * Removes the human from both possibleTargets and humansInside lists, notifies 
     * listeners, updates the human counter and shows the action in the log file.
     * 
     * @param h the human leaving
     */
    public void exit(Human h) 
    {
        pm.check();
        synchronized (possibleTargets)  // Uses the list's monitor for synchronization
        {
            possibleTargets.remove(h);
            humansInside.remove(h);
            logger.log("Human " + h.getHumanId() + " left unsafe area " + area + ".");
            humansInsideCount.decrementAndGet();
            // Update GUI
            notifyChange(false);
        }
        pm.check();
    }
    
    /**
     * Simulates human wandering through the unsafe area: explores, and if not attacked collects food.
     * If attacked, may defend or be turned into a zombie.
     * 
     * Uses attacks and zombiesInside monitor to ensure thread safe access when humans
     * are attacked, killed or reborn as zombies.
     * 
     * @param h the human
     */
    public void wander(Human h)
    {
        try
        {
            logger.log("Human " + h.getHumanId() + " is exploring unsafe area " + area + ".");
//        Thread.sleep(3000 + (int) (Math.random()*2000));

            // Exploration simulation with periodic pause checks
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.underAttackCheck();
            Thread.sleep(500 + (int) (Math.random() * 334)); pm.underAttackCheck();
            Thread.sleep(250 + (int) (Math.random() * 167)); pm.underAttackCheck();
            Thread.sleep(250 + (int) (Math.random() * 167)); pm.underAttackCheck();
            
            // Collect food 
            h.collectFood(fgenerator.gatherFood());
            h.collectFood(fgenerator.gatherFood());
            logger.log("Human " + h.getHumanId() + " gathered 2 units of food in unsafe area " + area + ".");
            Thread.sleep(10);  // To ensure that the interrupt gets triggered in the rare case that the zombie interrupts the human while collecting the food.
        }
        catch(InterruptedException ie)
        {
            try
            {
                h.toggleAttacked();  // Attack starts
                h.loseAllFood();     // In case the human is interrupted after collecting the food.
                notifyChange(true);
                // Under attack (time governed by the zombie, when the zombie ends it interrupts again)
                Thread.sleep(86400000);       // 24 hours is the maximum time the program can remain stopped during a zombie attack without changing the program behaviour
            }
            catch(InterruptedException ie2)
            {
                int defense = (int) (Math.random() * 3);  
                if (defense < 2)  // 2/3 chance to survive
                {
                    logger.log("Human " + h.getHumanId() + " successfully defended itself from the attack.");
                    
                    // If survived it's marked
                    h.toggleAttacked();
                    h.toggleMarked();
                    pm.check();
                    notifyChange(true);
                    
                    // Remove the attack from the hashmap using its monitor for synchronization
                    synchronized(attacks) 
                    { 
                        attacks.remove(h);
                    }
                } 
                else 
                {
                    logger.log("Human " + h.getHumanId() + " failed to defend itself from the attack.");
                    String zombieId = h.getHumanId().replaceFirst("H", "Z");
                    Zombie killer;
                    
                    // If the human failed to defend itself then get its killer and remove the entry from the hashmap using its monitor for synchronization
                    synchronized(attacks)
                    {  
                        killer = attacks.get(h);
                        attacks.remove(h);
                        humansInside.remove(h);   // Human died so it's removed from humansInside
                        humansInsideCount.decrementAndGet(); // Decrement the counter
                    }
                    notifyChange(false);
                    pm.check(); 
                    
                    // Increase the killer's killcount
                    killer.increaseKillCount();
                    logger.log("Zombie " + killer.getZombieId() + " killed human " + h.getHumanId() + " (Kill count: " + killer.getKillCount() + ")");
                    
                    // Report the kill in case the top 3 most lethal zombies need to be updated
                    riskZone.reportKill(killer);
                    
                    // The human converted to zombie
                    Zombie killed = new Zombie(zombieId,this);
                    synchronized(zombiesInside) // Add the new zombie to zombiesInside list using its monitor for synchronization
                    {
                        zombiesInside.add(killed);
                    }
                    
                    notifyChange(false);
                    logger.log("Human " + h.getHumanId() + " was reborn as " + "Zombie " + killed.getZombieId() + " in area " + area + ".");
                    killed.start();   // Begin zombie behaviour
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
    
    /**
     * Returns a copy of the list of humans currently inside the unsafe area.
     * Protected by humansInside's monitor.
     *
     * @return a copy of the list of humans inside the unsafe area
     */
    public ArrayList<Human> getHumansInside()
    {
        synchronized(humansInside)
        {
            return new ArrayList<Human>(humansInside);
        }
    }
    
    /**
     * Gets the current number of zombies in the unsafe area.
     * Protected by zombiesInside's monitor.
     * 
     * @return the number of humans currently inside
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
    
    /**
     * Returns a copy of the list of zombies currently inside the unsafe area.
     * Protected by zombiesInside's monitor.
     *
     * @return a copy of the list of zombies inside the unsafe area
     */
    public ArrayList<Zombie> getZombiesInside()
    {
        synchronized(zombiesInside)
        {
            return new ArrayList<Zombie>(zombiesInside);
        }
    }
}
