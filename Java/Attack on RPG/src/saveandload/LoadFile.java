package saveandload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadFile {
	public HeroFile heroFile;
	public String loadedMapName;
	
	public LoadFile(){
		File aFile = new File("./save/save1.txt");
		String thisLine;
		BufferedReader reader = null;
		try{
				InputStreamReader isr = new InputStreamReader(new FileInputStream(aFile), "UTF-8");
				reader = new BufferedReader(isr);
				thisLine = reader.readLine();
				heroFile = new HeroFile(thisLine);
				thisLine = reader.readLine();
				loadedMapName = new String(thisLine);
				isr.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public LoadFile(File aFile){
		BufferedReader reader = null;
		try{
				InputStreamReader isr = new InputStreamReader(new FileInputStream(aFile), "UTF-8");
				reader = new BufferedReader(isr);
				while(reader.readLine() != null){
					
				}
				isr.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public HeroFile getHeroFile(){
		return heroFile;
	}
	/*
	public static void main(String[] args){
		LoadFile A = new LoadFile();
		HeroFile B = A.getHeroFile();
		System.out.println(B.name);
		System.out.println(B.characterPose);
		System.out.println(B.mp);
	}*/
}
