/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guill
 */
public class Human extends Thread
{
    private final String humanId;
    private final Refuge refuge;
    private final Tunnels tunnels;
    private int selectedTunnel = -1;
    private boolean marked = false;
    private final Logger logger;
    private final List<Food> foodList = new ArrayList<>();
    
    public Human(int id, Refuge refuge, Tunnels tunnels, Logger logger)
    {
        this.humanId = String.format("H%04d", id);
        this.refuge = refuge;
        this.tunnels = tunnels;
        this.logger = logger;
    }
    
    @Override
    public void run()
    {
        try
        {
            refuge.access(this);
            
            while(true)
            {
                refuge.accessCommonArea(this);
                
                refuge.leave(this);
                
                tunnels.obtainTunnel(selectedTunnel).requestExit(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().enter(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().wander(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().exit(this);
                tunnels.obtainTunnel(selectedTunnel).requestReturn(this);
                
                refuge.access(this);
                refuge.depositFoodInDiningRoom(depositFood(), this);
                refuge.depositFoodInDiningRoom(depositFood(), this);
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
    
    //---method for registering being attacked and healed---
    public void toggleMarked() 
    {
        marked = !marked; 
    }
    
    public void collectFood(Food f)
    {
        
        foodList.add(f);
    }
    
    public Food depositFood()
    {
        return foodList.removeFirst();
    }
    
     //---method used when atacked but survived---
    public void looseAll()
    {
        foodList.clear();
    }

    public void setSelectedTunnel(int selectedTunnel) 
    {
        this.selectedTunnel = selectedTunnel;
    }
    
    
}
