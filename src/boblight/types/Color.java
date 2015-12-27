package boblight.types;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Color", propOrder = {
    "red",
    "green",
    "blue"
})
@XmlRootElement
public class Color {
	/*
	 * Create a logger
	 */
	private final static Logger log = Logger.getLogger(Color.class);

	/*
	 * Static strings used for logging
	 */
	private static String LOGSETCOLOR = "Set color to r: {0} g: {1} b: {2}";
	
	/*
	 * Variables that contain our data
	 */
	@XmlAttribute(name="red")
	private Double red		= (double) 0;
	@XmlAttribute(name="green")
	private Double green	= (double) 0;
	@XmlAttribute(name="blue")
	private Double blue		= (double) 0;

	/**
	 * Set color to the given value (0 - 1).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(double red, double green, double blue) {
		setColor(red, green, blue);
	}

	/**
	 * Set color to the given value.
	 * @param color
	 */
	public Color(Color color) {
		setColor(color);
	}

	/**
	 * Set color to the given value (0 - 255).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(int red, int green, int blue) {
		setColor(red, green, blue);
	}

	/**
	 * Set color to default values (0, 0, 0)
	 */
	public Color() {
		setColor(0, 0, 0);
	}
	/**
	 * Set color to the given value (0 - 1).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(double red, double green, double blue) {
		Object[] val = new Object[] {red, green, blue};
		log.debug(MessageFormat.format(LOGSETCOLOR, val));

		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * Set color to the given value.
	 * @param color
	 */
	public void setColor(Color color) {
		setColor(color.red, color.green, color.blue);
	}

	/**
	 * Set color to the given value (0 - 255).
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		setColor((double) red / 255, (double) green / 255,
				(double) blue / 255);
	}

	/**
	 * Set color to the given value (Hex: 000000 - FFFFFF).
	 * @param color
	 */
	public void setColor(String color) {
		Double red = (double) Integer.valueOf(color.substring(0, 2), 16) / 255;
		Double green = (double) Integer.valueOf(color.substring(2, 4), 16) / 255;
		Double blue = (double) Integer.valueOf(color.substring(4, 6), 16) / 255;
		setColor(red, green, blue);
	}

	/**
	 * Returns the value of red.
	 * @return
	 */
	public Double getRed() {
		return this.red;
	}

	/**
	 * Returns the value of green.
	 * @return
	 */
	public Double getGreen() {
		return this.green;
	}

	/**
	 * Returns the value of blue.
	 * @return
	 */
	public Double getBlue() {
		return this.blue;
	}

	/**
	 * Returns the value of red as integer.
	 * @return
	 */
	public Integer getRedAsInt() {
		return (int) (this.red * 255);
	}

	/**
	 * Returns the value of green as integer.
	 * @return
	 */
	public Integer getGreenAsInt() {
		return (int) (this.green * 255);
	}

	/**
	 * Returns the value of blue as integer.
	 * @return
	 */
	public Integer getBlueAsInt() {
		return (int) (this.blue * 255);
	}

	/**
	 * Returns the color as a string of integer values in the form "r: value_red g: value_green b: value_blue".
	 */
	@Override
	public String toString() {
		String s = "";
		s += "r: " + getRedAsInt();
		s += " g: " + getGreenAsInt();
		s += " b: " + getBlueAsInt();
		return s;
	}
}
