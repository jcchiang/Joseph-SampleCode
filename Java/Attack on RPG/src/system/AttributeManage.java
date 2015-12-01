package system;

/**
 * A class consists of HP, MP, STR, MSTR, AGI
 * 
 * @author B97502052
 * 
 */
public class AttributeManage {

	private String[] attributeStrArr = { "HP", "MP", "STR", "MSTR", "AGI" };
	private int[] attributeIntArr = new int[attributeStrArr.length];
	private static final int hpIndex = 0;
	private static final int mpIndex = 1;
	private static final int strIndex = 2;
	private static final int mstrIndex = 3;
	private static final int agiIndex = 4;

	public AttributeManage() {
	}

	public AttributeManage(int hp, int mp, int str, int mstr, int agi) {
		this.setHp(hp);
		this.setMp(mp);
		this.setStr(str);
		this.setMstr(mstr);
		this.setAgi(agi);
	}

	/**
	 * Add another AttributeManage's value into this one
	 * 
	 * @param another
	 *            An AttributeManage to be added
	 */
	public void add(AttributeManage another) {
		for (int i = 0; i < attributeStrArr.length; ++i)
			attributeIntArr[i] += another.attributeIntArr[i];
	}

	/**
	 * Add another AttributeManage's value into this one and the result values
	 * are in the range of max
	 * 
	 * @param another
	 *            An AttributeManage to be added
	 * @param max
	 *            The max value of each Attribute
	 */
	public void add(AttributeManage another, AttributeManage max) {
		for (int i = 0; i < attributeStrArr.length; ++i) {
			attributeIntArr[i] += another.attributeIntArr[i];
			if (attributeIntArr[i] > max.attributeIntArr[i])
				attributeIntArr[i] = max.attributeIntArr[i];
		}
	}

	/**
	 * Subtract another AttributeManage's value into this one and the result
	 * values should >= 0
	 * 
	 * @param another
	 *            An AttributeManage to be subtracted
	 */
	public void subtract(AttributeManage another) {
		for (int i = 0; i < attributeStrArr.length; ++i) {
			attributeIntArr[i] -= another.attributeIntArr[i];
			if (attributeIntArr[i] < 0)
				attributeIntArr[i] = 0;
		}
	}

	public int getHp() {
		return attributeIntArr[hpIndex];
	}

	public int getMp() {
		return attributeIntArr[mpIndex];
	}

	public int getStr() {
		return attributeIntArr[strIndex];
	}

	public int getMstr() {
		return attributeIntArr[mstrIndex];
	}

	public int getAgi() {
		return attributeIntArr[agiIndex];
	}

	public void setHp(int hp) {
		attributeIntArr[hpIndex] = hp;
	}

	public void setMp(int mp) {
		attributeIntArr[mpIndex] = mp;
	}

	public void setStr(int str) {
		attributeIntArr[strIndex] = str;
	}

	public void setMstr(int mstr) {
		attributeIntArr[mstrIndex] = mstr;
	}

	public void setAgi(int agi) {
		attributeIntArr[agiIndex] = agi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";
		for (int i = 0; i < attributeStrArr.length; ++i)
			ret = ret + attributeStrArr[i] + "\t"
					+ (attributeIntArr[i] >= 0 ? "+" : "") + attributeIntArr[i]
					+ "\n";
		return ret;
	}

	/**
	 * Testing the utility
	 * 
	 * @param args
	 *            No use
	 */
	/*
	public static void main(String[] args) {
		AttributeManage a = new AttributeManage();
		AttributeManage b = new AttributeManage();
		AttributeManage max = new AttributeManage(100, 100, 100, 100, 100);
		a.setHp(-1);
		a.setMp(1);
		b.setHp(100);
		b.setMp(100);
		System.out.print(a);
		System.out.print(b);
		a.add(b);
		System.out.print(a);
		a.add(b, max);
		System.out.print(a);
	}
	*/

}
