/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package utils;

import javax.swing.SwingUtilities;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class that manages in an ordered and efficient way the dispatchment of tasks to the EDT (Event Dispatch Thread).
 */
public class UiDispatcher 
{
    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean scheduled = new AtomicBoolean(false);
    
    // Policy parameters:
    private final int    maxQueueSize         = 1000;                    // Max pending tasks
    private final long   maxProcessingNanos   = TimeUnit.MILLISECONDS.toNanos(50); // Max drain time

    /**
     * Call from any thread.
     * @param uiTask
     */
    public void invoke(Runnable uiTask) 
    {
        // 1) Drop oldest if we’re already “full”
        if (queue.size() >= maxQueueSize) 
        {
            queue.poll();
        }
        queue.add(uiTask);
        scheduleIfNeeded();
    }

    /** 
    *  Schedule a single invokeLater if none pending.
    */
    private void scheduleIfNeeded() 
    {
        if (scheduled.compareAndSet(false, true)) 
        {
            SwingUtilities.invokeLater(this::drainQueue);
        }
    }

    /** 
     * Runs on the EDT.
     */
    private void drainQueue() 
    {
        long start = System.nanoTime();
        try 
        {
            Runnable task;
            while ((task = queue.poll()) != null) 
            {
                task.run();
                // 2) If we’ve spent too long, drop the rest and bail out
                if (System.nanoTime() - start > maxProcessingNanos)
                {
                    queue.clear();
                    break;
                }
            }
        } 
        finally 
        {
            // Allow re-scheduling
            scheduled.set(false);
            // If anything snuck in while we were running, schedule another drain
            if (!queue.isEmpty()) 
            {
                scheduleIfNeeded();
            }
        }
    }
}
