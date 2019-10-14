package level.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import gfx.TextureManager;
import level.Candy;

public class ParticleFallingCandy extends Particle{

	private Candy type;
	
	public ParticleFallingCandy(Candy type, int totalProgress) {
		super(totalProgress);
		this.type = type;
	}
	
	@Override
	public void update() {
		float percent = getPercentage();

		if (percent < 0.1) {
			progress += 2;
		} else if (percent < 0.25) {
			progress += 4;
		} else if (percent < 0.5) {
			progress += 5;
		} else {
			progress += 8;
		}
	}
	
	public Candy getType() {
		return type;
	}
	
	protected BufferedImage getImage() {
		return TextureManager.getTexture(type.getType());
	}

	@Override
	public void paint(Graphics g, Point p) {
		g.drawImage(getImage(), p.x, p.y + progress, Candy.DIM, Candy.DIM, null);
	}
}
