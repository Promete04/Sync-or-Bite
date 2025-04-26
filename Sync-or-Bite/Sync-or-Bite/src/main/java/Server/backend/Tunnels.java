/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 * Manages the 4 tunnels that connect the refuge to the unsafe areas.
 */
public class Tunnels 
{
    private Tunnel[] tunnels;  // The array of tunnels
    private RiskZone riskZone;
    
    /**
     * Constructs and initializes the tunnel system.
     * Each tunnel is linked to a specific UnsafeArea from the RiskZone.
     *
     * @param riskZone the RiskZone containing the UnsafeAreas
     * @param logger the logger
     * @param pm the pause manager
     */
    public Tunnels(RiskZone riskZone, Logger logger, PauseManager pm)
    {
        this.riskZone = riskZone;
        tunnels = new Tunnel[4];
        for(int i = 0; i<4; i++)
        {
            tunnels[i] = new Tunnel(riskZone.getUnsafeAreas()[i], logger,i, pm);
        }
    }
    
    /**
     * Returns a specific tunnel by index.
     *
     * @param tunnel index from 0 to 3 representing the tunnel ID
     * @return tunnel object associated with the given index
     */
    public Tunnel obtainTunnel(int tunnel) 
    {
        return tunnels[tunnel];
    }
}
