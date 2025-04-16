/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.ServerApp;
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
    private MapPage mapPage = ServerApp.getMapPage();
    
    public RestArea(Logger logger)
    {
        this.logger = logger;
    }
    
    public synchronized void enter(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        restList.add(h);
        logger.log("Human " + h.getHumanId() + " entered the rest area.");
        mapPage.setCounter("HR", String.valueOf(restList.size()));
        mapPage.addLabelToPanel("R", h.getHumanId());
        if(h.isMarked())
        {
            mapPage.setLabelColorInPanel("R", h.getHumanId(),utils.ColorManager.INJURED_COLOR);
        }
        pm.check();
        
    }
    
    public synchronized void exit(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        restList.remove(h);
        logger.log("Human " + h.getHumanId() + " left the rest area.");
        mapPage.setCounter("HR",String.valueOf(restList.size()));
        mapPage.removeLabelFromPanel("R", h.getHumanId());
        pm.check();
    }
    
    public void rest(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is resting in the rest area.");
        
//        Thread.sleep(2000 + (int) (Math.random()*2000));     

        Thread.sleep(500 + (int) (Math.random()*500));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*500));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*500));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*250));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*250));
        pm.check();
    }
    
    public void fullRecover(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is being fully recovered in the rest area.");
        mapPage.setLabelColorInPanel("R", h.getHumanId(),utils.ColorManager.BG_COLOR);

//        Thread.sleep(3000 + (int) (Math.random()*2000));

        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*334));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*167));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*167));
        pm.check();
        
        h.toggleMarked();
        pm.check();
    }
}
