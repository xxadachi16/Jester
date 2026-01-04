import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The Player class represents a player entity in the game, extending the Entity
 * class.
 * It handles player-specific attributes such as movement, physics,
 * interactions, and rendering.
 */
public class Player extends Entity {

   // physics
   private int speed = 4;
   private int MAX_SPEED = 5;
   private int jumpHeight = 16;
   private double tempo;
   private double MAX_TEMPO = 11.0;
   private int MAX_HP = 100;
   private int lastDirection;
   private BufferedImage sprite;
   private boolean swinging;
   private boolean isJumping = false; //jumping state for boost tiles
   private boolean iFrames; //might not even be needed
   private int iFramesCounter = 30; //lowk dependent on framerate I think but I don't care for now
   private int iFrameHp; //I love creating problems for later

   Rectangle hurtbox;
   // values
   private int coins;
   private int lastHp;

   // shop
   int healthPack = 10;
   int tempoPack = 20;
   int visibilityPack = 30;

   /**
    * Constructs a Player object with the specified initial position and tile map.
    *
    * @param x       The initial x-coordinate of the player.
    * @param y       The initial y-coordinate of the player.
    * @param tileMap The tile map associated with the player.
    */
   public Player(int x, int y, TileMap tileMap) {
      super(x, y, 32, 32, 200, 10);
      tempo = 0.0;
      this.tileMap = tileMap;
      lastHp = 100;
      hurtbox = new Rectangle(x, y, width, height);
      swinging = false;
      lastDirection = 1;

      // Load the sprite image
      try {
         sprite = ImageIO.read(getClass().getResource("Sprites/player.png"));
      } catch (IOException e) {
         e.printStackTrace();
         sprite = null; // Handle the error appropriately
      }
   }

   /**
    * Updates the player's state, including movement, physics, and interactions.
    * Handles tempo decay, health reset on death, and coin logging.
    */
   @Override
   public void update() {

      keys();
      move();
      spriteHandler();

      if (tempo > 0)
         tempo -= tempo / 100;
      if (tempo < 0)
         tempo = 0;
      if ((hp <= 0 && iFramesCounter <= 0) || y > tileMap.getYBounds() + 100) { //death reset //maybe don't use getYbounds and just store it or something idk
         hp = 100;
         x = tileMap.psx*TileMap.TILE_SIZE;
         y = tileMap.psy*TileMap.TILE_SIZE;

         /*try {
            PrintWriter writer = new PrintWriter("log.txt");
            writer.println("Coins: " + coins);
            writer.close();
         } catch (IOException e) {
            e.printStackTrace();
         } */
      }
      if (tempo > MAX_TEMPO)
         tempo = MAX_TEMPO;
      if (lastHp != hp && iFramesCounter <= 0) { //check to see if our guys took damage because polymorphism entity blah blah blah idk
         tempo -= 0.5;
         iFrames = true;
         iFrameHp = hp;
         lastHp = hp;
         iFramesCounter = 30;
      }
      if (hp > MAX_HP)
         hp = MAX_HP;
      if (iFramesCounter <= 0) {
         lastHp = hp;
      } else {
         iFramesCounter--;
         hp = iFrameHp; //instead of preventing damage it just heals it back
      }
   }

   /**
    * Checks for collisions between the player's hurtbox and an enemy.
    * If the player is swinging and intersects with the enemy, damages the enemy
    * and increases tempo.
    *
    * @param e The enemy to check for collisions with.
    */
   public void hurtboxCheck(Enemy e) {
      if (swinging) {
         Rectangle bounds = new Rectangle(e.x, e.y-32, e.width+32, e.height+32);
         if (bounds.intersects(hurtbox)) {
            e.setHp(e.getHp() - damage);
            tempo += 0.5;
         }
      }
   }

   /**
    * Detects and resolves collisions between the player and tiles in the tile map.
    * Handles interactions with solid tiles and coin tiles.
    */
   @Override
   protected void collisionDetection() {
      for (Tile tile : tileMap.getTiles()) {
         Rectangle r = tile.getBounds();
          Rectangle bounds = new Rectangle(x, y, width, height);

         if (tile.isSolid()) {

            if (bounds.intersects(r)) {
               if (tile.getType() == 2) { // spike tile
                  hp -= 10; //maybe make him jump too ouchie ouchie
               } 
               if (bounds.y < r.y) {
                  y = r.y - height;
                  dy = 0;
                  onGround = true; // collision is broken but it's lowkey a feature
               } else if (dy < 0 && bounds.y > r.y) {
                  y = r.y + r.height;
                  dy = 0;
               } else if (dx > 0 && bounds.x < r.x) {
                  x = r.x - width;
               } else if (dx < 0 && bounds.x > r.x) {
                  x = r.x + r.width;
               }
            }
         } else if (tile.getType() == 3) { // coin tile

            if (bounds.intersects(r)) {
               tileMap.removeTile(tile);
               coins++;
            }
         } else if (tile.getType() == 4) { // boost tile

            if (bounds.intersects(r)) {
               
               if (!isJumping && (GamePanel.keys[KeyEvent.VK_W] || GamePanel.keys[KeyEvent.VK_SPACE])) { //will probably cause shenanigans with double jump if collisoin() and key() are both pressed
                  dy = -jumpHeight;
                  onGround = false;
                  tempo += 1;
                  isJumping = true;
               }
            }
      
            }
      }
   }

