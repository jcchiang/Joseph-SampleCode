package system.actor;

//import saveandload.HeroFile;
import system.AttributeManage;
import system.arena.skill.*;
import system.item.*;
import system.item.BaseItem.ItemType;

/**
 * Main Character, controlled by user. 
 * Last Update: Add AttributeManage at 2013/06/23 18:58 
 * 
 * @author B98505005, B97502052
 * 
 */
public class MainHero extends BaseActor {
	
	private BackPack pack;
	private Equipment[] equipped;
	
	//public BaseItem[] myItems;
	//public int[] counts;
	
	public MainHero() {
		super();
		setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
		setAttributeNow(new AttributeManage(50, 80, 20, 20, 20));
		pack = new BackPack();
		equipped = new Equipment[7];
		initSkill();
	}

	public MainHero(int ID, String name) {
		super(ID, name);
		setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
		setAttributeNow(new AttributeManage(50, 80, 20, 20, 20));
		pack = new BackPack();
		equipped = new Equipment[7];
		initSkill();
		
		pack.addItem(0);
		pack.addItem(0);
		pack.addItem(0);
		pack.addItem(3);
		pack.addItem(3);
		pack.addItem(2);
		pack.addItem(4);
		pack.addItem(5);
		pack.addItem(6);
		pack.addItem(7);
		
		putOnEquip(1);

	}
	
	public BackPack getBackPack(){
		return pack;
	}
	
	private void initSkill() {
		addSkill(new StrAttackSkill("認真揍一拳", "我本來不想用這招的...", 20.0));
		addSkill(new MstrAttackSkill("耍冷", "救救就可可以嗎", 5.0));
		addSkill(new MstrAttackSkill("元氣彈", "地球上所有的 NPC 啊\n請分我一點攻擊力吧!!!", 40.0));
	}

	public void hasItem(BaseItem item){
		
	}
	
	public void useItem(int index){
		pack.useItem(index);
	} 
	
	public Equipment[] getEquipped(){
		return equipped;
	}
	
	public void putOnEquip(int index){
		if(index < pack.getCurrentEquipNumbers()){
			Equipment thisEquip = (Equipment) pack.getEquipInBag(index);
			ItemType type = thisEquip.getType();
			if(type == ItemType.WEAPON){
				switchEquip(thisEquip, index, 0);
			}
			else if(type == ItemType.HELMET){
				switchEquip(thisEquip, index, 1);
			}
			else if(type == ItemType.ACCESSORY){
				switchEquip(thisEquip, index, 2);
			}
			else if(type == ItemType.GLOVE){
				switchEquip(thisEquip, index, 3);
			}
			else if(type == ItemType.ARMOR){
				switchEquip(thisEquip, index, 4);
			}
			else if(type == ItemType.SHIELD){
				switchEquip(thisEquip, index, 5);
			}
			else if(type == ItemType.SHOE){
				switchEquip(thisEquip, index, 6);
			}
		}
	}
	
	public void putOffEquip(int position){
		if(equipped[position] != null){
			Equipment thisEquip = equipped[position];
			equipped[position] = null;
			pack.addItem(thisEquip);
		}
	}
	
	private void switchEquip(Equipment thisEquip, int itemIdx, int position){
		if(equipped[position] != null){
			Equipment oldEquip = equipped[position];
			pack.addItem(oldEquip);
		}
		equipped[position] = thisEquip;
		pack.removeEquip(itemIdx);
	}
	
	//This not yet applied to orininal project
	/*public MainHero(HeroFile oldHero){
		super(oldHero);
	}*/
}
