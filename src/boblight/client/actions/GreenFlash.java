package boblight.client.actions;

import java.io.IOException;

import boblight.types.ActionThread;
import boblight.types.Boblight;

public class GreenFlash extends ActionThread {
	public GreenFlash(String host, Integer port) throws IOException {
		super(host, port);
	}
	
	public GreenFlash(Boblight b) {
		super(b);
	}


	@Override
	protected void action() throws IOException {
		try {
			b.setSpeed(20);
			b.setPriority(1);
			b.setInterpolation(false);

			b.setColor("010101");
			sleep(2500);

			b.setSpeed(100);
			for (int i = 0; i < 20; i++) {
				b.setColor("00FF00");
				sleep(1);
				b.setColor("000000");
				sleep(100);
			}

			b.setSpeed(100);
			b.setColor("000000");
			sleep(5000);
			b.disconnect();
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}
