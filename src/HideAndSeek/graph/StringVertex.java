package HideAndSeek.graph;

public class StringVertex implements Comparable<StringVertex> {

	private static int nodes = -1;
	
	private int node;
	
	public StringVertex() {
		
		nodes++;
		
		node = nodes;
		
	}
	
	/**
	 * @return
	 */
	public int getNode() { 
		
		return node;
	
	}
	
	/**
	 * 
	 */
	public static void resetNodes() {
		
		nodes = -1;
		
	}
	
	public StringVertex(int node) {
		
		this.node = node;
		
	}
	
	public String toString() {
		
		return "v" + node;
		
	}
	
	public boolean equals(Object arg0) {
		
		//System.out.println("Comparing " + "v" + node + " and " + arg0.toString());
		
		return ("v" + node).equals(arg0.toString());
		
	}

	@Override
	public int compareTo(StringVertex o) {
		
		if (node < o.getNode()) { 
			
			return -1;
		
		} else if (node > o.getNode()) {
		
			return 1;
		
		} else {
		
			return 0;
		
		}
		
	}

}
