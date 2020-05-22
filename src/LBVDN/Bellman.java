package LBVDN;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Bellman {

	static Graph<String, DefaultEdge> minCycle = new SimpleDirectedWeightedGraph<>(DefaultEdge.class); //negative cycle for assignment re-adjustment based on distance only
	static Graph<String, DefaultEdge> minPath = new SimpleDirectedWeightedGraph<>(DefaultEdge.class); //negative path for penalty re-adjustment
	
	public static List<String> getMinCycle() {
		return (List<String>) minCycle;
	}

	public static List<String> getMinPath() {
		return (List<String>) minPath;
	}

	private static void initializeMinCycle(Integer anchor) {
		for(int i = 0; i < SCHashMap.size(); i++) {
			for(int j = 0; j < SCHashMap.size(); j++) {
				if(i != j) {
					String v1, v2;
					if(i == anchor) {
						v1 = anchor.toString()+"_out";
						v2 = String.valueOf(j);
					} else if(j == anchor) {
						v1 = String.valueOf(i);
						v2 = anchor.toString()+"_in";
					} else {
						v1 = String.valueOf(i);
						v2 = String.valueOf(j);
					}
					minCycle.addVertex(v1);
					minCycle.addVertex(v2);			
					minCycle.addEdge(v1, v2);
					minCycle.setEdgeWeight(v1, v2, CostMatrix.costDiffMatrix[i][j]);
				}
			}
		}
	}

	public static boolean isNegativeMinCycleDetected(Integer anchor) {
		initializeMinCycle(anchor);
		BellmanFordShortestPath<String, DefaultEdge> bellmanFordShortestPath = new BellmanFordShortestPath<String, DefaultEdge>(minCycle);
		List<String> minCycle = bellmanFordShortestPath.getPath(anchor.toString()+"_out", anchor.toString()+"_in").getVertexList();
//		System.out.print("First Negative Cycle Path for distance: ");
//		for(int i = 0; i<minCycle.size(); i++) {
//			System.out.print(minCycle.get(i)+",");
//		}
//		System.out.println();
		if(bellmanFordShortestPath.getPathWeight(anchor.toString()+"_out", anchor.toString()+"_in") < 0)
			return true;
		return false;
	}
	
	private static void initializeMinPath(Integer anchor) {
		for(int i = 0; i < SCHashMap.size(); i++) {
			for(int j = 0; j < SCHashMap.size(); j++) {
				if(i != j) {
					String v1, v2;
					Float EdgeWeight;
					if(i == anchor) {
						v1 = anchor.toString()+"_out";
						v2 = String.valueOf(j);
						EdgeWeight = CostMatrix.costDiffMatrix[i][j];
					} else if(j == anchor) {
						v1 = String.valueOf(i);
						v2 = anchor.toString()+"_in_";
						EdgeWeight = SCHashMap.get(Integer.parseInt(v1)).getPenalty() - SCHashMap.get(anchor).getPenalty();
					} else {
						v1 = String.valueOf(i);
						v2 = String.valueOf(j);
						EdgeWeight = CostMatrix.costDiffMatrix[i][j];
					}
					minPath.addVertex(v1);
					minPath.addVertex(v2);
					minPath.addEdge(v1, v2);
					minPath.setEdgeWeight(v1, v2, EdgeWeight);
				}
			}
		}
	}
	
	public static boolean isNegativeMinPathDetected(Integer anchor) {
		initializeMinPath(anchor);
		BellmanFordShortestPath<String, DefaultEdge> bellmanFordShortestPath = new BellmanFordShortestPath<String, DefaultEdge>(minCycle);
		List<String> minPath = bellmanFordShortestPath.getPath(anchor.toString()+"_out", anchor.toString()+"_in").getVertexList();
//		System.out.print("Second Negative Cycle Path for distance+penalty: ");
//		for(int i = 0; i<minPath.size(); i++) {
//			System.out.print(minPath.get(i)+",");
//		}
//		System.out.println();
		if(bellmanFordShortestPath.getPathWeight(anchor.toString()+"_out", anchor.toString()+"_in") < 0)
			return true;
		return false;
	}

}
