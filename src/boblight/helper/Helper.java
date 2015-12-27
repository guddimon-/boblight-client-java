package boblight.helper;

import boblight.types.Color;

/**
 * Helper Class for color calculations.
 * @author Michael
 *
 */
public class Helper {
	public static class IndexColor {
		Integer i = 0;
		Color color = new Color(0, 0, 0);

		public IndexColor(int i, Color color) {
			this.i = i;
			this.color = color;
		}
		
		public Integer getI() {
			return this.i;
		}
		
		public Color getColor() {
			return this.color;
		}
	}

	public static Integer colorPlus(Integer color) {
		while (color >= 256)
			color = color - 256;
		return color;
	}

	public static Color colorPlus(Color color, Integer plus) {
		color.setColor(colorPlus(color.getRedAsInt() + plus), colorPlus(color.getGreenAsInt() + plus), colorPlus(color.getBlueAsInt() + plus));
		
		return color;
	}

}
