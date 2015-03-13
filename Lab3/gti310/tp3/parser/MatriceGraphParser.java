package gti310.tp3.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import gti310.tp3.models.Chemin;
import gti310.tp3.models.MatriceGraphe;
import gti310.tp3.models.Ville;

public class MatriceGraphParser implements Parser<MatriceGraphe> {

	@Override
	public MatriceGraphe parse(String filename) throws FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		MatriceGraphe zone = null;
		
		try {
			String nbVillesString = br.readLine();
			String villeDepartString = br.readLine();
			int nbVilles = 0;
			int villeDepart = 0;
			
			if(villeDepartString.trim().isEmpty() == false || nbVillesString.trim().isEmpty() == false){
				try{
					villeDepart = Integer.parseInt(villeDepartString);
					nbVilles = Integer.parseInt(nbVillesString);
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
					zone.getGraphe()[depart][arrivee] = distance;
				}
				else if(donnees.length == 1){
					if("$".equals(donnees[0])){
						break;
					}
				}
			}
			
			//Mettre valeur infini quand aucun chemin n'est disponible
			for(int i=0; i<nbVilles; i++){
				for(int j=0; i<nbVilles; i++){
					if(zone.getGraphe()[i][j] == 0 && i!=j){
						zone.getGraphe()[i][j] = MatriceGraphe.INFINI;
					}
				}
			}

		} catch (IOException e) {
			return null;
		}
		
		return zone;
	
	}

}
