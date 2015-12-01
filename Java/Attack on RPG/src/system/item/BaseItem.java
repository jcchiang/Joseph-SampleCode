package system.item;

import system.actor.BaseActor;

/**
 * A basic type of item which needs more definition should be inherited.
 * 
 * @author B97502052
 * 
 */
public abstract class BaseItem {
	private String name;
	/**
	 * Description of this item will be showed. Example: its background or its
	 * effect
	 */
	private String description;
	private ItemType type;

	/**
	 * Define the type of item
	 * 
	 * @author B97502052
	 * 
	 */
	public enum ItemType {
		DEFAULTITEM, ITEM, WEAPON, SHIELD, HELMET, ARMOR, GLOVE, SHOE, ACCESSORY;
		public static final int size = ItemType.values().length;
	}

	public BaseItem() {
		this("DefaultBaseItem", ItemType.DEFAULTITEM,
				"DefaultBaseItemDescription");
	}

	public BaseItem(String name, ItemType type, String description) {
		this.name = name;
		this.type = type;
		this.description = description;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final ItemType getType() {
		return type;
	}

	/**
	 * The effect of item needs be defined
	 */
	public abstract void act();

	/**
	 * Stop the effect of non-consumable item. Example: equipment
	 */
	public abstract void stop();

	public void setTarget(BaseActor target) {

	}
}
