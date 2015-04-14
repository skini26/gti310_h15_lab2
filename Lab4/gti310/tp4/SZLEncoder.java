package gti310.tp4;

import java.util.ArrayList;
import java.util.List;

public class SZLEncoder {

	
	private final static int[][] qY = {
		{16, 40, 40, 40, 40, 40, 51, 61},
		{40, 40, 40, 40, 40, 58, 60, 55},
		{40, 40, 40, 40, 40, 57, 69, 56},
		{40, 40, 40, 40, 51, 87, 80, 62},
		{40, 40, 40, 56, 68, 109, 103, 77},
		{40, 40, 55, 64, 81, 104, 113, 92},
		{49, 64, 78, 87, 103, 121, 120, 101},
		{72, 92, 95, 98, 112, 100, 103, 95}
	};

	private final static int[][] qCbCr = {
		{17, 40, 40, 95, 95, 95, 95, 95},
		{40, 40, 40, 95, 95, 95, 95, 95},
		{40, 40, 40, 95, 95, 95, 95, 95},
		{40, 40, 95, 95, 95, 95, 95, 95},
		{95, 95, 95, 95, 95, 95, 95, 95},
		{95, 95, 95, 95, 95, 95, 95, 95},
		{95, 95, 95, 95, 95, 95, 95, 95},
		{95, 95, 95, 95, 95, 95, 95, 95}
     };

	
	
	
	
	public SZLEncoder(){
		
	}

	public void encode(String fileToEncode, String encodedFile,
			int qualityFactor) {
		
		//Lecture de l'image PPM
		PPMReaderWriter ppmReaderWriter = new PPMReaderWriter();
		int[][][] ppmImageRGB = ppmReaderWriter.readPPMFile(fileToEncode);
		
		
		//Convertir RGB 'a YCBR
		int[][][] ppmImageYCBR = ImageColorFormatConverter.convertRGBtoYCbCr(ppmImageRGB);
		int height = ppmImageYCBR[0][0][0];
		int width = ppmImageYCBR[1][0][0];
		
		
		//Decoupage en blocs Main.BLOCK_SIZE * Main.BLOCK_SIZE
		List<int[][][]> imageBlocksList = divideIntoBlocks(ppmImageYCBR);
		
		//DCT (sur chaque composante : Y Cb Cr)
		List<double[][]> yDCTBlocks = new ArrayList<double[][]>();
		List<double[][]> cbDCTBlocks = new ArrayList<double[][]>();
		List<double[][]> crDCTBlocks = new ArrayList<double[][]>();
		for(int[][][] imageBlock : imageBlocksList){
			double[][] yDCT = applyDCT(imageBlock[Main.Y]);
			yDCTBlocks.add(yDCT);
			double[][] cbDCT = applyDCT(imageBlock[Main.Cb]);
			yDCTBlocks.add(cbDCT);
			double[][] crDCT = applyDCT(imageBlock[Main.Cr]);
			yDCTBlocks.add(crDCT);
		}
		
		
		//Quantification
		List<int[][]> yQuantifiedBlocks = new ArrayList<int[][]>();
		List<int[][]> cbQuantifiedBlocks = new ArrayList<int[][]>();
		List<int[][]> crQuantifiedBlocks = new ArrayList<int[][]>();
		
		for(double[][] yDCTBlock : yDCTBlocks){
			int[][] quantifiedBlock = quantify(yDCTBlock, qualityFactor, Main.Y);
			yQuantifiedBlocks.add(quantifiedBlock);
		}
		for(double[][] cbDCTBlock : cbDCTBlocks){
			int[][] quantifiedBlock = quantify(cbDCTBlock, qualityFactor, Main.Cb);
			cbQuantifiedBlocks.add(quantifiedBlock);
		}
		for(double[][] crDCTBlock : crDCTBlocks){
			int[][] quantifiedBlock = quantify(crDCTBlock, qualityFactor, Main.Cr);
			crQuantifiedBlocks.add(quantifiedBlock);
		}
		
		//Zigzag 1) DPCM 2) RLC
		List<int[]> yZigZagBlocks = new ArrayList<int[]>();
		List<int[]> cbZigZagBlocks = new ArrayList<int[]>();
		List<int[]> crZigZagBlocks = new ArrayList<int[]>();
		
		for(int[][] yQuantifiedBlock : yQuantifiedBlocks){
			int[] zizagBlock = zigzag(yQuantifiedBlock);
			yZigZagBlocks.add(zizagBlock);
		}
		for(int[][] cbQuantifiedBlock : cbQuantifiedBlocks){
			int[] zizagBlock = zigzag(cbQuantifiedBlock);
			cbZigZagBlocks.add(zizagBlock);
		}
		for(int[][] crQuantifiedBlock : crQuantifiedBlocks){
			int[] zizagBlock = zigzag(crQuantifiedBlock);
			crZigZagBlocks.add(zizagBlock);
		}
		
		//Codage entropique
		Entropy.loadBitstream(Entropy.getBitstream());
		
		//Ecriture de l'image quasi-JPEG
		SZLReaderWriter.writeSZLFile(encodedFile, height, width, qualityFactor);
		
	}
	
