package gui.io;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

/**
 * Used to loaded Character's image and store
 * Last Update: Become Static. at 2013/06/15 21:49
 * @author B98505005
 *
 */
public class CharacterLoader{
	private static boolean isLoaded = false;
	private static final int SPRITEWIDTH = 32, SPRITEHEIGHT = 32;
	private static int[][][] characterPixels;  //[charaNumber][imgNumber][pixelNumber]
	private static int sheetWidth = 96;
	public static int numberOfCharacters = 6;
	
	public static void initial(){
		if(!isLoaded)
			isLoaded = true;
		characterPixels = new int[numberOfCharacters][12][SPRITEWIDTH * SPRITEHEIGHT];
		
		BufferedImage image;
		for(int characterNum = 0; characterNum < numberOfCharacters; characterNum ++){
			try {
				image = new BufferedImage(SPRITEWIDTH * 3, SPRITEHEIGHT * 4, BufferedImage.TYPE_INT_ARGB);
				image.getGraphics().drawImage(ImageLoader.getCharacter(characterNum), 0, 0, null);
				int[] temp = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
				for(int imgNum = 0; imgNum < 12; imgNum++){
					for(int y = 0; y < SPRITEHEIGHT; y++){
						for(int x = 0; x < SPRITEWIDTH; x++){
							characterPixels[characterNum][imgNum][y * SPRITEWIDTH + x] = 
									temp[((imgNum / 3) * SPRITEHEIGHT + y) * sheetWidth + ((imgNum % 3) * SPRITEWIDTH + x)];
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int[] getCharacterPixel(int charaID, int poseNo){
		if(!isLoaded){
			initial();
		}
		return characterPixels[charaID][poseNo];
	}
}
