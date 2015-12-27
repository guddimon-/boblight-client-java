package boblight.types;

import java.io.IOException;

abstract public class ActionThread extends Thread {
	protected Boblight b = new Boblight();

	@Override
	public void run() {
		try {
			action();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ActionThread(String host, Integer port) throws IOException {
		b.connect(host, port);
	}

	public ActionThread(Boblight b) {
		this.b = b;
	}

	abstract protected void action() throws IOException;
}
