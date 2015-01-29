package gti310.tp2.audio;

import java.util.ArrayList;

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
	
	public static final int WAVE_HEADER_SIZE = 44;
	public static final String FILE_FORMAT = "WAVE";
	public static final int FILE_TYPE = 1;
	
	public ConcreteAudioFilter(FileSource input, FileSink output, 
								int delai, int facteurAttenuation) throws Exception{
		this.input = input;
		this.output = output;
		this.delai = delai;
		this.facteurAttenuation = facteurAttenuation;
		
		checkHeader(input);
		
	}
	
	public void checkHeader(FileSource input) throws Exception{
		
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
		
		//
		
	}
	
	@Override
	public void process() {
		// TODO Auto-generated method stub

	}

}
