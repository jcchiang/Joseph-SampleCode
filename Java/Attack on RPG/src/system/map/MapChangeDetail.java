package system.map;

import gui.Game;

/**
 * Used to denote two points on map is connected together.
 * Last Update: Create at 2013/06/15 17:26
 * @author B98505005
 *
 */
public class MapChangeDetail {
	public int x, y;
	public int characterPose, newPose;
	public String nextMapName;
	public int newX, newY;
	public static final short TILE = 0, PIXEL = 1;
	private int SPRITEWIDTH = Game.SPRITEWIDTH, SPRITEHEIGHT = Game.SPRITEHEIGHT;
	
	/**
	 * If the hero moves onto a block, then it should go to another block.
	 * @param x the original x position of the hero
	 * @param y the original y position of the hero
	 * @param characterPose the original direction of face of the hero
	 * @param nextMapName the next map's name
	 * @param newX the new x position of the hero should be on in new map
	 * @param newY the new y position of the hero should be on in new map
	 * @param type the number is presented by tile numbers(0) or pixel numbers(1)
	 */
	public MapChangeDetail(int x, int y, int characterPose, String nextMapName, int newX, int newY, int newPose, short type){
		if(type == TILE){
			this.x = x * SPRITEWIDTH;
			this.y = y * SPRITEHEIGHT;
			this.characterPose = characterPose;
			this.nextMapName = nextMapName;
			this.newX = newX * SPRITEWIDTH;
			this.newY = newY * SPRITEHEIGHT;
			this.newPose = newPose;
		}
		else if(type == PIXEL){
			this.x = x;
			this.y = y;
			this.characterPose = characterPose;
			this.nextMapName = nextMapName;
			this.newX = newX;
			this.newY = newY;
			this.newPose = newPose;
		}
	}
}
