package gui.option;

import gui.io.ImageLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import system.actor.MainHero;
import system.item.BaseItem;
import system.item.BaseItemManage;
import system.item.Equipment;

/**
 * Show Options to user.
 * Last Update: Add Item and Equipment panel. at 2013/06/23 19:04
 * @author B98505005
 *
 */
public class OptionPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int optionsWidth;
	//private int[] optionsHeight, cursorWidth, cursorHeight;
	private boolean[] canSelect = {true, false, true, true, false, false, false, false, true};
	public final static int MAIN = 0, STATUS = 1, SKILL = 2, ITEM = 3, EQUIP = 4,
			 		  		MISSION = 5, PARTY = 6, SAVE = 7, LOAD = 8, QUIT = 9;
	private int currentLevel, currentChoice;
	//private int SCREENWIDTH, SCREENHEIGHT;
	private final int beginX = 40, beginY = 20;
	private final int cursorOffsetX = 11, cursorOffsetY = 11;
	private final int optionsOffsetY = 40;
	private final int toFaceWidth = 310;
	
	private BufferedImage[][] mainOption;
	private BufferedImage[] optionImage;
	private BufferedImage[] cursorImage;
	private BufferedImage[] numbers;
	
	private BufferedImage mainHeroFaces;
	private ArrayList<JComponent> statusDetails, itemDetails, equipDetails;
	
	private MainHero hero;
	
	//Status Detail JComponent//
	public boolean hasRefreshStatus = false;
	//No need to change
	private JTextField status_Exp, status_HP, status_MP;
	private JTextField status_STR, status_STR_CHT, status_MSTR, status_MSTR_CHT, status_AGI, status_AGI_CHT;
	private JTextField status_Equip, status_Equip_CHT;
	private BufferedImage expBox, expBar, hpBar, mpBar;
	//private Color unchangeFontColor = new Color(150,172,255);
	private Color unchangeFontColor = new Color(200,202,255);
	private Font unchangeFont = new Font("Comic Sans MS", Font.BOLD, 18);
	private Font unchangeFont_CHT = new Font("微軟正黑體", Font.BOLD, 12);
	//Need to change by time
	private BufferedImage currentFace;
	private JTextField status_ActorName, status_ActorLevel, status_ExpProgress;
	private JTextField status_ActorHP, status_ActorMP, status_ActorSTR, status_ActorMSTR, status_ActorAGI;
	private Color changeFontColor = Color.WHITE;
	
	//Item Detail JComponent//
	public boolean hasRefreshItem = false, refreshItemDescription = false;
	//No need to change
	private Font itemNameFont = new Font("微軟正黑體", Font.BOLD, 18);
	private Font itemCountFont = new Font("Comic Sans MS", Font.BOLD, 18);
	//Need to change
	private int itemHeroHas;
	private JTextField[][] itemName, itemCounts;
	private JTextArea itemDescription;
	
	//Equip Detail JComponent//
	public boolean hasRefreshEquip = false, refreshEquipAttribute = false;
	public boolean equip_CurrentlyOnTop = true, equip_EnterIsPressed = false;
	//No need to change
	private Font equipNameFont = new Font("微軟正黑體", Font.BOLD, 18);
	private JTextField[] equip_Attributes;
	private BufferedImage equip_arrow;
	//Need to change
	public Equipment[] activateEquip;
	private int equipHeroHas;
	private JTextField[] currentEquip;
	private JTextField[] equip_ActorAttributes, equipName, equip_EquipAttributes;
	
	public OptionPanel(int SCREENWIDTH, int SCREENHEIGHT){
		setLayout(null);
		setVisible(false);
		setOpaque(false);
		setDoubleBuffered(true);
		setSize(SCREENWIDTH, SCREENHEIGHT);

		currentLevel = MAIN;
		mainOption = new BufferedImage[QUIT+1][2]; // 0 : true, 1 : false
		optionImage = new BufferedImage[QUIT+1];
		cursorImage = new BufferedImage[QUIT+1]; 
		numbers = new BufferedImage[10];
		
		try{
			mainOption[0][0] = ImageLoader.getImage("op00");
			mainHeroFaces = ImageLoader.getImage("actorFace0");
			optionsWidth = mainOption[0][0].getWidth();
		}
		catch(IOException ex){
		}
		
		loadImages();
		statusDetails = new ArrayList<JComponent>();
		itemDetails = new ArrayList<JComponent>();
		equipDetails = new ArrayList<JComponent>();
		initialStatusDetail();
		initialItemDetail();
		initialEquipDetail();
		
	}

	public void setMainHero(MainHero hero){
		this.hero = hero;
	}
	
	private void loadImages(){
		try {
			for(int i = 1 ; i <= QUIT ; i++){
				String tempName = new String("op0" + i);
				mainOption[i][0] = ImageLoader.getImage(tempName+"true");
				mainOption[i][1] = ImageLoader.getImage(tempName+"false");
			}
			cursorImage[MAIN] = ImageLoader.getImage("op0cursor");
			for(int i = 1; i < MISSION; i++){
				String tempName = new String("options" + i);
				optionImage[i] = ImageLoader.getImage(tempName);
				
				tempName = new String("options" + i + "c");
				cursorImage[i] = ImageLoader.getImage(tempName);
			}
			
			for(int i = 0; i < 10; i++){
				String tempName = new String("num" + i);
				numbers[i] = ImageLoader.getImage(tempName);
			}
			
			currentFace = ImageLoader.getImage("face0");
			expBox = ImageLoader.getImage("expBox");
			expBar = ImageLoader.getImage("expBar");
			hpBar = ImageLoader.getImage("hpBar");
			mpBar = ImageLoader.getImage("mpBar");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialStatusDetail() {
		status_ActorName = new JTextField();
		initialJTextField(status_ActorName, changeFontColor, JTextField.LEFT); 
		status_ActorName.setSize(160, 30);
		status_ActorName.setBounds(beginX + optionsWidth + 120, beginY + 15, status_ActorName.getWidth(), status_ActorName.getHeight());
		status_ActorName.setFont(new Font("微軟正黑體", Font.BOLD, 22));
		status_ActorName.setText("我是主角啦怎樣");
		statusDetails.add(status_ActorName);
		add(status_ActorName);
		
		status_ActorLevel = new JTextField();
		initialJTextField(status_ActorLevel, changeFontColor, JTextField.RIGHT); 
		status_ActorLevel.setSize(80, 25);
		status_ActorLevel.setBounds(beginX + optionsWidth + 200, beginY + 45, status_ActorLevel.getWidth(), status_ActorLevel.getHeight());
		status_ActorLevel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		status_ActorLevel.setText("Lv   1");
		statusDetails.add(status_ActorLevel);
		add(status_ActorLevel);
		
		status_Exp = new JTextField();
		initialJTextField(status_Exp, unchangeFontColor, JTextField.LEFT); 
		status_Exp.setSize(100, 30);
		status_Exp.setBounds(beginX + optionsWidth + 120, beginY + 60, status_Exp.getWidth(), status_Exp.getHeight());
		status_Exp.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		status_Exp.setText("肝指數");
		statusDetails.add(status_Exp);
		add(status_Exp);
		
		status_ExpProgress = new JTextField();
		initialJTextField(status_ExpProgress, changeFontColor, JTextField.RIGHT); 
		status_ExpProgress.setSize(115, 30);
		status_ExpProgress.setBounds(beginX + optionsWidth + 55 + status_Exp.getWidth(), beginY + 66, status_ExpProgress.getWidth(), status_ExpProgress.getHeight());
		status_ExpProgress.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		status_ExpProgress.setText("Exp");
		statusDetails.add(status_ExpProgress);
		add(status_ExpProgress);
		
		status_HP = new JTextField();
		initialJTextField(status_HP, unchangeFontColor, JTextField.LEFT);
		status_HP.setSize(30, 30);
		status_HP.setBounds(beginX + optionsWidth + 20, beginY + 118, status_HP.getWidth(), status_HP.getHeight());
		status_HP.setFont(unchangeFont);
		status_HP.setText("HP");
		statusDetails.add(status_HP);
		add(status_HP);
		
		status_ActorHP = new JTextField();
		initialJTextField(status_ActorHP, changeFontColor, JTextField.RIGHT);
		status_ActorHP.setSize(85, 30);
		status_ActorHP.setBounds(beginX + optionsWidth + 20 + status_HP.getWidth(), beginY + 118, status_ActorHP.getWidth(), status_ActorHP.getHeight());
		status_ActorHP.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		status_ActorHP.setText("HP");
		statusDetails.add(status_ActorHP);
		add(status_ActorHP);
		
		status_MP = new JTextField();
		initialJTextField(status_MP, unchangeFontColor, JTextField.LEFT);
		status_MP.setSize(30, 30);
		status_MP.setBounds(beginX + optionsWidth + 160, beginY + 118, status_MP.getWidth(), status_MP.getHeight());
		status_MP.setFont(unchangeFont);
		status_MP.setText("MP");
		statusDetails.add(status_MP);
		add(status_MP);
		
		status_ActorMP = new JTextField();
		initialJTextField(status_ActorMP, changeFontColor, JTextField.RIGHT);
		status_ActorMP.setSize(85, 30);
		status_ActorMP.setBounds(beginX + optionsWidth + 160 + status_MP.getWidth(), beginY + 118, status_ActorMP.getWidth(), status_ActorMP.getHeight());
		status_ActorMP.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		status_ActorMP.setText("MP");
		statusDetails.add(status_ActorMP);
		add(status_ActorMP);
		
		status_STR = new JTextField();
		initialJTextField(status_STR, unchangeFontColor, JTextField.LEFT);
		status_STR.setSize(60, 30);
		status_STR.setBounds(beginX + optionsWidth + 20, beginY + 150, status_STR.getWidth(), status_STR.getHeight());
		status_STR.setFont(unchangeFont);
		status_STR.setText("STR");
		statusDetails.add(status_STR);
		add(status_STR);
		
		status_STR_CHT = new JTextField();
		initialJTextField(status_STR_CHT, unchangeFontColor, JTextField.LEFT);
		status_STR_CHT.setSize(50, 30);
		status_STR_CHT.setBounds(beginX + optionsWidth + 25, beginY + 165, status_STR_CHT.getWidth(), status_STR_CHT.getHeight());
		status_STR_CHT.setFont(unchangeFont_CHT);
		status_STR_CHT.setText("力量");
		statusDetails.add(status_STR_CHT);
		add(status_STR_CHT);
		
		status_ActorSTR = new JTextField();
		initialJTextField(status_ActorSTR, changeFontColor, JTextField.RIGHT);
		status_ActorSTR.setSize(55, 30);
		status_ActorSTR.setBounds(beginX + optionsWidth + 25 + status_STR.getWidth(), beginY + 155, status_ActorSTR.getWidth(), status_ActorSTR.getHeight());
		status_ActorSTR.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		status_ActorSTR.setText("0");
		statusDetails.add(status_ActorSTR);
		add(status_ActorSTR);
		
		status_MSTR = new JTextField();
		initialJTextField(status_MSTR, unchangeFontColor, JTextField.LEFT);
		status_MSTR.setSize(60, 30);
		status_MSTR.setBounds(beginX + optionsWidth + 20, beginY + 185, status_MSTR.getWidth(), status_MSTR.getHeight());
		status_MSTR.setFont(unchangeFont);
		status_MSTR.setText("MSTR");
		statusDetails.add(status_MSTR);
		add(status_MSTR);
		
		status_MSTR_CHT = new JTextField();
		initialJTextField(status_MSTR_CHT, unchangeFontColor, JTextField.LEFT);
		status_MSTR_CHT.setSize(50, 30);
		status_MSTR_CHT.setBounds(beginX + optionsWidth + 25, beginY + 200, status_MSTR_CHT.getWidth(), status_MSTR_CHT.getHeight());
		status_MSTR_CHT.setFont(unchangeFont_CHT);
		status_MSTR_CHT.setText("魔力");
		statusDetails.add(status_MSTR_CHT);
		add(status_MSTR_CHT);
		
		status_ActorMSTR = new JTextField();
		initialJTextField(status_ActorMSTR, changeFontColor, JTextField.RIGHT);
		status_ActorMSTR.setSize(55, 30);
		status_ActorMSTR.setBounds(beginX + optionsWidth + 25 + status_MSTR.getWidth(), beginY + 190, status_ActorMSTR.getWidth(), status_ActorMSTR.getHeight());
		status_ActorMSTR.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		status_ActorMSTR.setText("0");
		statusDetails.add(status_ActorMSTR);
		add(status_ActorMSTR);
		
		status_AGI = new JTextField();
		initialJTextField(status_AGI, unchangeFontColor, JTextField.LEFT);
		status_AGI.setSize(60, 30);
		status_AGI.setBounds(beginX + optionsWidth + 20, beginY + 220, status_AGI.getWidth(), status_AGI.getHeight());
		status_AGI.setFont(unchangeFont);
		status_AGI.setText("AGI");
		statusDetails.add(status_AGI);
		add(status_AGI);
		
		status_AGI_CHT = new JTextField();
		initialJTextField(status_AGI_CHT, unchangeFontColor, JTextField.LEFT);
		status_AGI_CHT.setSize(50, 30);
		status_AGI_CHT.setBounds(beginX + optionsWidth + 25, beginY + 235, status_AGI_CHT.getWidth(), status_AGI_CHT.getHeight());
		status_AGI_CHT.setFont(unchangeFont_CHT);
		status_AGI_CHT.setText("敏捷");
		statusDetails.add(status_AGI_CHT);
		add(status_AGI_CHT);
		
		status_ActorAGI = new JTextField();
		initialJTextField(status_ActorAGI, changeFontColor, JTextField.RIGHT);
		status_ActorAGI.setSize(55, 30);
		status_ActorAGI.setBounds(beginX + optionsWidth + 25 + status_MSTR.getWidth(), beginY + 225, status_ActorAGI.getWidth(), status_ActorAGI.getHeight());
		status_ActorAGI.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		status_ActorAGI.setText("0");
		statusDetails.add(status_ActorAGI);
		add(status_ActorAGI);
		
		status_Equip = new JTextField();
		initialJTextField(status_Equip, unchangeFontColor, JTextField.LEFT);
		status_Equip.setSize(60, 30);
		status_Equip.setBounds(beginX + optionsWidth + 20, beginY + 350, status_Equip.getWidth(), status_Equip.getHeight());
		status_Equip.setFont(unchangeFont);
		status_Equip.setText("EQUIP");
		statusDetails.add(status_Equip);
		add(status_Equip);
		
		status_Equip_CHT = new JTextField();
		initialJTextField(status_Equip_CHT, unchangeFontColor, JTextField.LEFT);
		status_Equip_CHT.setSize(50, 30);
		status_Equip_CHT.setBounds(beginX + optionsWidth + 25, beginY + 365, status_Equip_CHT.getWidth(), status_Equip_CHT.getHeight());
		status_Equip_CHT.setFont(unchangeFont_CHT);
		status_Equip_CHT.setText("裝備");
		statusDetails.add(status_Equip_CHT);
		add(status_Equip_CHT);
	}
	
	private void initialItemDetail(){
		itemDescription = new JTextArea();
		itemDescription.setForeground(Color.WHITE);
		itemDescription.setOpaque(false);
		itemDescription.setVisible(false);
		itemDescription.setEditable(false);
		itemDescription.setSize(300, 160);
		itemDescription.setBounds(beginX + optionsWidth + 30, beginY + 430, itemDescription.getWidth(), itemDescription.getHeight());
		itemDescription.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		itemDescription.setText("挖 \n 甲意 \n   DNF  \n\n  齁勝!!!!");
		itemDetails.add(itemDescription);
		add(itemDescription);
		
		itemName = new JTextField[2][12];
		itemCounts = new JTextField[2][12];
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 2; j++){
				itemName[j][i] = new JTextField();
				initialJTextField(itemName[j][i], changeFontColor, JTextField.LEFT);
				itemName[j][i].setSize(200, 30);
				itemName[j][i].setBounds(beginX + optionsWidth + 30 + j * 290, beginY + 20 + i * 30, itemName[j][i].getWidth(), itemName[j][i].getHeight());
				itemName[j][i].setFont(itemNameFont);
				itemName[j][i].setText("水晶靈藥");
				itemDetails.add(itemName[j][i]);
				add(itemName[j][i]);
				
				itemCounts[j][i] = new JTextField();
				initialJTextField(itemCounts[j][i], changeFontColor, JTextField.RIGHT);
				itemCounts[j][i].setSize(50, 30);
				itemCounts[j][i].setBounds(beginX + optionsWidth + 30 + itemName[j][i].getWidth() + j * 290, beginY + 20 + i * 30, itemCounts[j][i].getWidth(), itemCounts[j][i].getHeight());
				itemCounts[j][i].setFont(itemCountFont);
				itemCounts[j][i].setText("0");
				itemDetails.add(itemCounts[j][i]);
				add(itemCounts[j][i]);
			}
		}
	}
	
	private void initialEquipDetail(){
		try {
			equip_arrow = ImageLoader.getImage("equip_arrow");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		equip_Attributes = new JTextField[5];
		equip_ActorAttributes = new JTextField[5];
		equip_EquipAttributes = new JTextField[5];
		currentEquip = new JTextField[7];
		equipName = new JTextField[5];
		activateEquip = new Equipment[2];
		
		for(int i=0; i<5; i++){
			equip_Attributes[i] = new JTextField();
			initialJTextField(equip_Attributes[i], changeFontColor, JTextField.LEFT);
			equip_Attributes[i].setSize(60, 30);
			equip_Attributes[i].setBounds(beginX + optionsWidth + 30, beginY + 430 + i * 30, equip_Attributes[i].getWidth(), equip_Attributes[i].getHeight());
			equip_Attributes[i].setFont(equipNameFont);
			switch(i){
				case 0:
					equip_Attributes[i].setText("HP");
					break;
				case 1:
					equip_Attributes[i].setText("MP");
					break;
				case 2:
					equip_Attributes[i].setText("STR");
					break;
				case 3:
					equip_Attributes[i].setText("MSTR");
					break;
				case 4:
					equip_Attributes[i].setText("AGI");
					break;
			}
			equipDetails.add(equip_Attributes[i]);
			add(equip_Attributes[i]);
			
			equip_ActorAttributes[i] = new JTextField();
			initialJTextField(equip_ActorAttributes[i], changeFontColor, JTextField.RIGHT);
			equip_ActorAttributes[i].setSize(70, 30);
			equip_ActorAttributes[i].setBounds(beginX + optionsWidth + 30 + 60, beginY + 430 + i * 30, equip_ActorAttributes[i].getWidth(), equip_ActorAttributes[i].getHeight());
			equip_ActorAttributes[i].setFont(equipNameFont);
			switch(i){
				case 0:
					equip_ActorAttributes[i].setText("0");
					break;
				case 1:
					equip_ActorAttributes[i].setText("0");
					break;
				case 2:
					equip_ActorAttributes[i].setText("0");
					break;
				case 3:
					equip_ActorAttributes[i].setText("0");
					break;
				case 4:
					equip_ActorAttributes[i].setText("0");
					break;
			}
			equipDetails.add(equip_ActorAttributes[i]);
			add(equip_ActorAttributes[i]);
			
			equip_EquipAttributes[i] = new JTextField();
			initialJTextField(equip_EquipAttributes[i], changeFontColor, JTextField.RIGHT);
			equip_EquipAttributes[i].setSize(70, 30);
			equip_EquipAttributes[i].setBounds(beginX + optionsWidth + 30 + 60 + 120, beginY + 430 + i * 30, equip_EquipAttributes[i].getWidth(), equip_EquipAttributes[i].getHeight());
			equip_EquipAttributes[i].setFont(equipNameFont);
			equip_EquipAttributes[i].setText(null);
			equipDetails.add(equip_EquipAttributes[i]);
			add(equip_EquipAttributes[i]);
		}
		
		for(int i=0; i<7; i++){
			currentEquip[i] = new JTextField();
			initialJTextField(currentEquip[i], changeFontColor, JTextField.LEFT);
			currentEquip[i].setSize(140,30);
			currentEquip[i].setBounds(beginX + optionsWidth + 440, beginY + 60 + i * 40, currentEquip[i].getWidth(), currentEquip[i].getHeight());
			currentEquip[i].setFont(equipNameFont);
			currentEquip[i].setText(null);
			equipDetails.add(currentEquip[i]);
			add(currentEquip[i]);
			
		}
		for(int i=0; i<5; i++){
			equipName[i] = new JTextField();
			initialJTextField(equipName[i], changeFontColor, JTextField.LEFT);
			equipName[i].setSize(200, 30);
			equipName[i].setBounds(beginX + optionsWidth + 30 + 310, beginY + 430 + i * 30, equipName[i].getWidth(), equipName[i].getHeight());
			equipName[i].setFont(equipNameFont);
			equipName[i].setText(null);
			equipDetails.add(equipName[i]);
			add(equipName[i]);
		}
	}
	
	private void initialJTextField(JTextField aJTF, Color fontColor, int alignment){
		aJTF.setVisible(false);
		aJTF.setBackground(null);
		aJTF.setOpaque(false);
		aJTF.setBorder(null);
		aJTF.setEditable(false);
		aJTF.setForeground(fontColor);
		aJTF.setHorizontalAlignment(alignment);
	}
	
	public int getCurrentLevel(){
		return currentLevel;
	}
	
	public void setCurrentLevel(int newLevel){
		currentLevel = newLevel;
	}
	
	public int getCurrentChoice(){
		return currentChoice;
	}
	
	public void setCurrentChoice(int newChoice){
		if(this.currentLevel == MAIN){
			if(newChoice < 0){
				newChoice += (QUIT - STATUS + 1);
				setCurrentChoice(newChoice);
			}
			else if(newChoice > QUIT - STATUS){
				newChoice %= (QUIT - STATUS + 1);
			}
			else if(!canSelect[newChoice]){
				if(newChoice > currentChoice){
					for(int i=newChoice; ;i++){
						if(i > QUIT - STATUS) i = 0;
						if(i < 0) i = QUIT - STATUS;
						if(canSelect[i]){
							newChoice = i;
							break;
						}
					}
				}
				else{
					for(int i=newChoice; ;i--){
						if(i > QUIT - STATUS) i = 0;
						if(i < 0) i = QUIT - STATUS;
						if(canSelect[i]){
							newChoice = i;
							break;
						}
					}
				}
			}
			currentChoice = newChoice;
		}
		else if(this.currentLevel == ITEM){
			if(newChoice <= 0){
				currentChoice = 0;
			}
			else if(newChoice >= itemHeroHas){
				currentChoice = itemHeroHas - 1;
			}
			else{
				currentChoice = newChoice;
			}
		}
		else if(this.currentLevel == EQUIP){
			if(equip_CurrentlyOnTop){
				if(newChoice < 0){
					currentChoice = 6;
				}
				else if(newChoice > 6){
					currentChoice = 0;
				}
				else{
					currentChoice = newChoice;
				}
			}
			else{
				if(newChoice < 0){
					if(hero.getBackPack().getCurrentEquipNumbers() > 4)	currentChoice = 4;
					else currentChoice = hero.getBackPack().getCurrentEquipNumbers();
				}
				else if(newChoice > 4 || newChoice > hero.getBackPack().getCurrentEquipNumbers()){
					currentChoice = 0;
				}
				else{
					currentChoice = newChoice;
				}
			}
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(mainOption[0][0], beginX, beginY, null);
        if(currentLevel == 0){
        	g.drawImage(cursorImage[MAIN], beginX + cursorOffsetX, beginY + cursorOffsetY + optionsOffsetY * currentChoice, null);
        }
        //Draw Main Option Panel's selectable or not
        for(int i = 1; i <= QUIT; i++){
        	if(canSelect[i-1]){
        		g.drawImage(mainOption[i][0], beginX + 24, beginY + 13 + optionsOffsetY * (i-1), null);
        	}
        	else{
        		g.drawImage(mainOption[i][1], beginX + 24, beginY + 13 + optionsOffsetY * (i-1), null);
        	}
        }
       
        //Draw Status big face on top right corner
        if(currentLevel == STATUS){
        	showStatusBigFace(g);
        }
        
        //Draw child option panel's background and cursor
        for(int i = 1; i < QUIT + 1; i++){
        	if(currentLevel == i){
        		g.drawImage(optionImage[i], beginX + optionsWidth, beginY, null);
        		if(currentLevel == ITEM && hero.getBackPack().getCurrentItemNumbers() > 0){
        			g.drawImage(cursorImage[i], beginX + optionsWidth + 15 + 290 * (currentChoice % 2), beginY + 20 + 30 * (currentChoice / 2), null);
        		}
        		else if(currentLevel == EQUIP){
        			if(equip_CurrentlyOnTop){
        				g.drawImage(cursorImage[i], beginX + optionsWidth + 15 + 420, beginY + 58 + 41 * currentChoice, currentEquip[0].getWidth() + 10, currentEquip[0].getHeight(), null);
        			}
        			else{
        				g.drawImage(cursorImage[i], beginX + optionsWidth + 15 + 320, beginY + 430 + 30 * currentChoice, equipName[0].getWidth() + 10, equipName[0].getHeight(), null);
        			}
        		}
        	}
        }
        
        //Situation: STATUS(1)
        if(currentLevel == STATUS){
        	if(!hasRefreshStatus){
        		hasRefreshStatus = true;
        		refreshStatus();
        	}
        	showStatusDetail(g);
        }
        else{
        	hasRefreshStatus = false;
        	hideStatusDetail();
        }
        //Situation: ITEM(3)
        if(currentLevel == ITEM){
        	if(!hasRefreshItem){
        		hasRefreshItem = true;
        		refreshItem();
        		refreshItemDescription = true;
        	}
        	if(refreshItemDescription){
        		if(hero.getBackPack().getCurrentItemNumbers() > 0){
        			BaseItem itemNow = BaseItemManage.getBaseItem(itemName[currentChoice%2][currentChoice/2].getText());
        		    itemDescription.setText(itemNow.getDescription());
        			refreshItemDescription = false;
        		}
        		else{
        			itemDescription.setText(null);
        		}
        	}
        	showItemDetail(g);
        }
        else{
        	hasRefreshItem = false;
        	hideItemDetail();
        }
      //Situation: EQUIP(4)
        if(currentLevel == EQUIP){
        	if(!hasRefreshEquip){
        		hasRefreshEquip = true;
        		refreshEquip();
        	}
        	if(refreshEquipAttribute){
        		refreshEquipAttribute();
        		refreshEquipAttribute = false;
        	}
        	showEquipDetail(g);
        }
        else{
        	hasRefreshEquip = false;
        	hideEquipDetail();
        }
        
    }
	
	private void refreshStatus(){
		for(int i=0; i<7; i++){
			if(hero.getEquipped()[i] != null){
				hero.getEquipped()[i].setTarget(hero);
				hero.getEquipped()[i].act();
			}
		}
		status_ActorName.setText(hero.getName());
		status_ActorLevel.setText("Lv   " + hero.level);
		status_ExpProgress.setText(hero.exp + "/" + 80);
		status_ActorHP.setText(hero.getAttributeNow().getHp()+ "/" + hero.getAttributeMax().getHp());
		status_ActorMP.setText(hero.getAttributeNow().getMp()+ "/" + hero.getAttributeMax().getMp());
		status_ActorSTR.setText(hero.getAttributeNow().getStr()+"");
		status_ActorMSTR.setText(hero.getAttributeNow().getMstr()+"");
		status_ActorAGI.setText(hero.getAttributeNow().getAgi()+"");
		for(int i=0; i<7; i++){
			if(hero.getEquipped()[i] != null){
				hero.getEquipped()[i].stop();
				hero.getEquipped()[i].setTarget(null);
			}
		}
	}
	
	private void showStatusBigFace(Graphics g){
		g.drawImage(mainHeroFaces, beginX + optionsWidth + toFaceWidth, beginY + 10, null);
	}
	
	private void showStatusDetail(Graphics g){
		int hpBarWidth = 120;
		g.setColor(Color.CYAN);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		
		for(int i=0; i<statusDetails.size();i++){
			statusDetails.get(i).setVisible(true);
		}
		
		g.drawImage(expBar.getSubimage(0, 0, (int)((double)hero.exp * expBar.getWidth() / 80), 5), beginX + optionsWidth + 120, beginY + 90, null);
		g.drawImage(expBox, beginX + optionsWidth + 120 - 2, beginY + 90 - 2, null);
		g.drawImage(hpBar.getSubimage(0, 0, (int)((double)hero.getAttributeNow().getHp() * hpBarWidth / hero.getAttributeMax().getHp()), 5), beginX + optionsWidth + 20, beginY + 140, (int)((double)hpBarWidth * hero.getAttributeNow().getHp()/hero.getAttributeMax().getHp()), expBox.getHeight() - 2, null);
		g.drawImage(expBox, beginX + optionsWidth + 20 - 2, beginY + 140 - 2, 120 + 2, expBox.getHeight(), null);
		g.drawImage(mpBar.getSubimage(0, 0, (int)((double)hero.getAttributeNow().getMp() * hpBarWidth / hero.getAttributeMax().getMp()), 5), beginX + optionsWidth + 160, beginY + 140, (int)((double)hpBarWidth * hero.getAttributeNow().getMp()/hero.getAttributeMax().getMp()), expBox.getHeight() - 2, null);
		g.drawImage(expBox, beginX + optionsWidth + 160 - 2, beginY + 140 - 2, 120 + 2, expBox.getHeight(), null);
		
		g.drawImage(currentFace, beginX + optionsWidth + 10, beginY + 10, null);
	}
	
	private void hideStatusDetail(){
		for(int i=0; i<statusDetails.size();i++){
			statusDetails.get(i).setVisible(false);
		}
	}
	
	private void refreshItem(){
		itemHeroHas = hero.getBackPack().getCurrentItemNumbers();
		if(itemHeroHas == 0){
			itemName[0][0].setText(null);
			itemCounts[0][0].setText(null);
		}
		if(currentChoice >= itemHeroHas) currentChoice = itemHeroHas - 1;
		
		for(int i=0; i<24; i++){
			if(i < itemHeroHas){
				itemName[i%2][i/2].setText(hero.getBackPack().getItemInBag(i).getName());
				itemCounts[i%2][i/2].setText(hero.getBackPack().getItemNumbersInBag(i)+"");
			}
			else{
				itemName[i%2][i/2].setText(null);
				itemCounts[i%2][i/2].setText(null);
			}
		}
	}
	
	private void showItemDetail(Graphics g){
		if(itemHeroHas != 0){
			for(int i=0; i<itemDetails.size() && i < itemHeroHas * 2 + 1;i++){
				itemDetails.get(i).setVisible(true);
			}
		}
		else{
			for(int i=0; i<itemDetails.size(); i++){
				itemDetails.get(i).setVisible(true);
			}
		}
		//currentItemNumber = 0;
	}
	
	private void hideItemDetail(){
		for(int i=0; i<itemDetails.size() && i < itemHeroHas*2 + 1;i++){
			itemDetails.get(i).setVisible(false);
		}
	}
	
	public void useItem(){
		BaseItem someItem = BaseItemManage.getBaseItem(itemName[currentChoice%2][currentChoice/2].getText());
		someItem.setTarget(hero);
		someItem.act();
		hero.useItem(currentChoice);
		someItem.setTarget(null);
	}
	
	private void refreshEquip(){
		equipHeroHas = hero.getBackPack().getCurrentEquipNumbers();
		
		for(int i=0; i<7; i++){
			if(hero.getEquipped()[i] != null){
				//System.out.println(i + " is not null!!!");
				//System.out.println(hero.getAttributeNow().getHp());
				hero.getEquipped()[i].setTarget(hero);
				hero.getEquipped()[i].act();
				//System.out.println(hero.getAttributeNow().getHp());
			}
		}
		equip_ActorAttributes[0].setText(hero.getAttributeMax().getHp()+"");
		equip_ActorAttributes[1].setText(hero.getAttributeMax().getMp()+"");
		equip_ActorAttributes[2].setText(hero.getAttributeNow().getStr()+"");
		equip_ActorAttributes[3].setText(hero.getAttributeNow().getMstr()+"");
		equip_ActorAttributes[4].setText(hero.getAttributeNow().getAgi()+"");
		
		equip_EquipAttributes[0].setText(hero.getAttributeMax().getHp()+"");
		equip_EquipAttributes[1].setText(hero.getAttributeMax().getMp()+"");
		equip_EquipAttributes[2].setText(hero.getAttributeNow().getStr()+"");
		equip_EquipAttributes[3].setText(hero.getAttributeNow().getMstr()+"");
		equip_EquipAttributes[4].setText(hero.getAttributeNow().getAgi()+"");
		
		for(int i=0; i<7; i++){
			if(hero.getEquipped()[i] != null){
				currentEquip[i].setText(hero.getEquipped()[i].getName());
				hero.getEquipped()[i].stop();
				hero.getEquipped()[i].setTarget(null);
			}
			else{
				currentEquip[i].setText(null);
			}
		}
		
		for(int i=0; i<5; i++){
			if(i < equipHeroHas){
				equipName[i].setText(hero.getBackPack().getEquipInBag(i).getName());
			}
			else{
				equipName[i].setText(null);
			}
		}
		
		refreshEquipAttribute();
	}
	
	private void refreshEquipAttribute(){
		Equipment newEquipment;
		if(!equip_CurrentlyOnTop){
			newEquipment = (Equipment)BaseItemManage.getBaseItem(equipName[currentChoice].getText());
			if(newEquipment != null){
				for(int i=0; i<7; i++){
					if(hero.getEquipped()[i] != null && newEquipment.getType() != hero.getEquipped()[i].getType()){
						hero.getEquipped()[i].setTarget(hero);
						hero.getEquipped()[i].act();
					}
				}
				
				newEquipment.setTarget(hero);
				newEquipment.act();
				
				equip_EquipAttributes[0].setText(hero.getAttributeMax().getHp()+"");
				equip_EquipAttributes[1].setText(hero.getAttributeMax().getMp()+"");
				equip_EquipAttributes[2].setText(hero.getAttributeNow().getStr()+"");
				equip_EquipAttributes[3].setText(hero.getAttributeNow().getMstr()+"");
				equip_EquipAttributes[4].setText(hero.getAttributeNow().getAgi()+"");
				
				for(int i=0; i<5; i++){
					if(Integer.valueOf(equip_EquipAttributes[i].getText()) > Integer.valueOf(equip_ActorAttributes[i].getText()))
						equip_EquipAttributes[i].setForeground(Color.GREEN);
					else if(Integer.valueOf(equip_EquipAttributes[i].getText()) < Integer.valueOf(equip_ActorAttributes[i].getText()))
						equip_EquipAttributes[i].setForeground(Color.GRAY);
					else
						equip_EquipAttributes[i].setForeground(changeFontColor);
				}
				
				for(int i=0; i<7; i++){
					if(hero.getEquipped()[i] != null && newEquipment.getType() != hero.getEquipped()[i].getType()){
						hero.getEquipped()[i].stop();
						hero.getEquipped()[i].setTarget(null);
					}
				}
				newEquipment.stop();
				newEquipment.setTarget(null);
			}
			else{
				for(int i=0; i<5; i++){
					equip_EquipAttributes[i].setForeground(changeFontColor);
					equip_EquipAttributes[i].setText(equip_EquipAttributes[i].getText());	
				}
			}
		}
		else{
			for(int i=0; i<5; i++){
				equip_EquipAttributes[i].setForeground(changeFontColor);
				equip_EquipAttributes[i].setText(equip_EquipAttributes[i].getText());	
			}
		}
	}
	
	private void showEquipDetail(Graphics g){
		for(int i=0; i<equipDetails.size();i++){
			equipDetails.get(i).setVisible(true);
		}
		for(int i=0; i<5; i++){
			g.drawImage(equip_arrow, beginX + optionsWidth + 170, beginY + 434 + i * 30, null);
		}
	}
	
	private void hideEquipDetail(){
		for(int i=0; i<equipDetails.size();i++){
			equipDetails.get(i).setVisible(false);
		}
	}
	
	public void putOffEquip(){
		if(equip_CurrentlyOnTop){
			hero.putOffEquip(currentChoice);
		}
	}
	
	public void putOnEquip(){
		if(!equip_CurrentlyOnTop){
			hero.putOnEquip(currentChoice);
		}
	}
	
	/*
	public static void main(String[] args){
		ImageLoader.initial();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 700);
		
		OptionPanel A = new OptionPanel(800,700);
		A.setVisible(true);
		A.setBackground(null);
		frame.add(A);
		
		frame.setVisible(true);
		
	}
	*/
}

