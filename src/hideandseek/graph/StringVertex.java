package hideandseek.graph;

import Utility.ComparatorResult;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + node;
		
		return result;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		StringVertex other = (StringVertex) obj;
		
		if (node != other.getNode()) return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(StringVertex o) {
		
		if (node < o.getNode()) { 
			
			return ComparatorResult.BEFORE;
		
		} else if (node > o.getNode()) {
		
			return ComparatorResult.AFTER;
		
		} else {
		
			return ComparatorResult.EQUAL;
		
		}
		
	}

}
