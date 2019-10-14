package level.particles;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Particle {

	protected int totalProgress;
	protected int progress;
	
	public Particle(int totalProgress) {
		this.totalProgress = totalProgress;
		this.progress = 0;
	}
	
	public void update() {
		progress++;
	}
	
	public boolean isDone() {
		return progress >= totalProgress;
	}
	
	public float getPercentage() {
		return (float)progress/(float)totalProgress;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public int getTotalProgress() {
		return totalProgress;
	}
	
	public abstract void paint(Graphics g, Point p);
}
