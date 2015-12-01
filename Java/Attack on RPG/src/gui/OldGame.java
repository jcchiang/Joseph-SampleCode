package gui;

import gui.io.CharacterLoader;
import gui.io.DialogLoader;
import gui.io.ImageLoader;
import gui.io.MapFileLoader;
import gui.io.SpriteSheetLoader;
import gui.option.OptionPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.*;

import system.actor.MainHero;
import system.map.MapManage;

/**
 * Main method, the frame of GUI
 * Last Update: Modify map panel into two panels. at 2013/06/22 17:33
 * @author B98505005
 *
 */
public class OldGame extends JPanel implements Runnable{

	private static JFrame gameFrame;
	private static JPanel topPanel;
	/**
	 * Version 1.0.0
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1024, HEIGHT = 800;
	//private static int WIDTH = 800, HEIGHT = 600;
	private boolean isRunning = false;
	public static final int SPRITEWIDTH = 32, SPRITEHEIGHT = 32;
	//Background(BG) and Image related instances
	private final int TICKTIME = 30;
	private Screen currentScreen;
	private static BufferedImage bgImage;
	private static BufferedImage bgImage_lv2;//
	private static int[] pixels;
	private static int[] pixels_lv2;//
	
	private MainHero hero;
	private ActorKeyListener AKListen;
	private static OptionPanel options;
	private static DialogPanel dialog;
	
		
	public OldGame() {
		//bgImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) bgImage.getRaster().getDataBuffer()).getData();
		hero = new MainHero(0, "Player");
		currentScreen = new Screen(WIDTH, HEIGHT, hero);
		AKListen = new ActorKeyListener(currentScreen, hero);
		addKeyListener(AKListen);
		
		dialog.setMainHero(hero);
		
		currentScreen.setDialogPanel(dialog);
		currentScreen.setOptionPanel(options);
		
	}

	/**
	 * Let this Runnable thread start to run
	 */
	public void start() {
		isRunning = true;
		new Thread(this).start();
	}

	/**
	 * Load Static classes
	 */
	public static void init() {
		//Load In image, must be the first one
		ImageLoader.initial();
		//Load sprite image, must before all map's related instance
		SpriteSheetLoader.initial();
		//maps related
		MapFileLoader.loadMap();
		//character related
		CharacterLoader.initial();
		//dialog related, must after map and character done
		DialogLoader.initial();
		
		//Map Manager. Must execute after all loader be initialized.
		MapManage.initial();
		
		bgImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);//
		pixels = ((DataBufferInt) bgImage.getRaster().getDataBuffer()).getData();//
		bgImage_lv2 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);//
		pixels_lv2 = ((DataBufferInt) bgImage_lv2.getRaster().getDataBuffer()).getData();//
		
	}
	
	/**
	 * Update data
	 */
	public void tick(){
		currentScreen.cleanPixels(pixels);
		currentScreen.cleanPixels(pixels_lv2);
		
		//Draw Map that behind the actors
		pixels = currentScreen.drawLowerMap(pixels);
				
		//Draw Main Character
		pixels = currentScreen.drawActor(pixels, hero);

		//Draw Map that in front of the actors
		//pixels = currentScreen.drawTopMap(pixels);
		pixels_lv2 = currentScreen.drawTopMap(pixels_lv2);
		
		//Draw NPC
		pixels = currentScreen.drawNPC(pixels);
		
		//Update Main Character position
		hero.update();
		currentScreen.checkChangeMap();
	}

	/**
	 * Update graphics data that would showed to user
	 */
	@Override
	public void paint(Graphics g){
		g.drawImage(bgImage, 0, 0, null);
		//test1.getGraphics().drawImage(bgImage_lv2, 0, 0, null);
		//test1.repaint();
		requestFocus();
	}
	public void render(){
		gameFrame.repaint();
		//this.repaint();
	}
	
	
	/**
	 * Run the game by consecutively repaint the canvas
	 */
	public void run() {
		while (isRunning) {
			tick();
			render();
			try {
				Thread.sleep(TICKTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Main method
	 * @param args should not be used
	 */
	public static void main(String[] args) {
		init();
		
		gameFrame = new JFrame();
		gameFrame.setTitle("Shingeki no RPG");
		gameFrame.setLocation(10, 10);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(WIDTH, HEIGHT);
		gameFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		gameFrame.setResizable(false);
		gameFrame.setBackground(Color.BLACK);
		gameFrame.setLayout(null);
		gameFrame.getContentPane().setBackground(Color.BLACK);
		
		options = new OptionPanel(WIDTH, HEIGHT-32);
		options.setBounds(0 , 0, WIDTH, HEIGHT-32);
		gameFrame.add(options);
		
		dialog = new DialogPanel(620, 200);
		dialog.setBounds(WIDTH/2-300, HEIGHT-250, 620, 200);
		gameFrame.add(dialog);
		
		topPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				g.drawImage(bgImage_lv2, 0, 0, null);
			}
		};
		topPanel.setBounds(0,0,WIDTH, HEIGHT);
		topPanel.setOpaque(false);
		gameFrame.add(topPanel);
		
		
		OldGame RPG = new OldGame();
		RPG.setBounds(0, 0, WIDTH, HEIGHT);

		gameFrame.add(RPG);
		
		gameFrame.pack();
		gameFrame.setVisible(true);
		RPG.start();
		
	}
}
