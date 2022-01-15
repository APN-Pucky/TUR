package de.neuwirthinformatik.Alexander.TU.Render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.esotericsoftware.yamlbeans.YamlReader;

import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.Basic.SkillSpec;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.TU;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;
import de.neuwirthinformatik.Alexander.TU.util.Wget;

public class Render {
	Font optimus;
	Font arial;

	public Render() throws FontFormatException, IOException {
		optimus = Font.createFont(Font.TRUETYPE_FONT, TU.class.getResourceAsStream("/Optimus.otf"));
		arial = Font.createFont(Font.TRUETYPE_FONT, TU.class.getResourceAsStream("/arialbold.ttf"));
	}

	public BufferedImage renderTree(CardInstance c) {
		int offset_y = 10;
		int width = 1500;
		int height = 1150;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		HashMap<CardInstance, BufferedImage> hm = new HashMap<CardInstance, BufferedImage>();
		fillRenderTreeHashMap(hm, c);
		ArrayList<CardInstance> cur_cards = new ArrayList<CardInstance>();
		ArrayList<Integer> cards_per_level = new ArrayList<Integer>();
		cur_cards.add(c);
		int j = 0;// vertical
		while (cur_cards.size() > 0) {
			CardInstance[] cis = cur_cards.toArray(new CardInstance[] {});
			cur_cards.removeAll(Arrays.asList(cis));
			for (int i = 0; i < cis.length; i++) // horizontal
			{
				g.setColor(Color.red);
				g.fillRect((int) ((double) width * (i + 1)) / (cis.length + 1) - 80,
						(int) ((double) 300 * (j) + offset_y), 30, 30);
				g.drawImage(hm.get(cis[i]), (int) ((double) width * (i + 1)) / (cis.length + 1) - 80,
						(int) ((double) 300 * (j) + offset_y), Color.GREEN, null);
				cur_cards.addAll(Arrays.asList(cis[i].getMaterials()));
			}
			cards_per_level.add(j, cis.length);
			j++;
		}
		g.setColor(Color.RED);
		// TODO ARROWS
		// for(int i : cards_per_level) {System.out.println(i);}
		cur_cards.add(c);
		j = 0;
		while (cur_cards.size() > 0) {
			CardInstance[] cis = cur_cards.toArray(new CardInstance[] {});
			cur_cards.removeAll(Arrays.asList(cis));
			int ck = 0;
			for (int i = 0; i < cis.length; i++) // horizontal
			{
				// System.out.println("render " + cis[i].getName() + " @ " + (int)
				// ((double)width*(i+1))/(cis.length+1) +" "+(int)((double)250*(j)));
				// g.drawImage(hm.get(cis[i]), (int)
				// ((double)width*(i+1))/(cis.length+1),(int)((double)300*(j)),Color.GREEN,null);
				CardInstance[] cim = cis[i].getMaterials();
				if (cim.length > 0) {
					for (int k = 0; k < cim.length; k++) {
						// System.out.println((j+1) +": " + cards_per_level.get(j+1));
						drawArrowLine(g, (int) ((double) width * (ck + 1)) / (cards_per_level.get(j + 1) + 1),
								(int) ((double) 300 * (j + 1)) + offset_y,
								(int) ((double) width * (i + 1)) / (cis.length + 1),
								(int) ((double) 300 * (j) + 220 + offset_y), 10, 10);
						ck++;
					}
				}
				cur_cards.addAll(Arrays.asList(cis[i].getMaterials()));
			}
			// cards_per_level.add(j, cis.length);
			j++;
		}
		// TODO PLUSES
		return img;
	}

	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - d, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
		g.fillPolygon(xpoints, ypoints, 3);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.draw(new Line2D.Float(x1, y1, x2, y2));
	}

	public void fillRenderTreeHashMap(HashMap<CardInstance, BufferedImage> hm, CardInstance c) {
		if (!hm.containsKey(c)) {
			// System.out.println("Loaded " + c + " Hash: " + c.hashCode());
			// Test
			/*
			 * BufferedImage img = new BufferedImage(160,220, BufferedImage.TYPE_INT_ARGB);
			 * Graphics g = img.getGraphics(); g.setColor(Color.green); g.drawRect(0, 0,
			 * 160, 220); hm.put(c, img);
			 */
			hm.put(c, render(c));
			for (CardInstance ci : c.getMaterials()) {
				fillRenderTreeHashMap(hm, ci);
			}
		}
	}

	public BufferedImage render(CardInstance c) {
		return render(c, new String[] { "", "", "" }, "", null);
	}

	public BufferedImage render(CardInstance c, String[] txts, String file, CardType type) {
		BufferedImage img = new BufferedImage(160, 220, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		int[] style = GlobalData.style_borders[c.getFaction()][c.getRarity()];
		int[] frame = GlobalData.frame_borders[c.getFusionLevel()];
		int[] icon = null;
		String stype;
		if (type == null)
			stype = c.getUnitType().toLowerCase();
		else
			stype = type.toString().toLowerCase();
		icon = GlobalData.icon_borders.get("icon_" + stype + "_common");
		int[] costs = GlobalData.icon_borders.get("cost_container");
		Image bi = null;
		try {

			if (file == null || "".equals(file)) {
				bi = getCardImage(c.getCard().getAssetBundle(), c.getCard().getPicture());
			} else {
				bi = ImageIO.read(new File(file));
				bi = bi.getScaledInstance(150, 125, Image.SCALE_SMOOTH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} // TODO FINALLY RM FOLDER
		if (bi != null) {
			draw(g, bi, 0, 0, 150, 125, 5, 20, 150, 125);
		} else {
			draw(g, "cogs", 0, 0, 150, 125, 5, 20, 150, 125);
		}
		draw(g, "cardResources", style, new int[] { 0, 0, 160, 220 });
		draw(g, "cardResources", frame, new int[] { 0, 0, 160, 220 });
		draw(g, "cardResources", icon, new int[] { 2, 2, 24, 24 });
		drawLevel(g, c);
		g.setColor(Color.WHITE);
		if (c.getCost() > 0) {
			draw(g, "cardResources", costs, new int[] { 120, 26, 32, 32 });
			drawCenteredString(g, "" + c.getCost(), 136, 49, optimus.deriveFont(Font.PLAIN, 20));
		}
		if (stype.equals("assault")) {
			g.setFont(optimus.deriveFont(Font.PLAIN, 16));
			g.drawString("" + c.getAttack(), 24, 215);
		}
		drawRightAlignedString(g, "" + c.getHealth(), 136, 215, optimus.deriveFont(Font.PLAIN, 16));
		drawArialText(g, c.getCard().getName(), 35, 18, 120, arial.deriveFont(Font.BOLD, 12));
		drawArialText(g, StringUtil.capitalizeOnlyFirstLetters(GlobalData.factionToString(c.getFaction())), 10, 140,
				140, arial.deriveFont(Font.BOLD, 12));
		drawSkill(g, c, arial.deriveFont(Font.BOLD, 12), txts);
		return img;
	}

	public static void drawRightAlignedString(Graphics g, String text, int x, int y, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		x -= (metrics.stringWidth(text));
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public static void drawCenteredString(Graphics g, String text, int x, int y, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		x -= (metrics.stringWidth(text)) / 2;
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public static void drawSkill(Graphics g, CardInstance c, Font font) {
		drawSkill(g, c, font, new String[] { "", "", "" });
	}

	public static void drawSkill(Graphics g, CardInstance c, Font font, String[] custom_text) {
		SkillSpec[] ss = c.getSkills();
		for (int i = 0; i < ss.length; i++) {
			// System.out.println("Render: " +ss[i].id );
			int[] skill = GlobalData.skill_borders.get(ss[i].getId());
			if (skill != null) {
				draw(g, "skills0", skill, new int[] { 14, 148 + 16 * i, 16, 16 });
				if ("".equals(custom_text[i]))
					drawArialText(g, ss[i].text(), 32, 160 + 16 * i, 115, font);
				else
					drawArialText(g, custom_text[i], 32, 160 + 16 * i, 115, font);
			}
		}
	}

	public static void drawArialText(Graphics g, String str, int dx, int dy, int maxWidth, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		int wsIdX = 0, postDy;
		int x = 11;
		postDy = dy;
		boolean isLong = false;
		do {
			g.setFont(font.deriveFont(Font.PLAIN, x));
			x--;
		} while (g.getFontMetrics(g.getFont()).stringWidth(str) > maxWidth && x > 7);
		postDy = dy;
		if (x == 7) {
			// whitespace index.
			wsIdX = 0;
			isLong = true;
			int sl = (int) Math.floor(str.length() / 2);
			for (int i = 0; i < sl; i++) {
				// start searching from middle.
				if (str.charAt(sl - i) == ' ') {
					wsIdX = sl - i;
					break;
				} else if (str.charAt(sl + i + 1) == ' ') {
					wsIdX = sl + i + 1;
					break;
				}
			}
		} else {
			postDy += (x - 8) / 2;
		}
		if (isLong) {
			g.setFont(font.deriveFont(Font.BOLD, 9));
			g.drawString(str.substring(0, wsIdX), dx, postDy - 4);
			g.drawString(str.substring(wsIdX + 1, str.length()), dx, postDy + 4);
		} else {
			g.drawString(str, dx, postDy);
		}
	}

	public static void draw(Graphics g, String img, int[] s, int[] d) {
		draw(g, img, s[0], s[1], s[2], s[3], d[0], d[1], d[2], d[3]);
	}

	public static void draw(Graphics g, String img, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
		try {
			g.drawImage(ImageIO.read(TU.class.getResourceAsStream("/" + img + ".png")), dx, dy, dx + dw, dy + dh, sx,
					sy, sx + sw, sy + sh, null);
		} catch (IOException e) {
			System.out.println("" + img + ".png not found");
			e.printStackTrace();
		}
	}

	public static void draw(Graphics g, Image img, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
		g.drawImage(img, dx, dy, dx + dw, dy + dh, sx, sy, sx + sw, sy + sh, null);
	}

	public static void drawLevel(Graphics g, CardInstance ci) {
		int fused = (ci.getFusionLevel() > 0) ? 1 : 0;
		// max level
		int ml = ci.getCard().getIDs().length;
		// half level
		double hl = (ml > 6) ? Math.ceil(ml / 2) : ml;
		double x = Math.floor((160 - 11 * hl) / 2);
		int y = 205;
		int dxy = 11;

		int i = 0;
		for (i = 0; i < ml; i++) {
			// sort of linebreak at level hl + 1.
			if (i == hl) {
				x = Math.floor((160 - 11 * (ml - hl)) / 2);
				y -= dxy;
			}
			int filled = (i < ci.getLevel()) ? 1 : 0;
			// var path = "/root/icon[fused=" + fused + " and filled=" + filled +
			// "]/source[1]"
			int[] icon = GlobalData.icon_borders
					.get("icon_" + (fused > 0 ? "fused" : "unfused") + "_" + (filled > 0 ? "full" : "empty"));
			draw(g, "cardResources", icon, new int[] { (int) x, y, dxy, dxy });
			x += dxy;
		}
	}

	public static BufferedImage getCardImage(int card_bundle, String picture) {
		if (card_bundle == 0)
			return null;
		BufferedImage img = null;
		String card_name = picture.split("\\.")[0];
		System.out.println(card_name);
		int card_pack = card_bundle;
		String tmp_time = "" + System.currentTimeMillis();
		File f = new File("cardpack" + card_pack + "-" + tmp_time + "/");
		try {

			deleteDirectory(f);

			boolean success = f.mkdirs();
			if (!success)
				System.out.println("FOLDER CREATE ERROR");
			// TODO clear folder
			// String file = "cardpack" + card_pack + "/"+
			// "cardpack"+card_pack+"_Unity5_4_2_WebGL.unity3d";
			String file = getUnityCardPack(card_pack, tmp_time);

			/*
			 * ProcessBuilder builder = new ProcessBuilder("python3", "unitypucky.py",
			 * "--name", picture, "--outdir", f.toPath().toString(), file);
			 * builder.redirectError(ProcessBuilder.Redirect.INHERIT);
			 * builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			 * System.out.println("Exec extract"); Process p = builder.start();
			 */
			System.out.println(f.getAbsolutePath());
			System.out.println(
					"python3 unitypuck.py --name " + card_name + " --outdir " + f.toPath().toString() + " " + file);
			ProcessBuilder builder = new ProcessBuilder("python3", "unitypuck.py", "--name", card_name, "--outdir",
					f.toPath().toString(), file);
			// ProcessBuilder builder = new ProcessBuilder("unitypuck.exe", "--name",
			// card_name,
			// "--outdir", f.toPath().toString(), file);
			// builder.redirectError(ProcessBuilder.Redirect.INHERIT);
			// builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			System.out.println("Exec unitypucky.py");
			Process p = builder.start();
			StringBuilder byaml = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				byaml.append(line);
				byaml.append("\n");
			}
			p.waitFor();
			// System.out.println(byaml.toString());
			String yaml = byaml.toString();
			System.out.println("YAML:\n" + yaml);
			// yaml = yaml.replace("!PPtr", "m_ptr:");
			// yaml = yaml.replace("!!python/tuple", "m_tuple:");
			// yaml = yaml.toString().replaceAll("!unitypack:(.+?)(\\s)", "$1:$2");
			// yaml = yaml.replace("!", "");
			// System.out.println(yaml);
			/*
			 * int i = 0; String fyaml = ""; for (String l : yaml.split("\n")) { if
			 * (l.contains("spriteDefinitions")) { l = l.replace("spriteDefinitions",
			 * "spriteDefinitions" + i); i++; } fyaml += l + "\n"; } yaml = fyaml;
			 */
			if ("".equals(yaml) || yaml == null) {
				System.out.println("Install oyaml and unitypack(?) with:");
				System.out.println("$ pip install oyaml --user");
				System.out.println("$ pip install unitypack --user");
				/*
				 * ProcessBuilder bu = new ProcessBuilder("pip", "install", "oyaml", "--user");
				 * Process p1 = bu.start(); p1.waitFor(); bu = new ProcessBuilder("pip",
				 * "install", "unitypack", "--user"); p1 = bu.start(); p1.waitFor(); //
				 */
				System.out.println("then restart needed!");
			}
			YamlReader yr = new YamlReader(yaml);
			Object o = yr.read();
			Map map = (Map) o;
			/*
			 * ArrayList a_cont = (ArrayList) map.get("m_Container"); int[] id_map = new
			 * int[100]; int l =0; for(Object tuple : a_cont) { ArrayList a_info =
			 * (ArrayList)((Map)tuple).get("m_tuple"); //System.out.println(a_info); String
			 * name = a_info.get(0).toString(); //System.out.println("name: " +
			 * a_info.get(0)); if(name.contains("data/atlas0 material.mat")) {
			 * id_map[Integer.parseInt(((ArrayList)((Map)a_info.get(1)).get("asset")).get(0)
			 * .toString())]=l; l++; System.out.println("id: " +
			 * ((ArrayList)((Map)a_info.get(1)).get("asset")).get(0) + "=> " + (l-1)); }
			 * //System.out.println("id: " +
			 * ((ArrayList)((Map)a_info.get(1)).get("asset")).get(0)); }
			 */

			ArrayList<Integer> ids = new ArrayList<Integer>();
			double[][] s_d = new double[4][2];
			int s_id = 0;
			// for (int k = 0; k < i; k++) {
			ArrayList al = (ArrayList) map.get("spriteDefinitions"); // id4 => sprite1 => img2
																		// id2 => sprite2 => img0
																		// id3 => sprite0 => img1
			double[][] d_uvs = new double[4][2];
			for (Object s : al) {
				Map m = (Map) s;
				// for(Object k : m.keySet())System.out.println(k);
				int j = 0;
				ArrayList a_uvs = (ArrayList) m.get("uvs");
				for (Object o_uvs : a_uvs) {
					Map scale = (Map) o_uvs;
					d_uvs[j] = new double[] { Double.parseDouble(scale.get("x").toString()),
							Double.parseDouble(scale.get("y").toString()) };
					// System.out.println("x=" + scale.get("x") + " " + "y=" + scale.get("y"));
					j++;
				}
				ArrayList a_ptr = (ArrayList) m.get("material");
				int id = Integer.parseInt(a_ptr.get(0).toString());
				int id2 = Integer.parseInt(a_ptr.get(1).toString());
				id = id2 > id ? id2 : id;
				ids.add(id);
				// System.out.println(m.get("name").toString());
				// System.out.println(id);
				if (StringUtil.equalsIgnoreSpecial(m.get("name").toString(), card_name)) {
					s_d = d_uvs.clone();
					s_id = id;
				}
			}
			// }
			// if(s_id==0) {System.out.println("Error no name found");return;}

			// int atlas = id_map[s_id];
			int atlas = 0;
			// int atlas = toAtlasID(ids, s_id);
			System.out.println("s_id: " + s_id);
			System.out.println("Image: " + "atlas0" + atlas + ".png");
			Image all = ImageIO.read(new File(f.toPath().toString() + "/atlas0" + atlas + ".png"));
			img = new BufferedImage(150, 125, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.createGraphics();
			double[] bord = getBorderScale(all, s_d);
			// TODO image real size is 150 x 150
			// g.drawImage(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
			g.drawImage(all, 0, 0, 150, 125, (int) (bord[0] * all.getWidth(null)),
					(int) (bord[1] * all.getHeight(null))/* +50 */, (int) (bord[2] * all.getWidth(null)),
					(int) (bord[3] * all.getHeight(null))/* +50 */, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deleteDirectory(f);
		}
		return img;
	}

	private static int toAtlasID(ArrayList<Integer> ids, int s_id) {
		Collections.sort(ids, new Comparator<Integer>() {
			@Override
			public int compare(Integer i2, Integer i1) {
				// System.out.println(i2);
				// System.out.println(i1);
				return i2 - i1;
			}
		});
		int ret = 0;
		for (int i = 0; i < ids.size(); i++) {
			if (s_id == ids.get(i))
				return ret;
			else {
				if (i == 0 || (i > 0 && ids.get(i) != ids.get(i - 1)))
					ret++;
			}
		}
		return -1;
	}

	private static String getUnityCardPack(int num, String tmp) {
		String name = "";
		if (num < 1000)
			if (num < 119)
				name = "cardpack" + (num - 100) + "_Unity5_4_2_WebGL.unity3d";
			else if (num < 156)
				name = "cardpack" + (num - 101) + "_Unity5_4_2_WebGL.unity3d";
			else if (num < 209)
				name = "cardpack" + (num - 102) + "_Unity5_4_2_WebGL.unity3d";
			else if (num < 272)
				name = "cardpack" + (num - 101) + "_Unity5_4_2_WebGL.unity3d";
			else
				name = "cardpack" + (num - 100) + "_Unity5_4_2_WebGL.unity3d";
		else if (num < 2000)
			name = "oldcards" + (num - 1000) + "_Unity5_4_2_WebGL.unity3d";
		else if (num < 2500)
			name = "conquestmap" + (num - 2005) + "_Unity5_4_2_WebGL.unity3d";
		else if (num < 3000)
			name = "conquest_cards" + (num - 2500) + "_Unity5_4_2_WebGL.unity3d";
		else
			name = "dominion_and_shard_icons_Unity5_4_2_WebGL.unity3d";
		Wget.wGet("cardpack" + num + "-" + tmp + "/" + name,
				"http://cdn.synapse-games.com/unleashed/asset_bundles/5_4_2/" + name);
		return "cardpack" + num + "-" + tmp + "/" + name;
	}

	private static double[] getBorderScale(Image all, double[][] uvs) {
		double[] ret = new double[4];
		for (int i = 0; i < uvs.length; i++) {
			if (ret[0] == 0 || ret[0] > uvs[i][0])
				ret[0] = uvs[i][0];
			if (ret[1] == 0 || ret[1] > 1 - uvs[i][1])
				ret[1] = 1 - uvs[i][1];
			if (ret[2] == 0 || ret[2] < uvs[i][0])
				ret[2] = uvs[i][0];
			if (ret[3] == 0 || ret[3] < 1 - uvs[i][1])
				ret[3] = 1 - uvs[i][1];
		}
		return ret;
	}

	public static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}
}
