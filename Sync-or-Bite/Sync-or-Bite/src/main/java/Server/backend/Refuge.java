/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Lopex
 */
public class Refuge 
{
    private RestArea restArea;
    private CommonArea commonArea;
    private DiningRoom diningRoom;
    private AtomicInteger count;
    private Logger logger;
    
    public Refuge(Logger logger)
    {
        count = new AtomicInteger(0);
        restArea = new RestArea(logger);
        commonArea = new CommonArea(logger);
        diningRoom = new DiningRoom(logger);
        this.logger = logger;
    }
    
    public void access(Human h)
    {
        count.getAndIncrement();
        logger.log("Human " + h.getHumanId() + " enters the refuge.");
    }
    
    public void leave(Human h)
    {
        count.getAndDecrement();
        logger.log("Human " + h.getHumanId() + " leaves the refuge.");
    }
    
    public void accessCommonArea(Human h) throws InterruptedException
    {
        commonArea.enter(h);
        commonArea.prepare(h);
        commonArea.exit(h);
    }
    
    public void restInRestArea(Human h) throws InterruptedException
    {
        restArea.enter(h);
        restArea.rest(h);
        restArea.exit(h);
    }
    
    public void fullRecoverInRestArea(Human h) throws InterruptedException
    {
        restArea.enter(h);
        restArea.fullRecover(h);
        restArea.exit(h);
    }
    
    public void depositFoodInDiningRoom(Food f, Human h) throws InterruptedException  
    {
        diningRoom.storeFood(f, h);
    }
    
    public void accessDiningRoom(Human h) throws InterruptedException
    {
        diningRoom.enter(h);
        diningRoom.eatFood(h);
        diningRoom.exit(h);
    }
    
    
}
