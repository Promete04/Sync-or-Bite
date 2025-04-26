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
 */
public interface ChangeListener extends EventListener 
{
    /**
     * Called when the observed source’s state changes.
     *
     * @param source the object that generated the change event
     */
    void onChange(Object source, Boolean isRepainting);
}


