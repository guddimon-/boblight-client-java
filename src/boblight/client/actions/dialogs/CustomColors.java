package boblight.client.actions.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.xml.bind.JAXBException;

import boblight.helper.Helper;
import boblight.types.Boblight;
import boblight.types.Color;
import boblight.types.Light;

@SuppressWarnings("serial")
public class CustomColors extends JDialog {
	private Boblight b = new Boblight();
	private JDialog dialog = new JDialog();
	
	private String saveFile = "./lights.xml";

	
	
	private WindowListener closeWindow = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			try {
				b.serialize(saveFile);
				b.disconnect();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
		
	public CustomColors(String host, Integer port) throws IOException {
		b.connect(host, port);
		b.setSpeed(10);
		b.setInterpolation(true);
		calculateColors();
		try {
			b.deserialize(saveFile);
		} catch (JAXBException e) {
			//do nothing
		} catch (FileNotFoundException e) {
			// do nothing
		}
		b.sendColor();
		b.sendColor();
		b.setPriority(1);
	}
	
	public CustomColors(Boblight b) {
		this.b = b;
	}

	@Override
	public void show() {
		dialog.setTitle("Übersicht der einzelnen LED");
		dialog.setSize(1920, 1080);
		
		dialog.addWindowListener(closeWindow);
		
		for (int i=0; i<b.getLightsCount(); i++) {
			dialog.add(btnLED(i, b));
		}

//		JButton btnSave = new JButton("Speichern");
//		btnSave.setBounds(300, 200, 100, 30);
//		btnSave.addActionListener(saveLightsToXML);
//		dialog.add(btnSave);
//
//		JButton btnRead = new JButton("Lesen");
//		btnRead.setBounds(400, 200, 100, 30);
//		btnRead.addActionListener(readLightsToXML);
//		dialog.add(btnRead);

		/*
		 * Dirty hack to have no big window sized button in front of all others....
		 */
		JButton tmpButton1 = new JButton("");
		tmpButton1.setVisible(false);
		dialog.add(tmpButton1);

		setButtonBackground();
		dialog.setVisible(true);
	}

	private JButton btnLED(int i, final Boblight b) {
		final Light l = b.getLight().get(i);
		
		final JButton btn = new JButton();
		
		Integer x = (l.getHScanFrom().intValue() + Math.abs(l.getHScanFrom().intValue() - l.getHScanTo().intValue())/2) * dialog.getWidth() / 100 - 10;
		Integer y = (l.getVScanFrom().intValue() + Math.abs(l.getVScanFrom().intValue() - l.getVScanTo().intValue())/2) * dialog.getHeight() / 100 - 20;

		btn.setBounds(x, y, 25, 25);
//		System.out.println("LED: " + l.getName() + " x: " + x + " y: " + y);
//		System.out.println("x: " + btn.getX() + " y: " + btn.getY() + " Höhe: " + btn.getHeight() + " Breite: " + btn.getWidth());

		btn.setBackground(new java.awt.Color(0, 0, 0));
		btn.setToolTipText("LED " + l.getName());

		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				java.awt.Color c = JColorChooser.showDialog(null, "Farbe für LED " + l.getName(), new java.awt.Color(l.getColor().getRedAsInt(), l.getColor().getGreenAsInt(), l.getColor().getBlueAsInt()));
				if (c != null) {
					btn.setBackground(c);
					l.getColor().setColor(c.getRed(), c.getGreen(), c.getBlue());
					l.setSetManually(true);
				}
				else {
					btn.setBackground(new java.awt.Color(0, 0, 0));
					l.getColor().setColor(0, 0, 0);
					l.setSetManually(false);
				}
				calculateColors();
				b.sendColor();
			}
		});
		
		btn.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent arg0) {
				if (!l.getSetManually())
					btn.setBackground(new java.awt.Color(0, 0, 0));
			}
			
			public void mouseEntered(MouseEvent arg0) {
				if (!l.getSetManually())
					btn.setBackground(new java.awt.Color(l.getColor().getRedAsInt(), l.getColor().getGreenAsInt(), l.getColor().getBlueAsInt()));
			}
			
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		return btn;
	}

	protected void calculateColors() {
		ArrayList<Helper.IndexColor> tmp = (ArrayList<Helper.IndexColor>) new ArrayList<Helper.IndexColor>();
		Color defaultColor = new Color(1, 1, 1);
		
		int i = 0;
		for (i=0; i<b.getLightsCount(); i++) {
			if (b.getLight().get(i).getSetManually()) {
				tmp.add(new Helper.IndexColor(i, b.getLight().get(i).getColor()));
			}
			if (i == 0 && !b.getLight().get(i).getSetManually()) {
				b.getLight().get(i).getColor().setColor(defaultColor);
				tmp.add(new Helper.IndexColor(i, defaultColor));
			}
		}
		
//		System.out.println("tmp.size: " + tmp.size() + " tmp.get(tmp.size()-1).getI(): " + tmp.get(tmp.size()-1).getI() + " i: " + i);
		
		if (tmp.get(tmp.size()-1).getI() != i-1) {
			b.getLight().get(b.getLightsCount()-1).getColor().setColor(defaultColor);
			tmp.add(new Helper.IndexColor(b.getLightsCount()-1, defaultColor));
		}
		
//		System.out.println("tmp.size: " + tmp.size() + " tmp.get(tmp.size()-1).getI(): " + tmp.get(tmp.size()-1).getI() + " i: " + i);

		Integer tmpIndex = 0;
		
		for (Helper.IndexColor t : tmp) {
			Color cBegin = b.getLight().get(tmpIndex).getColor();
			Color cEnd = t.getColor();
			
//			System.out.println(cBegin.toString());
//			System.out.println(cEnd.toString());
			
			if ((t.getI() - tmpIndex) == 0) continue;
			Integer step_r = (cEnd.getRedAsInt() - cBegin.getRedAsInt()) / (t.getI() - tmpIndex);
			Integer step_g = (cEnd.getGreenAsInt() - cBegin.getGreenAsInt()) / (t.getI() - tmpIndex);
			Integer step_b = (cEnd.getBlueAsInt() - cBegin.getBlueAsInt()) / (t.getI() - tmpIndex);

//			System.out.println("Step r: " + step_r + " Step g: " + step_g + " Step b: " + step_b);
			
			Integer j = 0; 

			for (i = tmpIndex; i<t.getI(); i++) {
				b.getLight().get(i).getColor().setColor(cBegin.getRedAsInt() + j * step_r, cBegin.getGreenAsInt() + j * step_g, cBegin.getBlueAsInt() + j * step_b);
//				System.out.println(b.getLight().get(i).getColor().toString());
				j++;
			}
			
			tmpIndex = t.getI();
		}
	}

	protected void setButtonBackground() {
		JRootPane root = (JRootPane) dialog.getComponent(0);
		JLayeredPane layer = (JLayeredPane) root.getComponent(1);
		JPanel panel = (JPanel) layer.getComponent(0);
		
		for (int i=0; i<b.getLightsCount(); i++) {
			if (!b.getLight().get(i).getSetManually()) continue;
			JButton btn = (JButton) panel.getComponent(i);
			
			Color cLight = b.getLight().get(i).getColor();
			
			java.awt.Color cBtn = new java.awt.Color(cLight.getRedAsInt(), cLight.getGreenAsInt(), cLight.getBlueAsInt());
			
			btn.setBackground(cBtn);
		}
	}
}
