package shared;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*; 
public class SimpleDijkstra { 
	private float dist[]; 
	private Set<Integer> settled; 
	private PriorityQueue<Node> pq; 
	private int V; // Number of vertices 
	List<List<Node> > adj; 

	public SimpleDijkstra(int V) { 
		this.V = V; 
		dist = new float[V]; 
		settled = new HashSet<Integer>(); 
		pq = new PriorityQueue<Node>(V, new Node()); 
	} 

	// Function for Dijkstra's Algorithm 
	void dijkstra(List<List<Node> > adj, int src) { 
		this.adj = adj; 

		for (int i = 0; i < V; i++) 
			dist[i] = Integer.MAX_VALUE; 

		// Add source node to the priority queue 
		pq.add(new Node(src, 0)); 

		// Distance to the source is 0 
		dist[src] = 0; 
		while (settled.size() != V) { 

			// remove the minimum distance node  
			// from the priority queue  
			int u = pq.remove().node; 

			// adding the node whose distance is 
			// finalized 
			settled.add(u); 

			e_Neighbours(u); 
		} 
	} 

	// Function to process all the neighbours  
	// of the passed node 
	private void e_Neighbours(int u) { 
		float edgeDistance = -1; 
		float newDistance = -1; 

		// All the neighbors of v 
		for (int i = 0; i < adj.get(u).size(); i++) { 
			Node v = adj.get(u).get(i); 

			// If current node hasn't already been processed 
			if (!settled.contains(v.node)) { 
				edgeDistance = v.cost; 
				newDistance = dist[u] + edgeDistance; 

				// If new distance is cheaper in cost 
				if (newDistance < dist[v.node]) 
					dist[v.node] = newDistance; 

				// Add the current node to the queue
				pq.add(new Node(v.node, dist[v.node])); 
			} 
		} 
	} 

	// Driver code 
	static void runDijsktra(String scFileName, String edgesFileName) throws IOException { 
		int V; 
		int source = 0; 

		BufferedReader scReader = new BufferedReader(new FileReader(scFileName));
		// Adjacency list representation of the connected edges 
		List<List<Node>> adj = new ArrayList<List<Node>>(); 
		BufferedReader bufferedReader = new BufferedReader(new FileReader(edgesFileName));
		String line = bufferedReader.readLine();
		String[] details = line.split(" ");
		V = Integer.parseInt(details[5]);
		// Initialize list for every node 
		for (int i = 0; i < V; i++) { 
			List<Node> item = new ArrayList<Node>(); 
			adj.add(item); 
		} 

		// Inputs for the DPQ graph
		while((line = bufferedReader.readLine()) != null) {
			//			System.out.println(line);
			details = line.split(" ");
			adj.get(Integer.parseInt(details[1])).add(new Node(Integer.parseInt(details[2]), Float.parseFloat(details[3]))); 
		}
		PrintWriter out = new PrintWriter("distance.txt");
		String scLine = scReader.readLine();
		while((scLine = scReader.readLine()) != null) {
			// Calculate the single source shortest path 
			String[] breaks = scLine.split(" ");
			source = Integer.parseInt(breaks[0]);
			SimpleDijkstra dpq = new SimpleDijkstra(V); 
			dpq.dijkstra(adj, source); 
			// Print the shortest path to all the nodes from the source node 
//			System.out.println("The shorted path from node :"); 
			
			for (int i = 0; i < dpq.dist.length; i++) {
				out.print(dpq.dist[i]);
				if((i+1) != dpq.dist.length) out.print(",");
//				System.out.println(source + " to " + i + " is " + dpq.dist[i]); 
			}
			out.println();
		}
		out.close();
	} 
	
	public static void computeShortestPath(String scFileName, String edgesFileName, String distanceFileName) throws IOException {
		File distanceFile = new File(distanceFileName);
		if(!distanceFile.exists()) {
			System.out.println("Computing shortest path...");
			SimpleDijkstra.runDijsktra(scFileName, edgesFileName);
		}
	}
} 

// Class to represent a node in the graph 
class Node implements Comparator<Node> { 
	public int node; 
	public float cost; 

	public Node() {
	} 

	public Node(int node, float f) { 
		this.node = node; 
		this.cost = f; 
	} 

	@Override
	public int compare(Node node1, Node node2) { 
		if (node1.cost < node2.cost) 
			return -1; 
		if (node1.cost > node2.cost) 
			return 1; 
		return 0; 
	} 
}