package HideAndSeek.graph;

import org.jgrapht.graph.DefaultEdge;
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

	@Override
	public boolean equals(Object obj) {
		
		return getSource().equals(((StringEdge) obj).getSource()) && getTarget().equals(((StringEdge) obj).getTarget());
		
	}

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
