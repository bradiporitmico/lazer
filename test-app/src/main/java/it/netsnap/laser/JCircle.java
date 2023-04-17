package it.netsnap.laser;

import java.io.Serializable;
import javax.swing.JComponent;
import java.awt.*;

public class JCircle extends JComponent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Color color;
	private int radius;
	
	public JCircle() {
			this.setColor(Color.GREEN); 
			this.setRadius(10);
	}

	@Override
	public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(color);
			g.fillOval((getWidth() - radius) / 2, (getHeight() - radius) / 2, radius, radius);
	}

	public Color getColor() {
			return color;
	}

	public void setColor(Color color) {
			this.color = color;
			repaint();
	}

	public int getRadius() {
			return radius;
	}

	public void setRadius(int radius) {
			this.radius = radius;
			repaint();
	}
}