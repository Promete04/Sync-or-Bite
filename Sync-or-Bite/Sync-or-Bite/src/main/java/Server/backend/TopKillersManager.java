/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 *
 * @author Lopex
 */
public class TopKillersManager 
{
    private PriorityQueue<Zombie> top;
    private HashSet<Zombie> killers;

    public TopKillersManager() 
    {
        top = new PriorityQueue<>(new KillComparator());
        killers = new HashSet<>();
    }

    public synchronized void reportKill(Zombie z) 
    {
        if(!killers.contains(z))
        {
            killers.add(z);
            top.offer(z); 
        }
        
        if (top.size() > 3) 
        {
            top.poll();
        }

    }

}

