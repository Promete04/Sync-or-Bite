/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerData listens for incoming client requests over a socket and returns state information.
 * Supports pausing/unpausing the simulation and retrieving real-time status of the system components.
 * 
 * Available commands for clients:
 * - "get": Returns pause state, human counts in refuge, tunnels, and unsafe areas, zombie counts, and top killers (most lethal zombies).
 * - "togglePause": Toggles the pause state of the simulation.
 */
public class ServerData extends Thread
{
    private PauseManager pm;
    private Refuge r;
    private Tunnels t;
    private RiskZone rk;
    
    /**
     * Constructs a ServerData thread.
     *
     * @param pm PauseManager for managing pause state
     * @param t Tunnels object containing tunnel information
     * @param r Refuge object containing refuge information
     * @param rk RiskZone object for unsafe area information
     */
    public ServerData(PauseManager pm, Tunnels t, Refuge r, RiskZone rk)
    {
        this.pm = pm;
        this.t = t;
        this.r = r;
        this.rk = rk;
    }
    
    /**
     * Continuously listens for socket connections and processes commands from clients.
     * Supports toggling pause state and retrieving current program state.
     * Each client connection is handled individually and closed afterward.
     */
    public void run()
    {   
        try
        {
            // Server listens on port 1
            ServerSocket server = new ServerSocket(1);
            while (true) 
            {
                // Accept incoming client connection
                Socket connection = server.accept();
                System.out.println("Connection from: " + connection.getInetAddress().getHostName());

                // Create the input and output streams used for the connection
                DataInputStream input = new DataInputStream(connection.getInputStream());  // Receives client request
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());  // Sends system state back

                // Read UTF command
                String request = input.readUTF();

                if(request.equals("get")) 
                {
                    // Build system state string with counts from different components and a boolean representing the pause/resume state
                    String data = pm.isPaused()
                            + "|" + r.getCount().get()                 // Number of humans in refuge
                            + "|" + t.obtainTunnel(0).getInTunnel() + "|" + t.obtainTunnel(1).getInTunnel() + "|" + t.obtainTunnel(2).getInTunnel() + "|" + t.obtainTunnel(3).getInTunnel()       // Number of humans in each tunnel
                            + "|" + rk.obtainUnsafeArea(0).getHumansInside().get() + "|" + rk.obtainUnsafeArea(1).getHumansInside().get() + "|" + rk.obtainUnsafeArea(2).getHumansInside().get() + "|" + rk.obtainUnsafeArea(3).getHumansInside().get() // Number of humans in each unsafe area
                            + "|" + rk.obtainUnsafeArea(0).getZombiesInside() + "|" + rk.obtainUnsafeArea(1).getZombiesInside() + "|" + rk.obtainUnsafeArea(2).getZombiesInside() + "|" + rk.obtainUnsafeArea(3).getZombiesInside()   //Number of zombies in each unsafe area
                            + "|" + rk.obtainTopKillers();
                    
                    // Send the data response to client
                    output.writeUTF(data);
                } 
                else if(request.equals("togglePause")) 
                {
                    // Toggle paused/unpaused mode
                    pm.togglePause();
                } 
                else
                {
                    System.out.printf("Unknown message received");
                } 
                
                // Close client connection after handling request
                connection.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
