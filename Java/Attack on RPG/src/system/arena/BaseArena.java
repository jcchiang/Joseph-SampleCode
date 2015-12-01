package system.arena;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import system.actor.*;
import system.AttributeManage;
import system.arena.Event.Priority;

/**
 * @author B97502052
 * 
 */
public class BaseArena {

	private String name;
	private int clockTime;
	private int attackLimit;
	private double attackMul;
	private MainHero hero;
	private ArrayList<BaseActor> monsterList;
	private PriorityQueue<Event> queue;
	private static final int QUEUE_SIZE = 30;

	public BaseArena() {
		this("BaseArenaName", 100, 1.0);
	}

	public BaseArena(String name, int attackLimit, double attackMul) {
		this.name = name;
		this.attackLimit = attackLimit;
		this.attackMul = attackMul;
		clockTime = 0;
		Comparator<Event> comparator = new EventComparator();
		queue = new PriorityQueue<Event>(QUEUE_SIZE, comparator);
	}

	/**
	 * Because of leaking some information, this function should be called
	 * before use arena's method.
	 * 
	 * @param hero
	 *            A MainHero
	 * @param monsters
	 *            Monsters in this arena
	 */
	public void initBeforeUse(MainHero hero, BaseActor[] monsters) {
		this.hero = hero;
		setMonster(monsters);
	}

	public boolean addEvent(Event e) {
		return queue.add(e);
	}

	public Event peekEvent() {
		return queue.peek();
	}

	public Event pollEvent() {
		return queue.poll();
	}

	/**
	 * @return True if player can attack at this time.
	 */
	public boolean fight() {
		clockPlusOne();
		return hero.setForAttack(attackMul, attackLimit);
	}

	/**
	 * ClockTime plus one and check if any character can attack.
	 */
	protected void clockPlusOne() {
		++clockTime;
		for (BaseActor b : monsterList)
			if (b.isDead() == false && b.setForAttack(attackMul, attackLimit))
				b.decide(this);
	}

	public final int getClockTime() {
		return clockTime;
	}

	/**
	 * @return True if game over, else false.
	 */
	public boolean gameOver() {
		return (hero.isDead() || monsterAllDied());
	}

	private boolean monsterAllDied() {
		for (BaseActor b : monsterList)
			if (b.isDead() == false)
				return false;
		return true;
	}

	public void setMainHero(MainHero hero) {
		this.hero = hero;
	}

	public void setMonster(BaseActor[] monsters) {
		monsterList = new ArrayList<BaseActor>();
		for (BaseActor b : monsters)
			monsterList.add(b);
	}

	/**
	 * @return An BaseActor array of monsters
	 */
	public BaseActor[] getAllMonsters() {
		BaseActor[] ret = new BaseActor[0];
		return monsterList.toArray(ret);
	}

	public MainHero getMainHero() {
		return hero;
	}

	public String getName() {
		return name;
	}

	/**
	 * Compare time(small first) -> priority(small first) -> agi(big first)
	 * 
	 * @author B97502052
	 * 
	 */
	class EventComparator implements Comparator<Event> {
		public int compare(Event x, Event y) {
			int ret = comp(x.getTime(), y.getTime());
			if (ret != 0)
				return ret;
			ret = comp(x.getPriority().ordinal(), y.getPriority().ordinal());
			if (ret != 0)
				return ret;
			if (x.getUser() != null && y.getUser() != null) {
				ret = comp(x.getUser().getAttributeNow().getAgi(), y.getUser()
						.getAttributeNow().getAgi());
				return (ret * -1);
			}
			return 0;
		}

		private int comp(int x, int y) {
			if (x < y)
				return -1;
			else if (x > y)
				return 1;
			else
				return 0;
		}
	}

	/**
	 * Testing the utility of priority queue
	 * 
	 * @param args
	 *            No use
	 */
	public static void main(String[] args) {
		BaseArena b = new BaseArena("Priority Test", 100, 1.0);
		BaseActor a = new BaseActor();
		a.setAttributeMax(new AttributeManage(0, 0, 0, 0, 100));
		a.setAttributeNow(new AttributeManage(0, 0, 0, 0, 100));
		Event e = new Event("3rd", "", 2, Priority.ATTACK_SKILL, a);
		b.addEvent(e);
		e = new Event("1st", "", 1, Priority.ARENA_EFFECT, null);
		b.addEvent(e);
		a = new BaseActor();
		a.setAttributeMax(new AttributeManage(0, 0, 0, 0, 20));
		a.setAttributeNow(new AttributeManage(0, 0, 0, 0, 20));
		e = new Event("4th", "", 2, Priority.ATTACK_SKILL, a);
		b.addEvent(e);
		e = new Event("2nd", "", 1, Priority.DOT, null);
		b.addEvent(e);
		e = b.pollEvent();
		while (e != null) {
			System.out.println(e);
			e = b.pollEvent();
		}
	}

}
