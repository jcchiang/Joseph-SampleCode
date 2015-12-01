package system.item.item;

import system.item.Item;
import system.AttributeManage;

public class HpPotion extends Item {

	public HpPotion() {
		super("創傷藥", "多喝水沒事");
		numeral = new AttributeManage(20, 0, 0, 0, 0);
	}

}
