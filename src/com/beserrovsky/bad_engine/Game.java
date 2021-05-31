package com.beserrovsky.bad_engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/*TODO:
 * Implement: 
 * - player
 * - camera logic
 * - collision
 * - map
 * - sound
 * */

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {
	
	//GENERAL SETTINGS
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	public static final String WINDOW_TITLE = "";
	
	
	//INSTANCES
	
	
	
	private void Spawns() {
		
	}
	
	private void tickFrame() {
		
	}
	
	private void renderFrame(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight()); //Background w/ width and height directly from Component class
	}
	
	//ENGINE SETTINGS
	
	public boolean metrics = true; //Enable for FPS on screen
	
	
	//ENGINE LOGIC ;)
	
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
		renderFrame(g);
		if(metrics) renderMetrics(g);
		g.dispose();
		bs.show();
	}
	
	private int lastFrameRate = 0;
	private void renderMetrics(Graphics g) {
		System.out.println("FPS: "+ lastFrameRate);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				handle_tick();
				handle_render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				lastFrameRate = frames;
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
