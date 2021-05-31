package com.beserrovsky.bad_engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/*TODO:
 * Implement: 
 * - camera logic
 * - collision
 * - map
 * - sound
 * - sprite
 * */

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable, KeyListener {
	
	//GAME SETTINGS:
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = WIDTH/16*9;
	public static final int SCALE = 3;
	
	public static final String WINDOW_TITLE = "BadEngine - Beserrovsky";
	public Color BgColor = Color.BLACK; //Color for basic background
	
	public Scene scene;
	public Player player;
	
	private void Spawns() {
		this.scene = new Scene(); //CREATE YOUR OWN SCENE EXTENDING SCENE
		this.scene.objects.add(new GameObject());
		this.player = new Player(0, 0, 10, 10);
		addKeyListener(this);
	}
	
	private void tickFrame() {
		this.player.tick();
		this.scene.tick();
	}
	
	private void renderFrame(Graphics g) {
		this.scene.render(g);
		this.player.render(g);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) this.player.front = true;
		if(e.getKeyCode() == KeyEvent.VK_A) this.player.left = true;
		if(e.getKeyCode() == KeyEvent.VK_S) this.player.down = true;
		if(e.getKeyCode() == KeyEvent.VK_D) this.player.right = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) this.player.front = false;
		if(e.getKeyCode() == KeyEvent.VK_A) this.player.left = false;
		if(e.getKeyCode() == KeyEvent.VK_S) this.player.down = false;
		if(e.getKeyCode() == KeyEvent.VK_D) this.player.right = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {	
		
	}
	
	/*
	 * 
	 */
	
	//ENGINE SETTINGS:
	
	public boolean SimpleMetrics = true; //Enable for FPS on window name
	public boolean DrawnMetrics = false; //Enable for FPS on screen
	
	public int RenderLimit = 120; //Frames per second
	public double AmountOfTicks = 60.0; //Ticks per second

	public Color FpsColor = Color.YELLOW; //Color for FPS metrics	
	
	/*
	 * 
	 */
	
	//ENGINE LOGIC:
	
	private static JFrame frame;
	private Thread mainThread;
	private boolean isRunning = false;
	
	public Game() {
		initGUI();
		Spawns();
		
		this.start();
	}
	
	public synchronized void start() {
		this.mainThread = new Thread(this);
		this.isRunning = true;
		this.mainThread.start();
	}
	
	public synchronized void stop() throws InterruptedException {
		isRunning = false;
		this.mainThread.join();
	}
	
	private void initGUI(){
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		frame = new JFrame(WINDOW_TITLE);
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		new Game();
	}
	
	private void handle_tick() {
		tickFrame();
	}
	
	private void handle_render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs==null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(BgColor);
		g.fillRect(0, 0, getWidth(), getHeight()); //Background w/ width and height directly from Component class
		renderFrame(g);
		if(SimpleMetrics) frame.setTitle(WINDOW_TITLE + " - " + "UPS: " + lastUpdateRate + " | FPS: "+ lastFrameRate);;
		if(DrawnMetrics) renderMetrics(g);
		g.dispose();
		bs.show();
	}
	
	private int lastFrameRate = RenderLimit;
	private int lastUpdateRate = (int)AmountOfTicks;
	private void renderMetrics(Graphics g) {
		g.setColor(FpsColor);
		g.drawString("UPS: " + lastUpdateRate + " | FPS: "+ lastFrameRate, 0, 10);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nTicks = 1000000000 / AmountOfTicks;
		double nRender = 1000000000 / RenderLimit;
		double deltaTicks = 0, deltaRender = 0;
		int ticks = 0, frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			deltaTicks+= (now - lastTime) / nTicks;
			deltaRender+= (now - lastTime) / nRender;
			lastTime = now;
			if(deltaTicks >= 1) {
				handle_tick();
				ticks++;
				deltaTicks--;
			}
			if(deltaRender >= 1) {
				handle_render();
				frames++;
				deltaRender--;
			}
			if(System.currentTimeMillis() - timer >= 1000){
				lastUpdateRate = ticks;
				lastFrameRate = frames;
				ticks = 0;
				frames = 0;
				timer+=1000;
			}
		}
		
		try {
			this.stop();
		}catch(InterruptedException ex) {
			System.out.println("Got exception when killing main thread!");
		}
	}
}
