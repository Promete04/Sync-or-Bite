/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author Lopex
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger 
{
    private String file;
    private DateTimeFormatter filenameFormatter;
    private DateTimeFormatter lineFormatter;
    
    public Logger()
    {
        filenameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(filenameFormatter);
        this.file = "apocalypse_" + timestamp + ".txt";
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
            String timestamp = java.time.LocalDateTime.now().format(lineFormatter);
            pw.println("[" + timestamp + "] " + event);

        } 
        catch (IOException e) 
        {
            System.err.println("Logging failed: " + e.getMessage());
        } 
        finally 
        {
            try 
            {
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
                System.err.println("Error closing log file: " + e.getMessage());
            }
        }
    }
}
