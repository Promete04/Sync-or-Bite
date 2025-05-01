/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import Server.frontend.ServerApp;
import java.awt.*;
import java.io.InputStream;


/**
 * Font library.
 */
public class FontManager 
{
    public static final Font lightFont = loadFont("/fonts/UniSpaceIt.ttf", 16);
    public static final Font regularFont = loadFont("/fonts/UniSpaceRg.ttf", 16);
    public static final Font boldFont = loadFont("/fonts/UniSpaceBd.ttf", 16);

    public static final Font titleFont = boldFont.deriveFont(50f);
    public static final Font subtitleFont = boldFont.deriveFont(35f);
    public static final Font subText = lightFont.deriveFont(24f);

    /**
     * loadFont() adds a new custom text font to the proyect.
     * @param path the font path relative to "resources/" folder
     * @param size the size of the text
     * @return new custom font
     */
    private static Font loadFont(String path,float size)
    {
        Font resultFont = null;
        try 
        {
            InputStream is = ServerApp.class.getResourceAsStream(path);
            if (is != null)
            {
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                resultFont = font.deriveFont(size);
            }
            else
            {
                System.err.println("Font resource not found: " + path);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resultFont;
    }
}