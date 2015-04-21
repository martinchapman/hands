package HideAndSeek.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.VertexFactory;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;
import org.jgrapht.graph.DefaultWeightedEdge;

import HideAndSeek.GraphTraverser;
import HideAndSeek.Main;
import HideAndSeek.hider.Hider;
import HideAndSeek.seeker.Seeker;
import Utility.Metric;
import Utility.Utils;

/**
 * Encapsulates search graph, controlling the amount of
 * information that is released to hiders and searchers.
 *
 * @author Martin
 *
 * @param <V>
 * @param <E>
 */
public class GraphController<V, E> {

	/**
	 * 
	 */
	private HiddenObjectGraph<StringVertex, StringEdge> graph;
	
	/**
	 * 
	 */
	private TopologyProperties<StringVertex, StringEdge> topologyProperties;
	
	/**
	 * 
	 */
	private double fixedOrUpperValue;
	
	/**
	 * @return
	 */
	public double getFixedOrUpperValue() {
		
		return fixedOrUpperValue;
	
	}
	
	/**
	 * 
	 */
	private String fixedOrUpperBound;
	
	/**
	 * @return
	 */
	public String getFixedOrUpperBound() {
		
		return fixedOrUpperBound;
	
	}
	
	/**
	 * 
	 */
	private int edgeTraversalValue;
	
	/**
	 * @return
	 */
	public int getEdgeTraversalValue() {
		
		return edgeTraversalValue;
	
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return "GraphController";
		
	}
	
	/**
	 * @param graph
	 */
	public GraphController(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		this.fixedOrUpperValue = fixedOrUpperValue;
		
		this.fixedOrUpperBound = fixedOrUpperBound;
		
		this.edgeTraversalValue = edgeTraversalDecrement;
		
		/**************************
    	 * 
    	 * Set up search graph
    	 * 
    	 * * * * * * * * * * * * */
		
		ConnectivityInspector<StringVertex, StringEdge> con;
		
		do {
			
			// ~MDC 22/1/15 Messy?
			StringVertex.resetNodes();
			
			graph = new HiddenObjectGraph<StringVertex, StringEdge>(StringEdge.class);
			
			graph.setEdgeTraversalDecrement(edgeTraversalDecrement);
			
			GraphGenerator<StringVertex, StringEdge, StringVertex> generator = null;
			
			// Select generator
			
			if (	topology.equals("ring")	  ) {
		    	
		        generator = new RingGraphGenerator<StringVertex, StringEdge>(numberOfVertices);
		        
	    	} else if (	   topology.equals("random")    ) {
	    		
	    		generator = new RandomGraphGenerator<StringVertex, StringEdge>(numberOfVertices, numberOfVertices * 3);
	    		
	    	} else if (    topology.equals("scalefree")    ) {
	    		
	    		generator = new ScaleFreeGraphGenerator<StringVertex, StringEdge>(numberOfVertices);
	    		
	    	} else if (    topology.equals("complete") ) {
	    		
	    		generator = new CompleteGraphGenerator<StringVertex, StringEdge>(numberOfVertices);
	    		
	    	}
			
			
			// Generate graph
			
			Utils.talk("Graph Controller", "Generating graph.");
			
			VertexFactory<StringVertex> factory = new ClassBasedVertexFactory<StringVertex>(StringVertex.class);
			
			con = new ConnectivityInspector<StringVertex, StringEdge>(graph);
			
			generator.generateGraph(graph, factory, null);
			
		} while ( !con.isGraphConnected() );
		
		topologyProperties = new TopologyProperties<StringVertex, StringEdge>(topology, graph);
		
		// Assign nodes types
		
		graph.setNodeTypes(new char[]{ 'A', 'B', 'C' });
		
		// Set edge weights
		
		for ( StringEdge edge : graph.edgeSet() ) {
			
			if ( fixedOrUpperBound.equals("fixed") ) {
				
				graph.setEdgeWeight(edge, fixedOrUpperValue);
				
				// Explicit bi-directionality needed for shortest path algorithm
				graph.addEdgeWithWeight(edge.getTarget(), edge.getSource(), fixedOrUpperValue);
				
			} else if (fixedOrUpperBound.equals("random")) {
				
				Double value = Math.random() * fixedOrUpperValue;
				
				graph.setEdgeWeight(edge, value);
				
				graph.addEdgeWithWeight(edge.getTarget(), edge.getSource(), value);
				
			}
			
		}

		Utils.talk("Graph Controller", "Graph generated (" + topology + "). Edges: " + graph.edgeSet().size() + "\n" + graph.edgeSet());
		
	}
	
