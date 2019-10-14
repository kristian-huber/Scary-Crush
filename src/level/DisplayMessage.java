package level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import level.particles.Particle;

public class DisplayMessage extends Particle{

	public static final Font FONT = new Font("Chiller", Font.BOLD, 64);
	
	private Color color;
	private String msg;
	
	public DisplayMessage(String msg, int totalProgress) {
		super(totalProgress);
		this.msg = msg;
	}

	public void update() {
		progress++;
		color = new Color(1, 1, 0, 1 - (float)(progress)/(float)(totalProgress));
	}
	
	public String getText() {
		return msg;
	}

	public Color getColor() {
		return color;
	}
	
	@Override
	public void paint(Graphics g, Point p) {
		
	}
}
