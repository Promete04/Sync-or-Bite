/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import utils.LogListener;

public class Logger 
{
    private String file;
    private DateTimeFormatter filenameFormatter;
    private DateTimeFormatter lineFormatter;
    private List<LogListener> listeners;

    public Logger()
    {
    listeners = new ArrayList<>();
    filenameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    String timestamp = LocalDateTime.now().format(filenameFormatter);

    // Define una carpeta 'logs' dentro del directorio actual y asegúrate de que exista
    File logDir = new File("logs");
    if (!logDir.exists()) {
        logDir.mkdirs();
    }
    this.file = logDir.getAbsolutePath() + File.separator + "apocalypse_" + timestamp + ".txt";
    System.out.println("Se están generando los logs en: " + this.file);
}
    
    public synchronized String getFileName() 
    {
        return file;
    }
    
    // Register a new log listener
    public synchronized void addLogListener(LogListener listener) 
    {
        listeners.add(listener);
    }
    
    public synchronized void log(String event) 
    {
        FileWriter fw = null;
        PrintWriter pw = null;

        try 
        {
            fw = new FileWriter(file, true); 
            pw = new PrintWriter(fw);
            
            lineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = LocalDateTime.now().format(lineFormatter);
            String logEntry = "[" + timestamp + "] " + event;
            pw.println(logEntry);
            
            // Notify all registered listeners that a new log entry was added
            for (LogListener listener : listeners) {
                listener.onNewLog(logEntry);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("IO exception: " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (pw != null) {
                    pw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } 
            catch (IOException e) 
            {
                System.err.println("IO exception: " + e.getMessage());
            }
        }
    }
}