	/**
	 * @return Get information on the topology generated for the current game
	 */
	public TopologyProperties<StringVertex, StringEdge> getTopologyProperties() {
		
		return topologyProperties;
		
	}

	/*
	 * Private utility methods only available to Main class
	 */

	/**
	 * @param caller
	 */
	public void clearHideLocations(Object caller) {
		
		if (!(caller instanceof Main)) return;
		
		graph.clearHideLocations();
		
	}

	/**
	 * @param caller
	 * @param seeker
	 * @return
	 */
	public ArrayList<StringVertex> latestRoundPaths(Object caller, Seeker seeker) {
		
		if (!(caller instanceof Main)) return null;
		
		return graph.latestRoundPaths(seeker);
		
	}

	/**
	 * @param caller
	 */
	public void notifyEndOfRound(Object caller) {
		
		if (!(caller instanceof Main)) return;
		
		graph.notifyEndOfRound();
		
	}

	/**
	 * @param caller
	 * @param hider
	 * @return
	 */
	public double latestHiderRoundScores(Object caller, Hider hider) {
		
		if (!(caller instanceof Main)) return -1;
		
		return graph.latestHiderRoundScores(hider);
		
	}

	/**
	 * @param caller
	 */
	public void resetGameEnvironment(Object caller) {
		
		if (!(caller instanceof Main)) return;
		
		graph.resetGameEnvironment();
		
	}
	
	/**
	 * @param caller
	 * @return
	 */
	public Set<StringEdge> edgeSet(Object caller) {
		
		if (!(caller instanceof Main)) return null;
		
		return graph.edgeSet();
		
	}
	
	/* Hider only methods */
	
	/**
	 * @param location
	 */
	public void addHideLocation(Object caller, StringVertex location) {
		
		if (!(caller instanceof Hider)) return;
		
		graph.addHideLocation(location);
		
		
	}
	
	/**
	 * Whether a seeker is permitted to know the number of hide locations
	 */
	private static boolean SEEKER_KNOWS_NUMBER_OF_HIDE_LOCATIONS = true;
	
	/**
	 * @return
	 */
	public int numberOfHideLocations(Object caller) {
		
		if (!(caller instanceof Hider) && !SEEKER_KNOWS_NUMBER_OF_HIDE_LOCATIONS) return -1;
		
		return graph.numberOfHideLocations();
		
	}

	/////////////////////////////////////////////
	
	/**
	 * Let players know the edges of a given node
	 * 
	 * @param currentNode
	 * @return
	 */
	public Set<? extends StringEdge> edgesOf(GraphTraverser agent, StringVertex vertex) {
	
		if ( !agent.currentNode().equals(vertex) ) { 
			
			throw new UnsupportedOperationException("At " + agent.currentNode() + " asking for edges of " + vertex);
			 
			/*Set<? extends StringEdge> blankSet = new HashSet<StringEdge>(graph.edgesOf(vertex));
			
			blankSet.clear();
			
			return blankSet; */
			
		} else {
			
			return graph.edgesOf(vertex);
			
		}
		
	}
	
	
	/**
	 * Let players know size of topology
	 * 
	 * @return
	 */
	public ArrayList<StringVertex> vertexSet() {
		
		return new ArrayList<StringVertex>(graph.vertexSet());
		
	}

	/**
	 * Let players register themselves
	 * 
	 * @param graphTraverser
	 */
	public void registerTraversingAgent(GraphTraverser graphTraverser) {
		
		graph.registerTraversingAgent(graphTraverser);
		
	}
	
	/**
	 * Let players deregister themselves
	 * 
	 * @param graphTraverser
	 */
	public void deregisterTraversingAgent(GraphTraverser graphTraverser) {
		
		graph.deregisterTraversingAgent(graphTraverser);
		
	}
	
	/**
	 * Let players request the average performance of all seekers for a given round
	 * 
	 * @param roundsPassed
	 * @return
	 */
	public double averageSeekersRoundPerformance(int round) {
		
		return graph.averageSeekersRoundPerformance(round);
		
	}
	
	/**
	 * Let players request the average performance of all seekers for the LATEST round
	 * 
	 * @param roundsPassed
	 * @return
	 */
	public double averageSeekersPerformance(Metric metric) {
		
		return graph.averageSeekersPerformance(metric);
		
	}
	
	/**
	 * Let players know the weight of edges
	 * 
	 * @param edge
	 * @return
	 */
	public double getEdgeWeight(StringEdge edge) {
		
		return graph.getEdgeWeight(edge);
		
	}

	/**
	 * @param currentNode
	 * @return
	 */
	public boolean isHideLocation(StringVertex currentNode) {
		
		return graph.isHideLocation(currentNode);
		
	}

