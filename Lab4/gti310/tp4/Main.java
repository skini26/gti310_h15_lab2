package gti310.tp4;

/**
 * The Main class is where the different functions are called to either encode
 * a PPM file to the Squeeze-Light format or to decode a Squeeze-Ligth image
 * into PPM format. It is the implementation of the simplified JPEG block 
 * diagrams.
 * 
 * @author François Caron
 */
public class Main {

	/*
	 * The entire application assumes that the blocks are 8x8 squares.
	 */
	public static final int BLOCK_SIZE = 8;
	
	/*
	 * The number of dimensions in the color spaces.
	 */
	public static final int COLOR_SPACE_SIZE = 3;
	
	/*
	 * The RGB color space.
	 */
	public static final int R = 0;
	public static final int G = 1;
	public static final int B = 2;
	
	/*
	 * The YUV color space.
	 */
	public static final int Y = 0;
	public static final int U = 1;
	public static final int V = 2;
	
	/*
	 * The YCbCr color space.
	 * Y is already defined in YUV
	 */
	public static final int Cb = 1;
	public static final int Cr = 2;
	
	/**
	 * The application's entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Squeeze Light Media Codec !");
		
		//Arguments
		
		//Case 1 : Encoding
		String fileToEncode = null;
		String encodedFile = null;

		//Case 2 : Decoding
		String fileToDecode;
		String decodedFile;
		
		int qualityFactor = 0;
		//To know if case 1 or 2
		boolean encoding = false;
		
		
		try{
			//arg 0 is the quality factor, if it is set, then it is encoding
			try{
				qualityFactor = Integer.parseInt(args[0]);
				fileToEncode= args[1];
				encodedFile = args[2];
				encoding = true;
			}
			//If quality factor is not an integer, then it is a decoding
			catch(NumberFormatException e){
				encoding = false;
				fileToDecode = args[0];
				decodedFile = args[1];
			}
		}
		//ArrayIndexOutOfBound
		catch(Exception e){
			System.err.println("Invalid arguments");
			System.exit(-1);
		}
		
		if(encoding){
			SZLEncoder encoder = new SZLEncoder();
			encoder.encode(fileToEncode, encodedFile, qualityFactor);
		}
		
	}
}
