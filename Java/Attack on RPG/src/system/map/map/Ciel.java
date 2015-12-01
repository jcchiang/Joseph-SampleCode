package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;

public class Ciel extends BaseMap{

	public Ciel(){
		super("Ciel", 1);
	}
	
	@Override
	public void loadMapDetails() {
		// TODO Auto-generated method stub
		BaseActor clark = new BaseActor(5, "Charlie");
		clark.setCharacterPose(1);
		clark.setTilePostition(12, 5);
		addNPC(clark);
		
	}

	@Override
	public void addTeleportPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMapDialogs() {
		// TODO Auto-generated method stub
		
	}
	
}
