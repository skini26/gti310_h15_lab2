package gti310.tp3.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gti310.tp3.models.Chemins;
import gti310.tp3.models.MatriceGraphe;

public class ConcreteWriter implements Writer<Chemins> {

	@Override
	public void write(String filename, Chemins output) {
	
		File file = new File(filename);
		
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
				
			int origine = (output.getOrigine()+1);
			int[][] chemins = output.getChemins();
			
			System.out.println("============= OUTPUT ==============");
			
	
			//Ecrire la source
			bf.write(String.valueOf(origine));
			bf.write(System.lineSeparator());
			
			//DEBUG
			System.out.println("Source = "+origine);
			//DEBUG

			//Ecrire tous les chemins
			for(int i=0; i< chemins.length; i++){
				int dest = i+1;
				int dist = chemins[i][0];
				int parent = (chemins[i][1] == -1) ? -1:chemins[i][1]+1;
				
				//DEBUG
				System.out.println("Dest="+dest+" Parent="+parent+" Dist="+dist);
				//DEBUG
				
				bf.write(dest + "\t" + parent + "\t" + dist);
				bf.write(System.lineSeparator());
			}
			
			
			bf.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
