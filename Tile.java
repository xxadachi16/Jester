import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {
    public static final int SIZE = 32;
    private int x, y;
    private boolean solid;
    private BufferedImage sprite;
    enum TileType {
         AIR, //shouldn't really be used just used to ofset index to make it match with csv
         GROUND,
         SPIKE,
         COLLECTABLE,
         BOOST,
         HEALTH,
         SLOW,
         TROPHY,
         PLATFORM
   }
   private TileType type;

    public Tile(int x, int y, boolean solid, String spritePath) {
        this.x = x;
        this.y = y;
        this.solid = solid;
        if (solid) {
            this.type = TileType.GROUND;
        } else {
            this.type = TileType.COLLECTABLE;
        }
        try {
            this.sprite = ImageIO.read(getClass().getResource(spritePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.sprite = null;
        }
    }


    public Tile(int x, int y, boolean solid, String spritePath, int tileType) {
        this.x = x;
        this.y = y;
        this.solid = solid;
        if (tileType == -4) {
            this.type = TileType.TROPHY;
        } else if(tileType == 9) {
            this.type = TileType.PLATFORM;
        } else { //temporary solution
            this.type = TileType.values()[tileType];
        }
        
        try {
            this.sprite = ImageIO.read(getClass().getResource(spritePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.sprite = null;
        }
    }

    public void draw(Graphics g, int cameraX, int cameraY) {
        if (sprite != null) {
            g.drawImage(sprite, x - cameraX, y - cameraY, SIZE, SIZE, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - cameraX, y - cameraY, SIZE, SIZE);
        }
    }

    public boolean isSolid() {
        return solid;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getType() {
        if (type == TileType.TROPHY) return -4;
        if (type == TileType.PLATFORM) return 9;
        return type.ordinal();
    }
    public void setType(int tileType) { //don't use this until confident the enum stuff is figured out
        this.type = TileType.values()[tileType];
    }
}
