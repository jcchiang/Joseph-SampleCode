package system.arena;

import system.AttributeManage;
import system.actor.BaseActor;

/**
 * @author B97502052
 * 
 */
public class AttackSkill extends Event {
	private int value;
	private double multiplier;
	private BaseActor target;
	private BaseArena arena;

	public AttackSkill() {
		super();
		multiplier = 1;
	}

	public AttackSkill(String name, String message, double multiplier) {
		super(name, message, 0, Event.Priority.ATTACK_SKILL, null);
		this.multiplier = multiplier;
	}

	/**
	 * Call before a skill being used to supply the information
	 * 
	 * @param time
	 *            Time
	 * @param user
	 *            Skill user
	 * @param target
	 *            Skill target
	 * @param arena
	 *            An arena instance
	 */
	public void initBeforeUse(int time, BaseActor user, BaseActor target,
			BaseArena arena) {
		setTime(time);
		setUser(user);
		this.target = target;
		this.arena = arena;
		setValue(calculateValue());
	}

	public BaseActor getTarget() {
		return target;
	}

	public void setTarget(BaseActor target) {
		this.target = target;
	}

	public BaseArena getArena() {
		return arena;
	}

	public void setArena(BaseArena arena) {
		this.arena = arena;
	}

	/**
	 * Give a multiplier to multiply the original multiplier
	 * 
	 * @param mu
	 *            The multiplier to multiply
	 */
	public void mulMultiplier(double mu) {
		multiplier *= mu;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Value should be calculated by skill's formula.
	 */
	public int calculateValue() {
		return 0;
	}

	public void act() {
		AttributeManage val = new AttributeManage(getValue(), 0, 0, 0, 0);
		getTarget().getAttributeNow().subtract(val);
	}
}
