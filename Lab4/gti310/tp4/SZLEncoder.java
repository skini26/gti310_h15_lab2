package gti310.tp4;

import java.util.ArrayList;
import java.util.List;

public class SZLEncoder {

	public SZLEncoder(){
		
	}

	public void encode(String fileToEncode, String encodedFile,
			int qualityFactor) {
		
		//Lecture de l'image PPM
		PPMReaderWriter ppmReaderWriter = new PPMReaderWriter();
		int[][][] ppmImageRGB = ppmReaderWriter.readPPMFile(fileToEncode);
		
		
		//Convertir RGB 'a YCBR
		int[][][] ppmImageYCBR = ImageColorFormatConverter.convertRGBtoYCbCr(ppmImageRGB);
		
		//Decoupage en blocs Main.BLOCK_SIZExMain.BLOCK_SIZE
		List<int[][][]> imageBlocksList = divideInto8x8Blocks(ppmImageYCBR);
		
		//DCT
		
		
		//Quantification
		
		
		//Zigzag 1) DPCM 2) RLC
		
		
		//Codage entropique
		
		
		//Ecriture de l'image quasi-JPEG
		
		
	}
	
	/**
	 * Divide an image in many Main.BLOCK_SIZExMain.BLOCK_SIZE images
	 * @param image
	 * @return list of Main.BLOCK_SIZExMain.BLOCK_SIZE images
	 */
	private List<int[][][]> divideInto8x8Blocks(int[][][] image){
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
	
	private List<int[][][]> applyDCT(List<int[][][]> blocks){

        List<int[][][]> filteredBlocks = new ArrayList<int[][][]>();
		
		
			
		//For each block, apply the DCT
		for(int[][][] block : blocks){
			
			int[][] filteredBlock = new int[Main.BLOCK_SIZE][Main.BLOCK_SIZE];
			
			for(int u = 0; u < block.length; u++) {
				for(int v = 0; v < block.length; v++) {
					float sum = 0;
					for(int i = 0; i < block.length; i++) {
						for(int j = 0; j < block.length; j++) {
							double result = (Math.cos(((2 * i + 1) * u * Math.PI) / 16) * Math.cos(((2 * j + 1) * v * Math.PI) / 16));
							sum += result * block[i][j]; 
						}	
					}
					
					filteredBlock[u][v] = Math.round(((c(u) * c(v)) / 4.F) * sum);
				}
				filteredBlocks.add(filteredBlock);
			} 
		}
        return filteredBlocks;

	}
	
	
}
