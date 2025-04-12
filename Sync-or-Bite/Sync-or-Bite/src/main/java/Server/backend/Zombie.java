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
    private String zombieId;
    private int killCount = 0;
    private RiskZone riskZone;
    private Logger logger;

    public Zombie(RiskZone riskZone, Logger logger) 
    {
        this.zombieId = "Z0000";
        this.riskZone = riskZone; 
        this.logger = logger;
    }
    
    public Zombie(String zombieId, RiskZone riskZone, Logger logger)
    {
        this.zombieId = zombieId;
        this.riskZone = riskZone;
        this.logger = logger;
    }
    
    public void run()
    {
        try
        {
            int unsafeArea;
            while(true)
            {
                unsafeArea = (int) (Math.random()*4);
                logger.log("Zombie " + zombieId + " enters unsafe area " + unsafeArea + ".");
                riskZone.accessUnsafeArea(this, unsafeArea);
                logger.log("Zombie " + zombieId + " left unsafe area " + unsafeArea + ".");
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

    public void setId(String zombieId) 
    {
        this.zombieId = zombieId;
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
