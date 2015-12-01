package gui;

import gui.io.ImageLoader;
import gui.option.OptionPanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class MainFrame extends JFrame implements Runnable{
	/**
	 * Version 1.0.0
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1024, HEIGHT = 800;
	private final int TICKTIME = 30;
	private boolean isRunning = false;
	
	private OptionPanel options;
	private DialogPanel dialog;
	private BattlePanel battle;
	private Game RPG;
	
	public MainFrame(){
		setTitle("Shingeki no RPG");
		setLocation(10, 10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setBackground(Color.BLACK);
		setLayout(null);
		getContentPane().setBackground(Color.BLACK);
		
		options = new OptionPanel(WIDTH, HEIGHT-32);
		options.setBounds(0 , 0, WIDTH, HEIGHT-32);
		add(options);
		
		dialog = new DialogPanel(620, 200);
		dialog.setBounds(WIDTH/2-300, HEIGHT-250, 620, 200);
		add(dialog);
		
		battle = new BattlePanel(WIDTH, HEIGHT);
		battle.setBounds(0, 0, WIDTH, HEIGHT);
		add(battle);
		
		Game.init();
		RPG = new Game(this, options, dialog, battle);
		RPG.setBounds(0, 0, WIDTH, HEIGHT);
		add(RPG);
		
	}
	
	public static void init(){
		ImageLoader.initial();
	}
	
	public static void main(String[] args){
		init();
		MainFrame frame = new MainFrame();
		
		frame.pack();
		frame.setVisible(true);
		frame.start();
	}

	public void start(){
		isRunning = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while(isRunning){
			RPG.run();
			try {
				Thread.sleep(TICKTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
