package system.item;

import java.util.ArrayList;

import system.item.BaseItem.ItemType;

public class BackPack {
	
	private ArrayList<BaseItem> itemList, equipList;
	private ArrayList<Integer> itemCount;
	private int itemSize, equipSize;
	
	public BackPack(){
		itemList = new ArrayList<BaseItem>();
		itemCount= new ArrayList<Integer>();
		itemSize = 48;
		equipList = new ArrayList<BaseItem>();
		equipSize = 48;
	}
	
	public void hasItem(BaseItem aItem){
		
	}
	
	public void addItem(int ID){
		BaseItem newItem = BaseItemManage.getBaseItem(ID);
		addItem(newItem);
	}
	
	public void addItem(BaseItem newItem){
		if(newItem.getType() == ItemType.DEFAULTITEM || newItem.getType() == ItemType.ITEM){
			if(itemList.size() < itemSize){
				if(itemList.indexOf(newItem) >= 0){
					itemCount.set(itemList.indexOf(newItem), itemCount.get(itemList.indexOf(newItem))+ 1);
				}
				else{
					itemList.add(newItem);
					itemCount.add(1);
				}
			}
		}
		else{
			if(equipList.size() < equipSize){
				equipList.add(newItem);
			}
		}
	}
	
	public void useItem(int index){
		if(itemCount.get(index) <= 1){
			itemList.remove(index);
			itemCount.remove(index);
		}
		else{
			itemCount.set(index, itemCount.get(index) - 1);
		}
	}
	
	public void useItem(BaseItem theItem){
		if(theItem.getType() == ItemType.DEFAULTITEM || theItem.getType() == ItemType.ITEM){
			if(itemList.indexOf(theItem) < 0)
				return;
			else
				useItem(itemList.indexOf(theItem));
		}
	}
	
	public void removeEquip(int index){
		equipList.remove(index);
	}
	
	public BaseItem getItemInBag(int num){
		return itemList.get(num);
	}
	
	public BaseItem getEquipInBag(int num){
		return equipList.get(num);
	}
	
	public int getItemNumbersInBag(int num){
		return itemCount.get(num);
	}
	
	public int getCurrentItemNumbers(){
		return itemList.size();
	}
	
	
	public int getCurrentEquipNumbers(){
		return equipList.size();
	}
}
