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
    
    public Tunnels(Logger logger)
    {
        tunnels = new Tunnel[4];
        for(int i = 0; i<4; i++)
        {
            tunnels[i] = new Tunnel(i, logger);   //i corresponds to the UnsafeArea to which the tunnel is connected.
        }
    }
    
    public void accessTunnel(Human h, int tunnel) throws InterruptedException
    {
        tunnels[tunnel].requestExit(h);
    }
}
