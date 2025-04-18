/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Server.frontend;

import Server.backend.PauseManager;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author guill
 */
public class MapPage extends javax.swing.JPanel 
{

    /**
     * Creates new form map
     */
    private ImageIcon pauseIcon= new ImageIcon(getClass().getResource( "/images/PauseIcon.png" ));
    private ImageIcon resumeIcon= new ImageIcon(getClass().getResource( "/images/ResumeIcon.png" ));
    private ImageIcon humanIcon= new ImageIcon(getClass().getResource( "/images/HumanIcon.png" ));
    private ImageIcon zombieIcon= new ImageIcon(getClass().getResource( "/images/ZombieIcon.png" ));
    private ImageIcon diningIcon= new ImageIcon(getClass().getResource( "/images/DiningRoomicon.png" ));
    private ImageIcon groupIcon= new ImageIcon(getClass().getResource( "/images/Groupicon.png" ));
    private PauseManager  pm;
    private final Map<String, JLabel> Counters = new HashMap<>();
    private final Map<String, JPanel> Panels = new HashMap<>();
    
    
    public MapPage() 
    {
        this.pm = ServerApp.getPM();
        
        initComponents();
        
        confButtons();
        setupCounters();
        setupPanels();
        
        pauseResumeButton.setIcon(pauseIcon);

        pm.setPauseStateListener(() -> {
            ImageIcon current = pm.isPaused() ? resumeIcon : pauseIcon;
            pauseResumeButton.setIcon(current);
        });
    }
    private void setupPanels() 
    {
         Panels.put("RH1", RiskHuman1);
         Panels.put("RH2", RiskHuman2);
         Panels.put("RH3", RiskHuman3);   
         Panels.put("RH4", RiskHuman4);
         Panels.put("RZ1", RiskZombie1);
         Panels.put("RZ2", RiskZombie2);
         Panels.put("RZ3", RiskZombie3);
         Panels.put("RZ4", RiskZombie4);
         Panels.put("TR1", TunnelReturning1);
         Panels.put("TR2", TunnelReturning2);
         Panels.put("TR3", TunnelReturning3);
         Panels.put("TR4", TunnelReturning4);
         Panels.put("TE1", TunnelExiting1);
         Panels.put("TE2", TunnelExiting2);
         Panels.put("TE3", TunnelExiting3);
         Panels.put("TE4", TunnelExiting4);
         Panels.put("C", CommonList);
         Panels.put("D", DiningList);
         Panels.put("R", RestList);
    }
   
    private void setupCounters() 
    {
        Counters.put("H1", HumanCounter1);
        Counters.put("H2", HumanCounter2); 
        Counters.put("H3", HumanCounter3); 
        Counters.put("H4", HumanCounter4); 
        Counters.put("HC", commonHumanCounter);
        Counters.put("HD", humanDiningCounter);
        Counters.put("HR", restHumanCounter);
        Counters.put("Z1", ZombieCounter1);
        Counters.put("Z2", ZombieCounter2); 
        Counters.put("Z3", ZombieCounter3); 
        Counters.put("Z4", ZombieCounter4); 
        Counters.put("C1", currentCrossing1);
        Counters.put("C2", currentCrossing2); 
        Counters.put("C3", currentCrossing3); 
        Counters.put("C4", currentCrossing4); 
        Counters.put("FC",foodCounter);
        Counters.put("RC",refugeCounter);
        
    }
    
   public void pauseResume()
    {
        pm.togglePause();
        ImageIcon current = pm.isPaused() ? resumeIcon : pauseIcon;
        pauseResumeButton.setIcon(current);
    }
   
   public synchronized void addLabelToPanel(String panelKey, String labelText)
   {
       
        JPanel targetPanel = Panels.get(panelKey);

        if (targetPanel == null) {
            System.err.println("No panel found for key: " + panelKey);
            return;
        }

        JLabel label = new JLabel(labelText);
        label.setOpaque(true);
        label.setBackground(utils.ColorManager.HUMAN_COLOR); 
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); 

