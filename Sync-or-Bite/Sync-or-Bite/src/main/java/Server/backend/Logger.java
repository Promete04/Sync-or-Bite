/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import Server.frontend.LogPage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides thread safe (uses the monitor) logging functionality to both GUI and file output.
 * Entries include timestamps for easier tracking.
 * 
 * Logs are also displayed in the GUI log panel via LogPage.
 */
public class Logger 
{
    private LogPage logPage;
    private String file = null;
    private DateTimeFormatter filenameFormatter;
    private DateTimeFormatter lineFormatter;

    /**
     * Constructs a Logger instance and initializes a timestamped output file.
     */
    public Logger()
    {
        filenameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(filenameFormatter);
        this.file = "apocalypse_" + timestamp + ".txt"; 
    }
    
    /**
     * Logs a message to both the log file and GUI.
     * Protected with synchronized
     *
     * @param event the message/event description to log
     */
    public synchronized void log(String event) 
    {
        FileWriter fw = null;
        PrintWriter pw = null;

        try 
        {
            // Open file streams 
            fw = new FileWriter(file, true); 
            pw = new PrintWriter(fw);
            
            // Stablish formats
            lineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = LocalDateTime.now().format(lineFormatter);
            String logEntry = "[" + timestamp + "] " + event;
            
            pw.println(logEntry);  // Write log line to file
            logPage.onNewLog(logEntry);  // Notify GUI about the new log
        } 
        catch (IOException e) 
        {
            System.err.println("IO exception: " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                // Close file streams
                if (pw != null) 
                {
                    pw.close();
                }
                if (fw != null) 
                {
                    fw.close();
                }
            } 
            catch (IOException e) 
            {
                System.err.println("IO exception: " + e.getMessage());
            }
        }
    }
    
    /**
     * Returns the name of the log file currently being written.
     *
     * @return the filename of the log file
     */
    public String getFileName() 
    {
        return file;
    }

    /**
     * Sets the log panel GUI component to forward log messages to.
     *
     * @param logPage the GUI panel that displays log messages
     */
    public void setLogPage(LogPage logPage) 
    {
        this.logPage = logPage;
    }

}
