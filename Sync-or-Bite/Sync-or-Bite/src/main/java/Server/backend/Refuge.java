/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;
import Server.frontend.ServerApp;
import Server.frontend.MapPage;
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
    private MapPage mapPage = ServerApp.getMapPage();
    
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
     * Registers the entrance of a human into the refuge.
     *
     * @param h  the human entering the refuge
     */
    public void access(Human h)
    {
        mapPage.setCounter("RC", String.valueOf(count.incrementAndGet()));  // Increment the counter
        logger.log("Human " + h.getHumanId() + " entered the refuge.");
    }
    
    /**
     * Registers the exit of a human from the refuge.
     *
     * @param h  the human leaving the refuge
     */
    public void leave(Human h)
    {
        mapPage.setCounter("RC", String.valueOf(count.decrementAndGet()));  // Decrement the counter
        logger.log("Human " + h.getHumanId() + " left the refuge.");
    }
    
    /**
     * Allows a human to access the common area, perform preparation and then exit.
     *
     * @param h  the human accessing the common area
     * @throws InterruptedException if the thread is interrupted 
     */
    public void accessCommonArea(Human h) throws InterruptedException
    {
        commonArea.enter(h);
        commonArea.prepare(h);
        commonArea.exit(h);
    }
            
    /**
     * Allows a human to access the rest area, rest and then exit.
     *
     * @param h  the human resting
     * @throws InterruptedException if the thread is interrupted
     */
    public void restInRestArea(Human h) throws InterruptedException
    {
        restArea.enter(h);
        restArea.rest(h);
        restArea.exit(h);
    }
    
    /**
     * Allows a human to access the rest area, fully recover from injury and exit.
     *
     * @param h  the human recovering
     * @throws InterruptedException if the thread is interrupted 
     */
    public void fullRecoverInRestArea(Human h) throws InterruptedException
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
     * @throws InterruptedException if the thread is interrupted 
     */
    public void depositFoodInDiningRoom(Food f, Human h) throws InterruptedException  
    {
        diningRoom.storeFood(f, h);
    }
    
    /**
     * Allows a human to enter the dining room, consume food and exit.
     *
     * @param h  the human accessing the dining room
     * @throws InterruptedException if the thread is interrupted 
     */
    public void accessDiningRoom(Human h) throws InterruptedException
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
