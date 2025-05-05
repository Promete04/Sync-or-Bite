/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates unique Food objects by assigning each one a distinct ID.
 * Internally uses an AtomicInteger to ensure ID generation is protected.
 */
public class FoodGenerator 
{
    // ID counter
    private AtomicInteger ids = new AtomicInteger(0);
    
    /**
     * Constructs a FoodGenerator with an initial ID counter set to 0.
     */
    public void FoodGenerator()
    {
        // No need to initialize nothing beyond the AtomicInteger 
    }
    
    /**
     * Generates a new Food object with a unique ID.
     *
     * @return a new food item with a thread safe unique identifier
     */
    public Food gatherFood()
    {
        return new Food(ids.incrementAndGet());
    }
    
}
