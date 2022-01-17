package de.neuwirthinformatik.Alexander.TU.TUR.Test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageTest {
 public static void showImage(BufferedImage img ) {
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
