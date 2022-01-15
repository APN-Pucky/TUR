package de.neuwirthinformatik.Alexander.TU.TUR;

import de.neuwirthinformatik.Alexander.GitJarUpdate.Update;

public class Main {
	public static void main(String[] args)
	{
		Update.loadUpdate("TURender.jar", "APN-Pucky", "TURender");
		TUR.startTUR();
	}
}
