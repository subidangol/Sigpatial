package LBVDN;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class loads service centers, demand vertices and distance matrix from a file and performs assignments
 * @author subidangol
 */
public class PreProcessor {

	private static BufferedReader br;

	/**
	 * This function takes path of file containing service center details as argument and load in the Hash map
	 * @param filePath
	 * @throws IOException
	 */
	public static void loadAllUniqueServiceCenters(String filePath) throws IOException{
		br = new BufferedReader(new FileReader(filePath));
		String line="";
		int i=0;
		while((line=br.readLine()) != null) {
			String[] lineSplit = line.split(",");
			ServiceCenter serviceCenter = new ServiceCenter(lineSplit[0],Integer.parseInt(lineSplit[1]),Float.parseFloat(lineSplit[2]));
			SCHashMap.addSC(i,serviceCenter);
			i++;
		}
		new CostMatrix(i);
	}

	/**
	 * This function load all the vertex id along with vertex object(id and color) in HashMap
	 * @param filePath
	 * @throws IOException
	 */
	public static void loadAllVertices(String filePath) throws IOException{
		br = new BufferedReader(new FileReader(filePath));
		String line="";
		int i=0;
		while((line=br.readLine()) != null) {
			String[] lineSplit = line.split(",");
			if(lineSplit[0]!=null && !lineSplit[0].equals("") && !SCHashMap.containsID(lineSplit[0])) {
				DemandNode dn = new DemandNode(lineSplit[0]);
				DNHashMap.add(i,dn);
			}
			i++;
		}
	}

	/**
	 * Load everything in CCNVD
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static void loadCCNVD(String filePath) throws IOException {
		br = new BufferedReader(new FileReader(filePath));
		String line="";
		//adds in and out nodes with the edge weight to appropriate node
		while((line=br.readLine()) != null) {
			String[] details = line.split(",");
			//if both nodes of the edge are demand nodes
			if (DNHashMap.containsID(details[0]) && DNHashMap.containsID(details[1])) {
				DNHashMap.getDNbyID(details[0]).addOutNode(DNHashMap.getIndexbyID(details[1]), Float.parseFloat(details[2]));
				DNHashMap.getDNbyID(details[1]).addInNode(DNHashMap.getIndexbyID(details[0]), Float.parseFloat(details[2]));
			}
			//if both nodes of the edge are service centers
			else if (SCHashMap.containsID(details[0]) && SCHashMap.containsID(details[1])) {
				SCHashMap.getSCbyID(details[0]).addOutNode(SCHashMap.getIndexbyID(details[1]), Float.parseFloat(details[2]));
				SCHashMap.getSCbyID(details[1]).addInNode(SCHashMap.getIndexbyID(details[0]), Float.parseFloat(details[2]));
			}
			//if the first node is demand node and the second node of the edge is service center
			else if (DNHashMap.containsID(details[0]) && SCHashMap.containsID(details[1])) {
				DNHashMap.getDNbyID(details[0]).addOutNode(SCHashMap.getIndexbyID(details[1]), Float.parseFloat(details[2]));
				SCHashMap.getSCbyID(details[1]).addInNode(DNHashMap.getIndexbyID(details[0]), Float.parseFloat(details[2]));
			}
			//if the first node is service center and the second node of the edge is demand node
			else if (SCHashMap.containsID(details[0]) && DNHashMap.containsID(details[1])) {
				SCHashMap.getSCbyID(details[0]).addOutNode(DNHashMap.getIndexbyID(details[1]), Float.parseFloat(details[2]));
				DNHashMap.getDNbyID(details[1]).addInNode(SCHashMap.getIndexbyID(details[0]), Float.parseFloat(details[2]));
			}
			else {
				System.out.println("ERROR while loading CCNVD");
				System.exit(0);
			}
		}
	}

	/**
	 * Load all shortest paths from the matrix file to SC heaps
	 * @param filePath
	 * @throws IOException
	 */
	public static void LoadDistanceMatrixAndBuildMinHeap(String filePath) throws IOException {
		br = new BufferedReader(new FileReader(filePath));
		String line="";
		int dnIndex = 0;
		System.out.println("DemandNodeIndexMapSize : " + DNHashMap.size());
		System.out.println("ServiceNodeIndexMapSize : " + SCHashMap.size());
		//rows are demand nodes and columns are service centers
		while((line=br.readLine()) != null && !line.equals("")) {
			String[] lineSplit = line.split(",");
			DemandNode demandNode = DNHashMap.get(dnIndex);
			if(demandNode==null) {dnIndex++;continue;}
			for(int scIndex=0;scIndex<SCHashMap.size();scIndex++) {
				//System.out.println("i=" +i+" j="+j + " val=" + lineSplit[j]);
				/*
				 * demandNodeIndexMapping and serviceCenterIndexMapping starts from 0. 
				 * That's why j-size of total number of demand nodes.
				 * For more understanding check loadServiceCenter function.
				 * */
				if(!lineSplit[scIndex].contains("Infinite")) {
					float distance = Float.parseFloat(lineSplit[scIndex].trim());
					ServiceCenter sc = SCHashMap.get(scIndex);
					sc.addDistance(dnIndex, distance);
					MinHeapNode minHeapNode = new MinHeapNode(distance, dnIndex, scIndex);
					sc.addToMinHeap(minHeapNode);
				}
			}
			dnIndex++;
		}
		
		//Initialize the SC heap which has all the best demand nodes for each service center.
		//Loop size is number of service centers because one best DN is selected for each service center.
		for(int scIndex=0;scIndex<SCHashMap.size();scIndex++)
			SCHeap.updateSCheap(scIndex);
//		SCHeap.testPrintHeap();
//		SCHashMap.testPrintSCMinHeaps();


	}	
}
