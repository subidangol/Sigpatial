package LBVDN;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Collection class to maintain all service centers
public class SCHashMap {
	//maintained for easy access to SCs using indexes that are unique integers
	static HashMap<Integer, ServiceCenter> serviceCenterIndexMapping = new HashMap<Integer, ServiceCenter>(); //key = service center index, value = corresponding serviceCenter object
	
	public static HashMap<String, Integer> serviceCenterIDs = new HashMap<String, Integer>(); //key = service center ID, value = corresponding service center index

	public static void addSC(int scIndex, ServiceCenter serviceCenter) {
		serviceCenterIndexMapping.put(scIndex, serviceCenter);
		serviceCenterIDs.put(serviceCenter.getServiceCenterID(), scIndex);
	}
	
	public static ServiceCenter get(Integer scIndex) {
		return serviceCenterIndexMapping.get(scIndex);
	}
	
	public static int size() {
		return serviceCenterIndexMapping.size();
	}
	
	public static boolean contains(Integer scIndex) {
		return serviceCenterIndexMapping.containsKey(scIndex);
	}
	
	public static boolean containsID(String scID) {
		return serviceCenterIDs.containsKey(scID);
	}

	public static ServiceCenter getSCbyID(String scID) {
		return serviceCenterIndexMapping.get(serviceCenterIDs.get(scID));
	}
	
	public static Integer getIndexbyID(String dnID) {
		return serviceCenterIDs.get(dnID);
	}
	
	public static float getDistanceFromSCtoDN(int scIndex, Integer dnIndex) {
		return serviceCenterIndexMapping.get(scIndex).getDistance(dnIndex);
	}
	
	public static HashMap<Integer, HashMap<Integer, Integer>> getBoundaryVerticesofSC(Integer scIndex) {
		return serviceCenterIndexMapping.get(scIndex).getBoundaryVertices();
	}
	

	/**
	 * Checks if dn is a boundary vertex for given sc and updates the boundary vertex hashmap for that sc
	 * @param dnIndex
	 * @param scIndex
	 * @return bool if dnID is a boundar vertex of SC
	 */
	public static boolean isBVofSC(Integer dnIndex, Integer scIndex) {
		if(serviceCenterIndexMapping.get(scIndex).isDNAssignedtoThisSC(dnIndex)) {
			HashMap<Integer, Integer> vectorOfReasons = new HashMap<Integer, Integer>();
			for(int index = 0; index<DNHashMap.get(dnIndex).getOutNodes().size(); index++) {
				Integer outNodeID = DNHashMap.get(dnIndex).getOutNodes().get(index).adjVertexID;
				if(SCHashMap.contains(outNodeID))
					vectorOfReasons.put(outNodeID, 2);
				else if(DNHashMap.get(outNodeID).isAssigned() && 
						(scIndex != DNHashMap.get(outNodeID).getAssignedSC()))
					vectorOfReasons.put(outNodeID, 1);
				else
					vectorOfReasons.put(outNodeID, 3);	
			}
			if(vectorOfReasons.size() > 0) {
				serviceCenterIndexMapping.get(scIndex).addBoundaryVertex(dnIndex, vectorOfReasons);
				return true;
			}
		} 
		serviceCenterIndexMapping.get(scIndex).removeBoundaryVertex(dnIndex);
		return false;
	}
	
	public static void testPrintSCMinHeaps() {
		Iterator it = serviceCenterIndexMapping.entrySet().iterator();
		System.out.println("");
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        ((ServiceCenter) pair.getValue()).testPrintHeap();
	    }
	}

	

}
