
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Knight extends Enemy {
   private BufferedImage sprite;
   public Knight(int x, int y, TileMap tileMap) {
      super(x, y, 120, 120, 100, 10, tileMap);
      
      try {
         sprite = ImageIO.read(getClass().getResource("Sprites/knight.jpg"));
      } catch (IOException e) {
         e.printStackTrace();
         sprite = null; // Handle the error appropriately
      }     this.tileMap = tileMap; //lowkey might make two different tilemaps which could cause a dysync if the map updates or cause problems with map random generation
      shuffleCount = 0;
      dy = 10;
   }

   @Override
   public void update() {
      if (hp > 0) {
         shuffle();
         x += dx;
         y += dy;
   
         //move(); commented out so he can go through the floor and not fall from gravity
      } else {
         x = -100;
         y = -100;
      }
   }
   public void shuffle() {
      if (y < 150) dy += 1;
      if (y > 600) dy -= 1;
   }
   @Override
   public void draw(Graphics g, int cameraX, int cameraY) {
   
      g.setColor(Color.RED);
      //g.fillRect(x - cameraX, y - cameraY, width, height);
      g.drawImage(sprite, x - cameraX, y - cameraY, width, height, null);
   }
}
