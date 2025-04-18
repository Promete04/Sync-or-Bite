/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */
public class Tunnels 
{
    private Tunnel[] tunnels;
    private RiskZone riskZone;
    
    
    public Tunnels(RiskZone riskZone, Logger logger, PauseManager pm)
    {
        this.riskZone = riskZone;
        tunnels = new Tunnel[4];
        for(int i = 0; i<4; i++)
        {
            tunnels[i] = new Tunnel(riskZone.getUnsafeAreas()[i], logger,i, pm);
        }
    }
    
    public Tunnel obtainTunnel(int tunnel) 
    {
        return tunnels[tunnel];
    }
}
