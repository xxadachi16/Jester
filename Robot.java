
public class Robot extends Model {

   //no default because of TileMap?
   public Robot(int x, int y, TileMap tileMap) {
      String n = "Robot";
      String t = "Erudite's Eulogy"; //really just wanted to use the word erudite
      String d = "Ruined transformer. Lots of health and tempo but slow";
      super(x, y, tileMap);
      //super(n, t, d, x, y, tileMap, 400, 20, 5, 20, "no sprite D:");
   }


   public void moveAbility() {
      //shoulder bash from deep
      dx += 5; //maybe change to just solid speed stat
   }
   public void spellAbility() {
      //rockets
   }
   public void createAbility() {
      //armor up
   }
}
