import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
   protected int x, y, width, height;
   protected int dx, dy;
   protected int hp;
   protected int damage;
   protected boolean onGround;
   protected TileMap tileMap;//ts should be static in a different class or something
   protected final int TILE_SIZE = 32;
   protected int gravity= 1;

   public Entity(int x, int y, int width, int height, int hp, int damage) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.hp = hp;
      this.damage = damage;
    }

  


   public abstract void update();
   public abstract void draw(Graphics g, int cameraX, int cameraY);

   public void move() {
      x += dx;
      y += dy;
   
      dy += gravity;
   
      collisionDetection();
   }
   public void entityCollision(Entity e) {
      Rectangle bounds = new Rectangle(x, y, width, height);
      Rectangle bounds2 = new Rectangle(e.x, e.y, e.width, e.height);
      if (bounds.intersects(bounds2)) hp -= damage;
   }
   protected void collisionDetection() {
      for (Tile tile : tileMap.getTiles()) {
         if (tile.isSolid()) {
            Rectangle r = tile.getBounds();
            Rectangle bounds = new Rectangle(x, y, width, height);
         
            if (bounds.intersects(r)) {
               if(bounds.y < r.y){
                  y = r.y - height;
                  dy = 0;
                  onGround = true; //collision is broken but it's lowkey a feature
               } else if (dy < 0 && bounds.y > r.y) {
                  y = r.y + r.height;
                  dy = 0;
               } else if (dx > 0 && bounds.x < r.x) {
                  x = r.x - width;
               } else if (dx < 0 && bounds.x > r.x) {
                  x = r.x + r.width;
               }
            }
         }
      }
   }
   public int getHp() {
      return hp;
   }
   public void setHp(int h) {
      hp = h;
   }
}

