package gui.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A static instance that loads in all the map text files.
 * Last Update: Become static at 2013/06/15 17:24
 * @author B98505005
 *
 */
public class MapFileLoader {
	private static boolean isLoaded = false;
	private static ArrayList<File> mapFile = new ArrayList<File>();
	private static ArrayList<String> namesOfMap = new ArrayList<String>();
	private static HashMap<String, String> route = new HashMap<String, String>(); //MapName -> File Location
	
	/**
	 * Load all the maps.
	 */
	public static void loadMap(){
		if(!isLoaded){
			isLoaded = true;
			addMapList();
			
			for(int i = 0; i < namesOfMap.size(); i++){
				mapFile.add(new File(route.get(namesOfMap.get(i))));
			}
		}
	}
	
	/**
	 * The list of map that need to be loaded in.
	 */
	public static void addMapList(){
		route.put("empty", null);
		
		namesOfMap.add("Home");
		route.put("Home", "./map/Home.txt");
		namesOfMap.add("TestMap");
		route.put("TestMap", "./map/testMap.txt");	
		namesOfMap.add("TestMap2");
		route.put("TestMap2", "./map/testMap2.txt");
		namesOfMap.add("TestMap3");
		route.put("TestMap3", "./map/testMap3.txt");
		namesOfMap.add("Town");
		route.put("Town", "./map/town.txt");
		namesOfMap.add("Road");
		route.put("Road", "./map/road.txt");
		namesOfMap.add("Maoujou");
		route.put("Maoujou", "./map/maoujou.txt");
		namesOfMap.add("Ciel");
		route.put("Ciel", "./map/Ciel.txt");
		namesOfMap.add("CarSimulation");
		route.put("CarSimulation", "./map/carSimulation.txt");
		namesOfMap.add("LYS");
		route.put("LYS", "./map/LYS.txt");
	}
	
	/**
	 * Get the specific map file
	 * @param name the name of the map
	 * @return the map file
	 */
	public static File getMap(String name){
		if(!isLoaded)
			loadMap();
		return mapFile.get(namesOfMap.indexOf(name));
	}
	
	/**
	 * Get the total number of maps loaded in.
	 * @return total number of maps
	 */
	public static int getNumberOfMaps(){
		if(!isLoaded){
			loadMap();
		}
		return namesOfMap.size();
	}
	
	/**
	 * Get the specific map's ID in the list.
	 * @param name the map's name
	 * @return the map's ID
	 */
	public static int getMapID(String name){
		if(!isLoaded){
			loadMap();
		}
		return namesOfMap.indexOf(name);
	}
}
