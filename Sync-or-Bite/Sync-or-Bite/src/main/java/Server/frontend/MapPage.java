/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Server.frontend;

import Server.backend.ChangeListener;
import Server.backend.CommonArea;
import Server.backend.DiningRoom;
import Server.backend.Human;
import Server.backend.PauseManager;
import Server.backend.Refuge;
import Server.backend.RestArea;
import Server.backend.RiskZone;
import Server.backend.Tunnel;
import Server.backend.Tunnels;
import Server.backend.UnsafeArea;
import Server.backend.Zombie;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * MapPage is a JPanel that visually represents the state of the game.
 * It displays the current state of various areas (Risk Zones, Refuge, Tunnels, etc.)
 * and dynamically updates the UI based on changes in the game state.
 * 
 * Features:
 * - Displays humans and zombies in different areas.
 * - Dynamically updates counters and panels when the game state changes.
 * - Supports resizing to adjust the layout of the panels.
 * - Provides controls to pause/resume the game and navigate to the log page.
 * 
 * @author guill
 */
public class MapPage extends javax.swing.JPanel 
{

    // Icons for pause and resume button states
    private ImageIcon pauseIcon= new ImageIcon(getClass().getResource( "/images/PauseIcon.png" ));
    private ImageIcon resumeIcon= new ImageIcon(getClass().getResource( "/images/ResumeIcon.png" ));
    
    // PauseManager instance to manage pause/resume state
    private PauseManager  pm;
    
    // Maps to store UI components and their states
    private final Map<String, JLabel> counters = new HashMap<>();
    private final Map<String, JPanel> panels = new HashMap<>();
    private final Map<String, Set<String>> currentPanelState = new HashMap<>();
    
    
    // Backend components for different areas of the system
    private Refuge r = ServerApp.getR();
    private CommonArea ca = ServerApp.getCA();
    private RestArea ra = ServerApp.getRA();
    private DiningRoom dr = ServerApp.getDR();
    private RiskZone rz = ServerApp.getRZ();
    private Tunnels t = ServerApp.getT();
    
    /**
     * Constructor for MapPage.
     * Initializes the UI components, sets up listeners for simulation state changes,
     * and configures the layout for dynamic resizing.
     */
    public MapPage() 
    {
        initComponents();
        
        // Retrieve the PauseManager instance from ServerApp
        this.pm = ServerApp.getPM();
        
        // Initialize counters and panels in their respective maps
        setupCounters();
        setupPanels();
        
        // Set up a listener to update the pause/resume button icon based on the pause state
        pm.setPauseStateListener(new Runnable() 
        {
            @Override
            public void run() 
            {
                ImageIcon current = pm.isPaused() ? resumeIcon : pauseIcon;
                pauseResumeButton.setIcon(current);
            }
        });
        
        // Set up listeners for changes in the simulation state
        setupListeners();        
    }
    
