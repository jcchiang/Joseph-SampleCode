package gui.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DialogLoader {
	private static int numberOfCharacters = 0;
	private static ArrayList<ArrayList<String>> lines;
	private static HashMap<Integer, String> route;
	
	public static void initial(){
		route = new HashMap<Integer, String>();
		loadFiles();
		numberOfCharacters = CharacterLoader.numberOfCharacters;
		lines = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> charaLines;
		String aString;
		BufferedReader reader = null;
		File thisFile = null;
		try{
			for(int i = 0; i < numberOfCharacters; i++){
				thisFile = new File(route.get(i));
				InputStreamReader isr = new InputStreamReader(new FileInputStream(thisFile), "UTF-8");
				reader = new BufferedReader(isr);
				charaLines = new ArrayList<String>();
				while((aString = reader.readLine()) != null){
					charaLines.add(aString);
				}
				isr.close();
				lines.add(charaLines);
				//System.out.println(charaLines.get(0));
				//System.out.println(lines.size());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static void loadFiles(){
		route.put(0, "./dial/01.txt");
		route.put(1, "./dial/01.txt");
		route.put(2, "./dial/01.txt");
		route.put(3, "./dial/01.txt");
	}
	
	public static ArrayList<String> getDialog(int charaID){
		return lines.get(charaID);
		//return null;
	} 
	
	public static void main(String[] args){
		ImageLoader.initial();
		DialogLoader.initial();
	}
}
