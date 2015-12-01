package gui;

import gui.io.ImageLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import system.DialogDetail;
import system.actor.BaseActor;
import system.actor.MainHero;

/**
 * Used to show user of a dialog that contains who said what.
 * Last Update: Create. at 2013/06/17 18:49
 * @author B98505005
 *
 */
public class DialogPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField titleField, nameField;
	private JTextArea textArea;
	private BufferedImage bg, currentFace;
	private String[] realContent;
	private int showBegin = 0;
	
	public final static int DONE = -1, THIS_ONE_DONE = -2;
	private final int PHOTOWIDTH = 96, PHOTOHEIGHT = 96;
	private final int FACE_AND_NAME_OFFSET_Y = 10, FACE_AND_TEXT_OFFSET_X = 15;
	private final int NAME_WIDTH = PHOTOWIDTH, NAME_HEIGHT1 = 18, NAME_HEIGHT2 = 30;
	private final int TEXT_WIDTH, TEXT_HEIGHT;
	private final int BORDER_OFFSET = 15;
	
	private MainHero hero;
	private int conversationCounter;
	public boolean isPause = false;
	
	public DialogPanel(int width, int height){
		setLayout(null);
		setOpaque(false);
		setVisible(false);
		
		TEXT_WIDTH = width - (2 * BORDER_OFFSET + PHOTOWIDTH + FACE_AND_TEXT_OFFSET_X);
		TEXT_HEIGHT = height - 3 * BORDER_OFFSET;

		titleField = new JTextField();
		titleField.setOpaque(false);
		titleField.setBorder(null);
		titleField.setForeground(Color.WHITE);
		titleField.setHorizontalAlignment(JTextField.CENTER);
		titleField.setSize(width/2, 30);
		titleField.setBounds(BORDER_OFFSET, BORDER_OFFSET + PHOTOHEIGHT + FACE_AND_NAME_OFFSET_Y, 
							NAME_WIDTH, NAME_HEIGHT1);
		titleField.setEditable(false);
		titleField.setFont(new Font("微軟正黑體", Font.BOLD, 13));
		titleField.setText("南宋愛國詞人");
		add(titleField);
		
		nameField = new JTextField();
		nameField.setOpaque(false);
		nameField.setBorder(null);
		nameField.setForeground(Color.WHITE);
		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.setSize(width/2, 30);
		nameField.setBounds(BORDER_OFFSET, BORDER_OFFSET + PHOTOHEIGHT + FACE_AND_NAME_OFFSET_Y + NAME_HEIGHT1, 
							NAME_WIDTH, NAME_HEIGHT2);
		nameField.setEditable(false);
		nameField.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		nameField.setText("文天祥");
		add(nameField);
		
		textArea = new JTextArea();
		textArea.setForeground(Color.WHITE);
		textArea.setOpaque(false);
		textArea.setSize(width, height);
		textArea.setBounds(BORDER_OFFSET + PHOTOWIDTH + FACE_AND_TEXT_OFFSET_X, BORDER_OFFSET,
						   TEXT_WIDTH, TEXT_HEIGHT);
		textArea.setEditable(false);
		textArea.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		
		add(textArea);
		
		try {
			currentFace = ImageLoader.getImage("face0");
			bg = ImageLoader.getImage("dialogBG");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/*
	public static void main(String[] args){
		ImageLoader.initial();
		JFrame A = new JFrame();
		DialogPanel B = new DialogPanel(800,200);
		A.setSize(800,230);
		A.setResizable(false);
		A.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		A.setVisible(true);
		A.add(B);
	}
	*/
	
	public void beginConversation(DialogDetail toShow){
		//System.out.println("BEGIN!!");
		showBegin = 0;
		conversationCounter = 0;		
		keepConversation(toShow);
	}
	
	public void keepConversation(DialogDetail toShow){
		if(toShow.getSpeaker(conversationCounter).getID() == new MainHero().getID()){
			showDialog(hero, toShow.getContent(conversationCounter));
			try {
				currentFace = ImageLoader.getImage("face0");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			showDialog(toShow.getSpeaker(conversationCounter), toShow.getContent(conversationCounter));
			try {
				currentFace = ImageLoader.getImage("face"+toShow.getSpeaker(conversationCounter).getID());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		if(toShow.getSpeaker(conversationCounter).getID() == new MainHero().getID()){
			showDialog(hero, toShow.getHeroContent(hero, conversationCounter));
			try {
				currentFace = ImageLoader.getImage("face0");
			} catch (IOException e) {
				e.printStackTrace();
			};
		}
		else{
			showDialog(toShow.getSpeaker(conversationCounter), toShow.getContent(conversationCounter));
			try {
				currentFace = ImageLoader.getImage("face"+toShow.getSpeaker(conversationCounter).getID());
			} catch (IOException e) {
				e.printStackTrace();
			};
		}
		*/
		conversationCounter++;
		if(conversationCounter == toShow.getDetailSize()){
			showBegin = DONE;
		}
	}
	
	public void showDialog(BaseActor talker, String said){
		titleField.setText(null);
		nameField.setText(talker.getName());
		realContent = said.split("<br>");
		showBegin = 0;
		keepShowingDialog();
		setVisible(true);
	}

	public void keepShowingDialog(){
		StringBuffer thisDialog = new StringBuffer();
		boolean isEnd = false;
		textArea.setText(null);
		
		for(int i=0; i<5; i++){
			if(realContent.length == 0){
				isEnd = true;
				break;
			}
			thisDialog.append(realContent[showBegin+i]+"\n");
			if(showBegin + i == realContent.length - 1){
				isEnd = true;
				break;
			}
		}
		textArea.setText(thisDialog.toString());
		if(!isEnd){
			showBegin += 5;
		}
		else{
			showBegin = THIS_ONE_DONE;
			isPause = true;
		}
	}
	
	public void hideDialog(){
		showBegin = DONE;
		setVisible(false);
	}
	
	public int getBeginLines(){
		return showBegin;
	}
	
	public void setMainHero(MainHero hero){
		this.hero = hero;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);
        g.drawImage(currentFace, BORDER_OFFSET, BORDER_OFFSET, null); 
    }
	
	/*
	public void showDialog(int ID){
		if(ID == 0){
			content = new String(
					"天地有正氣，雜然賦流形。下則為河嶽，上則為日星。\n" +
					"於人曰浩然，沛乎塞蒼冥。皇路當清夷，含和吐明庭。\n" +
					"時窮節乃見，一一垂丹青。在齊太史簡，在晉董狐筆。\n" +
					"在秦張良椎，在漢蘇武節。為嚴將軍頭，為嵇侍中血。\n" +
					"為張睢陽齒，為顏常山舌。或為遼東帽，清操厲冰雪。\n" +
					"或為出師表，鬼神泣壯烈。或為渡江楫，慷慨吞胡羯。\n" +
					"或為擊賊笏，逆豎頭破裂。是氣所磅礡，凜烈萬古存。\n" +
					"當其貫日月，生死安足論。地維賴以立，天柱賴以尊。\n" +
					"三綱實繫命，道義為之根。嗟予遘陽九，隸也實不力。\n" +
					"楚囚纓其冠，傳車送窮北。鼎鑊甘如飴，求之不可得。\n" +
					"陰房闃鬼火，春院閟天黑。牛驥同一皂，雞棲鳳凰食。\n" +
					"一朝蒙霧露，分作溝中瘠。如此再寒暑，百沴自辟易。\n" +
					"哀哉沮洳場，為我安樂國。豈有他繆巧，陰陽不能賊。\n" +
					"顧此耿耿在，仰視浮雲白。悠悠我心悲，蒼天曷有極！\n" +
					"哲人日已遠，典型在夙昔。風簷展書讀，古道照顏色。");
			realContent = content.split("\n");
		}
		showBegin = 0;
		keepShowingDialog();
		setVisible(true);
	}
	*/
}
