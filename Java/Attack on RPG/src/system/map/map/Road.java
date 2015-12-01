package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;

public class Road extends BaseMap{

	public Road(){
		super("Road", 1);
	}
	
	@Override
	public void loadMapDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTeleportPoints() {
		
		teleport.add(new MapChangeDetail(0, 23, BaseActor.FACE_LEFT, "LYS", 24, 12, BaseActor.FACE_LEFT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(0, 24, BaseActor.FACE_LEFT, "LYS", 24, 12, BaseActor.FACE_LEFT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(69, 24, BaseActor.FACE_RIGHT, "Ciel", 0, 4, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(69, 25, BaseActor.FACE_RIGHT, "Ciel", 0, 4, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		
	}

	@Override
	public void loadMapDialogs() {
		// TODO Auto-generated method stub
		
	}
	
}
