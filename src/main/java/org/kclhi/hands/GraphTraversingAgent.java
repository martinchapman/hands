package org.kclhi.hands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.kclhi.hands.utility.ComparatorResult;
import org.kclhi.hands.utility.Utils;
import org.kclhi.hands.graph.GraphController;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

/**
 * @author Martin
 *
 */
public abstract class GraphTraversingAgent implements GraphTraverser {

	/**
	 * 
	 */
	protected GraphController<StringVertex, StringEdge> graphController;
	
	/**
	 * @return
	 */
	public GraphController<StringVertex, StringEdge> getGraphController() {
		
		return graphController;
		
	}

	/**
	 * 
	 */
	protected GraphTraverser responsibleAgent;
	
	public void setResponsibleAgent(GraphTraverser responsibleAgent) {
		
		this.responsibleAgent = responsibleAgent;
		
	}
	
	/**
	 * @return
	 */
	public GraphTraverser getResponsibleAgent() {
		
		return this.responsibleAgent;
		
	}
	
	/**
	 * 
	 */
	private ArrayList<StringVertex> hideLocations;
	
	/**
	 * @return
	 */
	protected ArrayList<StringVertex> hideLocations() {
		
		return hideLocations;
	
	}
	
	/**
	 * @param caller
	 * @return
	 */
	public ArrayList<StringVertex> requestHideLocations(GraphTraverser caller) {

		return hideLocations();
		
	}
	
	/**
	 * All hide locations encountered in a game.
	 */
	private ArrayList<StringVertex> allHideLocations;

	/**
	 * @return
	 */
	public ArrayList<StringVertex> allHideLocations() {
		
		return allHideLocations;
	
	}
	
	/**
	 * 
	 */
	private HashSet<StringVertex> uniqueHideLocations;
	
	/**
	 * @return
	 */
	public HashSet<StringVertex> uniqueHideLocations() {
		
		return uniqueHideLocations;
		
	}
	
	/**
	 * @return
	 */
	protected boolean automaticMove() {
		
		return true;
		
	}
	
	/**
	 * Used to save those nodes that shoud have been visited,
	 * if an automatic move is performed.
	 */
	protected ArrayList<StringVertex> queuedNodes;
	
	/**
	 * 
	 */
	private String ID;
	
	/**
	 * @return
	 */
	public String getID() {
		
		return name;
		
	}

	/**
	 * 
	 */
	private String name;
	
	/**
	 * @return
	 */
	public String getName() {
		
		return name;
		
	}

	/**
	 * @param name
	 */
	private void setName(String name) {
		
		this.name = name;
	
	}
	
	/**
	 * @param graphController
	 * @param responsibleAgent
	 * @param name
	 */
	public GraphTraversingAgent(GraphController<StringVertex, StringEdge> graphController, String name, GraphTraverser responsibleAgent) {
		
		this.graphController = graphController;
		
		if ( uniquelyVisitNodes == true ) {
		
			uniquelyVisitedNodes = new HashSet<StringVertex>();
		
			uniquelyVisitedEdges = new HashSet<StringEdge>();
			
		}
		
		// If a responsible agent is not supplied, check with a name has been,
		// otherwise default to the name of this class.
		if ( responsibleAgent == null ) { 
			
			this.responsibleAgent = this;
			
			if ( name.equals("") ) {
				
				setName(this.getClass().toString().substring(this.getClass().toString().lastIndexOf('.') + 1, this.getClass().toString().length()));
			
			} else {
				
				setName(name);
				
			}
		
		// Otherwise name this class after the responsble agent
		} else {
			
			this.responsibleAgent = responsibleAgent;
			
			setName(this.responsibleAgent.getClass().toString().substring(this.responsibleAgent.getClass().toString().lastIndexOf('.') + 1, this.responsibleAgent.getClass().toString().length()));
			
		}
		
		// ID according to time of generation
		ID = "" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + Math.random() * 100;

		graphController.registerTraversingAgent(this.responsibleAgent);
		
		// Record of where hidden items have been found
		hideLocations = new ArrayList<StringVertex>();
		
		// List of nodes that have been explored
		exploredNodes = new Hashtable<StringVertex, Integer>();
		
		queuedNodes = new ArrayList<StringVertex>();
		
		// Record of where hidden items have been found (in the whole game)
		allHideLocations = new ArrayList<StringVertex>();
		
		//
		uniqueHideLocations = new HashSet<StringVertex>();
		
	}
	
