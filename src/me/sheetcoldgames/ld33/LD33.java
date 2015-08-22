package me.sheetcoldgames.ld33;

import me.sheetcoldgames.ld33.engine.Entity;
import me.sheetcoldgames.ld33.engine.SheetCamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;

public class LD33 extends ApplicationAdapter {
	ShapeRenderer sr;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;
	
	SheetCamera camera;
	
	Input input;
	
	TiledMapTileLayer layer;
	public static boolean[][] collidableArea = new boolean[64][64];
	
	Entity player;
	
	public void create() {
		initRenderers();
		initPlayer();
		initCamera();
		initCollidableBlocks();
		
		
		input = new Input();
		Gdx.input.setInputProcessor(input);
	}
	
	private void initPlayer() {
		player = new Entity();
		player.pos.set(4f, 28f);
	}
	
	private void initRenderers() {
		map = new TmxMapLoader().load("first_level.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);
		sr = new ShapeRenderer();
	}
	
	private void initCamera() {
		camera = new SheetCamera(20f, 15f);
		camera.setTarget(player);
	}
	
	private void initCollidableBlocks() {
		layer = (TiledMapTileLayer) map.getLayers().get("Collidable");
		
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				
				// Checamos para ver se existe algum tile aqui				
				if (cell == null || cell.getTile() == null) {
					collidableArea[col][row] = false;
					continue;
				}
				
				// adicionamos essa posição à collidableBlocks;
				collidableArea[col][row] = true;
			}
		}
	}
	
	public void dispose() {
		sr.dispose();
		mapRenderer.dispose();
	}
	
	float dt;
	
	public void render() {
		dt = Gdx.graphics.getDeltaTime();
		handleInput();
		checkCollisions(player);
		updatePosition(player);
		camera.update();
		renderWorld();
	}
	
	public float MAX_VEL_X = .1f;
	public float MAX_VEL_Y = .3f;
	
	private void handleInput() {
		if (input.buttons[Input.RIGHT]) {
			if (player.vel.x <= 0f) {
				player.vel.x = .8f;
			}
			player.vel.x += .2f;
		} else if (input.buttons[Input.LEFT]) {
			if (player.vel.x >= 0f) {
				player.vel.x = -.8f;
			}
			player.vel.x -= .2f;
		} else {
			player.vel.x = 0f;
		}
		
		applyGravity(player, dt);
//		if (player.vel.y < 0f && player.vel.y > -.1f) {
//			player.vel.y = -.1f;
//		}
		
		if (input.buttons[Input.UP]) {
			jump(player);
		}
		
		// clamping the velocity
		player.vel.x = MathUtils.clamp(player.vel.x, -MAX_VEL_X, MAX_VEL_X);
		player.vel.y = MathUtils.clamp(player.vel.y, -MAX_VEL_Y, MAX_VEL_Y);
		
		System.out.println(player.vel);
	}
	
	private void jump(Entity ent) {
		if (ent.grounded) {
			ent.grounded = false;
			ent.vel.y = .3f;
		}
	}
	
	private void checkCollisions(Entity ent) {
		// System.out.println(ent.pos);
		float minorX = player.pos.x - player.width/2f + ent.vel.x;
		float majorX = player.pos.x + player.width/2f + ent.vel.x;
		
		float minorY = player.pos.y - player.height/2f + ent.vel.y * 2f;
		float majorY = player.pos.y + player.height/2f + ent.vel.y * 2f;
		
		if (collidableArea[MathUtils.round(minorX)][MathUtils.floor(ent.pos.y)]) {
			ent.vel.x = 0;
			ent.pos.x = minorX+.6f;
		}
		if (collidableArea[MathUtils.round(majorX)][MathUtils.floor(ent.pos.y)]) {
			ent.vel.x = 0;
			ent.pos.x = majorX-.6f;
		}
		
		// checkin y
		if (collidableArea[MathUtils.floor(ent.pos.x)][MathUtils.round(minorY)]) {
			ent.vel.y = 0;
			ent.pos.y = minorY + .6f;
			ent.grounded = true;
		}
		
		if (collidableArea[MathUtils.floor(ent.pos.x)][MathUtils.round(majorY)]) {
			ent.vel.y = 0;
			ent.pos.y = majorY-.6f;
		}
	}
	
	private void applyGravity(Entity ent, float dt) {
		ent.vel.y -= 10f * 1/16f * dt;
	}
	
	private void updatePosition(Entity ent) {
		// updating the position
		ent.pos.x += ent.vel.x;
		ent.pos.y += ent.vel.y;
	}
	
	private void renderWorld() {
		clearScreen();
		sr.setProjectionMatrix(camera.combined);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		sr.begin(ShapeType.Filled);
		renderNeighbourCollisionAreas(sr);
		renderPlayer(sr);
		sr.end();
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void renderPlayer(ShapeRenderer sr) {
		sr.setColor(Color.PURPLE);
		sr.rect(player.pos.x, player.pos.y, player.width, player.height);
	}
	
	private void renderNeighbourCollisionAreas(ShapeRenderer sr) {
		float minorX = player.pos.x - player.width/2f;
		float majorX = player.pos.x + player.width/2f;
		
		float minorY = player.pos.y - player.height/2f;
		float majorY = player.pos.y + player.height/2f;
		
		sr.setColor(Color.NAVY);
		sr.rect(MathUtils.round(minorX), player.pos.y, 1f, 1f);
		sr.rect(MathUtils.round(majorX), player.pos.y, 1f, 1f);
		sr.rect(player.pos.x, MathUtils.round(minorY), 1f, 1f);
		sr.rect(player.pos.x, MathUtils.round(majorY), 1f, 1f);
	}
}
