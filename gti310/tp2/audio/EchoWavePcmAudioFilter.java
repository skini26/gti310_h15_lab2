package gti310.tp2.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;



public class EchoWavePcmAudioFilter implements AudioFilter {

	private FileSource input;
	private FileSink output;
	private int delai;
	private int facteurAttenuation;
	
	private int numberOfChannels;
	private int sampleRate;
	private int bitsPerSample;
	private int dataSize;
	
	//This filter doesn't change anything in the header
	private byte[] header;
	
	public static final int WAVE_HEADER_SIZE = 44;
	public static final String FILE_FORMAT = "WAVE";
	public static final int FILE_TYPE = 1;
	
	public EchoWavePcmAudioFilter(FileSource input, FileSink output, 
								int delai, int facteurAttenuation) throws Exception{
		this.input = input;
		this.output = output;
		this.delai = delai;
		this.facteurAttenuation = facteurAttenuation;
		
		checkHeader();
		
	}
	
	public void checkHeader() throws Exception{
		
		// http://www.topherlee.com/software/pcm-tut-wavformat.html
		
		header = input.pop(WAVE_HEADER_SIZE);
		
		//check file format : 9-12
		char[] fileFormat = new char[4];
		fileFormat[0] = (char) header[8];
		fileFormat[1] = (char)header[9];
		fileFormat[2] = (char)header[10];
		fileFormat[3] = (char)header[11];
		String fileFormatString = String.valueOf(fileFormat);
		
		System.out.println("File format : "+fileFormatString);
		if(FILE_FORMAT.equals(fileFormatString) == false){
			throw new Exception("File type is not "+FILE_FORMAT);
		}
		
		
		//check type (pcm = 1) : 21-22
		
		int type = ((header[21] & 0xFF) << 8) | ((header[20] & 0xFF)<< 0);
		System.out.println("File type : "+type);
		if(type != FILE_TYPE){
			throw new Exception("File type is not "+FILE_TYPE);
		}
		
		//check number of channels : 23-24
		
		numberOfChannels = ((header[23] & 0xFF) << 8) | ((header[22] & 0xFF)<< 0);
		System.out.println("Number of channels : "+numberOfChannels);
		
		//check sample rate : 25-28
		
		sampleRate = ((header[27])<< 24) | ((header[26] & 0xFF) << 16) | 
				     ((header[25] & 0xFF) << 8) | ((header[24] & 0xFF)<< 0);
		System.out.println("Sample rate : "+sampleRate);
		
		//check bits per sample : 35-36
		
		bitsPerSample = ((header[35] & 0xFF) << 8) | ((header[34] & 0xFF)<< 0);
		System.out.println("Bits per sample : "+bitsPerSample);
		if(bitsPerSample != 8 && bitsPerSample != 16){
			throw new Exception("Bits per sample not OK = "+bitsPerSample);
		}
		
		//check data size : 41-44
		dataSize = ((header[43])<< 24) | ((header[42] & 0xFF) << 16) | 
			     ((header[41] & 0xFF) << 8) | ((header[40] & 0xFF)<< 0);
		System.out.println("Data size : "+dataSize/8+" bytes");
		
		//
		
	}
	
	public void writeHeader(){
		//Same header as input file this filter doesn't change it.
		output.push(header);
	}
	
	@Override
	public void process() {
		try {
			//Write header
			writeHeader();
			
			//Get Audio Data and process it.
			byte[] data = input.pop(dataSize);
			ByteArrayInputStream dataIn = new ByteArrayInputStream(data);
			
			int bytePerSubSample = bitsPerSample/8;
			System.out.println("Bytes per sub sample : "+bytePerSubSample);
			int bytePerBigSample = numberOfChannels*bytePerSubSample; 
			System.out.println("Bytes per complete sample : "+bytePerBigSample);
			
			//Sauter une ligne
			System.out.println();
			
			for(int i = 0; i<data.length; i+=bytePerSubSample){
				
				//Progress bar
				System.out.print(".");
				
				dataIn.reset();
				dataIn.skip(i);
				
				byte[] sample = new byte[bytePerSubSample];
				dataIn.read(sample);
				
				if(i<bytePerBigSample){
					output.push(sample);
				}
				else{
					/*
					 * L’effet d’écho à implémenter pour ce laboratoire consiste à répéter
					 *  le signal d’origine, noté x, après un délai de M échantillons. 
					 *  Le signal répété est atténué par un facteur a. 
					 *  L’équation de cette effet correspond à :
						y[n] = x[n] + a.x[n-M] (1)
						Où y représente le signal de sortie et n, l’échantillon courant.
					 */
					
					//a.x[n-M]
					int m = delai*bytePerBigSample;
					
					//x[n] == sample
					
					//x[n-m] == delayedSample
					dataIn.reset();
					dataIn.skip(i-m);
					byte[] delayedSample = new byte[bytePerSubSample];
					dataIn.read(delayedSample);
					
					//y[n] = x[n] + a.x[n-M] = sample + facteurAttenuation*delayedSample
					
					//Transform byte array to int
					ByteBuffer sampleByteBuffer = ByteBuffer.wrap(sample);
					sampleByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
					int sampleInt = sampleByteBuffer.getShort();
					
					ByteBuffer delayedSampleByteBuffer = ByteBuffer.wrap(sample);
					delayedSampleByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
					int delayedSampleInt = delayedSampleByteBuffer.getShort();
					
					//y[n] = x[n] + a.x[n-M] 
					int outSampleInt = sampleInt + (facteurAttenuation*delayedSampleInt);
					
					//Convert to byte array
					byte[] outSample = ByteBuffer.allocate(bytePerSubSample).putShort((short) outSampleInt).array();

					//Write byte array on the file.
					output.push(outSample);
					
				}
				
			}
			dataIn.close();
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
