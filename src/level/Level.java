package level;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gfx.TextureManager;
import gfx.Window;
import level.particles.ParticleDestroyAnimation;
import level.particles.ParticleDestroyPoints;
import level.particles.ParticleFallingCandy;

public class Level {

	private BufferedImage backgroundImage;
	private BufferedImage ui;
	private BufferedImage drawing;

	private Candy[][] grid;

	private int xOff = Candy.DIM * 2;
	private int yOff = 38 + Candy.DIM * 2;

	private boolean isSwapping = false;
	private boolean isUndoing = false;
	private Point c1 = new Point(0, 0);
	private Point c2 = new Point(0, 0);
	private int progress;
	private int dir;

	private ParticleMaster particleMaster;

	private DisplayMessage currentMsg;

	private int scorePerSecond = 0;
	private int ticks = 0;

	private int score = 0;
	private int movesLeft = 0;
	int max;

	private boolean movesFlag = false;

	private Color foreground;
	private Color background;

	private Candy[] candyInstances;
	private String[] flavorTexts;

	/**
	 * @description: Initializes all of the variables for the game
	 * 
	 * @param background
	 *            - Name of the file you want to load for the background. It will be
	 *            under /assets/backgrounds/NAME.png
	 * @param width
	 *            - How many candies wide the game is
	 * @param height
	 *            - How many candies tall the game is
	 */
	public Level(String background, String ui, Color foreground, Color backgroundColor, int max, int moves, String[] flavorTexts, Candy[] candyInstances) {

		// Initialize the BufferedImage that everything will be rendered on to
		drawing = new BufferedImage(Window.WIDTH, Window.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		backgroundImage = TextureManager.getTexture(background);
		this.ui = TextureManager.getTexture(ui);
		this.candyInstances = candyInstances;
		this.flavorTexts = flavorTexts;
		
		// Initialize the ParticleMaster
		particleMaster = new ParticleMaster();

		// Fill the level in
		initializeLevel(9, 7);
		this.max = max;
		this.movesLeft = moves;

		this.foreground = foreground;
		this.background = new Color(backgroundColor.getRed() / 255f, backgroundColor.getGreen() / 255f,
				backgroundColor.getBlue() / 255f, 0.5f);
	}

	/**
	 * @description: Populates the level with candies, none of which are in groups
	 *               of 3 or more
	 * 
	 * @param width
	 *            - How many candies wide the game is
	 * @param height
	 *            - How many candies tall the game is
	 */
	private void initializeLevel(int width, int height) {

		// Initialize the grid
		grid = new Candy[width][height];

		// Fill the grid with random candies
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grid[i][j] = generateCandy();
			}
		}

