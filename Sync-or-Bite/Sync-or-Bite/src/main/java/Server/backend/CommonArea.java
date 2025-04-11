/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author guill
 */
public class CommonArea 
{
    ArrayList<Tunnel> tunnelList;
    ArrayList<Human> commonList;
    Semaphore me = new Semaphore(1,true);
    
    
    public void CommonArea (int numTunnel)
    {
        tunnelList = new ArrayList<>(numTunnel);
    }
    
    public void wander(Human h) throws InterruptedException
    {
        me.acquire();
        commonList.add(h);
        me.release();
        
        Thread.sleep((int) (1000 + Math.random() * 1000));
        
        me.acquire();
        commonList.remove(h);
        me.release();
    }
    
    //  Methods for monitoring
    public synchronized int getCommon() throws InterruptedException
    {
        me.acquire();
        try 
        {
            return commonList.size();
        } 
        finally 
        {
           me.release();
        }
    }
    public synchronized List<String> getCommonIds() throws InterruptedException 
    {
        List<String> ids = new ArrayList<>();
        
        me.acquire();
       
        for (Human h : commonList) 
        {
            ids.add(h.getId());
        }
        
         me.release();
         return ids;
    }
    
}
