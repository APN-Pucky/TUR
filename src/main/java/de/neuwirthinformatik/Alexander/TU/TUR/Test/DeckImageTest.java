package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.neuwirthinformatik.Alexander.TU.Basic.Deck;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Render.Render;

public class DeckImageTest {

	public static void main(String[] args) throws Exception {
		GlobalData.init();
		//BufferedImage img = new BufferedImage(160, 220, BufferedImage.TYPE_INT_ARGB);
		//Graphics g = img.createGraphics();
		//g.drawImage(getImage(222,"rightwidewings_lv3"), 0, 0, 150, 125,0,0,150,125,null);
		Render r = new Render();
		//System.out.println(Data.getCardInstanceByNameAndLevel("Bulkhead Brawler-1").getSkills()[2].x);
		Deck c = GlobalData.constructDeck("Cyrus,Obsidian Overlord, Obsidian#9");
		BufferedImage img = r.renderDeck(c);
		//BufferedImage img = getImage(204,"xenowavebreak_lv3"); //Obsdin id=4 img=0
		//ImageIO.write(img,"png", new File("output.png"));
		ImageIcon iicon = new ImageIcon(img);
		JFrame jframe = new JFrame();
		jframe.setLayout(new FlowLayout());
		jframe.setSize(2000, 2000);
		JLabel lbl = new JLabel();
		lbl.setIcon(iicon);
		jframe.add(lbl);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