		// While there are groups of 3, fill the empty spaces with candies
		while (removeAllGroups(false)) {

			// Check each space to see if its null
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {

					// Populate it if it is null
					if (grid[i][j] == null)
						grid[i][j] = generateCandy();
				}
			}
		}
	}

	/**
	 * @description: Uses a factory pattern to create a random candy
	 * 
	 * @return: One of the 5 instances of candies in Window.java
	 */
	private Candy generateCandy() {
		int rand = (int) (candyInstances.length * Math.random());
		return candyInstances[rand];
	}

	private boolean constainsNulls() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if(grid[i][j] == null)
					return true;
			}
		}
		return false;
	}
	
	private void makeThingsFall() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {

				if (particleMaster.isFallingAt(i, j) || grid[i][j] == null) {
					continue;
				}

				int numToMoveDown = 0;
				try {
					int currentY = j;
					while (grid[i][currentY + 1] == null) {
						currentY++;
						numToMoveDown++;
					}
				} catch (Exception e) {

				}

				// If it has to fall
				if (numToMoveDown > 0) {
					particleMaster.addParticle(new Point(i, j),
							new ParticleFallingCandy(grid[i][j], numToMoveDown * Candy.DIM));

					// Need to make it so that everything above falls too
					int y = j;
					while (y > 0) {
						y--;

						// What if there are multiple nulls?
						if (grid[i][y] != null) {
							particleMaster.addParticle(new Point(i, y),
									new ParticleFallingCandy(grid[i][y], numToMoveDown * Candy.DIM));
						}
					}
				}
			}
		}
	}

	private boolean removeAllGroups(boolean points) {
		boolean removedSomething = false;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (isInGroup(i, j, points))
					removedSomething = true;
			}
		}

		return removedSomething;
	}

	private boolean removeAllGroups() {
		return removeAllGroups(true);
	}

	private boolean isInGroup(int x, int y, boolean givePoints) {
		int curX = x, curY = y;
		int streak = 0;

		ArrayList<Point> group = new ArrayList<Point>();

		Candy type = grid[x][y];

		// Check up
		while (curY >= 0) {
			if (grid[curX][curY] == type && !particleMaster.isFallingAt(curX, curY)) {
				streak++;
				group.add(new Point(curX, curY));
			} else {
				break;
			}
			curY--;
		}

		// Check down
		curY = y + 1;
		while (curY < grid[0].length) {
			if (grid[curX][curY] == type && !particleMaster.isFallingAt(curX, curY)) {
				streak++;
				group.add(new Point(curX, curY));
			} else {
				break;
			}
			curY++;
		}

		// Check if there is 3 or more of a kind
		if (streak >= 3 && type != null) {
			// Test
			for (Point p : group) {
				if (type.getAnimation() != null && givePoints) {
					particleMaster.addParticle(new Point(p.x, p.y),
							new ParticleDestroyAnimation(type.getAnimation(), 9));
				}
				/**
				 * Removing here
				 */
				grid[p.x][p.y] = null;
				if (givePoints) {
					particleMaster.addParticle(new Point(p.x, p.y), new ParticleDestroyPoints(100 * streak, 100));
					scorePerSecond += 100 * streak;
					score += 100 * streak;
				}
			}
			return true;
		}

		// Reset variables
		curY = y;
		streak = 0;
		group.clear();

		// Check left
		while (curX >= 0) {
			if (grid[curX][curY] == type && !particleMaster.isFallingAt(curX, curY)) {
				streak++;
				group.add(new Point(curX, curY));
			} else {
				break;
			}
			curX--;
		}

		// Check left
		curX = x + 1;
		while (curX < grid.length) {
			if (grid[curX][curY] == type && !particleMaster.isFallingAt(curX, curY)) {
				streak++;
				group.add(new Point(curX, curY));
			} else {
				break;
			}
			curX++;
		}

		// Check if there is 3 or more of a kind
		if (streak >= 3 && type != null) {
			// Test
			for (Point p : group) {
				if (type.getAnimation() != null && givePoints) {
					particleMaster.addParticle(new Point(p.x, p.y),
							new ParticleDestroyAnimation(type.getAnimation(), 9));
				}
				
				/**
				 * Removing here
				 */
				grid[p.x][p.y] = null;
				if (givePoints) {
					particleMaster.addParticle(new Point(p.x, p.y), new ParticleDestroyPoints(100 * streak, 100));
					scorePerSecond += 100 * streak;
					score += 100 * streak;
				}
			}
			return true;
		}

		return false;
	}

	public void update() {

		for (int i = 0; i < grid.length; i++) {
			if (grid[i][0] == null)
				grid[i][0] = generateCandy();
		}

		if (currentMsg != null) {
			if (currentMsg.isDone()) {
				currentMsg = null;
			} else {
				currentMsg.update();
			}
		}

		ticks++;
		if (ticks >= 100) {
			if (scorePerSecond > 1000 && currentMsg == null) {
				int rand = (int) (flavorTexts.length * Math.random());
				currentMsg = new DisplayMessage(flavorTexts[rand], 100);
			}

			ticks = 0;
			scorePerSecond = 0;
		}

		removeAllGroups();
		makeThingsFall();

		// Swap things
		if (isSwapping) {
			if (progress >= Candy.DIM) {
				isSwapping = false;
				Candy temp = grid[c1.x][c1.y];
				grid[c1.x][c1.y] = grid[c2.x][c2.y];
				grid[c2.x][c2.y] = temp;
				progress = 0;
				if (!isInGroup(c1.x, c1.y, true) && !isInGroup(c2.x, c2.y, true) && !isUndoing) {
					isSwapping = true;
					Point temp2 = c1;
					c1 = c2;
					c2 = temp2;
					if (dir % 2 == 0) {
						dir--;
					} else {
						dir++;
					}
					isUndoing = true;
					movesFlag = true;
				} else {
					if (!movesFlag) {
						movesLeft--;
					} else {
						movesFlag = false;
					}
					isUndoing = false;
				}
			} else {
				progress += 3;
			}
		}

		particleMaster.update(grid);
	}

	public void onDrag(Point start, Point end) {
		if (isSwapping)
			return;

		try {
			int xDiff = end.x - start.x;
			int yDiff = end.y - start.y;

			if (Math.abs(xDiff) < 10 && Math.abs(yDiff) < 10) {
				return;
			}

			int i = (start.x - xOff) / Candy.DIM;
			int j = (start.y - yOff) / Candy.DIM;

			if (Math.abs(xDiff) < Math.abs(yDiff)) {
				if (yDiff < 0) {
					c2 = new Point(i, j - 1);
					dir = 1;
				} else {
					c2 = new Point(i, j + 1);
					dir = 2;
				}
			} else {
				if (xDiff > 0) {
					c2 = new Point(i + 1, j);
					dir = 3;
				} else {
					c2 = new Point(i - 1, j);
					dir = 4;
				}
			}

			// Error checking
			grid[c2.x][c2.y].getType();

			isSwapping = true;
			c1 = new Point(i, j);
			progress = 0;

		} catch (Exception e) {
			c2 = new Point(0, 0);
			System.out.println("[Level.java/onDrag()]: Not on grid");
		}
	}

	public BufferedImage getDrawing() {
		Graphics g = drawing.getGraphics();
		g.drawImage(backgroundImage, 0, 0, Window.WIDTH, Window.HEIGHT, null);

		g.drawImage(ui, 5, 38, Window.WIDTH / 3, 100, null);
		g.drawImage(ui, -3 + 2 * Window.WIDTH / 3, 38, Window.WIDTH / 3, 100, null);

		g.setColor(foreground);
		g.setFont(DisplayMessage.FONT.deriveFont(Font.BOLD, 36));
		g.drawString("Score:", 25, 80);
		g.setFont(DisplayMessage.FONT.deriveFont(Font.BOLD, 28));
		g.drawString(score + "/" + max, 50, 110);

		g.setFont(DisplayMessage.FONT.deriveFont(Font.BOLD, 36));
		g.drawString("Moves Left:", 25 + 2 * Window.WIDTH / 3, 80);
		g.setFont(DisplayMessage.FONT.deriveFont(Font.BOLD, 28));
		g.drawString(movesLeft + "", 50 + 2 * Window.WIDTH / 3, 110);

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				g.setColor(background);
				g.fillRect(xOff + i * Candy.DIM, yOff + j * Candy.DIM, Candy.DIM, Candy.DIM);
			}
		}

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {

				if (grid[i][j] == null || particleMaster.isFallingAt(i, j) || particleMaster.isSwappingAt(i, j)) {
					continue;
				}

				// Check if current element is being swapped
				int xMore = 0;
				int yMore = 0;

				// Swapping
				switch (dir) {
				case 1:
					// First one
					if (c1.x == i && c1.y == j) {
						yMore -= progress;
					}
					// Second One
					if (c2.x == i && c2.y == j) {
						yMore += progress;
					}
					break;
				case 2:
					// First one
					if (c1.x == i && c1.y == j) {
						yMore += progress;
					}
					// Second One
					if (c2.x == i && c2.y == j) {
						yMore -= progress;
					}
					break;
				case 3:
					// First one
					if (c1.x == i && c1.y == j) {
						xMore += progress;
					}
					// Second One
					if (c2.x == i && c2.y == j) {
						xMore -= progress;
					}
					break;
				case 4:
					// First one
					if (c1.x == i && c1.y == j) {
						xMore -= progress;
					}
					// Second One
					if (c2.x == i && c2.y == j) {
						xMore += progress;
					}
					break;
				}

				g.drawImage(grid[i][j].getImage(), xMore + xOff + i * Candy.DIM, yMore + yOff + j * Candy.DIM,
						Candy.DIM, Candy.DIM, null);
			}
		}

		particleMaster.render(g, xOff, yOff);

		if (currentMsg != null) {
			FontMetrics m = g.getFontMetrics(DisplayMessage.FONT);
			int width = m.stringWidth(currentMsg.getText());
			int height = m.getHeight();

			int x = Window.WIDTH / 2 - width / 2;
			int y = Window.HEIGHT / 2 - height / 2;

			g.setFont(DisplayMessage.FONT);
			g.setColor(currentMsg.getColor());
			g.drawString(currentMsg.getText(), x, y);
		}

		return drawing;
	}

	public boolean complete() {
		return movesLeft <= 0;
	}

	public boolean stableComplete() {
		return complete() && particleMaster.isStable() && !constainsNulls();
	}
	
	public boolean success() {
		return score >= max;
	}

	public int getStars() {
		if(score < max) {
			return 1;
		}else if(score >= max && score <= max * 1.5) {
			return 2;
		}
		return 3;
	}
}