	/**
	 * @param graphController
	 * @param responsibleAgent
	 */
	public GraphTraversingAgent(GraphController<StringVertex, StringEdge> graphController, GraphTraverser responsibleAgent) {
		
		this(graphController,  "", responsibleAgent);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 */
	public GraphTraversingAgent(GraphController<StringVertex, StringEdge> graphController, String name) {
		
		this(graphController, name, null);
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 */
	public GraphTraversingAgent(GraphController<StringVertex, StringEdge> graphController) {
		
		this(graphController, "", null);
		
	}
	
	/**
	 * @return
	 */
	protected StringVertex firstNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		return graphController.vertexSet().toArray(vertices)[0];
				
	}
	
	/**
	 * @return
	 */
	protected StringVertex randomNode() {
		
		StringVertex[] vertices = new StringVertex[graphController.vertexSet().size()];
		
		graphController.vertexSet().toArray(vertices);
		
		return vertices[(int)(Math.random() * vertices.length)];
		
	}
	
	/**
	 * 
	 */
	protected boolean uniquelyVisitNodes = true;
	
	/**
	 * 
	 */
	private HashSet<StringVertex> uniquelyVisitedNodes; 
	
	/**
	 * @param node
	 */
	protected void addUniquelyVisitedNode(StringVertex node) {
		
		uniquelyVisitedNodes.add(node);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#uniquelyVisitedNodes()
	 */
	@Override
	final public HashSet<StringVertex> uniquelyVisitedNodes() {
		
		return uniquelyVisitedNodes;
		
	}
	
	/**
	 * 
	 */
	private HashSet<StringEdge> uniquelyVisitedEdges;
	
	/**
	 * @param node
	 */
	protected void addUniquelyVisitedEdge(StringEdge edge) {
		
		uniquelyVisitedEdges.add(edge);
		
	}
	

	@Override
	public HashSet<StringEdge> uniquelyVisitedEdges() {
		
		return uniquelyVisitedEdges;
		
	}
	
	/**
	 * @param edge
	 * @return
	 */
	protected boolean visitedEdge(StringEdge edge) {
		
		return uniquelyVisitedEdges.contains(edge);
		
	}
	
	/**
	 * 
	 */
	private Hashtable<StringVertex, Integer> exploredNodes;

	/**
	 * @return
	 */
	public Hashtable<StringVertex, Integer> exploredNodesTable() {
	
		return exploredNodes;
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverser#exploredNodesSize()
	 */
	public int exploredNodesSize() {
		
		int exploredNodesSize = 0;
		
		for ( Entry<StringVertex, Integer> entry : exploredNodes.entrySet() ) {
			
			exploredNodesSize += entry.getValue();
		}
		
		return exploredNodesSize;
	
	}
	
	/**
	 * @param startNode
	 */
	protected void atStart(StringVertex startNode) {
		
		Utils.talk(toString(), "Start node: " + startNode);
		
		currentNode = startNode;
		
	}
	
	protected void addExploredNode(StringVertex node) {
		
		if ( exploredNodes.containsKey(node) ) {
			
			int frequency = exploredNodes.get(node);
			
			exploredNodes.put(node, ++frequency);
			
		} else {
			
			exploredNodes.put(node, 1);
			
		}
		
	}
	
	/**
	 * @param node
	 */
	protected void addAllExploredNodes(Hashtable<StringVertex, Integer> nodeToFrequency) {
		
		for ( Entry<StringVertex,Integer> nodeToFrequencyEntry : nodeToFrequency.entrySet() ) {
			
			if ( exploredNodes.containsKey(nodeToFrequencyEntry.getKey()) ) {
				
				int frequency = exploredNodes.get(nodeToFrequencyEntry.getKey());
				
				exploredNodes.put(nodeToFrequencyEntry.getKey(), frequency + nodeToFrequencyEntry.getValue());
				
			} else {
				
				exploredNodes.put(nodeToFrequencyEntry.getKey(), 1);
				
			}
			
		}
		
		
	}
	
	/**
	 * 
	 */
	protected void atNode() {
	
		Utils.talk(toString(), "At node " + currentNode);
		
		addExploredNode(currentNode);
		
		addUniquelyVisitedNode(currentNode);
		
	}
	
	/**
	 * @param nextNode
	 */
	protected void atNextNode(StringVertex nextNode) {
	
		addUniquelyVisitedEdge(graphController.getEdge(currentNode, nextNode));
		
		currentNode = nextNode;
	
	}
	
	/**
	 * Print all important current attributes for this player.
	 * @return
	 */
	protected String getStatus() {
		
		TreeSet<StringVertex> copy_uniquelyVisitedNodes = new TreeSet<StringVertex>(uniquelyVisitedNodes);
		
		//copy_uniquelyVisitedNodes.removeAll(new HashSet<StringVertex>(graphController.vertexSet()));
		
		return "\n\nTraverser: " + toString() + "\n" +
			   "Nodes in graph: " + graphController.vertexSet().size() + "\n" +
			   "Explored nodes: " + exploredNodes.size() + "\n" +
		       "Uniquely visited nodes: " + uniquelyVisitedNodes.size() + "\n" +
		       "Unvisited nodes: " + copy_uniquelyVisitedNodes + "\n" +
			   "Hide locations: " + hideLocations + " (" + hideLocations.size() + ")\n" +
		       "Current node: " + currentNode + "\n" +
			   "Connected node: " + getConnectedEdges(currentNode) + " (" + getConnectedEdges(currentNode).size() + ")";
		
	}
	
	/**
	 * Working with connectedNode in order to determine how nodes
	 * from a set are selected. Default is random.
	 * 
	 * @param connectedEdges
	 * @return
	 */
	protected StringEdge getConnectedEdge(StringVertex currentNode, List<StringEdge> connectedEdges) {
	
		return connectedEdges.get((int)(Math.random() * connectedEdges.size()));
		
	}
	
	/**
	 * Return the edges linked to the specified node. Default is
	 * all connected nodes.
	 * 
	 * @param currentNode
	 * @return
	 */
	protected List<StringEdge> getConnectedEdges(StringVertex vertex) {
	
		return new ArrayList<StringEdge>(graphController.edgesOf(responsibleAgent, vertex));
		
	}
	
	/**
	 * Select a connected node according to how edges are sorted, and how
	 * edges are selected from this set.
	 * 
	 * A wrapper method, of sorts, to enforce additional constraints such a
	 * no backtracking.
	 * 
	 * @param currentNode
	 * @return
	 */
	protected StringVertex connectedNode(StringVertex currentNode) {
		
		List<StringEdge> connectedEdges = getConnectedEdges(currentNode);
		
		StringEdge connectedEdge = null;
		
		StringVertex target = null;
		
		HashSet<StringEdge> selectedInThisSession = new HashSet<StringEdge>();
		
		do {
			
			// If we have tried all outgoing edges available (more a programmatic choice than a strategic one), return random.
			if (selectedInThisSession.size() == connectedEdges.size()) {
				
				target = edgeToTarget(connectedEdges.get((int)(Math.random() * connectedEdges.size())), currentNode);
				
				break;
				
			}
			
			connectedEdge = getConnectedEdge(currentNode, connectedEdges);
			
			target = edgeToTarget(connectedEdge, currentNode);
			
			selectedInThisSession.add(connectedEdge);
			
					// Otherwise, loop while not allowed to repeat nodes
		} while (   uniquelyVisitNodes == true && uniquelyVisitedNodes.contains( target ) );
		
		addUniquelyVisitedNode(target);
		
		addUniquelyVisitedEdge(connectedEdge);
		
		return target;
		
	}
	
	/**
	 * @param connectedEdge
	 * @param currentNode
	 * @return
	 */
	protected StringVertex edgeToTarget(StringEdge connectedEdge, StringVertex currentNode) {
		
		if (connectedEdge.getSource().equals(currentNode)) {
			
			return connectedEdge.getTarget();
			
		} else if (connectedEdge.getTarget().equals(currentNode)) {
			
			return connectedEdge.getSource();
			
		}
		
		return null;
		
	}
	
	/**
	 * 
	 */
	protected StringVertex currentNode = null;
	
	/**
	 * @return
	 */
	public StringVertex currentNode() {
		
		if (currentNode == null) {
			
			return randomNode();
			
		} else {
			
			return currentNode;
			
		}
		
	}
	
	/**
	 * Record of the number of rounds passed 
	 */
	protected int roundsPassed = 0;
	
	/**
	 * @return
	 */
	public int roundsPassed() {
		
		return roundsPassed;
	
	}

	/**
	 * 
	 */
	public void endOfRound() { 
		
		roundsPassed++;

	}
	
	/**
	 * Flags whether this strategy develops over all rounds.
	 * Required to test correct number of times (i.e. a single
	 * set of rounds isn't sufficient if this is true).
	 */
	protected boolean strategyOverRounds = false;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#getStrategyOverRounds()
	 */
	@Override
	public boolean strategyOverRounds() {
		
		return strategyOverRounds;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#setStrategyOverRounds(boolean)
	 */
	@Override
	public void strategyOverRounds(boolean strategyOverRounds) {
		
		this.strategyOverRounds = strategyOverRounds;
		
	}
	
	/**
	 * 
	 */
	private boolean playing;
	
	/**
	 * 
	 */
	public void startPlaying() {
		
		playing = true;
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#isPlaying()
	 */
	@Override
	public boolean isPlaying() {
		
		return playing;
		
	}
	
	/**
	 * 
	 */
	public void endOfGame() {
		
		playing = false;
		
		roundsPassed = 0;
		
		uniquelyVisitedNodes.clear();
		
		uniquelyVisitedEdges.clear();
		
		allHideLocations.clear();
		
		uniqueHideLocations.clear();
		
		hideLocations.clear();
		
		exploredNodes.clear();
		
		queuedNodes.clear();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public abstract StringVertex nextNode(StringVertex currentNode);
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#startNode()
	 */
	@Override
	public abstract StringVertex startNode();

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#printGameStats()
	 */
	@Override
	public String printGameStats() {
		
		return "";
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraverserTemplate#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		return "";
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.hider.HiderTemplate#toString()
	 */
	@Override
	public String toString() {
		
		return "t" + getName();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		
		return result;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		// If a class equals itself, clearly all fields will be equal
		if (responsibleAgent == ((GraphTraverser)((GraphTraverser)obj)).getResponsibleAgent()) return true;
		
		if (responsibleAgent.getClass() != ((GraphTraverser)obj).getClass()) return false;
		
		GraphTraversingAgent other = (GraphTraversingAgent) ((GraphTraverser)obj);
		
		if (responsibleAgent.getName() == null) {
			
			if (other.getResponsibleAgent().getName() != null)
			
				return false;
		
		} else if (!getName().equals(other.getResponsibleAgent().getName())) {
			
			return false;
			
		}
		
		return true;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(GraphTraverser traverser) {
		
		if ( getName().equals(traverser.getName()) ) {
			
			return ComparatorResult.EQUAL;
			
		} else if ( getName().compareTo(traverser.getName()) >= ComparatorResult.AFTER ) {
			
			return ComparatorResult.AFTER;
			
		} else {
			
			return ComparatorResult.BEFORE;
			
		}
	    
	    
	}

	/**
	 * Allows for the data from another strategy to be merged 
	 * into this one.
	 * 
	 * MDC 15/7 Should be addAll or just straight assignment?
	 * 
	 * @param traverser
	 */
	public void mergeOtherTraverser(GraphTraverser traverser) {
		
		try {
			
			this.currentNode = traverser.currentNode();
		
		} catch ( Exception e ) {
			
			System.err.println(this.name);
			
			System.err.println(this.currentNode + " " + traverser.currentNode());
			
		}
		
		this.roundsPassed = traverser.roundsPassed();
		
		this.uniquelyVisitedEdges.addAll(traverser.uniquelyVisitedEdges());
		
		this.uniquelyVisitedNodes.addAll(traverser.uniquelyVisitedNodes());
		
		addAllExploredNodes(traverser.exploredNodesTable());
		
		this.hideLocations.addAll(traverser.requestHideLocations(responsibleAgent));
		
		this.allHideLocations.addAll(traverser.allHideLocations());
		
		uniqueHideLocations.clear();
		
		Utils.talk(toString(), "traverser.uniqueHideLocations() " + traverser.uniqueHideLocations());
		this.uniqueHideLocations.addAll(traverser.uniqueHideLocations());	
		
	}

}
