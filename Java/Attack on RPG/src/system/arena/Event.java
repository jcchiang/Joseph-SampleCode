package system.arena;

import system.actor.BaseActor;

/**
 * @author B97502052
 * 
 */
public class Event {
	/**
	 * Define the priority of events.
	 * 
	 */
	public enum Priority {
		SYSTEM_KEEP, ARENA_EFFECT, SPECIAL_EVENT, ONE_ROUND_EFFECT_START, SPECIAL_SKILL, DOT, HEAL_SKILL, ATTACK_SKILL, ONE_ROUND_EFFECT_END, CLOCK_DEPEND;
		public static final int size = Priority.values().length;
	}

	private int time;
	private String name;
	private String message;
	private Priority priority;
	private BaseActor user;

	public Event() {
		this("DefaltEventName", "DefaultEventMsg", 0, Priority.SYSTEM_KEEP, null);
	}

	public Event(String name, String message, int time, Priority priority,
			BaseActor user) {
		this.name = name;
		this.message = message;
		this.time = time;
		this.priority = priority;
		this.user = user;
	}

	public boolean setTime(int t) {
		if (t < 0)
			return false;
		time = t;
		return true;
	}

	public void setUser(BaseActor user) {
		this.user = user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMessage(String msg) {
		message = msg;
	}

	public void setPriority(Priority p) {
		priority = p;
	}

	public final BaseActor getUser() {
		return user;
	}

	public final int getTime() {
		return time;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public final Priority getPriority() {
		return priority;
	}

	public String toString() {
		return name + "@" + time + "_" + priority + " "
				+ (user == null ? "null" : user.getAttributeNow().getAgi())
				+ "\n";
	}

	public void act() {

	}

}
