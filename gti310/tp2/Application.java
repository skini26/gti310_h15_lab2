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
		
		String inputFileName = args[0];
		String delaiString = args[1];
		String facteurAttenuationString = args[2];
		
		if(inputFileName != null && delaiString != null 
					&& facteurAttenuationString != null){
			try {
				
				int delai = Integer.parseInt(delaiString);
				int facteurAttenuation = Integer.parseInt(facteurAttenuationString);
				
				System.out.println("Fichier : "+inputFileName);
				System.out.println("Delai : "+delai);
				System.out.println("Facteur d'attenuation : "+facteurAttenuation);
				System.out.println("Fichier de sortie : "+inputFileName+"_Filtered");
				
				FileSource input = new FileSource(inputFileName);
				FileSink output = new FileSink(inputFileName+"_Filtered");
				
				EchoWavePcmAudioFilter filter = new EchoWavePcmAudioFilter
							(input, output, delai, facteurAttenuation);
				
				System.out.println("Filtrage en cours ...");
				filter.process();
				System.out.println("Filtrage fini.");
				
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
	}
}
