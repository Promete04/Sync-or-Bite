/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.frontend;
import Server.backend.*;
import javax.swing.*;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import java.awt.Dimension;
/**
 *
 * @author guill
 */
public class ServerApp 
{    
    // Frame where every page (jpanel) will appear
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cards = new JPanel(cardLayout);
    private static JFrame frame = new JFrame("Sync-or-Bite");
    
    // Declared 
    private static MapPage mapPage;
    private static LogPage logPage;
    
    
    private static Logger logger=new Logger();
    private static PauseManager pm = new PauseManager();  
    
    private static Map<String, JPanel> pages;
    private static JPanel currentPanel; 
    
    private static RiskZone riskZone;
    private static Refuge refuge;
    private static Tunnels tunnels;
   
    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        riskZone = new RiskZone(logger,pm);
        refuge = new Refuge(logger,pm);
        tunnels = new Tunnels(riskZone,logger, pm);
        
        logPage= new LogPage();
        mapPage = new MapPage();
        mapPage.enableAutoResize();
        
        ServerData server = new ServerData(pm, tunnels, refuge, riskZone);
        
        pages = new HashMap<>() 
        {
            {
                put("MAP", mapPage);
                put("LOG", logPage);
            }
        };
        currentPanel = pages.get("MAP");
        setupFrame();
        redirect("MAP");
             
        new Zombie(riskZone).start();  // Patient zero
        
        Runnable hr = new Runnable()
        {
            public void run()
            {
                try 
                {
                    for (int i = 1; i < 10001; i++) 
                    {
                        new Human(i, refuge, tunnels).start();
                        Thread.sleep(500 + (int) (Math.random() * 1500));
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
        
        humanGenerator.start();
        server.start();
    }   
    
    /**
     *
     * @return
     */
    public static PauseManager getPM()
    {
        return pm;
    }
    
    public static Refuge getR()
    {
        return refuge;
    }
    
    public static CommonArea getCA()
    {
        return refuge.getCA();
    }
    
    public static RestArea getRA()
    {
        return refuge.getRA();
    }
    
    public static DiningRoom getDR()
    {
        return refuge.getDR();
    }
    
    public static RiskZone getRZ()
    {
        return riskZone;
    }
    
    public static Tunnels getT()
    {
        return tunnels;
    }
     public static Logger getL()
     {
         return logger;
     }
     
    /**
     * Pages are inside a CardLayout. Inside the page's procedure, they can
     * call the App.redirect() method to change the view.
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
     *
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


