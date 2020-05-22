package LBVDN;

import java.util.HashMap;

//Collection class to maintain all demand nodes
public class DNHashMap {
	//maintained for easy access to DNs using indexes that are unique integers
	public static HashMap<Integer, DemandNode> demandNodeIndexMapping = new HashMap<Integer, DemandNode>(); //key = dn index, value = corresponding demandNode object
	
	public static HashMap<String, Integer> demandNodeIDs = new HashMap<String, Integer>(); //key = dn ID, value = corresponding dn index

	public static HashMap<Integer, DemandNode> getAllVertices() {
		return demandNodeIndexMapping;
	}
	
	public static void add(Integer dnIndex, DemandNode demandNode) {
		demandNodeIndexMapping.put(dnIndex, demandNode);
		demandNodeIDs.put(demandNode.getnodeID(), dnIndex);
	}
	
	public static DemandNode get(Integer dnIndex) {
		return demandNodeIndexMapping.get(dnIndex);
	}

	public static DemandNode getDNbyID(String dnID) {
		return demandNodeIndexMapping.get(demandNodeIDs.get(dnID));
	}
	
	public static Integer getIndexbyID(String dnID) {
		return demandNodeIDs.get(dnID);
	}
	
	public static int size() {
		return demandNodeIndexMapping.size();
	}
	
	public static boolean contains(Integer dnIndex) {
		return demandNodeIndexMapping.containsKey(dnIndex);
	}
	
	public static boolean containsID(String dnID) {
		return demandNodeIDs.containsKey(dnID);
	}

	
}
