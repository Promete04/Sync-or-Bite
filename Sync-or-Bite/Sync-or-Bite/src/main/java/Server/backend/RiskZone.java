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
    private FoodGenerator fgenerator = new FoodGenerator();
    private TopKillersManager tkm = new TopKillersManager();
    
    public RiskZone(Logger logger)
    {
        unsafeAreas = new UnsafeArea[4];
        for(int i=0; i<4; i++)
        {
            unsafeAreas[i] = new UnsafeArea(i+1, logger, fgenerator, this);
        }
    }
    
    public UnsafeArea[] getUnsafeAreas()
    {
        return unsafeAreas;
    }
    
    public UnsafeArea accessUnsafeArea(int area)
    {
        return unsafeAreas[area];
    }

    public TopKillersManager getTkm() 
    {
        return tkm;
    }
}
