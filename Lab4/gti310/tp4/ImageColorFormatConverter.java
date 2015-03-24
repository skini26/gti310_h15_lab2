package gti310.tp4;

public class ImageColorFormatConverter {

	public static int[][][] convertRGBtoYCbCr(int[][][] ppmImageRGB) {
		int[][][] yCbCrImage = new int[3][ppmImageRGB[0].length][ppmImageRGB[0][0].length];
		
		for(int i = 0; i < ppmImageRGB[0].length; i++) {
			for(int j = 0; j < ppmImageRGB[0][0].length; j++) {
				float r = ppmImageRGB[Main.R][i][j];
				float g = ppmImageRGB[Main.G][i][j];
				float b = ppmImageRGB[Main.B][i][j];
				
				double y  = (0.299 * r     + 0.587 * g    + 0.114 * b    + 0);
				double cb = (-0.168736 * r - 0.331264 * g + 0.5 * b      + 128);
				double cr = (0.5 * r       - 0.418688 * g - 0.081312 * b + 128);
				
				yCbCrImage[Main.Y][i][j] = (int) Math.round(y);
				yCbCrImage[Main.Cb][i][j] = (int) Math.round(cb);
				yCbCrImage[Main.Cr][i][j] = (int) Math.round(cr);
			}	
		}
		
		return yCbCrImage;
	}
	
	public static int[][][] convertYCbCrtoRGB(int[][][] yCbCrImage) {
		int[][][] ppmImageRGB = new int[3][yCbCrImage[0].length][yCbCrImage[0][0].length];
		
		for(int i = 0; i < ppmImageRGB[0].length; i++) {
			for(int j = 0; j < ppmImageRGB[0][0].length; j++) {
				int y = yCbCrImage[Main.Y][i][j];
				int cb = yCbCrImage[Main.Cb][i][j];
				int cr = yCbCrImage[Main.Cr][i][j];
				
				int r = (int) Math.round(((1 * y + 0 * (cb - 128)     + 1.4 * (cr - 128))));
				int g = (int) Math.round(((1 * y - 0.343 * (cb - 128) - 0.711 * (cr - 128))));
				int b = (int) Math.round(((1 * y + 1.765 * (cb - 128) + 0.0 * (cr - 128))));
				
				ppmImageRGB[Main.R][i][j] = r;
				ppmImageRGB[Main.G][i][j] = g;
				ppmImageRGB[Main.B][i][j] = b;
			}	
		}
		
		return ppmImageRGB;
	}

}
