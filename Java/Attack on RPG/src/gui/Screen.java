package gui;


import system.DialogHandler;
import system.actor.BaseActor;
import system.actor.MainHero;
import system.map.*;
import gui.io.*;
import gui.music.BGMPlayer;
import gui.option.OptionPanel;

/**
 * Processing the needed pixels to show on screen, load full map inside here to wait for moving of screen
 * Last Update: Add optionPanel related method at 2013/06/17 19:48
 * @author B98505005
 *
 */
public class Screen {
	private int SCREENWIDTH, SCREENHEIGHT;
	private final int SPRITEWIDTH = Game.SPRITEWIDTH, SPRITEHEIGHT = Game.SPRITEHEIGHT;
	private int[][] pixels_base, pixels_top;
	private BaseMap currentMap;
	private MainHero hero;
	
	public static final int MENU = 0, MAP = 1, OPTIONS = 2, DIALOG = 3, BATTLE = 4, MAX = 5;
	private int currentStatus = MAP;
	
	private OptionPanel options;
	public int optionsLevel = 0;
	
	private DialogPanel dialog;
	private BattlePanel battle;
	
	public boolean isConstantlyRedraw = false;

	/**
	 * Constructor
	 * 
	 * @param w The width of the needed screen
	 * @param h The height of the needed screen
	 */
	public Screen(int w, int h, MainHero actor){
		SCREENWIDTH = w;
		SCREENHEIGHT = h - 32;
			
		hero = actor;
		currentMap = MapManage.getMap("Home");
		
		options = new OptionPanel(SCREENWIDTH, SCREENHEIGHT);
		loadNewMap();
	}
	
	public Screen(int w, int h, MainHero actor, String mapName){
		SCREENWIDTH = w;
		SCREENHEIGHT = h - 32;
			
		hero = actor;
		currentMap = MapManage.getMap(mapName);
		
		options = new OptionPanel(SCREENWIDTH, SCREENHEIGHT);
		loadNewMap();
	}
	
	public int getScreenWidth(){
		return SCREENWIDTH;
	}
	
	public int getScreenHeight(){
		return SCREENHEIGHT;
	}
	
