package LBVDN;

import java.util.LinkedList;

public class DemandNode {
	private String demandNodeID;
	private Integer assignedTo;
	private LinkedList<AdjNode> inNodes;
	private LinkedList<AdjNode> outNodes;

	public DemandNode(String demandNodeID) {
		super();
		this.demandNodeID = demandNodeID;
		this.assignedTo = null;
		this.inNodes = new LinkedList<AdjNode>();
		this.outNodes = new LinkedList<AdjNode>();
	}

	public String getnodeID() {
		return this.demandNodeID;
	}

	public LinkedList<AdjNode> getInNodes() {
		return this.inNodes;
	}

	public LinkedList<AdjNode> getOutNodes() {
		return this.outNodes;
	}

	public void addOutNode(Integer adjNodeIndex, float weight) {
		this.outNodes.add(new AdjNode(adjNodeIndex, weight));

	}

	public void addInNode(Integer adjNodeIndex, float weight) {
		this.inNodes.add(new AdjNode(adjNodeIndex, weight));
	}
	
	public void addAssignedSC(Integer scID) {
		this.assignedTo = scID;
	}
	
	public void removeAssignedSC(Integer scID) {
		this.assignedTo = null;
	}
	
	public boolean isAssigned() {
		if(this.assignedTo == null) return false;
		return true;
	}
	
	public Integer getAssignedSC() {
		if(this.assignedTo == null) {
			System.out.println("Error: Vertex - assignedSC().");
			System.exit(0);
		};
		return this.assignedTo;
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