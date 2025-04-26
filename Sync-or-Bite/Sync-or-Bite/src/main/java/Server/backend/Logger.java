/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides thread safe (uses the monitor) logging functionality to both GUI and file output.
 * Entries include timestamps for easier tracking.
 * 
 * Logs are also displayed in the GUI log panel via LogPage.
 */
public class Logger 
{
    private String file = null;
    private DateTimeFormatter filenameFormatter;
    private DateTimeFormatter lineFormatter;
    private String lastLogEntry;

    // Observer list
    private final List<ChangeListener> listeners = new ArrayList<>();
    
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
     * Adds a new change listener to be notified when a log entry is made.
     *
     * @param l the change listener to be added
     */
    public void addChangeListener(ChangeListener l) 
    {
        listeners.add(l);
    }
    
    /**
     * Notifies all registered listeners that a change has occurred.
     */
    private void notifyChange() 
    {
        for (ChangeListener l : listeners) 
        {
            l.onChange(this, false);
        }
    }
    
    /**
     * Logs a message to both the log file and GUI.
     * Protected with the monitor
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
            this.lastLogEntry = logEntry;
            notifyChange();  // Notify GUI about the new log
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
     * Retrieves the most recent log entry.
     *
     * @return the last log entry string
     */
    public String getLastLogEntry() 
    {
        return lastLogEntry;
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
}
