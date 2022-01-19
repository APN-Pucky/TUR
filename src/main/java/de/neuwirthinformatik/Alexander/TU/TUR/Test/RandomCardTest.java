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

public class RandomCardTest extends CardImageTest {
	public static void main(String[] args) throws FontFormatException, IOException {
		GlobalData.init();
		String name = "DR_F3LL_HOLL___";
		int seed = name.hashCode();
		CardInstance ci = Gen.genCardInstance(name,seed,(c) -> c.getCardType()==CardType.DOMINION);
		BufferedImage img = new Render().render(ci,new String[] {"","",""},"in.png",ci.getCardType());
		if(img == null)
			System.out.println("null img");
		else
			showImage(img);
	}

}
