package boblight.client.actions;

import java.io.IOException;

import boblight.types.ActionThread;
import boblight.types.Boblight;
import boblight.types.Color;

public class ColorWheel2 extends ActionThread {
	private Double frequency1 = 0.9;
	private Double frequency2 = 0.9;
	private Double frequency3 = 0.9;
	private Float phase1 = (float) 0;
	private Float phase2 = (float) 4;
	private Float phase3 = (float) 8;

	public ColorWheel2(String host, Integer port) throws IOException {
		super(host, port);
	}

	public ColorWheel2(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		b.setSpeed(60);
//		b.setPriority(new Integer(inPriority.getText()));
		b.setPriority(1);
		b.setInterpolation(false);

		try {
			int currentLight = 0;
			int len = 10;
			int center = 128;
			int width = 255;
			boolean first = true;
			while (true) {
				for (int i = 0; i < len; ++i) {
					int red = (int) (Math.sin(frequency1 * i + phase1) * width + center);
					int grn = (int) (Math.sin(frequency2 * i + phase2) * width + center);
					int blu = (int) (Math.sin(frequency3 * i + phase3) * width + center);
					b.getLight().get(currentLight).getColor().setColor(new Color(red, grn, blu));
					currentLight++;
					if (currentLight >= b.getLightsCount()) {
						currentLight = 0;
						first = false;
					}
				}
				if (!first) {
					b.sendColor();
					Thread.sleep(100);
				}
			}
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}