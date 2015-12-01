package system.item;

import java.util.ArrayList;

import system.actor.MainHero;

/**
 * @author B97502052
 * 
 */
public class BaseItemManage {
	private static boolean isInitial;
	private static String[] baseItemName = { 
		"HpPotion", "HpPotion2", "MpPotion", "MpPotion2", "DoranHelmet", 
		"DoranShield", "DoranBlade", "LinkShield" };
	private static ArrayList<BaseItem> baseItemList;

	public static void initial() {
		isInitial = true;
		baseItemList = new ArrayList<BaseItem>();
		BaseItem A;

		for (int i = 0; i < baseItemName.length; i++) {
			try {
				String className = new String("system.item.item."
						+ baseItemName[i]);
				A = (BaseItem) Class.forName(className).getConstructor()
						.newInstance();
				baseItemList.add(A);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param name
	 *            The BaseItem name to search
	 * @return A BaseItem if exists, else return null
	 */
	public static BaseItem getBaseItem(String name) {
		if (isInitial == false)
			initial();
		for (BaseItem b : baseItemList)
			if (b.getName().equals(name))
				return b;
		return null;
	}

	/**
	 * @param index
	 *            The index of BaseItem to get
	 * @return A BaseItem if index is legal, else return null
	 */
	public static BaseItem getBaseItem(int index) {
		if (index < 0 || index >= baseItemList.size())
			return null;
		return baseItemList.get(index);
	}

	/**
	 * @param index
	 *            The index of BaseItem's name to get
	 * @return A BaseItem's name if index is legal, else return null
	 */
	public static String getBaseItemName(int index) {
		BaseItem ret = getBaseItem(index);
		if (ret == null)
			return null;
		return ret.getName();
	}

	/**
	 * @param name
	 *            The name of BaseItem's index to get
	 * @return A BaseItem's index if name is found, else return -1
	 */
	public static int getBaseItemIndex(String name) {
		BaseItem ret = getBaseItem(name);
		if (ret == null)
			return -1;
		return baseItemList.indexOf(ret);
	}

	/**
	 * @param b
	 *            The BaseItem to search its index
	 * @return A BaseItem's index if it is found, else return -1
	 */
	public static int getBaseItemIndex(BaseItem b) {
		return baseItemList.indexOf(b);
	}
	
	public static int getItemListSize(){
		return baseItemName.length;
	}

	public static void main(String[] args) {
		MainHero mm = new MainHero();
		BaseItem hp = getBaseItem("創傷藥");
		System.out.print(mm.getAttributeMax());
		System.out.print(mm.getAttributeNow());
		System.out.println(mm.getAttributeNow() == mm.getAttributeMax());
		System.out.println();
		hp.setTarget(mm);
		hp.act();
		System.out.print(mm.getAttributeNow());
	}
}
