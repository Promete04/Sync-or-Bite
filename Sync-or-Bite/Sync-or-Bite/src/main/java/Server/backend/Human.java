/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author guill
 */
public class Human extends Thread
{
    private String humanId;
    private Refuge refuge;
    private RiskZone riskZone;
    private Tunnels tunnels;
    private boolean marked = false;
    private Logger logger;
    
    public Human(int id, Refuge refuge, Tunnels tunnels, RiskZone riskZone, Logger logger)
    {
        this.humanId = String.format("H%04d", id);
        this.refuge = refuge;
        this.tunnels = tunnels;
        this.riskZone = riskZone;
        this.logger = logger;
    }
    
    public void run()
    {
        try
        {
            refuge.access();
            logger.log("Human " + humanId + " enters the refuge.");
            
            int tunnel;
            while(true)
            {
                refuge.accessCommonArea(this);
                tunnel = (int) (Math.random()*4);
                tunnels.accessTunnel(this, tunnel);
                refuge.depositFoodInDiningRoom();
                refuge.restInRestArea(this);
                refuge.accessDiningRoom(this);
                if(marked)
                {
                    refuge.fullRecoverInRestArea(this);
                }
            }
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
    }
    
    public String getHumanId() 
    {
        return humanId;
    }
    
}
