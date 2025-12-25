import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.*;

public class Game {//fix bannana jump glitch
//add speedrun timer
//add final boss 
    public static void main(String[] args) throws IOException {
        System.out.println("wowzas2");

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Jester Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // show title panel first
            TitlePanel title = new TitlePanel();
            title.setTitleActionListener(action -> {
                try {
                    if ("start".equals(action)) {
                        GamePanel gp = new GamePanel();
                        frame.setContentPane(gp);
                        frame.revalidate();
                        frame.pack();
                        gp.requestFocusInWindow();
                    } else if ("test".equals(action)) {
                        GamePanel tp = new GamePanel(1);
                        frame.setContentPane(tp);
                        frame.revalidate();
                        frame.pack();
                        tp.requestFocusInWindow();
                    } else if ("exit".equals(action)) {
                        System.exit(0);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            frame.setContentPane(title);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
