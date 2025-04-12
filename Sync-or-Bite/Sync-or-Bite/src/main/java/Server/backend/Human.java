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
    
    public Human(int id, Refuge refuge, Tunnels tunnels, RiskZone riskZone)
    {
        this.humanId = String.format("H%04d", id);
        this.refuge = refuge;
        this.tunnels = tunnels;
        this.riskZone = riskZone;
    }
    
    public void run()
    {
        try
        {
            refuge.access();
            int tunnel = (int) (Math.random()*4);
            while(true)
            {
                tunnels.accessTunnel(this, tunnel);
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
