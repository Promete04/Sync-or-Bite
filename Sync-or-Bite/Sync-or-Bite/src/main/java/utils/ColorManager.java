package utils;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author guill
 */
public class ColorManager {
    
    // Main color, made for bigger visual impact
    public static Color MAIN_COLOR =            new Color(255,61,61);
    //Standar zombie color
    public static Color ZOMBIE_COLOR =       new Color(153, 78, 255);
    //Atacking zombie color
    public static Color ATACKING_COLOR =       new Color(204, 0, 204);
    
    public static Color TEXT_COLOR =       new Color(0, 0, 0);
    
    // Input background color
    public static Color BG_COLOR =        new Color(200,200,200,90);
  
   
    // Transparent color
    public static Color TRANSPARENT_COLOR =     new Color(0,0,0,0);
    
    // Human color
    public static Color HUMAN_COLOR =              new Color(204,255,204);
    // Human waiting for group color
    public static Color WAITING4GROUP_COLOR =          new Color(153,204,0);
    // Injured human color
    public static Color INJURED_COLOR =    new Color(255,80,80);
    //Being atacked color
     public static Color ATACKED_COLOR =        new Color(255,0,102);
    
    /**
     * Applies ZOMBIE_COLOR to the element when the mouse is hovering
 it.
     * @param component the component to be highlighted
     */
    public static void highlightOnHover(JButton component)
    {
        component.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(component.isEnabled())
                {
                    component.setBackground(ColorManager.ZOMBIE_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) 
            {
                if(component.isEnabled())
                {
                    component.setBackground(ColorManager.MAIN_COLOR);
                }
            }
        });
    }
    
    public static void highlightOnHover(JLabel component)
    {
        component.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e) 
            {
                if(component.isEnabled()){
                    component.setForeground(ColorManager.ZOMBIE_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) 
            {
                if(component.isEnabled()){
                    component.setForeground(ColorManager.MAIN_COLOR);
                }
            }
        });
    }
    
    /**
     * Disables the element and applies the ATACKED_COLOR
     * @param component the component to disable
     */
    public static void disable(JComponent component){
        component.setEnabled(false);
        component.setBackground(ATACKED_COLOR);
    }
    
    public static void enable(JComponent component){
        component.setEnabled(true);
        component.setBackground(MAIN_COLOR);
    }
    
}
    

