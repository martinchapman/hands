package HideAndSeek.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.jgrapht.VertexFactory;
import org.jgrapht.alg.ConnectivityInspector;
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
 * information that is released to searchers

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
	 * @param graph
	 */
	public GraphController(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		/**************************
    	 * 
    	 * Set up search graph
    	 * 
    	 * * * * * * * * * * * * */
		
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
    		
    	}
		
		
		// Generate graph
		
		Utils.talk("Graph Controller", "Generating graph.");
		
		VertexFactory<StringVertex> factory = new ClassBasedVertexFactory<StringVertex>(StringVertex.class);
		
		ConnectivityInspector<StringVertex, StringEdge> con = new ConnectivityInspector<StringVertex, StringEdge>(graph);
		
		do {
			
			generator.generateGraph(graph, factory, null);
			
			if (!con.isGraphConnected()) { graph.removeAllEdges(graph.edgeSet()); }
			
		} while ( !con.isGraphConnected() );
		
		Utils.talk("Graph Controller", "Graph generated. \n" + graph.edgeSet());
		
		
		// Assign nodes types
		
		graph.setNodeTypes(new char[]{ 'A', 'B', 'C' });
		
		
		// Set edge weights
		
		for ( StringEdge edge : graph.edgeSet() ) {
			
			if ( fixedOrUpperBound.equals("fixed") ) {
				
				graph.setEdgeWeight(edge, fixedOrUpperValue);
				
			} else if (fixedOrUpperBound.equals("upper")) {
				
				graph.setEdgeWeight(edge, Math.random() * fixedOrUpperValue);
				
			}
			
		}
		
		Utils.talk("Graph Controller", "Graph generated. \n" + graph.edgeSet().size());
		
	}

	/**
	 * Let players know the edges of a given node
	 * 
	 * @param currentNode
	 * @return
	 */
	public Collection<? extends StringEdge> edgesOf(StringVertex currentNode) {
		
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
	 * 
	 * Let players request the average performance of all seekers for a given round
	 * 
	 * @param roundsPassed
	 * @return
	 */
	public double requestAverageSeekerPerformance(int roundsPassed) {
		
		return graph.requestAverageSeekerPerformance(roundsPassed);
		
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
	 * 
	 */
	public void newGame(Object caller) {
		
		if (!(caller instanceof Main)) return;
		
		graph.newGame();
		
	}

	
	
}
