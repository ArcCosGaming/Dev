package game;

import static graphics.GraphicsEngine.getRelX;
import static graphics.GraphicsEngine.getRelY;

import geometry.Vector;
import graphics.*;

import org.lwjgl.input.Keyboard;

public class GameStarter {
	public final static String GAME_TITLE = "MegaMon";
	
	public static int Up = Keyboard.KEY_W;
	public static int Down = Keyboard.KEY_S;
	public static int Left = Keyboard.KEY_A;
	public static int Right = Keyboard.KEY_D;
	public static int End = Keyboard.KEY_ESCAPE;
	
	private static int distance, speed;
	private static Player player;
	private static float viewX, viewY;
	private static boolean paused;
	private static Vector shouldPos;
	
	public static void start() {
		distance = 20;
		speed = 1;
		viewX = 0;
		viewY = 0;
		GraphicsEngine.setResolution(640, 480);
		GraphicsEngine.create();
		GraphicsEngine.updateView(viewX, viewY);
		player = new Player();
		GraphicsEngine.setGrid(true);
		GraphicsEngine.setGridDistance(distance);
		GraphicsEngine.addRenderable(player);
		//GraphicsEngine.addRenderable(new RenderObject());
		//GraphicsEngine.addRenderable(new Map());
		//GraphicsEngine.addRenderable(new RenderObject(400, 400, 40, 80));
		//this.isPos = new Point(player.getPosX(), player.getPosY());
		shouldPos = player.getPos();
		for (int i = 0; i < 1000000; i++)
		{
			if (GraphicsEngine.isFinished())
				break;
			if (!paused)
				logic();
			GraphicsEngine.updateView(viewX, viewY);
			GraphicsEngine.runOnce();
		}
	}
	
	public static void main (String[] args)
	{
		GameStarter.start();
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
	public static void setView(float x, float y) {
		viewX = x;
		viewY = y;
	}
	
	public static void setKey(int key, int keyCode)
	{
		if (key == 0)
			Up = keyCode;
		else if (key == 1)
			Down = keyCode;
		else if (key == 2)
			Left = keyCode;
		else if (key == 3)
			Right = keyCode;
	}
	
	private static void logic()
	{
	    if (Keyboard.isKeyDown(End))
	    	GraphicsEngine.end();
	    else if (Keyboard.isKeyDown(Up)) {
	    	if (player != null && player.isStanding()) {
		    	shouldPos.translate(0, getRelY(distance));
		    	player.walk(shouldPos, 0, speed);
	    	} else {
	    		//viewY += getRelY(distance);
	    	}
	    }
		else if (Keyboard.isKeyDown(Left)) {
			if (player != null && player.isStanding()) {
				shouldPos.translate(-getRelX(distance), 0);
		    	player.walk(shouldPos, 1, speed);
			} else {
				//viewX -= getRelX(distance);
			}
		}
		else if (Keyboard.isKeyDown(Down)) {
			if (player != null && player.isStanding()) {
				shouldPos.translate(0, -getRelY(distance));
		    	player.walk(shouldPos, 2, speed);
			} else {
	    		//viewY -= getRelY(distance);
	    	}
		}
		else if (Keyboard.isKeyDown(Right)) {
			if (player != null && player.isStanding()) {
				shouldPos.translate(getRelX(distance), 0);
		    	player.walk(shouldPos, 3, speed);
			} else {
				//viewX += getRelX(distance);
			}
		}
	}
}
