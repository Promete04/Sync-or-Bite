/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;
import Server.frontend.App;
import Server.frontend.MapPage;
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
    private MapPage mapPage = App.getMapPage();
    
    public Refuge(Logger logger)
    {
        count = new AtomicInteger(0);
        restArea = new RestArea(logger);
        commonArea = new CommonArea(logger);
        diningRoom = new DiningRoom(logger);
        this.logger = logger;
    }
    
    public void access(Human h, PauseManager pm)
    {
        count.getAndIncrement();
        logger.log("Human " + h.getHumanId() + " entered the refuge.");
        mapPage.setCounter("RC", String.valueOf(count));
    }
    
    public void leave(Human h, PauseManager pm)
    {
        count.getAndDecrement();
        logger.log("Human " + h.getHumanId() + " left the refuge.");
        mapPage.setCounter("RC", String.valueOf(count));
    }
    
    public void accessCommonArea(Human h, PauseManager pm) throws InterruptedException
    {
        commonArea.enter(h, pm);
        commonArea.prepare(h, pm);
        commonArea.exit(h, pm);
    }
            
    public void restInRestArea(Human h, PauseManager pm) throws InterruptedException
    {
        restArea.enter(h, pm);
        restArea.rest(h, pm);
        restArea.exit(h, pm);
    }
    
    public void fullRecoverInRestArea(Human h, PauseManager pm) throws InterruptedException
    {
        restArea.enter(h, pm);
        restArea.fullRecover(h, pm);
        restArea.exit(h, pm);
    }
    
    public void depositFoodInDiningRoom(Food f, Human h, PauseManager pm) throws InterruptedException  
    {
        diningRoom.storeFood(f, h, pm);
    }
    
    public void accessDiningRoom(Human h, PauseManager pm) throws InterruptedException
    {
        diningRoom.enter(h, pm);
        diningRoom.eatFood(h, pm);
        diningRoom.exit(h, pm);
    }

    public AtomicInteger getCount() 
    {
        return count;
    }
    
}
