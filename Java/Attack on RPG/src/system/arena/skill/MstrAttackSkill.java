package system.arena.skill;

import system.arena.AttackSkill;

/**
 * @author B97502052
 * 
 */
public class MstrAttackSkill extends AttackSkill {

	public MstrAttackSkill(String name, String message, double multiuplier) {
		super(name, message, multiuplier);
	}

	public int calculateValue() {
		return (int) (getUser().getAttributeNow().getMstr() * getMultiplier());
	}

}
