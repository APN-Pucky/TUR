package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Render.Render;

import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Result;
public class ScoreImageTest extends CardImageTest {
	public static void main(String[] args) throws FontFormatException, IOException {
	
		GlobalData.init();
		String name = "DR_F3LL";
		int seed = name.hashCode();
		CardInstance com = Gen.genCardInstance(name,seed,true);
		CardInstance dom = Gen.genCardInstance(name,seed,CardType.DOMINION);
		CardInstance ass = Gen.genCardInstance(name,seed,CardType.ASSAULT);

		BufferedImage img = new Render().renderScore(new Result(100,0,0,0),com,dom,ass,com,dom,ass,new File("in.png"),new File("in.png"));
		if(img == null)
			System.out.println("null img");
		else
			showImage(img);
	}
}
