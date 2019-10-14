package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import flavor.Levels;
import level.Candy;

public class Window extends JFrame implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 850;
	public static final int HEIGHT = (int) (WIDTH * (3.0f / 4.0f));

	private BufferedImage screen;

	private Levels levels;

	private Point mouseClick;

	private Timer t;

	public Window() {
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Scary Crush");
		this.addMouseListener(this);
		this.setResizable(false);
		this.addKeyListener(this);

		levels = new Levels();
	
		screen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

		TimerTask task = new TimerTask() {
			public void run() {
				levels.getCurrentLevel().update();
				repaint();
			}
		};

		t = new Timer(true);
		t.scheduleAtFixedRate(task, 0, 10);

		this.setIconImage(TextureManager.getTexture(Candy.TYPE_SKULL));
		this.setVisible(true);
	}

	public void paint(Graphics g) {
		Graphics g2 = screen.getGraphics();
		g2.drawImage(levels.getCurrentLevel().getDrawing(), 0, 0, WIDTH, HEIGHT, null);
		if (levels.getCurrentLevel().stableComplete()) {
			g2.setColor(new Color(0, 0, 0, 0.5f));
			g2.fillRect(0, 0, WIDTH, HEIGHT);
			levels.getFinishAnimation().paint(g2, levels.getCurrentLevel().success(), levels.getCurrentLevel().getStars());
		}

		g.drawImage(screen, 0, 0, WIDTH, HEIGHT, null);
	}

	public static void main(String[] args) {
		new Window();
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		mouseClick = arg0.getPoint();
	}

	public void mouseReleased(MouseEvent arg0) {
		if (!levels.getCurrentLevel().complete())
			levels.getCurrentLevel().onDrag(mouseClick, arg0.getPoint());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (levels.getCurrentLevel().stableComplete() && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if(levels.getCurrentLevel().success()) {
				levels.nextLevel();
			}else {
				levels.reset();	
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
