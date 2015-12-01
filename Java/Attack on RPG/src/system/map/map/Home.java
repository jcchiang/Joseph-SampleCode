package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;

public class Home extends BaseMap{
	//boolean testFlag;
	public Home(){
		super("Home", 2);
	}
	
	@Override
	public void loadMapDetails() {
		//testFlag = true;
	}

	@Override
	public void addTeleportPoints() {
		teleport.add(new MapChangeDetail(13, 8, BaseActor.FACE_DOWN, "Town", 10, 17, BaseActor.FACE_DOWN, MapChangeDetail.TILE));
	}

	@Override
	public void loadMapDialogs() {
		
	}

}
