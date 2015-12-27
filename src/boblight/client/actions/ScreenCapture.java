package boblight.client.actions;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import boblight.types.ActionThread;
import boblight.types.Boblight;
import boblight.types.Color;
import boblight.types.Light;

public class ScreenCapture extends ActionThread {
	public ScreenCapture(String host, Integer port) throws IOException {
		super(host, port);
	}

	public ScreenCapture(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		b.setSpeed(60);
		b.setPriority(1);
		
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		Rectangle screen = new Rectangle(0, 0, screenWidth, screenHeight);
		BufferedImage image = null;

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rgb;
		double red = 0;
		double green = 0;
		double blue = 0;
		double all = 0;
		
		int hBegin = 0;
		int hEnd = 0;
		int vBegin = 0;
		int vEnd = 0;
		
		while (!interrupted()) {
			try {
				image = robot.createScreenCapture(screen);
				for (Light l : b.getLight()) {
					red = 0;
					blue = 0;
					green = 0;
					all = 0;
					hBegin = l.getHScanFrom().intValue();
					hEnd = l.getHScanTo().intValue();
					vBegin = l.getVScanFrom().intValue();
					vEnd = l.getVScanTo().intValue();
					for (int i = screenWidth / 100 * hBegin; i < screenWidth / 100 * hEnd; i++) {
						for (int j = screenHeight / 100 * vBegin; j < screenHeight / 100 * vEnd; j++) {
//							rgb = robot.getPixelColor(i, j).getRGB();
							rgb = image.getRGB(i, j);
							red += (int) (rgb & 0x00ff0000) >> 16;
							green += (int) (rgb & 0x0000ff00) >> 8;
							blue += (int) rgb & 0x000000ff;
							all += 255;
						}
					}
					l.getColor().setColor(new Color((int) (red / all * 255), (int) (green
							/ all * 255), (int) (blue / all * 255)));
				}
				b.sendColor();
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
