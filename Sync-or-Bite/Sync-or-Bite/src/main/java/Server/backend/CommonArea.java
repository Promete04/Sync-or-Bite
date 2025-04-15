/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.App;
import Server.frontend.MapPage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author guill
 */
public class CommonArea 
{
    private List<Human> commonList = new ArrayList<>();
    private Semaphore mutex = new Semaphore(1,true);
    private Logger logger;
    private MapPage mapPage = App.getMapPage();
    
    
    public CommonArea(Logger logger)
    {
        this.logger = logger;
    }
    
    public synchronized void enter(Human h) throws InterruptedException
    {
        commonList.add(h);
        logger.log("Human " + h.getHumanId() + " entered the common area.");
        mapPage.setCounter("HC", String.valueOf(commonList.size()));
    }
    
    public synchronized void exit(Human h) throws InterruptedException
    {
        commonList.remove(h);
        logger.log("Human " + h.getHumanId() + " left the common area.");
        mapPage.setCounter("HC", String.valueOf(commonList.size()));
    }
    
    public void prepare(Human h) throws InterruptedException
    {  
        logger.log("Human " + h.getHumanId() + " is getting prepared in the common area.");
        Thread.sleep((int) (1000 + Math.random() * 1000));
        int selectedTunnel = (int)(Math.random()*4);
        logger.log("Human " + h.getHumanId() + " chose tunnel " + selectedTunnel + " in the common area.");
        h.setSelectedTunnel(selectedTunnel);
    }
    
    // Methods for monitoring
    public synchronized List<String> getCommonIds() throws InterruptedException 
    {
        List<String> ids = new ArrayList<>();
        
        mutex.acquire();
       
        for (Human h : commonList) 
        {
            ids.add(h.getHumanId());
        }
        
         mutex.release();
         return ids;
    }
    
}
