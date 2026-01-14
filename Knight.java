
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Knight extends Enemy {
   private BufferedImage sprite;
   private int attackCountdown;
   private final int numberOfAttacks = 3;
   public Knight(int x, int y, TileMap tileMap) {
      super(x, y, 120, 120, 100, 10, tileMap);
      
      try {
         sprite = ImageIO.read(getClass().getResource("Sprites/knight.jpg"));
      } catch (IOException e) {
         e.printStackTrace();
         sprite = null; // Handle the error appropriately
      }     this.tileMap = tileMap; //lowkey might make two different tilemaps which could cause a dysync if the map updates or cause problems with map random generation
      shuffleCount = 0;
      attackCountdown = 500;
      dy = 10;
   }

   @Override
   public void update() {
      
      if (hp > 0) {
         determineAction();
         x += dx;
         y += dy;
         //move(); commented out so he can go through the floor and not fall from gravity
      } else {
         x = -100; //I am not a smart fella
         y = -100;
      }
   }
   @Override
   public void shuffle() {
      if (y < 150) dy += 1;
      if (y > 600) dy -= 1;
   }

   private void determineAction() { //really only making these private because it doesn't make sense for them to be public
      if (attackCountdown == 0) {
         attackCountdown = 500;
         attack();
      } else {
         if (Math.random() < 0.001) { //go big or go home
            System.out.println("Early Cycle!");
            attack();
         } else { //believe in the nest
            attackCountdown--;
            shuffle();
         }
      }
   }

   private void attack() {
      int n = attackSelector();
      switch (n) {
         case 0:
            System.out.println("Projectile");
            break;
         case 1:
            System.out.println("Hazards");
            break;
         case 2:
            System.out.println("Body Slam");
            break;
      }
   }

   private int attackSelector() {
      return (int)(Math.random()*numberOfAttacks);
   }
   @Override
   public void draw(Graphics g, int cameraX, int cameraY) {
   
      g.setColor(Color.RED);
      //g.fillRect(x - cameraX, y - cameraY, width, height);
      g.drawImage(sprite, x - cameraX, y - cameraY, width, height, null);
   }
}
