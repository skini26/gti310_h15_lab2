package gti310.tp3.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import gti310.tp3.models.MatriceGraphe;


public class ConcreteParser implements Parser<MatriceGraphe> {

	@Override
	public MatriceGraphe parse(String filename) {
		
		BufferedReader br;
		MatriceGraphe zone = null;
		
		try {
			br = new BufferedReader(new FileReader(filename));
			String nbVillesString = br.readLine();
			String villeDepartString = br.readLine();
			int nbVilles = 0;
			int villeDepart = 0;
			
			if(villeDepartString.trim().isEmpty() == false || nbVillesString.trim().isEmpty() == false){
				try{
					villeDepart = Integer.parseInt(villeDepartString);
					nbVilles = Integer.parseInt(nbVillesString);
					
					//Debug
					System.out.println("Ville de depart : "+villeDepart);
					System.out.println("Nombre de villes : "+nbVilles);
					//Debug
				}
				catch(NumberFormatException e){
					return null;
				}
			}
			
			zone = new MatriceGraphe(nbVilles, villeDepart);
		
			String chemin = null;
			while((chemin = br.readLine()) != null){
				String[] donnees = chemin.split("\\t");
				if(donnees.length == 3){
					int depart = Integer.parseInt(donnees[0]);
					int arrivee= Integer.parseInt(donnees[1]);
					int distance = Integer.parseInt(donnees[2]);
					zone.getGraphe()[depart-1][arrivee-1] = distance;
					
					//Debug
					//System.out.println("Depart="+depart+" Dest="+arrivee+" Dist="+distance);
				}
				else if(donnees.length == 1){
					if("$".equals(donnees[0])){
						break;
					}
				}
			}
			
			//Mettre valeur infini quand aucun chemin n'est disponible
			for(int i=0; i<zone.getGraphe().length; i++){
				for(int j=0; j<zone.getGraphe().length; j++){
					if(zone.getGraphe()[i][j] == 0 && (i!=j)){
						zone.getGraphe()[i][j] = MatriceGraphe.INFINI;
					}
				}
			}
		}catch (FileNotFoundException e1) {
			System.err.println("FileNotFoundException");
			return null;
		}
		 catch (IOException e) {
			 System.err.println("IOException");
			 return null;
		}
		
		
		//DEBUG
		for (int i = 0; i < zone.getGraphe().length; i++) {
			for (int j = 0; j < zone.getGraphe().length; j++) {
				int depart = i+1;
				int dest = j+1;
				int dist = zone.getGraphe()[i][j];
				System.out.println("Depart="+depart+" Dest="+dest+" Dist="+dist);
				
			}
		}
		//DEBUG
		
		return zone;
	
	}

}
