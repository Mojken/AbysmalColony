package enums;

import net.abysmal.engine.handlers.misc.Tile;

public enum Tiles {

	emptyF(0, "void", 0),
	emptyR(0, "void", 1),
	emptyB(0, "voidB", 2),
	
	sand(1, "sand", 0),
	sand2(2, "sand2", 0),
	sand3(3, "sand3", 0),
	
	fence(1, "fence", 1)
	;

	final int id, layer;
	final String path;
	final String[] traits;

	private Tiles(int id, String path, int layer) {
		this.id = id;
		this.layer = layer;
		this.path = path;
		this.traits = new String[] { "" };
		new Tile(id, path, null);
		Tile.getArrayList(layer).add(new Tile(id, path, traits));
	}

	private Tiles(int id, String path, String[] traits, int layer) {
		this.id = id;
		this.layer = layer;
		this.path = path;
		this.traits = traits;
		new Tile(id, path, traits);
		Tile.getArrayList(layer).add(new Tile(id, path, traits));
	}
}