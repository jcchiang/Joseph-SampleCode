package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import system.actor.BaseActor;
import system.actor.MainHero;

/**
 * @author B98505005,B97502052
 * 
 */
public class DialogHandler {

	public static void init(String mapName, int mapFileNumber, BaseActor[] npc) {
		File file = null;
		String line = null;
		String npcName = null;
		BufferedReader reader = null;
		ArrayList<String> dialogList = null;
		DialogDetail aDD = null;

		for (int i = 0; i < mapFileNumber; ++i) {
			String fileName = new String("./dial/" + mapName + i + ".txt");
			//System.out.println(fileName);
			file = new File(fileName);
			try {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
				reader = new BufferedReader(isr);
				dialogList = new ArrayList<String>();
				String temp = reader.readLine();
				npcName = temp.substring(temp.indexOf("=") + 1, temp.lastIndexOf("="));
				//System.out.println(npcName);
				aDD = new DialogDetail();
				while ((line = reader.readLine()) != null){
					//System.out.println(line);
					dialogList.add(line);
				}
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int npc_number = -1;
			for(int test=0; test<npc.length; test++){
				if(npc[test].getName().equals(npcName)){
					npc_number = test;
					break;
				}
			}
			if(npc_number > 0){
				for(int test=0; test<dialogList.size(); test++){
					String number = dialogList.get(test).substring(0, dialogList.get(test).indexOf(":"));
					String detail = dialogList.get(test).substring(dialogList.get(test).indexOf(":") + 1);
					//System.out.println(number);
					//System.out.println(detail);
					if(Integer.valueOf(number) == 0){
						aDD.addDialog(new MainHero(), detail);
					}
					else{
						aDD.addDialog(npc[npc_number], detail);
					}
				}
				npc[npc_number].addDialogDetail(aDD);
			}
			else if(npc_number == 0){
				for(int test=0; test<dialogList.size(); test++){
					String number = dialogList.get(test).substring(0, dialogList.get(test).indexOf(":"));
					String detail = dialogList.get(test).substring(dialogList.get(test).indexOf(":") + 1);
					//System.out.println(number);
					//System.out.println(detail);
					if(Integer.valueOf(number) == 0){
						aDD.addDialog(new MainHero(), detail);
					}
					else{
						aDD.addDialog(new MainHero(), detail);
					}
				}
				npc[npc_number].addDialogDetail(aDD);
			}
		}
	}

	/*
	public static void main(String[] args) {
		TestMap3 t = new TestMap3(2);
		BaseActor[] npc = t.getAllNpc();
		
		//System.out.println(npc[npc.length-1].getName());
				
		init(t.getName(), t.getDialogFileNumber(), npc);
		
		for (BaseActor b : npc){
			//System.out.println(b.getDialogListSize());
			for (int i = 0; i < b.getDialogListSize(); ++i) {
				String[] strs = b.getDialog(i);
				for (String s : strs)
					System.out.println(s);
			}
		}
		
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (BaseActor b : npc)
			for (int i = 0; i < b.getDialogListSize(); ++i) {
				String[] strs = b.getDialog(i);
				for (String s : strs) {
					String[] arr = s.split(" ");
					for (String a : arr)
						tmp.add(a);
				}
			}
		System.err.println(tmp);
	}
	*/
}
