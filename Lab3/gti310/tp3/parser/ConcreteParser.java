package gti310.tp3.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import gti310.tp3.models.Chemin;
import gti310.tp3.models.Ville;
import gti310.tp3.models.Zone;

public class ConcreteParser implements Parser<Zone> {

	@Override
	public Zone parse(String filename){
		Zone zone = null;
		ArrayList<Chemin> chemins = null;
		ArrayList<Ville> villes = null;
		Ville villeDepart;
		
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(filename));
			long nbVilles = Long.parseLong(br.readLine());
			
			//Créer la Liste de villes et la peupler
			villes = new ArrayList<Ville>((int) nbVilles);
			for(long i=0; i<nbVilles; i++){
				villes.add(new Ville(i, null));
			}
			
			long villeDepartId = 0;
			//Si pas de ville de départ spécifiée, prendre la premiere
			try{
				villeDepartId = Long.parseLong(br.readLine());
			}
			catch(NumberFormatException e){
				villeDepartId = 1;
			}
			
			villeDepart = new Ville(villeDepartId, null);
		
			String ligne;
			while((ligne = br.readLine()) != null){
				String[] donnees = ligne.split("\\t");
				if(donnees.length == 3){
					Ville depart = new Ville(Long.parseLong(donnees[0]), null);
					Ville arrivee= new Ville(Long.parseLong(donnees[1]), null);
					int distance = Integer.parseInt(donnees[2]);
					Chemin chemin = new Chemin(depart, arrivee, distance);
					chemins.add(chemin);
				}
				else if(donnees.length == 1){
					if("$".equals(donnees[0])){
						break;
					}
				}
			}
			
			zone = new Zone(villes, chemins, villeDepart);

			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		return zone;
	}


}
