package HideAndSeek.graph;

import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.VertexFactory;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.generate.RingGraphGenerator;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.ClassBasedVertexFactory;

import HideAndSeek.GraphTraverser;
import HideAndSeek.Main;
import HideAndSeek.hider.Hider;
import HideAndSeek.seeker.Seeker;
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
	 * @param graph
	 */
	public GraphController(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		this.fixedOrUpperValue = fixedOrUpperValue;
		
		/**************************
    	 * 
    	 * Set up search graph
    	 * 
    	 * * * * * * * * * * * * */
		
		ConnectivityInspector<StringVertex, StringEdge> con;
		
		do {
			
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
		
		Utils.talk("Graph Controller", "Graph generated. \n" + graph.edgeSet());
		
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
		
		Utils.talk("Graph Controller", "Graph generated. Edges: " + graph.edgeSet().size());
		
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
	public void newGame(Object caller) {
		
		if (!(caller instanceof Main)) return;
		
		graph.newGame();
		
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
	
	/////////////////////////////////////////////
	
	/**
	 * Although an existing diameter value can be obtained from the
	 * FWSP, this is in terms of edge weights. This finds the number
	 * of vertices in the greatest path, as an idea of the number
	 * of hops to achieve the max diameter.
	 * 
	 * Could return a rough estimate, for slightly less clear information.
	 * 
	 * @return
	 */
	public double requestGraphDiameter() {
		
		FloydWarshallShortestPaths<StringVertex, StringEdge> FWSP = new FloydWarshallShortestPaths<StringVertex, StringEdge>(graph);
		
		for (GraphPath<StringVertex, StringEdge> GP : FWSP.getShortestPaths()) {
			
			// Return the length of the path with the greatest weight
			if (GP.getWeight() == FWSP.getDiameter()) return GP.getEdgeList().size();
			
		}
		
		return -1;
		
	}
	/**
	 * Let players know the edges of a given node
	 * 
	 * @param currentNode
	 * @return
	 */
	public Set<? extends StringEdge> edgesOf(StringVertex currentNode) {
	
		return graph.edgesOf(currentNode);
		
	}
	
	
	/**
	 * Let players know size of topology
	 * 
	 * @return
	 */
	public Set<StringVertex> vertexSet() {
		
		return graph.vertexSet();
		
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
	 * Let players request the average performance of all seekers for a given round
	 * 
	 * @param roundsPassed
	 * @return
	 */
	public double requestAverageSeekersRoundPerformance(int round) {
		
		return graph.requestAverageSeekersRoundPerformance(round);
		
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
	 * @return
	 */
	public int numberOfHideLocations() {
		
		return graph.numberOfHideLocations();
		
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
	public double requestRoundCost(int roundsPassed, GraphTraverser traverser) {
		
		return graph.requestRoundCost(roundsPassed, traverser);
	
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
	public double requestAverageHiderScore(Hider hider) {
		
		return graph.requestAverageHiderScore(hider);
		
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
	public double latestRoundCosts(GraphTraverser traverser) {
		
		return graph.latestRoundCosts(traverser);
		
	}

	/**
	 * @param traverser
	 * @return
	 */
	public double requestAverageGameCosts(GraphTraverser traverser) {
		
		return graph.requestAverageGameCosts(traverser);
		
	}

	/**
	 * @param seeker
	 * @return
	 */
	public double requestAverageSeekerScore(Seeker seeker) {
		
		return graph.requestAverageSeekerScore(seeker);
		
	}

	/**
	 * @param traverser
	 * @return
	 */
	public double requestAveragePathLength(GraphTraverser traverser) {
		
		return graph.requestAveragePathLength(traverser);
		
	}

	/**
	 * @return
	 */
	public double getEdgeTraverselDecrement() {
		
		return graph.getEdgeTraversalDecrement();
		
	}

}
