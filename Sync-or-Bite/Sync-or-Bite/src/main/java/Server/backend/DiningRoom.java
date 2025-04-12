/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.ArrayList;

/**
 *
 * @author guill
 */
public class DiningRoom 
{
    private Food food;
    private ArrayList<Human> diningList;
    
    public DiningRoom()
    {
        food = new Food();
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
