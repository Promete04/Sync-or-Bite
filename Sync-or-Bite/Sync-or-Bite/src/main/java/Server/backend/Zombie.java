/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */
public class Zombie extends Thread
{
    private final String zombieId;
    private int killCount = 0;
    private final RiskZone riskZone;
    private int areaWhereReborned = -1;
    private final Logger logger;

    public Zombie(RiskZone riskZone, Logger logger) 
    {
        this.zombieId = "Z0000";
        this.riskZone = riskZone; 
        this.logger = logger;
    }
    
    public Zombie(String zombieId, UnsafeArea unsafeArea, Logger logger)
    {
        this.zombieId = zombieId;
        this.riskZone = unsafeArea.getRiskZone();
        this.logger = logger;
        this.areaWhereReborned = unsafeArea.getArea();
    }
    
    public void run()
    {
        try
        {
            int unsafeArea;
            while(true)
            {
                if(areaWhereReborned != -1)
                {
                    riskZone.accessUnsafeArea(areaWhereReborned).wander(this);
                    riskZone.accessUnsafeArea(areaWhereReborned).exit(this);
                    areaWhereReborned = -1;
                }
                else
                {
                    unsafeArea = (int) (Math.random() * 4);
                    riskZone.accessUnsafeArea(unsafeArea).enter(this);
                    riskZone.accessUnsafeArea(unsafeArea).wander(this);
                    riskZone.accessUnsafeArea(unsafeArea).exit(this);
                } 
            }
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }

    public String getZombieId() 
    {
        return zombieId;
    }

    public int getKillCount() 
    {
        return killCount;
    }

    public void increaseKillCount() 
    {
        killCount++;
    }
    
    
}
