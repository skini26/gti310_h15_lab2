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
	private int bytesNbInOneMiliSecond;
	
	//This filter doesn't change anything in the header
	private byte[] header;
	
	public static final int WAVE_HEADER_SIZE = 44;
	public static final String FILE_FORMAT = "WAVE";
	public static final int FILE_TYPE = 1;
	//Filter 1 MB at a time to not load the entire file in memory
	public static final int BATCH_SIZE = 1024; 
	
	public EchoWavePcmAudioFilter(FileSource input, FileSink output, 
								int delai, int facteurAttenuation) throws Exception{
		this.input = input;
		this.output = output;
		this.delai = delai;
		this.facteurAttenuation = facteurAttenuation;
		
		//Read the header, extract informations
		//and check if the file is a correct one.
		checkHeader();
		
	}
	
	/**
	 * Read the header, extract informations
	 * and check if the file is a correct one.
	 * @throws Exception
	 */
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
		bytesNbInOneMiliSecond = (sampleRate*bitsPerSample/8)/1000;
		
	}
	
	/**
	 * Write the Header of the filtered file
	 */
	public void writeHeader(){
		//Same header as input file this filter doesn't change it.
		output.push(header);
	}
	
	/**
	 * Apply the filter to the audio file.
	 */
	@Override
	public void process() {
		
		
		//Write header
		writeHeader();
		
		byte[] data;
		int bytesPerSubSample = bitsPerSample/8;
		int bytesPerBigSample = numberOfChannels*bytesPerSubSample; 
		int index = 0;
		
		
		do{
			//Get Audio Data by batches and process it until data == null.
			data = input.pop(BATCH_SIZE);
			
			if(data != null){
				//Progress bar
				System.out.print(".");
				
				for(int i = 0; i<data.length; i+=bytesPerSubSample){
					
					/*
					 * L�effet d��cho � impl�menter pour ce laboratoire consiste � r�p�ter
					 *  le signal d�origine, not� x, apr�s un d�lai de M �chantillons. 
					 *  Le signal r�p�t� est att�nu� par un facteur a. 
					 *  L��quation de cette effet correspond � :
						y[n] = x[n] + a.x[n-M] (1)
						O� y repr�sente le signal de sortie et n, l��chantillon courant.
					 */
					
					//x[n] == sample
					byte[] sample = Arrays.copyOfRange(data, i, i+bytesPerSubSample);
					
					
					//x[n-M] == delayedSample
					//M = number of samples behind
					int m = delai*bytesPerBigSample;
					int delayedSampleIndex = i-m;
					byte[] delayedSample = new byte[bytesPerSubSample];
					if(delayedSampleIndex>=0) {  
						delayedSample = Arrays.copyOfRange(data, delayedSampleIndex, delayedSampleIndex+bytesPerSubSample);
					}
					//else delayedSample = [0,0]
					
					short sampleShort = 0;
					short delayedSampleShort = 0;
					short outSampleShort = 0;
					byte[] outSample = null;
					float a = 1.0f/(float)facteurAttenuation;
					float delayedDecayedSample = 0.0f;
					//y[n] = x[n] + a.x[n-M] = sample + facteurAttenuation*delayedSample
					
					switch(bytesPerSubSample){
						//8bits
						case 1 : sampleShort = ByteBuffer.wrap(sample).getShort();
								 delayedSampleShort = ByteBuffer.wrap(delayedSample).getShort();
								 //a.x[n-M] = delayedDecayedSample
								 delayedDecayedSample = (float)delayedSampleShort*a;
								 //y[n] = outSample...
								 float outSampleFloat8Bits = (float)sampleShort + delayedDecayedSample;
								 outSampleShort = (short) outSampleFloat8Bits;
								//Convert to byte array
								 outSample = ByteBuffer.allocate(bytesPerSubSample).put((byte) outSampleShort).array();
								 break;
						//16bits
						case 2 : sampleShort = ByteBuffer.wrap(sample).getShort();
								 delayedSampleShort = ByteBuffer.wrap(delayedSample).getShort();
								 //y[n] = x[n] + a.x[n-M] 
								 delayedDecayedSample = (float)delayedSampleShort*a;
								 float outSampleFloat16Bits = (float)sampleShort + delayedDecayedSample;
								 outSampleShort = (short) outSampleFloat16Bits;
								//Convert to byte array
								 outSample = ByteBuffer.allocate(bytesPerSubSample).putShort((short) outSampleShort).array();
								 break;
					}
				
					index = index + bytesPerSubSample;
					//Write byte array on the file.
					output.push(outSample);
					//System.out.println("i absolue="+index+" | i="+i+" | delay index="+delayedSampleIndex+" | sample="+sampleShort+" | delayedSample="+delayedSampleShort+" | outSample="+outSampleShort);

				}
			}
			
		
		}while(data != null); 

		output.close();
	
	}

}
