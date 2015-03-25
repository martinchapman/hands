package HideAndSeek;

public interface AdaptiveGraphTraverserStrategy {
	
	/**
	 * Adaptive strategies must provide 
	 * a mechanism that reports on their ability to 
	 * operate effectively under their given strategy,
	 * based upon game conditions, and the performance/attributes
	 * of their opponent.
	 * 
	 * This information guides the decision as to whether adaptation
	 * should take place. 
	 * 
	 * @return
	 */
	
	/**
	 * The relevance of a strategy to the environment or to the
	 * behaviour of the opponent.
	 * 
	 * A lower value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public double relevanceOfStrategy();
	
	/**
	 * The performance of an opponent.
	 * 
	 * A higher value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public double performanceOfOpponent();
	
	/**
	 * The performance of a strategy itself.
	 * 
	 * A lower value here signals that adaptation should occur. 
	 * 
	 * @return
	 */
	public double performanceOfSelf();

}
