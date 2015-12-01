package system.map.map;

import system.actor.BaseActor;
import system.map.BaseMap;
import system.map.MapChangeDetail;

public class TestMap3 extends BaseMap{

	public TestMap3(){
		super("TestMap3", 1);
	}
	
	@Override
	public void loadMapDetails(){
		BaseActor clark = new BaseActor(2, "Ccr");
		clark.setCharacterPose(1);
		clark.setTilePostition(19, 7);
		addNPC(clark);
	}

	@Override
	public void addTeleportPoints() {
		teleport.add(new MapChangeDetail(19, 1, BaseActor.FACE_UP, "CarSimulation", 31, 7, BaseActor.FACE_LEFT, MapChangeDetail.TILE));
		teleport.add(new MapChangeDetail(37, 7, BaseActor.FACE_RIGHT, "LYS", 0, 12, BaseActor.FACE_RIGHT, MapChangeDetail.TILE));
		
		//teleport.add(new MapChangeDetail(19, 1, BaseActor.FACE_UP, "TestMap", 15, 24, BaseActor.FACE_UP, MapChangeDetail.TILE));
	}

	@Override
	public void loadMapDialogs() {
		//DialogDetail dd;
		//dd = new DialogDetail();
		//dd.addDialog(new MainHero(), 0);
		//dd.addDialog(this.getNPC(0), 0);
		//dd.addDialog(new MainHero(), 1);
		//mapDialog.add(dd);
	}

}
