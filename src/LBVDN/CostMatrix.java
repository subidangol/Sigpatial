package LBVDN;

import java.util.HashMap;
import java.util.Iterator;

public class CostMatrix {

	public static float[][] costDiffMatrix; //matrix for differences in cost when the best boundary vertex is sent from SCi to SCj
	public static Integer[][] bestBVtoSendMatrix; //matrix for next boundary vertex IDs or demand node indexes

	public CostMatrix(int nSC) {
		costDiffMatrix = new float[nSC][nSC];
		bestBVtoSendMatrix = new Integer[nSC][nSC];
	}
	
	public static void updateCostMatrix() {
		for(int sc_i = 0; sc_i < SCHashMap.size(); sc_i++) {
			for(int sc_j = 0; sc_j < SCHashMap.size(); sc_j++) {
				Integer minBV = null;
				float minDist = Float.MAX_VALUE;
				if(sc_i != sc_j) {
					Iterator bvItr = SCHashMap.getBoundaryVerticesofSC(sc_i).entrySet().iterator();
					boolean hasBoundaryVertices = false;
					while (bvItr.hasNext()) { 
						hasBoundaryVertices = true;
						HashMap.Entry mapElement = (HashMap.Entry)bvItr.next(); 
						Integer bvID = (Integer) mapElement.getKey(); 
						float newDistance = SCHashMap.getDistanceFromSCtoDN(sc_j, bvID);
						if(newDistance < minDist) {
							minBV = bvID;
							minDist = newDistance;
						}
					}
					if(hasBoundaryVertices)
						costDiffMatrix[sc_i][sc_j] = minDist - SCHashMap.getDistanceFromSCtoDN(sc_i, minBV);
				} else {
					costDiffMatrix[sc_i][sc_j] = 0;
				}
				bestBVtoSendMatrix[sc_i][sc_j] = minBV;
			}
		}
	}

	public static void testPrintCostMatrices() {
		System.out.println("\nCost Matrix");
		for (int i = 0; i < costDiffMatrix.length; i++) {
		    for (int j = 0; j < costDiffMatrix[i].length; j++) {
		        System.out.print(costDiffMatrix[i][j] + " ");
		    }
		    System.out.println();
		}
		System.out.println("\nBest BV to send");
		for (int i = 0; i < bestBVtoSendMatrix.length; i++) {
		    for (int j = 0; j < bestBVtoSendMatrix[i].length; j++) {
		        System.out.print(bestBVtoSendMatrix[i][j] + " ");
		    }
		    System.out.println();
		}
		System.out.println();
	}

	
}
