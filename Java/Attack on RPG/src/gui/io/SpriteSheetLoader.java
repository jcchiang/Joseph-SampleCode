package gui.io;

import gui.Game;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

/**
 * Used to do Sprite related loading jobs
 * Last Update: Become Static. at 2013/06/15 21:49 
 * @author B98505005
 *
 */
public class SpriteSheetLoader {
	private static boolean isLoaded = false;
	private static int SPRITESHEETWIDTH, SPRITESHEETHEIGHT, TREASUREWIDTH, TREASUREHEIGHT;
	private final static int SPRITEWIDTH = Game.SPRITEWIDTH, SPRITEHEIGHT = Game.SPRITEHEIGHT;
	private static int[] sheetPixels, treasureChestPixels;
	private static int sheetWidth, treasureChestWidth;
	
	public static void initial(){
		if(!isLoaded)
			isLoaded = true;
		BufferedImage image = null, image2 = null;
		try {
			SPRITESHEETWIDTH = ImageLoader.getImage("tiles").getWidth();
			SPRITESHEETHEIGHT = ImageLoader.getImage("tiles").getHeight();
			image = new BufferedImage(SPRITESHEETWIDTH, SPRITESHEETHEIGHT, BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(ImageLoader.getImage("tiles"), 0, 0, null);
						
			TREASUREWIDTH = ImageLoader.getImage("treasure").getWidth();
			TREASUREHEIGHT = ImageLoader.getImage("treasure").getHeight();
			image2 = new BufferedImage(TREASUREWIDTH, TREASUREHEIGHT, BufferedImage.TYPE_INT_ARGB);
			image2.getGraphics().drawImage(ImageLoader.getImage("treasure"), 0, 0, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sheetPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		sheetWidth = image.getWidth();
		
		treasureChestPixels = ((DataBufferInt) image2.getRaster().getDataBuffer()).getData();
		treasureChestWidth = image2.getWidth();
	}
	
	/**
	 * Load the pixel data of any desired tile.
	 * @param tile the tile number other object wants to obtain
	 * @param width the width of the desired tile
	 * @param height the height of the desired tile
	 * @return 2-D array pixel value
	 */
	public static int[][] grabTile(int tile) {
		if(!isLoaded){
			initial();
		}
		int[][] pixels = new int[SPRITEHEIGHT][SPRITEWIDTH];
		int pixelsSize = (SPRITESHEETWIDTH/16) * (SPRITESHEETHEIGHT/16);
		if(tile < pixelsSize){
			int xTile = tile % 64;
			int yTile = tile / 64;
			
			for (int y = 0; y < SPRITEHEIGHT; y++) {
				for (int x = 0; x < SPRITEWIDTH; x++) {
					int refX = xTile * 16;
					int refY = yTile * 16;
					pixels[y][x] = sheetPixels[(refY + y) * sheetWidth + (refX + x)];
				}
			}
		}
		else{
			//Begin from 8064
			int xTile = (tile - pixelsSize) % 12;
			int yTile = (tile - pixelsSize) / 12;
			
			for (int y = 0; y < SPRITEHEIGHT; y++) {
				for (int x = 0; x < SPRITEWIDTH; x++) {
					int refX = xTile * 16;
					int refY = yTile * 16;
					pixels[y][x] = treasureChestPixels[(refY + y) * treasureChestWidth + (refX + x)];
				}
			}
		}

		
		return pixels;
	}
}