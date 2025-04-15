/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.Comparator;

/**
 *
 * @author Lopex
 */
public class KillComparator implements Comparator<Zombie>
{

    public KillComparator()
    {
    }
    
    public int compare(Zombie z1, Zombie z2) 
    {
        return Integer.compare(z2.getKillCount(), z1.getKillCount());
    }
}
