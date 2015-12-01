package system.item.item;

import system.AttributeManage;
import system.item.Equipment;

public class DoranShield extends Equipment{
	
	public DoranShield() {
		super("多藍盾", ItemType.SHIELD, "測試用的盾牌", new AttributeManage(10, 0, 0, 0, 0));
	}
}
