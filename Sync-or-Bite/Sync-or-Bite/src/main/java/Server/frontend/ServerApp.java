/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.frontend;
import Server.backend.*;
import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *Main application class for the "Sync-or-Bite" game.
 * This class initializes and manages the GUI, backend components, and system logic.
 * It uses a CardLayout to switch between different pages (JPanels) in the application.
 * 
 * @author guill
 */
public class ServerApp 
{    
    // Frame where every page (jpanel) will appear
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cards = new JPanel(cardLayout);
    private static JFrame frame = new JFrame("Sync-or-Bite");
    
    // Pages for the application
    private static MapPage mapPage;
    private static LogPage logPage;
    
    // Backend components
    private static Logger logger=new Logger();
    private static PauseManager pm = new PauseManager();  

    // Map to store pages and the currently displayed panel
    private static Map<String, JPanel> pages;
    private static JPanel currentPanel; 
    
    // Simulation components
    private static RiskZone riskZone;
    private static Refuge refuge;
    private static Tunnels tunnels;
   
    /**
     * 
     * Main entry point for the application.
     * Initializes all components, sets up the GUI, and starts the system logic.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Initialize simulation components
        riskZone = new RiskZone(logger,pm);
        refuge = new Refuge(logger,pm);
        tunnels = new Tunnels(riskZone,logger, pm);
        
        // Initialize pages
        logPage= new LogPage();
        mapPage = new MapPage();
        
        // Initialize server data, started later
        ServerData server = new ServerData(pm, tunnels, refuge, riskZone);
        
        // Map pages to their names
        pages = new HashMap<>() 
        {
            {
                put("MAP", mapPage);
                put("LOG", logPage);
            }
        };
        currentPanel = pages.get("MAP");

        // Setup the application frame
        setupFrame();
        
        //Trigger MAP component resizing
        mapPage.enableAutoResize();

        // Redirect to the initial page
        redirect("MAP");
        
        // Start the first zombie (patient zero)
        new Zombie(riskZone).start(); 
        
        // Runnable to generate humans at random bounded (500ms-2000ms) intervals
        Runnable hr = new Runnable()
        {
            public void run()
            {
                try 
                {
                    for (int i = 1; i < 10001; i++) 
                    {
                        new Human(i, refuge, tunnels).start();
                        Thread.sleep(500 + (int) (Math.random()*1500));
                        pm.check();
                    }
                } 
                catch (InterruptedException ie) 
                {
                    ie.printStackTrace();
                }
            }
        }; 
        Thread humanGenerator = new Thread(hr);
        
        // Start the human generator thread
        humanGenerator.start();
        // Start the server
        server.start();
    }   
    
    /**
     *Gets the PauseManager instance.
     * 
     * @return
     */
    public static PauseManager getPM()
    {
        return pm;
    }
    
    /**
     * Gets the Refuge instance.
     * 
     * @return The Refuge instance.
     */
    public static Refuge getR()
    {
        return refuge;
    }
    
    /**
     * Gets the CommonArea instance from the Refuge.
     * 
     * @return The CommonArea instance.
     */
    public static CommonArea getCA()
    {
        return refuge.getCA();
    }
    
    /**
     * Gets the RestArea instance from the Refuge.
     * 
     * @return The RestArea instance.
     */
    public static RestArea getRA()
    {
        return refuge.getRA();
    }
    
    /**
     * Gets the DiningRoom instance from the Refuge.
     * 
     * @return The DiningRoom instance.
     */
    public static DiningRoom getDR()
    {
        return refuge.getDR();
    }
    
    /**
     * Gets the RiskZone instance.
     * 
     * @return The RiskZone instance.
     */
    public static RiskZone getRZ()
    {
        return riskZone;
    }
    
    /**
     * Gets the Tunnels instance.
     * 
     * @return The Tunnels instance.
     */
    public static Tunnels getT()
    {
        return tunnels;
    }

    /**
     * Gets the Logger instance.
     * 
     * @return The Logger instance.
     */
    public static Logger getL()
    {
        return logger;
    }
     
    /**
     * Redirects the application to a different page.
     * 
     * @param page A string with the page name.
     */
    public static void redirect(String page)
   {
        
        JPanel p = pages.get(page);
        cards.add(p, page);
        
        cardLayout.show(cards, page);
        if(currentPanel != p){
            cards.remove(currentPanel);
        }
        currentPanel = p;
        frame.repaint();
        frame.revalidate();
        
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


