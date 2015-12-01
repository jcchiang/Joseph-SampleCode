package system;

import java.util.ArrayList;
import system.actor.BaseActor;

public class DialogDetail {
	private ArrayList<BaseActor> actor;
	//private ArrayList<Integer> dialogID;
	private ArrayList<String> content;
	
	public DialogDetail(){
		actor = new ArrayList<BaseActor>();
		//dialogID = new ArrayList<Integer>();
		content = new ArrayList<String>();
	}
	
	public void addDialog(BaseActor speaker, String content){
		actor.add(speaker);
		//this.dialogID.add(new Integer(-1));
		this.content.add(content);
		
	}
	
	public int getDetailSize(){
		if(actor.size() == content.size())
			return actor.size();
		else
			return -1;
	}
	
	public BaseActor getSpeaker(int number){
		return actor.get(number);
	}
	
	public String getContent(int number){
		return content.get(number);
	}
}
