package gui;

import gui.option.OptionPanel;

import java.awt.event.*;

import system.AttributeManage;
import system.actor.BaseActor;
import system.actor.MainHero;
import system.arena.BaseArena;
import system.map.MapMoveable;

/**
 * A KeyListener to handle user's keyboard action
 * Last Update: Add interaction on ITEM mode in optionPanel. at 2013/06/24 01:37
 * @author B98505005
 *
 */
public class ActorKeyListener implements KeyListener{
	private Screen screen;
	private MainHero hero;
	
	public ActorKeyListener(Screen screen, MainHero hero){
		this.screen = screen;
		this.hero = hero;
		//eachMove = Game.SPRITEHEIGHT / 4;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		//System.out.println(hero.getX()/32 +" "+ hero.getY()/32);
		if(!hero.isStillMoving && screen.getCurrentStatus() == Screen.MAP){
			keyPressedOnMap(arg0);
		}
		else if(!hero.isStillMoving && screen.getCurrentStatus() == Screen.OPTIONS){
			keyPressedOnOption(arg0);
		}
		else if(!hero.isStillMoving && screen.getCurrentStatus() == Screen.DIALOG){
			keyPressedOnDialog(arg0);
		}
		else if(!hero.isStillMoving && screen.getCurrentStatus() == Screen.BATTLE){
			keyPressedInBattle(arg0);
		}
	}
	
