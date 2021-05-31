package com.beserrovsky.bad_engine;

import java.awt.Graphics;
import java.util.Stack;

public class Scene {
	
	public Stack<GameObject> objects = new Stack<GameObject>();
	
	public void tick() {
		for(int i = 0; i < objects.size(); i++ ) {
			objects.elementAt(i).tick();
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < objects.size(); i++ ) {
			objects.elementAt(i).render(g);
		}
	}
}
