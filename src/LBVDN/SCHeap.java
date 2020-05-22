package LBVDN;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class SCHeap {
	private static PriorityQueue<MinHeapNode> minDistHeap = new PriorityQueue<MinHeapNode>(1,new Comparator<MinHeapNode>() {
		@Override
		public int compare(MinHeapNode o1, MinHeapNode o2) {
			if(o1.getDistance() == o2.getDistance()){
				return o1.getDnID().compareTo(o2.getDnID());
			} else {
				return (int) (o1.getDistance()-o2.getDistance());
			}
		}
	});

	public static int size() {
		return minDistHeap.size();
	}

	public static void add(MinHeapNode minHeapNode) {
		minDistHeap.add(minHeapNode);
	}

	public static MinHeapNode remove() {
		return minDistHeap.remove();
	}

	public static void updateSCheap(Integer scID) {
		ServiceCenter sc = SCHashMap.get(scID);
		if(sc.minHeapSize() == 0) return;
		MinHeapNode forHeap = sc.removeFromMinHeap();
		minDistHeap.add(forHeap); //removed from SC and added to the heap	
	}

	public static void updateSCheapAfterReassignment(Integer scID) {
		Iterator<MinHeapNode> value = minDistHeap.iterator(); 
		// Displaying the values after iterating through the queue 
		System.out.println("The iterator values are: "); 
		while (value.hasNext()) { 
			System.out.println(value.next()); 
		} 
	}

	public static void testPrintHeap() {
		System.out.println("Test Print: SCHeap best DNs to be assigned: [<dn_id> to SC <sc_id>=<distance>]");
		System.out.println(minDistHeap);
		//		System.out.println("Best demand for "+bestDNforSC.getScID()+" is "+bestDNforSC.getDnID());
	}
}