	private void keyPressedOnMap(KeyEvent arg0){
		if(arg0.getKeyCode() == KeyEvent.VK_Z){
			screen.setCurrentStatus(Screen.BATTLE);
			//MainHero hero = new MainHero(0, "Player");
			BaseActor[] monster = new BaseActor[3];
			monster[0] = new BaseActor(3,"TA A");
			monster[0].setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
			monster[0].setAttributeNow(new AttributeManage(50, 80, 3, 5, 15));
			monster[1] = new BaseActor(3,"TA B");
			monster[1].setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
			monster[1].setAttributeNow(new AttributeManage(50, 80, 7, 4, 6));
			monster[2] = new BaseActor(3,"TA C");
			monster[2].setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
			monster[2].setAttributeNow(new AttributeManage(50, 80, 4, 6, 8));
			
			screen.getBattlePanel().setBattleField(new BaseArena("test", 1000, 1.0), hero, monster);
			screen.getBattlePanel().setStatus(BattlePanel.BEGIN);
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_V){
			screen.isConstantlyRedraw = !screen.isConstantlyRedraw;
		}
		
		
		if(arg0.getKeyCode() == KeyEvent.VK_UP)	hero.setCharacterPose(10);
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) hero.setCharacterPose(1);
		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) hero.setCharacterPose(7);
		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT) hero.setCharacterPose(4);
		
		if(arg0.getKeyCode() == KeyEvent.VK_UP && 
		   hero.getY() - Game.SPRITEHEIGHT >= 0 && 
		   canMoveUp())
		{
			hero.move(BaseActor.UP);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN &&
				hero.getY() + Game.SPRITEHEIGHT < screen.getCurrentMap().getHeight() && 
				canMoveDown())
		{
			hero.move(BaseActor.DOWN);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT && 
				hero.getX() + Game.SPRITEWIDTH < screen.getCurrentMap().getWidth() && 
				canMoveRight())
		{
			hero.move(BaseActor.RIGHT);
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT && 
				hero.getX() - Game.SPRITEHEIGHT >= 0 && 
				canMoveLeft())
		{
			hero.move(BaseActor.LEFT);
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE || arg0.getKeyCode() == KeyEvent.VK_F1){
			screen.getOptionPanel().setCurrentChoice(0);
			screen.getOptionPanel().setVisible(true);
			screen.setCurrentStatus(Screen.OPTIONS);
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE || arg0.getKeyCode() == KeyEvent.VK_F2){
			if(checkAdjacentTalkable()){
				BaseActor A = findTheNextActor();
				if(A.getDialogListSize() > 0){
					screen.setCurrentStatus(Screen.DIALOG);
					screen.getDialogPanel().beginConversation(A.getDialog(A.nextDialogID));
				}
				//screen.getDialogPanel().beginConversation(screen.getCurrentMap().getDialog(0));
				//screen.getDialogPanel().showDialog(0);
			}
		}
		
		//talk to myself
		if(arg0.getKeyCode() == KeyEvent.VK_F4){
			BaseActor A = screen.getCurrentMap().getNPC(0);
			if(A.getDialogListSize() > 0){
				screen.setCurrentStatus(Screen.DIALOG);
				screen.getDialogPanel().beginConversation(A.getDialog(A.nextDialogID));
			}
		}
	}

	private void keyPressedOnOption(KeyEvent arg0){
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE || arg0.getKeyCode() == KeyEvent.VK_F1){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.MAIN){
				screen.getOptionPanel().setCurrentChoice(0);
				screen.getOptionPanel().setVisible(false);
				screen.setCurrentStatus(Screen.MAP);
			}
			else{
				screen.getOptionPanel().setCurrentChoice(0);
				screen.getOptionPanel().setCurrentLevel(OptionPanel.MAIN);
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_UP){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.MAIN)
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() - 1);
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.ITEM){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() - 2);
				screen.getOptionPanel().refreshItemDescription = true;
			}
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.EQUIP){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() - 1);
				if(!screen.getOptionPanel().equip_CurrentlyOnTop)
					screen.getOptionPanel().refreshEquipAttribute = true;
			}
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.MAIN){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() + 1);
			}
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.ITEM){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() + 2);
				screen.getOptionPanel().refreshItemDescription = true;
			}
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.EQUIP){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() + 1);
				if(!screen.getOptionPanel().equip_CurrentlyOnTop)
					screen.getOptionPanel().refreshEquipAttribute = true;
			}
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.ITEM){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() + 1);
				screen.getOptionPanel().refreshItemDescription = true;
			}
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.ITEM){
				screen.getOptionPanel().setCurrentChoice(screen.getOptionPanel().getCurrentChoice() - 1);
				screen.getOptionPanel().refreshItemDescription = true;
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_SHIFT){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.EQUIP){
				screen.getOptionPanel().equip_CurrentlyOnTop = !screen.getOptionPanel().equip_CurrentlyOnTop;
				screen.getOptionPanel().setCurrentChoice(0);
				screen.getOptionPanel().refreshEquipAttribute = true;
			}
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER || arg0.getKeyCode() == KeyEvent.VK_SPACE){
			if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.MAIN){
				if(screen.getOptionPanel().getCurrentChoice() == OptionPanel.QUIT - 1){
					
				}
				else{
					screen.getOptionPanel().setCurrentLevel(screen.getOptionPanel().getCurrentChoice() + 1);
					screen.getOptionPanel().setCurrentChoice(0);
				}
			}
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.ITEM){
				if(hero.getBackPack().getCurrentItemNumbers() > 0){
					screen.getOptionPanel().useItem();
					screen.getOptionPanel().hasRefreshItem = false;
				}
			}
			else if(screen.getOptionPanel().getCurrentLevel() == OptionPanel.EQUIP){
				if(screen.getOptionPanel().equip_CurrentlyOnTop){
					screen.getOptionPanel().putOffEquip();
				}
				else{
					screen.getOptionPanel().putOnEquip();
				}
				screen.getOptionPanel().hasRefreshEquip = false;
				screen.getOptionPanel().refreshEquipAttribute = true;
			}
		}
	}
	
	private void keyPressedOnDialog(KeyEvent arg0){
		System.out.println("HERE");
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE || arg0.getKeyCode() == KeyEvent.VK_F2){
			if(screen.getDialogPanel().getBeginLines() >= 0){
				screen.getDialogPanel().keepShowingDialog();
			}
			else if(screen.getDialogPanel().getBeginLines() == DialogPanel.DONE){
				if(findTheNextActor() != null){
					
				}
				else{
					BaseActor A = screen.getCurrentMap().getNPC(0);
					if(A.nextDialogID + 1 < A.getDialogListSize())
						A.nextDialogID++;
					else
						A.nextDialogID = 0;
				}
				screen.getDialogPanel().hideDialog();
				screen.setCurrentStatus(Screen.MAP);
			}
			else if(screen.getDialogPanel().getBeginLines() == DialogPanel.THIS_ONE_DONE){
				//screen.getDialogPanel().keepConversation(screen.getCurrentMap().getDialog(0));
				if(findTheNextActor() != null){
					screen.getDialogPanel().keepConversation(findTheNextActor().getDialog(0));
				}
				else{
					BaseActor A = screen.getCurrentMap().getNPC(0);
					System.out.println("SIZE:"+A.getDialogListSize());
					System.out.println("ID:"+A.nextDialogID);
					screen.getDialogPanel().keepConversation(A.getDialog(A.nextDialogID));
				}
			}
		}
	}
	
	private void keyPressedInBattle(KeyEvent arg0){
		if(screen.getBattlePanel().isShowingResult && arg0.getKeyCode() == KeyEvent.VK_SPACE){
			screen.setCurrentStatus(Screen.MAP);
			screen.getBattlePanel().finishBattle();
		}
		
		if(screen.getBattlePanel().getStatus() != BattlePanel.WAIT_FOR_ACTION 
				&& arg0.getKeyCode() == KeyEvent.VK_ENTER
				&& screen.getBattlePanel().currentlyShowingMessages){
			screen.getBattlePanel().messageCounter = screen.getBattlePanel().TIME_PAUSE;
		}
		
		if(screen.getBattlePanel().getStatus() == BattlePanel.WAIT_FOR_ACTION){
			if(arg0.getKeyCode() == KeyEvent.VK_A){
				
			}
			if(arg0.getKeyCode() == KeyEvent.VK_UP){
				screen.getBattlePanel().setCurrentChoice(screen.getBattlePanel().getCurrentChoice() - 1);
			}
			else if(arg0.getKeyCode() == KeyEvent.VK_DOWN){
				screen.getBattlePanel().setCurrentChoice(screen.getBattlePanel().getCurrentChoice() + 1);
			}
			else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT){
				if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.PICK_MONSTER){
					screen.getBattlePanel().setCurrentChoice(screen.getBattlePanel().getCurrentChoice() + 1);
				}
			}
			else if(arg0.getKeyCode() == KeyEvent.VK_LEFT){
				if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.PICK_MONSTER){
					screen.getBattlePanel().setCurrentChoice(screen.getBattlePanel().getCurrentChoice() - 1);
				}
			}
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
				if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.MENU){
					switch(screen.getBattlePanel().getCurrentChoice()){
						case 0:
							screen.getBattlePanel().setSelectedSkillIndex(0);
							screen.getBattlePanel().setCurrentLevel(BattlePanel.PICK_MONSTER);
							screen.getBattlePanel().setCurrentChoice(0);
							break;
						case 1:
							screen.getBattlePanel().setCurrentLevel(BattlePanel.SKILL);
							screen.getBattlePanel().loadHeroSkill();
							screen.getBattlePanel().setCurrentChoice(0);
							break;
						case 2:
							screen.getBattlePanel().setCurrentLevel(BattlePanel.DONE);
							screen.getBattlePanel().setCurrentChoice(0);
							break;
						default:
							screen.getBattlePanel().setCurrentLevel(BattlePanel.MENU);
							screen.getBattlePanel().setCurrentChoice(0);
							break;
					}
				}
				else if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.SKILL){
					screen.getBattlePanel().setSelectedSkillIndex(screen.getBattlePanel().getCurrentChoice());
					screen.getBattlePanel().setSkillSelected();
					screen.getBattlePanel().setCurrentLevel(BattlePanel.PICK_MONSTER);
					screen.getBattlePanel().setCurrentChoice(0);
				}
				else if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.PICK_MONSTER){
					screen.getBattlePanel().setSelectedTargetIndex(screen.getBattlePanel().getCurrentChoice());
					screen.getBattlePanel().setCurrentLevel(BattlePanel.DONE);
					screen.getBattlePanel().setCurrentChoice(0);
				}
			}
			if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(screen.getBattlePanel().getCurrentLevel() == BattlePanel.MENU)
					screen.getBattlePanel().setCurrentChoice(2);
				else{
					screen.getBattlePanel().returnToMenu();
					screen.getBattlePanel().setCurrentLevel(BattlePanel.MENU);
					screen.getBattlePanel().setCurrentChoice(0);
				}
			}
			
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	/**
	 * Check whether actor go up is available
	 * @return available or not
	 */
	public boolean canMoveUp(){
		int xTile = hero.getX() / Game.SPRITEWIDTH;
		int yTile = hero.getY() / Game.SPRITEHEIGHT - 1;
		
		return checkNextTileMoveable(xTile, yTile);
	}
	
	/**
	 * Check whether actor go down is available
	 * @return available or not
	 */
	public boolean canMoveDown(){
		int xTile = hero.getX() / Game.SPRITEWIDTH;
		int yTile = hero.getY() / Game.SPRITEHEIGHT + 1;
		
		return checkNextTileMoveable(xTile, yTile);
	}
	
	/**
	 * Check whether actor go right is available
	 * @return available or not
	 */
	public boolean canMoveRight(){
		int xTile = hero.getX() / Game.SPRITEWIDTH + 1;
		int yTile = hero.getY() / Game.SPRITEHEIGHT;
		
		return checkNextTileMoveable(xTile, yTile);
	}
	
	/**
	 * Check whether actor go left is available
	 * @return available or not
	 */
	public boolean canMoveLeft(){
		int xTile = hero.getX() / Game.SPRITEWIDTH - 1;
		int yTile = hero.getY() / Game.SPRITEHEIGHT;
		
		return checkNextTileMoveable(xTile, yTile);
	}

	/**
	 * Check a specific tile is occupied by some can't move on objects or not
	 * @param xTile the x number of tile 
	 * @param yTile the y number of tile
	 * @return true if actor can go onto the tile
	 */
	private boolean checkNextTileMoveable(int xTile, int yTile){
		int level0 = MapMoveable.getMoveable(screen.getCurrentMap().getSprite(0, xTile, yTile));
		int level1 = MapMoveable.getMoveable(screen.getCurrentMap().getSprite(1, xTile, yTile));
		int level2 = MapMoveable.getMoveable(screen.getCurrentMap().getSprite(2, xTile, yTile));
		
		// Check tile's characteristic
		if (level0 == MapMoveable.CANT_MOVE_TO ||
			level1 == MapMoveable.CANT_MOVE_TO ||
			level2 == MapMoveable.CANT_MOVE_TO 
			){
				return false;
			}
		
		// Check if there's NPC or not
		for(int i = 0; i < screen.getCurrentMap().getNumberOfNPC(); i++){
			if (screen.getCurrentMap().getNPC(i).getX() == xTile * Game.SPRITEWIDTH &&
				screen.getCurrentMap().getNPC(i).getY() == yTile * Game.SPRITEHEIGHT
				){
				return false;
			}
		}
		
		return true;
	}

	private boolean checkAdjacentTalkable(){
		for(int i = 0; i < screen.getCurrentMap().getNumberOfNPC(); i++){
			if(hero.getCharacterPose() == BaseActor.UP * 3 + 1){
				if(hero.getX() == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() - Game.SPRITEHEIGHT == screen.getCurrentMap().getNPC(i).getY()){
					return true;
				}
			}
			else if(hero.getCharacterPose() == BaseActor.DOWN * 3 + 1){
				if(hero.getX() == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() + Game.SPRITEHEIGHT == screen.getCurrentMap().getNPC(i).getY()){
					return true;
				}
			}
			else if(hero.getCharacterPose() == BaseActor.RIGHT * 3 + 1){
				if(hero.getX() + Game.SPRITEWIDTH == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() == screen.getCurrentMap().getNPC(i).getY()){
					return true;
				}
			}
			else if(hero.getCharacterPose() == BaseActor.LEFT * 3 + 1){
				if(hero.getX() - Game.SPRITEWIDTH == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() == screen.getCurrentMap().getNPC(i).getY()){
					return true;
				}
			}
			
		}
		return false;
	}

	private BaseActor findTheNextActor(){
		for(int i = 0; i < screen.getCurrentMap().getNumberOfNPC(); i++){
			if(hero.getCharacterPose() == BaseActor.UP * 3 + 1){
				if(hero.getX() == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() - Game.SPRITEHEIGHT == screen.getCurrentMap().getNPC(i).getY()){
					return screen.getCurrentMap().getNPC(i);
				}
			}
			else if(hero.getCharacterPose() == BaseActor.DOWN * 3 + 1){
				if(hero.getX() == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() + Game.SPRITEHEIGHT == screen.getCurrentMap().getNPC(i).getY()){
					return screen.getCurrentMap().getNPC(i);
				}
			}
			else if(hero.getCharacterPose() == BaseActor.RIGHT * 3 + 1){
				if(hero.getX() + Game.SPRITEWIDTH == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() == screen.getCurrentMap().getNPC(i).getY()){
					return screen.getCurrentMap().getNPC(i);
				}
			}
			else if(hero.getCharacterPose() == BaseActor.LEFT * 3 + 1){
				if(hero.getX() - Game.SPRITEWIDTH == screen.getCurrentMap().getNPC(i).getX() && 
				   hero.getY() == screen.getCurrentMap().getNPC(i).getY()){
					return screen.getCurrentMap().getNPC(i);
				}
			}
			
		}
		return null;
	}
}
