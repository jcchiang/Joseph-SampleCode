package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;


public class Town extends BaseMap{
	public Town(){
		super("Town", 0);
	}
	
	@Override
	public void loadMapDetails() {
		//testFlag = true;
	}

	@Override
	public void addTeleportPoints() {
		teleport.add(new MapChangeDetail(10, 17, BaseActor.FACE_UP, "Home", 13, 8, BaseActor.FACE_UP, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(49, 13, BaseActor.FACE_RIGHT, "CarSimulation", 0, 7, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(49, 14, BaseActor.FACE_RIGHT, "CarSimulation", 0, 7, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(49, 15, BaseActor.FACE_RIGHT, "CarSimulation", 0, 7, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
			
		//teleport.add(new MapChangeDetail(49, 13, BaseActor.FACE_RIGHT, "Road", 0, 23, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		//teleport.add(new MapChangeDetail(49, 14, BaseActor.FACE_RIGHT, "Road", 0, 24, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		//teleport.add(new MapChangeDetail(49, 15, BaseActor.FACE_RIGHT, "Road", 0, 24, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
	}

	@Override
	public void loadMapDialogs() {
		
	}
}
