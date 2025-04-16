package Server.backend;


import Server.frontend.App;
import Server.frontend.MapPage;
import java.awt.Color;
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

    private UnsafeArea unsafeArea;
    private PauseManager pm;
    
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
    private final Queue<Human> waitingToExitShelter = new LinkedList<>();  // Exit queue.
    private final Queue<Human> waitingToEnterShelter = new LinkedList<>();   // Return queue.
    
    // Variable use to easily control the number neede to launch an raid
    private static final int GROUP_SIZE = 3;
    
    private Logger logger;
    
    private MapPage mapPage = App.getMapPage();
    private int ID;
    
    // Constructor: associates Tunnel with a specific unsafe area.
    public Tunnel(UnsafeArea unsafeArea, Logger logger,int pID, PauseManager pm)
    {
        this.pm = pm;
        this.ID=pID;
        this.logger = logger;
        this.unsafeArea = unsafeArea;
        groups = new CyclicBarrier(GROUP_SIZE, new Runnable() 
        {
            @Override
            public void run() 
            {
                logger.log("A group of " + GROUP_SIZE + " has been formed for exiting to unsafe area " + unsafeArea.getArea() + ".");
            }
        });
    }
    
    public UnsafeArea getUnsafeArea() 
    {
        return unsafeArea;
    }
    
    // Methods for Humans Exiting to the Risk Zone
    public void requestExit(Human h) throws InterruptedException 
    {
        pm.check();
        // Add the human to the waiting-to-exit queue (using entryWaitingLock).
        exitWaitingLock.lock();
        try 
        {
            waitingToExitShelter.add(h);
            logger.log("Human " + h.getHumanId() + " is waiting to form a group to exit to unsafe area " + unsafeArea.getArea() + ".");
            mapPage.addLabelToPanel("TE"+String.valueOf(ID+1), h.getHumanId());
            mapPage.setLabelColorInPanel("TE"+String.valueOf(ID+1), h.getHumanId(), utils.ColorManager.WAITING4GROUP_COLOR);
        } 
        finally 
        {
            exitWaitingLock.unlock();
            pm.check();
        }
        
        // Wait at the barrier until a group of 3 is formed.
        try 
        {
            groups.await();
            pm.check();
        } 
        catch (BrokenBarrierException e)
        {
            logger.log("Barrier broken for human " + h.getHumanId() + ": " + e.getMessage());
        }
        // Now, cross the tunnel individually.
        mapPage.setLabelColorInPanel("TE"+String.valueOf(ID+1), h.getHumanId(), utils.ColorManager.BG_COLOR);
        cross(h);
    }
    
    // Internal method for crossing to the risk zone.
    private void cross(Human h) throws InterruptedException
    {
        pm.check();
        // Use the usingLock to ensure only one human is in the tunnel.
        usingLock.lock();
        try 
        {
            pm.check();
            // While the tunnel is busy, or if any returners are waiting, the exiter must wait.
            while (tunnelBusy || hasReturnersWaiting()) 
            {
                exitCondition.await();
            }
            
            pm.check();
            //Remove form waiting as it is going to cross
            waitingToExitShelter.remove(h);
            mapPage.removeLabelFromPanel("TE"+String.valueOf(ID+1), h.getHumanId());
            pm.check();
           
            // Reserve the tunnel.
            tunnelBusy = true;
            currentInside = h;
            mapPage.setCounter("C"+String.valueOf(ID+1), currentInside.getHumanId());
            pm.check();
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
        pm.check();
        // Simulate the crossing (e.g., 1 second).
        logger.log("Human " + h.getHumanId() + " is crossing to unsafe area " + unsafeArea.getArea() + ".");
//        Thread.sleep(1000);
        Thread.sleep(500);
        pm.check();
        Thread.sleep(500);
        pm.check();
        
        logger.log("Human " + h.getHumanId() + " has reached unsafe area " + unsafeArea.getArea() + ".");
        pm.check();
        
        // Release the tunnel.
        usingLock.lock();
        try 
        {
            pm.check();
            tunnelBusy = false;
            currentInside = null;
            mapPage.setCounter("C"+String.valueOf(ID+1), "-----");
            pm.check();
            // Give priority to returners.
            if (hasReturnersWaiting()) 
            {
                entryCondition.signal();
                pm.check();
            } 
            else
            {
                exitCondition.signal();
                pm.check();
            }
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
    }
    
    // Methods for humans returning to the refuge
    
    public void requestReturn(Human h) throws InterruptedException 
    {
        pm.check();
        // Add the human to the waiting-to-enter queue (using entryWaitingLock).
        entryWaitingLock.lock();
        try 
        {
            waitingToEnterShelter.add(h);
            logger.log("Human " + h.getHumanId() + " queued to return via tunnel from unsafe area " + unsafeArea.getArea() + ".");
            pm.check();
            mapPage.addLabelToPanel("TR"+String.valueOf(ID+1), h.getHumanId());
            pm.check();
            if(h.isMarked())
            {
                mapPage.setLabelColorInPanel("TR"+String.valueOf(ID+1), h.getHumanId(),utils.ColorManager.INJURED_COLOR );
            }
        } 
        finally
        {
            entryWaitingLock.unlock();
            pm.check();
        }
        
        // Acquire the tunnel using usingLock.
        usingLock.lock();
        try 
        {
            pm.check();
            // Wait until the tunnel is free.
            while (tunnelBusy)
            {
                entryCondition.await();
            }
            pm.check();
            
            mapPage.removeLabelFromPanel("TR"+String.valueOf(ID+1), h.getHumanId());
            tunnelBusy = true;
            currentInside = h;
            mapPage.setCounter("C"+String.valueOf(ID+1), currentInside.getHumanId());
            pm.check();
            // Remove this human from the waiting queue.
            entryWaitingLock.lock();
            try 
            {
                waitingToEnterShelter.remove(h);
            } 
            finally 
            {
                entryWaitingLock.unlock();
                pm.check();
            }
        } 
        finally 
        {
            usingLock.unlock();
        }
        
        pm.check();
        // Simulate the crossing (e.g., 1 second).
        logger.log("Human " + h.getHumanId() + " is crossing to refuge from unsafe area " + unsafeArea.getArea() + ".");
//        Thread.sleep(1000);
        Thread.sleep(500);
        pm.check();
        Thread.sleep(500);
        pm.check();
        logger.log("Human " + h.getHumanId() + " has reached the refuge from unsafe area " + unsafeArea.getArea() + ".");
        
        // Release the tunnel.
        usingLock.lock();
        try 
        {
            pm.check();
            tunnelBusy = false;
            currentInside = null;
            mapPage.setCounter("C"+String.valueOf(ID+1), "-----");
            pm.check();
            if (hasReturnersWaiting()) 
            {
                entryCondition.signal();
                pm.check();
            } 
            else
            {
                exitCondition.signal();
                pm.check();
            }
        } 
        finally 
        {
            usingLock.unlock();
            pm.check();
        }
    }
    
    // Helper method to check if returners are waiting.
    private boolean hasReturnersWaiting() 
    {
        pm.check();
        entryWaitingLock.lock();
        try
        {
            return !waitingToEnterShelter.isEmpty();
        } 
        finally 
        {
            entryWaitingLock.unlock();
            pm.check();
        }
    }
    
    // --- Methods for Monitoring ---
    
    public synchronized List<String> getWaitingToExitIds() 
    {
        List<String> ids = new ArrayList<>();
        exitWaitingLock.lock();
        try {
            for (Human h : waitingToExitShelter)
            {
                ids.add(h.getHumanId ());
            }
        } finally {
            exitWaitingLock.unlock();
        }
  
        return ids;
    }
    
    public synchronized List<String> getWaitingToEnterIds() 
    {
        List<String> ids = new ArrayList<>();
        entryWaitingLock.lock();
        try {
            for (Human h : waitingToEnterShelter)
            {
                ids.add(h.getHumanId());
            }
        } finally {
            entryWaitingLock.unlock();
        }
       
        return ids;
    }
}
