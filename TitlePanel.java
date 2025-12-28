import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class TitlePanel extends JPanel {

    private String date = "12/28/25"; //update day

    private static final long serialVersionUID = 1L;
    public interface TitleActionListener {
        void onAction(String action);
    }

    private TitleActionListener listener;
    private Image backgroundImage = null;
    private boolean scaleBackground = true; // if true, image will be stretched to panel size

    public TitlePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        setBackgroundImageFromFile("Sprites/sadku.png", true);

        // Buttons panel
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton startBtn = new JButton("Start");
        JButton testBtn = new JButton("Test");
        JButton exitBtn = new JButton("Exit");

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener == null) return;
                Object src = e.getSource();
                if (src == startBtn) listener.onAction("start");
                else if (src == testBtn) listener.onAction("test");
                else if (src == exitBtn) listener.onAction("exit");
            }
        };

        startBtn.addActionListener(al);
        testBtn.addActionListener(al);
        exitBtn.addActionListener(al);

        buttons.add(startBtn);
        buttons.add(testBtn);
        buttons.add(exitBtn);

        setLayout(new BorderLayout());
        add(buttons, BorderLayout.SOUTH);
    }

    public void setTitleActionListener(TitleActionListener l) {
        this.listener = l;
    }

    /**
     * Set a background image to be painted behind the title.
     * If {@code scale} is true the image will be drawn to fill the panel.
     */
    public void setBackgroundImage(Image img, boolean scale) {
        this.backgroundImage = img;
        this.scaleBackground = scale;
        repaint();
    }

    /** Convenience: set background image and scale it to panel size. */
    public void setBackgroundImage(Image img) {
        setBackgroundImage(img, true);
    }

    /** Load image from disk and set as background. Returns true on success. */
    public boolean setBackgroundImageFromFile(String path, boolean scale) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            setBackgroundImage(img, scale);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Clear background image and revert to solid background color. */
    public void clearBackgroundImage() {
        this.backgroundImage = null;
        repaint();
    }

    /**
     * Change the title panel background color. Pass null to reset to default (black).
     */
    public void setBackgroundColor(Color color) {
        if (color == null) {
            setBackground(Color.BLACK);
        } else {
            setBackground(color);
        }
        repaint();
    }

    /** Reset background to default black. */
    public void resetBackground() {
        setBackground(Color.BLACK);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // draw image background if present, otherwise clear with background color
        if (backgroundImage != null) {
            if (scaleBackground) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                int ix = (getWidth() - backgroundImage.getWidth(this)) / 2;
                int iy = (getHeight() - backgroundImage.getHeight(this)) / 2;
                g.drawImage(backgroundImage, ix, iy, this);
            }
        } else {
            super.paintComponent(g);
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(Color.MAGENTA);
        g2.setFont(new Font("SansSerif", Font.BOLD, 64));
        String title = "Jester";
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(title)) / 2;
        int y = getHeight() / 3;
        g2.drawString(title, x, y);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        String hint = "Updated " + date;
        int hx = (getWidth() - g2.getFontMetrics().stringWidth(hint)) / 2;
        g2.drawString(hint, hx, y + 60);
    }
}
