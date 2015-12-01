package system.item.item;

import system.AttributeManage;
import system.item.Item;

public class MpPotion2 extends Item {

	public MpPotion2() {
		super("高級回魔藥", "1200 C.C.");
		numeral = new AttributeManage(0, 50, 0, 0, 0);
	}

}
