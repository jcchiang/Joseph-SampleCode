package system.actor;

import java.util.ArrayList;

//import saveandload.HeroFile;

import gui.Game;
import system.arena.*;
import system.arena.skill.StrAttackSkill;
import system.AttributeManage;
import system.DialogDetail;

/**
 * Define basic information of human actor. Last Update: Add AttributeManage at
 * 2013/06/23 18:59
 * 
 * @author B98505005, B97502052
 * 
 */
public class BaseActor {
	private final int ID;
	private String name;
	private AttributeManage attributeMax;
	private AttributeManage attributeNow;
	private ArrayList<AttackSkill> attackSkillList = new ArrayList<AttackSkill>();

	/* GUI related */
	private int x = 64, y = 128;
	private int characterPose;
	private int move, eachMove, lastMove;
	private int attackCount;
	public boolean isStillMoving;
	public final static int IM_MAIN_HERO = -1;
	public final static int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;
	public final static int FACE_DOWN = 1, FACE_LEFT = 4, FACE_RIGHT = 7,
			FACE_UP = 10;
	public int nextDialogID = 0;
	/* Data */
	//private ArrayList<String> dialogs;

	/* Not be used data now */
	public int level = 1, exp = 46;

	public BaseActor() {
		this(IM_MAIN_HERO, "test");
	}

	public BaseActor(int ID, String name) {
		this.ID = ID;
		this.name = name;
		init();
	}

	// This not yet applied to orininal project
	/*
	 * public BaseActor(HeroFile oldHero){ ID = oldHero.ID; name = new
	 * String(oldHero.name); x = oldHero.x; y = oldHero.y; characterPose =
	 * oldHero.characterPose; level = oldHero.level; exp = oldHero.exp; hp =
	 * oldHero.hp; mp = oldHero.mp; str = oldHero.str; agi = oldHero.agi; mstr =
	 * oldHero.mstr; }
	 */

	private void init() {
		characterPose = 1;
		move = 0;
		eachMove = 0;
		lastMove = 0;
		isStillMoving = false;
		initSkill();
		//dialogs = new ArrayList<String>();
		//if (ID != IM_MAIN_HERO) {
		//	dialogs = DialogLoader.getDialog(ID);
		//}
		//dialogListList = new ArrayList<ArrayList<String>>();
		
	}

	private void initSkill() {
		addSkill(new StrAttackSkill("隨便揮一拳", "打我啊笨蛋~~", 1.0));
	}
	
	/**
	 * Because of leaking some information, this function should be called
	 * before use BaseActor's method.
	 * 
	 * @param attributeMax
	 * @param attributeNow
	 */
	public void initBeforeUse(AttributeManage attributeMax,
			AttributeManage attributeNow) {
		this.attributeMax = attributeMax;
		this.attributeNow = attributeNow;
	}

	/**
	 * @param attackSkill
	 *            Skill to be added
	 */
	public void addSkill(AttackSkill attackSkill) {
		attackSkillList.add(attackSkill);
	}

	public AttackSkill getSkill(int index) {
		return attackSkillList.get(index);
	}

	/**
	 * @return The skill number of this character
	 */
	public int getSkillNumber() {
		return attackSkillList.size();
	}

	/**
	 * Each tick the attackCount will be added the value of arena's multiplier *
	 * agi of character. Once attackCount access the attackLimit, the character
	 * can take an attack in this time.
	 * 
	 * @param attackMul
	 *            The attack multiplier of arena
	 * @param attackLimit
	 *            The attack limit of arena
	 * @return True if character can attack in this time, else false.
	 * 
	 */
	public boolean setForAttack(double attackMul, int attackLimit) {
		attackCount += getAttributeNow().getAgi() * attackMul;
		if (attackCount >= attackLimit) {
			attackCount -= attackLimit;
			return true;
		}
		return false;
	}

	/**
	 * This method implements the fighting policy of character.
	 * 
	 * @param arena
	 *            An arena instance.
	 */
	public void decide(BaseArena arena) {
		AttackSkill selectedSkill = getSkill(0);
		selectedSkill.setTime(arena.getClockTime());
		selectedSkill.initBeforeUse(selectedSkill.getTime(), this, arena.getMainHero(), arena);
		arena.addEvent(selectedSkill);
	}

