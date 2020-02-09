package de.neuwirthinformatik.Alexander.TU.TURender;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance.Info;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Faction;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Level;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Rarity;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Basic.SkillSpec;
import de.neuwirthinformatik.Alexander.TU.Render.Render;
import de.neuwirthinformatik.Alexander.TU.util.GUI;

public class RenderPanel extends JPanel{
	JTextField path;
	JPanel datapanel;
	JTextField dataname;
	JTextField imgfile;
	JTextField load;
	GUI.IntegerField dhealth,ddelay,dattack,ddef,drarity,dfaction,dlevel;
	
	JComboBox<Faction> jcbfaction;
	JComboBox<Level> jcblevel;
	JComboBox<Rarity> jcbrarity;
	JComboBox<CardType> jcbtype;
	
	JTextField sid1,sid2,sid3;
	GUI.IntegerField x1,x2,x3;
	JTextField y1,y2,y3;
	JCheckBox all1,all2,all3;
	GUI.IntegerField n1,n2,n3;
	GUI.IntegerField c1,c2,c3;
	GUI.IntegerField id1,id2,id3;
	JTextField s11,s12,s13;
	JTextField s21,s22,s23;
	JTextField trigger1,trigger2,trigger3;
	JTextField txt1,txt2,txt3;
	JPanel ipanel;
	JTextField xml;
	
	BufferedImage cimg;

	Render r;
	
	
	public void saveIMG()
	{
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Save image");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    //chooser.setFileFilter(FileFilter.);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    
	    chooser.setSelectedFile(new File(path.getText()));
	    String nnn = path.getText();
	    
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
	    	nnn = chooser.getSelectedFile().getAbsolutePath();
	    }
	    else
	    {
	    	return;
	    }
	    
	    try {
			ImageIO.write(cimg, "png",new File(nnn));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void chooseIMG()
	{
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Choose image");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    //chooser.setFileFilter(FileFilter.);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    
	    chooser.setSelectedFile(new File("in.png"));
	    String nnn = "imgfile";
	    
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
	    	nnn = chooser.getSelectedFile().getAbsolutePath();
	    }
	    else {
	    	return;
	    }
	    
	    imgfile.setText(nnn);
	}
	
	public void loadIMG()
	{
		CardInstance ci =null;
		try {
		ci= GlobalData.getCardInstanceByNameAndLevel(load.getText());
		}catch(Exception e) {}
		if(ci==null || ci==CardInstance.NULL)
		{
			try {
			ci = GlobalData.getCardInstanceById(Integer.parseInt(load.getText()));
			}catch(Exception e) {}
			if(ci==null || ci==CardInstance.NULL)
			{
				load.setText("unknown");
				return;
			}
		}
		BufferedImage img = r.render(ci);
		cimg = img;
		ImageIcon iicon = new ImageIcon(img);
		ipanel.removeAll();
		JLabel limg=new JLabel();
		limg.setIcon(iicon);
		ipanel.add(limg);
		recurse_summon(ci);
		this.updateUI();
	}
	
