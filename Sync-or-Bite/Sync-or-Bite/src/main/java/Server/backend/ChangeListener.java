/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Server.backend;

import java.util.EventListener;

/**
 * A generic listener interface for observing state changes in backend objects.
 * 
 * This interface is designed for decoupled communication between backend logic 
 * and other parts of the application (e.g., frontend or controller logic).
 * 
 * The idea is similar to the Observer pattern: backend entities can notify 
 * interested parties when their internal state changes, without needing to 
 * know who those parties are or what they will do with the information.
 * 
 * Why use this instead of a simple Runnable?
 * - A Runnable has no context: it runs a task, but doesn't indicate *what* triggered it.
 * - ChangeListener provides the source of the change, allowing for more flexible and contextual reactions.
 * - You can create specific implementations that behave differently depending on the source.
 * 
 * This is useful in scenarios where:
 * - You want to reflect state changes in the UI.
 * - You want to trigger logging, statistics, or side effects only when specific data changes.
 * - Multiple listeners may need to observe the same source.
 * 
 * Example usage:
 *   area.addChangeListener(new ChangeListener() {
 *       public void onChange(Object source) {
 *           // Update UI, log change, etc.
 *       }
 *   });
 * 
 * Note: This design supports multiple listeners per source.
 * 
 */
public interface ChangeListener extends EventListener 
{
    /**
     * Called when the observed source’s state changes.
     *
     * @param source the object that generated the change event
     */
    void onChange(Object source);
}


