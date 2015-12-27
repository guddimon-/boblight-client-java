package boblight.types;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

/**
 * This class is used for storing the information of each light.
 * @author Michael Gutmann
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Light", propOrder = {
    "name",
    "setManually",
    "color"
})
@XmlRootElement
public class Light {
	/*
	 * Create a logger
	 */
	private final static Logger log = Logger.getLogger(Light.class);
	
	/*
	 * Static strings used for logging
	 */
	private static String LOGNEWLIGHT		= "New light: {0} - hScan: [{1}, {2}] - vScan: [{3}, {4}]";
	private static String LOGSPEED			= "Set speed of light {0} to {1}";
	private static String LOGINTERPOLATION	= "Set interpolation of light {0} to {1}";
	
	/*
	 * Variables that contain our data
	 */
	@XmlAttribute(name="name", required=true)
	private String name				= null;
	private Double vScanFrom		= (double) 0;
	private Double vScanTo			= (double) 0;
	private Double hScanFrom		= (double) 0;
	private Double hScanTo			= (double) 0;
	private Double speed			= (double) 0;
	private Boolean interpolation	= false;
	@XmlElement(type=Color.class, name="color", required=true)
	private Color color				= new Color();
	@XmlAttribute(name="setManually", required=true)
	private Boolean setManually		= false;
	
	
	public Light() {
	}
	
	public Light(String name) {
		this.name = name;
	}
	
	/**
	 * Called by class Boblight to set initial values for fetched lights.
	 * @param name
	 * @param vSF
	 * @param vST
	 * @param hSF
	 * @param hST
	 */
	public Light(String name, String vSF, String vST, String hSF, String hST) {
		Object[] val = new Object[] {name, hSF, hST, vSF, vST};
		log.debug(MessageFormat.format(LOGNEWLIGHT, val));

		this.name = name;
		this.vScanFrom = Double.parseDouble(vSF);
		this.vScanTo = Double.parseDouble(vST);
		this.hScanFrom = Double.parseDouble(hSF);
		this.hScanTo = Double.parseDouble(hST);
	}
	
	public Light(String name, Boolean setManually, Color color, Double speed, Boolean interpolation) {
		this.name = name;
		this.setManually = setManually;
		this.color = color;
		this.speed = speed;
		this.interpolation = interpolation;
	}
	
	public void setSetManually(boolean setManually) {
		this.setManually = setManually;
	}

	/**
	 * Set speed to the given value (0 - 100).
	 * @param speed
	 */
	public void setSpeed(Double speed) {
		Object[] val = new Object[] {this.name, speed.toString()};
		log.debug(MessageFormat.format(LOGSPEED, val));

		this.speed = speed;
	}

	/**
	 * Returns the name of the light.
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the speed of the light.
	 * @return
	 */
	public Double getSpeed() {
		return this.speed;
	}

	/**
	 * Returns the color of the light.
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Returns the starting point of hScan of the light.
	 * @return
	 */
	public Double getHScanFrom() {
		return this.hScanFrom;
	}

	/**
	 * Returns the ending point of hScan of the light.
	 * @return
	 */
	public Double getHScanTo() {
		return this.hScanTo;
	}

	/**
	 * Returns the starting point of vScan of the light.
	 * @return
	 */
	public Double getVScanFrom() {
		return this.vScanFrom;
	}

	/**
	 * Returns the ending point of vscan of the light.
	 * @return
	 */
	public Double getVScanTo() {
		return this.vScanTo;
	}

	/**
	 * Returns interpolation of the light.
	 * @return
	 */
	public Boolean getInterpolation() {
		return this.interpolation;
	}

	/**
	 * Set interpolation of the light.
	 * @param interpolation
	 */
	public void setInterpolation(Boolean interpolation) {
		Object[] val = new Object[] {this.name, interpolation.toString()};
		log.debug(MessageFormat.format(LOGINTERPOLATION, val));

		this.interpolation = interpolation;
	}

	public Boolean getSetManually() {
		return this.setManually;
	}
}
