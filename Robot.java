
public class Robot extends Model {

   //no default because of TileMap?
   public Robot(int x, int y, TileMap tileMap) {
      super("Robot", "Erudite's Eulogy", "Ruined transformer. Lots of health and tempo but slow", x, y, tileMap, 400, 20, 5, 20, "no sprite D:");
   }


   public void moveAbility() {
      //shoulder bash from deep
      setSpeed(getSpeed() + 5); //maybe change to just solid speed stat
      System.out.println("Dash!");
   }
   public void spellAbility() {
      //rockets
      //hurtbox.setWidth(100);
      System.out.println("Rockets!");
   }
   public void createAbility() {
      //armor up
      hp += 20;
      System.out.println("Armor!");
   }
}
