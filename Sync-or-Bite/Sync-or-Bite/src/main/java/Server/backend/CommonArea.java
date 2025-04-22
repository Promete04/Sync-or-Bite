/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.ServerApp;
import Server.frontend.MapPage;

/**
 * Represents the common area where humans temporarily gather before choosing a tunnel.
 * 
 * Synchronization is handled using the monitor for coordinated access.
 * 
 * Each human that enters will be visualized in the GUI, logged, and will eventually
 * choose a tunnel after a preparation delay.
 */
public class CommonArea 
{
    // Counter for humans inside (no need of Atomic variable since the update is done in synchronized method)
    private int humansInside;
    private Logger logger;
    // GUI controller
    private MapPage mapPage = ServerApp.getMapPage();
    
    /**
     * Constructs a CommonArea object.
     * 
     * @param logger the logger
     */
    public CommonArea(Logger logger)
    {
        this.logger = logger;
    }
    
    /**
     * Called when a human enters the common area.
     * Updates internal list, GUI, and shows the action in the log file.
     * 
     * @param h the human entering the area
     * @param pm the pause manager
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void enter(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " entered the common area.");
        mapPage.setCounter("HC", String.valueOf(++humansInside));
        mapPage.addLabelToPanel("C", h.getHumanId());
        pm.check();
    }
    
    /**
     * Called when a human leaves the common area.
     * Updates internal list, GUI, and shows the action in the log file.
     * 
     * @param h the human leaving the area
     * @param pm the pause manager
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void exit(Human h, PauseManager pm) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " left the common area.");
        mapPage.setCounter("HC", String.valueOf(--humansInside));
        mapPage.removeLabelFromPanel("C", h.getHumanId() );
        pm.check();
    }
    
    /**
     * Human gets prepared and choses a tunnel.
     * 
     * @param h the human preparing in the common area
     * @param pm the pause manager 
     * @throws InterruptedException if the thread is interrupted 
     */
    public void prepare(Human h, PauseManager pm) throws InterruptedException
    {  
        pm.check();
        logger.log("Human " + h.getHumanId() + " is getting prepared in the common area.");
        
//        Thread.sleep(1000 + (int) (Math.random() * 1000));

        // Simulate preparing time with periodic pause checks
        Thread.sleep(500 + (int) (Math.random() * 500));
        pm.check();
        Thread.sleep(250 + (int) (Math.random() * 250));
        pm.check();
        Thread.sleep(250 + (int) (Math.random() * 250));
        pm.check();
        
        // Tunnel selection (from 0 to 3)
        int selectedTunnel = (int)(Math.random()*4);
        logger.log("Human " + h.getHumanId() + " chose tunnel " + selectedTunnel + " in the common area.");
        h.setSelectedTunnel(selectedTunnel);
        pm.check();
    }
}
