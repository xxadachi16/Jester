import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TileMap {
    private int[][] map;
    private String seed;
    private static final int ROOM_COUNT = 5;
    private static int roomNum = 0;
    private ArrayList<Tile> tiles;
    private ArrayList<Tile> removeTiles;
    public ArrayList<Integer> enemyPos;
    public int psx;
    public int psy;
    public static final int TILE_SIZE = Tile.SIZE;

    public TileMap() throws IOException {
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        map = generateRandomMap("veryEpicSeed"); //why yellow
        tiles = new ArrayList<>();
        map = createBoundries(map);
        psx = 3;
        psy = map.length - 4;
        generateTiles(); // Generate tiles after loading the map
    }

    public TileMap(String s, int n) throws IOException {
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        roomNum = n; //primarily to deal with the overloaded constructors n should almost always be 0
        map = generateRandomMap(s);
        tiles = new ArrayList<>();
        map = createBoundries(map);
        generateTiles(); // Generate tiles after loading the map
        psx = 3;
        psy = map.length - 4; //PRAY it is not in a wall we really need to figure out this grid stuff
    }

    public TileMap(int[][] w, boolean b) throws IOException { //generate based on world
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        map = worldLoader(w);
        tiles = new ArrayList<>();
        map = createBoundries(map);
        generateTiles(); // Generate tiles after loading the map
    }

    public TileMap(String csvFilePath) throws IOException {
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        loadMap(csvFilePath);
        tiles = new ArrayList<>();
        generateTiles(); // Generate tiles after loading the map
    }

    public TileMap(int[][] m) {
        enemyPos = new ArrayList<>();
        removeTiles = new ArrayList<>();
        map = m;
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

    private int[][] loadMap(String csvFilePath, boolean isRoom) throws IOException { //merge this two methods later
        List<int[]> rows = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            int[] row = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
            rows.add(row);
        }
        reader.close();

        return rows.toArray(new int[0][]); //split might lowkey be better here
    }

    private void generateTiles() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int tileType = map[row][col];
                String spritePath = "";
                boolean solid = false;
                if (tileType == -1){
                    enemyPos.add(col);
                    enemyPos.add(row);
                }
                if (tileType == -2) {
                    psx = col;
                    psy = row;
                }
                if (tileType == -3) {
                    //placeholder for door thingy for randomgen
                }
                if (tileType == -4) {
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/trophy.jpg", -4)); //I'm sure this inconsistant naming won't cause any trouble :D
                }
                if(tileType==1){ //ground
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, true, "/Sprites/rock.png"));
                }
                if (tileType == 2){ //spike
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, true, "/Sprites/spike.png", 2));
                }
                if (tileType == 3){ //coin
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/bannana.png", 3));
                }
                if (tileType == 4) { //boost
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/boost.jpg", 4)); //woe is me
                }
                if (tileType == 5) { //heal
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/basedguri.png", 5)); 
                }
                if (tileType == 6) { //slow
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, false, "/Sprites/slow.png", 6)); 
                }
                if (tileType == 9) { //platform
                    tiles.add(new Tile(col * TILE_SIZE, row * TILE_SIZE, true, "/Sprites/platform.png", 9)); 
                }
                
            }
        }
    }

    public int[][] createBoundries(int[][] m) {
        // Copy the original map into the center of the bordered map
        for (int r = 0; r < m.length; r++) {
            if (r == 0 || r == m.length - 1) {
            for (int c = 0; c < m[0].length; c++) {
                m[r][c] = 1;
            }
            } else {
                m[r][0] = 1;
                m[r][m[0].length - 1] = 1;
            }
        }

        return m;
    }

    /*
    all the generation stuff will be in this folder and the orignal final product stuff outside

    12/28/25

    first draft of random generation with seeds

    seedInput(String s) takes in any characters from the player
    seedFormater(String s) turns it into a usable seed that the player can use using the binary blah blah blah it doesn't really matter as long as no 1 x gives 2 y returns a number probably
    roomRandom(int n) 
    has a static value that ranges from 1-the total number of rooms created, 
    takes the seed and puts it through an abirtrary formula mod(rooms possible) to pick a room then update a number, 
    if the room is incompatible just go again, 
    can and should be used for other things even though that would techinically make all random things bound by the room number,
    returns an integer

    worldCreator(int n) takes value from roomRandom and creates a new tileMap,
    there's a big static map "world" containing on the maps currently loaded
    EX: 
    [ -1, -1, -1,
      -1, -1, -1,
       0, -1, -1, ]
    start at world[world.length-1-r][c] and fill this in with room0 where r = c = 0
    iterate through this filling in each -1 with a random room iterating r for every c
    end at world[0][world[0].length-1] = -2 which is the goal room 
    this will make it so the player always starts at the bottom and works his way to the top corner which takes advantage of the tempo mechanic

    ! ! !
    MAKE SURE EDGE ROOMS ARE DIFFERENT THAN THE MIDDLE ROOMS BECAUSE THE DOORS ARE DIFFERENT
    ! ! !

    if you want to have rooms that are fancy shapes it looks the same on the macro level but on the micro level just make sure it completes
    EX: room 2 would actually just be 2 rooms put together with predictable exits

    if you want the exits to not be predictable create the whole map and then remove chunks between each room

    worldGenerator(int[][] w) takes worldCreator and turns it into a map
    the advantage of having worldCreator and worldGenerator seperate even though world isn't static is uhhh so you can put in strings and worlds to get the same result which may come in useful later most likely probably
    maybe do all this on a loop before player is loaded
    */ 

    public int[][] generateRandomMap(String seed)  throws IOException {
        // Simple random map generation based on seed
       int intSeed = seedFormater(seed);
       return worldLoader(worldCreator(intSeed));
    }

    public int seedFormater(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            n += s.codePointAt(i); //maybe learn a little bit of computer science but it should work
        }
        return n % 1000000;
    }

    public static int roomRandom(int intSeed) {
        //make sure to add and then random to we always start with room 0 for now
        roomNum = ((roomNum + 7)*intSeed) % ROOM_COUNT; // Simple deterministic update
        return roomNum;
        /* 
        // Simple pseudo-random number generator based on a static value
        long a = 1664525;
        long c = 1013904223;
        long m = (1L << 32);
        long seed = System.currentTimeMillis() % m; // Use current time as seed
        seed = (a * seed + c) % m;
        return (int)(seed % n); */
    }

    public int[][] worldCreator(int intSeed) {
        int worldRows = 4;
        int worldCols = 4;
        int[][] world = new int[worldRows][worldCols];
        for (int r = 0; r < worldRows; r++) {
            for (int c = 0; c < worldCols; c++) { //make the edge rooms different
                if (r == worldRows - 1 && c == 0) {
                    world[r][c] = 0; // Start room
                } else if (r == 0 && c == worldCols - 1) {
                    world[r][c] = -2; // Goal room
                } else if (r == 0 || r == worldRows - 1 || c == 0 || c == worldCols - 1) { // Edge rooms
                    int rR = roomRandom(intSeed);
                    //not needed for now because we creatre boundries
                    /* while (rR > 7) { //O(n^3) :smile_blushing: we can and should fix this
                        rR = roomRandom(intSeed);
                    } */
                    world[r][c] = rR; // Inbetween rooms
                } else {
                    world[r][c] = roomRandom(intSeed);
                }
            }
        }
        for (int r = 0; r < worldRows; r++) {
            for (int c = 0; c < worldCols; c++) {
                System.out.print(world[r][c] + " ");
            }
            System.out.println();
        }
        return world;
        
    }

    public int[][] worldLoader(int[][] w) throws IOException {
        // For simplicity, assume each room is a 20x20 grid
        int roomSize = 20;
        int worldRows = w.length;
        int worldCols = w[0].length;
        int mapRows = worldRows * roomSize;
        int mapCols = worldCols * roomSize;
        int[][] fullMap = new int[mapRows+1][mapCols+1]; //resist the urge to 1 line

        for (int r = 0; r < worldRows; r++) {
            for (int c = 0; c < worldCols; c++) {
                int[][] roomMap = loadMap("Maps/Seed/randRoom" + w[r][c] + ".csv", true); // Generate room map based on type

                // Place roomMap into fullMap
                for (int i = 0; i < roomSize; i++) {
                    for (int j = 0; j < roomSize; j++) {
                        fullMap[r * roomSize + i][c * roomSize + j] = roomMap[i][j];
                    }
                }
            }
        }

        return fullMap;
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

    public int getXBounds() {
        return map[0].length * TILE_SIZE;
    }
    
    public int getYBounds() {
        return map.length * TILE_SIZE;
    }
}