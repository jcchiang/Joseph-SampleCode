package gui;

import gui.io.BGMFileLoader;
import gui.io.CharacterLoader;
import gui.io.MapFileLoader;
import gui.io.SpriteSheetLoader;
import gui.option.OptionPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.*;

import saveandload.LoadFile;
import system.AttributeManage;
import system.actor.BaseActor;
import system.actor.MainHero;
import system.arena.BaseArena;
import system.item.BaseItemManage;
import system.map.MapManage;
import system.map.map.CarSimulation;

/**
 * Main method, the frame of GUI
 * Last Update: Modify map panel into two panels. at 2013/06/22 17:33
 * @author B98505005
 *
 */
public class Game extends JPanel implements Runnable{

	private MainFrame fatherFrame;
	private JPanel topPanel;
	/**
	 * Version 1.0.0
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1024, HEIGHT = 800;
	//private static int WIDTH = 800, HEIGHT = 600;
	public static final int SPRITEWIDTH = 32, SPRITEHEIGHT = 32;
	//Background(BG) and Image related instances
	private Screen currentScreen;
	private BufferedImage bgImage;
	private BufferedImage bgImage_lv2;//
	private int[] pixels;
	private int[] pixels_lv2;//
	
	private MainHero hero;
	private ActorKeyListener AKListen;
	private OptionPanel options;
	private DialogPanel dialog;
	private BattlePanel battle;
	
	private BaseArena testArena;
	
	public Game(MainFrame father, OptionPanel options, DialogPanel dialog, BattlePanel battle) {
		fatherFrame = father;
		initialMapDrawer();
		
		hero = new MainHero(0, "Player");
		//hero = new MainHero(new LoadFile().getHeroFile());
		currentScreen = new Screen(WIDTH, HEIGHT, hero);
		//currentScreen = new Screen(WIDTH, HEIGHT, hero, new LoadFile().loadedMapName);
		AKListen = new ActorKeyListener(currentScreen, hero);
		addKeyListener(AKListen);
		
		this.options = options;
		this.dialog = dialog;
		this.battle = battle;
		dialog.setMainHero(hero);
		options.setMainHero(hero);
		
		currentScreen.setDialogPanel(this.dialog);
		currentScreen.setOptionPanel(this.options);
		currentScreen.setBattlePanel(this.battle);
		
		BaseActor monster[] = new BaseActor[1];
		monster[0] = new BaseActor(1,"A");
		monster[0].setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
		monster[0].setAttributeNow(new AttributeManage(50, 80, 20, 20, 20));
		
		//monster[1] = new BaseActor(1,"B");
		//monster[1].setAttributeMax(new AttributeManage(100, 100, 20, 20, 20));
		//monster[1].setAttributeNow(new AttributeManage(50, 80, 20, 20, 20));
		testArena = new BaseArena("TEst", 1000, 1.0);
		testArena.setMainHero(hero);
		testArena.setMonster(monster);
	}
	
	public Game(MainFrame father, OptionPanel options, DialogPanel dialog, LoadFile file) {
		/*
		fatherFrame = father;
		initialMapDrawer();
		hero = new MainHero(file.getHeroFile());
		
		currentScreen = new Screen(WIDTH, HEIGHT, hero);
		AKListen = new ActorKeyListener(currentScreen, hero);
		addKeyListener(AKListen);
		
		this.options = options;
		this.dialog = dialog;
		dialog.setMainHero(hero);
		options.setMainHero(hero);
		
		currentScreen.setDialogPanel(this.dialog);
		currentScreen.setOptionPanel(this.options);
		*/
	}
	
	private void initialMapDrawer(){
		
		bgImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);//
		pixels = ((DataBufferInt) bgImage.getRaster().getDataBuffer()).getData();//
		bgImage_lv2 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);//
		pixels_lv2 = ((DataBufferInt) bgImage_lv2.getRaster().getDataBuffer()).getData();//
		
		topPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				g.drawImage(bgImage_lv2, 0, 0, null);
			}
		};
		topPanel.setBounds(0, 0, WIDTH, HEIGHT);
		topPanel.setOpaque(false);
		fatherFrame.add(topPanel);
	}

	/**
	 * Load Static classes
	 */
	public static void init() {
		//Load sprite image, must before all map's related instance
		SpriteSheetLoader.initial();
		//maps related
		MapFileLoader.loadMap();
		//character related
		CharacterLoader.initial();
		//dialog related, must after map and character done
		//DialogLoader.initial();
		//Music related
		BGMFileLoader.initial();
		
		//Map Manager. Must execute after all loader be initialized.
		MapManage.initial();
		
		BaseItemManage.initial();
		
		
	}
	
	/**
	 * Update data
	 */
	public void tick(){
		if(currentScreen.getCurrentStatus() != Screen.BATTLE){
			currentScreen.cleanPixels(pixels);
			currentScreen.cleanPixels(pixels_lv2);
			
			if(currentScreen.isConstantlyRedraw){
				if(currentScreen.getCurrentMap().getName().equals("CarSimulation")){
					CarSimulation A = (CarSimulation)(currentScreen.getCurrentMap());
					A.change();
					//screen.reloadThisMap();
					currentScreen.reloadThisMap();
				}
				else{
					currentScreen.isConstantlyRedraw = false;
				}
			}
			
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
		else{
			if(currentScreen.getBattlePanel().getStatus() == BattlePanel.BEGIN){
				//Initialize Battle Scene
				battle.startBattle();
			}
			else if(currentScreen.getBattlePanel().getStatus() == BattlePanel.IN_PROGRESS){
				//Ticking and Checking
				currentScreen.getBattlePanel().checkEvents();
			}
			else if(currentScreen.getBattlePanel().getStatus() == BattlePanel.WAIT_FOR_ACTION){
				//Asked User to choose Actions
				currentScreen.getBattlePanel().takeAction();
			}
			else if(currentScreen.getBattlePanel().getStatus() == BattlePanel.RESULT){
				//Showing Battle Result
				if(!currentScreen.getBattlePanel().isShowingResult)
					currentScreen.getBattlePanel().showResult();
			}
		}
	}

	/**
	 * Update graphics data that would showed to user
	 */
	@Override
	public void paint(Graphics g){
		g.drawImage(bgImage, 0, 0, null);
		requestFocus();
	}
	
	public void render(){
		fatherFrame.repaint();
		if(currentScreen.getCurrentStatus() != Screen.BATTLE){	
			this.repaint();
			topPanel.repaint();
			options.repaint();
		}
		else{
			//this.repaint();
			battle.repaint();
		}
	}
	
	/**
	 * Run the game by consecutively repaint the canvas
	 */
	public void run() {
		tick();
		render();
	}
}
