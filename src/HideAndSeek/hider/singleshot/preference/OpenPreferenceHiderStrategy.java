package HideAndSeek.hider.singleshot.preference;

import java.util.LinkedHashSet;

import HideAndSeek.graph.HiddenObjectGraph;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.OpenHiderStrategy;

/**
 * Implement to ensure that a preference strategy exposes
 * its hide mechanism for use by other strategies.
 * 
 * @author Martin
 *
 */
public interface OpenPreferenceHiderStrategy extends OpenHiderStrategy {

	/**
	 * Providing the local graph circumvents the requirement to move the strategy with
	 * the adopting hider.
	 * 
	 * @param localGraph
	 * @return
	 */
	public LinkedHashSet<StringVertex> computeTargetNodes(HiddenObjectGraph<StringVertex, StringEdge> localGraph);
	
}