	public void act(BaseArena arena) {

	}

	/**
	 * @return True if character is dead, else false.
	 */
	public boolean isDead() {
		return (getAttributeNow().getHp() <= 0);
	}

	public final void setName(String Name) {
		name = Name;
	}

	public final String getName() {
		return name;
	}

	public final int getID() {
		return ID;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final void setX(int newX) {
		x = newX;
	}

	public final void setY(int newY) {
		y = newY;
	}

	public final void setPositition(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public final void setTilePostition(int newX, int newY) {
		x = newX * Game.SPRITEWIDTH;
		y = newY * Game.SPRITEHEIGHT;
	}

	public final int getCharacterPose() {
		return characterPose;
	}

	public final void setCharacterPose(int pose) {
		characterPose = pose;
	}

//	public final String getDialog(int dialogID) {
//		return dialogs.get(dialogID);
//	}

	public final void move(int direction) {
		isStillMoving = true;
		lastMove = (lastMove + 1) % 2;
		if (direction == DOWN) {
			move = Game.SPRITEHEIGHT;
			eachMove = move / 4;
			// setCharacterPose(DOWN * 3);
		} else if (direction == LEFT) {
			move = Game.SPRITEWIDTH;
			eachMove = move / 4;
			// setCharacterPose(LEFT * 3);
		} else if (direction == RIGHT) {
			move = Game.SPRITEWIDTH;
			eachMove = move / 4;
			// setCharacterPose(RIGHT * 3);
		} else if (direction == UP) {
			move = Game.SPRITEHEIGHT;
			eachMove = move / 4;
			// setCharacterPose(UP * 3);
		}
	}

	public final void update() {
		int[][] down = { { 1, 1, 2, 2, 1 }, { 1, 1, 0, 0, 1 } }, left = {
				{ 4, 4, 5, 5, 4 }, { 4, 4, 3, 3, 4 } }, right = {
				{ 7, 7, 8, 8, 7 }, { 7, 7, 6, 6, 7 } }, up = {
				{ 10, 10, 11, 11, 10 }, { 10, 10, 9, 9, 10 } };
		if (move != 0) {
			if (characterPose / 3 == DOWN) {
				setY(getY() + eachMove);
				move -= eachMove;
				setCharacterPose(down[lastMove][(Game.SPRITEWIDTH - move)
						/ eachMove]);
				if (move == 0)
					isStillMoving = false;
			} else if (characterPose / 3 == LEFT) {
				setX(getX() - eachMove);
				move -= eachMove;
				setCharacterPose(left[lastMove][(Game.SPRITEWIDTH - move)
						/ eachMove]);
				if (move == 0)
					isStillMoving = false;
			} else if (characterPose / 3 == RIGHT) {
				setX(getX() + eachMove);
				move -= eachMove;
				setCharacterPose(right[lastMove][(Game.SPRITEHEIGHT - move)
						/ eachMove]);
				if (move == 0)
					isStillMoving = false;
			} else if (characterPose / 3 == UP) {
				setY(getY() - eachMove);
				move -= eachMove;
				setCharacterPose(up[lastMove][(Game.SPRITEHEIGHT - move)
						/ eachMove]);
				if (move == 0)
					isStillMoving = false;
			}
		}
	}

	public AttributeManage getAttributeMax() {
		return attributeMax;
	}

	public void setAttributeMax(AttributeManage attributeMax) {
		this.attributeMax = attributeMax;
	}

	public AttributeManage getAttributeNow() {
		return attributeNow;
	}

	public void setAttributeNow(AttributeManage attributeNow) {
		this.attributeNow = attributeNow;
	}
	
	private ArrayList<DialogDetail> actorDialog = new ArrayList<DialogDetail>();
	public void addDialogDetail(DialogDetail dd){
		actorDialog.add(dd);
	}
	public int getDialogListSize(){
		return actorDialog.size();
	}
	public DialogDetail getDialog(int index) {
		return actorDialog.get(index);
	}
	
}
