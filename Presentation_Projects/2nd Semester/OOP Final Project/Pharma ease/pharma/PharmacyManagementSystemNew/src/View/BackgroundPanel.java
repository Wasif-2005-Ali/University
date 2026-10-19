package View;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float transparency; // For background image transparency
    private boolean applyBlackOverlay; // Whether to apply black blur

    public BackgroundPanel(Image backgroundImage, float transparency, boolean applyBlackOverlay) {
        this.backgroundImage = backgroundImage;
        this.transparency = transparency;
        this.applyBlackOverlay = applyBlackOverlay;
        setLayout(null);
    }

    public void setApplyBlackOverlay(boolean applyBlackOverlay) {
        this.applyBlackOverlay = applyBlackOverlay;
        repaint(); // Refresh panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Draw background image
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Conditionally apply black blur
        if (applyBlackOverlay) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }
}

