package level.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import gfx.TextureManager;
import level.Candy;

public class ParticleDestroyAnimation extends Particle {

	public static final String TYPE_BLACKSMOKE = "blackSmoke";
	public static final String TYPE_EXPLOSION = "explosion";

	private String type;

	public ParticleDestroyAnimation(String animation, int totalFrames) {
		super(totalFrames * 3);
		this.type = animation;
	}

	public void paint(Graphics g, Point p) {
		g.drawImage(getImage(), p.x, p.y, Candy.DIM, Candy.DIM, null);
	}

	protected BufferedImage getImage() {		
		int frame = progress / 3;
		return TextureManager.getTexture(type + frame);
	}
}
