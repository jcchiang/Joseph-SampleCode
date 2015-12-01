package gui.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Image related processor. Including: load images, load sprite, etc.
 * Last Update: Add option-related images. at 2013/06/24 01:37
 * @author B98505005
 *
 */
public class ImageLoader {
	private static boolean isLoaded = false;
	private static String currentName = "empty";
	private static BufferedImage currentImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
	private static HashMap<String, String> route;
	
	public static void initial(){
		if(!isLoaded){
			isLoaded= true;
			route = new HashMap<String, String>();
			
			route.put("empty", null);
			//tiles and backgroud
			route.put("tiles", "./img/tiles/VX_Tiles.png");
			route.put("treasure", "./img/tiles/treasurechest.png");
			route.put("menu", "./img/tiles/testBG.jpg");
			//character
			route.put("chara0", "./img/character/01.png");
			route.put("chara1", "./img/character/02.png");
			route.put("chara2", "./img/character/03.png");
			route.put("chara3", "./img/character/04.png");
			route.put("chara4", "./img/character/05.png");
			route.put("chara5", "./img/character/06.png");
			//dialog
			route.put("dialogBG", "./img/dialog/bg.png");
			//options
			route.put("options1", "./img/options/options10.png");
			route.put("options1c", "./img/options/options0cursor.png");
			route.put("options2", "./img/options/options1.png");
			route.put("options2c", "./img/options/options0cursor.png");
			route.put("options3", "./img/options/options30.png");
			route.put("options3c", "./img/options/options3cursor.png");
			route.put("options4", "./img/options/options40.png");
			route.put("options4c", "./img/options/options3cursor.png");
			
			route.put("op00", "./img/options/options00.png");
			route.put("op0cursor", "./img/options/options0cursor.png");
			route.put("op01true", "./img/options/options01_true.png");
			route.put("op01false", "./img/options/options01_false.png");
			route.put("op02true", "./img/options/options02_true.png");
			route.put("op02false", "./img/options/options02_false.png");
			route.put("op03true", "./img/options/options03_true.png");
			route.put("op03false", "./img/options/options03_false.png");
			route.put("op04true", "./img/options/options04_true.png");
			route.put("op04false", "./img/options/options04_false.png");
			route.put("op05true", "./img/options/options05_true.png");
			route.put("op05false", "./img/options/options05_false.png");
			route.put("op06true", "./img/options/options06_true.png");
			route.put("op06false", "./img/options/options06_false.png");
			route.put("op07true", "./img/options/options07_true.png");
			route.put("op07false", "./img/options/options07_false.png");
			route.put("op08true", "./img/options/options08_true.png");
			route.put("op08false", "./img/options/options08_false.png");
			route.put("op09true", "./img/options/options09_true.png");
			route.put("op09false", "./img/options/options09_false.png");
			
			route.put("expBox", "./img/options/expbox.png");
			route.put("expBar", "./img/options/expbar.png");
			route.put("hpBar", "./img/options/HPbar.png");
			route.put("mpBar", "./img/options/MPbar.png");
			route.put("num0", "./img/options/number0.png");
			route.put("num1", "./img/options/number1.png");
			route.put("num2", "./img/options/number2.png");
			route.put("num3", "./img/options/number3.png");
			route.put("num4", "./img/options/number4.png");
			route.put("num5", "./img/options/number5.png");
			route.put("num6", "./img/options/number6.png");
			route.put("num7", "./img/options/number7.png");
			route.put("num8", "./img/options/number8.png");
			route.put("num9", "./img/options/number9.png");
			
			route.put("equip_arrow", "./img/options/equip_arrow.png");
			//face
			route.put("face0", "./img/face/main0.png");
			route.put("face1", "./img/face/clark1.png");
			route.put("face2", "./img/face/main1.png");
			route.put("face3", "./img/face/main1.png");
			route.put("face4", "./img/face/main1.png");
			route.put("face5", "./img/face/charlieL.png");
			//actor face
			route.put("actorFace0", "./img/face/actor_status/face001.png");
			
			//battle
			route.put("battleDialog", "./img/battle/dialog.png");
			route.put("battle0", "./img/battle/00.jpg");
			route.put("battleMenuCursor", "./img/battle/menucursor.png");
		}
	}

	/**
	 * Load an image
	 * 
	 * @param name
	 *            the image code name
	 * @return the image other instances needed
	 * @throws IOException if file not found
	 */
	public static BufferedImage getImage(String name) throws IOException {
		if (currentName.equals(name)) {
			return currentImage;
		}
		currentName = name;
		currentImage = ImageIO.read(new File(route.get(name)));
		
		return currentImage;
	}
	
	/**
	 * Load a Character's image set
	 * @param ID the ID of the specific character
	 * @return the character's image other instances needed
	 * @throws IOException if file not found
	 */
	public static BufferedImage getCharacter(int ID) throws IOException {
		if(!isLoaded){
			initial();
		}
		if (currentName.equals(new String("chara" + ID))) {
			return currentImage;
		}
		currentName = new String("chara" + ID);
		currentImage = ImageIO.read(new File(route.get(new String("chara" + ID))));
		return currentImage;
	}
}
