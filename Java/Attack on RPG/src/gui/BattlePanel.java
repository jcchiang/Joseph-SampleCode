package gui;

import gui.io.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import system.actor.BaseActor;
import system.actor.MainHero;
import system.arena.AttackSkill;
import system.arena.BaseArena;
import system.arena.Event;

public class BattlePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NOT_BATTLE = -1, BEGIN = 0, IN_PROGRESS = 1, 
							WAIT_FOR_ACTION = 2, RESULT = 3; 
	
	private BufferedImage bg, dialogBG;
	
	//MECHANISM
	public static final int MENU = 0, ATTACK = 1, SKILL = 2, PICK_MONSTER = 3, DONE = 4;
	private int currentStatus = NOT_BATTLE;
	private static BaseArena currentArena;
	
	//Hero Side
	private MainHero hero;
	private BufferedImage heroImage;
	private int[] heroPixels, heroImagePixels;
	private int heroWidth = Game.SPRITEWIDTH, heroHeight = Game.SPRITEHEIGHT;
	
	//Monster Side
	private BaseActor[] monsters;
	private BufferedImage[] monstersImage;
	private int[][] monstersPixels, monstersImagePixels;
	
	//Components
	private JTextField[] options, skills;
	private ArrayList<JComponent> optionsItem, skillsItem;
	private BufferedImage cursor;
	private JTextArea content;
	
	//Pauser
	public boolean alreadyPaused, currentlyShowingMessages, isShowingResult;
	private int currentLevel, currentChoice;
	public int TIME_PAUSE = 100;
	public int messageCounter = 0;
	
	//Target Selection
	private int selectedSkillIndex, selectedTargetIndex;
	//private AttackSkill selectedSkill;
	//private BaseActor selectedTarget;
	
	public BattlePanel(int width, int height){
		setSize(width, height);
		setOpaque(false);
		setVisible(false);
		setLayout(null);
		this.setBackground(null);
		
		currentArena = new BaseArena();
		options = new JTextField[3];
		skills = new JTextField[4];
		optionsItem = new ArrayList<JComponent>();
		skillsItem = new ArrayList<JComponent>();
		
		initialComponents();
		
		try {
			bg = ImageLoader.getImage("battle0");
			dialogBG = ImageLoader.getImage("battleDialog");
			heroImage = new BufferedImage(heroWidth, heroHeight, BufferedImage.TYPE_INT_ARGB);
			cursor = ImageLoader.getImage("battleMenuCursor");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialComponents(){
		for(int i=0; i<options.length; i++){
			options[i] = new JTextField();
			initialJTextField(options[i], Color.WHITE, JTextField.LEFT);
			options[i].setSize(80, 30);
			options[i].setBounds(230, 500 + i * 30, options[i].getWidth(), options[i].getHeight());
			options[i].setFont(new Font("微軟正黑體", Font.BOLD, 22));
			switch(i){
				case 0:
					options[i].setText("戰    鬥");
					break;
				case 1:
					options[i].setText("技    能");
					break;
				case 2:
					options[i].setText("逃    跑");
					break;
				case 3:
					options[i].setText("跳    舞");
					break;
			}
			optionsItem.add(options[i]);
			add(options[i]);
		}
		
		for(int i=0; i<skills.length; i++){
			skills[i] = new JTextField();
			initialJTextField(skills[i], Color.WHITE, JTextField.LEFT);
			skills[i].setSize(300, 30);
			skills[i].setBounds(400, 500 + i * 40, skills[i].getWidth(), skills[i].getHeight());
			skills[i].setFont(new Font("微軟正黑體", Font.BOLD, 22));
			skills[i].setText(null);
			skillsItem.add(skills[i]);
			add(skills[i]);
		}
		
		content = new JTextArea();
		content.setOpaque(false);
		content.setVisible(false);
		content.setEditable(false);
		content.setSize(550, 120);
		content.setBounds(240, 510, content.getWidth(), content.getHeight());
		content.setForeground(Color.WHITE);
		content.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		content.setText("發生戰鬥!");
		add(content);
	}
	
	public void initialJTextField(JTextField aJTF, Color color, int alignment){
		aJTF.setVisible(false);
		aJTF.setBackground(null);
		aJTF.setOpaque(false);
		aJTF.setBorder(null);
		aJTF.setEditable(false);
		aJTF.setForeground(color);
		aJTF.setHorizontalAlignment(alignment);
	}
	
	public int getStatus(){
		return currentStatus;
	}
	
	public void setStatus(int newStatus){
		currentStatus = newStatus;
	}
	
	public BaseArena getArena(){
		return currentArena;
	}
	
	public void setArena(BaseArena aArena){
		currentArena = aArena;
	}
	
	public void setBattleField(BaseArena arena, MainHero hero, BaseActor[] monsters){
		currentArena = arena;
		this.hero = hero;
		this.monsters = monsters;
		currentArena.setMainHero(hero);
		currentArena.setMonster(monsters);
		
		try {
			bg = ImageLoader.getImage("battle0");
			heroImage = new BufferedImage(heroWidth, heroHeight, BufferedImage.TYPE_INT_ARGB);
			monstersImage = new BufferedImage[monsters.length];
			for(int num = 0; num < monsters.length; num++){
				monstersImage[num] = new BufferedImage(heroWidth, heroHeight, BufferedImage.TYPE_INT_ARGB);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		heroPixels = CharacterLoader.getCharacterPixel(this.hero.getID(), BaseActor.FACE_RIGHT);
		heroImagePixels = ((DataBufferInt) heroImage.getRaster().getDataBuffer()).getData();
		for(int y = 0; y < heroImage.getHeight(); y++){
			for(int x = 0; x < heroImage.getWidth(); x++){
				int temp = y * heroImage.getWidth() + x;
				heroImagePixels[temp] = heroPixels[temp]; 
			}
		}
		
		monstersPixels = new int[monsters.length][];
		monstersImagePixels = new int[monsters.length][];
		for(int num = 0; num < monsters.length; num++){
			System.out.println(monsters[num].getID());
			monstersPixels[num] = CharacterLoader.getCharacterPixel(monsters[num].getID(), BaseActor.FACE_LEFT);
			monstersImagePixels[num] = ((DataBufferInt) monstersImage[num].getRaster().getDataBuffer()).getData();
			for(int y = 0; y < monstersImage[num].getHeight(); y++){
				for(int x = 0; x < monstersImage[num].getWidth(); x++){
					int temp = y * monstersImage[num].getWidth() + x;
					monstersImagePixels[num][temp] = monstersPixels[num][temp]; 
				}
			}
		}
		
	}
	
	public void startBattle(){
		setVisible(true);
		alreadyPaused = false;
		currentlyShowingMessages = false;
		setStatus(BattlePanel.IN_PROGRESS);
		selectedSkillIndex = -1;
		selectedTargetIndex = -1;
		messageCounter = 0;
	}
		
	public void checkEvents(){
		//System.out.println("ARENA TIME:" + currentArena.getClockTime());
		if(currentlyShowingMessages){
			if(messageCounter < TIME_PAUSE){
				messageCounter++;
				return;
			}
			else{
				messageCounter = 0;
				currentlyShowingMessages = false;
				content.setVisible(false);
			}
		}
		Event aEvent;
		aEvent = currentArena.peekEvent();
		while(aEvent != null){
			if(aEvent.getTime() == currentArena.getClockTime()){
				Event activateEvent = currentArena.pollEvent();
				AttackSkill atk = (AttackSkill)activateEvent;
				content.setText(atk.getUser().getName()+" 對 " + atk.getTarget().getName() + " 使用了 "
								+ atk.getName() + "，並造成了 " + atk.getValue() + "點傷害！");
				content.setVisible(true);
				//System.out.println(activateEvent.getUser().getName()+"於 t = " + activateEvent.getTime() + "使用了"
				//		+ activateEvent.getName());
				//System.out.println("BEFORE:"+monsters[0].getAttributeNow().getHp());
				activateEvent.act();
				//System.out.println("AFTER:"+monsters[0].getAttributeNow().getHp());
				//System.out.println("====================");
				currentlyShowingMessages = true;
				return;
			}
			else break;
		}
		
		if(currentArena.gameOver()){
			setStatus(BattlePanel.RESULT);
		}
		else if(currentArena.fight()){
			setStatus(BattlePanel.WAIT_FOR_ACTION);
		}
	}
	
	public void takeAction(){
		if(!alreadyPaused){
			alreadyPaused = true;
			for(int i=0; i<optionsItem.size(); i++){
				optionsItem.get(i).setVisible(true);
			}
			currentLevel = MENU;
			currentChoice = 0;
			
			selectedSkillIndex = -1;
			selectedTargetIndex = -1;
			
			setCurrentChoice(getCurrentChoice());
		}
		if(currentLevel == DONE){
			playerActionDone();
		}
	}
	
	public void playerActionDone(){
		for(int i=0; i<optionsItem.size(); i++){
			optionsItem.get(i).setVisible(false);
		}
		for(int i=0; i<skillsItem.size(); i++){
			skillsItem.get(i).setVisible(false);
		}
		//Push Actions into Queue Here
		if(selectedSkillIndex < 0 && selectedTargetIndex < 0){
			content.setText("速度太慢，無法逃跑!!");
			content.setVisible(true);
			currentlyShowingMessages = true;
		}
		else{
			if(selectedSkillIndex < 0) selectedSkillIndex = 0;
			if(selectedTargetIndex < 0) selectedTargetIndex = 0;
			AttackSkill selectedSkill = hero.getSkill(selectedSkillIndex);
			selectedSkill.setTime(currentArena.getClockTime());
			//System.out.println(selectedSkill.getTime());
			selectedSkill.initBeforeUse(selectedSkill.getTime(), hero, monsters[selectedTargetIndex], currentArena);
			currentArena.addEvent(selectedSkill);
		}
		setStatus(BattlePanel.IN_PROGRESS);
		alreadyPaused = false;
		setSkillDeselected();
	}
	
	public void finishBattle(){
		int i;
		for(i=0; i<optionsItem.size(); i++){
			optionsItem.get(i).setVisible(false);
		}
		for(i=0; i<skillsItem.size(); i++){
			skillsItem.get(i).setVisible(false);
		}
		selectedSkillIndex = -1;
		selectedTargetIndex = -1;
		messageCounter = 0;
		
		isShowingResult = false;
		content.setVisible(false);
		setVisible(false);
	}
	
	public void returnToMenu(){
		int i;
		for(i=0; i<skillsItem.size(); i++){
			skillsItem.get(i).setVisible(false);
		}
		setSkillDeselected();
		setSelectedSkillIndex(-1);
		setSelectedTargetIndex(-1);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		if(currentStatus != NOT_BATTLE){
			super.paintComponent(g);
			g.drawImage(bg, 0, (getHeight()/2-bg.getHeight()) , getWidth(), bg.getHeight() * 2, null);
			g.drawImage(heroImage, 200, 400, null);
			for(int i=0; i<monsters.length; i++){
				if(monsters[i].getAttributeNow().getHp() > 0){
					g.drawImage(monstersImage[i], 700 + 50 * i, 400, null);
				}
			}
		}
		if(currentlyShowingMessages || isShowingResult)
			g.drawImage(dialogBG, 200, 480, null);
		if(currentStatus == WAIT_FOR_ACTION && currentLevel != DONE){
			g.drawImage(dialogBG, 200, 480, null);
			if(currentLevel == MENU){
				g.drawImage(cursor, 225, 497 + currentChoice * 30, null);
			}
			else if(currentLevel == SKILL){
				g.drawImage(cursor, 380, 500 + currentChoice * 40, skills[0].getWidth()+30, skills[0].getHeight(), null);
			}
			else if(currentLevel == PICK_MONSTER){
				g.drawImage(cursor, 695 + currentChoice * 50, 395 , monstersImage[currentChoice].getWidth() + 10 , monstersImage[currentChoice].getHeight() + 10, null);
				
			}
		}
	}
	
	public void loadHeroSkill(){
		for(int i = 0; i < skills.length; i++){
			if(i < hero.getSkillNumber()){
				skills[i].setText(hero.getSkill(i).getName());
			}
			else{
				skills[i].setText(null);
			}
		}
		for(int i = 0; i < skillsItem.size(); i++){
			skillsItem.get(i).setVisible(true);
		}
	}
	
	public int getCurrentChoice(){
		return currentChoice;
	}
	
	public void setCurrentChoice(int count){
		if(currentLevel == MENU){
			if(count >= options.length)
				currentChoice = 0;
			else if(count < 0)
				currentChoice = options.length - 1;
			else
				currentChoice = count;
		}
		else if(currentLevel == SKILL){
			if(count < 0){
				currentChoice = 0;
			}
			else if(count >= hero.getSkillNumber()){
				currentChoice = hero.getSkillNumber() - 1;
			}
			else if(count >= skills.length){
				currentChoice = skills.length - 1;
			}
			else{
				currentChoice = count;
			}
		}
		else if(currentLevel == PICK_MONSTER){
			if(currentChoice < count){
				for(int i = currentChoice+1; ;i++){
					if(i >= monsters.length){
						i = 0;
					}
					if(checkMonsterSelectable(i)){
						currentChoice = i;
						break;
					}
				}
			}
			else if(currentChoice >= count){
				for(int i = currentChoice-1; ;i--){
					if(i < 0){
						i = monsters.length - 1;
					}
					if(checkMonsterSelectable(i)){
						currentChoice = i;
						break;
					}
				}
			}
		}
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
	
	public void setCurrentLevel(int count){
		currentLevel = count;
	}
	
	public boolean checkSkillSelectable(int index){
		//if(hero.getAttributeNow().getMp() >= hero.getSkill(index).getMp()){
		//	return false;
		//}
		//else{
			return true;
		//}
	}
	
	public void setSelectedSkillIndex(int index){
		selectedSkillIndex = index;
	}
	
	public boolean checkMonsterSelectable(int index){
		if(monsters[index].getAttributeNow().getHp() <= 0)
			return false;
		else
			return true;
	}
	
	public void setSelectedTargetIndex(int index){
		selectedTargetIndex = index;
	}
	
	public void setSkillSelected(){
		skills[selectedSkillIndex].setForeground(Color.YELLOW);
	}
	
	public void setSkillDeselected(){
		for(int i=0; i<skills.length; i++){
			skills[i].setForeground(Color.WHITE);
		}
	}
	
	public void showResult(){
		isShowingResult = true;
		int someExp = 10;
		content.setText(
				hero.getName() + " 獲得了 " + someExp + "  肝指數!\n" +
				//hero.getName() + " 獲得了 " + someMoney + " G!\n" +
				"[請按空白鍵繼續]"
		);
		hero.exp += 10;
		content.setVisible(true);
	}

}
