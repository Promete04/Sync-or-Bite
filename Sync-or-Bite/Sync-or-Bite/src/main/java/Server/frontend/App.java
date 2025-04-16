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
public class App 
{    
    
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cards = new JPanel(cardLayout);
    public static JFrame frame = new JFrame("Sync-or-Bite");
    private static MapPage mapPage;
    public static Logger logger=new Logger();
    private static LogPage logPage;
    public static PauseManager pm = new PauseManager();  
    private static Map<String, JPanel> pages;
    public static JPanel currentPanel; 
   
    public static void main(String[] args)
    {
        mapPage = new MapPage();
        logPage= new LogPage();
        
        RiskZone riskZone = new RiskZone(logger);
        Refuge refuge = new Refuge(logger);
        Tunnels tunnels = new Tunnels(riskZone,logger, pm);
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
             
        new Zombie(riskZone, logger, pm).start();  // Patient zero
        
        Runnable hr = new Runnable()
        {
            public void run()
            {
                try 
                {
                    for (int i = 1; i < 10000; i++) 
                    {
                        new Human(i, refuge, tunnels, logger, pm).start();
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
   
    public static PauseManager getPM()
    {
        return pm;
    }
    
    public static MapPage getMapPage() 
    {
    return mapPage;
    }
     
    public static Logger getLogger()
    {
        return logger;
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


