package saveandload;

public class HeroFile {
	public int ID;
	public String name;
	public int x, y;
	public int characterPose;
	public int level = 1, exp = 20;
	public int hp = 160, mp = 180, str = 10, agi = 10, mstr = 10;
	
	public HeroFile(String content){
		String[] attributes = content.split(",");
		if(attributes.length == 12){
			ID = Integer.valueOf(attributes[0]);
			name = attributes[1];
			x = Integer.valueOf(attributes[2]);
			y = Integer.valueOf(attributes[3]);
			characterPose = Integer.valueOf(attributes[4]);
			level = Integer.valueOf(attributes[5]);
			exp = Integer.valueOf(attributes[6]);
			hp = Integer.valueOf(attributes[7]);
			mp = Integer.valueOf(attributes[8]);
			str = Integer.valueOf(attributes[9]);
			agi = Integer.valueOf(attributes[10]);
			mstr = Integer.valueOf(attributes[11]);
		}
	}
}
