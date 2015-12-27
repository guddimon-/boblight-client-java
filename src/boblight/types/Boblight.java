package boblight.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

/**
 * This class is used for the communication to Boblight daemon and it stores information of the connected lights.
 * @author Michael Gutmann
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Boblight", propOrder = {
    "light"
})
@XmlRootElement
public class Boblight {
	/*
	 * Create a logger
	 */
	private final static Logger log = Logger.getLogger(Boblight.class);
	
	/*
	 * Static strings used for logging
	 */
	private static String LOGCONNECT				= "Connect to server: {0}:{1}";
	private static String LOGGETLIGHTS				= "Get lights from server";
	private static String LOGFETCHEDLIGHTS			= "Got {0} lights from server";
	private static String LOGSENDPRIORITY			= "Set client priority to {0}";
	private static String LOGSENDCOLOR				= "Send color of each light to server";
	private static String LOGSETCOLOR				= "Set color of each light to r: {0} g: {1} b: {2}";
	private static String LOGDISCONNECT				= "Disconnect from server";
	private static String LOGSENDSPEED				= "Send speed of each light to server";
	private static String LOGSETSPEED				= "Set speed of each light to {0}";
	private static String LOGSETINTERPOLATION		= "Set interpolation of each light to {0}";
	private static String LOGSENDINTERPOLATION		= "Send interpolation of each light to server";
	private static String LOGSENDSYNC				= "Send sync to server";
	private static String LOGCLIENTUSED				= "Client is used by server";
	private static String LOGCLIENTNOTUSED			= "Client is not used by server";
	private static String XMLNOTVALID_COUNTOFLIGHTS	= "Count of lights read from server({0}) not equal to count of lights read from XML ({1})";
	
	/*
	 * static strings used for communication with server
	 */
	private static String HELLO					= "hello";
	private static String GETLIGHTS				= "get lights";
	private static String SETPRIORITY			= "set priority {0}";
	private static String SETLIGHTRGB			= "set light {0} rgb {1} {2} {3}";
	private static String SETLIGHTSPEED			= "set light {0} speed {1}";
	private static String SETLIGHTINTERPOLATION	= "set light {0} interpolation {1}";
	private static String SYNC					= "sync";
	private static String PING					= "ping";
	private static String SPLITTER				= " ";

	/*
	 * Variables for communication with server
	 */
	private Socket socket					= null;
	private PrintWriter sendToServer		= null;
	private BufferedReader readFromServer	= null;

	/*
	 * Variables that contain our data
	 */
	@XmlElementWrapper(name="lights")
	@XmlElement(type=Light.class, name="light")
	private ArrayList<Light> light	= (ArrayList<Light>) new ArrayList<Light>();
	private Integer priority		= 0;

	/**
	 * Creates a new object without any further initialization.
	 */
	public Boblight() {
	}

	/**
	 * Creates a new object and connects to the given server with the given priority.
	 * After the connection was opened, the light information is fetched.
	 * 
	 * @param host Hostname or IP address of Boblight daemon.
	 * @param port Port of Boblight daemon.
	 * @param priority Priority of the client (0 - 254).
	 * @throws IOException
	 */
	public Boblight(String host, int port, int priority) throws IOException {
		connect(host, port);
		setPriority(priority);
	}

	private int checkPriority(int priority) {
		if (priority < 0) priority = 0;
		if (priority > 254) priority = 254;
		return priority;
	}

	/**
	 * Connects to the given server and fetches the light information.
	 * 
	 * @param host Hostname or IP address of Boblight daemon.
	 * @param port Port of Boblight daemon.
	 * @throws IOException
	 */
	public void connect(String host, Integer port) throws IOException {
		Object[] val = new Object[] {host, port.toString()};
		String l = MessageFormat.format(LOGCONNECT, val);
		log.debug(l);

		this.socket = new Socket(host, port);
		this.sendToServer = new PrintWriter(this.socket.getOutputStream(), true);
		this.readFromServer = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));

		this.sendToServer.println(HELLO);
		String s = this.readFromServer.readLine();
		
		log.debug(s);

		getLightsFromServer();
	}

	private void getLightsFromServer() throws IOException {
		log.info(LOGGETLIGHTS);
		
		this.sendToServer.println(GETLIGHTS);
		this.readFromServer.readLine();

		String line = null;
		String[] lineSplit = null;

		while (readFromServer.ready()) {
			line = this.readFromServer.readLine();
			lineSplit = line.split(SPLITTER);

			log.debug(line);
			this.light.add(new Light(lineSplit[1], lineSplit[3], lineSplit[4], lineSplit[5], lineSplit[6]));
		}
		
		log.info(MessageFormat.format(LOGFETCHEDLIGHTS, getLightsCount()));
	}

	/**
	 * Change client priority to the given value.
	 * @param priority Priority of the client (0 - 254).
	 */
	public void setPriority(int priority) {
		this.priority = checkPriority(priority);
		sendPriority();
	}

	private void sendPriority() {
		log.info(MessageFormat.format(LOGSENDPRIORITY, priority));
		
		String s = MessageFormat.format(SETPRIORITY, this.priority);
		this.sendToServer.println(s);
		
		log.debug(s);
	}

	/**
	 * Send each lights color to the connected Boblight daemon.
	 * After that synchronization with server is initiated.
	 */
	public void sendColor() {
		log.info(LOGSENDCOLOR);
		
		String s = null;
		Object[] val = null;

		for (Light l : this.light) {
			val = new Object[] {
					l.getName(),
					l.getColor().getRed(),
					l.getColor().getGreen(),
					l.getColor().getBlue()
			};
			s = MessageFormat.format(SETLIGHTRGB, val);
			this.sendToServer.println(s);
			
			log.debug(s);
		}
		sync();
	}

	/**
	 * Set color for all lights to the given value (0 - 1) and sends them to the Boblight daemon.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(double red, double green, double blue) {
		Object[] val = new Object[] {red, green, blue};
		log.info(MessageFormat.format(LOGSETCOLOR, val));
		
		for (Light l : light) {
			l.getColor().setColor(checkColor(red), checkColor(green), checkColor(blue));
		}
		sendColor();
	}

	private double checkColor(double color) {
		if (color < 0) color = 0;
		if (color > 1) color = 1;
		return color;
	}

	private int checkColor(int color) {
		if (color < 0) color = 0;
		if (color > 255) color = 255;
		return color;
	}

	/**
	 * Set color for all lights to the given value (0 - 255) and sends them to the Boblight daemon.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		Object[] val = new Object[] {red, green, blue};
		log.info(MessageFormat.format(LOGSETCOLOR, val));
		
		for (Light l : light) {
			l.getColor().setColor(checkColor(red), checkColor(green), checkColor(blue));
		}
		sendColor();
	}

	/**
	 * Set color for all lights to the given value (Hex: 000000 - FFFFFF) and sends them to the Boblight daemon.
	 * @param color
	 */
	public void setColor(String color) {
		double red = (double) Integer.valueOf(color.substring(0, 2), 16) / 255;
		double green = (double) Integer.valueOf(color.substring(2, 4), 16) / 255;
		double blue = (double) Integer.valueOf(color.substring(4, 6), 16) / 255;
		setColor(red, green, blue);
	}

	/**
	 * Disconnects the client from server.
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		log.info(LOGDISCONNECT);
		this.sendToServer.close();
		this.readFromServer.close();
		this.socket.close();
	}

	private void sendSpeed() {
		log.info(LOGSENDSPEED);
		
		String s = null;
		Object[] val = null;

		for (Light l : this.light) {
			val = new Object[] {
					l.getName(),
					l.getSpeed().toString()
			};
			s = MessageFormat.format(SETLIGHTSPEED, val);
			this.sendToServer.println(s);
			
			log.debug(s);
		}
	}

	/**
	 * Set speed for all lights to the given value (0 - 100) and sends it to the Boblight daemon.
	 * @param speed
	 */
	public void setSpeed(double speed) {
		log.info(MessageFormat.format(LOGSETSPEED, speed));

		for (Light l : this.light) {
			l.setSpeed(checkSpeed(speed));
		}
		sendSpeed();
	}

	private double checkSpeed(double speed) {
		if (speed < 0) speed = 0;
		if (speed > 100) speed = 100;
		return speed;
	}

	/**
	 * Set interpolation for all lights to the given value and sends it to the Boblight daemon.
	 * @param interpolation
	 */
	public void setInterpolation(Boolean interpolation) {
		log.info(MessageFormat.format(LOGSETINTERPOLATION, interpolation));
		
		for (Light l : this.light) {
			l.setInterpolation(interpolation);
		}
		sendInterpolation();
	}

	private void sendInterpolation() {
		log.info(LOGSENDINTERPOLATION);

		String s = null;
		Object[] val = null;

		for (Light l : this.light) {
			val = new Object[] {
					l.getName(),
					l.getInterpolation().toString()
			};
			s = MessageFormat.format(SETLIGHTINTERPOLATION, val);
			this.sendToServer.println(s);
			
			log.debug(s);
		}
	}

	/**
	 * Send sync to Boblight daemon to tell that data are ready to be read.
	 */
	public void sync() {
		log.info(LOGSENDSYNC);
		this.sendToServer.println(SYNC);
	}

	/**
	 * Returns the amount of lights the server has.
	 * @return
	 */
	public Integer getLightsCount() {
		return light.size();
	}

	/**
	 * Returns the list with all lights.
	 * @return
	 */
	public ArrayList<Light> getLight() {
		return light;
	}

	/**
	 * Ask server if the output of this client is used.
	 * @return
	 * true: Output is used<br />
	 * false: Output is not used
	 * @throws IOException
	 */
	public Boolean ping() throws IOException {
		Boolean isUsed = false;
		String s = null;
		this.sendToServer.println(PING);
		s = this.readFromServer.readLine();
		
		if (s == "ping 1"){
			isUsed = true;
			log.info(LOGCLIENTUSED);
		}
		else {
			isUsed = false;
			log.info(LOGCLIENTNOTUSED);
		}
		
		return isUsed;
	}
	
	public void serialize(String saveFile) throws Exception {
		FileOutputStream file = new FileOutputStream(saveFile);

		JAXBContext jaxbContext = JAXBContext.newInstance(Boblight.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, file);
	}
	
	public void deserialize(String saveFile) throws FileNotFoundException, JAXBException {
		File file = new File(saveFile);
        JAXBContext jaxbContext = JAXBContext.newInstance(Boblight.class);
        
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Boblight b = (Boblight) jaxbUnmarshaller.unmarshal(file);

        if (this.getLightsCount() == b.getLightsCount()) {
	        for (int i=0; i<this.getLightsCount(); i++) {
	        	this.getLight().get(i).setInterpolation(b.getLight().get(i).getInterpolation());
	        	this.getLight().get(i).setSpeed(b.getLight().get(i).getSpeed());
	        	this.getLight().get(i).setSetManually(b.getLight().get(i).getSetManually());
	        	this.getLight().get(i).getColor().setColor(b.getLight().get(i).getColor());
	        }
        }
        else {
    		Object[] val = null;

    		val = new Object[] {
					this.getLightsCount(),
					b.getLightsCount()
			};
    		
			log.warn(MessageFormat.format(XMLNOTVALID_COUNTOFLIGHTS, val));
        }
	}
}