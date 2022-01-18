package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class ImageTest {
	public static void showImage(BufferedImage img) {
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

	public static void main(String[] args) throws Exception {
		Wget.wGet("in.png",
				"https://sprofile.line-scdn.net/0hTpQVNSrvC198Lya8AUh1IAx_CDVfXlJNVUxAOUsuUm1AHxhZVkgTOR56BmsTGBhZUkxFMBkmUG5wPHw5Ynn3a3sfVWhAH0kKU0pEsA");
		BufferedImage img =ImageIO.read(new File("in.png"));
		showImage(img);
	}

}
