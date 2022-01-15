package de.neuwirthinformatik.Alexander.TU.TUR;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
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
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.neuwirthinformatik.Alexander.TU.TU;
import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance.Info;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Faction;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Level;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Rarity;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Basic.SkillSpec;
import de.neuwirthinformatik.Alexander.TU.Basic.XMLParser;
import de.neuwirthinformatik.Alexander.TU.Render.Render;
import de.neuwirthinformatik.Alexander.TU.TUR.XMLView.XmlTextPane;
import de.neuwirthinformatik.Alexander.TU.util.GUI;
import de.neuwirthinformatik.Alexander.TU.util.Task;
import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class RenderPanel extends JPanel{
	JTextField path;
	JPanel datapanel;
	JTextField dataname;
	GUI.IntegerField did;
	JTextField imgfile;
	JTextField load;
	JCheckBox render_all_levels;
	GUI.IntegerField dhealth,ddelay,dattack,ddef,drarity,dfaction,dlevel;
	
	JComboBox<Faction> jcbfaction;
	JComboBox<Level> jcblevel; // fusion
	JComboBox<Integer> jcbrank;
	JComboBox<Integer> jcbmaxrank;
	JComboBox<Rarity> jcbrarity;
	JComboBox<CardType> jcbtype;
	
	//JTextField sid1,sid2,sid3;
	//GUI.IntegerField x1,x2,x3;
	//JTextField y1,y2,y3;
	//JCheckBox all1,all2,all3;
	//GUI.IntegerField n1,n2,n3;
	//GUI.IntegerField c1,c2,c3;
	//GUI.IntegerField id1,id2,id3;
	//JTextField s11,s12,s13;
	//JTextField s21,s22,s23;
	//JTextField trigger1,trigger2,trigger3;
	//JTextField txt1,txt2,txt3;
	
	JTextField[] sid = new JTextField[3];
	GUI.IntegerField[] x = new GUI.IntegerField[3];
	JTextField[] y = new JTextField[3];
	JCheckBox[] all = new JCheckBox[3];
	GUI.IntegerField[] n = new GUI.IntegerField[3];
	GUI.IntegerField[] c = new GUI.IntegerField[3];
	GUI.IntegerField[] id = new GUI.IntegerField[3];
	JTextField[] s1 = new JTextField[3];
	JTextField[] s2 = new JTextField[3];
	JTextField[] trigger = new JTextField[3];
	JTextField[] txt = new JTextField[3];
	
	JPanel[] ipanel = new JPanel[10];
	//JPanel ipanel,ipanel2;
	XmlTextPane xml;
	BufferedImage cimg;

	Render r;
	boolean block_img_update= false;
	
	CardInstance[] cis = new CardInstance[10];
	
	
	XMLParser xmlp = new XMLParser(true);
	
	
	
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
	
	
	
	public static void updateXML()  {updateXML(true);}
	public static void updateXML(boolean dev)
	{
		String tyrant_url = (dev?"http://mobile-dev.tyrantonline.com/assets/":"http://mobile.tyrantonline.com/assets/");
		//XML
		System.out.println("Downloading new XMLs ...");
		Task.start(() -> {
			int i =0;
	        Wget.Status status = Wget.Status.Success;
	        while (status == Wget.Status.Success)
	        {
	        	i++;
				String sec = "cards_section_" + i + ".xml";
				status = Wget.wGet("data" + GlobalData.file_seperator + sec, tyrant_url + sec);
			}
		});
		final String[] arr = new String[] {"fusion_recipes_cj2","missions","levels","skills_set"};
		for(int i =0; i < arr.length;i++)
		{
			final String sec = arr[i] + ".xml";
			Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + sec,tyrant_url + sec));
		}
		Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + "raids.xml", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/raids.xml"));
		Task.start(() -> Wget.wGet("data" + GlobalData.file_seperator + "bges.txt", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/bges.txt"));	
        Task.sleepForAll();
        System.out.println("...done -> reload");
        GlobalData.init();
	}
	
	
	
	public RenderPanel() 
	{
		super();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel tmp = new JPanel();
		tmp.add(GUI.buttonSync("Update", () -> {block_img_update=false;updateIMG();}));
		tmp.add(path= GUI.textEdit("out.png"));
		tmp.add(GUI.buttonSync("Save", () -> saveIMG()));
		tmp.add(load = GUI.textEdit("load card name or id"));
		tmp.add(GUI.buttonSync("Load", () -> loadCI()));
		tmp.add(GUI.buttonSync("LoadAll", () -> loadCard()));
		tmp.add(GUI.buttonSync("updateXML", () -> updateXML()));
		tmp.add(GUI.buttonSync("reloadXML", () -> GlobalData.init()));
		panel.add(tmp);
		
		tmp=new JPanel();
		tmp.add(GUI.buttonSync("Clear levels", () -> {clear();}));
		tmp.add(GUI.textSmall("level"));
		jcbrank = new JComboBox<Integer>(Arrays.stream( new int[] {1,2,3,4,5,6,7,8,9,10} ).boxed().toArray( Integer[]::new ));

		tmp.add(GUI.buttonSync("dec", ()->jcbrank.setSelectedItem(((Integer)jcbrank.getSelectedItem()-1)%((Integer)jcbmaxrank.getSelectedItem()+1))));
		tmp.add(jcbrank);
		tmp.add(GUI.buttonSync("inc", ()->jcbrank.setSelectedItem(((Integer)jcbrank.getSelectedItem()+1)%((Integer)jcbmaxrank.getSelectedItem()+1))));
		jcbmaxrank = new JComboBox<Integer>(Arrays.stream( new int[] {1,2,3,4,5,6,7,8,9,10} ).boxed().toArray( Integer[]::new ));
		jcbmaxrank.setSelectedItem(6);
		tmp.add(GUI.textSmall("maxlevel"));
		tmp.add(jcbmaxrank);
		tmp.add(render_all_levels= GUI.check("All",false));
		//tmp.add(dlevel = GUI.numericEdit(1));
		panel.add(tmp);

		JPanel datapanel = new JPanel();
		datapanel.setLayout(new BoxLayout(datapanel, BoxLayout.Y_AXIS));

		JPanel skillpanel = new JPanel();
		skillpanel.setLayout(new BoxLayout(skillpanel, BoxLayout.Y_AXIS));
		tmp = new JPanel();
		tmp.add(dataname= GUI.textEdit("yourcardnamehere"));
		tmp.add(GUI.textSmall("id"));
		tmp.add(did= GUI.numericEdit(999999));
		datapanel.add(tmp);
		
		tmp = new JPanel();
		tmp.add(imgfile = GUI.text("in.png"));
		tmp.add(GUI.buttonAsync("choose IMG", () -> chooseIMG()));
		datapanel.add(tmp);
		
		JPanel ddp = new JPanel();
		JPanel datapanel_ = new JPanel();
		datapanel_.setLayout(new BoxLayout(datapanel_, BoxLayout.Y_AXIS));
		
		tmp = new JPanel();
		tmp.add(GUI.textSmall("hp"));
		tmp.add(dhealth = GUI.numericEdit(420));
		datapanel_.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("delay"));
		tmp.add(ddelay = GUI.numericEdit(3));
		datapanel_.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("atk"));
		tmp.add(dattack = GUI.numericEdit(69));
		datapanel_.add(tmp);
		ddp.add(datapanel_);
		
		JSeparator js = new JSeparator();
		js.setOrientation(SwingConstants.VERTICAL);
		ddp.add(js);
		
		datapanel_ = new JPanel();
		datapanel_.setLayout(new BoxLayout(datapanel_, BoxLayout.Y_AXIS));

		tmp=new JPanel();
		tmp.add(GUI.textSmall("rarity"));
		jcbrarity = new JComboBox<Rarity>(Rarity.values());
		tmp.add(jcbrarity);
		//tmp.add(drarity = GUI.numericEdit(1));
		datapanel_.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("faction"));
		jcbfaction = new JComboBox<Faction>(Faction.values());
		tmp.add(jcbfaction);
		//tmp.add(dfaction = GUI.numericEdit(1));
		datapanel_.add(tmp);
		tmp=new JPanel();
		tmp.add(GUI.textSmall("fusion"));
		jcblevel = new JComboBox<Level>(Level.values());
		tmp.add(jcblevel);
		//tmp.add(dlevel = GUI.numericEdit(1));
		datapanel_.add(tmp);
		
		

		tmp=new JPanel();
		tmp.add(GUI.textSmall("type"));
		jcbtype = new JComboBox<CardType>(CardType.values());
		tmp.add(jcbtype);
		//tmp.add(dlevel = GUI.numericEdit(1));
		datapanel_.add(tmp);
		
		
		ddp.add(datapanel_);
		datapanel.add(ddp);
		//String id, int x, String y, int n, int c, String s, String s2, boolean all,int card_id, String trigger
		for (int k =0 ; k < 3;k++) {
		JPanel s1p = new JPanel();
		JPanel s1p_ = new JPanel();
		s1p.add(GUI.label("id"));
		s1p.add(sid[k]= GUI.text("strike",true,125));
		s1p.add(all[k] = GUI.check("All",false));
		s1p.add(GUI.label("y"));
		s1p.add(y[k]= GUI.text("allfactions",true,125));
		s1p.add(GUI.label("x"));
		s1p.add(x[k]= GUI.numericEdit(0));
		s1p.add(GUI.label("n"));
		s1p.add(n[k]= GUI.numericEdit(0)); 
		s1p.add(GUI.label("every"));
		s1p.add(c[k]= GUI.numericEdit(0));
		s1p.add(GUI.label("on"));
		s1p.add(trigger[k]= GUI.text("attacked",true,125));
		s1p_.add(GUI.label("s1"));
		s1p_.add(s1[k]= GUI.text("no_skill",true,125));
		s1p_.add(GUI.label("s2"));
		s1p_.add(s2[k]= GUI.text("no_skill",true,125));
		s1p_.add(GUI.label("summon_id"));
		s1p_.add(id[k]= GUI.numericEdit(0));// todo render summon tooo!!!!!	
		s1p_.add(GUI.label("custom"));
		s1p_.add(txt[k]= GUI.text("",true,125));
		skillpanel.add(s1p);
		skillpanel.add(s1p_);
		skillpanel.add(new JSeparator());
		}
		/*
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
		skillpanel.add(s3p_);*/

		JPanel allipanel = new JPanel();
		allipanel.setLayout(new BoxLayout(allipanel, BoxLayout.Y_AXIS));
		for(int i = 00; i < 10;i++) {
			ipanel[i] = new JPanel();
			final int k = i;
			ipanel[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					jcbrank.setSelectedIndex(k);
					
				}
			});
			allipanel.add(ipanel[i]);
		}
		try {
			r = new Render();
		}
		catch(Exception e) {e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error on input!");
		}
		//panel.add(new JScrollPane(ipanel));
		
		tmp = new JPanel();
		tmp.add(datapanel);
		
		//splitpane.setPreferredSize(new Dimension(1000,400));
		//splitpane.setOneTouchExpandable(true);
		//splitpane.setDividerLocation(500);
		
		//tmp.add(splitpane);
		panel.add(tmp);
		panel.add(skillpanel);
		

		//panel.add(xml= GUI.textEdit("soon your xml here in/out"));
		tmp = new JPanel();
		
		xml = new XMLView.XmlTextPane();
		xml.setDisabledTextColor(new Color(38,139,210));
		xml.setSelectionColor(new Color(38,139,210));
		//xml.setDisabledTextColor(new Color(38,139,210));
		//xml = new JTextArea();
		//xml.setEditable(false);
		JScrollPane jscp = new JScrollPane(xml);
		Dimension xd = jscp.getPreferredSize();
		xd.width = 700;
		xd.height = 200;
		jscp.setPreferredSize(xd);
		tmp.add(jscp);
		

		updateIMG();
		
		tmp.add(GUI.buttonSync("Copy", () -> {
			String get = xml.getText();
			StringSelection selec = new StringSelection(get);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selec, selec);
		}));
		panel.add(tmp);

		tmp.add(GUI.buttonSync("Load", () -> {
			int CARD_SECTIONS_COUNT=0;
			List<String> lines = new ArrayList<String>();
			String get = xml.getText();
		    String line = null;

	        try {
			File dir = new File("data/");
			for (File f : dir.listFiles()) {
				if (f.getName().matches("cards_section_\\d+.xml") && GlobalData.readFile(f.getAbsolutePath()).length() > 10)
					CARD_SECTIONS_COUNT++;
			}
			File f1 = new File("data/cards_section_"+CARD_SECTIONS_COUNT + ".xml");
			FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (line.contains("</root>"))
                    line = line.replace("</root>", get + "</root>");
                lines.add(line);
            }
            fr.close();
            br.close();
            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines)
                 out.write(s + "\n");
            out.flush();
            out.close();
	        GlobalData.init();
	        load.setText(""+did.getNumber());
	        loadCard();
	        }catch (Exception e) {
	        	TUR.log.e("XML write crashed, maybe manula fix needed", "load from xml");
	        	e.printStackTrace();
	        }
			
		}));
		panel.add(tmp);
		
		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panel,new JScrollPane(allipanel));
		Dimension d = new Dimension(1500,750);//Toolkit.getDefaultToolkit().getScreenSize();
		//d.width = d.width - 50;
		//d.height = d.height - 100;
		splitpane.setPreferredSize(d);
		splitpane.setOneTouchExpandable(true);
		splitpane.setDividerLocation(1000);
		add(splitpane);
		d.width = d.width + 10;
		d.height = d.height + 10;
		this.setPreferredSize(d);
		jcbrank.addItemListener((e) -> {
			int prev = (Integer)e.getItem();
			int next = (Integer)jcbrank.getSelectedItem();
			if(prev != next) {
				CardInstance cci = cis[next-1];	
				if(cci==null) {
					cis[next-1] = cis[prev-1];
				}
				else {
					loadFromCI(cci);
				}
				Task.start(()->updateIMG());
			}
		});
		
		
		jcbmaxrank.addItemListener((e) -> Task.start(()->updateIMG()));	
		jcblevel.addItemListener((e) -> Task.start(()->updateIMG()));
		jcbrarity.addItemListener((e) -> Task.start(()->updateIMG()));
		jcbtype.addItemListener((e) -> Task.start(()->updateIMG()));
		jcbfaction.addItemListener((e) -> Task.start(()->updateIMG()));
		DocumentListener dl = new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  	//Task.start(()->updateIMG());
				  	//System.out.println("ch");
				  }
				  public void removeUpdate(DocumentEvent e) {
					  //Task.start(()->updateIMG());
					  	//System.out.println("rm");
				  }
				  public void insertUpdate(DocumentEvent e) {
					  Task.start(()->updateIMG());
					  	//System.out.println("in");
				  }
		};
		imgfile.getDocument().addDocumentListener(dl);
		dataname.getDocument().addDocumentListener(dl);
		did.getDocument().addDocumentListener(dl);
		dhealth.getDocument().addDocumentListener(dl);
		ddelay.getDocument().addDocumentListener(dl);
		dattack.getDocument().addDocumentListener(dl);
		
		for(int k = 0 ; k < 3;k++)
		{
			sid[k].getDocument().addDocumentListener(dl);
			all[k].addChangeListener((e)-> Task.start(()->updateIMG()));
			y[k].getDocument().addDocumentListener(dl);
			x[k].getDocument().addDocumentListener(dl);
			n[k].getDocument().addDocumentListener(dl);
			c[k].getDocument().addDocumentListener(dl);

			trigger[k].getDocument().addDocumentListener(dl);

			s1[k].getDocument().addDocumentListener(dl);
			s2[k].getDocument().addDocumentListener(dl);
			id[k].getDocument().addDocumentListener(dl);
			txt[k].getDocument().addDocumentListener(dl);
		}	
	}
	
	public void clear()
	{
		for(int i = 0 ; i  < cis.length;++i) {
			if(i!=(Integer)jcbrank.getSelectedItem()-1) {
				cis[i]=null;
				ipanel[i].removeAll();
			}
		}
		this.updateUI();
		
	}
	
	public CardInstance  getLoadCI()
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
				return null;
			}
		}
		return ci;
	}
	
	public synchronized void loadCard()
	{
		CardInstance ci = getLoadCI();
		if(ci == null) return;
		block_img_update = true;
		Card c = ci.getCard();
		int[] arr = c.getIDs();
		for(int i=0;i<arr.length-1;i++)
			loadFromCI(GlobalData.getCardInstanceById(arr[i]));
		loadFromCI(GlobalData.getCardInstanceById(arr[arr.length-1]));
		block_img_update = false;
	}
	
	public void loadCI()
	{
		CardInstance ci = getLoadCI();
		if(ci == null) return;
		
		loadFromCI(ci);
	}
	
	public synchronized void loadFromCI(CardInstance ci)
	{
		boolean pre_tmp = block_img_update;
		block_img_update = true;
		
		dataname.setText(ci.getCard().getName());
		did.setNumber(ci.getCard().getLowestID());
		dhealth.setNumber(ci.getHealth());
		dattack.setNumber(ci.getAttack());
		ddelay.setNumber(ci.getCost());
		
		jcbrarity.setSelectedItem(Rarity.get(ci.getRarity()));
		jcbfaction.setSelectedItem(Faction.get(ci.getFaction()));
		jcblevel.setSelectedItem(Level.get(ci.getFusionLevel()));
		if((Integer)jcbrank.getSelectedItem()!=ci.getLevel())jcbrank.setSelectedItem(ci.getLevel());
		jcbmaxrank.setSelectedItem(ci.getIDs().length);
		jcbtype.setSelectedItem(CardType.getByID(ci.getID()));
		
		SkillSpec[] ss = ci.getSkills();
		for(int k = 0; k < ss.length && k < 3;k++)
		{
			sid[k].setText(ss[k].getId());
			x[k].setNumber(ss[k].getX());
			y[k].setText(ss[k].getY());
			n[k].setNumber(ss[k].getN());
			c[k].setNumber(ss[k].getC());
			s1[k].setText(ss[k].getS());
			s2[k].setText(ss[k].getS2());
			all[k].setSelected(ss[k].isAll());
			id[k].setNumber(ss[k].getCard_id());
			trigger[k].setText(ss[k].getTrigger());
			txt[k].setText("");
		}
		for(int k = ss.length;k < 3;k++)sid[k].setText("no_skill");
		
		
		
		
		/*
		if(ss.length>0) {
		sid1.setText(ss[0].getId());
		x1.setNumber(ss[0].getX());
		y1.setText(ss[0].getY());
		n1.setNumber(ss[0].getN());
		c1.setNumber(ss[0].getC());
		s11.setText(ss[0].getS());
		s21.setText(ss[0].getS2());
		all1.setSelected(ss[0].isAll());
		id1.setNumber(ss[0].getCard_id());
		trigger1.setText(ss[0].getTrigger());
		}else {sid1.setText("no_skill");}
		if(ss.length>1) {
		sid2.setText(ss[1].getId());
		x2.setNumber(ss[1].getX());
		y2.setText(ss[1].getY());
		n2.setNumber(ss[1].getN());
		c2.setNumber(ss[1].getC());
		s12.setText(ss[1].getS());
		s22.setText(ss[1].getS2());
		all2.setSelected(ss[1].isAll());
		id2.setNumber(ss[1].getCard_id());
		trigger2.setText(ss[1].getTrigger());
		}else {sid2.setText("no_skill");}
		if(ss.length>2) {
		sid3.setText(ss[2].getId());
		x3.setNumber(ss[2].getX());
		y3.setText(ss[2].getY());
		n3.setNumber(ss[2].getN());
		c3.setNumber(ss[2].getC());
		s13.setText(ss[2].getS());
		s23.setText(ss[2].getS2());
		all3.setSelected(ss[2].isAll());
		id3.setNumber(ss[2].getCard_id());
		trigger3.setText(ss[2].getTrigger());
		}else {sid3.setText("no_skill");}
		
		*/
		
		cis[(Integer)jcbrank.getSelectedItem()-1] = ci;
		
		
		imgfile.setText("in.png");
		try {
		BufferedImage bi = Render.getCardImage(ci.getCard().getAssetBundle(), ci.getCard().getPicture());
		if(bi == null) bi = ImageIO.read(TU.class.getResourceAsStream("/cogs.png"));
		
			ImageIO.write(bi, "png",new File("in.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		block_img_update = pre_tmp;
		loadIMG(ci);
	}
	
	Object sync_flag = new Object();
	
	
	public void updateIMG()
	{
		if(block_img_update)return;
		synchronized(sync_flag) {
		try {
		SkillSpec[] ss = new SkillSpec[3];
		for(int  k = 0; k < ss.length;k++)
			ss[k] = new SkillSpec(sid[k].getText().toLowerCase(),x[k].getNumber(),y[k].getText().toLowerCase(),n[k].getNumber(),c[k].getNumber(),s1[k].getText().toLowerCase(),s2[k].getText().toLowerCase(),all[k].isSelected(),id[k].getNumber(),"".equals(trigger[k].getText())?"activate": trigger[k].getText().toLowerCase());
		//SkillSpec ss2 = new SkillSpec(sid2.getText().toLowerCase(),x2.getNumber(),y2.getText().toLowerCase(),n2.getNumber(),c2.getNumber(),s12.getText().toLowerCase(),s22.getText().toLowerCase(),all2.isSelected(),id2.getNumber(), trigger2.getText().toLowerCase());
		//SkillSpec ss3 = new SkillSpec(sid3.getText().toLowerCase(),x3.getNumber(),y3.getText().toLowerCase(),n3.getNumber(),c3.getNumber(),s13.getText().toLowerCase(),s23.getText().toLowerCase(),all3.isSelected(),id3.getNumber(), trigger3.getText().toLowerCase());
		Info i = new Info(dattack.getNumber(),dhealth.getNumber(),ddelay.getNumber(),((Level)jcblevel.getSelectedItem()).toInt(),ss);
		int mrank = (Integer)jcbmaxrank.getSelectedItem();
		int rank = (Integer)jcbrank.getSelectedItem();
		Info[] ia = new Info[mrank];
		int[] ids = new int[mrank];
		for(int j =0; j < ids.length;j++)ids[j]=did.getNumber()+j;
		//ids[rank-1] = 2; // TODO Define card id somewhere close to name
		ia[rank-1] = i;
		Card c = new Card(ids,dataname.getText(),((Rarity)jcbrarity.getSelectedItem()).toInt(),((Level)jcblevel.getSelectedItem()).toInt(),new int[] {},0,0,((Faction)jcbfaction.getSelectedItem()).toInt(),ia, "",0);
		CardInstance ci = CardInstance.get(did.getNumber()+rank-1,c,i);
		cis[rank-1] = ci;
		loadIMG(cis[rank-1]);
		//System.out.println(Data.getCardInstanceByNameAndLevel("Bulkhead Brawler-1").getSkills()[2].x);
		/*BufferedImage img = r.render(ci,new String[] {txt[0].getText(),txt[1].getText(),txt[2].getText()},imgfile.getText(),(CardType)jcbtype.getSelectedItem());
		cimg = img;
		ImageIcon iicon = new ImageIcon(img);
		ipanel.removeAll();
		ipanel2.removeAll();
		JLabel limg=new JLabel();
		limg.setIcon(iicon);
		ipanel.add(limg);
		for(int j : new int[] {id[0].getNumber(),id[1].getNumber(),id[2].getNumber()})
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
		this.updateUI(); */
		}catch(Exception e) {e.printStackTrace();
		JOptionPane.showMessageDialog(this, "Error on input!");
		}}
	}
	
	public int ilevel()
	{
		return (Integer)jcbrank.getSelectedItem()-1;
	}
	
	public void loadIMG(CardInstance ci) //{loadIMG(ci,false);}
	//public void loadIMG(CardInstance ci, boolean custom)
	{
		BufferedImage img = r.render(ci,new String[] {txt[0].getText(),txt[1].getText(),txt[2].getText()},imgfile.getText(),(CardType)jcbtype.getSelectedItem());
				
		cimg = img;
		ImageIcon iicon = new ImageIcon(img);
		
		ipanel[ilevel()].removeAll();
		JLabel limg=new JLabel();
		limg.setIcon(iicon);
		ipanel[ilevel()].add(limg);
		//if(bg == null)bg = ipanel[ilevel()].getBorder();
		for( int i = 0; i  < ipanel.length; i++)ipanel[i].setBorder(BorderFactory.createEmptyBorder());
		ipanel[ilevel()].setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
		recurse_summon(ci);
		updateCardXML();
		this.updateUI();
		//
	}
	Color bg = null;
	Color fg = Color.GRAY;
	public void updateCardXML()
	{
		int mrank = (Integer)jcbmaxrank.getSelectedItem();
		int[] ids = new int[mrank];
		Info[] infos = new Info[mrank];
		for(int i =0; i < mrank;++i) {
			if(cis[i]!=null)
			{
				ids[i] = cis[i].getID();
				infos[i] = cis[i].getInfo();
			}
			else
			{
				int rank = (Integer)jcbrank.getSelectedItem();
				infos[i] = cis[rank-1].getInfo();
				ids[i] = cis[rank-1].getID();
			}
		}
		Card pc = cis[0].getCard();
		Card c = new Card(ids,pc.getName(),pc.getRarity(),pc.getFusionLevel(),pc.getMaterials(),pc.getFortType(),pc.getSet(),pc.getFaction(), infos,pc.getPicture(), pc.getAssetBundle() );
		
		String s = xmlp.getCardXML(c);
		xml.setText(s);
	}
	
	public void recurse_summon(CardInstance ci)
	{
		for(SkillSpec ss : ci.getSkills()) {
			int j	 = ss.getCard_id();
			if(j != 0 && j != ci.getID()) {
			CardInstance cj = GlobalData.getCardInstanceById(j);
			if(cj.getID()!=0)
			{
				BufferedImage img = r.render(cj);
				ImageIcon iicon = new ImageIcon(img);
				JLabel limg=new JLabel();
				limg.setIcon(iicon);
				ipanel[ilevel()].add(limg);
				recurse_summon(cj);
			}
			else {
				TUR._this.log.w("Can't render non existing card", "Summon");
			}
			}
		}
	}
}
