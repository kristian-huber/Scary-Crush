package level.particles;

import java.awt.Graphics;
import java.awt.Point;

import level.Candy;

public class ParticleSwappingCandy extends Particle{

	private Point moveVec;
	
	public ParticleSwappingCandy(int movX, int movY) {
		super(Candy.DIM);
		
		moveVec = new Point(movX, movY);
	}

	@Override
	public void paint(Graphics g, Point p) {
		
	}
}