   /**
    * Handles the player's sprite rendering logic, including flipping and
    * animation.
    */
   private void spriteHandler() {
      if (!swinging) {
         try {
            sprite = ImageIO.read(getClass().getResource("Sprites/player.png"));
            if (dx < 0) {
               sprite = flipImageHorizontally(sprite);
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      } else {
         ImageIcon icon = new ImageIcon(getClass().getResource("Sprites/attack.gif"));
         sprite = toBufferedImage(icon.getImage());

      }
   }

   /**
    * Converts a given Image to a BufferedImage.
    *
    * @param img The image to convert.
    * @return The converted BufferedImage.
    */
   private BufferedImage toBufferedImage(Image img) {
      if (img instanceof BufferedImage) {
         return (BufferedImage) img; // If it's already a BufferedImage, return it as is
      }

      // Otherwise, create a new BufferedImage and draw the Image into it
      BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = bimage.createGraphics();
      g.drawImage(img, 0, 0, null);
      g.dispose();
      return bimage;
   }

   /**
    * Flips a BufferedImage horizontally.
    *
    * @param image The image to flip.
    * @return The horizontally flipped BufferedImage.
    */
   private BufferedImage flipImageHorizontally(BufferedImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
      Graphics2D g = flippedImage.createGraphics();
      g.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
      g.dispose();
      return flippedImage;
   }

   /**
    * Handles player input and updates movement, actions, and shop interactions.
    */
   private void keys() {
      if (GamePanel.keys[KeyEvent.VK_A]) {
         dx -= speed;
         dx = Math.max(dx, -MAX_SPEED - (int) (tempo));
         lastDirection = -1;
      } else if (GamePanel.keys[KeyEvent.VK_D]) {
         dx += speed;
         dx = Math.min(dx, MAX_SPEED + (int) (tempo));
         lastDirection = 1;
      } else {
         dx = 0;
      }

      if (GamePanel.keys[KeyEvent.VK_W] || GamePanel.keys[KeyEvent.VK_SPACE]) {
         if (onGround) {
            dy = -jumpHeight;
            onGround = false;
            isJumping = true;
            tempo += 1;
         }
      } else {
         isJumping = false; 
      }
      if (GamePanel.keys[KeyEvent.VK_E]) {
         if (!swinging) {
            swinging = true;
            hurtbox.x = x + 32 * lastDirection;// lowkey breaks when player goes to far to the right
            hurtbox.y = y;
         }
      } else {
         swinging = false;
      }
      if (GamePanel.keys[KeyEvent.VK_U]) {
         if (coins >= 10) {
            coins -= 10;
            hp += 50;
         }
      }
      if (GamePanel.keys[KeyEvent.VK_I]) {
         if (coins >= 20) {
            coins -= 20;
            MAX_TEMPO += 4.0;
         }
      }
      if (GamePanel.keys[KeyEvent.VK_O]) {
         if (coins >= 30) {
            coins -= 30;
            // make it lighter bruh
         }
      }
   }

   /**
    * Gets the number of coins the player has collected.
    *
    * @return The number of coins.
    */
   public int getCoins() {
      return coins;
   }

   /**
    * Sets the number of coins the player has.
    *
    * @param c The number of coins to set.
    */
   public void setCoins(int c) {
      coins = c;
   }
   public void setHp(int hp) {
      this.hp = hp;
   }  

   /**
    * Checks if the player has invincibility frames (iFrames) active.
    *
    * @return True if iFrames are active, false otherwise.
    */
   public boolean getIFrames() {
      return iFrames;
   }

    /**
    * Draws the player on the screen, including the sprite or a fallback rectangle.
    *
    * @param g        The Graphics object used for rendering.
    * @param cameraX  The x-coordinate of the camera.
    * @param cameraY  The y-coordinate of the camera.
    */
   @Override
   public void draw(Graphics g, int cameraX, int cameraY) {
      g.setColor(Color.BLUE);
      if (sprite != null) {
         g.drawImage(sprite, x - cameraX, y - cameraY, width, height, null);
      } else {
         g.fillRect(x - cameraX, y - cameraY, width, height);
      }
      /*
       * //tempo
       * g.setColor(Color.YELLOW);
       * g.fillRect(200, 650, (int) MAX_TEMPO*25, 25);
       * g.setColor(Color.MAGENTA);
       * g.fillRect(200, 650, (int)(tempo)*25, 25);
       * //health
       * g.setColor(Color.RED);
       * g.fillRect(200, 675, 100*25/10, 25);
       * g.setColor(Color.GREEN);
       * g.fillRect(200, 675, hp*25/10, 25);
       */
      // hurtbox
      // g.setColor(Color.ORANGE);
      // g.fillRect(hurtbox.x - cameraX, hurtbox.y - cameraY, width, height);
   }

   /**
    * Gets the player's current x-coordinate.
    *
    * @return The x-coordinate of the player.
    */
   public int getX() {
      return x;
   }

   /**
    * Gets the player's current y-coordinate.
    *
    * @return The y-coordinate of the player.
    */
   public int getY() {
      return y;
   }

    /**
    * Gets the maximum tempo value for the player.
    *
    * @return The maximum tempo value.
    */
   public double getMAX_TEMPO() {
      return MAX_TEMPO;
   }

   /**
    * Gets the player's current tempo value.
    *
    * @return The current tempo value.
    */
   public double getTempo() {
      return tempo;
   }

   /**
    * Gets the maximum health value for the player.
    *
    * @return The maximum health value.
    */
   public double getMAX_HP() {
      return MAX_HP;
   }
}
