package gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TextureManager {

	private static HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();

	public static void loadTexture(String texture) {
		try {
			BufferedImage i = ImageIO.read(TextureManager.class.getResource("/assets/" + texture + ".png"));
			
			String[] parts = texture.split("/");
			textures.put(parts[parts.length - 1], i);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage getTexture(String ID) {
		return textures.get(ID);
	}
}
