package Server.backend;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;



public class Tunnel 
{

    private RiskZone riskZone;
    
    // Barrier to wait for groups of 3 exiters.
    private CyclicBarrier groups;
    
    // Lock and conditions for controlling tunnel crossing.
    private final ReentrantLock usingLock = new ReentrantLock(true);  // Fair lock to avoid starvation.
    private final Condition entryCondition = usingLock.newCondition(); // For humans returning.
    private final Condition exitCondition = usingLock.newCondition();  // For humans exiting.
    
    // Locks to protect waiting queues.
    private final ReentrantLock entryWaitingLock = new ReentrantLock(true);
    private final ReentrantLock exitWaitingLock = new ReentrantLock(true);
    
    // State tracking variables for tunnel crossing.
    private boolean tunnelBusy = false;  // True if someone is crossing.
    private Human currentInside = null;  // The human currently inside the tunnel.
    
    // Queues for tracking waiting humans.
    private Queue<Human> waitingToExitShelter = new LinkedList<>();  // Exit queue.
    private Queue<Human> waitingToEnterShelter = new LinkedList<>();   // Return queue.
    
    // Variable use to easily control the number neede to launch an raid
    private static final int GROUP_SIZE = 3;
    
    private Logger logger;
    
    // Constructor: associates Tunnel with a specific risk zone.
    public Tunnel(RiskZone riskZone) 
    {
        this.riskZone = riskZone;
        groups = new CyclicBarrier(GROUP_SIZE, new Runnable() 
        {
            public void run() 
            {
                System.out.println("A group of " + GROUP_SIZE + " has been formed for exiting to " + riskZone);
            }
        });
    }
    
    public Tunnel(int unsafeArea, Logger logger)
    {
        this.logger = logger;
    }
    
    public RiskZone getRiskZone() 
    {
        return riskZone;
    }
    
    // Methods for Humans Exiting to the Risk Zone
    public void requestExit(Human h) throws InterruptedException 
    {
        // Add the human to the waiting-to-exit queue (using entryWaitingLock).
        exitWaitingLock.lock();
        try 
        {
            waitingToExitShelter.add(h);
            System.out.println("Human " + h.getHumanId() + " is waiting to form a group to exit to " + riskZone);
        } 
        finally 
        {
            exitWaitingLock.unlock();
        }
        
        // Wait at the barrier until a group of 3 is formed.
        try 
        {
            groups.await();
        } 
        catch (BrokenBarrierException e)
        {
            System.out.println("Barrier broken for human " + h.getHumanId() + ": " + e.getMessage());
            return;
        }
        
        // Remove the human from the waiting queue once released.
        exitWaitingLock.lock();
        try 
        {
            waitingToExitShelter.remove(h);
        } 
        finally 
        {
            exitWaitingLock.unlock();
        }
        
        // Now, cross the tunnel individually.
        crossToRiskZone(h);
    }
    
    // Internal method for crossing to the risk zone.
    private void crossToRiskZone(Human h) throws InterruptedException
    {
        // Use the usingLock to ensure only one human is in the tunnel.
        usingLock.lock();
        try 
        {
            // While the tunnel is busy, or if any returners are waiting, the exiter must wait.
            while (tunnelBusy || hasReturnersWaiting()) 
            {
                exitCondition.await();
            }
            // Reserve the tunnel.
            tunnelBusy = true;
            currentInside = h;
        } 
        finally 
        {
            usingLock.unlock();
        }
        
        // Simulate the crossing (e.g., 1 second).
        System.out.println("Human " + h.getHumanId() + " is crossing to risk zone " + riskZone);
        Thread.sleep(1000);
        System.out.println("Human " + h.getHumanId() + " has reached risk zone " + riskZone);
        
        // Release the tunnel.
        usingLock.lock();
        try 
        {
            tunnelBusy = false;
            currentInside = null;
            // Give priority to returners.
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
        }
    }
    
    // Methods for Humans Returning to the Refuge
    
    public void requestReturn(Human h) throws InterruptedException 
    {
        // Add the human to the waiting-to-enter queue (using entryWaitingLock).
        entryWaitingLock.lock();
        try 
        {
            waitingToEnterShelter.add(h);
            System.out.println("Human " + h.getHumanId() + " queued to return via tunnel from " + riskZone);
        } 
        finally
        {
            entryWaitingLock.unlock();
        }
        
        // Acquire the tunnel using usingLock.
        usingLock.lock();
        try 
        {
            // Wait until the tunnel is free.
            while (tunnelBusy)
            {
                entryCondition.await();
            }
            tunnelBusy = true;
            currentInside = h;
            // Remove this human from the waiting queue.
            entryWaitingLock.lock();
            try 
            {
                waitingToEnterShelter.remove(h);
            } 
            finally 
            {
                entryWaitingLock.unlock();
            }
        } 
        finally 
        {
            usingLock.unlock();
        }
        
        // Simulate the crossing (e.g., 1 second).
        System.out.println("Human " + h.getHumanId() + " is crossing to refuge from " + riskZone);
        Thread.sleep(1000);
        System.out.println("Human " + h.getHumanId() + " has reached the refuge from " + riskZone);
        
        // Release the tunnel.
        usingLock.lock();
        try 
        {
            tunnelBusy = false;
            currentInside = null;
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
        }
    }
    
    // Helper method to check if returners are waiting.
    private boolean hasReturnersWaiting() 
    {
        entryWaitingLock.lock();
        try
        {
            return !waitingToEnterShelter.isEmpty();
        } 
        finally 
        {
            entryWaitingLock.unlock();
        }
    }
    
    // --- Methods for Monitoring ---
    
    public synchronized Human getCurrentInside() 
    {
        return currentInside;
    }
    
    public synchronized int getWaitingToExitCount()
    {
        exitWaitingLock.lock();
        try 
        {
            return waitingToExitShelter.size();
        } 
        finally 
        {
            exitWaitingLock.unlock();
        }
    }
    
    public synchronized int getWaitingToEnterCount() 
    {
        entryWaitingLock.lock();
        try 
        {
            return waitingToEnterShelter.size();
        } 
        finally
        {
            entryWaitingLock.unlock();
        }
    }
    
    public synchronized List<String> getWaitingToExitIds() 
    {
        List<String> ids = new ArrayList<>();
        exitWaitingLock.lock();
        
        for (Human h : waitingToExitShelter) 
        {
            ids.add(h.getHumanId ());
        }
      
        exitWaitingLock.unlock();
  
        return ids;
    }
    
    public synchronized List<String> getWaitingToEnterIds() 
    {
        List<String> ids = new ArrayList<>();
        entryWaitingLock.lock();
        
        for (Human h : waitingToEnterShelter)
        {
            ids.add(h.getHumanId());
        }
        entryWaitingLock.unlock();
       
        return ids;
    }
}
