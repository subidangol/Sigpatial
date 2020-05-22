package LBVDN;
public class MinHeapNode {
	//stores the minimum distance of demandNode to particular Service center
	private float cost;
	//stores demand Node id
    private Integer dnIndex;
    //stores service center id of service center from which distance of demand node is minimum
    private Integer scIndex;
    
	public MinHeapNode(float distance, Integer dnIndex, Integer scIndex) {
		super();
		this.cost = distance;
		this.dnIndex = dnIndex;
		this.scIndex = scIndex;
	}
	
	@Override
	public String toString() {
		return "("+dnIndex+" to SC "+scIndex+"="+cost+")";
	}

	public float getDistance() {
		return cost;
	}
	
	public Integer getDnID() {
		return dnIndex;
	}
	
	public Integer getScID() {
		return scIndex;
	}

	public void increaseCost(Float penalty) {
		// TODO Auto-generated method stub
		this.cost+=penalty;
	}

}