/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a human. Each human runs as a separate thread.
 * 
 * Synchronization with shared resources is handled inside the refuge areas, the different tunnels and unsafe areas.
 * 
 */
public class Human extends Thread
{
    private final String humanId;
    private final Refuge refuge;
    private final Tunnels tunnels;
    // Tunnel selected by this human
    private int selectedTunnel = -1;
    // Whether is waiting for a group
    private boolean waitGroup = false;
    // Whether is being currently attacked
    private boolean beingAttacked = false;
    // Whether this human was attacked by a zombie
    private boolean marked = false;
    // Represents the food it collected from the unsafe area
    private final List<Food> foodList = new ArrayList<>();
    
    /**
     * Constructs a Human object with a unique ID and references to the refuge,
     * the tunnel system, logger, and pause manager.
     *
     * @param id the human identifier 
     * @param refuge the refuge with the different areas
     * @param tunnels the tunnels leading to unsafe areas
     */
    public Human(int id, Refuge refuge, Tunnels tunnels)
    {
        this.humanId = String.format("H%04d", id);
        this.refuge = refuge;
        this.tunnels = tunnels;
    }
    
    /**
     * Main execution method for the human thread.
     */
    public void run()
    {
        try
        {
            // The human behaviour starts in the refuge
            refuge.access(this);
            
            while(true)
            {
                // Access common area for choosing the tunnel
                refuge.accessCommonArea(this);
                // Leave the refuge to go to the tunnels
                refuge.leave(this);
                
                // Go to the unsafe area connected to the tunnel selected
                tunnels.obtainTunnel(selectedTunnel).requestExit(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().enter(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().wander(this);
                sleep(10);         //Sleep just to trigger the interrupt in case it was interrupted (killed).
                
                // Leave the unsafe area through the tunnel
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().exit(this);
                tunnels.obtainTunnel(selectedTunnel).requestReturn(this); 
                
                // Return to the refuge
                refuge.access(this);
                // If the human wasn't attacked
                if(!marked)
                {
                    // Deposits food
                    refuge.depositFoodInDiningRoom(depositFood(), this);
                    refuge.depositFoodInDiningRoom(depositFood(), this);
                }
                
                // Rest in the rest area
                refuge.restInRestArea(this);
                
                // Eat 1 unit of food in the dining room
                refuge.accessDiningRoom(this);
                
                // If the human was attacked
                if(marked)
                {
                    // Returns to the rest area to get fully recovered
                    refuge.fullRecoverInRestArea(this);
                }
            }
        }
        catch(InterruptedException ie)
        {
            // Human thread has been interrupted (died)
        }
    }
    
    /**
     * Returns the human's unique identifier.
     *
     * @return the human ID string (e.g., "H0001")
     */
    public String getHumanId() 
    {
        return humanId;
    }
    
    /**
     * Toggles the marked status of the human.
     */
    public void toggleMarked() 
    {
        marked = !marked; 
    }
    
    /**
     * Indicates whether this human has been attacked.
     *
     * @return true if marked (attacked), false otherwise
     */
    public boolean isMarked()
    {
        return marked;
    }
    
    /**
     * Toggles the attacked status of the human.
     */
    public void toggleAttacked()
    {
        beingAttacked=!beingAttacked;
    }
    
    /**
     * Checks whether this human is currently being attacked.
     *
     * @return true if being attacked, false otherwise
     */
    public boolean isBeingAttacked()
    {
        return beingAttacked;
    }
    
    /**
     * Toggles the group waiting status.
     */
    public void toggleWaitGroup()
    {
        waitGroup=!waitGroup;
    }
    
    /**
     * Checks if the human is currently waiting for a group.
     *
     * @return true if waiting, false otherwise
     */
    public boolean isWaiting()
    {
        return waitGroup;
    }
    
    /**
     * Adds a piece of food collected from an unsafe area.
     *
     * @param f the food item to collect
     */
    public void collectFood(Food f)
    {
        
        foodList.add(f);
    }
    
    /**
     * Removes and returns the first collected food item.
     *
     * @return the first food item
     */
    public Food depositFood()
    {
        return foodList.removeFirst();
    }
    
    /**
     * Clears all collected food.
     */
    public void loseAllFood()
    {
        foodList.clear();
    }

    /**
     * Sets the tunnel index which the human will use to travel.
     *
     * @param selectedTunnel the index of the tunnel
     */
    public void setSelectedTunnel(int selectedTunnel) 
    {
        this.selectedTunnel = selectedTunnel;
    }
    
    public Color getHumanColor() 
    {
        Color color = isBeingAttacked() ? utils.ColorManager.ATTACKED_COLOR 
                :isMarked() ? utils.ColorManager.INJURED_COLOR 
                :utils.ColorManager.HUMAN_COLOR;
        return color;
    }

    
}
