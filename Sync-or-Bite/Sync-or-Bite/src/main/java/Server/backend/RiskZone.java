/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.backend;

/**
 *
 * @author guill
 */
public class RiskZone 
{
    private UnsafeArea[] unsafeAreas;
    private FoodGenerator fgenerator = new FoodGenerator();
    private Zombie[] topKillers = new Zombie[3];
    private Logger logger;
    
    public RiskZone(Logger logger)
    {
        this.logger = logger;
        unsafeAreas = new UnsafeArea[4];
        for(int i=0; i<4; i++)
        {
            unsafeAreas[i] = new UnsafeArea(i, logger, fgenerator, this);
        }
    }
    
    public UnsafeArea[] getUnsafeAreas()
    {
        return unsafeAreas;
    }
    
    public UnsafeArea obtainUnsafeArea(int area)
    {
        return unsafeAreas[area];
    }

    public synchronized void reportKill(Zombie z) 
    {
        int i = 0;
        boolean in = false;

        while (i < 3 && !in) 
        {
            if (topKillers[i] != null && (topKillers[i].getZombieId() == z.getZombieId())) 
            {
                reorderTopKillers();
                in = true;
            }
            i++;
        }

        if (!in) 
        {
            i = 0;
            while (i < 3 && !in) 
            {
                if (topKillers[i] == null) 
                {
                    topKillers[i] = z;
                    reorderTopKillers();
                    in = true;
                }
                i++;
            }
        }

        if (!in && (topKillers[2] == null || z.getKillCount() > topKillers[2].getKillCount())) 
        {
            topKillers[2] = z;
            reorderTopKillers();
        }

        showTopKillers();
    }


    // Bubble sort over the kill count.
    private void reorderTopKillers() 
    {
        for (int i = 0; i < topKillers.length - 1; i++) 
        {
            for (int j = i + 1; j < topKillers.length; j++) 
            {
                if (topKillers[i] != null && topKillers[j] != null
                        && topKillers[j].getKillCount() > topKillers[i].getKillCount()) 
                {
                    Zombie aux = topKillers[i];
                    topKillers[i] = topKillers[j];
                    topKillers[j] = aux;
                }
            }
        }
    }


    public void showTopKillers() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Top 3 Zombies:\n");

        for (int i = 0; i < 3; i++) 
        {
            if (topKillers[i] != null) 
            {
                sb.append("                                             ").append(i + 1).append(". ").append(topKillers[i].getZombieId()).append(":  ").append(topKillers[i].getKillCount()).append("\n");
            }
        }

        logger.log(sb.toString());
    }
    
    public String obtainTopKillers() 
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) 
        {
            if (topKillers[i] != null) 
            {
                sb.append(topKillers[i].getZombieId()).append("|").append(topKillers[i].getKillCount()).append("|");
            }
            else
            {
                sb.append("Z----"+"|"+"0"+"|");
            }
        }
        return sb.toString();
    }
}
