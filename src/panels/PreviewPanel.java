package panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	/**
	 * Creates a PreviewPanel
	 */
	public PreviewPanel() {
		this.setPreferredSize(new Dimension(56, 112));
	}
	
	/**
	 * Draws the preview image
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null){
			int x = (getWidth() / 2) - (image.getWidth() / 2);
			int y = (getHeight() / 2) - (image.getHeight() / 2);
			g.drawImage(image, x, y, null);
		}
	}
	
	/**
	 * Sets the image to be draw
	 * @param image
	 */
	public void setImage(BufferedImage image){
		this.image = image;
		repaint();
	}

}
