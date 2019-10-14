package level;

import java.awt.image.BufferedImage;

import gfx.TextureManager;

public class Candy {

	public static final String TYPE_BONE = "bone";
	public static final String TYPE_CANDLE = "candle";
	public static final String TYPE_HAND = "hand";
	public static final String TYPE_HEART = "heart";
	public static final String TYPE_SKULL = "skull";
	public static final String TYPE_BACKGROUND = "background";
	
	public static final int DIM = 64;

	private String da;
	private String type;
	
	public Candy(String type, String da) {
		this.da = da;		
		this.type = type;
	}
	
	public Candy(String type) {
		this(type, null);
	}
	
	public String getType() {
		return type;
	}
	
	public BufferedImage getImage() {
		return TextureManager.getTexture(type);
	}
	
	public String getAnimation() {
		return da;
	}
}
