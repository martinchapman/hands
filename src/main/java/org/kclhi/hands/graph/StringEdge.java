package org.kclhi.hands.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class StringEdge extends DefaultWeightedEdge implements Comparable<StringEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public StringVertex getSource() {
		
		// TODO Auto-generated method stub
		return (StringVertex) super.getSource();
	}

	@Override
	public StringVertex getTarget() {
		// TODO Auto-generated method stub
		return (StringVertex) super.getTarget();
	}
	
	public double getWeight() {
		
		return super.getWeight();
		
	}
	
	public String toString() {
		
		return "(" + getSource() + " : " + getTarget() + ") " + getWeight();
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
		
		result = prime * result + ((getTarget() == null) ? 0 : getTarget().hashCode());
		
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
		
		StringEdge other = (StringEdge) obj;
		
		if (getSource() == null) {
			
			if (other.getSource() != null) return false;
		
		} else if (!getSource().equals(other.getSource())) {
			
			return false;
			
		}
		
		if (getTarget() == null) {
			
			if (other.getTarget() != null) return false;
			
		} else if (!getTarget().equals(other.getTarget())) {
			
			return false;
			
		}
		
		return true;
		
	}
	
	/*@Override
	public boolean equals(Object obj) {
		
		return getSource().equals(((StringEdge) obj).getSource()) && getTarget().equals(((StringEdge) obj).getTarget());
		
	}*/

	@Override
	public int compareTo(StringEdge o) {
		
		if ( getWeight() < o.getWeight() ) {
			
			return -1;
			
		} else if ( getWeight() > o.getWeight() ) {
			
			return 1;
			
		} else {
		
			return -1;
		
		}
		
	}

}
