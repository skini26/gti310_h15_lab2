package gti310.tp3.algorithms;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import gti310.tp3.models.MatriceGraphe;

/**
 * 
 * @author Yanis
 * Base sur : https://www.swila.be/files/labo-ppm/PPM0708.pdf
 */
public class DjikstraAlgorithm {

	/**
	 * Initialiser le graphe de sortie et la queue
	 * @param graph
	 * @param originId
	 * @return le queue contenant toutes les villes
	 */
	private static ArrayList<Integer> init(int [][] graph, int originId){
		
		ArrayList<Integer> q = new ArrayList<Integer>(graph.length);
		
		for(int i=0; i<graph.length; i++){
			graph[i][0] = MatriceGraphe.INFINI; //Distance inconnue de la source 'a i
			graph[i][1] = -1;	//Noeud precedent dans le chemin optimal indefini
			q.add(i); //Ajouter tous les noeuds 'a la queue
		}
		
		graph[originId][0] = 0; //Distance de source 'a source
		graph[originId][1] = -1; //Sommet precedent 'a la source indefini
		
		return q;
	}
	
	/**
	 * Algorithme de Dijkstra qui permettra de trouver les meilleurs
	 * chemins 'a partir d'un point de depart
	 * @param originalGraph : graphe representant tous les chemins (matrice de poids)
	 * @param originId : point de depart
	 * @return
	 */
	public static int[][] computePaths(int[][] originalGraph, int originId){
		
		// [i][0] Distance , [i][1] Parent ou i = numero du noeud
		int[][] outGraph = new int[originalGraph.length][2];
		
		ArrayList<Integer> vertexQueue = init(outGraph, originId);
		
		while(vertexQueue.isEmpty() == false){
			
			Integer nearestVertex = findShortestPath(vertexQueue, outGraph);
			vertexQueue.remove(nearestVertex);
			
			/*
			for(int i=0;i<vertexQueue.size();i++){
				if(vertexQueue.get(i) == nearestVertex){
					vertexQueue.remove(i);
				}
			}
			*/
			
			System.out.println("Nearest vertex = "+(nearestVertex+1));
			
			for (int i = 0; i < originalGraph.length; i++) {
				
				int distance = originalGraph[nearestVertex][i];
				
				if(distance != MatriceGraphe.INFINI){
					System.out.println("distance entre "+(nearestVertex+1)+" et "+(i+1)+" = "+distance);
					relax(nearestVertex, i, originalGraph, outGraph);
				}
			}
		}
		
		return outGraph;
	}

	/**
	 * Trouve la ville la plus proche de la source
	 * @param vertexQueue
	 * @param outGraph
	 * @return
	 */
	private static Integer findShortestPath(ArrayList<Integer> vertexQueue, int[][] outGraph) {
		
		int shortestDistance = MatriceGraphe.INFINI;
		int shortestPathVertex = 0;
		
		//Visiter tous les voisins
		for(Integer vertex : vertexQueue) {
			
			int newShortestDistance = outGraph[vertex][0];
	
			if(newShortestDistance < shortestDistance){
				shortestDistance = newShortestDistance;
				shortestPathVertex = vertex;		
			}

		}

		return shortestPathVertex;
	}
	
	/**
	* Assigne les valeurs des meilleurs chemins dans le graphe de sortie
	* @param u Noeud avec plus petite valeur
	* @param v index dans le graphe
	* @param originalGraph
	* @param graphe
	*/
	private static void relax(int u, int v, int[][] originalGraph, int[][] outGraph){
		
		int dist = outGraph[u][0] + originalGraph[u][v];
		if(dist < outGraph[v][0]){
			outGraph[v][0] = dist;
			outGraph[v][1] = u;
			System.out.println("Nouvelle distance plus petite = "+dist);
		}
	}
	
	
}
