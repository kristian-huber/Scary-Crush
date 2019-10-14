package level.particles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class ParticleDestroyPoints extends Particle{

	public static final Font FONT = new Font("Chiller", Font.BOLD, 32);
	
	protected Color color;
	private int value;
	
	public ParticleDestroyPoints(int value, int totalProgress) {
		super(totalProgress);
		this.color = new Color(1, 0, 0, 1);
		this.value = value;
	}
	
	public void update() {
		super.update();
		color = new Color(1, 1, 0, 1 - getPercentage());
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public void paint(Graphics g, Point p) {
		g.setFont(ParticleDestroyPoints.FONT);
		g.setColor(color);
		
		g.drawString("" + value, p.x, p.y - progress);
	}
}
