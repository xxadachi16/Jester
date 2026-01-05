
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {
   private BufferedImage sprite;
   int shuffleCount;
   public Enemy(int x, int y, TileMap tileMap) {
      super(x, y, 40, 40, 20, 10);
try {
         sprite = ImageIO.read(getClass().getResource("Sprites/enemy.png"));
      } catch (IOException e) {
         e.printStackTrace();
         sprite = null; // Handle the error appropriately
      }      this.tileMap = tileMap; //lowkey might make two different tilemaps which could cause a dysync if the map updates or cause problems with map random generation
      dx = 2;
      shuffleCount = 0;
   }
   public Enemy(int x, int y, int w, int h, int hp, int d, TileMap tileMap) {
      super(x, y, w, h, hp, d);
      this.tileMap = tileMap;
   }

   @Override
   public void update() {
      if (hp > 0) {
         shuffle();
         move();
      } else {
         x = -100;
         y = -100;
      }
   }
   public void shuffle() {
      if (shuffleCount <= 0) shuffleCount = 100;
      if (shuffleCount < 50)  {
         dx = -2;
      } else {
         dx = 2;
      }
      //shuffleCount--;
      shuffleCount -= (int)(Math.random()*10-4);
   }
   @Override
   public void draw(Graphics g, int cameraX, int cameraY) {

      g.setColor(Color.RED);
      g.drawImage(sprite, x - cameraX, y - cameraY, width, height, null);
   }
}
