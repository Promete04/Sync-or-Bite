/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Server.frontend;

import Server.backend.Human;
import Server.backend.Logger;
import Server.backend.PauseManager;
import static Server.frontend.ServerApp.logger;
import static Server.frontend.ServerApp.pm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author guill
 */
public class LogPage extends javax.swing.JPanel
{

    /**
     * Creates new form LogPage
     */
    private PauseManager  pm;
    private Logger logger;
    private ImageIcon pauseIcon= new ImageIcon(getClass().getResource( "/images/PauseIcon.png" ));
    private ImageIcon resumeIcon= new ImageIcon(getClass().getResource( "/images/ResumeIcon.png" ));
    
    
    
    public LogPage() 
    {
        this.logger = ServerApp.getLogger();
        this.pm = ServerApp.getPM();
        logger.setLogPage(this);
        
        initComponents();
        mapButton.setIcon(new ImageIcon(getClass().getResource("/images/MapIcon.png")));
        mapButton.setBorder(null);
        
        ImageIcon initialState = pm.isPaused() ?  resumeIcon : pauseIcon;
        
        pm.setPauseStateListener(() -> {
            ImageIcon current = pm.isPaused() ? resumeIcon : pauseIcon;
            pauseResumeButton.setIcon(current);
        });
       
        // Load existing log file content
        loadLogs();
        
        
    }
    
  
    
    private void loadLogs() 
    {
        BufferedReader br = null;
        File file = new File(logger.getFileName());
        if(file.exists())
        {
            try 
            {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) 
                {
                    logsArea.append(line + "\n");
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            } 
            finally 
            {
                if (br != null) 
                {
                    try 
                    {
                        br.close();
                    } 
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        }    
    }
    

    public void onNewLog(String logEntry)
    {
        logsArea.append(logEntry + "\n");  
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        mapButton = new javax.swing.JButton();
        pauseResumeButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logsArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setForeground(utils.ColorManager.BG_COLOR);
        jPanel2.setLayout(new java.awt.BorderLayout());

        mapButton.setForeground(utils.ColorManager.BG_COLOR);
        mapButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MapIcon.png"))); // NOI18N
        mapButton.setBorderPainted(false);
        mapButton.setContentAreaFilled(false);
        mapButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mapButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        mapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapButtonActionPerformed(evt);
            }
        });
        jPanel2.add(mapButton, java.awt.BorderLayout.EAST);

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
        jPanel2.add(pauseResumeButton, java.awt.BorderLayout.LINE_START);

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setForeground(utils.ColorManager.BG_COLOR);

        logsArea.setEditable(false);
        logsArea.setColumns(20);
        logsArea.setFont(utils.FontManager.regularFont);
        logsArea.setForeground(utils.ColorManager.TEXT_COLOR);
        logsArea.setRows(5);
        jScrollPane1.setViewportView(logsArea);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void mapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mapButtonActionPerformed
        ServerApp.redirect("MAP");
    }//GEN-LAST:event_mapButtonActionPerformed

    private void pauseResumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseResumeButtonActionPerformed
        pm.togglePause();
    }//GEN-LAST:event_pauseResumeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logsArea;
    private javax.swing.JButton mapButton;
    private javax.swing.JButton pauseResumeButton;
    // End of variables declaration//GEN-END:variables
}
