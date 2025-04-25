package Server.backend;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a tunnel that allows humans to move between the refuge and an unsafe area. 
 *
 * Synchronization is handled using CyclicBarrier to form groups of 3 for exiting and 
 * ReentrantLock and Condition to coordinate access to the tunnel and also the monitors
 * of the waitingToExitQueue and waitingToReturnQueue.
 *
 * The synchronization tools ensure that only one human can cross the tunnel at a time, 
 * with returners having priority over exiters.
 */
public class Tunnel 
{
    // Barrier to wait for groups of 3 exiters
    private CyclicBarrier groups;
    
    // Fair lock and conditions for controlling tunnel crossing.
    // Since it's fair if more than one group of 3 is made the groups will go through the tunnel in order of arrival.
    private final ReentrantLock usingLock = new ReentrantLock(true); 
    private final Condition entryCondition = usingLock.newCondition(); // For humans returning
    private final Condition exitCondition = usingLock.newCondition();  // For humans exiting
    
// State tracking variables for tunnel crossing
    private boolean tunnelBusy = false;  // True if someone is crossing
    private Human currentInside = null;  // The human currently inside the tunnel
    
    // Waiting queues
    private final Queue<Human> waitingToExitQueue = new LinkedList<>();  // Exit queue
    private final Queue<Human> waitingToReturnQueue = new LinkedList<>();   // Return queue
    
    // The pause manager used to pause/resume
    private PauseManager pm;
    // The logger to log events
    private Logger logger;
    
    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
    private UnsafeArea unsafeArea;
    private int id;

    /**
     * Constructs a Tunnel associated with an unsafe area.
     *
     * @param unsafeArea the unsafe area connected to this tunnel
     * @param logger the logger
     * @param id Tunnel identifier 
     * @param pm the pause manager
     */
    public Tunnel(UnsafeArea unsafeArea, Logger logger,int id, PauseManager pm)
    {
        this.pm = pm;
        this.id=id;
        this.logger = logger;
        this.unsafeArea = unsafeArea;
        groups = new CyclicBarrier(3, new Runnable() // Cyclic barrier for forming groups of 3
        {
            public void run() 
            {
                logger.log("A group of " + 3 + " has been formed for exiting to unsafe area " + unsafeArea.getArea() + ".");
            }
        });
    }
    
