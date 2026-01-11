
public abstract class Model extends Player {

   public String name;
   public String title; //purely for TOUGH ass descriptions we can be based guys remember waguri
   public String description;
   //no default because of TileMap?
   public Model(int x, int y, TileMap tileMap) {
      super(x, y, tileMap);
   }
   public Model(String n, String t, String desc, int x, int y, TileMap tileMap, int hp, int tmp, int spd, int dmg, String sprite) {
      super(x, y, tileMap, hp, tmp, spd, dmg, sprite);
      name = n;
      title = t;
      description = desc;
   }


   public abstract void moveAbility();
   public abstract void spellAbility();
   public abstract void createAbility();

   public String getName() {
      return name;
   }
   public String getTitle() {
      return title;
   }
   public String getDescription() {
    return description;
   }
}
