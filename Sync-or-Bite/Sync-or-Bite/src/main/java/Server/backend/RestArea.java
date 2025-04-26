/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rest area where humans can enter, rest, and get fully recovered.
 * 
 * Synchronization is handled using the monitor for coordinated access.
 * 
 * Each human that enters will be visualized in the GUI and logged.
 * 
 * The pause manager is periodically checked for pausing/resuming the system.
 * 
 * Observers are registered to receive updates when the state changes (for the GUI).
 */
public class RestArea 
{
    // List of humans inside
    private List<Human> humansInside = new ArrayList<>();
    // The logger to log events
    private Logger logger;
    // The pause manager used to pause/resume
    private PauseManager pm;
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    /**
     * Constructor for RestArea.
     *
     * @param logger the logger 
     * @param pm the pause manager
     */
    public RestArea(Logger logger, PauseManager pm)
    {
        this.logger = logger;
        this.pm = pm;
    }
    
    /**
     * Registers a new change listener to be notified when state update occurs.
     *
     * @param l the listener to add
     */
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
    
    /**
     * Notifies all registered listeners about a change in the state.
     */
    private void notifyChange() 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this);
        }
    }
    
    /**
     * Called when a human enter the rest area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using the monitor.
     *
     * @param h  the human entering the rest area
     */
    public synchronized void enter(Human h) 
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " entered the rest area.");
        humansInside.add(h);
        notifyChange();
        pm.check();
        
    }
    
    /**
     * Called when a human exits the rest area.
     * Updates internal list, notifies listeners and shows the action in the log file.
     * Protected using the monitor.
     *
     * @param h  the human exiting the rest area
     */
    public synchronized void exit(Human h) 
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " left the rest area.");
        humansInside.remove(h);
        notifyChange();
        pm.check();
    }
    
    /**
     * Called when the human has to rest in the rest area.
     *
     * @param h  the human who is resting
     */
    public void rest(Human h)
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is resting in the rest area.");
        
        try
        {
//        Thread.sleep(2000 + (int) (Math.random()*2000));     

            // Simulate resting time with periodic pause checks
            Thread.sleep(500 + (int) (Math.random() * 500)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 500)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 500)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 250)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 250)); pm.check();
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }
    
    /**
     * Called when a human has to get fully recovered.
     *
     * @param h  the human recovering
     */
    public void fullRecover(Human h) 
    {
        pm.check();
        logger.log("Human " + h.getHumanId() + " is being fully recovered in the rest area.");
        
        try
        {
//        Thread.sleep(3000 + (int) (Math.random()*2000));

            // Simulate recovering time with periodic pause checks
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 333)); pm.check();
            Thread.sleep(500 + (int) (Math.random() * 334)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 167)); pm.check();
            Thread.sleep(250 + (int) (Math.random() * 167)); pm.check();
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }

        // Unmark the human after full recovery
        h.toggleMarked();
        pm.check();
    }
    
    /**
     * Gets the current number of humans in the rest area.
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
     * Returns a copy of the list of humans currently inside the rest area.
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
