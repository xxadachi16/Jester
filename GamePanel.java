import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

   enum GameMode {
      PLAYGROUND,
      TEST,
      RANDOM
   }
   GameMode mode;
   public static boolean[] keys = new boolean[256];
   private Thread gameThread;
   private Player player; // Ensure this is explicitly typed as Player
   private TileMap tileMap;
   private ArrayList<Enemy> enemies = new ArrayList<>();
   private boolean pause;

   private UIBarPanel uiBarPanel;
   

   private int cameraX = 0;
   private int cameraXMIN = 0;
   private int cameraXMAX = 1000;
   private int cameraY = 0;
   private int cameraYMIN = 0;
   private int cameraYMAX = 1000; //figure out the math for this someday using the tiles but not right now

   private BufferedImage background;

   public GamePanel() throws IOException {
      try {
         background = ImageIO.read(getClass().getResource("Sprites/background.png"));
      
      } catch (IOException e) {
         e.printStackTrace();
         background = null; // Handle the error case
      }
      
      mode = GameMode.PLAYGROUND;
      System.out.println(mode);
   
      setPreferredSize(new Dimension(900, 700));
      setFocusable(true);
      addKeyListener(this);
   
      setLayout(new BorderLayout());
   
      tileMap = new TileMap("Maps/Seed/randRoom0.csv"); // Pass the correct file path
      player = new Player(tileMap.psx*TileMap.TILE_SIZE, tileMap.psy*TileMap.TILE_SIZE, tileMap);
      System.out.println("Spawning the player at " + tileMap.psx + ", " + tileMap.psy);
   
      for(int i = 0; i < tileMap.enemyPos.size(); i += 2) {
         int x = tileMap.enemyPos.get(i);
         int y = tileMap.enemyPos.get(i + 1);
         enemies.add(new Enemy(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, tileMap));
      }
      
      
      // Replace UIPanel with UIBarPanel
      uiBarPanel = new UIBarPanel(player);
      add(uiBarPanel, BorderLayout.SOUTH);
   
      gameThread = new Thread(this);
      gameThread.start();
   
   }

   public GamePanel(int n) throws IOException {
      try {
         background = ImageIO.read(getClass().getResource("Sprites/background.png"));
      
      } catch (IOException e) {
         e.printStackTrace();
         background = null; // Handle the error case
      }
      
      mode = GameMode.TEST;
      System.out.println(mode);
   
      setPreferredSize(new Dimension(900, 700));
      setFocusable(true);
      addKeyListener(this);
   
      setLayout(new BorderLayout());
   
      tileMap = new TileMap("Maps/BossRoom1.csv"); // Pass the correct file path
      player = new Player(tileMap.psx*TileMap.TILE_SIZE, tileMap.psy*TileMap.TILE_SIZE, tileMap);
      System.out.println("Spawning the player at " + tileMap.psx + ", " + tileMap.psy);
   
      for(int i = 0; i < tileMap.enemyPos.size(); i += 2) {
         int x = tileMap.enemyPos.get(i);
         int y = tileMap.enemyPos.get(i + 1);
         enemies.add(new Enemy(x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, tileMap));
      }
      
      
      // Replace UIPanel with UIBarPanel
      uiBarPanel = new UIBarPanel(player);
      add(uiBarPanel, BorderLayout.SOUTH);
   
      gameThread = new Thread(this);
      gameThread.start();
   
   }

   @Override
   public void run() {
      while (true) {
         update();
         repaint();
         try {
            Thread.sleep(16);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   private void update() {
      
      for (int i = 0; i < enemies.size(); i++) {
         player.entityCollision(enemies.get(i));
         player.hurtboxCheck(enemies.get(i));
         enemies.get(i).update(); // yes the bruh
      }
      cameraX = player.getX() - 400; // Follow player with slight offset
      if (cameraX < cameraXMIN)
         cameraX = cameraXMIN;
      if (cameraX > cameraXMAX)
         cameraX = cameraXMAX;

      cameraY = player.getY() - 400;
      if (cameraY < cameraYMIN)
         cameraY = cameraYMIN;
      if (cameraY > cameraYMAX)
         cameraY = cameraYMAX;
      uiBarPanel.repaint(); // Ensure the UI panel updates
      
      /*if (keys[KeyEvent.VK_P]) {
         if (pause) {
            pause = false;
         } else {
            pause = true;
         }
         System.out.println(pause);
      }*/   
   }

   @Override
   protected void paintComponent(Graphics g) {
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
      
   }

   public void dark(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      int width = getWidth();
      int height = getHeight();
      RadialGradientPaint gradient = new RadialGradientPaint(
            new Point(player.getX() - cameraX, player.getY() - cameraY),
            300, // Radius of the light area
            new float[] { 0f, 1f },
            new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 255) } // Transparent to dark
         );
      g2d.setPaint(gradient);
      g2d.fillRect(0, 0, width, height);
   }

   @Override
   public void keyPressed(KeyEvent e) {
      keys[e.getKeyCode()] = true;
   }

   @Override
   public void keyReleased(KeyEvent e) {
      keys[e.getKeyCode()] = false;
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }
}
