package boblight.client.actions;

import java.io.IOException;

import boblight.helper.Helper;
import boblight.types.ActionThread;
import boblight.types.Boblight;

public class ColorWheel1 extends ActionThread {
	public ColorWheel1(String host, Integer port) throws IOException {
		super(host, port);
	}

	public ColorWheel1(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		b.setSpeed(20);
//		b.setPriority(new Integer(inPriority.getText()));
		b.setPriority(1);
		b.setInterpolation(true);

		try {
			Integer color_r = 0;
			Integer color_g = 85;
			Integer color_b = 170;
			
			int i = 0;
	
			while (true) {
				for (i = 0; i < b.getLightsCount(); i++) {
					b.getLight().get(i).getColor().setColor(Helper.colorPlus(color_r + i),
							Helper.colorPlus(color_g + i), Helper.colorPlus(color_b
									+ i));
	
//					System.out.println("r: " + b.getLights().get(i).getColor().getRed()
//							+ " g: " + b.getLights().get(i).getColor().getGreen()
//							+ " b: " + b.getLights().get(i).getColor().getBlue());
				}
				color_r = Helper.colorPlus(color_r + 5);
				color_g = Helper.colorPlus(color_g + 5);
				color_b = Helper.colorPlus(color_b + 5);
	
				b.sendColor();
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}