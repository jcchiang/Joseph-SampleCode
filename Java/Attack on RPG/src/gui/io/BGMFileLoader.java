package gui.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BGMFileLoader {
	private static boolean isLoaded = false;
	private static ArrayList<File> bgmFile = new ArrayList<File>();
	private static ArrayList<String> namesOfMap = new ArrayList<String>();
	private static HashMap<String, String> route = new HashMap<String, String>(); //MapName -> BGM File Location
	
	public static void initial(){
		if(!isLoaded){
			isLoaded = true;
			addBGMMap();
			
			for(int i = 0; i < namesOfMap.size(); i++){
				bgmFile.add(new File(route.get(namesOfMap.get(i))));
			}
		}
	}
	
	public static void addBGMMap(){
		namesOfMap.add("Home");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/Home.wav");
		namesOfMap.add("TestMap");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/TestMap.wav");	
		namesOfMap.add("TestMap2");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/TestMap2.wav");
		namesOfMap.add("TestMap3");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/wedding.wav");
		namesOfMap.add("Town");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/town.wav");
		namesOfMap.add("Road");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/Road.wav");
		namesOfMap.add("Maoujoou");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/Home.wav");
		namesOfMap.add("Ciel");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/Swordland.wav");
		namesOfMap.add("CarSimulation");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/Home.wav");
		namesOfMap.add("LYS");
		route.put(namesOfMap.get(namesOfMap.size()-1), "./bgm/money.wav");
		
	}
	
	public static File getBGMFile(String mapName){
		if(!isLoaded){
			initial();
		}
		if(namesOfMap.indexOf(mapName) < 0) 
			return bgmFile.get(0);
		else 
			return bgmFile.get(namesOfMap.indexOf(mapName));
	}
}	
