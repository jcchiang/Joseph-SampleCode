package system.item.item;

import system.item.Item;
import system.AttributeManage;

public class HpPotion2 extends Item {

	public HpPotion2() {
		super("高級創傷藥", "沒事多喝水");
		numeral = new AttributeManage(50, 0, 0, 0, 0);
	}

}
