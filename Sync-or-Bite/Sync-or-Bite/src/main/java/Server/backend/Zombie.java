/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 * Represents a zombie. Each zombie runs as a separate thread.
 * 
 * Synchronization with shared resources is handled inside the unsafe areas.
 * 
 */
public class Zombie extends Thread
{
    private PauseManager pm;
    private final String zombieId;
    private int killCount = 0;
    private final RiskZone riskZone;
    private int areaWhereReborn = -1; // Specific area if reborn
    private final Logger logger;

    /**
     * Constructs patient zero zombie.
     * This zombie always starts without a predefined reborn area.
     * 
     * @param riskZone the risk zone where the zombie will operate in
     * @param logger the logger
     * @param pm the pause manager
     */
    public Zombie(RiskZone riskZone, Logger logger, PauseManager pm) 
    {
        this.zombieId = "Z0000";
        this.riskZone = riskZone; 
        this.logger = logger;
        this.pm = pm;
    }
    
    /**
     * Constructs a zombie that was created after an infection in a specific unsafe area.
     * 
     * @param zombieId the unique ID assigned to the zombie (e.g., Z0001)
     * @param unsafeArea the unsafe area where the zombie was reborn
     * @param logger the logger
     * @param pm the pause manager
     */
    public Zombie(String zombieId, UnsafeArea unsafeArea, Logger logger, PauseManager pm)
    {
        this.pm = pm;
        this.zombieId = zombieId;
        this.riskZone = unsafeArea.getRiskZone();
        this.logger = logger;
        this.areaWhereReborn = unsafeArea.getArea();
    }
    
    /**
     * Main execution method for the zombie thread.
     */
    public void run()
    {
        try
        {
            int unsafeArea;
            while(true)
            {
                // If the zombie was reborn
                if(areaWhereReborn != -1)
                {
                    // Zombie starts in a specific area
                    riskZone.obtainUnsafeArea(areaWhereReborn).wander(this, pm);
                    riskZone.obtainUnsafeArea(areaWhereReborn).exit(this, pm);
                    // Reset to behave like normal zombies next iteration
                    areaWhereReborn = -1;
                }
                else
                {
                    // Random roaming logic (chooses an unsafe area from 0 to 3)
                    unsafeArea = (int) (Math.random() * 4);
                    riskZone.obtainUnsafeArea(unsafeArea).enter(this, pm);
                    riskZone.obtainUnsafeArea(unsafeArea).wander(this, pm);
                    riskZone.obtainUnsafeArea(unsafeArea).exit(this, pm);
                } 
            }
        }
        catch(InterruptedException ie)  // Interruptions handling
        {
            ie.printStackTrace();
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
}
