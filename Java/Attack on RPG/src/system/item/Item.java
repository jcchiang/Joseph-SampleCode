package system.item;

import system.AttributeManage;
import system.actor.*;

/**
 * Consumable item like. Example: drug, food.
 * 
 * @author B97502052
 * 
 */
public abstract class Item extends BaseItem {
	protected BaseActor target;
	protected AttributeManage numeral;

	public Item() {
		super("DefaultItem", BaseItem.ItemType.ITEM, "DefaultItemDescription");
	}

	public Item(String name, String description) {
		super(name, BaseItem.ItemType.ITEM, description);
	}

	public void setTarget(BaseActor target) {
		this.target = target;
	}

	@Override
	public void act() {
		target.getAttributeNow().add(numeral, target.getAttributeMax());
	}

	@Override
	public void stop() {
	}
}
