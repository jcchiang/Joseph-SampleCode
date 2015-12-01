package system.item.item;

import system.AttributeManage;
import system.item.Equipment;

public class LinkShield extends Equipment{
	
	public LinkShield() {
		super("林克盾", ItemType.SHIELD, "林克用過的盾牌", new AttributeManage(0, 0, 20, 0, 0));
	}
}
