/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.App;
import Server.frontend.MapPage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author guill
 */
public class DiningRoom 
{
    private final List<Human> diningList = new ArrayList<>();
    private final Queue<Food> foodList = new ConcurrentLinkedQueue<>();
    private final Semaphore mutex = new Semaphore(1,true);
    private final Semaphore foodCount = new Semaphore(0,true);
    private MapPage mapPage = App.getMapPage();
    private Logger logger;
    
    public DiningRoom(Logger logger)
    {
        this.logger = logger;
    }
    
    public void storeFood(Food f, Human h, PauseManager pm) throws InterruptedException  
    {
        pm.check();
        synchronized(foodList)
        {
            foodList.offer(f);
            foodCount.release();
            logger.log("Human " + h.getHumanId() + " has deposited 1 unit of food. " + "Total current food: " + foodList.size() + ".");
            mapPage.setCounter("FC",String.valueOf(foodList.size()));
        } 
        pm.check();
    }
    
    //Used semaphores to ensure fairness
    public void eatFood(Human h, PauseManager pm) throws InterruptedException  
    {   
        pm.check();
        foodCount.acquire();
        synchronized(foodList)
        {  
            foodList.poll();
            logger.log("Human " + h.getHumanId() + " is eating 1 unit of food. " + "Total current food: " + foodList.size() + ".");
            mapPage.setCounter("FC",String.valueOf(foodList.size()));
        }
        
//        Thread.sleep(3000 + (int) (Math.random()*2000));

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
        Thread.sleep(500 + (int) (Math.random() * 334));

        pm.check();
    }
    
    public void enter(Human h, PauseManager pm) throws InterruptedException
    {
        try
        {
            pm.check();
            mutex.acquire();
            diningList.add(h);
            logger.log("Human " + h.getHumanId() + " entered the dining room.");
            mapPage.setCounter("HD",String.valueOf( diningList.size()));
            mapPage.addLabelToPanel("D", h.getHumanId());
            if(h.isMarked())
            {
            mapPage.setLabelColorInPanel("D", h.getHumanId(),utils.ColorManager.INJURED_COLOR);
            }
        }
        finally
        {
            mutex.release();
            pm.check();
        }
        
    }
    
    public void exit(Human h, PauseManager pm) throws InterruptedException
    {
        try 
        {
            pm.check();
            mutex.acquire();
            diningList.remove(h);
            logger.log("Human " + h.getHumanId() + " left the dining room.");
            mapPage.setCounter("HD", String.valueOf(diningList.size()));
            mapPage.removeLabelFromPanel("D", h.getHumanId() );
        } 
        finally 
        {
            mutex.release();
            pm.check();
        }
    }
}
