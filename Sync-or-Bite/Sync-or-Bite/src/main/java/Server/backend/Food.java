/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Lopex
 */
public class Food 
{
    private Semaphore counter = new Semaphore(0, true);
    
    public Food()
    {
    }
    
    public void deposit()
    {
        counter.release(2);
    }
    
    public void eat() throws InterruptedException
    {
        counter.acquire();
        Thread.sleep(3000 + (int) (Math.random() * 2000));
    }
}
