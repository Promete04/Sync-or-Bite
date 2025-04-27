/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Client.frontend;

import Client.backend.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


/**
 *
 * The MainClientPage class represents the main user interface panel for the client application.
 * It provides dynamic updates to different labels displaying the state of humans, zombies,
 * and tunnel activity, as well as a podium for the kills.
 * 
 * It uses a SingleThreadExecutor to submit tasks, retrieves results via a Future,
 * and ensures updates happen every 500 milliseconds to avoid overloading the system.
 * 
 */
public class MainClientPage extends javax.swing.JPanel 
{
    // Toggler for pause/resume 
    private final Toggler toggler = new Toggler();
    
    // ExecutorService responsible for periodic data updates in a background thread
    private ExecutorService automaticUpdater = Executors.newSingleThreadExecutor();
    
    // Icons
    private ImageIcon pauseIcon= new ImageIcon(getClass().getResource( "/images/PauseIcon.png" ));
    private ImageIcon resumeIcon= new ImageIcon(getClass().getResource( "/images/ResumeIcon.png" ));
    
    // Map associating label IDs with their corresponding JLabel instances
    private final Map<Integer, JLabel> labels = new HashMap<>();
    

    /**
     * Creates new form MainClientPage
     */
    public MainClientPage() 
    {
        initComponents();
        setupLabels();
        
        //Prevent podium component corruption
        pTop1.setStringPainted(false);
        pTop2.setStringPainted(false);
        pTop3.setStringPainted(false);
        
        // Update the pause/resume button icon when toggling
        toggler.setPauseStateListener(new Runnable() 
        {
            public void run() 
            {
                ImageIcon current = toggler.isPaused() ? pauseIcon : resumeIcon;
                pauseResumeButton.setIcon(current);
            }
        });
        
        updateData();
        
    }
    
    /**
     * Starts the main loop responsible for gathering and updating data periodically.
     * This method uses a thread to fetch system state and update the GUI accordingly.
     */
    public void updateData()
    {
        Runnable r = new Runnable() 
        {
            public void run() 
            {
                String[] data;

                try 
                {
                    while (true) 
                    {
                        int totalKills=0;
                        
                        // Submit a Callable task that retrieves updated data
                        Future<String[]> future = automaticUpdater.submit(new AutomaticUpdaterTask());
                        
                        // Wait for the result
                        data = future.get();
                        
                        for (int i = 0; i < data.length; i++) 
                        {
                            //Check server pause/resume state
                            if(i==0)
                            {
                                if(data[0].equals(String.valueOf(true)))
                                    {
                                        toggler.resume();
                                    }
                                else
                                    {
                                        toggler.pause();
                                    }
                            }
                            // Provide labels with gathered data
                            else if(i<=14 || i==16 || i==18)
                            {
                                JLabel label = labels.get(i);
                                label.setText(data[i]);
                            }
                            // Calculate and show a dynamic podium with JScrollBar component
                            else 
                            {
                                totalKills=totalKills+Integer.parseInt(data[i]);
                                if (totalKills != 0) 
                                {
                                    kill1.setText(data[15]);
                                    kill2.setText(data[17]);
                                    kill3.setText(data[19]);
                                    
                                    int finalTotalKills = totalKills;
                                    int val1 = Integer.parseInt(data[15]);
                                    int val2 = Integer.parseInt(data[17]);
                                    int val3 = Integer.parseInt(data[19]);

                                    // Obtain and set the relative kill percentage
                                    pTop1.setValue((val1 * 100) / finalTotalKills);
                                    pTop2.setValue((val2 * 100) / finalTotalKills);
                                    pTop3.setValue((val3 * 100) / finalTotalKills);


                                    // Refresh the GUI
                                    pTop1.getParent().revalidate();
                                    pTop1.getParent().repaint();
                                    pTop2.getParent().revalidate();
                                    pTop2.getParent().repaint();
                                    pTop3.getParent().revalidate();
                                    pTop3.getParent().repaint();
                                }
                            }
                        }
                        
                        // Wait 500 milliseconds before fetching and updating data again
                        Thread.sleep(500);
                    }
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        };

        // Create and start the thread responsible of the periodic background updates
        Thread updater = new Thread(r);
        updater.start();
    }
    
    /**
    * Sets up the mapping between labels keys and their corresponding JLabel instances.
    * This mapping is used to update the system dynamically when its state changes.
    */
    private void setupLabels() 
    {
         labels.put(1,HR);
         labels.put(2, HT1);
         labels.put(3, HT2);
         labels.put(4, HT3);
         labels.put(5, HT4);
         labels.put(6, HR1);
         labels.put(7, HR2);
         labels.put(8, HR3);
         labels.put(9, HR4);
         labels.put(10, ZR1);
         labels.put(11, ZR2);
         labels.put(12, ZR3);
         labels.put(13, ZR4);
         labels.put(14,top1);
         labels.put(16, top2);
         labels.put(18,top3);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pauseResumeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel51 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        ZR1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        ZR2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ZR3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        ZR4 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        HR1 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        HR2 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        HR3 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        HR4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel53 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        HT1 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        HT2 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        HT3 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        HT4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel55 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        HR = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        top2 = new javax.swing.JLabel();
        pTop2 = new javax.swing.JProgressBar();
        kill2 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        top1 = new javax.swing.JLabel();
        pTop1 = new javax.swing.JProgressBar();
        kill1 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        top3 = new javax.swing.JLabel();
        pTop3 = new javax.swing.JProgressBar();
        kill3 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        pauseResumeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PauseIcon.png"))); // NOI18N
        pauseResumeButton.setBorderPainted(false);
        pauseResumeButton.setContentAreaFilled(false);
        pauseResumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseResumeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(pauseResumeButton, java.awt.BorderLayout.WEST);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(2, 0));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
        jPanel3.add(jSeparator1);

        jPanel51.setForeground(utils.ColorManager.BG_COLOR);
        jPanel51.setLayout(new javax.swing.BoxLayout(jPanel51, javax.swing.BoxLayout.X_AXIS));

        jPanel5.setForeground(utils.ColorManager.BG_COLOR);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(utils.FontManager.boldFont);
        jLabel1.setForeground(utils.ColorManager.TEXT_COLOR);
        jLabel1.setText("Nº Zombies in each unsafe area");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 55));
        jPanel5.add(jLabel1);

