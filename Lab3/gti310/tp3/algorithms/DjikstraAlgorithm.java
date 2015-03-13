package gti310.tp3.algorithms;

import java.util.PriorityQueue;
import java.util.Queue;

import gti310.tp3.models.MatriceGraphe;

/**
 * 
 * @author Yanis
 * Base sur : https://www.swila.be/files/labo-ppm/PPM0708.pdf
 */
public class DjikstraAlgorithm {

	private static int[][] initSource(int [][] graph, int originId, Queue q){
		for(int i=0; i<graph.length; i++){
			graph[i][0] = MatriceGraphe.INFINI;
			graph[i][1] = 0;
			q.add(i);
		}
		graph[originId][0] = 0;
		graph[originId][1] = -1;
		return graph;
	}
	
	public static int[][] djikstra(int[][] originalGraph, int outGraph[][], int originId){
		
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(outGraph.length);
		outGraph = initSource(outGraph, originId, q);

		while((q.peek()) != null){
			Integer vertex = q.poll();
			
			int nearestVertex = findShortestPath(vertex, originalGraph);
			
		}
		
		return outGraph;
	}

	private static int findShortestPath(Integer vertex, int[][] originalGraph) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
