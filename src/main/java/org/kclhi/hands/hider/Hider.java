package org.kclhi.hands.hider;

import java.util.ArrayList;

import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.graph.StringVertex;
import org.kclhi.hands.seeker.Seeker;

public interface Hider extends GraphTraverser {

	/**
	 * @param location
	 */
	public abstract void addHideLocation(StringVertex location);

	/**
	 * @param vertex
	 * @return
	 */
	public abstract boolean hideHere(StringVertex vertex);
	
	/**
	 * @return
	 */
	public int numberOfHideLocations();
	
	/**
	 * @param traverser
	 */
	public void mergeOtherTraverser(Hider traverser);
	

}