        targetPanel.add(label);
        targetPanel.revalidate();
        targetPanel.repaint();
    }
    public synchronized void removeLabelFromPanel(String panelKey, String labelText) 
    {
        JPanel targetPanel = Panels.get(panelKey);

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
                targetPanel.revalidate();
                targetPanel.repaint();
                return;
            }
        }

        System.out.println("Label with text '" + labelText + "' not found in panel " + panelKey);
    }
    public synchronized void setLabelColorInPanel(String panelKey, String labelText, Color color) 
    {
        JPanel targetPanel = Panels.get(panelKey);

        if (targetPanel == null)
        {
            System.err.println("No panel found for key: " + panelKey);
            return;
        }

        for (Component comp : targetPanel.getComponents()) 
        {
            if (comp instanceof JLabel label && label.getText().equals(labelText)) 
            {
                label.setOpaque(true); // necesario para que el color de fondo se vea
                label.setBackground(color);
                targetPanel.repaint();
                return;
            }
        }

        System.out.println("Label with text '" + labelText + "' not found in panel " + panelKey);
    }


    public synchronized void setCounter(String nameLabel, String value) 
    {
        Counters.get(nameLabel).setText(value);
    }

    public final void confButtons()
    {

        List<JButton> buttonList = new ArrayList<>(); //to simplify mass modification
        List<JButton> humanList = new ArrayList<>(); //to simplify mass modification
        List<JButton> zombieList = new ArrayList<>(); //to simplify mass modification
        buttonList.add(HumanIcon1);
        humanList.add(HumanIcon1);
        buttonList.add(HumanIcon2);
        humanList.add(HumanIcon2);
        buttonList.add(HumanIcon3);
        humanList.add(HumanIcon3);
        buttonList.add(HumanIcon4);
        humanList.add(HumanIcon4);
        buttonList.add(ZombieIcon1);
        zombieList.add(ZombieIcon1);
        buttonList.add(ZombieIcon2);
        zombieList.add(ZombieIcon2);
        buttonList.add(ZombieIcon3);
        zombieList.add(ZombieIcon3);
        buttonList.add(ZombieIcon4);
        zombieList.add(ZombieIcon4);
        buttonList.add(commonHumanIcon);
        humanList.add(commonHumanIcon);
        buttonList.add(diningHumanIcon);
        humanList.add(diningHumanIcon);
        buttonList.add(foodIcon);
        buttonList.add(logsButton);
        buttonList.add(pauseResumeButton);
        buttonList.add(refugeIcon);
        buttonList.add(restHumanIcon);
        humanList.add(restHumanIcon);
        
        for(JButton button:buttonList)
        {
            button.setBorder(null);
            //button.setBackground(utils.ColorManager.TRANSPARENT_COLOR);
            button.setForeground(utils.ColorManager.TRANSPARENT_COLOR);
            button.setLabel("");
        }
        for(JButton button:humanList)
        {
            button.setIcon(humanIcon);
        }
        
        for(JButton button:zombieList)
        {
            button.setIcon(zombieIcon);
        }
        
        foodIcon.setIcon(diningIcon);
        refugeIcon.setIcon(groupIcon);
    }
    /**
     * 
     * 
     */
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
        HumanIcon1 = new javax.swing.JButton();
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
        HumanIcon2 = new javax.swing.JButton();
        HumanCounter2 = new javax.swing.JLabel();
        ZombieIcon2 = new javax.swing.JButton();
        ZombieCounter2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        riskZonePanel4 = new javax.swing.JScrollPane();
        RiskHuman2 = new javax.swing.JPanel();
        riskZonePanel7 = new javax.swing.JScrollPane();
        RiskZombie2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        Risk3 = new javax.swing.JPanel();
        riskZoneInfo3 = new javax.swing.JPanel();
        HumanIcon3 = new javax.swing.JButton();
        HumanCounter3 = new javax.swing.JLabel();
        ZombieIcon3 = new javax.swing.JButton();
        ZombieCounter3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        riskZonePanel8 = new javax.swing.JScrollPane();
        RiskHuman3 = new javax.swing.JPanel();
        riskZonePanel9 = new javax.swing.JScrollPane();
        RiskZombie3 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        Risk4 = new javax.swing.JPanel();
        riskZoneInfo4 = new javax.swing.JPanel();
        HumanIcon4 = new javax.swing.JButton();
        HumanCounter4 = new javax.swing.JLabel();
        ZombieIcon4 = new javax.swing.JButton();
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
        diningHumanIcon = new javax.swing.JButton();
        humanDiningCounter = new javax.swing.JLabel();
        foodIcon = new javax.swing.JButton();
        foodCounter = new javax.swing.JLabel();
        restAreaPanel1 = new javax.swing.JPanel();
        restAreaPanel = new javax.swing.JScrollPane();
        RestList = new javax.swing.JPanel();
        restAreaInfo = new javax.swing.JPanel();
        restHumanIcon = new javax.swing.JButton();
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

        HumanIcon1.setText("jButton1");
        HumanIcon1.setEnabled(false);
        HumanIcon1.setFocusPainted(false);
        HumanIcon1.setFocusable(false);
        riskZoneInfo1.add(HumanIcon1);

        HumanCounter1.setFont(utils.FontManager.regularFont);
        HumanCounter1.setText("0");
        riskZoneInfo1.add(HumanCounter1);

        ZombieIcon1.setText("jButton4");
        ZombieIcon1.setEnabled(false);
        ZombieIcon1.setFocusPainted(false);
        ZombieIcon1.setFocusable(false);
        riskZoneInfo1.add(ZombieIcon1);

        ZombieCounter1.setFont(utils.FontManager.regularFont);
        ZombieCounter1.setText("0");
        riskZoneInfo1.add(ZombieCounter1);

        Risk1.add(riskZoneInfo1, java.awt.BorderLayout.NORTH);

        jPanel1.setForeground(utils.ColorManager.BG_COLOR);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel1.setForeground(utils.ColorManager.BG_COLOR);
        riskZonePanel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskHuman1.setLayout(new javax.swing.BoxLayout(RiskHuman1, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel1.setViewportView(RiskHuman1);

        jPanel1.add(riskZonePanel1);

        riskZonePanel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskZombie1.setLayout(new javax.swing.BoxLayout(RiskZombie1, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel3.setViewportView(RiskZombie1);

        jPanel1.add(riskZonePanel3);

        Risk1.add(jPanel1, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk1);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator1);

        Risk2.setForeground(utils.ColorManager.BG_COLOR);
        Risk2.setLayout(new java.awt.BorderLayout());

        riskZoneInfo2.setForeground(utils.ColorManager.BG_COLOR);

        HumanIcon2.setText("jButton1");
        HumanIcon2.setEnabled(false);
        HumanIcon2.setFocusPainted(false);
        HumanIcon2.setFocusable(false);
        HumanIcon2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HumanIcon2ActionPerformed(evt);
            }
        });
        riskZoneInfo2.add(HumanIcon2);

        HumanCounter2.setFont(utils.FontManager.regularFont);
        HumanCounter2.setText("0");
        riskZoneInfo2.add(HumanCounter2);

        ZombieIcon2.setText("jButton4");
        ZombieIcon2.setEnabled(false);
        ZombieIcon2.setFocusPainted(false);
        ZombieIcon2.setFocusable(false);
        riskZoneInfo2.add(ZombieIcon2);

        ZombieCounter2.setFont(utils.FontManager.regularFont);
        ZombieCounter2.setText("0");
        riskZoneInfo2.add(ZombieCounter2);

        Risk2.add(riskZoneInfo2, java.awt.BorderLayout.NORTH);

        jPanel2.setForeground(utils.ColorManager.BG_COLOR);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskHuman2.setLayout(new javax.swing.BoxLayout(RiskHuman2, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel4.setViewportView(RiskHuman2);

        jPanel2.add(riskZonePanel4);

        riskZonePanel7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskZombie2.setLayout(new javax.swing.BoxLayout(RiskZombie2, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel7.setViewportView(RiskZombie2);

        jPanel2.add(riskZonePanel7);

        Risk2.add(jPanel2, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk2);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator2);

        Risk3.setForeground(utils.ColorManager.BG_COLOR);
        Risk3.setLayout(new java.awt.BorderLayout());

        riskZoneInfo3.setForeground(utils.ColorManager.BG_COLOR);

        HumanIcon3.setText("jButton1");
        HumanIcon3.setEnabled(false);
        HumanIcon3.setFocusPainted(false);
        HumanIcon3.setFocusable(false);
        riskZoneInfo3.add(HumanIcon3);

        HumanCounter3.setFont(utils.FontManager.regularFont);
        HumanCounter3.setText("0");
        riskZoneInfo3.add(HumanCounter3);

        ZombieIcon3.setText("jButton4");
        ZombieIcon3.setEnabled(false);
        ZombieIcon3.setFocusPainted(false);
        ZombieIcon3.setFocusable(false);
        riskZoneInfo3.add(ZombieIcon3);

        ZombieCounter3.setFont(utils.FontManager.regularFont);
        ZombieCounter3.setText("0");
        riskZoneInfo3.add(ZombieCounter3);

        Risk3.add(riskZoneInfo3, java.awt.BorderLayout.NORTH);

        jPanel3.setForeground(utils.ColorManager.BG_COLOR);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskHuman3.setLayout(new javax.swing.BoxLayout(RiskHuman3, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel8.setViewportView(RiskHuman3);

        jPanel3.add(riskZonePanel8);

        riskZonePanel9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskZombie3.setLayout(new javax.swing.BoxLayout(RiskZombie3, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel9.setViewportView(RiskZombie3);

        jPanel3.add(riskZonePanel9);

        Risk3.add(jPanel3, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk3);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        riskZonesPanel.add(jSeparator3);

        Risk4.setForeground(utils.ColorManager.BG_COLOR);
        Risk4.setLayout(new java.awt.BorderLayout());

        riskZoneInfo4.setForeground(utils.ColorManager.BG_COLOR);

        HumanIcon4.setText("jButton1");
        HumanIcon4.setEnabled(false);
        HumanIcon4.setFocusPainted(false);
        HumanIcon4.setFocusable(false);
        riskZoneInfo4.add(HumanIcon4);

        HumanCounter4.setFont(utils.FontManager.regularFont);
        HumanCounter4.setText("0");
        riskZoneInfo4.add(HumanCounter4);

        ZombieIcon4.setText("jButton4");
        ZombieIcon4.setEnabled(false);
        ZombieIcon4.setFocusPainted(false);
        ZombieIcon4.setFocusable(false);
        riskZoneInfo4.add(ZombieIcon4);

        ZombieCounter4.setFont(utils.FontManager.regularFont);
        ZombieCounter4.setText("0");
        riskZoneInfo4.add(ZombieCounter4);

        Risk4.add(riskZoneInfo4, java.awt.BorderLayout.NORTH);

        jPanel4.setForeground(utils.ColorManager.BG_COLOR);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        riskZonePanel10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskHuman4.setLayout(new javax.swing.BoxLayout(RiskHuman4, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel10.setViewportView(RiskHuman4);

        jPanel4.add(riskZonePanel10);

        riskZonePanel11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        RiskZombie4.setLayout(new javax.swing.BoxLayout(RiskZombie4, javax.swing.BoxLayout.Y_AXIS));
        riskZonePanel11.setViewportView(RiskZombie4);

        jPanel4.add(riskZonePanel11);

        Risk4.add(jPanel4, java.awt.BorderLayout.CENTER);

        riskZonesPanel.add(Risk4);

        mainPanel.add(riskZonesPanel);

        tunnelsPanel.setForeground(utils.ColorManager.BG_COLOR);
        tunnelsPanel.setLayout(new javax.swing.BoxLayout(tunnelsPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setForeground(utils.ColorManager.BG_COLOR);
        jPanel5.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning1.setLayout(new javax.swing.BoxLayout(TunnelReturning1, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel1.setViewportView(TunnelReturning1);

        jPanel5.add(tunnelPanel1);

        currentCrossing1.setFont(utils.FontManager.boldFont);
        currentCrossing1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing1.setText("-----");
        currentCrossing1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel5.add(currentCrossing1);

        tunnelPanel6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting1.setLayout(new javax.swing.BoxLayout(TunnelExiting1, javax.swing.BoxLayout.X_AXIS));
        tunnelPanel6.setViewportView(TunnelExiting1);

        jPanel5.add(tunnelPanel6);

        tunnelsPanel.add(jPanel5);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator4);

        jPanel9.setForeground(utils.ColorManager.BG_COLOR);
        jPanel9.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning2.setLayout(new javax.swing.BoxLayout(TunnelReturning2, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel2.setViewportView(TunnelReturning2);

        jPanel9.add(tunnelPanel2);

        currentCrossing2.setFont(utils.FontManager.boldFont);
        currentCrossing2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing2.setText("-----");
        jPanel9.add(currentCrossing2);

        tunnelPanel16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting2.setLayout(new javax.swing.BoxLayout(TunnelExiting2, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel16.setViewportView(TunnelExiting2);

        jPanel9.add(tunnelPanel16);

        tunnelsPanel.add(jPanel9);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator5);

        jPanel10.setForeground(utils.ColorManager.BG_COLOR);
        jPanel10.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning3.setLayout(new javax.swing.BoxLayout(TunnelReturning3, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel3.setViewportView(TunnelReturning3);

        jPanel10.add(tunnelPanel3);

        currentCrossing3.setFont(utils.FontManager.boldFont);
        currentCrossing3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing3.setText("-----");
        jPanel10.add(currentCrossing3);

        tunnelPanel17.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting3.setLayout(new javax.swing.BoxLayout(TunnelExiting3, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel17.setViewportView(TunnelExiting3);

        jPanel10.add(tunnelPanel17);

        tunnelsPanel.add(jPanel10);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        tunnelsPanel.add(jSeparator6);

        jPanel11.setForeground(utils.ColorManager.BG_COLOR);
        jPanel11.setLayout(new java.awt.GridLayout(3, 0));

        tunnelPanel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelReturning4.setLayout(new javax.swing.BoxLayout(TunnelReturning4, javax.swing.BoxLayout.LINE_AXIS));
        tunnelPanel4.setViewportView(TunnelReturning4);

        jPanel11.add(tunnelPanel4);

        currentCrossing4.setFont(utils.FontManager.boldFont);
        currentCrossing4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentCrossing4.setText("-----");
        jPanel11.add(currentCrossing4);

        tunnelPanel18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        TunnelExiting4.setLayout(new javax.swing.BoxLayout(TunnelExiting4, javax.swing.BoxLayout.LINE_AXIS));
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
        commonAreaPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Common Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont));
        commonAreaPanel.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        commonAreaPanel.setViewportView(CommonList);

        commonAreaPanel1.add(commonAreaPanel, java.awt.BorderLayout.CENTER);

        commonAreainfo.setForeground(utils.ColorManager.BG_COLOR);

        commonHumanIcon.setText("jButton1");
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
        diningRoomPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dining Room", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont));
        diningRoomPanel.setViewportView(DiningList);

        diningRoomPanel1.add(diningRoomPanel, java.awt.BorderLayout.CENTER);

        diningRoomInfo.setForeground(utils.ColorManager.BG_COLOR);

        diningHumanIcon.setText("jButton4");
        diningHumanIcon.setEnabled(false);
        diningHumanIcon.setFocusPainted(false);
        diningHumanIcon.setFocusable(false);
        diningRoomInfo.add(diningHumanIcon);

        humanDiningCounter.setFont(utils.FontManager.regularFont);
        humanDiningCounter.setText("0");
        diningRoomInfo.add(humanDiningCounter);

        foodIcon.setText("jButton6");
        foodIcon.setEnabled(false);
        foodIcon.setFocusPainted(false);
        foodIcon.setFocusable(false);
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
        restAreaPanel.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rest Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, utils.FontManager.boldFont));
        restAreaPanel.setViewportView(RestList);

        restAreaPanel1.add(restAreaPanel, java.awt.BorderLayout.CENTER);

        restAreaInfo.setForeground(utils.ColorManager.BG_COLOR);

        restHumanIcon.setText("jButton5");
        restHumanIcon.setEnabled(false);
        restHumanIcon.setFocusPainted(false);
        restHumanIcon.setFocusable(false);
        restAreaInfo.add(restHumanIcon);

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

        refugeIcon.setText("jButton1");
        refugeIcon.setEnabled(false);
        refugeIcon.setFocusable(false);
        refugeIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refugeCounters.add(refugeIcon);

        refugeCounter.setFont(utils.FontManager.regularFont);
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

    private void HumanIcon2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HumanIcon2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HumanIcon2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CommonList;
    private javax.swing.JPanel DiningList;
    private javax.swing.JLabel HumanCounter1;
    private javax.swing.JLabel HumanCounter2;
    private javax.swing.JLabel HumanCounter3;
    private javax.swing.JLabel HumanCounter4;
    private javax.swing.JButton HumanIcon1;
    private javax.swing.JButton HumanIcon2;
    private javax.swing.JButton HumanIcon3;
    private javax.swing.JButton HumanIcon4;
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
    private javax.swing.JButton ZombieIcon2;
    private javax.swing.JButton ZombieIcon3;
    private javax.swing.JButton ZombieIcon4;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JScrollPane commonAreaPanel;
    private javax.swing.JPanel commonAreaPanel1;
    private javax.swing.JPanel commonAreainfo;
    private javax.swing.JLabel commonHumanCounter;
    private javax.swing.JButton commonHumanIcon;
    private javax.swing.JLabel currentCrossing1;
    private javax.swing.JLabel currentCrossing2;
    private javax.swing.JLabel currentCrossing3;
    private javax.swing.JLabel currentCrossing4;
    private javax.swing.JButton diningHumanIcon;
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
    private javax.swing.JButton restHumanIcon;
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
