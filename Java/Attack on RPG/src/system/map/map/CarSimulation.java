package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;

public class CarSimulation extends BaseMap{

	public CarSimulation(){
		super("CarSimulation", 1);
	}
	
	public void change(){
		//System.out.println(WIDTH);
		int[] nextLineOfSprites = new int[this.WIDTH/this.SPRITEWIDTH];
		//for(int i=0; i<nextLineOfSprites.length - 1; i++){
		//	System.out.print(sprites[2][3][i]+ " ");
		//}
		//System.out.println();
		for(int i=1; i<nextLineOfSprites.length - 2; i++){
			if(i==1 && this.sprites[2][3][1] == 0 && this.sprites[2][3][2] == 0 && this.sprites[2][3][3] == 0){
				nextLineOfSprites[1] = 6184;
			}
			else if(this.sprites[2][3][i] == 6184){
				nextLineOfSprites[i+1] = 6184;
			}
			else{
				nextLineOfSprites[i+1] = 0;
			}
		}
		for(int i=1; i<nextLineOfSprites.length - 2; i++){
			sprites[2][3][i] = nextLineOfSprites[i];
		}
		//for(int i=0; i<nextLineOfSprites.length - 1; i++){
		//	System.out.print(sprites[2][3][i]+ " ");
		//}
		//System.out.println();
	}
	
	@Override
	public void loadMapDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTeleportPoints() {
		teleport.add(new MapChangeDetail(0, 7, BaseActor.FACE_LEFT, "Town", 49, 13, BaseActor.FACE_LEFT, MapChangeDetail.TILE));
		//teleport.add(new MapChangeDetail(31, 7, BaseActor.FACE_RIGHT, "Road", 0, 24, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(31, 7, BaseActor.FACE_RIGHT, "TestMap3", 19, 1, BaseActor.FACE_DOWN, MapChangeDetail.TILE));
	}

	@Override
	public void loadMapDialogs() {
		// TODO Auto-generated method stub
		
	}

}
