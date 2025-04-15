/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.App;
import Server.frontend.MapPage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guill
 */
public class RestArea 
{
    private List<Human> restList = new ArrayList<Human>();
    private Logger logger;
    private MapPage mapPage = App.getMapPage();
    
    public RestArea(Logger logger)
    {
        this.logger = logger;
    }
    
    public synchronized void enter(Human h) throws InterruptedException
    {
        restList.add(h);
        logger.log("Human " + h.getHumanId() + " entered the rest area.");
        mapPage.setCounter("HR", String.valueOf(restList.size()));
        
    }
    
    public synchronized void exit(Human h) throws InterruptedException
    {
        restList.remove(h);
        logger.log("Human " + h.getHumanId() + " left the rest area.");
        mapPage.setCounter("HR",String.valueOf(restList.size()));
    }
    
    public void rest(Human h) throws InterruptedException
    {
        logger.log("Human " + h.getHumanId() + " is resting in the rest area.");
        Thread.sleep(2000 + (int) (Math.random()*2000));  
    }
    
    public void fullRecover(Human h) throws InterruptedException
    {
        logger.log("Human " + h.getHumanId() + " is being fully recovered in the rest area.");
        Thread.sleep(3000 + (int) (Math.random()*2000));
        h.toggleMarked();
    }
}
