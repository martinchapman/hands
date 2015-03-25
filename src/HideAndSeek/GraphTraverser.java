package HideAndSeek;

import java.util.HashSet;

import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;

public interface GraphTraverser extends Runnable {

	/**
	 * @return
	 */
	public abstract HashSet<StringVertex> uniquelyVisitedNodes();

	/**
	 * @return
	 */
	public abstract HashSet<StringEdge> uniquelyVisitedEdges();

	/**
	 * @return
	 */
	public abstract boolean getStrategyOverRounds();

	/**
	 * @param strategyOverRounds
	 */
	public abstract void setStrategyOverRounds(boolean strategyOverRounds);

	/**
	 * @return
	 */
	public abstract boolean isPlaying();
	
	/**
	 * 
	 */
	public abstract void startPlaying();

	/**
	 * @param currentNode
	 * @return
	 */
	public abstract StringVertex nextNode(StringVertex currentNode);

	/**
	 * @return
	 */
	public abstract StringVertex startNode();

	/**
	 * 
	 */
	public abstract void endOfRound();

	/**
	 * 
	 */
	public abstract void endOfGame();

	/**
	 * @return
	 */
	public abstract String printGameStats();

	/**
	 * @return
	 */
	public abstract String printRoundStats();

	/**
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * @return
	 */
	public int getRoundsPassed();

}