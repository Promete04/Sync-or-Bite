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
    private PauseManager pm;
    private final String humanId;
    private final Refuge refuge;
    private final Tunnels tunnels;
    private int selectedTunnel = -1;
    private boolean marked = false;
    private final Logger logger;
    private final List<Food> foodList = new ArrayList<>();
    
    public Human(int id, Refuge refuge, Tunnels tunnels, Logger logger, PauseManager pm)
    {
        this.pm = pm;
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
                pm.check();
                refuge.accessCommonArea(this);
                pm.check();
                refuge.leave(this);
                pm.check();
                tunnels.obtainTunnel(selectedTunnel).requestExit(this);
                pm.check();
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().enter(this);
                pm.check();
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().wander(this, pm);
                pm.check();
                sleep(1);         //Sleep just to trigger the interrupt in case it was interrupted (killed).
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().exit(this);
                pm.check();
                tunnels.obtainTunnel(selectedTunnel).requestReturn(this); 
                pm.check();
                refuge.access(this);
                pm.check();
                if(!marked)
                {
                    refuge.depositFoodInDiningRoom(depositFood(), this);
                    pm.check();
                    refuge.depositFoodInDiningRoom(depositFood(), this);
                    pm.check();
                }
                
                refuge.restInRestArea(this);
                pm.check();
                refuge.accessDiningRoom(this);
                pm.check();
                
                if(marked)
                {
                    refuge.fullRecoverInRestArea(this);
                    pm.check();
                }
            }
        }
        catch(InterruptedException ie)
        {
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
    
    public boolean isMarked()
    {
        return marked;
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
