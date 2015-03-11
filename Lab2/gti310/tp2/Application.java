package gti310.tp2;

import gti310.tp2.audio.EchoWavePcmAudioFilter;
import gti310.tp2.io.FileSink;
import gti310.tp2.io.FileSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Application {

	/**
	 * Launch the application
	 * @param args This parameter is ignored
	 */
	public static void main(String args[]) {
		System.out.println("Echo audio filter project!");
		
		//Extract parameters
		String inputFileName = args[0];
		String outputFileName = args[1];
		String delaiString = args[2];
		String facteurAttenuationString = args[3];
		
		//Check if parameters are not null
		if(inputFileName != null && outputFileName != null 
				&& delaiString != null && facteurAttenuationString != null){
			
			try {
				
				//Convert "delai" and "facteur" to int
				int delai = Integer.parseInt(delaiString);
				double facteurAttenuation = Double.parseDouble(facteurAttenuationString);
				
				//Display parameters
				System.out.println("Fichier : "+inputFileName);
				System.out.println("Delai : "+delai);
				System.out.println("Facteur d'attenuation : "+facteurAttenuation);
				System.out.println("Fichier de sortie : "+outputFileName);
				
				//Create the FileSource (Input)
				FileSource input = new FileSource(inputFileName);
				//Create the FileSink (Output)
				FileSink output = new FileSink(outputFileName);
				
				//Create the audio filter
				EchoWavePcmAudioFilter filter = new EchoWavePcmAudioFilter
							(input, output, delai, facteurAttenuation);
				
				//Filter the audio
				System.out.println("Filtrage en cours ...");
				filter.process();
				System.out.println("Filtrage fini.");
				
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