        jPanel51.add(jPanel5);

        jPanel6.setForeground(utils.ColorManager.BG_COLOR);
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jPanel7.setForeground(utils.ColorManager.BG_COLOR);
        jPanel7.setLayout(new java.awt.BorderLayout());

        ZR1.setFont(utils.FontManager.regularFont);
        ZR1.setForeground(utils.ColorManager.TEXT_COLOR);
        ZR1.setText("0");
        jPanel7.add(ZR1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7);

        jPanel8.setForeground(utils.ColorManager.BG_COLOR);
        jPanel8.setLayout(new java.awt.BorderLayout());

        ZR2.setFont(utils.FontManager.regularFont);
        ZR2.setForeground(utils.ColorManager.TEXT_COLOR);
        ZR2.setText("0");
        jPanel8.add(ZR2, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel8);

        jPanel9.setForeground(utils.ColorManager.BG_COLOR);
        jPanel9.setLayout(new java.awt.BorderLayout());

        ZR3.setFont(utils.FontManager.regularFont);
        ZR3.setForeground(utils.ColorManager.TEXT_COLOR);
        ZR3.setText("0");
        jPanel9.add(ZR3, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel9);

        jPanel10.setForeground(utils.ColorManager.BG_COLOR);
        jPanel10.setLayout(new java.awt.BorderLayout());

        ZR4.setFont(utils.FontManager.regularFont);
        ZR4.setForeground(utils.ColorManager.TEXT_COLOR);
        ZR4.setText("0");
        jPanel10.add(ZR4, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10);

        jPanel51.add(jPanel6);

        jPanel3.add(jPanel51);

        jPanel52.setForeground(utils.ColorManager.BG_COLOR);
        jPanel52.setLayout(new javax.swing.BoxLayout(jPanel52, javax.swing.BoxLayout.X_AXIS));

