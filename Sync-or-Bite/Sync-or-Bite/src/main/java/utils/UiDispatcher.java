 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package utils;

import java.util.Queue;
import javax.swing.SwingUtilities;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * UiDispatcher is a utility class designed to efficiently manage the execution
 * of GUI-related tasks on the Swing Event Dispatch Thread (EDT).
 * 
 * It enables smooth and responsive user interface by batching UI update tasks
 * and limiting how long they are processed in a single execution cycle. The
 * dispatcher ensures that updates from background threads are safely enqueued
 * and executed on the EDT without overwhelming it.
 */
public class UiDispatcher 
{
    // Thread safe queue to store pending UI update tasks
    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    
    // AtomicBoolean to track whether tasks are already scheduled for execution on the EDT 
    private final AtomicBoolean scheduled = new AtomicBoolean(false);
    
    // Policy parameters:
    private final int    maxQueueSize         = 1000;                              // Max pending tasks
    private final long   maxProcessingNanos   = TimeUnit.MILLISECONDS.toNanos(50); // Max time to execute the queued tasks

    /**
     * Call from any thread.
     * @param uiTask GUI modification task (e.g. Add a label)
     */
    public void invoke(Runnable uiTask) 
    {
        // Drop oldest if we're already "full"
        if (queue.size() >= maxQueueSize) 
        {
            queue.poll();
        }
        
        // Add the new task to the queue
        queue.add(uiTask);
        
        // Execute tasks if not already scheduled
        scheduleIfNeeded();
    }

    /** 
    *  Schedule a single invokeLater if none pending.
    */
    private void scheduleIfNeeded() 
    {
        if (scheduled.compareAndSet(false, true)) 
        {
            SwingUtilities.invokeLater(this::executeQueuedTasks); // Schedule the task processing on the EDT
        }
    }

    /** 
     * Runs on the EDT.
     */
    private void executeQueuedTasks() 
    {
        long start = System.nanoTime(); // Track the start time to monitor processing duration

        boolean stop = false;
        Runnable task;
        while (((task = queue.poll()) != null) && !stop) 
        {
            task.run();
            // If we've spent too long, clear the remaining tasks and stop
            if (System.nanoTime() - start > maxProcessingNanos)
            {
                queue.clear();
                stop = true;
            }
        }

        // Allow re-scheduling
        scheduled.set(false);
        // If any tasks were added during processing, schedule another execution
        if (!queue.isEmpty()) 
        {
            scheduleIfNeeded();
        }
    }
}
