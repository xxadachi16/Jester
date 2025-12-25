import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIBarPanel extends JPanel implements ActionListener {
    private Player player;
    private Timer timer;
    int time;

    public UIBarPanel(Player player) {
        this.time = 30;
        this.timer = new Timer(1000, this);
        timer.start();
        this.player = player;
        setPreferredSize(new Dimension(900, 100));
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

           //Coin label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Coins: " + player.getCoins(), 10, 20);

        // Time label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Time: " + time/60 + ":" + time % 60, 10, 50);

    }

    @Override
   public void actionPerformed(ActionEvent e) {
      time--;
      if ( time <= 0) {
         player.setHp(0);
         player.setCoins(0);
         time = 30;
      }
   }

   public int getTime() {
      return time;
   }
}
