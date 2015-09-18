package HideAndSeek.hider.repeatgame.deceptive;

import java.util.ArrayList;
import java.util.TreeSet;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.singleshot.random.RandomSet;
import Utility.Utils;

public class DeceptiveNew extends RandomSet {

	public DeceptiveNew(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, GraphTraverser responsibleAgent, int deceptionDuration) {
	
		this(graphController, "", numberOfHideLocations, responsibleAgent, deceptionDuration);
	
	}

	public DeceptiveNew(GraphController<StringVertex, StringEdge> graphController, int numberOfHideLocations, int deceptionDuration) {
		
		this(graphController, "", numberOfHideLocations, null, deceptionDuration);
		
	}

	public DeceptiveNew(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int deceptionDuration) {
		
		this(graphController, name, numberOfHideLocations, null, deceptionDuration);
		
	}
	
	/**
	 * 
	 */
	private ArrayList<StringVertex> deceptiveNodes;
	
	/**
	 * 
	 */
	private int deceptionDuration;
	
	public DeceptiveNew(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, GraphTraverser responsibleAgent, int deceptionDuration) {
		
		super(graphController, name, numberOfHideLocations, responsibleAgent);
		
		deceptiveNodes = new ArrayList<StringVertex>();
		
		this.deceptionDuration = deceptionDuration;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#hideHere(HideAndSeek.graph.StringVertex)
	 */
	public boolean hideHere(StringVertex currentNode) {
		
		if ( deceptiveNodes.size() == 0 ) {
			
			return true;
			
		} else {
			
			return super.hideHere(currentNode);
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#nextNode(HideAndSeek.graph.StringVertex)
	 */
	public StringVertex nextNode(StringVertex currentNode) {
		
		if ( deceptiveNodes.size() == 0 ) {
			
			return super.connectedNode(currentNode);
			
		} else {
		
			return super.nextNode(currentNode);
					
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.singleshot.random.RandomSet#endOfRound()
	 */
	public void endOfRound() {
		
		if ( deceptiveNodes.size() == 0 ) {
			
			deceptiveNodes.addAll(hideLocations());
			
			Utils.talk(toString(), "Setting deceptive nodes " + deceptiveNodes);
			
		}
		
		super.endOfRound();
		
		Utils.talk(toString(), roundsPassed + " " + deceptionDuration);
		
		if ( roundsPassed < deceptionDuration ) {
			
			populateHideSet(deceptiveNodes);
			
		} else {
			
			populateHideSet(createRandomSet(numberOfHideLocations, new TreeSet<StringVertex>(deceptiveNodes)));
			
		}
		
	}

}
