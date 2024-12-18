
package com;

import javax.microedition.lcdui.Canvas;

/**
 *
 * @author Roman
 */
public class Keyboard {

	public final int UP, DOWN, LEFT, RIGHT;
	public final int FIRE, KEY7, KEY9;
	
	private boolean up, down, left, right;
	private boolean fire, key7,  key9;

	public Keyboard(Canvas canvas) {
		if(isSelect(canvas, -26)) { //Siemens
			LEFT = -61;
			RIGHT = -62;
			DOWN = -60;
			UP = -59;
			FIRE = -26;
			KEY7 = -1;
			KEY9 = -4;
		} else if(isSelect(canvas, -20)) { //Motorolla
			LEFT = -2;
			RIGHT = -5;
			DOWN = -6;
			UP = -1;
			FIRE = -20;
			KEY7 = -21;
			KEY9 = -22;
		} else { //Nokia, SE
			LEFT = -3;
			RIGHT = -4;
			DOWN = -2;
			UP = -1;
			FIRE = -5;
			KEY7 = -6;
			KEY9 = -7;
		}
	}

	private static boolean isSelect(Canvas c, int keyCode) {
		try {
			return c.getKeyName(keyCode).toUpperCase().indexOf("SELECT") != -1;
		} catch(Exception ex) {
			return false;
		}
	}
	
    public void keyPressed(int keyCode) {keyInput(keyCode, true);}
    public void keyReleased(int keyCode) {keyInput(keyCode, false);}

	public void keyInput(int keyCode, boolean pressed) {
		if(keyCode == UP || keyCode == Canvas.KEY_NUM2) up = pressed;
        if(keyCode == DOWN || keyCode == Canvas.KEY_NUM8) down = pressed; else
        if(keyCode == LEFT || keyCode == Canvas.KEY_NUM4) left = pressed; else
        if(keyCode == RIGHT || keyCode == Canvas.KEY_NUM6) right = pressed; else
        if(keyCode == FIRE || keyCode == Canvas.KEY_NUM5) fire = pressed; else
        
        if(keyCode == Canvas.KEY_NUM7) key7 = pressed; else
        if(keyCode == Canvas.KEY_NUM9) key9 = pressed;
	}

	public boolean keyUp() {return up;}
	public boolean keyDown() {return down;}
	public boolean keyLeft() {return left;}
	public boolean keyRight() {return right;}
	
	public boolean keyCentre() {return fire;}
	public boolean key7() {return key7;}
	public boolean key9() {return key9;}
}
