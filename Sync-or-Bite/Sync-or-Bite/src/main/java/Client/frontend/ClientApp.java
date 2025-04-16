/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.frontend;

import static Server.frontend.ServerApp.currentPanel;
import static Server.frontend.ServerApp.frame;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author guill
 */
public class ClientApp {
    
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cards = new JPanel(cardLayout);
    public static JFrame frame = new JFrame("Sync-or-Bite");
    
    private static Map<String, JPanel> pages;
    private static MainClientPage mainPage;

    public static void main(String[] args)
    {
        mainPage = new MainClientPage();

        
        pages = new HashMap<>() 
        {
            {
                put("MAIN", mainPage);
            }
        };
        currentPanel = pages.get("MAP");
        setupFrame();
        redirect("MAIN");
         
    }   
    
     public static void redirect(String page)
   {
        
        JPanel p = pages.get(page);
        cards.add(p, page);
        
        cardLayout.show(cards, page);
        if(currentPanel != p){
            cards.remove(currentPanel);
        }
        currentPanel = p;
    }
   
   public static void setupFrame()
   {
        frame.setContentPane(cards);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1200, 810));
        frame.setMaximumSize(new Dimension(1920, 1080));
        frame.setVisible(true);
   }  
}
