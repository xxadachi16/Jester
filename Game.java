import java.io.IOException;

import javax.swing.*;

public class Game {//fix bannana jump glitch
//add speedrun timer
//add final boss 
    public static void main(String[] args) throws IOException {
        System.out.println("wowzas");
        JFrame frame = new JFrame("Jester Game");
        GamePanel panel = new GamePanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
