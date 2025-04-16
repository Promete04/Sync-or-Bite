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
 *
 * @author Lopex
 */
public class ServerData extends Thread
{
    private PauseManager pm;
    private Refuge r;
    private Tunnels t;
    private RiskZone rk;
    
    public ServerData(PauseManager pm, Tunnels t, Refuge r, RiskZone rk)
    {
        this.pm = pm;
        this.t = t;
        this.r = r;
        this.rk = rk;
    }
    
    public void run()
    {   
        try
        {
            ServerSocket server = new ServerSocket(1);
            while (true) 
            {
                Socket connection = server.accept();
                System.out.println("Connection from: " + connection.getInetAddress().getHostName());

                DataInputStream input = new DataInputStream(connection.getInputStream());
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());

                String request = input.readUTF();

                if(request.equals("get")) 
                {
                    String data = r.getCount().get()                 // Number of humans in refuge
                            + "|" + t.obtainTunnel(0).getInTunnel() + "|" + t.obtainTunnel(1).getInTunnel() + "|" + t.obtainTunnel(2).getInTunnel() + "|" + t.obtainTunnel(3).getInTunnel()       // Number of humans in each tunnel
                            + "|" + rk.accessUnsafeArea(0).getHumansInside().get() + "|" + rk.accessUnsafeArea(1).getHumansInside().get() + "|" + rk.accessUnsafeArea(2).getHumansInside().get() + "|" + rk.accessUnsafeArea(3).getHumansInside().get() // Number of humans in each unsafe area
                            + "|" + rk.accessUnsafeArea(0).getZombiesInside() + "|" + rk.accessUnsafeArea(1).getZombiesInside() + "|" + rk.accessUnsafeArea(2).getZombiesInside() + "|" + rk.accessUnsafeArea(3).getZombiesInside()   //Number of zombies in each unsafe area
                            + "|" + rk.obtainTopKillers();
                    
                    output.writeUTF(data);
                } 
                else if(request.equals("togglePause")) 
                {
                    pm.togglePause();
                } 
                else
                {
                    System.out.printf("Unknown message received");
                } 
                connection.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
