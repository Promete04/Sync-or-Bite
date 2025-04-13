/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author guill
 */
public class FoodGenerator 
{
    AtomicInteger IDs = new AtomicInteger(0);
    
    public void FoodGenerator()
    {
    }
    
    public Food gatherFood()
    {
        return new Food(IDs.incrementAndGet());
    }
    
}
