/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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
    private int humansInside = 0;
    private List<Human> humansObjInside = new ArrayList<>();
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    /**
     * Constructs a CommonArea object.
     * 
     * @param logger the logger
     * @param pm the pause manager
     */
    public CommonArea(Logger logger, PauseManager pm)
    {
        this.logger = logger;
        this.pm = pm;
    }
    
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
    public void removeChangeListener(ChangeListener l) 
    {
        listeners.remove(l);
    }
    
    private void notifyChange() 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this);
        }
    }
    
    /**
     * Called when a human enters the common area.
     * Updates internal list, GUI, and shows the action in the log file.
     * 
     * @param h the human entering the area
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void enter(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " entered the common area.");
        humansInside=humansInside+1;
        humansObjInside.add(h);
        notifyChange();
        pm.check();
    }
    
    /**
     * Called when a human leaves the common area.
     * Updates internal list, GUI, and shows the action in the log file.
     * 
     * @param h the human leaving the area
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void exit(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " left the common area.");
        humansObjInside.remove(h);
        humansInside=humansInside-1;
        notifyChange();
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
     * Exposed so UI can query the current count 
     * @return 
     */
    public int getHumansInside() 
    {
        return humansInside;
    }
    
    public synchronized List<Human> getHumansObjInside() 
    {
        return humansObjInside;
    }
}
