/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import Server.frontend.App;
import java.awt.*;
import java.io.InputStream;

/**
 *
 * @author guill
 */

public class FontManager {


    public static final Font lightFont = loadFont("/fonts/UniSpaceIt.ttf", 14);
    public static final Font regularFont = loadFont("/fonts/UniSpaceRg.ttf", 14);
    public static final Font boldFont = loadFont("/fonts/UniSpaceBd.ttf", 14);

    public static final Font titleFont = boldFont.deriveFont(36f);
    public static final Font subtitleFont = boldFont.deriveFont(20f);
    public static final Font subText = lightFont.deriveFont(12f);

    /**
     * useFont() adds a new custom text font to the proyect.
     * @param path the font path relative to "resources/" folder
     * @param size the size of the text
     * @return a new custom font
     */
    private static Font loadFont(String path,float size){
        try {
            InputStream is = App.class.getResourceAsStream(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(16f);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}