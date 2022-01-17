package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.yamlbeans.YamlReader;

import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Render.Render;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;
import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class TreeImageTest extends ImageTest{

	public static void main(String[] args) throws Exception {
		GlobalData.init();
		//BufferedImage img = new BufferedImage(160, 220, BufferedImage.TYPE_INT_ARGB);
		//Graphics g = img.createGraphics();
		//g.drawImage(getImage(222,"rightwidewings_lv3"), 0, 0, 150, 125,0,0,150,125,null);
		Render r = new Render();
		//System.out.println(Data.getCardInstanceByNameAndLevel("Bulkhead Brawler-1").getSkills()[2].x);
		CardInstance c = GlobalData.getCardInstanceByNameAndLevel("Obsidian Overlord");
		
		
		BufferedImage img = r.renderTree(c);
		System.out.println(c.getCard().picture);
		showImage(img);
	}

}
