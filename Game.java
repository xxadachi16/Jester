import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.*;

public class Game {//fix bannana jump glitch
//add speedrun timer
//add final boss 
    public static void main(String[] args) throws IOException {

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
                        GamePanel tp = new GamePanel("0"); //maybe when we test later we might make another constructer so we can do certain boss rooms
                        frame.setContentPane(tp);
                        frame.revalidate();
                        frame.pack();
                        tp.requestFocusInWindow();
                    } else if ("random".equals(action)) {
                        GamePanel rp = new GamePanel("1"); //just to differentiate between the two randoms for now later this should be deleted
                        frame.setContentPane(rp);
                        frame.revalidate();
                        frame.pack();
                        rp.requestFocusInWindow();
                    } else if("random".equals(action.substring(0,6))) {
                        GamePanel rp = new GamePanel(action.substring(6));
                        frame.setContentPane(rp);
                        frame.revalidate();
                        frame.pack();
                        rp.requestFocusInWindow();
                    }else if ("exit".equals(action)) {
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
