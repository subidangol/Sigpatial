package LBVDN;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import LBVDN.DemandNode.AdjNode;

public class ServiceCenter {
	private String serviceCenterID;
	private int maxCapacity;
	private Float penaltyRate;
	private LinkedList<AdjNode> inNodes;
	private LinkedList<AdjNode> outNodes;
	HashSet<Integer> allocations;
	private HashMap<Integer, Float> distances;
	private PriorityQueue<MinHeapNode> minheap;
	HashMap<Integer, HashMap<Integer, Integer>> boundaryVertices; //Hashmap<BV ID, Hashmap<Vertex ID, Reason>>

	public ServiceCenter(String serviceCenterID, int maxCapacity, Float penaltyRate) {
		this.serviceCenterID  = serviceCenterID;
		this.maxCapacity      = maxCapacity;
		this.penaltyRate      = penaltyRate;
		this.inNodes          = new LinkedList<AdjNode>();
		this.outNodes         = new LinkedList<AdjNode>();
		this.allocations      = new HashSet<>();
		this.distances        = new HashMap<Integer, Float>();
		this.boundaryVertices = new HashMap<Integer, HashMap<Integer, Integer>>();
		this.minheap          = new PriorityQueue<MinHeapNode>(1,new Comparator<MinHeapNode>() {
			@Override
			public int compare(MinHeapNode o1, MinHeapNode o2) {
				if(o1.getDistance() == o2.getDistance()){
					return o1.getDnID().compareTo(o2.getDnID());
				} else{
					return (int) (o1.getDistance()-o2.getDistance());
				}
			}
		});
	}

	public String getServiceCenterID() {
		return this.serviceCenterID;
	}
	
	public LinkedList<AdjNode> getInNodes() {
		return this.inNodes;
	}

	public LinkedList<AdjNode> getOutNodes() {
		return this.outNodes;
	}

	public void addOutNode(Integer adjNodeID, float weight) {
		this.outNodes.add(new AdjNode(adjNodeID, weight));

	}

	public void addInNode(Integer adjNodeID, float weight) {
		this.inNodes.add(new AdjNode(adjNodeID, weight));
	}
	
	public void addNewAllocation(Integer dnID){
		allocations.add(dnID);
	}
	
	public void removeAllocation(Integer dnID) {
		allocations.remove(dnID);
	}

	public HashSet<Integer> getAllocations() {
		return this.allocations;
	}

	public int getMaxCapacity() {
		return this.maxCapacity;
	}

	public Float getPenalty() {
		if(hasVacancy()) {
			return this.penaltyRate;		
		}
		return (float) 0;
	}   
	
	public Float getPenaltyRate() {
		return this.penaltyRate;
	}
	
	public Float totalPenaltyIncurred() {
		if(this.allocations.size()>this.maxCapacity)
			return this.penaltyRate * (this.allocations.size()-this.maxCapacity);
		else 
			return (float) 0;
	}
	
	public boolean hasVacancy() {
		if(this.allocations.size()<this.maxCapacity)
			return true;
		return false;
	}
	
	public void addDistance(Integer vertexID, Float distance) {
		this.distances.put(vertexID, distance);
	}
	
	public float getDistance(Integer vertexID) {
		if(this.distances.get(vertexID) == null) { 
			return Float.MAX_VALUE;
		}
		return this.distances.get(vertexID);
	}
	
	public int minHeapSize() {
		return this.minheap.size();
	}
	
	public PriorityQueue<MinHeapNode> getMinHeap() {
		return this.minheap;
	}
	
	public void addToMinHeap(MinHeapNode minHeapNode) {
		this.minheap.add(minHeapNode);
	}
	
	public MinHeapNode removeFromMinHeap() {
		return this.minheap.remove();
	}
	
	public Integer getDnIDfromMinHeapElement() {
		return this.minheap.element().getDnID();
	}
	
	public void updateMinCostHeap() {
		System.out.println("Empty function.");
	}

	public void testPrintHeap() {
		System.out.println("Service Center ID: "+this.serviceCenterID+". Number of nodes in minheap: "+this.minheap.size());
		System.out.println(this.minheap);
		System.out.println();
	}
	
	public void addBoundaryVertex(Integer boundaryVertexID, HashMap<Integer, Integer> vectorOfReasons) {
		this.boundaryVertices.put(boundaryVertexID, vectorOfReasons);
	}
	
	public void removeBoundaryVertex(Integer boundaryVertexID) {
		this.boundaryVertices.remove(boundaryVertexID);
	}
	
	public HashMap<Integer, HashMap<Integer, Integer>> getBoundaryVertices() {
		return this.boundaryVertices;
	}
	
	public boolean isDNAssignedtoThisSC(Integer vertexID) {
		return this.allocations.contains(vertexID);
	}

	public boolean isBoundaryVertex(Integer inNodeID) {
		return this.boundaryVertices.containsKey(inNodeID);
	}

	public boolean hasNodeasReasonforBV(Integer nodeID, Integer boundaryVertexID) {
		if(isBoundaryVertex(boundaryVertexID)) {
			return boundaryVertices.containsKey(nodeID);
		}
		return false;
	}
	
	public class AdjNode {
		public int adjVertexID;
		public float weight;

		AdjNode(int adjVertexID, float weight) {
			this.adjVertexID = adjVertexID;
			this.weight = weight;
		}
	}
	
}