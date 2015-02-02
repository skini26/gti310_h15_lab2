package gti310.tp2.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;



public class ConcreteAudioFilter implements AudioFilter {

	private FileSource input;
	private FileSink output;
	private int delai;
	private int facteurAttenuation;
	
	private int numberOfChannels;
	private int sampleRate;
	private int bitsPerSample;
	private int dataSize;
	
	public static final int WAVE_HEADER_SIZE = 44;
	public static final String FILE_FORMAT = "WAVE";
	public static final int FILE_TYPE = 1;
	
	public ConcreteAudioFilter(FileSource input, FileSink output, 
								int delai, int facteurAttenuation) throws Exception{
		this.input = input;
		this.output = output;
		this.delai = delai;
		this.facteurAttenuation = facteurAttenuation;
		
		checkHeader();
		process();
		
	}
	
	public void checkHeader() throws Exception{
		
		// http://www.topherlee.com/software/pcm-tut-wavformat.html
		
		byte[] header = input.pop(WAVE_HEADER_SIZE);
		
		//check file format : 9-12
		char[] fileFormat = new char[4];
		fileFormat[0] = (char) header[9];
		fileFormat[1] = (char)header[10];
		fileFormat[2] = (char)header[11];
		fileFormat[3] = (char)header[12];
		String fileFormatString = String.valueOf(fileFormat);
		if(FILE_FORMAT.equals(fileFormatString) == false){
			throw new Exception("File type is not "+FILE_FORMAT);
		}
		
		//check type (pcm = 1) : 21-22
		
		int type = ((header[21] & 0xFF) << 8) | ((header[22] & 0xFF)<< 0);
		if(type != FILE_TYPE){
			throw new Exception("File type is not "+FILE_TYPE);
		}
		
		//check number of channels : 23-24
		
		numberOfChannels = ((header[23] & 0xFF) << 8) | ((header[24] & 0xFF)<< 0);
		
		//check sample rate : 25-28
		
		sampleRate = ((header[25])<< 24) | ((header[26] & 0xFF) << 16) | 
				     ((header[27] & 0xFF) << 8) | ((header[28] & 0xFF)<< 0);
		
		//check bits per sample : 35-36
		
		bitsPerSample = ((header[35] & 0xFF) << 8) | ((header[36] & 0xFF)<< 0);
		if(bitsPerSample != 8 && bitsPerSample != 16){
			throw new Exception("Bits per sample = "+bitsPerSample);
		}
		
		//check data size : 41-44
		dataSize = ((header[41])<< 24) | ((header[42] & 0xFF) << 16) | 
			     ((header[43] & 0xFF) << 8) | ((header[44] & 0xFF)<< 0);
		
		
		//
		
	}
	
	@Override
	public void process() {
		try {
			
			byte[] data = input.pop(dataSize);
			ByteArrayInputStream dataIn = new ByteArrayInputStream(data);
			
			int bytePerSubSample = bitsPerSample/8;
			int bytePerBigSample = numberOfChannels*bytePerSubSample; 
			ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
			
			for(int i = 0; i<data.length; i+=bytePerBigSample){
				
				dataIn.reset();
				dataIn.skip(i);
				
				byte[] sample = new byte[bytePerBigSample];
				dataIn.read(sample);
				//dataOut.write(sample);
				
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
					byte[] outSample = new byte[bytePerBigSample]; //y
					
					//a.x[n-M]
					int m = delai*bytePerBigSample;
					
					//x[n] == sample
					
					//x[n-m] == delayedSample
					dataIn.reset();
					dataIn.skip(i-m);
					byte[] delayedSample = new byte[bytePerBigSample];
					dataIn.read(delayedSample);
					
					sample+(facteurAttenuation*delayedSample);
					
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
