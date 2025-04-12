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
public class DiningRoom 
{
    private final List<Human> diningList = new ArrayList<>();
    private final List<Food> foodList = new ArrayList<>();
    
    public DiningRoom()
    {
    }
    
    public synchronized void storeFood(Food f)
    {
        foodList.add(f);
    }
    
    public  void eatFood(Human h) throws InterruptedException
    {
        
        foodList.removeFirst();
    }
}
