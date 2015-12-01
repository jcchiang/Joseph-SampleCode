package system.map;

import java.util.ArrayList;
/**
 * The manager for all the maps. 
 * Last Update: Change the method to create map classes at 2013/06/23 00:54
 * 
 * @author B98505005, B97502052
 * 
 */
public class MapManage {
	private static String[] nameList = {
		"Home", "TestMap", "TestMap2", "TestMap3", "Town",
		"Road", "Maoujou", "Ciel", "CarSimulation", "LYS"
	};
	private static ArrayList<BaseMap> maps;

	public static void initial() {
		maps = new ArrayList<BaseMap>();
		BaseMap A;
	
		for(int i=0; i<nameList.length; i++){
			try {
				String className = new String("system.map.map." + nameList[i]);
				A = (BaseMap) Class.forName(className).getConstructor().newInstance();
				maps.add(A);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param name
	 *            Map name
	 * @return Map if the map exists, else return null
	 */
	public static BaseMap getMap(String name) {
		for (BaseMap b : maps)
			if (name.equals(b.getName()))
				return b;
		return null;
	}
}

