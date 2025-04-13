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
public class RestArea 
{
    private List<Human> restList = new ArrayList<Human>();
    
    public RestArea()
    {
        
    }
    
    public synchronized void enter(Human h) throws InterruptedException
    {
        restList.add(h);
    }
    
    public synchronized void exit(Human h) throws InterruptedException
    {
        restList.remove(h);
    }
    
    public void rest(Human h) throws InterruptedException
    {
        Thread.sleep(2000 + (int) (Math.random()*2000));
    }
    
    public void fullRecover(Human h) throws InterruptedException
    {
        Thread.sleep(3000 + (int) (Math.random()*2000));
    }
}