    /**
     * Registers a new change listener to be notified when state update occurs.
     *
     * @param l the listener to register
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
     * Requests exit from the shelter to the unsafe area. 
     * The human joins a group of 3, waits for a turn to cross
     * and is animated crossing the tunnel in the GUI. Also handles logs.
     *
     * @param h the human requesting to exit
     * @throws InterruptedException if the thread is interrupted
     */
    public void requestExit(Human h) throws InterruptedException 
    {
        pm.check();
        synchronized(waitingToExitQueue)  // Protected using the queue's monitor
        {
            // Join the exit queue
            h.toggleWaitGroup();
            waitingToExitQueue.add(h);
            logger.log("Human " + h.getHumanId() + " is waiting to form a group to exit to unsafe area " + unsafeArea.getArea() + ".");
        }
        notifyChange();
        pm.check();
        
        // Wait for a group of 3 to form
        try 
        {
            groups.await();
        } 
        catch (BrokenBarrierException e)
        {
            logger.log("Barrier broken for human " + h.getHumanId() + ": " + e.getMessage());
        }
        // Group is formed, prepare for individual tunnel access
        h.toggleWaitGroup();
        notifyChange();
        pm.check();
        
        // Use the usingLock to ensure only one human is in the tunnel
        usingLock.lock();
        try 
        {
            // While the tunnel is busy, or if any returners are waiting, the exiter must wait
            while (tunnelBusy || hasReturnersWaiting()) 
            {
                exitCondition.await();
            }
            pm.check();
            // Remove from waiting as it is going to cross
            synchronized(waitingToExitQueue) // Protected using the queue's monitor
            {
                waitingToExitQueue.remove(h);
            }

            // Reserve the tunnel
            tunnelBusy = true;
            currentInside = h;
            notifyChange();
            pm.check();
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
        pm.check();
        // Simulate crossing with periodic pause checks
        logger.log("Human " + h.getHumanId() + " is crossing to unsafe area " + unsafeArea.getArea() + ".");
//        Thread.sleep(1000);
        Thread.sleep(500);
        pm.check();
        Thread.sleep(250);
        pm.check();
        Thread.sleep(250);
        pm.check();
        
        logger.log("Human " + h.getHumanId() + " has reached unsafe area " + unsafeArea.getArea() + ".");
        pm.check();
        
        // Release the tunnel and notify next human
        usingLock.lock();
        try 
        {
            pm.check();
            tunnelBusy = false;
            currentInside = null;
            notifyChange();
            // Give priority to returners
            if (hasReturnersWaiting()) 
            {
                entryCondition.signal();
            } 
            else
            {
                exitCondition.signal();
            }
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
    }
    
    /**
     * Requests return to the refuge from the unsafe area.
     * Ensures only one human is crossing and handles GUI and logs.
     *
     * @param h the human requesting to return
     * @throws InterruptedException if the thread is interrupted
     */
    public void requestReturn(Human h) throws InterruptedException 
    {
        pm.check();
        synchronized(waitingToReturnQueue) // Protected using the queue's monitor
        {
            // Join the return queue
            waitingToReturnQueue.add(h);
            logger.log("Human " + h.getHumanId() + " queued to return via tunnel from unsafe area " + unsafeArea.getArea() + ".");
        }
        notifyChange();
        pm.check();

        // Acquire the tunnel using usingLock
        usingLock.lock();
        try 
        {
            // Wait until the tunnel is free
            while(tunnelBusy)
            {
                entryCondition.await();
            }            
            pm.check();
            
            // Remove this human from the waiting queue as it is going to cross
            synchronized(waitingToReturnQueue) // Protected using the queue's monitor
            {
                waitingToReturnQueue.remove(h);
            }
            
            // Tunnel free, reserve it
            tunnelBusy = true;
            currentInside = h;
            notifyChange();
            pm.check();
            
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
        
        pm.check();
        // Simulate crossing with periodic pause checks
        logger.log("Human " + h.getHumanId() + " is crossing to refuge from unsafe area " + unsafeArea.getArea() + ".");
//        Thread.sleep(1000);
        Thread.sleep(500);
        pm.check();
        Thread.sleep(250);
        pm.check();
        Thread.sleep(250);
        pm.check();
        logger.log("Human " + h.getHumanId() + " has reached the refuge from unsafe area " + unsafeArea.getArea() + ".");
        
        // Release the tunnel
        usingLock.lock();
        try 
        {
            tunnelBusy = false;
            currentInside = null;
            notifyChange();
            
            // Give priority to returners
            if (hasReturnersWaiting()) 
            {
                entryCondition.signal();
            } 
            else
            {
                exitCondition.signal();
            }
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
    }
    
    /**
     * Checks if there are any humans currently waiting to return to the shelter.
     * Uses waitingToReturnQueue's monitor to protect the access.
     *
     * @return true if there are waiting returners, false otherwise.
     */
    private boolean hasReturnersWaiting() 
    {
        synchronized(waitingToReturnQueue)
        {
            return !waitingToReturnQueue.isEmpty();
        }
    }
    
    /**
     * Reports the number of humans currently involved in the tunnel
     *
     * @return total number of humans in the tunnel (waiting and crossing)
     */
    public int getTotalInTunnel() 
    {
        int result;
        if(tunnelBusy)
        {
            // One human is currently crossing the tunnel
            result=1+waitingToReturnQueue.size()+waitingToExitQueue.size();
        }
        else
        {
            // No one is crossing, only count those in the queues
             result=waitingToReturnQueue.size()+waitingToExitQueue.size();
        }
        return result;
    }
    
    /**
     * Returns the ID of the human currently inside the tunnel. 
     * If tunnel is not in use, returns placeholder "-----".
     * Protected with the usingLock lock to ensure safety.
     *
     * @return ID of current human inside tunnel or "-----" if empty.
     */
    public String getInTunnel()
    {
        String inside = "-----";
        usingLock.lock();
        try
        {
            if(tunnelBusy)
            {
                inside = currentInside.getHumanId();
            }
        }
        finally
        {
         usingLock.unlock();
        }
         
        return inside;
    }
    
    /**
     * Returns a copy of the queue of humans waiting to return to the refuge. 
     * 
     * Uses waitingToReturnQueue's monitor to ensure safety.
     *
     * @return a copy of the queue of humans waiting to return
     * @throws InterruptedException if thread is interrupted
     */
    public Queue<Human> getEntering() throws InterruptedException
    {
        synchronized(waitingToReturnQueue)
        {
            return new LinkedList<>(waitingToReturnQueue);
        }
    }
    
    /**
     * Returns a copy of the queue of humans waiting to exit to the unsafe area. 
     * 
     * Uses waitingToExitQueue's monitor to ensure safety.
     *
     * @return a copy of the queue of humans waiting to exit
     * @throws InterruptedException if thread is interrupted
     */
    public Queue<Human> getExiting() throws InterruptedException
    {
        synchronized(waitingToExitQueue)
        {
            return new LinkedList<>(waitingToExitQueue);
        }
    }

    /**
     * Returns the ID of this tunnel.
     *
     * @return the tunnel's ID
     */
    public int getId() 
    {
        return id;
    }
    
    /**
     * Gets the associated UnsafeArea.
     *
     * @return the UnsafeArea of the tunnel.
     */
    public UnsafeArea getUnsafeArea() 
    {
        return unsafeArea;
    }
}
