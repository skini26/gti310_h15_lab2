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
				
			//Ecrire la source
			for(int i=0;i<output.getChemins().length;i++){
					if(output.getChemins()[i][1] == -1){
						bf.write((i+1) + "\n");
					}
			}
			
			//Permet d'écrire toutes les données du graphe
			for(int i=0;i<output.getChemins().length;i++){
									
					bf.write(output.getChemins()[i][1] + "\t" + output.getChemins()[i][0]);
					bf.write("\n");
			}
			
			
			bf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
