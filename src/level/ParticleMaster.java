package level;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import level.particles.Particle;
import level.particles.ParticleFallingCandy;
import level.particles.ParticleSwappingCandy;

public class ParticleMaster {

	private HashMap<Point, ArrayList<Particle>> particles;
	private HashMap<Point, Boolean> updatedSince;

	public ParticleMaster() {
		particles = new HashMap<Point, ArrayList<Particle>>();
		updatedSince = new HashMap<Point, Boolean>();
	}

	public void addParticle(Point p1, Particle p2) {
		if (particles.containsKey(p1)) {
			particles.get(p1).add(p2);
		} else {
			ArrayList<Particle> parts = new ArrayList<Particle>();
			parts.add(p2);
			particles.put(p1, parts);
		}

		if (p2 instanceof Particle) {
			updatedSince.put(p1, false);
		}
	}

	public boolean isStable() {
		for(ArrayList<Particle> list : particles.values()) {
			for(Particle p : list) {
				if(p instanceof ParticleFallingCandy) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isFallingAt(int i, int j) {
		if(!particles.containsKey(new Point(i, j)))
			return false;
		
		for (Particle p : particles.get(new Point(i, j))) {
			if (p instanceof ParticleFallingCandy)
				return true;
		}

		return false;
	}
	
	public boolean isSwappingAt(int i, int j) {
		if(!particles.containsKey(new Point(i, j)))
			return false;
		
		for (Particle p : particles.get(new Point(i, j))) {
			if (p instanceof ParticleSwappingCandy)
				return true;
		}

		return false;
	}
	
	public boolean isParticleAt(int i, int j) {
		Point p = new Point(i, j);
		return particles.containsKey(p) && particles.get(p).size() > 0;
	}

	public void update(Candy[][] grid) {
		for (Point point : particles.keySet()) {

			Iterator<Particle> iterator = particles.get(point).iterator();
			while (iterator.hasNext()) {
				Particle particle = iterator.next();

				if (particle.isDone()) {
					if (particle instanceof ParticleFallingCandy) {						
						if (!updatedSince.get(point)) {
							grid[point.x][point.y] = null;
						}

						grid[point.x][point.y
								+ particle.getTotalProgress() / Candy.DIM] = ((ParticleFallingCandy) particle)
										.getType();
						updatedSince.put(new Point(point.x, point.y + particle.getTotalProgress() / Candy.DIM), true);
					}

					iterator.remove();
				} else {
					particle.update();
				}
			}
		}
	}
	
	public void render(Graphics g, int xOff, int yOff) {
		try {
			for(Point point : particles.keySet()) {
				
				int x = xOff + point.x * Candy.DIM;
				int y = yOff + point.y * Candy.DIM;
				
				for(Particle particle : particles.get(point)) {
					particle.paint(g, new Point(x, y));		
				}
			}	
		}catch(Exception e) {
			
		}
	}
}
