package system.map;

import gui.Game;
import gui.io.BGMFileLoader;
import gui.io.MapFileLoader;
import java.io.*;
import java.util.ArrayList;

import system.DialogDetail;
import system.actor.BaseActor;

/**
 * The base of map
 * Last Update: Add Moving between map methods on 2013/06/15 17:18
 * @author B98505005
 *
 */
public abstract class BaseMap {
	/**
	 * Map's name
	 */
	protected String name;
	/**
	 * The file that stores the width, height, and sprite data
	 */
	protected File mapFile;
	protected File bgmFile;
	/**
	 * Map's width and height 
	 */
	protected int WIDTH, HEIGHT;
	/**
	 * Map's sprite index array
	 */
	protected int[][][] sprites; //[level][y][x]
	/**
	 * width and height for each sprite
	 */
	protected int SPRITEWIDTH = Game.SPRITEWIDTH, SPRITEHEIGHT = Game.SPRITEHEIGHT;
	
	//
	/**
	 * Dialog file number of this map
	 */
	private int dialogFileNumber;
	
	protected ArrayList<BaseActor> npc;
	protected ArrayList<MapChangeDetail> teleport;
	public ArrayList<DialogDetail> mapDialog;
	
	public boolean dialogIsLoaded = false;
	
	/**
	 * Basic Constructor
	 */
	public BaseMap(){
		init();
	}
	
	public BaseMap(String name){
		this.name = name;
		init();
	}
	
	public BaseMap(String name, int dialogNum){
		this.name = name;
		dialogFileNumber = dialogNum;
		init();
	}
	
	/**
	 * Initialize basic components
	 */
	public void init(){
		npc = new ArrayList<BaseActor>();
		mapFile = MapFileLoader.getMap(name);
		bgmFile = BGMFileLoader.getBGMFile(name);
		teleport = new ArrayList<MapChangeDetail>();
		mapDialog = new ArrayList<DialogDetail>();
		
		BaseActor heroForDialog = new BaseActor(0, "Hero");
		heroForDialog.setCharacterPose(1);
		heroForDialog.setTilePostition(0, 0);
		npc.add(heroForDialog);
		
		loadMapSprites();
		loadMapDetails();
		addTeleportPoints();
		loadMapDialogs();
	}
	
	/**
	 * Get this map's width
	 * @return the width of this map in pixels
	 */
	public int getWidth(){
		return WIDTH;
	}
	
	/**
	 * Get this map's height
	 * @return the height of this map in pixels
	 */
	public int getHeight(){
		return HEIGHT;
	}
	
	/**
	 * Get this map's name
	 * @return the name of this map
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get a specific tile of a map
	 * @param level the level of map desired
	 * @param x the tile number on x-axis
	 * @param y the tile number on y-axis
	 * @return the specific tile's sprite number
	 */
	public int getSprite(int level, int x, int y){
		return sprites[level][y][x];
	}
	
	/**
	 * Get a level of the sprite map of this map
	 * @param level which level is needed
	 * @return a 2-D array that contains which sprite is used for each block
	 */
	public int[][] getSprites(int level){
		return sprites[level];
	}
	
	/**
	 * Load map sprite data into drawing pixels
	 */
	public void loadMapSprites(){
		String contents = new String();
	    BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(mapFile));
			contents = reader.readLine();
			String[] parameters = contents.split(" ");
			//First Line contains 2 numbers, first is the numbers of tiles in x axis, second in y axis
			int xTileNumber = Integer.valueOf(parameters[0]);
			int yTileNumber = Integer.valueOf(parameters[1]);
					
			WIDTH = xTileNumber * SPRITEWIDTH;
			HEIGHT = yTileNumber * SPRITEHEIGHT;
			sprites = new int[3][yTileNumber][xTileNumber];
			
			//Load in, each line should contains xTilenumber of tiles, yTilenumber of lines in total
			for(int y = 0; y < yTileNumber; y++){
				contents = reader.readLine();
				for(int x = 0; x < xTileNumber; x++){
					parameters = contents.split(" ");
					sprites[0][y][x] = Integer.valueOf(Integer.valueOf(parameters[x]));
				}
			}
			contents = reader.readLine();
			//Load in 2nd level(objects on floor) of tiles
			for(int y = 0; y < yTileNumber; y++){
				contents = reader.readLine();
				for(int x = 0; x < xTileNumber; x++){
					parameters = contents.split(" ");
					sprites[1][y][x] = Integer.valueOf(Integer.valueOf(parameters[x]));
				}
			}
			contents = reader.readLine();
			//Load in 3rd level(objects on floor) of tiles
			for(int y = 0; y < yTileNumber; y++){
				contents = reader.readLine();
				for(int x = 0; x < xTileNumber; x++){
					parameters = contents.split(" ");
					sprites[2][y][x] = Integer.valueOf(Integer.valueOf(parameters[x]));
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a NPC into the NPC array
	 * @param newNPC the NPC wants to add in
	 */
	public void addNPC(BaseActor newNPC){
		npc.add(newNPC);
	}
	
	/**
	 * Get the number of NPC on this map
	 * @return the number
	 */
	public int getNumberOfNPC(){
		return npc.size();
	}
	
	/**
	 * Get the specific NPC instance 
	 * @param num the identical number of NPC of this map
	 * @return an actor instance that represent the NPC
	 */
	public BaseActor getNPC(int num){
		return npc.get(num);
	}
	
	public ArrayList<MapChangeDetail> getTeleportPoints(){
		return teleport;
	}
	
	public DialogDetail getDialog(int ID){
		return mapDialog.get(ID);
	}
	
	public File getBGMFile(){
		return bgmFile;
	}
	
	public BaseActor[] getAllNpc() {
		BaseActor[] ret = new BaseActor[getNumberOfNPC()];
		for(int i=0; i<getNumberOfNPC(); i++){
			ret[i] = npc.get(i);
		}
		return ret;
	}

	public int getDialogFileNumber() {
		return dialogFileNumber;
	}

	public void setDialogFileNumber(int dialogFileNumber) {
		this.dialogFileNumber = dialogFileNumber;
	}
	
	/**
	 * Used to load other things of a map, for example: flags, NPC
	 */
	public abstract void loadMapDetails();
	public abstract void addTeleportPoints();
	public abstract void loadMapDialogs();
}
