/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the common area where humans temporarily gather before choosing a tunnel.
 * 
 * Synchronization is handled using the monitor for coordinated access.
 * 
 * Each human that enters will be visualized in the GUI and logged.
 * 
 * The pause manager is periodically checked for pausing/resuming the system.
 * 
 * Observers are registered to receive updates when the state changes (for the GUI).
 */
public class CommonArea 
{
    // List of humans inside
    private List<Human> humansInside = new ArrayList<>();
    // The logger to log events
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    
    /**
     * Constructor for CommonArea.
     * 
     * @param logger the logger
     * @param pm the pause manager
     */
    public CommonArea(Logger logger, PauseManager pm)
    {
        this.logger = logger;
        this.pm = pm;
    }
    
    /**
     * Called when a human enters the common area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using the monitor.
     * 
     * @param h the human entering the area
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void enter(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " entered the common area.");
        humansInside.add(h);
        pm.check();
    }
    
    /**
     * Called when a human leaves the common area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using the monitor.
     * 
     * @param h the human leaving the area
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void exit(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " left the common area.");
        humansInside.remove(h);
        pm.check();
    }
    
    /**
     * Human gets prepared and choses a tunnel.
     * 
     * @param h the human preparing in the common area
     * @throws InterruptedException if the thread is interrupted 
     */
    public void prepare(Human h) throws InterruptedException
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
    
    /**
     * Gets the current number of humans in the common area.
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
     * Returns a copy of the list of humans currently inside the common area.
     * No explicit synchronization is required since this method is used
     * by GUI listeners which already run within synchronized contexts (notifyChange() is used under protection).
     *
     * @return a copy of the list of humans inside the area
     */
    public ArrayList<Human> getHumansInside() 
    {
        return new ArrayList<>(humansInside);
    }
}
