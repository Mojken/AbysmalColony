package main;

import javax.swing.JFrame;
import enums.Tiles;
import net.abysmal.engine.GlobalVariables;
import net.abysmal.engine.graphics.Window;
import net.abysmal.engine.handlers.misc.Settings;
import net.abysmal.engine.handlers.misc.World;

public class launchGame {
	
	Window w;
	public static JFrame f;
	public static boolean initialized = false;
	World wo;
	int screenSize = 800;
	
	public static void main(String[] args) {
		new launchGame();
	}

	public launchGame() {
		System.out.print("Initiating");
		w = new Window();
		f = w.createWindow("Abysmal Colony", screenSize);
		System.out.print(".");
		init();
		w.start(new Update(this));
		System.out.print(".");
		System.out.println(".");
		System.out.println("Done!");
		Window.frame.setVisible(true);
		initialized = true;
	}

	public void init() { // innit mate?
		for(Tiles t:Tiles.values()) t.name();
			
		Settings.setDvorak();
		GlobalVariables.debug = true;
	}
}