    /**
     * Enables automatic resizing of panels based on the size of the MapPage.
     * Adjusts the width of panels dynamically to fit the available space.
     */
    public void enableAutoResize() 
    {
        this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {
                int totalWidth = getWidth(); // Visible width of MapPage

                for (Map.Entry<String, JPanel> entry : panels.entrySet()) 
                {
                    String key = entry.getKey();
                    JPanel panel = entry.getValue();

                    int maxWidth;

                    // Determine the maximum width for each panel based on its type
                    if (key.startsWith("RH") || key.startsWith("RZ")) 
                    {
                        maxWidth = totalWidth / 8;
                    } 
                    else if (key.startsWith("TR") || key.startsWith("TE")) 
                    {
                        maxWidth = totalWidth / 4;
                    } 
                    else if (key.equals("C") || key.equals("D") || key.equals("R")) 
                    {
                        maxWidth = totalWidth / 3;
                    } else 
                    {
                        continue;
                    }

                    // Update the panel's size
                    Dimension size = panel.getPreferredSize();
                    panel.setMaximumSize(new Dimension(maxWidth, size.height));
                    panel.setPreferredSize(new Dimension(maxWidth,size.height));
                    panel.revalidate();
                }

                revalidate();
                repaint();
            }
        });
    }
    
    /**
     * Sets up listeners for changes in the game state.
     * Updates the UI dynamically when changes occur in the backend components.
     */
    private void setupListeners() 
    {
        ChangeListener masterChangeListener = new ChangeListener() 
        {
            @Override
            public void onChange(Object source) 
            {
                switch (source) 
                {
                    case CommonArea ca -> 
                    {
                        // Handle changes in the CommonArea
                        updatePanel("C", ca.getHumansObjInside().stream()
                            .map(Human::getHumanId)
                            .toList());
                        
                        // Update label colors based on human states
                        for (Human human : ca.getHumansObjInside()) 
                        {
                            if (human.isBeingAttacked()) 
                            {
                                setLabelColorInPanel("C", human.getHumanId(), utils.ColorManager.ATTACKED_COLOR);
                            } 
                            else if (human.isMarked())
                            {
                                setLabelColorInPanel("C", human.getHumanId(), utils.ColorManager.INJURED_COLOR);  
                            }
                            else
                            {
                                setLabelColorInPanel("C", human.getHumanId(), utils.ColorManager.HUMAN_COLOR);
                            }
                        }
                        
                        // Update counters for the CommonArea
                        setCounter("HC", String.valueOf(ca.getHumansInside()));
                        setCounter("RC", String.valueOf(r.getCount()));
                    }

                    case RestArea ra -> 
                    {
                        // Handle changes in the RestArea
                        updatePanel("R", ra.getHumansObjInside().stream()
                            .map(Human::getHumanId)
                            .toList());
                        
                        // Update label colors based on human states
                        for (Human human : ra.getHumansObjInside()) 
                        {
                            if (human.isBeingAttacked()) 
                            {
                                setLabelColorInPanel("R", human.getHumanId(), utils.ColorManager.ATTACKED_COLOR);
                            } 
                            else if (human.isMarked())
                            {
                                setLabelColorInPanel("R", human.getHumanId(), utils.ColorManager.INJURED_COLOR);  
                            }
                            else
                            {
                                setLabelColorInPanel("R", human.getHumanId(), utils.ColorManager.HUMAN_COLOR);
                            }
                        }
                        
                        // Update counters for the RestArea
                        setCounter("HR", String.valueOf(ra.getHumansInside()));
                        setCounter("RC", String.valueOf(r.getCount()));
                    }

                    case DiningRoom dr -> 
                    {
                        // Handle changes in the DiningRoom
                        updatePanel("D", dr.getHumansObjInside().stream()
                            .map(Human::getHumanId)
                            .toList());
                        
                        // Update label colors based on human states
                        for (Human human : dr.getHumansObjInside()) 
                        {
                            if (human.isBeingAttacked()) 
                            {
                                setLabelColorInPanel("D", human.getHumanId(), utils.ColorManager.ATTACKED_COLOR);
                            } 
                            else if (human.isMarked())
                            {
                                setLabelColorInPanel("D", human.getHumanId(), utils.ColorManager.INJURED_COLOR);  
                            }
                            else
                            {
                                setLabelColorInPanel("D", human.getHumanId(), utils.ColorManager.HUMAN_COLOR);
                            }
                        }    
                        
                        // Update counters for the DiningRoom
                        setCounter("HD", String.valueOf(dr.getHumansInside()));
                        setCounter("FC", String.valueOf(dr.getFoodCount()));
                        setCounter("RC", String.valueOf(r.getCount()));
                    }

                    case UnsafeArea area -> 
                    {
                        // Handle changes in UnsafeArea
                        int index = rz.getIndexOfUnsafeArea(area);
                        String humansPanelId = "RH" + String.valueOf(index + 1);
                        String zombiesPanelId = "RZ" + String.valueOf(index + 1);

                        // Update humans in the UnsafeArea
                        updatePanel(humansPanelId, area.getHumansInside().stream()
                            .map(Human::getHumanId)
                            .toList());
                        
                        // Update label colors based on human states
                        for (Human human : area.getHumansInside()) 
                        {
                            if (human.isBeingAttacked()) 
                            {
                                setLabelColorInPanel(humansPanelId, human.getHumanId(), utils.ColorManager.ATTACKED_COLOR);
                            } 
                            else if (human.isMarked())
                            {
                                setLabelColorInPanel(humansPanelId, human.getHumanId(), utils.ColorManager.INJURED_COLOR);  
                            }
                            else
                            {
                                setLabelColorInPanel(humansPanelId, human.getHumanId(), utils.ColorManager.HUMAN_COLOR);
                            }
                        }

                        // Update zombies in the UnsafeArea
                        updatePanel(zombiesPanelId, area.getZombiesInside().stream()
                            .map(Zombie::getZombieId)
                            .toList());
                        
                        // Update label colors based on zombie states
                        for (Zombie zombie : area.getZombiesInside()) 
                        {
                            if (zombie.isAttacking()) 
                            {
                                setLabelColorInPanel(zombiesPanelId, zombie.getZombieId(), utils.ColorManager.ATTACKING_COLOR);  
                            } 
                            else 
                            {
                                setLabelColorInPanel(zombiesPanelId, zombie.getZombieId(), utils.ColorManager.ZOMBIE_COLOR);  
                            }
                        }

                        // Update counters for the UnsafeArea
                        setCounter("H" + String.valueOf(index + 1), String.valueOf(area.getHumansInsideCount()));
                        setCounter("Z" + String.valueOf(index + 1), String.valueOf(area.getZombiesInsideCount()));

                    }

                    case Tunnel tunnel -> 
                    {
                        // Handle changes in Tunnel
                        int index = t.getIndexOfTunnel(tunnel);
                        String entryPanel = "TR" + String.valueOf(index + 1);
                        String exitPanel = "TE" + String.valueOf(index + 1);
                        
                        try
                        {
                            Queue<Human> entering = tunnel.getEntering();
                            Queue<Human> exiting  = tunnel.getExiting();
                            String crossing = tunnel.getInTunnel();

                            // Update humans returning to refuge
                            updatePanel(entryPanel, entering.stream()
                           .map(Human::getHumanId)
                           .toList());

                            // Update label colors based on human states
                            for (Human human : entering) 
                            {
                                if (human.isMarked()) 
                                {
                                    setLabelColorInPanel(entryPanel, human.getHumanId(), utils.ColorManager.INJURED_COLOR);
                                } 
                            }

                            // Update humans exiting from refuge
                            updatePanel(exitPanel, exiting.stream()
                                .map(Human::getHumanId)
                                .toList());

                            // Update label colors based on human states
                            for (Human human : exiting) 
                            {
                                if (human.isWaiting()) 
                                {
                                    setLabelColorInPanel(exitPanel, human.getHumanId(), utils.ColorManager.WAITING4GROUP_COLOR);
                                } 
                                else
                                {
                                    setLabelColorInPanel(exitPanel, human.getHumanId(), utils.ColorManager.HUMAN_COLOR);
                                }
                            }
                            
                            // Update the crossing human state in the correct tunnel
                            switch (index + 1) 
                            {
                                case 1 -> currentCrossing1.setText(crossing);
                                case 2 -> currentCrossing2.setText(crossing);
                                case 3 -> currentCrossing3.setText(crossing);
                                case 4 -> currentCrossing4.setText(crossing);
                            }
                            
                            setCounter("RC", String.valueOf(r.getCount()));
                        } 
                        catch (InterruptedException ex) 
                        {
                            Logger.getLogger(MapPage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    default -> 
                    {
                        System.err.println("Unknown source for change: " + source);
                    }
                }
            }

        };
        
        // Add the master listener to backend components
        ca.addChangeListener(masterChangeListener);
        dr.addChangeListener(masterChangeListener);
        ra.addChangeListener(masterChangeListener);

        for (int i = 0; i < 4; i++) 
        {
            rz.obtainUnsafeArea(i).addChangeListener(masterChangeListener);
            t.obtainTunnel(i).addChangeListener(masterChangeListener);
        }

    }

    /**
    * Sets up the mapping between panel keys and their corresponding JPanel instances.
    * This mapping is used to dynamically update the UI when the game state changes.
    */
    private void setupPanels() 
    {
         panels.put("RH1", RiskHuman1);
         panels.put("RH2", RiskHuman2);
         panels.put("RH3", RiskHuman3);   
         panels.put("RH4", RiskHuman4);
         panels.put("RZ1", RiskZombie1);
         panels.put("RZ2", RiskZombie2);
         panels.put("RZ3", RiskZombie3);
         panels.put("RZ4", RiskZombie4);
         panels.put("TR1", TunnelReturning1);
         panels.put("TR2", TunnelReturning2);
         panels.put("TR3", TunnelReturning3);
         panels.put("TR4", TunnelReturning4);
         panels.put("TE1", TunnelExiting1);
         panels.put("TE2", TunnelExiting2);
         panels.put("TE3", TunnelExiting3);
         panels.put("TE4", TunnelExiting4);
         panels.put("C", CommonList);
         panels.put("D", DiningList);
         panels.put("R", RestList);
    }
   
    /**
    * Sets up the mapping between counter keys and their corresponding JLabel instances.
    * This mapping is used to update counters dynamically when the system state changes.
    */
    private void setupCounters() 
    {
        counters.put("H1", HumanCounter1);
        counters.put("H2", HumanCounter2); 
        counters.put("H3", HumanCounter3); 
        counters.put("H4", HumanCounter4); 
        counters.put("HC", commonHumanCounter);
        counters.put("HD", humanDiningCounter);
        counters.put("HR", restHumanCounter);
        counters.put("Z1", ZombieCounter1);
        counters.put("Z2", ZombieCounter2); 
        counters.put("Z3", ZombieCounter3); 
        counters.put("Z4", ZombieCounter4); 
        counters.put("C1", currentCrossing1);
        counters.put("C2", currentCrossing2); 
        counters.put("C3", currentCrossing3); 
        counters.put("C4", currentCrossing4); 
        counters.put("FC",foodCounter);
        counters.put("RC",refugeCounter);
    }
    
    /**
    * Updates the content of a panel based on the provided list of IDs.
    * Adds or removes labels as needed to match the new state.
    * 
    * @param panelId The key of the panel to update.
    * @param newIds The list of IDs to display in the panel.
    */
    private synchronized void updatePanel(String panelId, List<String> newIds) 
    {
        Set<String> currentIds = currentPanelState.getOrDefault(panelId, new HashSet<>());
        Set<String> updatedIds = new HashSet<>(newIds);

        // Compare what should be and what is there, delete what shouldn't be there
        for (String id : new HashSet<>(currentIds)) 
        {
            if (!updatedIds.contains(id)) 
            {
                removeLabelFromPanel(panelId, id);
                currentIds.remove(id);
            }
        }

        // Compare what is there and what should be, add what should be there
        for (String id : updatedIds) 
        {
            if (!currentIds.contains(id)) 
            {
                addLabelToPanel(panelId, id);
                currentIds.add(id);
            }
        }

        currentPanelState.put(panelId, currentIds);
    }

    
    /**
     * Toggles the pause state of the game and updates the pause/resume button icon.
     */
    public void pauseResume()
    {
        pm.togglePause();
        ImageIcon current = pm.isPaused() ? resumeIcon : pauseIcon;
        pauseResumeButton.setIcon(current);
    }
   
    
    /**
     * Adds a new label to the specified panel.
     * 
     * @param panelKey The key of the panel where the label should be added.
     * @param labelText The text to display on the label.
     */
    public synchronized void addLabelToPanel(String panelKey, String labelText)
    {
       
        JPanel targetPanel = panels.get(panelKey);

        if (targetPanel == null) {
            System.err.println("No panel found for key: " + panelKey);
            return;
        }

        JLabel label = new JLabel(labelText);
        label.setOpaque(true);
        Color background = labelText.startsWith("H")? utils.ColorManager.HUMAN_COLOR : utils.ColorManager.ZOMBIE_COLOR;
        label.setBackground(background); 
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); 

        targetPanel.add(label);
        updatePanelPreferredHeight(targetPanel);
        
        targetPanel.revalidate();
        targetPanel.repaint();
    }
    
    /**
     * Removes a label with the specified text from the given panel.
     * 
     * @param panelKey The key of the panel from which the label should be removed.
     * @param labelText The text of the label to remove.
     */
    public synchronized void removeLabelFromPanel(String panelKey, String labelText) 
    {
        JPanel targetPanel = panels.get(panelKey);

        if (targetPanel == null) 
        {
            System.err.println("No panel found for key: " + panelKey);
            return;
        }

        Component[] components = targetPanel.getComponents();
        for (Component comp : components) 
        {
            if (comp instanceof JLabel label && label.getText().equals(labelText)) 
            {
                targetPanel.remove(label);
                updatePanelPreferredHeight(targetPanel);
                targetPanel.revalidate();
                targetPanel.repaint();
                return;
            }
        }

        System.out.println("Label with text '" + labelText + "' not found in panel " + panelKey);
    }
    
    public synchronized void setLabelColorInPanel(String panelKey, String labelText, Color color) 
    {
        JPanel targetPanel = panels.get(panelKey);

        if (targetPanel == null)
        {
            System.err.println("No panel found for key: " + panelKey);
            return;
        }

        for (Component comp : targetPanel.getComponents()) 
        {
            if (comp instanceof JLabel label && label.getText().equals(labelText)) 
            {
                label.setOpaque(true); 
                label.setBackground(color);
                targetPanel.revalidate();
                targetPanel.repaint();
                return;
            }
        }

        System.out.println("Label with text '" + labelText + "' not found in panel " + panelKey);
    }

    private void updatePanelPreferredHeight(JPanel panel) 
    {
    int rowCount = getFlowLayoutRowCount(panel);
    int labelHeight = 30; // approximate height per row
    int padding = 10;

    Dimension currentSize = panel.getPreferredSize();
    panel.setPreferredSize(new Dimension(currentSize.width, rowCount * (labelHeight + padding/2)));
    panel.revalidate();
    }

    public synchronized void setCounter(String nameLabel, String value) 
    {
        counters.get(nameLabel).setText(value);
    }
    
    private int getFlowLayoutRowCount(JPanel panel) 
    {
    int panelWidth = panel.getWidth();
    if (panelWidth == 0) 
    {
        panelWidth = panel.getPreferredSize().width;
    }

    int x = 0;
    int rows = 1; // At least one row if there's any content
    int hgap = ((FlowLayout) panel.getLayout()).getHgap();

    for (Component comp : panel.getComponents()) 
    {
        if (comp != null) 
        {  
            Dimension d = comp.getPreferredSize();
            if (x + d.width > panelWidth) 
            {
                rows++;
                x = 0;
            }
            x += d.width + hgap;
        }
    }

    return rows;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        pauseResumeButton = new javax.swing.JButton();
        logsButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        riskZonesPanel = new javax.swing.JPanel();
        Risk1 = new javax.swing.JPanel();
        riskZoneInfo1 = new javax.swing.JPanel();
        commonHumanIcon3 = new javax.swing.JButton();
        HumanCounter1 = new javax.swing.JLabel();
        ZombieIcon1 = new javax.swing.JButton();
        ZombieCounter1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        riskZonePanel1 = new javax.swing.JScrollPane();
        RiskHuman1 = new javax.swing.JPanel();
        riskZonePanel3 = new javax.swing.JScrollPane();
        RiskZombie1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        Risk2 = new javax.swing.JPanel();
        riskZoneInfo2 = new javax.swing.JPanel();
        commonHumanIcon4 = new javax.swing.JButton();
        HumanCounter2 = new javax.swing.JLabel();
        ZombieIcon5 = new javax.swing.JButton();
        ZombieCounter2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        riskZonePanel4 = new javax.swing.JScrollPane();
        RiskHuman2 = new javax.swing.JPanel();
        riskZonePanel7 = new javax.swing.JScrollPane();
        RiskZombie2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        Risk3 = new javax.swing.JPanel();
        riskZoneInfo3 = new javax.swing.JPanel();
        commonHumanIcon5 = new javax.swing.JButton();
        HumanCounter3 = new javax.swing.JLabel();
        ZombieIcon6 = new javax.swing.JButton();
        ZombieCounter3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        riskZonePanel8 = new javax.swing.JScrollPane();
        RiskHuman3 = new javax.swing.JPanel();
        riskZonePanel9 = new javax.swing.JScrollPane();
        RiskZombie3 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        Risk4 = new javax.swing.JPanel();
        riskZoneInfo4 = new javax.swing.JPanel();
        commonHumanIcon6 = new javax.swing.JButton();
        HumanCounter4 = new javax.swing.JLabel();
        ZombieIcon7 = new javax.swing.JButton();
        ZombieCounter4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        riskZonePanel10 = new javax.swing.JScrollPane();
        RiskHuman4 = new javax.swing.JPanel();
        riskZonePanel11 = new javax.swing.JScrollPane();
        RiskZombie4 = new javax.swing.JPanel();
        tunnelsPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        tunnelPanel1 = new javax.swing.JScrollPane();
        TunnelReturning1 = new javax.swing.JPanel();
        currentCrossing1 = new javax.swing.JLabel();
        tunnelPanel6 = new javax.swing.JScrollPane();
        TunnelExiting1 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();
        tunnelPanel2 = new javax.swing.JScrollPane();
        TunnelReturning2 = new javax.swing.JPanel();
        currentCrossing2 = new javax.swing.JLabel();
        tunnelPanel16 = new javax.swing.JScrollPane();
        TunnelExiting2 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel10 = new javax.swing.JPanel();
        tunnelPanel3 = new javax.swing.JScrollPane();
        TunnelReturning3 = new javax.swing.JPanel();
        currentCrossing3 = new javax.swing.JLabel();
        tunnelPanel17 = new javax.swing.JScrollPane();
        TunnelExiting3 = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        tunnelPanel4 = new javax.swing.JScrollPane();
        TunnelReturning4 = new javax.swing.JPanel();
        currentCrossing4 = new javax.swing.JLabel();
        tunnelPanel18 = new javax.swing.JScrollPane();
        TunnelExiting4 = new javax.swing.JPanel();
        refuge = new javax.swing.JPanel();
        refugePanel = new javax.swing.JPanel();
        commonAreaPanel1 = new javax.swing.JPanel();
        commonAreaPanel = new javax.swing.JScrollPane();
        CommonList = new javax.swing.JPanel();
        commonAreainfo = new javax.swing.JPanel();
        commonHumanIcon = new javax.swing.JButton();
        commonHumanCounter = new javax.swing.JLabel();
        diningRoomPanel1 = new javax.swing.JPanel();
        diningRoomPanel = new javax.swing.JScrollPane();
        DiningList = new javax.swing.JPanel();
        diningRoomInfo = new javax.swing.JPanel();
        commonHumanIcon1 = new javax.swing.JButton();
        humanDiningCounter = new javax.swing.JLabel();
        foodIcon = new javax.swing.JButton();
        foodCounter = new javax.swing.JLabel();
        restAreaPanel1 = new javax.swing.JPanel();
        restAreaPanel = new javax.swing.JScrollPane();
        RestList = new javax.swing.JPanel();
        restAreaInfo = new javax.swing.JPanel();
        commonHumanIcon2 = new javax.swing.JButton();
        restHumanCounter = new javax.swing.JLabel();
        refugeInfo = new javax.swing.JPanel();
        refugeName = new javax.swing.JLabel();
        refugeCounters = new javax.swing.JPanel();
        refugeIcon = new javax.swing.JButton();
        refugeCounter = new javax.swing.JLabel();

        setBackground(utils.ColorManager.BG_COLOR);
        setForeground(utils.ColorManager.BG_COLOR);
        setLayout(new java.awt.BorderLayout());

        buttonPanel.setForeground(utils.ColorManager.BG_COLOR);
        buttonPanel.setLayout(new java.awt.BorderLayout());

        pauseResumeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PauseIcon.png"))); // NOI18N
        pauseResumeButton.setBorderPainted(false);
        pauseResumeButton.setContentAreaFilled(false);
        pauseResumeButton.setMaximumSize(new java.awt.Dimension(50, 51));
        pauseResumeButton.setMinimumSize(new java.awt.Dimension(50, 51));
        pauseResumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseResumeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(pauseResumeButton, java.awt.BorderLayout.LINE_START);

        logsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LogsIcon.png"))); // NOI18N
        logsButton.setBorderPainted(false);
        logsButton.setContentAreaFilled(false);
        logsButton.setMaximumSize(new java.awt.Dimension(50, 51));
        logsButton.setMinimumSize(new java.awt.Dimension(50, 51));
        logsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logsButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(logsButton, java.awt.BorderLayout.LINE_END);

        add(buttonPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setForeground(utils.ColorManager.BG_COLOR);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        riskZonesPanel.setForeground(utils.ColorManager.BG_COLOR);
        riskZonesPanel.setLayout(new javax.swing.BoxLayout(riskZonesPanel, javax.swing.BoxLayout.LINE_AXIS));

        Risk1.setForeground(utils.ColorManager.BG_COLOR);
        Risk1.setLayout(new java.awt.BorderLayout());

        riskZoneInfo1.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon3.setBorderPainted(false);
        commonHumanIcon3.setContentAreaFilled(false);
        commonHumanIcon3.setEnabled(false);
        commonHumanIcon3.setFocusPainted(false);
        commonHumanIcon3.setFocusable(false);
        commonHumanIcon3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon3ActionPerformed(evt);
            }
        });
        riskZoneInfo1.add(commonHumanIcon3);

        HumanCounter1.setFont(utils.FontManager.regularFont);
        HumanCounter1.setText("0");
        riskZoneInfo1.add(HumanCounter1);

        ZombieIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ZombieIcon.png"))); // NOI18N
        ZombieIcon1.setBorderPainted(false);
        ZombieIcon1.setContentAreaFilled(false);
        ZombieIcon1.setEnabled(false);
        ZombieIcon1.setFocusPainted(false);
        ZombieIcon1.setFocusable(false);
        ZombieIcon1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZombieIcon1ActionPerformed(evt);
            }
        });
        riskZoneInfo1.add(ZombieIcon1);

        ZombieCounter1.setFont(utils.FontManager.regularFont);
        ZombieCounter1.setText("0");
        riskZoneInfo1.add(ZombieCounter1);

        Risk1.add(riskZoneInfo1, java.awt.BorderLayout.NORTH);

        jPanel1.setForeground(utils.ColorManager.BG_COLOR);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel1.setForeground(utils.ColorManager.BG_COLOR);
        riskZonePanel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel1.setViewportView(RiskHuman1);

        jPanel1.add(riskZonePanel1);

        riskZonePanel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel3.setViewportView(RiskZombie1);

        jPanel1.add(riskZonePanel3);

        Risk1.add(jPanel1, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk1);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator1);

        Risk2.setForeground(utils.ColorManager.BG_COLOR);
        Risk2.setLayout(new java.awt.BorderLayout());

        riskZoneInfo2.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon4.setBorderPainted(false);
        commonHumanIcon4.setContentAreaFilled(false);
        commonHumanIcon4.setEnabled(false);
        commonHumanIcon4.setFocusPainted(false);
        commonHumanIcon4.setFocusable(false);
        commonHumanIcon4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon4ActionPerformed(evt);
            }
        });
        riskZoneInfo2.add(commonHumanIcon4);

        HumanCounter2.setFont(utils.FontManager.regularFont);
        HumanCounter2.setText("0");
        riskZoneInfo2.add(HumanCounter2);

        ZombieIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ZombieIcon.png"))); // NOI18N
        ZombieIcon5.setBorderPainted(false);
        ZombieIcon5.setContentAreaFilled(false);
        ZombieIcon5.setEnabled(false);
        ZombieIcon5.setFocusPainted(false);
        ZombieIcon5.setFocusable(false);
        ZombieIcon5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZombieIcon5ActionPerformed(evt);
            }
        });
        riskZoneInfo2.add(ZombieIcon5);

        ZombieCounter2.setFont(utils.FontManager.regularFont);
        ZombieCounter2.setText("0");
        riskZoneInfo2.add(ZombieCounter2);

        Risk2.add(riskZoneInfo2, java.awt.BorderLayout.NORTH);

        jPanel2.setForeground(utils.ColorManager.BG_COLOR);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel4.setViewportView(RiskHuman2);

        jPanel2.add(riskZonePanel4);

        riskZonePanel7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel7.setViewportView(RiskZombie2);

        jPanel2.add(riskZonePanel7);

        Risk2.add(jPanel2, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk2);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator2);

        Risk3.setForeground(utils.ColorManager.BG_COLOR);
        Risk3.setLayout(new java.awt.BorderLayout());

        riskZoneInfo3.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon5.setBorderPainted(false);
        commonHumanIcon5.setContentAreaFilled(false);
        commonHumanIcon5.setEnabled(false);
        commonHumanIcon5.setFocusPainted(false);
        commonHumanIcon5.setFocusable(false);
        commonHumanIcon5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon5ActionPerformed(evt);
            }
        });
        riskZoneInfo3.add(commonHumanIcon5);

        HumanCounter3.setFont(utils.FontManager.regularFont);
        HumanCounter3.setText("0");
        riskZoneInfo3.add(HumanCounter3);

        ZombieIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ZombieIcon.png"))); // NOI18N
        ZombieIcon6.setBorderPainted(false);
        ZombieIcon6.setContentAreaFilled(false);
        ZombieIcon6.setEnabled(false);
        ZombieIcon6.setFocusPainted(false);
        ZombieIcon6.setFocusable(false);
        ZombieIcon6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZombieIcon6ActionPerformed(evt);
            }
        });
        riskZoneInfo3.add(ZombieIcon6);

        ZombieCounter3.setFont(utils.FontManager.regularFont);
        ZombieCounter3.setText("0");
        riskZoneInfo3.add(ZombieCounter3);

        Risk3.add(riskZoneInfo3, java.awt.BorderLayout.NORTH);

        jPanel3.setForeground(utils.ColorManager.BG_COLOR);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel8.setViewportView(RiskHuman3);

        jPanel3.add(riskZonePanel8);

        riskZonePanel9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel9.setViewportView(RiskZombie3);

        jPanel3.add(riskZonePanel9);

        Risk3.add(jPanel3, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk3);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator3);

        Risk4.setForeground(utils.ColorManager.BG_COLOR);
        Risk4.setLayout(new java.awt.BorderLayout());

        riskZoneInfo4.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon6.setBorderPainted(false);
        commonHumanIcon6.setContentAreaFilled(false);
        commonHumanIcon6.setEnabled(false);
        commonHumanIcon6.setFocusPainted(false);
        commonHumanIcon6.setFocusable(false);
        commonHumanIcon6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon6ActionPerformed(evt);
            }
        });
        riskZoneInfo4.add(commonHumanIcon6);

        HumanCounter4.setFont(utils.FontManager.regularFont);
        HumanCounter4.setText("0");
        riskZoneInfo4.add(HumanCounter4);

        ZombieIcon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ZombieIcon.png"))); // NOI18N
        ZombieIcon7.setBorderPainted(false);
        ZombieIcon7.setContentAreaFilled(false);
        ZombieIcon7.setEnabled(false);
        ZombieIcon7.setFocusPainted(false);
        ZombieIcon7.setFocusable(false);
        ZombieIcon7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZombieIcon7ActionPerformed(evt);
            }
        });
        riskZoneInfo4.add(ZombieIcon7);

        ZombieCounter4.setFont(utils.FontManager.regularFont);
        ZombieCounter4.setText("0");
        riskZoneInfo4.add(ZombieCounter4);

        Risk4.add(riskZoneInfo4, java.awt.BorderLayout.NORTH);

        jPanel4.setForeground(utils.ColorManager.BG_COLOR);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel10.setViewportView(RiskHuman4);

        jPanel4.add(riskZonePanel10);

        riskZonePanel11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riskZonePanel11.setViewportView(RiskZombie4);

        jPanel4.add(riskZonePanel11);

        Risk4.add(jPanel4, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk4);

        mainPanel.add(riskZonesPanel);

        tunnelsPanel.setForeground(utils.ColorManager.BG_COLOR);
        tunnelsPanel.setLayout(new javax.swing.BoxLayout(tunnelsPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setForeground(utils.ColorManager.BG_COLOR);
        jPanel5.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel1.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning1.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel1.setViewportView(TunnelReturning1);

        jPanel5.add(tunnelPanel1);

        currentCrossing1.setFont(utils.FontManager.boldFont);
        currentCrossing1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing1.setText("-----");
        currentCrossing1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel5.add(currentCrossing1);

        tunnelPanel6.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting1.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel6.setViewportView(TunnelExiting1);

        jPanel5.add(tunnelPanel6);

        tunnelsPanel.add(jPanel5);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator4);

        jPanel9.setForeground(utils.ColorManager.BG_COLOR);
        jPanel9.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel2.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning2.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel2.setViewportView(TunnelReturning2);

        jPanel9.add(tunnelPanel2);

        currentCrossing2.setFont(utils.FontManager.boldFont);
        currentCrossing2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing2.setText("-----");
        jPanel9.add(currentCrossing2);

        tunnelPanel16.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting2.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel16.setViewportView(TunnelExiting2);

        jPanel9.add(tunnelPanel16);

        tunnelsPanel.add(jPanel9);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator5);

        jPanel10.setForeground(utils.ColorManager.BG_COLOR);
        jPanel10.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel3.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning3.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel3.setViewportView(TunnelReturning3);

        jPanel10.add(tunnelPanel3);

        currentCrossing3.setFont(utils.FontManager.boldFont);
        currentCrossing3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing3.setText("-----");
        jPanel10.add(currentCrossing3);

        tunnelPanel17.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel17.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting3.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel17.setViewportView(TunnelExiting3);

        jPanel10.add(tunnelPanel17);

        tunnelsPanel.add(jPanel10);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator6);

        jPanel11.setForeground(utils.ColorManager.BG_COLOR);
        jPanel11.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel4.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning4.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel4.setViewportView(TunnelReturning4);

        jPanel11.add(tunnelPanel4);

        currentCrossing4.setFont(utils.FontManager.boldFont);
        currentCrossing4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing4.setText("-----");
        jPanel11.add(currentCrossing4);

        tunnelPanel18.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting4.setForeground(utils.ColorManager.BG_COLOR);
        tunnelPanel18.setViewportView(TunnelExiting4);

        jPanel11.add(tunnelPanel18);

        tunnelsPanel.add(jPanel11);

        mainPanel.add(tunnelsPanel);

        refuge.setForeground(utils.ColorManager.BG_COLOR);
        refuge.setLayout(new java.awt.BorderLayout());

        refugePanel.setForeground(utils.ColorManager.BG_COLOR);
        refugePanel.setLayout(new javax.swing.BoxLayout(refugePanel, javax.swing.BoxLayout.X_AXIS));

        commonAreaPanel1.setForeground(utils.ColorManager.BG_COLOR);
        commonAreaPanel1.setMaximumSize(new java.awt.Dimension(550, 2147483647));
        commonAreaPanel1.setLayout(new java.awt.BorderLayout());

        commonAreaPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commonAreaPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Common Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont, utils.ColorManager.TEXT_COLOR));
        commonAreaPanel.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        commonAreaPanel.setViewportView(CommonList);

        commonAreaPanel1.add(commonAreaPanel, java.awt.BorderLayout.CENTER);

        commonAreainfo.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon.setBorderPainted(false);
        commonHumanIcon.setContentAreaFilled(false);
        commonHumanIcon.setEnabled(false);
        commonHumanIcon.setFocusPainted(false);
        commonHumanIcon.setFocusable(false);
        commonHumanIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIconActionPerformed(evt);
            }
        });
        commonAreainfo.add(commonHumanIcon);

        commonHumanCounter.setFont(utils.FontManager.regularFont);
        commonHumanCounter.setText("0");
        commonAreainfo.add(commonHumanCounter);

        commonAreaPanel1.add(commonAreainfo, java.awt.BorderLayout.SOUTH);

        refugePanel.add(commonAreaPanel1);

        diningRoomPanel1.setForeground(utils.ColorManager.BG_COLOR);
        diningRoomPanel1.setMaximumSize(new java.awt.Dimension(550, 2147483647));
        diningRoomPanel1.setLayout(new java.awt.BorderLayout());

        diningRoomPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        diningRoomPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dining Room", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont, utils.ColorManager.TEXT_COLOR));
        diningRoomPanel.setViewportView(DiningList);

        diningRoomPanel1.add(diningRoomPanel, java.awt.BorderLayout.CENTER);
        diningRoomPanel.setViewportView(DiningList);

        diningRoomInfo.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon1.setBorderPainted(false);
        commonHumanIcon1.setContentAreaFilled(false);
        commonHumanIcon1.setEnabled(false);
        commonHumanIcon1.setFocusPainted(false);
        commonHumanIcon1.setFocusable(false);
        commonHumanIcon1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon1ActionPerformed(evt);
            }
        });
        diningRoomInfo.add(commonHumanIcon1);

        humanDiningCounter.setFont(utils.FontManager.regularFont);
        humanDiningCounter.setText("0");
        diningRoomInfo.add(humanDiningCounter);

        foodIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/DiningRoomIcon.png"))); // NOI18N
        foodIcon.setBorderPainted(false);
        foodIcon.setContentAreaFilled(false);
        foodIcon.setEnabled(false);
        foodIcon.setFocusPainted(false);
        foodIcon.setFocusable(false);
        foodIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodIconActionPerformed(evt);
            }
        });
        diningRoomInfo.add(foodIcon);

        foodCounter.setFont(utils.FontManager.regularFont);
        foodCounter.setText("0");
        diningRoomInfo.add(foodCounter);

        diningRoomPanel1.add(diningRoomInfo, java.awt.BorderLayout.SOUTH);

        refugePanel.add(diningRoomPanel1);

        restAreaPanel1.setForeground(utils.ColorManager.BG_COLOR);
        restAreaPanel1.setMaximumSize(new java.awt.Dimension(550, 2147483647));
        restAreaPanel1.setLayout(new java.awt.BorderLayout());

        restAreaPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        restAreaPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rest Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont, utils.ColorManager.TEXT_COLOR));
        restAreaPanel.setViewportView(RestList);

        restAreaPanel1.add(restAreaPanel, java.awt.BorderLayout.CENTER);

        restAreaInfo.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HumanIcon.png"))); // NOI18N
        commonHumanIcon2.setBorderPainted(false);
        commonHumanIcon2.setContentAreaFilled(false);
        commonHumanIcon2.setEnabled(false);
        commonHumanIcon2.setFocusPainted(false);
        commonHumanIcon2.setFocusable(false);
        commonHumanIcon2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commonHumanIcon2ActionPerformed(evt);
            }
        });
        restAreaInfo.add(commonHumanIcon2);

        restHumanCounter.setFont(utils.FontManager.regularFont);
        restHumanCounter.setText("0");
        restAreaInfo.add(restHumanCounter);

        restAreaPanel1.add(restAreaInfo, java.awt.BorderLayout.SOUTH);

        refugePanel.add(restAreaPanel1);

        refuge.add(refugePanel, java.awt.BorderLayout.CENTER);

        refugeInfo.setForeground(utils.ColorManager.BG_COLOR);
        refugeInfo.setLayout(new java.awt.BorderLayout());

        refugeName.setFont(utils.FontManager.titleFont);
        refugeName.setText("Refuge");
        refugeName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        refugeInfo.add(refugeName, java.awt.BorderLayout.WEST);

        refugeCounters.setForeground(utils.ColorManager.BG_COLOR);
        refugeCounters.setLayout(new java.awt.GridLayout(1, 2));

        refugeIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GroupIcon.png"))); // NOI18N
        refugeIcon.setBorderPainted(false);
        refugeIcon.setContentAreaFilled(false);
        refugeIcon.setEnabled(false);
        refugeIcon.setFocusable(false);
        refugeIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refugeIcon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        refugeIcon.setMaximumSize(new java.awt.Dimension(100, 100));
        refugeIcon.setMinimumSize(new java.awt.Dimension(30, 30));
        refugeCounters.add(refugeIcon);

        refugeCounter.setFont(utils.FontManager.titleFont);
        refugeCounter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        refugeCounter.setText("0");
        refugeCounter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        refugeCounter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refugeCounters.add(refugeCounter);

        refugeInfo.add(refugeCounters, java.awt.BorderLayout.EAST);

        refuge.add(refugeInfo, java.awt.BorderLayout.NORTH);

        mainPanel.add(refuge);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void pauseResumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseResumeButtonActionPerformed
        pauseResume();
    }//GEN-LAST:event_pauseResumeButtonActionPerformed

    private void logsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logsButtonActionPerformed
       ServerApp.redirect("LOG");
    }//GEN-LAST:event_logsButtonActionPerformed

    private void commonHumanIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIconActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIconActionPerformed

    private void commonHumanIcon1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon1ActionPerformed

    private void foodIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodIconActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_foodIconActionPerformed

    private void commonHumanIcon2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon2ActionPerformed

    private void commonHumanIcon3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon3ActionPerformed

    private void commonHumanIcon4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon4ActionPerformed

    private void commonHumanIcon5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon5ActionPerformed

    private void commonHumanIcon6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commonHumanIcon6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_commonHumanIcon6ActionPerformed

    private void ZombieIcon1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZombieIcon1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ZombieIcon1ActionPerformed

    private void ZombieIcon5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZombieIcon5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ZombieIcon5ActionPerformed

    private void ZombieIcon6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZombieIcon6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ZombieIcon6ActionPerformed

    private void ZombieIcon7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZombieIcon7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ZombieIcon7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CommonList;
    private javax.swing.JPanel DiningList;
    private javax.swing.JLabel HumanCounter1;
    private javax.swing.JLabel HumanCounter2;
    private javax.swing.JLabel HumanCounter3;
    private javax.swing.JLabel HumanCounter4;
    private javax.swing.JPanel RestList;
    private javax.swing.JPanel Risk1;
    private javax.swing.JPanel Risk2;
    private javax.swing.JPanel Risk3;
    private javax.swing.JPanel Risk4;
    private javax.swing.JPanel RiskHuman1;
    private javax.swing.JPanel RiskHuman2;
    private javax.swing.JPanel RiskHuman3;
    private javax.swing.JPanel RiskHuman4;
    private javax.swing.JPanel RiskZombie1;
    private javax.swing.JPanel RiskZombie2;
    private javax.swing.JPanel RiskZombie3;
    private javax.swing.JPanel RiskZombie4;
    private javax.swing.JPanel TunnelExiting1;
    private javax.swing.JPanel TunnelExiting2;
    private javax.swing.JPanel TunnelExiting3;
    private javax.swing.JPanel TunnelExiting4;
    private javax.swing.JPanel TunnelReturning1;
    private javax.swing.JPanel TunnelReturning2;
    private javax.swing.JPanel TunnelReturning3;
    private javax.swing.JPanel TunnelReturning4;
    private javax.swing.JLabel ZombieCounter1;
    private javax.swing.JLabel ZombieCounter2;
    private javax.swing.JLabel ZombieCounter3;
    private javax.swing.JLabel ZombieCounter4;
    private javax.swing.JButton ZombieIcon1;
    private javax.swing.JButton ZombieIcon5;
    private javax.swing.JButton ZombieIcon6;
    private javax.swing.JButton ZombieIcon7;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JScrollPane commonAreaPanel;
    private javax.swing.JPanel commonAreaPanel1;
    private javax.swing.JPanel commonAreainfo;
    private javax.swing.JLabel commonHumanCounter;
    private javax.swing.JButton commonHumanIcon;
    private javax.swing.JButton commonHumanIcon1;
    private javax.swing.JButton commonHumanIcon2;
    private javax.swing.JButton commonHumanIcon3;
    private javax.swing.JButton commonHumanIcon4;
    private javax.swing.JButton commonHumanIcon5;
    private javax.swing.JButton commonHumanIcon6;
    private javax.swing.JLabel currentCrossing1;
    private javax.swing.JLabel currentCrossing2;
    private javax.swing.JLabel currentCrossing3;
    private javax.swing.JLabel currentCrossing4;
    private javax.swing.JPanel diningRoomInfo;
    private javax.swing.JScrollPane diningRoomPanel;
    private javax.swing.JPanel diningRoomPanel1;
    private javax.swing.JLabel foodCounter;
    private javax.swing.JButton foodIcon;
    private javax.swing.JLabel humanDiningCounter;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JButton logsButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pauseResumeButton;
    private javax.swing.JPanel refuge;
    private javax.swing.JLabel refugeCounter;
    private javax.swing.JPanel refugeCounters;
    private javax.swing.JButton refugeIcon;
    private javax.swing.JPanel refugeInfo;
    private javax.swing.JLabel refugeName;
    private javax.swing.JPanel refugePanel;
    private javax.swing.JPanel restAreaInfo;
    private javax.swing.JScrollPane restAreaPanel;
    private javax.swing.JPanel restAreaPanel1;
    private javax.swing.JLabel restHumanCounter;
    private javax.swing.JPanel riskZoneInfo1;
    private javax.swing.JPanel riskZoneInfo2;
    private javax.swing.JPanel riskZoneInfo3;
    private javax.swing.JPanel riskZoneInfo4;
    private javax.swing.JScrollPane riskZonePanel1;
    private javax.swing.JScrollPane riskZonePanel10;
    private javax.swing.JScrollPane riskZonePanel11;
    private javax.swing.JScrollPane riskZonePanel3;
    private javax.swing.JScrollPane riskZonePanel4;
    private javax.swing.JScrollPane riskZonePanel7;
    private javax.swing.JScrollPane riskZonePanel8;
    private javax.swing.JScrollPane riskZonePanel9;
    private javax.swing.JPanel riskZonesPanel;
    private javax.swing.JScrollPane tunnelPanel1;
    private javax.swing.JScrollPane tunnelPanel16;
    private javax.swing.JScrollPane tunnelPanel17;
    private javax.swing.JScrollPane tunnelPanel18;
    private javax.swing.JScrollPane tunnelPanel2;
    private javax.swing.JScrollPane tunnelPanel3;
    private javax.swing.JScrollPane tunnelPanel4;
    private javax.swing.JScrollPane tunnelPanel6;
    private javax.swing.JPanel tunnelsPanel;
    // End of variables declaration//GEN-END:variables
}
