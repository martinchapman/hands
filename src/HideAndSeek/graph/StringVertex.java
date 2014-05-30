package HideAndSeek.graph;

public class StringVertex {

	private static int nodes = -1;
	private int node;
	
	public StringVertex() {
		
		nodes++;
		
		node = nodes;
		
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

}
