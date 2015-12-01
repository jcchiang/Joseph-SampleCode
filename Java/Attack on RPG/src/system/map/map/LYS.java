package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;

public class LYS extends BaseMap{
	public LYS(){
		super("LYS", 1);
	}

	@Override
	public void loadMapDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTeleportPoints() {
		teleport.add(new MapChangeDetail(0, 12, BaseActor.FACE_LEFT, "TestMap3", 37, 7, BaseActor.FACE_LEFT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(24, 12, BaseActor.FACE_RIGHT, "Road", 0, 24, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
	}

	@Override
	public void loadMapDialogs() {
		// TODO Auto-generated method stub
		
	}
	
}
