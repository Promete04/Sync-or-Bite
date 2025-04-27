/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.frontend;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main application class for the "Sync-or-Bite" client system.
 * This class initializes and manages the GUI, backend components, and system logic.
 */
public class ClientApp 
{
    
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cards = new JPanel(cardLayout);

    public static JFrame frame = new JFrame("Sync-or-Bite");
    
    private static Map<String, JPanel> pages;
    private static MainClientPage mainPage;
    private static JPanel currentPanel;

    /**
     * 
     * Main entry point for the application.
     * Initializes all components, sets up the GUI, and starts the system logic.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        mainPage = new MainClientPage();

        
        pages = new HashMap<>() 
        {
            {
                put("MAIN", mainPage);
            }
        };
        currentPanel = pages.get("MAIN");
        setupFrame();
        redirect("MAIN");
         
    }   
    
    /**
     * Redirects the application to a different page.
     * 
     * @param page a string with the page name
     */
    public static void redirect(String page)
    {
        
        JPanel p = pages.get(page);
        cards.add(p, page);
        
        cardLayout.show(cards, page);
        if(currentPanel != p)
        {
            cards.remove(currentPanel);
        }
        currentPanel = p;
    }
   
    /**
     * Sets up the main application frame.
     * Configures the frame's content, size, and visibility.
     */
    public static void setupFrame()
   {
        frame.setContentPane(cards);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1200, 810));
        frame.setMaximumSize(new Dimension(1920, 1080));
        frame.setVisible(true);
   }  
}
