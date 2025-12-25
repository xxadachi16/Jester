import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MenuPanel extends JPanel {
   public static boolean[] keys = new boolean[256];
 
   private BufferedImage background;

   public MenuPanel() throws IOException {
            setPreferredSize(new Dimension(900, 700));
      setFocusable(true);
      

      setLayout(new BorderLayout());

   }

   
   

  
   /*protected void paintComponent(Graphics g) {
      player.update();
      super.paintComponent(g);

      // Draw the background scaled to fill the panel
      if (background != null) {
         g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
      }

      player.draw(g, cameraX, cameraY);
      
      for (int i = 0; i < enemies.size(); i++) {
         enemies.get(i).draw(g, cameraX, cameraY);
      }
      tileMap.draw(g, cameraX, cameraY);

      tileMap.clearRemovedTiles();
      dark(g);
      
   }*/

   

   
}
