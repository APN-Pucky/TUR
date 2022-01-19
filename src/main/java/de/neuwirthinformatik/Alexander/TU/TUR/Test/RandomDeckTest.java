package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Render.Render;

public class RandomDeckTest extends CardImageTest {
	public static void main(String[] args) throws FontFormatException, IOException {
		GlobalData.init();
		String name = "DR_F3LL";
		int seed = name.hashCode();
		CardInstance com = Gen.genCardInstance(name,seed,true);
		CardInstance dom = Gen.genCardInstance(name,seed,(c) -> c.getCardType()==CardType.DOMINION);
		CardInstance ass = Gen.genCardInstance(name,seed,(c) -> c.getCardType()==CardType.ASSAULT);

		BufferedImage img = new Render().renderDeck(new CardInstance[] {com,dom,ass,ass,ass,ass,ass,ass,ass,ass,ass,ass},"in.png");
		if(img == null)
			System.out.println("null img");
		else
			showImage(img);
	}

}
