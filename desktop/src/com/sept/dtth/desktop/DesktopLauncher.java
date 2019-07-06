package com.sept.dtth.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sept.dtth.DownTo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = DownTo.V_HEIGHT*2;
		config.width = DownTo.V_WIDTH*2;
		new LwjglApplication(new DownTo(), config);
	}
}
