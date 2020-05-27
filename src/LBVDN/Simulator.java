package LBVDN;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Simulator {
	static String datasetType = "Dataset3";
	static String totalCapacityRatio = "400";
	static String scFileName = "./Resource/"+totalCapacityRatio+"/ServiceCenter.txt";
	static String allVFileName = "./Resource/"+totalCapacityRatio+"/nodes.txt";
	static String edgesFileName = "./Resource/"+totalCapacityRatio+"/edges.txt";
	static String distanceFileName = "./Resource/"+totalCapacityRatio+"/CostMatrix.txt";

	static float objectiveFunctionCost = 0;
	static float totalPenalty = 0;
	static double startTime;
	static double endTime;
 
	public static void main(String[] args) throws IOException{ 
		PreProcessor.loadAllUniqueServiceCenters(scFileName);
		PreProcessor.loadAllVertices(allVFileName);
		PreProcessor.loadCCNVD(edgesFileName);
		PreProcessor.LoadDistanceMatrixAndBuildMinHeap(distanceFileName);
		startTime = System.nanoTime();
		startAssignment();
	}
	
	public static void assign(Integer dnIndex, Integer scIndex) {
		ServiceCenter serviceCenter = SCHashMap.get(scIndex);
		objectiveFunctionCost += serviceCenter.getDistance(dnIndex);
		serviceCenter.addNewAllocation(dnIndex);
		DNHashMap.get(dnIndex).addAssignedSC(scIndex);
	}

	public static void unassign(Integer dnIndex) {
		Integer scIndex = DNHashMap.get(dnIndex).getAssignedSC();	
		ServiceCenter serviceCenter = SCHashMap.get(scIndex);
		serviceCenter.removeAllocation(dnIndex);
		DNHashMap.get(dnIndex).removeAssignedSC(scIndex);
		objectiveFunctionCost -= serviceCenter.getDistance(dnIndex);
	}

	public static void startAssignment() throws IOException {	
		int count = 0;
		boolean hasBeenPenalized = false;
		while(SCHeap.size() != 0) {
			MinHeapNode minHeapNode = SCHeap.remove();
			Integer dnIndex = minHeapNode.getDnID();
			Integer scIndex = minHeapNode.getScID();

			while(DNHashMap.get(dnIndex).isAssigned()) {
				SCHeap.updateSCheap(scIndex);
				if(SCHeap.size() == 0) endAndDisplayResults(count);
				minHeapNode = SCHeap.remove();
				dnIndex = minHeapNode.getDnID();
				scIndex = minHeapNode.getScID();
			}
			
			if(!hasBeenPenalized && SCHashMap.get(scIndex).getPenalty()>0) hasBeenPenalized = true;
			
			updateAllotmentandMasterHashMap(dnIndex, scIndex);
			SCHeap.updateSCheap(scIndex);
			if(hasBeenPenalized) {
//				System.out.println("Penalty! "+dnIndex+" assigned to "+scIndex);
				CostMatrix.updateCostMatrix();
//				CostMatrix.testPrintCostMatrices();
				if(Bellman.isNegativeMinCycleDetected(scIndex))
					startReassigningMinCycle(Bellman.getMinCycle(), scIndex);
				CostMatrix.updateCostMatrix();
				if(Bellman.isNegativeMinPathDetected(scIndex))
					startReassigningMinPath(Bellman.getMinPath(), scIndex);
			}
			count++;
			System.out.println(count);
		}
		endAndDisplayResults(count);	
	}

	public static void updateAllotmentandMasterHashMap(Integer dnIndex, Integer new_scIndex) {
		if(DNHashMap.get(dnIndex).isAssigned()) {
			Integer old_scIndex = DNHashMap.get(dnIndex).getAssignedSC();
			unassign(dnIndex);
			for(int index = 0; index<DNHashMap.get(dnIndex).getInNodes().size(); index++) {
				Integer inNodeID = DNHashMap.get(dnIndex).getInNodes().get(index).adjVertexID;
				if(SCHashMap.get(old_scIndex).isDNAssignedtoThisSC(inNodeID)) {
					SCHashMap.isBVofSC(inNodeID, old_scIndex);
				}
			}
		}

		assign(dnIndex, new_scIndex);

		for(int index = 0; index<DNHashMap.get(dnIndex).getInNodes().size(); index++) {
			Integer inNodeID = DNHashMap.get(dnIndex).getInNodes().get(index).adjVertexID;
			if(DNHashMap.contains(inNodeID) && DNHashMap.get(inNodeID).isAssigned() && 
					SCHashMap.get(new_scIndex).isBoundaryVertex(inNodeID) &&
					SCHashMap.get(new_scIndex).hasNodeasReasonforBV(dnIndex, inNodeID)) {
				SCHashMap.isBVofSC(inNodeID, new_scIndex);
			}
		}		
		SCHashMap.isBVofSC(dnIndex, new_scIndex);
	}

	public static void startReassigningMinCycle(List<String> minCycle, Integer anchor) {
		Integer sc_i = anchor;
		Integer sc_j = Integer.parseInt(minCycle.get(1));

		for(int i = 1; i < minCycle.size()-1; i++) {
//			System.out.println(sc_i+" "+sc_j);
			Integer bvToSend = CostMatrix.bestBVtoSendMatrix[sc_i][sc_j];
			unassign(bvToSend);
//			System.out.println(bvToSend +" assigned to "+sc_j);
			updateAllotmentandMasterHashMap(bvToSend, sc_j);
//			SCHeap.updateSCheapAfterReassignment(sc_j);

			sc_i = Integer.parseInt(minCycle.get(i));
			if(i == minCycle.size()-2) {
				sc_j = anchor;
			} else {
				sc_j = Integer.parseInt(minCycle.get(i+1));
			}
		}

//		System.out.println(sc_i+" "+sc_j);
		Integer bvToSend = CostMatrix.bestBVtoSendMatrix[sc_i][sc_j];
		unassign(bvToSend);
//		System.out.println(bvToSend +" assigned to "+sc_j);
		updateAllotmentandMasterHashMap(bvToSend, sc_j);
//		SCHeap.updateSCheapAfterReassignment(sc_j);
//		System.out.println();
	}

	/**
	 * @param minCycle
	 * @param anchor
	 */
	public static void startReassigningMinPath(List<String> minCycle, Integer anchor) {
		Integer sc_i = anchor;
		Integer sc_j = Integer.parseInt(minCycle.get(1));

		for(int i = 1; i < minCycle.size()-2; i++) {
//			System.out.println(sc_i+" "+sc_j);
			Integer bvToSend = CostMatrix.bestBVtoSendMatrix[sc_i][sc_j];
			unassign(bvToSend);
//			System.out.println(bvToSend +" assigned to "+sc_j);
			updateAllotmentandMasterHashMap(bvToSend, sc_j);
//			SCHeap.updateSCheapAfterReassignment(sc_j);

			sc_i = Integer.parseInt(minCycle.get(i));
			sc_j = Integer.parseInt(minCycle.get(i+1));
		}
//		System.out.println();
	}

	private static void endAndDisplayResults(int count) throws IOException {
		endTime = System.nanoTime();
		printAllInformation();
//		System.out.println("assignment count: "+count);
		System.out.println("objectiveFunctionCost: "+objectiveFunctionCost);
		System.out.println("totalPenalty: "+totalPenalty);
		
		System.out.println("Total Execution time in ns = " + (endTime - startTime));
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("./"+datasetType+"_"+totalCapacityRatio+"_results.txt"));
		String line = "Bellman "+datasetType+" "+totalCapacityRatio+"\n";
		line += "objectiveFunctionCost: "+objectiveFunctionCost+"\n";
		line += "totalPenalty: "+totalPenalty+"\n";
		line += "Total Execution time in ns = " + (endTime - startTime);
		bw.write(line);
		bw.newLine();
		bw.close();
		
		System.exit(0);
	}
	
	public static void printAllInformation() {
		System.out.println("------------------------------------------------");
		for(Map.Entry<Integer, ServiceCenter> entry : SCHashMap.serviceCenterIndexMapping.entrySet()) {
			System.out.println("Service Center : " + entry.getValue().getServiceCenterID() + " Max Capacity : " + entry.getValue().getMaxCapacity() + " Penalty : " + entry.getValue().totalPenaltyIncurred());
			totalPenalty += entry.getValue().totalPenaltyIncurred();
			System.out.print("Demand Node Allocated : ");
			for(Integer demandNode : entry.getValue().allocations) {
				System.out.print(DNHashMap.get(demandNode).getnodeID()+", ");
			}
			System.out.println();
//			System.out.println("Boundary Vertices :");
//			for(DemandNode demandNode : entry.getValue().boundaryVertices) {
//				System.out.println("Boundary Vertex : " + demandNode.dnid);
//			}
			System.out.println("\n------------------------------------------------");
		}
		
	}
	
	
}