	public void reloadThisMap(){
		pixels_base = new int[currentMap.getHeight()][currentMap.getWidth()];
		pixels_top = new int[currentMap.getHeight()][currentMap.getWidth()];
		int xTileNumber = currentMap.getWidth() / SPRITEWIDTH;
		int yTileNumber = currentMap.getHeight() / SPRITEHEIGHT;
		
		
		for(int y = 0; y < yTileNumber; y++){
			for(int x = 0; x < xTileNumber; x++){
				if(MapMoveable.getMoveable(currentMap.getSprites(0)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 0);
				else setTopTile(x, y, 0);
				if(MapMoveable.getMoveable(currentMap.getSprites(1)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 1);
				else setTopTile(x, y, 1);
				if(MapMoveable.getMoveable(currentMap.getSprites(2)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 2);
				else setTopTile(x, y, 2);
				
			}
		}
	}
	
	/**
	 * Load the current map data into pixels array
	 */
	public void loadNewMap(){
		pixels_base = new int[currentMap.getHeight()][currentMap.getWidth()];
		pixels_top = new int[currentMap.getHeight()][currentMap.getWidth()];
		int xTileNumber = currentMap.getWidth() / SPRITEWIDTH;
		int yTileNumber = currentMap.getHeight() / SPRITEHEIGHT;
		
		
		for(int y = 0; y < yTileNumber; y++){
			for(int x = 0; x < xTileNumber; x++){
				if(MapMoveable.getMoveable(currentMap.getSprites(0)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 0);
				else setTopTile(x, y, 0);
				if(MapMoveable.getMoveable(currentMap.getSprites(1)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 1);
				else setTopTile(x, y, 1);
				if(MapMoveable.getMoveable(currentMap.getSprites(2)[y][x]) != MapMoveable.SHOULD_ON_TOP) setBaseTile(x, y, 2);
				else setTopTile(x, y, 2);
				
			}
		}
		
		if(getCurrentMap().getName().equals("CarSimulation")) isConstantlyRedraw = true;
		else isConstantlyRedraw = false;
		
		hero.nextDialogID = 0;
		BGMPlayer.playFile(currentMap.getBGMFile());
		if(!currentMap.dialogIsLoaded){
			//System.out.println("Load Dialog into " + currentMap.getName());
			currentMap.dialogIsLoaded = true;
			DialogHandler.init(currentMap.getName(), currentMap.getDialogFileNumber(), currentMap.getAllNpc());
		}
	}
	
	public void changeNewMap(BaseMap newMap){
		currentMap = newMap;
		loadNewMap();
	}
	
	/**
	 * Load a sprite, to let a 32x32 block on screen is occupied by this sprite(below characters)
	 * @param x The number of sprite in x axis
	 * @param y The number of sprite in y axis
	 * @param level The level this tile should be presented on, totally 3 levels
	 */
	public void setBaseTile(int x, int y, int level){
		int[][] tempPixel = SpriteSheetLoader.grabTile(currentMap.getSprites(level)[y][x]);
		if(level == 0){
			for(int y2 = 0; y2 < SPRITEHEIGHT; y2++){
				for(int x2 = 0; x2 < SPRITEWIDTH; x2++){
					pixels_base[y * SPRITEHEIGHT + y2][x * SPRITEWIDTH + x2] = tempPixel[y2][x2];
				}
			}
			//draw_base[y][x] = SpriteSheetLoader.grabTileImage(currentMap.getSprites(level)[y][x]);
		}
		else{
			for(int y2 = 0; y2 < SPRITEHEIGHT; y2++){
				for(int x2 = 0; x2 < SPRITEWIDTH; x2++){
					if(tempPixel[y2][x2] < 0){
						pixels_base[y * SPRITEHEIGHT + y2][x * SPRITEWIDTH + x2] = tempPixel[y2][x2];
					}
				}
			}
			//draw_base[y][x] = SpriteSheetLoader.grabTileImage(currentMap.getSprites(level)[y][x]);
		}
	}
	
	/**
	 * Load a sprite, to let a 32x32 block on screen is occupied by this sprite(above characters)
	 * @param x The number of sprite in x axis
	 * @param y The number of sprite in y axis
	 * @param level The level this tile should be presented on, totally 3 levels
	 */
	public void setTopTile(int x, int y, int level){
		int[][] tempPixel = SpriteSheetLoader.grabTile(currentMap.getSprites(level)[y][x]);
		if(level == 0){
			for(int y2 = 0; y2 < SPRITEHEIGHT; y2++){
				for(int x2 = 0; x2 < SPRITEWIDTH; x2++){
					pixels_top[y * SPRITEHEIGHT + y2][x * SPRITEWIDTH + x2] = tempPixel[y2][x2];
				}
			}
		}
		else{
			for(int y2 = 0; y2 < SPRITEHEIGHT; y2++){
				for(int x2 = 0; x2 < SPRITEWIDTH; x2++){
					if(tempPixel[y2][x2] < 0){
						pixels_top[y * SPRITEHEIGHT + y2][x * SPRITEWIDTH + x2] = tempPixel[y2][x2];
					}	
				}
			}
		}	
	}
	
	/**
	 * Drawing method(map that behind actors), to show the data by graphics
	 * 
	 * @param currentPixels the original pixels before this method process
	 * @return the pixels that already loaded sprite in
	 */
	public int[] drawLowerMap(int[] currentPixels){
		
		for(int y = hero.getY() - SCREENHEIGHT/2, y2 = 0 ; y < hero.getY() + SCREENHEIGHT/2; y++, y2++){
			if(y < 0 || y >= currentMap.getHeight()) continue;
			for(int x = hero.getX() - SCREENWIDTH/2, x2 = 0; x < hero.getX() + SCREENWIDTH/2; x++, x2++){
				if(x < 0 || x >= currentMap.getWidth()) continue;
				currentPixels[y2 * SCREENWIDTH + x2] = pixels_base[y][x];
			}
		}
		return currentPixels;
	}
	
	/**
	 * Drawing method(map that above of actors), to show the data by graphics
	 * 
	 * @param currentPixels the original pixels before this method process
	 * @return the pixels that already loaded sprite in
	 */
	public int[] drawTopMap(int[] currentPixels){
		
		for(int y = hero.getY() - SCREENHEIGHT/2, y2 = 0 ; y < hero.getY() + SCREENHEIGHT/2; y++, y2++){
			if(y < 0 || y >= currentMap.getHeight()) continue;
			for(int x = hero.getX() - SCREENWIDTH/2, x2 = 0; x < hero.getX() + SCREENWIDTH/2; x++, x2++){
				if(x < 0 || x >= currentMap.getWidth()) continue;
				if(pixels_top[y][x] < 0){
					currentPixels[y2 * SCREENWIDTH + x2] = pixels_top[y][x];
				}
			}
		}
		return currentPixels;
	}
	
	/**
	 * Get the current map instance
	 * @return the map
	 */
	public BaseMap getCurrentMap(){
		return currentMap;
	}
	
	/**
	 * Clean up the pixels data to be initial
	 * 
	 * @param currentPixels the pixels wanted to clear
	 */
	public void cleanPixels(int[] currentPixels) {
		for(int i = 0; i < currentPixels.length; i++){
			currentPixels[i] = 0;
		}
	}

	/**
	 * Drawing method for character, which will always be in the middle of screen
	 * 
	 * @param currentPixels the original pixels before this method process
	 * @param hero the actor that will be drawn on screen
	 * @return the pixels that already loaded character's image in
	 */
	public int[] drawActor(int[] currentPixels, BaseActor hero){
		for(int y = 0; y < SPRITEHEIGHT; y++){
			for(int x = 0; x < SPRITEWIDTH; x++){
				int[] tempPixels = CharacterLoader.getCharacterPixel(hero.getID(), hero.getCharacterPose());
				if(tempPixels[y * SPRITEWIDTH + x] < 0){
					currentPixels[(SCREENHEIGHT/2 + y) * SCREENWIDTH + (SCREENWIDTH/2 + x)] = tempPixels[y * SPRITEWIDTH + x];
				}
			}
		}
		return currentPixels;
	}
	
	/**
	 * Drawing method for NPC, which will be drawn if they're in player's sight
	 * @param currentPixels the original pixels before this method process
	 * @return the pixels that already loaded NPC's image in
	 */
	public int[] drawNPC(int[] currentPixels){
		for(int i = 1; i < currentMap.getNumberOfNPC(); i++){
			BaseActor thisNPC = currentMap.getNPC(i);
			if(Math.abs(thisNPC.getX() - hero.getX()) < SCREENWIDTH && Math.abs(thisNPC.getY() - hero.getY()) < SCREENHEIGHT){
				for(int y = 0; y < SPRITEHEIGHT; y++){
					for(int x = 0; x < SPRITEWIDTH; x++){
						int[] tempPixels = CharacterLoader.getCharacterPixel(thisNPC.getID(), thisNPC.getCharacterPose());
						int xOnScreen = SCREENWIDTH/2 + (thisNPC.getX() - hero.getX()) + x;
						int yOnScreen = SCREENHEIGHT/2 + (thisNPC.getY() - hero.getY()) + y;
						if(xOnScreen < 0 || xOnScreen >= SCREENWIDTH) continue;
						if(yOnScreen < 0 || yOnScreen >= SCREENHEIGHT) continue;
						if(tempPixels[y * SPRITEWIDTH + x] < 0){
							currentPixels[yOnScreen * SCREENWIDTH + xOnScreen]
									= tempPixels[y * SPRITEWIDTH + x];
						}
					}
				}
			}
		}
		return currentPixels;
	}
	
	/**
	 * Get the current status
	 * @return the status
	 */
	public int getCurrentStatus(){
		return currentStatus;
	}

	public void setCurrentStatus(int newStatus){
		currentStatus = (newStatus % MAX);
	}
	
	public void checkChangeMap(){
		boolean flag = false;
		MapChangeDetail detail = null;
		for(int i=0; i<currentMap.getTeleportPoints().size(); i++){
			//System.out.println(hero.getX()+","+hero.getY()+","+hero.getCharacterPose());
			detail = currentMap.getTeleportPoints().get(i);
			if(detail.x == hero.getX() && detail.y == hero.getY() && detail.characterPose == hero.getCharacterPose()){
				flag = true;
				break;
			}
		}
		if(flag){
			currentMap = MapManage.getMap(detail.nextMapName);
			hero.setPositition(detail.newX, detail.newY);
			hero.setCharacterPose(detail.newPose);
			loadNewMap();
		}
	}
	
	public OptionPanel getOptionPanel(){
		return options;
	}
	
	public void setOptionPanel(OptionPanel op){
		options = op;
	}
	
	public DialogPanel getDialogPanel(){
		return dialog;
	}
	
	public BattlePanel getBattlePanel(){
		return battle;
	}
	
	public void setBattlePanel(BattlePanel bp){
		battle = bp;
	}
	
	public void setDialogPanel(DialogPanel dp){
		dialog = dp;
	}
}
