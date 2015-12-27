package boblight.client.actions;

import java.io.IOException;

import boblight.types.ActionThread;
import boblight.types.Boblight;

public class SmoothColors2 extends ActionThread {
	public SmoothColors2(String host, Integer port) throws IOException {
		super(host, port);
	}

	public SmoothColors2(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		try {
			b.setSpeed(0);
	    	b.setPriority(1);
	    	b.setInterpolation(true);
	
			while(!interrupted()) {
		        b.setColor("FFFF00");
		        sleep(10000);
		
		    	b.setColor("00FFFF");
		    	sleep(10000);
		
		    	b.setColor("FF00FF");
		    	sleep(10000);
			}
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}
