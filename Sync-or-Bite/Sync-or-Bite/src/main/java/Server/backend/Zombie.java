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
    private PauseManager pm;
    private final String zombieId;
    private int killCount = 0;
    private final RiskZone riskZone;
    private int areaWhereReborned = -1;
    private final Logger logger;

    public Zombie(RiskZone riskZone, Logger logger, PauseManager pm) 
    {
        this.zombieId = "Z0000";
        this.riskZone = riskZone; 
        this.logger = logger;
        this.pm = pm;
    }
    
    public Zombie(String zombieId, UnsafeArea unsafeArea, Logger logger, PauseManager pm)
    {
        this.pm = pm;
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
                pm.check();
                if(areaWhereReborned != -1)
                {
                    riskZone.accessUnsafeArea(areaWhereReborned).wander(this, pm);
                    pm.check();
                    riskZone.accessUnsafeArea(areaWhereReborned).exit(this, pm);
                    areaWhereReborned = -1;
                }
                else
                {
                    unsafeArea = (int) (Math.random() * 4);
                    riskZone.accessUnsafeArea(unsafeArea).enter(this, pm);
                    pm.check();
                    riskZone.accessUnsafeArea(unsafeArea).wander(this, pm);
                    pm.check();
                    riskZone.accessUnsafeArea(unsafeArea).exit(this, pm);
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
