package com.beserrovsky.bad_engine;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject{

	public boolean front, left, down, right;
	
	public Player(int x, int y, int width, int height) {
		this.x = x; this.y = y; this.width = width; this.height = height;
	}
	
	public void tick() {
		if(front) y--;
		if(left) x--;
		if(down) y++;
		if(right) x++;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(x, y, width, height);
	}
	
}
