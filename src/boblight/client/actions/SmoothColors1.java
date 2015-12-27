package boblight.client.actions;

import java.io.IOException;

import boblight.types.ActionThread;
import boblight.types.Boblight;

public class SmoothColors1 extends ActionThread {
	
	public SmoothColors1(String host, Integer port) throws IOException {
		super(host, port);
		// TODO Auto-generated constructor stub
	}
	
	public SmoothColors1(Boblight b) {
		super(b);
	}

	@Override
	protected void action() throws IOException {
		try {
			b.setSpeed(0);
	    	b.setPriority(1);
	    	b.setInterpolation(true);

			while(!interrupted()) {
		        b.setColor("FF0000");
		        sleep(10000);
	
		    	b.setColor("00FF00");
		    	sleep(10000);
	
		    	b.setColor("0000FF");
		    	sleep(10000);
			}
		} catch (InterruptedException e) {
			b.disconnect();
		}
	}
}
