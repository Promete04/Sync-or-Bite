/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.awt.Color;

/**
 * Represents a zombie. Each zombie runs as a separate thread.
 * 
 * Synchronization with shared resources is handled inside the unsafe areas.
 * 
 */
public class Zombie extends Thread
{
    private final String zombieId;
    private final RiskZone riskZone;
    private int killCount = 0;
    private int areaWhereReborn = -1; // Specific area if reborn
    
    private boolean attacking = false; // Whether the zombie is currently attacking

    /**
     * Constructs patient zero zombie.
     * This zombie starts without a predefined reborn area.
     * 
     * @param riskZone the risk zone where the zombie will operate in
     */
    public Zombie(RiskZone riskZone) 
    {
        this.zombieId = "Z0000";
        this.riskZone = riskZone; 
    }
    
    /**
     * Constructs a zombie that was created after an infection in a specific unsafe area.
     * 
     * @param zombieId the unique ID assigned to the zombie (e.g., Z0001)
     * @param unsafeArea the unsafe area where the zombie was reborn
     */
    public Zombie(String zombieId, UnsafeArea unsafeArea)
    {
        this.zombieId = zombieId;
        this.riskZone = unsafeArea.getRiskZone();
        this.areaWhereReborn = unsafeArea.getArea();
    }
    
    /**
     * Main execution method for the zombie thread.
     */
    public void run()
    {

        int lastArea = -1;  // Last area the zombie visited
        int nextArea = (int) (Math.random() * 4);   // Next area that will be visited
        
        while(true)
        {
            // If the zombie was reborn
            if(areaWhereReborn != -1)
            {
                // Zombie starts in a specific area
                riskZone.obtainUnsafeArea(areaWhereReborn).wander(this);
                riskZone.obtainUnsafeArea(areaWhereReborn).exit(this);
                
                // Reset to behave like normal zombies next iteration
                areaWhereReborn = -1;
                lastArea = areaWhereReborn;
                nextArea = (int) (Math.random() * 4);
            }
            else
            {
                // Random roaming logic (chooses an unsafe area from 0 to 3 without repeating consecutively)
                while (lastArea == nextArea)
                {
                    nextArea = (int) (Math.random() * 4);
                }
                
                riskZone.obtainUnsafeArea(nextArea).enter(this);
                riskZone.obtainUnsafeArea(nextArea).wander(this);
                riskZone.obtainUnsafeArea(nextArea).exit(this);
                
                lastArea = nextArea;
            } 
        }
    }

    /**
     * Returns the unique identifier for this zombie.
     * 
     * @return the zombie ID (e.g., "Z0001")
     */
    public String getZombieId() 
    {
        return zombieId;
    }

    /**
     * Returns how many humans this zombie has killed.
     * 
     * @return the number of kills
     */
    public int getKillCount() 
    {
        return killCount;
    }

     /**
     * Increases kill count by one.
     */
    public void increaseKillCount() 
    {
        killCount++;
    }
    
    /**
     * Sets the attacking status to the parameter.
     * 
     * @param attacking true if is attacking, false otherwise
     */
    public void setAttacking(boolean attacking)
    {
        this.attacking=attacking;
    }
    
    /**
     * Checks whether the zombie is currently attacking.
     * 
     * @return true if the zombie is attacking, false otherwise
     */
    public boolean isAttacking()
    {
        return attacking;
    }
    
    /**
     * Obtains the color that the zombie should have in the GUI.
     *
     * @returns the color that will be used by the GUI
     */
    public Color getZombieColor() 
    {
        Color color = isAttacking()? utils.ColorManager.ATTACKING_COLOR : utils.ColorManager.ZOMBIE_COLOR;
        return color;
    }
}
