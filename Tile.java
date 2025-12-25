import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Tile {
    public static final int SIZE = 32;
    private int x, y;
    private boolean solid;
    private BufferedImage sprite;
    private boolean isCoin;


    public Tile(int x, int y, boolean solid, String spritePath) {
        this.x = x;
        this.y = y;
        this.solid = solid;
        this.isCoin = false;
        try {
            this.sprite = ImageIO.read(getClass().getResource(spritePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.sprite = null;
        }
    }

    public Tile(int x, int y, boolean solid, String spritePath, boolean isCoin) {
        this.x = x;
        this.y = y;
        this.solid = solid;
        this.isCoin = isCoin;
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

    public boolean isCoin() {
        return isCoin;
    }
}
