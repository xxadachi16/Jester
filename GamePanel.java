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
   private boolean timeFreeze;
   private int time = 30;
   private boolean lastP;
   private boolean lastT;

   private UIBarPanel uiBarPanel;
   

   private int cameraX = 0;
   private int cameraXMIN = 0;
   private int cameraXMAX = 20000;
   private int cameraY = 0;
   private int cameraYMIN = -20000;
   private int cameraYMAX = 20000; //figure out the math for this someday using the tiles but not right now

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
   
      tileMap = new TileMap("Maps/big.csv"); // Pass the correct file path
      
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

   public GamePanel(String n) throws IOException {
      try {
         background = ImageIO.read(getClass().getResource("Sprites/background.png"));
      
      } catch (IOException e) {
         e.printStackTrace();
         background = null; // Handle the error case
      }


      setPreferredSize(new Dimension(900, 700));
      setFocusable(true);
      addKeyListener(this);

      setLayout(new BorderLayout());
   
      if (n.equals("0")) {
         tileMap = new TileMap("Maps/BossRoom1.csv"); // Pass the correct file path
         mode = GameMode.TEST;
      } else if (n.equals("1")) {
         tileMap = new TileMap(new int[][] {{3, 4, 4}, {0, 1, 2}}, true);
         mode = GameMode.RANDOM;
      } else if (n.equals("Zainshouldfollowmeongithub")) {
         tileMap = new TileMap();
         mode = GameMode.RANDOM;
      } else {
         tileMap = new TileMap(n, 0);
         mode = GameMode.RANDOM;
         setCameraBounds(0, 10000,-40000, 40000);
      }
      System.out.println(mode);
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
      if (!pause) {
      for (int i = 0; i < enemies.size(); i++) {
         player.entityCollision(enemies.get(i));
         player.hurtboxCheck(enemies.get(i));
         enemies.get(i).update(); // yes the bruh
         }
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
      
      //this little cluster should migrate soon but not now
      if (timeFreeze) {
         uiBarPanel.setTime(time); // Freeze time
      } else if (keys[KeyEvent.VK_T] && keys[KeyEvent.VK_R]) {
         uiBarPanel.setTime(45); // Reset time or set to desired value
      }
      if (lastP != keys[KeyEvent.VK_P] && keys[KeyEvent.VK_P]) {
         lastP = true;
         pause = !pause;
         timeFreeze = !timeFreeze;
         System.out.println("Pause " + pause);
      } else if (!keys[KeyEvent.VK_P]) {
         lastP = false;
      }
      if (lastT != keys[KeyEvent.VK_T] && keys[KeyEvent.VK_T]) {
         if (!pause) {
            lastT = true;
            time = uiBarPanel.getTime();
            System.out.println("Freeze " + timeFreeze);
            timeFreeze = !timeFreeze;
         }
      } else if (!keys[KeyEvent.VK_T]) {
         lastT = false;
      }
      if (!keys[KeyEvent.VK_T] && keys[KeyEvent.VK_R]) {
         System.out.println("Reseting");
         player.setHp(0);
      }
   }
   public void setCameraBounds(int xMin, int xMax, int yMin, int yMax) { //figure out this figure out later multiply it by 32 or maybe there's some final constant idk
      this.cameraXMIN = xMin;
      this.cameraXMAX = xMax;
      this.cameraYMIN = yMin;
      this.cameraYMAX = yMax;
   }
   @Override
   protected void paintComponent(Graphics g) {
      player.update(); //what the bruh
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
