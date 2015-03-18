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
			
			//Si les donnees de depart sont presentes
			if(villeDepartString.trim().isEmpty() == false || nbVillesString.trim().isEmpty() == false){
				try{
					villeDepart = Integer.parseInt(villeDepartString);
					nbVilles = Integer.parseInt(nbVillesString);
					
					//Debug
					System.out.println("========== INPUT ===========");
					System.out.println("Ville de depart : "+villeDepart);
					System.out.println("Nombre de villes : "+nbVilles);
					//Debug
				}
				catch(NumberFormatException e){
					return null;
				}
			}
			//Sinon retourner null, fichier invalide
			else{
				return null;
			}
			
			//Instancier notre objet representant le graphe avec le noeud source
			zone = new MatriceGraphe(nbVilles, villeDepart);
		
			String chemin = null;
			//Lire tous les chemins et les stocker dans le graphe
			while((chemin = br.readLine()) != null){
				String[] donnees = chemin.split("\\t");
				//Si toutes les donnees sont presentes
				if(donnees.length == 3){
					int depart = Integer.parseInt(donnees[0]);
					int arrivee= Integer.parseInt(donnees[1]);
					int distance = Integer.parseInt(donnees[2]);
					zone.getGraphe()[depart-1][arrivee-1] = distance;
					
					//Debug
					System.out.println("Depart="+depart+" Dest="+arrivee+" Dist="+distance);
				}
				//EOF
				else if(donnees.length == 1){
					if("$".equals(donnees[0])){
						break;
					}
				}
				//Sinon retourner null, fichier invalide
				else{
					return null;
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
		
		return zone;
	}

}
