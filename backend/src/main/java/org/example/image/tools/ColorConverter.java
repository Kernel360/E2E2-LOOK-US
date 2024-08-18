package org.example.image.tools;

public class ColorConverter {

	// CIE XYZ tristiumulus values of the reference white D65
	private static final float[] STANDARD_TRISTIMULUS = {95.047f, 100.000f, 108.883f};

	/**
	 * RGB -> CIE-LAB.
	 * @param red Red coefficient. Values in the range [0..255].
	 * @param green Green coefficient. Values in the range [0..255].
	 * @param blue Blue coefficient. Values in the range [0..255].
	 * @return CIE-LAB color space.
	 */
	// float[] tristimulus param maybe need if more accurate converter for specific environment or lighting condition
	public static float[] RGBtoLAB(int red, int green, int blue){
		float[] xyz = RGBtoXYZ(red, green, blue);
		float[] lab = XYZtoLAB(xyz[0], xyz[1], xyz[2], STANDARD_TRISTIMULUS);

		return lab;
	}
//
	/**
	 * CIE-LAB -> RGB.
	 * @param l L coefficient.
	 * @param a A coefficient.
	 * @param b B coefficient.
	 * @return RGB color space.
	 */
	// float[] tristimulus param maybe need if more accurate converter for specific environment or lighting condition
	public static int[] LABtoRGB(float l, float a, float b){
		float[] xyz = LABtoXYZ(l, a, b, STANDARD_TRISTIMULUS);
		return XYZtoRGB(xyz[0], xyz[1], xyz[2]);
	}

	/**
	 * XYZ -> CIE-LAB.
	 * @param x X coefficient.
	 * @param y Y coefficient.
	 * @param z Z coefficient.
	 * @param tristimulus XYZ Tristimulus.
	 * @return CIE-LAB color space.
	 */
	public static float[] XYZtoLAB(float x, float y, float z, float[] tristimulus){
		float[] lab = new float[3];

		x /= tristimulus[0];
		y /= tristimulus[1];
		z /= tristimulus[2];

		if (x > 0.008856)
			x = (float)Math.pow(x,0.33f);
		else
			x = (7.787f * x) + ( 0.1379310344827586f );

		if (y > 0.008856)
			y = (float)Math.pow(y,0.33f);
		else
			y = (7.787f * y) + ( 0.1379310344827586f );

		if (z > 0.008856)
			z = (float)Math.pow(z,0.33f);
		else
			z = (7.787f * z) + ( 0.1379310344827586f );

		lab[0] = ( 116 * y ) - 16;
		lab[1] = 500 * ( x - y );
		lab[2] = 200 * ( y - z );

		return lab;
	}

	/**
	 * CIE-LAB -> XYZ.
	 * @param l L coefficient.
	 * @param a A coefficient.
	 * @param b B coefficient.
	 * @param tristimulus XYZ Tristimulus.
	 * @return XYZ color space.
	 */
	public static float[] LABtoXYZ(float l, float a, float b, float[] tristimulus){
		float[] xyz = new float[3];

		float y = ( l + 16f ) / 116f;
		float x = a / 500f + y;
		float z = y - b / 200f;

		//Y
		if ( Math.pow(y,3) > 0.008856 )
			y = (float)Math.pow(y,3);
		else
			y = (float)(( y - 16 / 116 ) / 7.787);

		//X
		if ( Math.pow(x,3) > 0.008856 )
			x = (float)Math.pow(x,3);
		else
			x = (float)(( x - 16 / 116 ) / 7.787);

		// Z
		if ( Math.pow(z,3) > 0.008856 )
			z = (float)Math.pow(z,3);
		else
			z = (float)(( z - 16 / 116 ) / 7.787);

		xyz[0] = x * tristimulus[0];
		xyz[1] = y * tristimulus[1];
		xyz[2] = z * tristimulus[2];

		return xyz;
	}


	/**
	 * RGB -> XYZ
	 * @param red Red coefficient. Values in the range [0..255].
	 * @param green Green coefficient. Values in the range [0..255].
	 * @param blue Blue coefficient. Values in the range [0..255].
	 * @return XYZ color space.
	 */
	public static float[] RGBtoXYZ(int red, int green, int blue){
		float[] xyz = new float[3];

		float r = red / 255f;
		float g = green / 255f;
		float b = blue / 255f;

		//R
		if ( r > 0.04045)
			r = (float)Math.pow(( ( r + 0.055f ) / 1.055f ), 2.4f);
		else
			r /= 12.92f;

		//G
		if ( g > 0.04045)
			g = (float)Math.pow(( ( g + 0.055f ) / 1.055f ), 2.4f);
		else
			g /= 12.92f;

		//B
		if ( b > 0.04045)
			b = (float)Math.pow(( ( b + 0.055f ) / 1.055f ), 2.4f);
		else
			b /= 12.92f;

		r *= 100;
		g *= 100;
		b *= 100;

		float x = 0.412453f * r + 0.35758f * g + 0.180423f * b;
		float y = 0.212671f * r + 0.71516f * g + 0.072169f * b;
		float z = 0.019334f * r + 0.119193f * g + 0.950227f * b;

		xyz[0] = x;
		xyz[1] = y;
		xyz[2] = z;

		return xyz;
	}

	/**
	 * XYZ -> RGB
	 * @param x X coefficient.
	 * @param y Y coefficient.
	 * @param z Z coefficient.
	 * @return RGB color space.
	 */
	public static int[] XYZtoRGB(float x, float y, float z){
		int[] rgb = new int[3];

		x /= 100;
		y /= 100;
		z /= 100;

		float r = 3.240479f * x - 1.53715f * y - 0.498535f * z;
		float g = -0.969256f * x + 1.875991f * y + 0.041556f * z;
		float b = 0.055648f * x - 0.204043f * y + 1.057311f * z;

		if ( r > 0.0031308 )
			r = 1.055f * ( (float)Math.pow(r, 0.4166f) ) - 0.055f;
		else
			r = 12.92f * r;

		if ( g > 0.0031308 )
			g = 1.055f * ( (float)Math.pow(g, 0.4166f) ) - 0.055f;
		else
			g = 12.92f * g;

		if ( b > 0.0031308 )
			b = 1.055f * ( (float)Math.pow(b, 0.4166f) ) - 0.055f;
		else
			b = 12.92f * b;

		rgb[0] = (int)(r * 255);
		rgb[1] = (int)(g * 255);
		rgb[2] = (int)(b * 255);

		return rgb;
	}


}
