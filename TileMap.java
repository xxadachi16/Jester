import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TileMap {
    private int[][] map;
    private ArrayList<Tile> tiles;
    private ArrayList<Tile> removeTiles;
    public ArrayList<Integer> enemyPos;
    public int psx;
    public int psy;
    public static final int TILE_SIZE = Tile.SIZE;

    public TileMap(String csvFilePath) throws IOException {
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        loadMap(csvFilePath);
        tiles = new ArrayList<>();
        generateTiles(); // Generate tiles after loading the map
    }

    private void loadMap(String csvFilePath) throws IOException {
        List<int[]> rows = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            int[] row = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
            rows.add(row);
        }
        reader.close();

        map = rows.toArray(new int[0][]);
    }

    private void generateTiles() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int tileType = map[row][col];
                String spritePath = "";
                boolean solid = false;

                if(tileType==1){
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, true, "/Sprites/rock.png"));
                }
                if (tileType == 4){
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/bannana.png", true));
                }
                if (tileType == -1){
                    enemyPos.add(col);
                    enemyPos.add(row);
                }
                if (tileType == -2) {
                    psx = col;
                    psy = row;
                }
            }
        }
    }

    public void draw(Graphics g, int cameraX, int cameraY) {
        for (Tile tile : tiles) {
            tile.draw(g, cameraX, cameraY);
        }
    }
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void removeTile(Tile tile) {
        removeTiles.add(tile);
    }

    public void clearRemovedTiles() {
        for (Tile tile : removeTiles) {
            tiles.remove(tile);
        }
        removeTiles.clear();
    }
}