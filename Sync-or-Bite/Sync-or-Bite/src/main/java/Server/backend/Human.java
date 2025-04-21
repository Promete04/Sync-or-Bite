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
            refuge.access(this,pm);
            
            while(true)
            {
                refuge.accessCommonArea(this,pm);
                refuge.leave(this,pm);
                tunnels.obtainTunnel(selectedTunnel).requestExit(this);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().enter(this, pm);
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().wander(this, pm);
                sleep(10);         //Sleep just to trigger the interrupt in case it was interrupted (killed).
                tunnels.obtainTunnel(selectedTunnel).getUnsafeArea().exit(this, pm);
                tunnels.obtainTunnel(selectedTunnel).requestReturn(this); 
                refuge.access(this,pm);
                if(!marked)
                {
                    refuge.depositFoodInDiningRoom(depositFood(), this, pm);
                    refuge.depositFoodInDiningRoom(depositFood(), this, pm);
                }
                
                refuge.restInRestArea(this, pm);
                refuge.accessDiningRoom(this, pm);
                
                if(marked)
                {
                    refuge.fullRecoverInRestArea(this, pm);
                }
            }
        }
        catch(InterruptedException ie)
        {
            // Human died
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
