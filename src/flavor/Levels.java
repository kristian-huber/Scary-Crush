package flavor;

import java.awt.Color;

import gfx.TextureManager;
import level.Candy;
import level.FinishAnimation;
import level.Level;
import level.particles.ParticleDestroyAnimation;

public class Levels {

	private static final Candy bone = new Candy(Candy.TYPE_BONE, ParticleDestroyAnimation.TYPE_BLACKSMOKE);
	private static final Candy candle = new Candy(Candy.TYPE_CANDLE, ParticleDestroyAnimation.TYPE_EXPLOSION);
	private static final Candy hand = new Candy(Candy.TYPE_HAND, ParticleDestroyAnimation.TYPE_BLACKSMOKE);
	private static final Candy heart = new Candy(Candy.TYPE_HEART, ParticleDestroyAnimation.TYPE_BLACKSMOKE);
	private static final Candy skull = new Candy(Candy.TYPE_SKULL, ParticleDestroyAnimation.TYPE_BLACKSMOKE);
	
	private Level level1;
	private FinishAnimation level1Finish;
	
	public Levels() {
		loadTextures();
		reset();
	}
	
	public Level getCurrentLevel() {
		return level1;
	}
	
	public void reset() {
		level1 = new Level("Level1", "Sign", Color.BLACK, Color.RED, 1000, 10, new String[] {"Awful!", "Deathly"}, new Candy[]{bone, candle, hand, heart, skull});
		level1Finish = new FinishAnimation("Level1", Candy.TYPE_SKULL, Color.BLACK, Color.RED, "You're one of the lucky ones...", "Death is upon us...");
	}
	
	public FinishAnimation getFinishAnimation() {
		return level1Finish;
	}
	
	public void nextLevel() {
		System.out.println("test");
	}
	
	private void loadTextures() {

		// Candy types
		TextureManager.loadTexture("candies/background");
		TextureManager.loadTexture("candies/bone");
		TextureManager.loadTexture("candies/candle");
		TextureManager.loadTexture("candies/hand");
		TextureManager.loadTexture("candies/heart");
		TextureManager.loadTexture("candies/skull");

		// Backgrounds
		TextureManager.loadTexture("backgrounds/Level1");

		// UI
		TextureManager.loadTexture("ui/Sign");

		// Particles
		for (int i = 0; i < 9; i++) {
			TextureManager.loadTexture("animations/blackSmoke/blackSmoke" + i);
			TextureManager.loadTexture("animations/explosion/explosion" + i);
		}
	}
}
