package me.sheetcoldgames.ld33.engine;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	public Vector2 pos;
	public Vector2 vel;
	
	public float width;
	public float height;
	
	public boolean jump;
	public boolean isJumping;
	public boolean grounded = false;
	
	public Entity(float w, float h) {
		this.width = w;
		this.height = h;
		pos = new Vector2();
		vel = new Vector2();
	}
	
	public Entity() {
		this(1f, 1f);
	}
	
	public void set(float x, float y) {
		pos.set(x, y);
	}
}