	/**
	 * Divide an image in many Main.BLOCK_SIZExMain.BLOCK_SIZE images
	 * @param image
	 * @return list of Main.BLOCK_SIZExMain.BLOCK_SIZE images
	 * complexite : O(N^2)*O(8^2)
	 */
	private List<int[][][]> divideIntoBlocks(int[][][] image){
		List<int[][][]> blocks = new ArrayList<int[][][]>();

		 for (int i=0;i<(image[0].length/Main.BLOCK_SIZE);i++){
              for (int l=0;l<(image[0][i].length/Main.BLOCK_SIZE);l++){
        	 	
            	  int[][][] imageBloc = new int[Main.COLOR_SPACE_SIZE][Main.BLOCK_SIZE][Main.BLOCK_SIZE];
                   
            	  for (int j=0;j<Main.BLOCK_SIZE;j++)
                     
            		  for (int k=0;k<Main.BLOCK_SIZE;k++){
                         imageBloc[Main.Y][j][k]=image[Main.Y][j+i*Main.BLOCK_SIZE][k+l*Main.BLOCK_SIZE];
                         imageBloc[Main.Cb][j][k]=image[Main.Cb][j+i*Main.BLOCK_SIZE][k+l*Main.BLOCK_SIZE];
                         imageBloc[Main.Cr][j][k]=image[Main.Cr][j+i*Main.BLOCK_SIZE][k+l*Main.BLOCK_SIZE];
                     }
            	  
                 blocks.add(imageBloc);
                }
         }
		return blocks;
	}
	
	/**
	 * Applique la DCT sur un block et retourne le block transforme
	 * @param block
	 * @return block transforme
	 * complexite : O(N^4)
	 */
	private double[][] applyDCT(int[][] block){
	
		double[][] filteredBlock = new double[Main.BLOCK_SIZE][Main.BLOCK_SIZE];
		
		for(int u = 0; u < block.length; u++) {
			for(int v = 0; v < block.length; v++) {
				double somme = 0;
				for(int i = 0; i < block.length; i++) {
					for(int j = 0; j < block.length; j++) {
						double result = (Math.cos(((2 * i + 1) * u * Math.PI) / 16) * Math.cos(((2 * j + 1) * v * Math.PI) / 16));
						somme+= result * block[i][j]; 
					}	
				}
				
				filteredBlock[u][v] = Math.round(((coeff(u) * coeff(v)) / 4.F) * somme);
			}	
		} 
        return filteredBlock;

	}
	
	/**
	 * Calcule le coefficient
	 * @param a
	 * @return coefficient
	 */
	private double coeff(int a){
		if(a == 0){
			return (1 / Math.sqrt(2.0));
		}
		else{
			return 1;
		}
	}
	
	/**
	 * Quantifie un block
	 * @param yDCTBlock
	 * @param qualityFactor
	 * @param layer
	 * @return
	 * 
	 * complexite O(N ^2)
	 */
	public static int[][] quantify(double[][] yDCTBlock, int qualityFactor, int layer) {
		
		int[][] quantifiedImage = new int[yDCTBlock.length][yDCTBlock.length];
		
		for(int u = 0; u < yDCTBlock.length; u++) {
			for(int v = 0; v < yDCTBlock.length; v++) {
				if(qualityFactor == 100) {
					quantifiedImage[u][v] = (int) yDCTBlock[u][v];
				}
				else {
					double denominator = 0;
					
					if(layer == Main.Y) {
						denominator = alpha(qualityFactor) * qY[u][v];
					}
					else {
						denominator = alpha(qualityFactor) * qCbCr[u][v];
					}
					
					quantifiedImage[u][v] = (int) Math.round(yDCTBlock[u][v] / denominator);
				}
			}
		}
		
		return quantifiedImage;
	}
	
	/**
	 * Calcul le alpha en fonction du facteur de qualite
	 * @param qualityFactor
	 * @return alpha
	 */
	private static double alpha(int qualityFactor) {
		
		if(1 <= qualityFactor && qualityFactor <= 50){
			return (50 / (double) qualityFactor);
		}
		else if(50 <= qualityFactor && qualityFactor <= 99){
			return ((200 - 2 * (double) qualityFactor) / 100);
		}
		else{
			return 0;
		}
	}
	
	/**
	 * Algorithme de zigzag sur des bloc 8x8
	 * @param bloc
	 * @return matrice zigzag
	 * 
	 * complexite : O(N)
	 */
	public static int[] zigzag(int[][] bloc){
		
		int size = (bloc.length)*(bloc.length);
		int[] zizagMat = new int[size];
        int x= 0;
        int y= 0;
           
           for(int i=0; i<size; i++){
        		zizagMat[i] = bloc[x][y];
        		
                if((x + y) % 2 == 0){   
                    if(y < 8){
                        y++;
                    }else{
                        x+= 2;
                    }
                    if(x > 1){
                        x--;
                    }
                }
                else{
                    if(x < 8){
                         x++;
                    }else{
                         y+= 2;
                    }
                    if(y > 1){
                         y--;
                    }
                }
            }
		 return zizagMat;
	}
	
}
