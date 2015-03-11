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
	private int delai; // in MS
	private double facteurAttenuation;
	
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
	public static final int MAX_CLIPPING = 32767; 
	
	public EchoWavePcmAudioFilter(FileSource input, FileSink output, 
								int delai, double facteurAttenuation) throws Exception{
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
			
		do{
			//Get Audio Data by batches and process it until data == null.
			data = input.pop(dataSize);
			
			if(data != null){
				//Progress bar
				System.out.print(".");
				
				for(int i = 0; i<data.length; i+=bytesPerSubSample){
					
					/*
					 * L’effet d’écho à implémenter pour ce laboratoire consiste à répéter
					 *  le signal d’origine, noté x, après un délai de M échantillons. 
					 *  Le signal répété est atténué par un facteur a. 
					 *  L’équation de cette effet correspond à :
						y[n] = x[n] + a.x[n-M] (1)
						Où y représente le signal de sortie et n, l’échantillon courant.
					 */
					
					//x[n] == sample
					byte[] sample = Arrays.copyOfRange(data, i, i+bytesPerSubSample);
					
					
					//x[n-M] == delayedSample
					//M = number of samples behind
					int m = delai*(sampleRate/1000); //delay in ms
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
					double a = facteurAttenuation;
					double delayedDecayedSample = 0.0;
					//y[n] = x[n] + a.x[n-M] = sample + facteurAttenuation*delayedSample
					
					switch(bytesPerSubSample){
						//8bits
						case 1 : int unsignedSample = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).get() & 0xFF;;   
								 int unsignedDelayedSample = ByteBuffer.wrap(delayedSample).order(ByteOrder.LITTLE_ENDIAN).get() & 0xFF;
								 //a.x[n-M] = delayedDecayedSample
								 delayedDecayedSample = (double)unsignedDelayedSample*a;
								 //y[n] = outSample...
								 double outSampleDouble8Bits = (double)unsignedSample + delayedDecayedSample;
								
								 //cliping
								 if(outSampleDouble8Bits>MAX_CLIPPING){
									 outSampleDouble8Bits = MAX_CLIPPING;
								 }
								 else if(outSampleDouble8Bits< -MAX_CLIPPING){
									 outSampleDouble8Bits = -MAX_CLIPPING;
								 }
								 
								 outSampleShort = (short) outSampleDouble8Bits;
								//Convert to byte array
								 outSample = ByteBuffer.allocate(bytesPerSubSample).order(ByteOrder.LITTLE_ENDIAN).put((byte) outSampleShort).array();
								 break;
						//16bits
						case 2 : sampleShort = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).getShort();
								 delayedSampleShort = ByteBuffer.wrap(delayedSample).order(ByteOrder.LITTLE_ENDIAN).getShort();
								//a.x[n-M] = delayedDecayedSample
								 delayedDecayedSample = (double)delayedSampleShort*a;
								 //y[n] = outSample...
								 double outSampleDouble16Bits = (double)sampleShort + delayedDecayedSample;
								 
								 //cliping
								 if(outSampleDouble16Bits>MAX_CLIPPING){
									 outSampleDouble16Bits = MAX_CLIPPING;
								 }
								 else if(outSampleDouble16Bits< -MAX_CLIPPING){
									 outSampleDouble16Bits = -MAX_CLIPPING;
								 }
								 
								 outSampleShort = (short) outSampleDouble16Bits;
								//Convert to byte array
								 outSample = ByteBuffer.allocate(bytesPerSubSample).order(ByteOrder.LITTLE_ENDIAN).putShort((short) outSampleShort).array();
								 break;
					}
				
					//Write byte array on the file.
					output.push(outSample);
					//System.out.println("i="+i+" | delay index="+delayedSampleIndex+" | sample="+sampleShort+" ["+sample[0]+","+sample[1]+"] | delayedSample="+delayedSampleShort+" | outSample="+outSampleShort+" ["+outSample[0]+","+outSample[1]+"]");

				}
			}
			
		
		}while(data != null); 

		output.close();
	
	}

}
