package boblight.client.actions;

import java.io.IOException;

import boblight.helper.Helper;
import boblight.types.ActionThread;
import boblight.types.Boblight;
import boblight.types.Color;
import boblight.types.Light;

public class ColorWheel3 extends ActionThread {
	public ColorWheel3(String host, Integer port) throws IOException {
		super(host, port);
	}

	public ColorWheel3(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		try {
			b.setSpeed(40);
//			b.setPriority(new Integer(inPriority.getText()));
			b.setPriority(1);
			b.setInterpolation(false);
	
			Color color = new Color(0, 85, 170);
			
			int i = 0;
	
			while (!interrupted()) {
				i = 0;
				for (Light l : b.getLight()) {
					l.getColor().setColor(Helper.colorPlus(color, i++));
					//System.out.println("r: " + color.getRed() + "g: " + color.getGreen() + "b: " + color.getBlue());
				}
				color = Helper.colorPlus(color, 5);
				
				b.sendColor();
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}