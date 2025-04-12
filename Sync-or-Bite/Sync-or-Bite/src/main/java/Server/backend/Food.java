/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */
public class Food 
{
    private int ID;
    
    public void Food(int pID)
    {
        this.ID=pID;
    }
     public int getFoodID()
     {
         return ID;
     }
}
