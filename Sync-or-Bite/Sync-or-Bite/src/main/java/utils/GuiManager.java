package utils;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author guill
 */
public class GuiManager {
    
    public GuiManager()
    {
    }
    public static void confButton(JButton button,String path)
    {
       //button.setIcon(new ImageIcon(getClass().getResource( "/images/"+path+".png" )));
       button.setBackground(null);
       button.setBorder(null);
       
    }
    
    
    
}
