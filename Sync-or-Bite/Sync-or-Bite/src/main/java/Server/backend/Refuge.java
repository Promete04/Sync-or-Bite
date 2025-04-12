/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Lopex
 */
public class Refuge 
{
    private RestArea restArea;
    private CommonArea commonArea;
    private DiningRoom diningRoom;
    private AtomicInteger count;
    private Food food;
    
    public Refuge()
    {
        AtomicInteger count = new AtomicInteger(0);
        restArea = new RestArea();
        commonArea = new CommonArea();
        diningRoom = new DiningRoom();
        food = new Food();
    }
    
    public void access()
    {
        count.getAndIncrement();
    }
    
    public void accessCommonArea(Human h) throws InterruptedException
    {
        commonArea.wander(h);
    }
    
    public void accessRestArea(Human h)
    {
        
    }
    
    public void accessDiningRoom(Human h)
    {
        
    }
}
