package boblight.client.actions.dialogs;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boblight.types.Boblight;

@SuppressWarnings("serial")
public class ColorChooser extends JDialog implements ChangeListener {
	private JColorChooser colorChooser = null;
	private Boblight b = new Boblight();
	
	public ColorChooser(String host, Integer port) throws IOException {
		b.connect(host, port);
		b.setSpeed(10);
		b.setPriority(1);
		b.setInterpolation(false);
	}
	
	public ColorChooser(Boblight b) {
		this.b = b;
	}

	public void show() {
		JDialog dialog = new JDialog();
		
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);

		dialog.setTitle("Farbauswahl");
		dialog.setSize(450, 300);
		
		dialog.getContentPane().add(colorChooser);

		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					b.disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	public void stateChanged(ChangeEvent arg0) {
		Color newColor = colorChooser.getColor();

		b.setSpeed(40);
		b.setColor(newColor.getRed(), newColor.getGreen(), newColor.getBlue());
	}
}