package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gfx.TextureManager;
import gfx.Window;
import level.particles.ParticleDestroyPoints;

public class FinishAnimation {

	private Color background;
	private Color foreground;
	private BufferedImage character;
	private BufferedImage star;
	private String flavorText, flavorTextBad;
	
	private static final int HEIGHT = 400;
	
	private static final int STAR_SIZE = 128;
	
	private int tick = 0;
	
	public FinishAnimation(String character, String stars, Color background, Color foreground, String flavorText, String flavorTextBad) {
		this.character = TextureManager.getTexture(character);
		this.star = TextureManager.getTexture(stars);
		this.background = background;
		this.foreground = foreground;
		this.flavorText = flavorText;
		this.flavorTextBad = flavorTextBad;
	}
	
	public void paint(Graphics g, boolean success, int stars) {
		tick++;
		
		g.setColor(background);
		if(tick < 45) {
			double radians = Math.toRadians(tick * 2);
			g.fillRect((int) (Math.sin(radians) * Window.WIDTH) - Window.WIDTH, Window.HEIGHT / 2 - HEIGHT / 2, Window.WIDTH, HEIGHT);
		}else {
			int y = Window.HEIGHT / 2 - HEIGHT / 2;
			g.fillRect(0, y, Window.WIDTH, HEIGHT);
			g.drawImage(character, 15, y + 10, 300, HEIGHT - 20, null);
			
			g.setColor(foreground);
			g.setFont(ParticleDestroyPoints.FONT);
			if(success) {
				g.drawString(flavorText, 325, y + 30);
				g.drawString("Press enter to go to the next level", 325, y + HEIGHT - 20);
			}else {
				g.drawString(flavorTextBad, 325, y + 30);
				g.drawString("Press enter to restart the level", 325, y + HEIGHT - 20);
			}
			
			int add = 0;
			if(tick < 55) {
				add = 55 - tick;
			}
			
			for(int i = 0; i < stars; i++) {
				g.drawImage(this.star, 375 + STAR_SIZE * i, y + 100, STAR_SIZE + 5 * add, STAR_SIZE + 5 * add, null);
			}
		}
	}
}
