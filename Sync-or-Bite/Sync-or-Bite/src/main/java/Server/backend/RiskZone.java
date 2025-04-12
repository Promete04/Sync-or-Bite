/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author guill
 */
public class RiskZone 
{
    private UnsafeArea[] unsafeAreas;
    
    public RiskZone()
    {
        unsafeAreas = new UnsafeArea[4];
        for(int i=0; i<4; i++)
        {
            unsafeAreas[i] = new UnsafeArea(i);
        }
    }
    
    public void accessUnsafeArea(Zombie z, int area) throws InterruptedException
    {
        unsafeAreas[area].wander(z);
    }
    
    public void accessUnsafeArea(Human h, int area)
    {
        unsafeAreas[area].wander(h);
    }
            
}
