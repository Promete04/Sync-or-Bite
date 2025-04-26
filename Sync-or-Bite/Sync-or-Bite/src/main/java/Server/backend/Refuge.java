/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a refuge that contains multiple areas (RestArea, CommonArea, DiningRoom).
 * 
 * This class delegates functionality to its internal areas and maintains a count
 * of the number of humans currently inside using an AtomicInteger to avoid race conditions.
 */
public class Refuge 
{
    // The pause manager used to pause/resume
    private PauseManager pm;
    private RestArea restArea;
    private CommonArea commonArea;
    private DiningRoom diningRoom;
    private AtomicInteger count;  // Number of humans in the refuge
    private Logger logger;
    
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    
    /**
     * Constructs the Refuge and initializes all internal areas.
     *
     * @param logger the logger 
     * @param pm the pause manager
     */
    public Refuge(Logger logger, PauseManager pm)
    {
        this.pm = pm;
        count = new AtomicInteger(0);
        restArea = new RestArea(logger, pm);
        commonArea = new CommonArea(logger, pm);
        diningRoom = new DiningRoom(logger, pm);
        this.logger = logger;
    }
    
    /**
     * @return the RestArea instance
     */
    public RestArea getRA()
    {
        return restArea;
    }
    
    /**
     * @return the CommonArea instance
     */
    public CommonArea getCA()
    {
        return commonArea;
    }
    
    /**
     * @return the DiningRoom instance
     */
    public DiningRoom getDR()
    {
        return diningRoom;
    }
    
    /**
     * Adds a listener that will be notified when the refuge state changes.
     *
     * @param l the listener to add
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
     * Registers the entrance of a human into the refuge and updates the counter.
     *
     * @param h  the human entering the refuge
     */
    public void access(Human h)
    {
        count.incrementAndGet();
        notifyChange(false);  
        logger.log("Human " + h.getHumanId() + " entered the refuge.");
    }
    
    /**
     * Registers the exit of a human from the refuge and updates the counter.
     *
     * @param h  the human leaving the refuge
     */
    public void leave(Human h)
    {
        count.decrementAndGet();
        notifyChange(false); 
        logger.log("Human " + h.getHumanId() + " left the refuge.");
    }
    
    /**
     * Allows a human to access the common area, perform preparation and then exit.
     *
     * @param h  the human accessing the common area
     */
    public void accessCommonArea(Human h) 
    {
        commonArea.enter(h);
        commonArea.prepare(h);
        commonArea.exit(h);
    }
            
    /**
     * Allows a human to access the rest area, rest and then exit.
     *
     * @param h  the human resting
     */
    public void restInRestArea(Human h)
    {
        restArea.enter(h);
        restArea.rest(h);
        restArea.exit(h);
    }
    
    /**
     * Allows a human to access the rest area, fully recover from injury and exit.
     *
     * @param h  the human recovering
     */
    public void fullRecoverInRestArea(Human h)
    {
        restArea.enter(h);
        restArea.fullRecover(h);
        restArea.exit(h);
    }
    
    /**
     * Allows a human to deposit food in the dining room.
     *
     * @param f  the food to be deposited
     * @param h  the human performing the deposit
     */
    public void depositFoodInDiningRoom(Food f, Human h) 
    {
        diningRoom.storeFood(f, h);
    }
    
    /**
     * Allows a human to enter the dining room, consume food and exit.
     *
     * @param h  the human accessing the dining room
     */
    public void eatInDiningRoom(Human h) 
    {
        diningRoom.enter(h);
        diningRoom.eatFood(h);
        diningRoom.exit(h);
    }

    /**
     * Returns the current count of humans inside the refuge.
     *
     * @return the atomic counter tracking humans in the refuge
     */
    public AtomicInteger getCount() 
    {
        return count;
    }
    
}