	/**
	 * @param seeker
	 * @param currentNode
	 * @param nextNode
	 * @return
	 */
	public boolean fromVertexToVertex(GraphTraverser traverser, StringVertex currentNode,
			StringVertex nextNode) {
		
		return graph.fromVertexToVertex(traverser, currentNode, nextNode);
		
	}
	
	/**
	 * Walks a given traverser over a specific path
	 * 
	 * @param traverser
	 * @param sourceVertex
	 * @param targetVertex
	 * @return
	 */
	public void walkPathFromVertexToVertex(GraphTraverser traverser, StringVertex sourceVertex, StringVertex targetVertex) {
		
		List<StringEdge> path = graph.pathFromVertexToVertex(traverser, sourceVertex, targetVertex);
		
		for (StringEdge edge : path) {
			
			Utils.talk(this.toString(), "Walk: " + edge);
			
			graph.fromVertexToVertex(traverser, edge.getSource(), edge.getTarget());
			
		}
		
	}

	/**
	 * @param seeker
	 * @return
	 */
	public ArrayList<StringVertex> latestRoundPaths(GraphTraverser traverser) {
		
		return graph.latestRoundPaths(traverser);
		
	}

	/**
	 * Allow players to know how many edges there are in the graph
	 * @return
	 */
	public int edgeSetSize() {
		
		return graph.edgeSet().size();
		
	}

	/**
	 * @param roundsPassed
	 * @param traverser
	 * @return
	 */
	public double roundCost(int roundsPassed, GraphTraverser traverser) {
		
		return graph.roundCost(roundsPassed, true, traverser);
	
	}

	/**
	 * @param traverser
	 * @param source
	 * @param target
	 * @return
	 */
	public double traverserEdgeCost(GraphTraverser traverser, StringVertex source, StringVertex target) {
		
		return graph.traverserEdgeCost(traverser, source, target);
		
	}

	/**
	 * @param hider
	 * @return
	 */
	public double averageHiderScore(Hider hider) {
		
		return graph.averageHiderScore(hider);
		
	}

	/**
	 * @param vertex
	 * @return
	 */
	public int degreeOf(StringVertex vertex) {

		return graph.degreeOf(vertex);
		
	}

	/**
	 * @param edgeToTarget
	 * @return
	 */
	public char getNodeType(StringVertex vertex) {
		
		return graph.getNodeType(vertex);
		
	}

	/**
	 * @return
	 */
	public int getNumberOfNodeTypes() {
		
		return graph.getNumberOfNodeTypes();
		
	}

	/**
	 * @param currentNode
	 * @param nextNode
	 * @return
	 */
	public StringEdge getEdge(StringVertex currentNode, StringVertex nextNode) {
		
		return graph.getEdge(currentNode, nextNode);
		
	}

	/**
	 * @param currentNode
	 * @param nextNode
	 * @return
	 */
	public boolean containsEdge(StringVertex currentNode, StringVertex nextNode) {
		
		return graph.containsEdge(currentNode, nextNode);
		
	}

	/**
	 * @param seeker
	 * @return
	 */
	public double latestRoundCosts(GraphTraverser traverser, boolean normalised) {
		
		return graph.latestRoundCosts(traverser, normalised);
		
	}

	/**
	 * @param traverser
	 * @return
	 */
	public double averageGameCosts(GraphTraverser traverser) {
		
		return graph.averageGameCosts(traverser);
		
	}

	/**
	 * @param seeker
	 * @return
	 */
	public double averageSeekerScore(Seeker seeker) {
		
		return graph.averageSeekerScore(seeker);
		
	}

	/**
	 * @param traverser
	 * @return
	 */
	public double averagePathLength(GraphTraverser traverser) {
		
		return graph.averagePathLength(traverser);
		
	}

	/**
	 * @return
	 */
	public double getEdgeTraversalDecrement() {
		
		return graph.getEdgeTraversalDecrement();
		
	}

	/**
	 * @param hider
	 * @return
	 */
	public double latestHiderRoundScores(Hider hider) {
		
		return graph.latestHiderRoundScores(hider);
		
	}
	
	/**
	 * @param edge
	 * @param weight
	 */
	public void setEdgeWeight(StringEdge edge, double weight) {
		
		graph.setEdgeWeight(edge, weight);
		
	}
	
	/**
	 * @param traverser
	 * @param metric
	 * @return
	 */
	public double latestTraverserRoundPerformance(GraphTraverser traverser, Metric metric) {
		
		return graph.latestTraverserRoundPerformance(traverser, metric);
		
	}

}
