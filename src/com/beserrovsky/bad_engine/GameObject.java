package com.beserrovsky.bad_engine;

import java.awt.Color;
import java.awt.Graphics;

public class GameObject {
	
	public int x = 0,y = 0,width = 10,height = 10;
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
	}
}
