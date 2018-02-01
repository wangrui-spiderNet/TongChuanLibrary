package alpha.cyber.intellib.utils;

import android.graphics.Bitmap;

public class BitmapConverter {

	/**
	 * Get grayscale data from argb image to byte array
	 */
	public static byte[] ARGB2Gray(Bitmap img) {

		int width = img.getWidth();
		int height = img.getHeight();

		int[] pixels = new int[height * width];
		byte grayIm[] = new byte[height * width];

		img.getPixels(pixels, 0, width, 0, 0, width, height);

		int pixel = 0;
		int count = width * height;

		while (count-- > 0) {
			int inVal = pixels[pixel];

			// Get the pixel channel values from int
			double r = (double) ((inVal & 0x00ff0000) >> 16);
			double g = (double) ((inVal & 0x0000ff00) >> 8);
			double b = (double) (inVal & 0x000000ff);

			grayIm[pixel++] = (byte) (0.2989 * r + 0.5870 * g + 0.1140 * b);
		}

		return grayIm;
	}

	/**
	 * Create a gray scale bitmap from byte array
	 */
	public static Bitmap gray2ARGB(byte[] data, int width, int height) {
		int count = height * width;
		int[] outPix = new int[count];
		int pixel = 0;
		while (count-- > 0) {
			int val = data[pixel] & 0xff; // convert byte to unsigned
			outPix[pixel++] = 0xff000000 | val << 16 | val << 8 | val;
		}

		Bitmap out = Bitmap.createBitmap(outPix, 0, width, width, height, Bitmap.Config.ARGB_8888);
		return out;
	}

	public static byte[] extendGray(byte[] data) {
		int len = data.length;
		byte ret[] = new byte[len * 2];

		int j = 0;
		for (int i = 0; i < len; i++, j += 2) {
			ret[j] = (byte) (data[i] << 4);
			ret[j + 1] = (byte) (data[i] & 0xf0);
		}
		return ret;
	}

}