        jPanel11.setForeground(utils.ColorManager.BG_COLOR);
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setFont(utils.FontManager.boldFont);
        jLabel6.setForeground(utils.ColorManager.TEXT_COLOR);
        jLabel6.setText("Nº Humans in each unsafe area ");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 55));
        jPanel11.add(jLabel6);

        jPanel52.add(jPanel11);

        jPanel13.setForeground(utils.ColorManager.BG_COLOR);
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jPanel14.setForeground(utils.ColorManager.BG_COLOR);
        jPanel14.setLayout(new java.awt.BorderLayout());

        HR1.setFont(utils.FontManager.regularFont);
        HR1.setForeground(utils.ColorManager.TEXT_COLOR);
        HR1.setText("0");
        jPanel14.add(HR1, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel14);

        jPanel15.setForeground(utils.ColorManager.BG_COLOR);
        jPanel15.setLayout(new java.awt.BorderLayout());

        HR2.setFont(utils.FontManager.regularFont);
        HR2.setForeground(utils.ColorManager.TEXT_COLOR);
        HR2.setText("0");
        jPanel15.add(HR2, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel15);

        jPanel16.setForeground(utils.ColorManager.BG_COLOR);
        jPanel16.setLayout(new java.awt.BorderLayout());

        HR3.setFont(utils.FontManager.regularFont);
        HR3.setForeground(utils.ColorManager.TEXT_COLOR);
        HR3.setText("0");
        jPanel16.add(HR3, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel16);

        jPanel17.setForeground(utils.ColorManager.BG_COLOR);
        jPanel17.setLayout(new java.awt.BorderLayout());

        HR4.setFont(utils.FontManager.regularFont);
        HR4.setForeground(utils.ColorManager.TEXT_COLOR);
        HR4.setText("0");
        jPanel17.add(HR4, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel17);

        jPanel52.add(jPanel13);

        jPanel3.add(jPanel52);
        jPanel3.add(jSeparator3);

        jPanel53.setForeground(utils.ColorManager.BG_COLOR);
        jPanel53.setLayout(new javax.swing.BoxLayout(jPanel53, javax.swing.BoxLayout.X_AXIS));

        jPanel18.setForeground(utils.ColorManager.BG_COLOR);
        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18, javax.swing.BoxLayout.LINE_AXIS));

        jLabel11.setFont(utils.FontManager.boldFont);
        jLabel11.setForeground(utils.ColorManager.TEXT_COLOR);
        jLabel11.setText("Nº Humans in tunnels          ");
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 55));
        jPanel18.add(jLabel11);

        jPanel53.add(jPanel18);

        jPanel19.setForeground(utils.ColorManager.BG_COLOR);
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.LINE_AXIS));

        jPanel20.setForeground(utils.ColorManager.BG_COLOR);
        jPanel20.setLayout(new java.awt.BorderLayout());

        HT1.setFont(utils.FontManager.regularFont);
        HT1.setForeground(utils.ColorManager.TEXT_COLOR);
        HT1.setText("0");
        jPanel20.add(HT1, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel20);

        jPanel21.setForeground(utils.ColorManager.BG_COLOR);
        jPanel21.setLayout(new java.awt.BorderLayout());

        HT2.setFont(utils.FontManager.regularFont);
        HT2.setForeground(utils.ColorManager.TEXT_COLOR);
        HT2.setText("0");
        jPanel21.add(HT2, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel21);

        jPanel22.setForeground(utils.ColorManager.BG_COLOR);
        jPanel22.setLayout(new java.awt.BorderLayout());

        HT3.setFont(utils.FontManager.regularFont);
        HT3.setForeground(utils.ColorManager.TEXT_COLOR);
        HT3.setText("0");
        jPanel22.add(HT3, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel22);

        jPanel23.setForeground(utils.ColorManager.BG_COLOR);
        jPanel23.setLayout(new java.awt.BorderLayout());

        HT4.setFont(utils.FontManager.regularFont);
        HT4.setForeground(utils.ColorManager.TEXT_COLOR);
        HT4.setText("0");
        jPanel23.add(HT4, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel23);

        jPanel53.add(jPanel19);

        jPanel3.add(jPanel53);
        jPanel3.add(jSeparator4);

        jPanel55.setForeground(utils.ColorManager.BG_COLOR);
        jPanel55.setLayout(new javax.swing.BoxLayout(jPanel55, javax.swing.BoxLayout.X_AXIS));

        jPanel30.setForeground(utils.ColorManager.BG_COLOR);
        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30, javax.swing.BoxLayout.LINE_AXIS));

        jLabel21.setFont(utils.FontManager.boldFont);
        jLabel21.setForeground(utils.ColorManager.TEXT_COLOR);
        jLabel21.setText("Nº Humans in refuge");
        jLabel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 55));
        jPanel30.add(jLabel21);

        jPanel55.add(jPanel30);

        jPanel31.setForeground(utils.ColorManager.BG_COLOR);

        jPanel34.setForeground(utils.ColorManager.BG_COLOR);
        jPanel34.setLayout(new java.awt.BorderLayout());

        HR.setFont(utils.FontManager.regularFont);
        HR.setForeground(utils.ColorManager.TEXT_COLOR);
        HR.setText("0");
        jPanel34.add(HR, java.awt.BorderLayout.CENTER);

        jPanel31.add(jPanel34);

        jPanel55.add(jPanel31);

        jPanel3.add(jPanel55);
        jPanel3.add(jSeparator2);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(utils.FontManager.titleFont);
        jLabel2.setForeground(utils.ColorManager.TEXT_COLOR);
        jLabel2.setText("TOP 3 ZOMBIES");
        jPanel12.add(jLabel2);

        jPanel4.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel24.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel24.setForeground(utils.ColorManager.MAIN_COLOR);
        jPanel24.setLayout(new javax.swing.BoxLayout(jPanel24, javax.swing.BoxLayout.LINE_AXIS));

        jPanel25.setBorder(javax.swing.BorderFactory.createEmptyBorder(-2, 5, 1, 5));
        jPanel25.setLayout(new java.awt.BorderLayout());

        top2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        top2.setText("Z-----");
        jPanel25.add(top2, java.awt.BorderLayout.CENTER);

        pTop2.setBackground(utils.ColorManager.BG_COLOR);
        pTop2.setForeground(utils.ColorManager.MAIN_COLOR);
        pTop2.setOrientation(1);
        pTop2.setToolTipText("");
        pTop2.setBorderPainted(false);
        jPanel25.add(pTop2, java.awt.BorderLayout.PAGE_END);

        kill2.setText("0");
        jPanel25.add(kill2, java.awt.BorderLayout.LINE_END);

        jPanel24.add(jPanel25);

        jPanel28.setBorder(javax.swing.BorderFactory.createEmptyBorder(-2, 5, 1, 5));
        jPanel28.setLayout(new java.awt.BorderLayout());

        top1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        top1.setText("Z----");
        jPanel28.add(top1, java.awt.BorderLayout.CENTER);

        pTop1.setBackground(utils.ColorManager.BG_COLOR);
        pTop1.setForeground(utils.ColorManager.MAIN_COLOR);
        pTop1.setOrientation(1);
        pTop1.setBorderPainted(false);
        jPanel28.add(pTop1, java.awt.BorderLayout.PAGE_END);

        kill1.setText("0");
        jPanel28.add(kill1, java.awt.BorderLayout.LINE_END);

        jPanel24.add(jPanel28);

        jPanel29.setBorder(javax.swing.BorderFactory.createEmptyBorder(-2, 5, 1, 5));
        jPanel29.setLayout(new java.awt.BorderLayout());

        top3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        top3.setText("Z----");
        jPanel29.add(top3, java.awt.BorderLayout.CENTER);

        pTop3.setBackground(utils.ColorManager.BG_COLOR);
        pTop3.setForeground(utils.ColorManager.MAIN_COLOR);
        pTop3.setOrientation(1);
        pTop3.setBorderPainted(false);
        jPanel29.add(pTop3, java.awt.BorderLayout.PAGE_END);

        kill3.setText("0");
        jPanel29.add(kill3, java.awt.BorderLayout.LINE_END);

        jPanel24.add(jPanel29);

        jPanel4.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void pauseResumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseResumeButtonActionPerformed
        toggler.togglePause();
    }//GEN-LAST:event_pauseResumeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HR;
    private javax.swing.JLabel HR1;
    private javax.swing.JLabel HR2;
    private javax.swing.JLabel HR3;
    private javax.swing.JLabel HR4;
    private javax.swing.JLabel HT1;
    private javax.swing.JLabel HT2;
    private javax.swing.JLabel HT3;
    private javax.swing.JLabel HT4;
    private javax.swing.JLabel ZR1;
    private javax.swing.JLabel ZR2;
    private javax.swing.JLabel ZR3;
    private javax.swing.JLabel ZR4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel kill1;
    private javax.swing.JLabel kill2;
    private javax.swing.JLabel kill3;
    private javax.swing.JProgressBar pTop1;
    private javax.swing.JProgressBar pTop2;
    private javax.swing.JProgressBar pTop3;
    private javax.swing.JButton pauseResumeButton;
    private javax.swing.JLabel top1;
    private javax.swing.JLabel top2;
    private javax.swing.JLabel top3;
    // End of variables declaration//GEN-END:variables
}
