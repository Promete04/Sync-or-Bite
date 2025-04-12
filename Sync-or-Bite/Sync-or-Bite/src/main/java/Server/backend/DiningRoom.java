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
    private final Food food = new Food();
    private final List<Human> diningList = new ArrayList<>();
    
    public DiningRoom()
    {
    }
    
    public void depositFood()
    {
        food.deposit();
    }
    
    public void eatFood() throws InterruptedException
    {
        food.eat();
    }
}
