/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 * The RiskZone class represents a big risk zone composed of 4 unsafe areas.
 * It keeps track of the top 3 most lethal zombies.
 * The access to the top 3 most lethal zombies array is protected using the monitor.
 */
public class RiskZone 
{
    private UnsafeArea[] unsafeAreas;
    // Shared FoodGenerator used by all unsafe areas.
    private FoodGenerator fgenerator = new FoodGenerator();
    private Zombie[] topKillers = new Zombie[3];  // Top 3 most lethal zombies
    private Logger logger;
    
    /**
     * Constructor for RiskZone. Initializes 4 UnsafeArea objects and assigns a number
     * from 0 to 3, the Logger, the FoodGenerator, the RiskZone and the PauseManager.
     *
     * @param logger the logger
     * @param pm the pause manager
     */
    public RiskZone(Logger logger, PauseManager pm)
    {
        this.logger = logger;
        unsafeAreas = new UnsafeArea[4];
        for(int i=0; i<4; i++)
        {
            unsafeAreas[i] = new UnsafeArea(i, logger, fgenerator, this, pm);
        }
    }
    
    /**
     * Returns the array containing the 4 UnsafeArea instances in the zone.
     *
     * @return an array of UnsafeArea.
     */
    public UnsafeArea[] getUnsafeAreas()
    {
        return unsafeAreas;
    }
    
    /**
     * Returns a specific UnsafeArea based on the index provided.
     *
     * @param area index of the unsafe area from 0 to 3.
     * @return the UnsafeArea at the given index.
     */
    public UnsafeArea obtainUnsafeArea(int area)
    {
        return unsafeAreas[area];
    }

    /**
     * Reports a zombie kill. Updates the topKillers array if the
     * zombie qualifies as a top killer. Ensures synchronization using the monitor.
     *
     * @param z the zombie who made a kill
     */
    public synchronized void reportKill(Zombie z) 
    {
        int i = 0;
        boolean in = false;
        
        // First, check if the zombie is already in the topKillers array
        while (i < 3 && !in) 
        {
            if (topKillers[i] != null && (topKillers[i].getZombieId() == z.getZombieId())) 
            {
                // Reorder the list if it's already there as its kill count might have increased
                reorderTopKillers();
                in = true;
            }
            i++;
        }
        
        // If it's not in the list, try to find a null slot to insert it
        if (!in) 
        {
            i = 0;
            while (i < 3 && !in) 
            {
                if (topKillers[i] == null) 
                {
                    topKillers[i] = z;
                    reorderTopKillers();        // Reorder the list
                    in = true;
                }
                i++;
            }
        }

        // If the list is full and this zombie has more kills than the third place (the one with least kills in the top), replace it
        if (!in && (topKillers[2] == null || z.getKillCount() > topKillers[2].getKillCount())) 
        {
            topKillers[2] = z;
            reorderTopKillers();          // Reorder the list
        }

        // Show in the log the updated list of top killers
        showTopKillers();
    }


    /**
     * Reorders the topKillers array in descending order of kill counts.
     * Uses a simple bubble sort algorithm.
     */
    private void reorderTopKillers() 
    {
        for (int i = 0; i < topKillers.length - 1; i++) 
        {
            for (int j = i + 1; j < topKillers.length; j++) 
            {
                if (topKillers[i] != null && topKillers[j] != null
                        && topKillers[j].getKillCount() > topKillers[i].getKillCount()) 
                {
                    Zombie aux = topKillers[i];
                    topKillers[i] = topKillers[j];
                    topKillers[j] = aux;
                }
            }
        }
    }


    /**
     * Shows in the logs a formatted string with the top 3 zombie killers and their kill counts.
     */

    public void showTopKillers() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Top 3 Zombies:\n");

        for (int i = 0; i < 3; i++) 
        {
            if (topKillers[i] != null) 
            {
                sb.append("                                             ").append(i + 1).append(". ").append(topKillers[i].getZombieId()).append(":  ").append(topKillers[i].getKillCount()).append("\n");
            }
        }

        logger.log(sb.toString());
    }
    
    /**
     * Returns a compact representation of the top 3 zombie killers. It is 
     * given to the client when information about the top 3 killers is requested.
     * Protected using the monitor.
     *
     * @return A string in the format 
     * "ZombieID|KillCount|ZombieID|KillCount|ZombieID|KillCount|" If a position
     * is empty, it uses "Z----|0|". The order is top 1, top 2 and top 3 from left to right.
     */
    public synchronized String obtainTopKillers() 
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) 
        {
            if (topKillers[i] != null) 
            {
                sb.append(topKillers[i].getZombieId()).append("|").append(topKillers[i].getKillCount()).append("|");
            }
            else
            {
                // Emtpy slot
                sb.append("Z----"+"|"+"0"+"|");
            }
        }
        return sb.toString();
    }
}
