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
 * Represents the dining room where humans store food and temporaly eat.
 * 
 * Synchronization is handled using the foodList monitor and fair Semaphores to ensure
 * order of arrival.
 * 
 * Each human that enters will be visualized in the GUI and logged.
 * 
 * The pause manager is periodically checked for pausing/resuming the system.
 * 
 * Observers are registered to receive updates when the state changes (for the GUI).
 */
public class DiningRoom 
{
    // List of humans inside
    private List<Human> humansInside = new ArrayList<>();
    // Concurrent non-blocking queue for storing food units
    private Queue<Food> foodList = new ConcurrentLinkedQueue<>();
    // Fair Semaphore for mutual exclusion
    private Semaphore mutex = new Semaphore(1,true);
    // Fair Semaphore used to track the number of food units available (if there are no units a queue will be formed in order of arrival)
    private Semaphore foodCount = new Semaphore(0,true);
    // The logger to log events
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    /**
     * Constructor for DiningRoom.
     * 
     * @param logger the logger
     * @param pm the pause manager
     */
    public DiningRoom(Logger logger, PauseManager pm)
    {
        this.logger = logger;
        this.pm = pm;
    }
    
    /**
     * Registers a new change listener to be notified when state update occurs.
     *
     * @param l the listener to add
     */
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
     
    /**
     * Notifies all registered listeners about a change in the state.
     */
    private void notifyChange() 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this);
        }
    }
    
    /**
     * Allows a human to deposit one unit of food into the dining room.
     * Access to foodList protected by the ConcurrentLinkedQueue but we use its monitor
     * to ensure that the logs, the semaphore and the GUI are updated together
     * 
     * @param f  the food unit being deposited
     * @param h  the human who is depositing the food
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void storeFood(Food f, Human h) throws InterruptedException  
    {
        pm.check();
        synchronized(foodList)
        {
            foodList.offer(f);
            foodCount.release();    // One food available, so add one permit or unblock a waiting thread (in FIFO order)
            logger.log("Human " + h.getHumanId() + " has deposited 1 unit of food. " + "Total current food: " + foodList.size() + ".");
            notifyChange(); 
        } 
        pm.check();
    }
    
    /**
     * Allows a human to consume one unit of food from the dining room.
     * A semaphore ensures that food cannot be eaten unless available, FIFO queue formed in case there are no food units.
     * 
     * @param h  the human who is eating
     * @throws InterruptedException if the thread is interrupted 
     */
    public void eatFood(Human h) throws InterruptedException  
    {   
        pm.check();
        foodCount.acquire();  // Wait until food is available
        synchronized(foodList) // foodList update and also log and GUI protected by foodList monitor
        {  
            foodList.poll();
            logger.log("Human " + h.getHumanId() + " is eating 1 unit of food. " + "Total current food: " + foodList.size() + ".");
            notifyChange();
        } 
        
//        Thread.sleep(3000 + (int) (Math.random()*2000));

        // Simulate eating time with periodic pause checks
        Thread.sleep(500 + (int) (Math.random() * 333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random() * 333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random() * 333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random() * 333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random() * 334));
        pm.check();
        Thread.sleep(250 + (int) (Math.random() * 167));
        pm.check();
        Thread.sleep(250 + (int) (Math.random() * 167));
        pm.check();
    }
    
    /**
     * Called when a human enters the dining room.
     * The operation is protected by a fair semaphore to ensure mutual exclusion and FIFO access.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * 
     * @param h  the human entering the room.
     * @throws InterruptedException if the thread is interrupted
     */
    public void enter(Human h) throws InterruptedException
    {
        try
        {
            pm.check();
            mutex.acquire(); // Mutual exclusion, critical section starts
            logger.log("Human " + h.getHumanId() + " entered the dining room.");
            humansInside.add(h);
            notifyChange(); 
        }
        finally
        {
            mutex.release(); // Mutual exclusion, critical section ends
            pm.check();
        }
        
    }
    
    /**
     * Called when a human exits the dining room.
     * The operation is protected by a fair semaphore to ensure mutual exclusion and FIFO access.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * 
     * @param h  the human leaving the room.
     * @throws InterruptedException if the thread is interrupted 
     */
    public void exit(Human h) throws InterruptedException
    {
        try 
        {
            pm.check();
            mutex.acquire();  // Mutual exclusion, critical section starts
            logger.log("Human " + h.getHumanId() + " left the dining room.");
            humansInside.remove(h);
            notifyChange(); 
        } 
        finally 
        {
            mutex.release();  // Mutual exclusion, critical section ends
            pm.check();
        }
    }
    
    /**
     * Gets the current number of food units stored in the dining room.
     * No explicit synchronization is required since this method is used
     * by GUI listeners which already run within synchronized contexts (notifyChange() is used under protection).
     *
     * @return the number of food units available
     */
    public int getFoodCount() 
    {
        return foodList.size();
    }
    
    /**
     * Gets the current number of humans in the dining room.
     * No explicit synchronization is required since this method is used
     * by GUI listeners which already run within synchronized contexts (notifyChange() is used under protection).
     * 
     * @return the number of humans currently inside
     */
    public int getHumansInsideCounter() 
    {
        return humansInside.size();
    }
    
    
    /**
     * Gets the list of humans currently inside the dining room.
     * No explicit synchronization is required since this method is used
     * by GUI listeners which already run within synchronized contexts (notifyChange() is used under protection).
     *
     * @return the list of humans inside the area
     */
    public List<Human> getHumansInside() 
    {
        return humansInside;
    }
}
