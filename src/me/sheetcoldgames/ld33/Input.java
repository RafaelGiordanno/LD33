package me.sheetcoldgames.ld33;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class Input implements InputProcessor {
	public boolean buttons[];
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public Input() {
		buttons = new boolean[32];
		for (int k = 0; k < buttons.length; k++) {
			buttons[k] = false;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A || keycode == Keys.LEFT) {
			buttons[LEFT] = true;
		} else if (keycode == Keys.W || keycode == Keys.UP ) {
			buttons[UP] = true;
		} else if (keycode == Keys.S || keycode == Keys.DOWN ) {
			buttons[DOWN] = true;
		} else if (keycode == Keys.D || keycode == Keys.RIGHT ) {
			buttons[RIGHT] = true;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.A || keycode == Keys.LEFT) {
			buttons[LEFT] = false;
		} else if (keycode == Keys.W || keycode == Keys.UP ) {
			buttons[UP] = false;
		} else if (keycode == Keys.S || keycode == Keys.DOWN ) {
			buttons[DOWN] = false;
		} else if (keycode == Keys.D || keycode == Keys.RIGHT ) {
			buttons[RIGHT] = false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
