package me.sheetcoldgames.ld33.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class SheetCamera extends OrthographicCamera {
	Entity target;
	
	public SheetCamera(float w, float h) {
		super(w, h);
		position.set(viewportWidth/2f, viewportHeight/2f, 0f);
		update();
	}
	
	public SheetCamera() {
		super();
		update();
	}
	
	public void update() {
		if (hasTarget()) {
			position.x = target.pos.x;
			position.y = target.pos.y;
		}
		clamp();
		super.update();
	}
	
	private void clamp() {
		position.x = MathUtils.clamp(position.x, viewportWidth/2f, 48);
		position.y = MathUtils.clamp(position.y, viewportHeight/2f, 48f);
	}
	
	public void setTarget(Entity other) {
		this.target = other;
	}
	
	public boolean hasTarget() {
		return target != null;
	}
	
	public Entity getTarget() {
		return target;
	}

}
