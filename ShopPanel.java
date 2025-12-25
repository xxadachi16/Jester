import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private Player player;

    public ShopPanel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

      

        // Tempo bar
        g.setColor(Color.YELLOW);
        g.fillRect(200, 25, (int) player.getMAX_TEMPO() * 25, 25);
        g.setColor(Color.MAGENTA);
        g.fillRect(200, 25, (int) (player.getTempo()) * 25, 25);

        // Health bar
        g.setColor(Color.RED);
        g.fillRect(200, 50, 100 * 25 / 10, 25);
        g.setColor(Color.GREEN);
        g.fillRect(200, 50, player.getHp() * 25 / 10, 25);

       
    }
}
