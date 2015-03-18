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
		
		//DEBUG
		int origine = output.getOrigine();
		int[][] chemins = output.getChemins();
		System.out.println("============= OUTPUT ==============");
		System.out.println("Source = "+origine);
		
		for (int i = 0; i < chemins.length; i++) {
			
				int dest = i;
				int dist = chemins[i][0];
				int parent = chemins[i][1];
				System.out.println("Dest="+dest+" Parent="+parent+" Dist="+dist);
			
		}
		//DEBUG
		
		File file = new File(filename);
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
				
			//Ecrire la source
			for(int i=0;i<chemins.length;i++){
					if(chemins[i][1] == -1){
						bf.write((i+1) + "\n");
					}
			}
			
			//Permet d'écrire toutes les données du graphe
			for(int i=0;i<chemins.length;i++){
									
					bf.write(chemins[i][1] + "\t" + chemins[i][0]);
					bf.write("\n");
			}
			
			
			bf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
