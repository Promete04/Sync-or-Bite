/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



/**
 * Represents a rest area where humans can enter, rest, and get fully recovered.
 * 
 * Synchronization is handled using the monitor for coordinated access.
 * 
 * Each human that enters will be visualized in the GUI and logged.
 */
public class RestArea 
{
    // Counter for humans inside (no need of Atomic variable since the update is done in synchronized method)
    private int humansInside = 0;
    private List<Human> humansIdsInside = new LinkedList<>();
    
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    /**
     * Constructs a RestArea with a shared logger.
     *
     * @param logger the logger 
     * @param pm the pause manager
     */
    public RestArea(Logger logger, PauseManager pm)
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
     * Called when a human enter the rest area.
     * Updates internal list, GUI, and shows the action in the log file.
     *
     * @param h  the human entering the rest area
     * @throws InterruptedException if the thread is interrupted during a pause check
     */
    public synchronized void enter(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " entered the rest area.");
        humansIdsInside.add(h);
        humansInside=humansInside+1;
        notifyChange();
        pm.check();
        
    }
    
    /**
     * Called when a human exits the rest area.
     * Updates internal list, GUI, and shows the action in the log file.
     *
     * @param h  the human exiting the rest area
     * @throws InterruptedException if the thread is interrupted 
     */
    public synchronized void exit(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " left the rest area.");
        humansIdsInside.remove(h);
        humansInside=humansInside-1;
        notifyChange();
        pm.check();
    }
    
    /**
     * Called when the human has to rest in the rest area.
     *
     * @param h  the human who is resting
     * @throws InterruptedException if the thread is interrupted 
     */
    public void rest(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is resting in the rest area.");
        
//        Thread.sleep(2000 + (int) (Math.random()*2000));     

        // Simulate resting time with periodic pause checks
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
    
    /**
     * Called when a human has to get fully recovered.
     *
     * @param h  the human recovering
     * @throws InterruptedException if the thread is interrupted
     */
    public void fullRecover(Human h) throws InterruptedException
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is being fully recovered in the rest area.");
        

//        Thread.sleep(3000 + (int) (Math.random()*2000));

        // Simulate recovering time with periodic pause checks
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        notifyChange();
        Thread.sleep(500 + (int) (Math.random()*333));
        pm.check();
        Thread.sleep(500 + (int) (Math.random()*334));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*167));
        pm.check();
        Thread.sleep(250 + (int) (Math.random()*167));
        pm.check();
        
        // Unmark the human after full recovery
        h.toggleMarked();
        pm.check();
    }
    
    public synchronized List<Human> getHumansIdsInside() 
    {
        return humansIdsInside;
    }
    
    public synchronized int getHumansInside()
    {
        return humansInside;
    }
}
