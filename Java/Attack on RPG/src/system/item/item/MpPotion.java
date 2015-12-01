package system.item.item;

import system.AttributeManage;
import system.item.Item;

public class MpPotion extends Item {

	public MpPotion() {
		 super("回魔藥", "500 C.C.");
		numeral = new AttributeManage(0, 20, 0, 0, 0);
	}

}
