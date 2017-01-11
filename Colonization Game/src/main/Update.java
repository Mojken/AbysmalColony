package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.abysmal.engine.graphics.Graphics;
import net.abysmal.engine.graphics.Window;
import net.abysmal.engine.graphics.renderding2D.ISOGraphics;
import net.abysmal.engine.handlers.HID.Keyboard;
import net.abysmal.engine.handlers.misc.Tick;
import net.abysmal.engine.handlers.misc.Tile;
import net.abysmal.engine.maths.Vector;

public class Update implements Tick {

	Window w;
	URL map = ClassLoader.getSystemResource("map.png");
	BufferedImage player, marker, sand, sand2, sand3, cactus;
	ISOGraphics isoG = new ISOGraphics();
	ArrayList<Vector> tiles;
	Vector scroll = new Vector(0, 0);
	Vector pPos = Vector.ZERO();
	int tileSize = 64;
	Map<Integer, Map<Integer, BufferedImage>> worldMap;
	Map<Integer, Map<Integer, BufferedImage>> buildMap;

	public Update(launchGame game) {
		w = game.w;

		worldMap = new HashMap<>();
		buildMap = new HashMap<>();

		try {
			player = ImageIO.read(ClassLoader.getSystemResource("player.png"));
			marker = ImageIO.read(ClassLoader.getSystemResource("tiles/marker.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {

	}

	int time = 0;

	@Override
	public void render(java.awt.Graphics g2) {

		@SuppressWarnings("deprecation")
		boolean[] mb = Keyboard.getPressedMovementButtons();
		time--;
		if (mb[0] && time < 0) {
			pPos = pPos.add(new Vector(0, -.15f));
			time = 1;
		}
		if (mb[1] && time < 0) {
			pPos = pPos.add(new Vector(-.15f, 0));
			time = 1;
		}
		if (mb[2] && time < 0) {
			pPos = pPos.add(new Vector(0, .15f));
			time = 1;
		}
		if (mb[3] && time < 0) {
			pPos = pPos.add(new Vector(.15f, 0));
			time = 1;
		}

		g2.clearRect(0, 0, w.getSize().getWidth(), w.getSize().getHeight());
		Graphics g = new Graphics((Graphics2D) g2.create());

		Vector start = isoG.getTileCoordinates(Vector.ZERO(), tileSize, pPos).add(new Vector((int) pPos.x, (int) pPos.y)).add(new Vector(-3, 0));
		Vector end = isoG.getTileCoordinates(w.getSize().toVector(), tileSize, pPos).add(new Vector((int) pPos.x, (int) pPos.y)).add(new Vector(1, 2));

		for (int x = (int) (start.x + start.y); x < (int) (end.x + end.y); x++) {
			for (int y = (int) (start.x - start.y); y < (int) (end.x - end.y); y++) {
				if ((y & 1) != (x & 1)) continue;
				for (int i = 0; i < 2; i++) {
					Vector pos = new Vector((int) (x + y) / 2, (int) (x - y) / 2, i);
					BufferedImage img = getTile(pos);
					if (img == null) continue;
					g.drawImage(img, isoG.twoDToIso(pos.add(pPos.add(-.5f).multiply(-1)).multiply(tileSize)).add(tileSize).add(new Vector(0, -img.getHeight())));
				}
			}
		}

		Vector markerPos = isoG.getTileCoordinates(w.mouseListener.getMousePosition(), tileSize, pPos).add(new Vector(-(pPos.x - (int) pPos.x), -(pPos.y - (int) pPos.y)));

		if (w.mouseListener.getClickInfo()[1][4] == 1) {
			Vector temp = new Vector(markerPos.x - 1, markerPos.y + 1).add(pPos);
			buildMap.putIfAbsent((int) temp.x, new HashMap<>());

			buildMap.get((int) temp.x).putIfAbsent((int) temp.y, Tile.getArrayList(1).get(1).getImage());
		}
		g.drawImage(marker, isoG.twoDToIso(markerPos.multiply(tileSize).add(new Vector(0, marker.getHeight()))));
	}

	private BufferedImage getTile(Vector pos) {

		if ((int) pos.z == 0) {
			worldMap.putIfAbsent((int) pos.x, new HashMap<>());
			if (null == worldMap.get((int) pos.x).get((int) pos.y)) {
				int rand = (int) System.currentTimeMillis() % 3;
				worldMap.get((int) pos.x).put((int) pos.y, Tile.getArrayList(0).get(rand + 1).getImage());
			}

			return worldMap.get((int) pos.x).get((int) pos.y);

		} else if ((int) pos.z == 1) {
			if (buildMap.containsKey((int) pos.x)) {
				if (buildMap.get((int) pos.x).containsKey((int) pos.y)) { return buildMap.get((int) pos.x).get((int) pos.y); }
			}
		}

		return null;
	}
}