	public RenderPanel() 
	{
		super();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel tmp = new JPanel();
		tmp.add(GUI.buttonAsync("Update", () -> updateIMG()));
		tmp.add(path= GUI.textEdit("out.png"));
		tmp.add(GUI.buttonAsync("Save", () -> saveIMG()));
		tmp.add(load = GUI.textEdit("load card name or id"));
		tmp.add(GUI.buttonAsync("Load", () -> loadIMG()));
		panel.add(tmp);

		JPanel datapanel = new JPanel();
		datapanel.setLayout(new BoxLayout(datapanel, BoxLayout.Y_AXIS));

		JPanel skillpanel = new JPanel();
		skillpanel.setLayout(new BoxLayout(skillpanel, BoxLayout.Y_AXIS));
		tmp = new JPanel();
		tmp.add(dataname= GUI.textEdit("yourcardnamehere"));
		datapanel.add(tmp);
		
		tmp = new JPanel();
		tmp.add(imgfile = GUI.text("in.jpg"));
		tmp.add(GUI.buttonAsync("choose IMG", () -> chooseIMG()));
		datapanel.add(tmp);
		//JPanel d1panel = new JPanel();

		tmp = new JPanel();
		tmp.add(GUI.textSmall("hp"));
		tmp.add(dhealth = GUI.numericEdit(420));
		datapanel.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("delay"));
		tmp.add(ddelay = GUI.numericEdit(3));
		datapanel.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("atk"));
		tmp.add(dattack = GUI.numericEdit(69));
		datapanel.add(tmp);
		//datapanel.add(d1panel);

		datapanel.add(new JSeparator());
		tmp=new JPanel();
		tmp.add(GUI.textSmall("rarity"));
		jcbrarity = new JComboBox<Rarity>(Rarity.values());
		tmp.add(jcbrarity);
		//tmp.add(drarity = GUI.numericEdit(1));
		datapanel.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("faction"));
		jcbfaction = new JComboBox<Faction>(Faction.values());
		tmp.add(jcbfaction);
		//tmp.add(dfaction = GUI.numericEdit(1));
		datapanel.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("level"));
		jcblevel = new JComboBox<Level>(Level.values());
		tmp.add(jcblevel);
		//tmp.add(dlevel = GUI.numericEdit(1));
		datapanel.add(tmp);
		

		tmp=new JPanel();
		tmp.add(GUI.textSmall("type"));
		jcbtype = new JComboBox<CardType>(CardType.values());
		tmp.add(jcbtype);
		//tmp.add(dlevel = GUI.numericEdit(1));
		datapanel.add(tmp);
		//datapanel.add(d2panel);
		//String id, int x, String y, int n, int c, String s, String s2, boolean all,int card_id, String trigger
		JPanel s1p = new JPanel();
		JPanel s1p_ = new JPanel();
		s1p.add(GUI.label("id"));
		s1p.add(sid1= GUI.textEdit("summon"));
		s1p.add(all1 = GUI.check("All",false));
		s1p.add(GUI.label("y"));
		s1p.add(y1= GUI.textEdit("allfactions"));
		s1p.add(GUI.label("x"));
		s1p.add(x1= GUI.numericEdit(0));
		s1p.add(GUI.label("n"));
		s1p.add(n1= GUI.numericEdit(0)); 
		s1p.add(GUI.label("every"));
		s1p.add(c1= GUI.numericEdit(0));
		s1p.add(GUI.label("On"));
		s1p.add(trigger1= GUI.textEdit("attacked"));
		s1p_.add(GUI.label("s1"));
		s1p_.add(s11= GUI.textEdit("no_skill"));
		s1p_.add(GUI.label("s2"));
		s1p_.add(s21= GUI.textEdit("no_skill"));
		s1p_.add(GUI.label("summon_id"));
		s1p_.add(id1= GUI.numericEdit(57801));// todo render summon tooo!!!!!	
		s1p_.add(GUI.label("custom"));
		s1p_.add(txt1= GUI.textEdit(""));
		skillpanel.add(s1p);
		skillpanel.add(s1p_);
		skillpanel.add(new JSeparator());
		
		JPanel s2p = new JPanel();
		JPanel s2p_ = new JPanel();
		s2p.add(GUI.label("id"));
		s2p.add(sid2= GUI.textEdit("strike"));
		s2p.add(all2 = GUI.check("All",false));
		s2p.add(GUI.label("y"));
		s2p.add(y2= GUI.textEdit("allfactions"));
		s2p.add(GUI.label("x"));
		s2p.add(x2= GUI.numericEdit(9));
		s2p.add(GUI.label("n"));
		s2p.add(n2= GUI.numericEdit(0));
		s2p.add(GUI.label("every"));
		s2p.add(c2= GUI.numericEdit(0));
		s2p.add(GUI.label("On"));
		s2p.add(trigger2= GUI.textEdit("death"));
		s2p_.add(GUI.label("s1"));
		s2p_.add(s12= GUI.textEdit("no_skill"));
		s2p_.add(GUI.label("s2"));
		s2p_.add(s22= GUI.textEdit("no_skill"));
		s2p_.add(GUI.label("summon_id"));
		s2p_.add(id2= GUI.numericEdit(0));// todo render summon tooo!!!!!	
		s2p_.add(GUI.label("custom"));
		s2p_.add(txt2= GUI.textEdit("")); 
		skillpanel.add(s2p);
		skillpanel.add(s2p_);
		skillpanel.add(new JSeparator());
		
		JPanel s3p = new JPanel();
		JPanel s3p_ = new JPanel();
		s3p.add(GUI.label("id"));
		s3p.add(sid3= GUI.textEdit("disease"));
		s3p.add(all3 = GUI.check("All",false));
		s3p.add(GUI.label("y"));
		s3p.add(y3= GUI.textEdit("allfactions"));
		s3p.add(GUI.label("x"));
		s3p.add(x3= GUI.numericEdit(9));
		s3p.add(GUI.label("n"));
		s3p.add(n3= GUI.numericEdit(0));	 
		s3p.add(GUI.label("every"));
		s3p.add(c3= GUI.numericEdit(0));
		s3p.add(GUI.label("On"));
		s3p.add(trigger3= GUI.textEdit("death"));
		s3p_.add(GUI.label("s1"));
		s3p_.add(s13= GUI.textEdit("no_skill"));
		s3p_.add(GUI.label("s2"));
		s3p_.add(s23= GUI.textEdit("no_skill"));
		s3p_.add(GUI.label("summon_id"));
		s3p_.add(id3= GUI.numericEdit(0));// todo render summon tooo!!!!!
		s3p_.add(GUI.label("custom"));
		s3p_.add(txt3= GUI.textEdit("Always Laugh"));
		skillpanel.add(s3p);
		skillpanel.add(s3p_);
		ipanel = new JPanel();
		try {
			r = new Render();
		}
	catch(Exception e) {e.printStackTrace();
	JOptionPane.showMessageDialog(this, "Error on input!");
	}
		updateIMG();
		//panel.add(new JScrollPane(ipanel));
		
		tmp = new JPanel();
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(ipanel),datapanel);
		splitpane.setPreferredSize(new Dimension(1000,400));
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(500);
		
		tmp.add(splitpane);
		panel.add(tmp);
		panel.add(skillpanel);
		

		panel.add(xml= GUI.textEdit("soon your xml here in/out"));
		
		add(panel);
		
	}
	
	public void updateIMG()
	{
		try {
		SkillSpec ss1 = new SkillSpec(sid1.getText().toLowerCase(),x1.getNumber(),y1.getText().toLowerCase(),n1.getNumber(),c1.getNumber(),s11.getText().toLowerCase(),s21.getText().toLowerCase(),all1.isSelected(),id1.getNumber(), trigger1.getText().toLowerCase());
		SkillSpec ss2 = new SkillSpec(sid2.getText().toLowerCase(),x2.getNumber(),y2.getText().toLowerCase(),n2.getNumber(),c2.getNumber(),s12.getText().toLowerCase(),s22.getText().toLowerCase(),all2.isSelected(),id2.getNumber(), trigger2.getText().toLowerCase());
		SkillSpec ss3 = new SkillSpec(sid3.getText().toLowerCase(),x3.getNumber(),y3.getText().toLowerCase(),n3.getNumber(),c3.getNumber(),s13.getText().toLowerCase(),s23.getText().toLowerCase(),all3.isSelected(),id3.getNumber(), trigger3.getText().toLowerCase());
		Info i = new Info(dattack.getNumber(),dhealth.getNumber(),ddelay.getNumber(),((Level)jcblevel.getSelectedItem()).toInt(),new SkillSpec[] {ss1,ss2,ss3});
		Info[] ia = new Info[] {i};
		Card c = new Card(new int[] {1},dataname.getText(),((Rarity)jcbrarity.getSelectedItem()).toInt(),((Level)jcblevel.getSelectedItem()).toInt(),new int[] {},0,0,((Faction)jcbfaction.getSelectedItem()).toInt(),ia, "",0);
		CardInstance ci = CardInstance.get(1,c,i);
		
		//System.out.println(Data.getCardInstanceByNameAndLevel("Bulkhead Brawler-1").getSkills()[2].x);
		BufferedImage img = r.render(ci,new String[] {txt1.getText(),txt2.getText(),txt3.getText()},imgfile.getText(),(CardType)jcbtype.getSelectedItem());
		cimg = img;
		ImageIcon iicon = new ImageIcon(img);
		ipanel.removeAll();
		JLabel limg=new JLabel();
		limg.setIcon(iicon);
		ipanel.add(limg);
		for(int j : new int[] {id1.getNumber(),id2.getNumber(),id3.getNumber()})
		{
			if(j != 0) {
				CardInstance cj = GlobalData.getCardInstanceById(j);
				img = r.render(cj);
				iicon = new ImageIcon(img);
				limg=new JLabel();
				limg.setIcon(iicon);
				ipanel.add(limg);
				recurse_summon(cj);
			}
		}
		this.updateUI();
		}catch(Exception e) {e.printStackTrace();
		JOptionPane.showMessageDialog(this, "Error on input!");
		}
	}
	
	public void recurse_summon(CardInstance ci)
	{
		for(SkillSpec ss : ci.getSkills()) {
			int j	 = ss.getCard_id();
			if(j != 0) {
			CardInstance cj = GlobalData.getCardInstanceById(j);
			BufferedImage img = r.render(cj);
			ImageIcon iicon = new ImageIcon(img);
			JLabel limg=new JLabel();
			limg.setIcon(iicon);
			ipanel.add(limg);
			recurse_summon(cj);
			}
		}
	}
}
