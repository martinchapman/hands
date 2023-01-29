package org.kclhi.hands.utility.output;

/**
 * @author Martin
 *
 */
public class AttributeSetIdentifier extends Number {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return
	 */
	public String getGameOrRound() {
	
		if ( roundNumber == -1 ) return "Game";
		return "Round";
	
	}

	/**
	 * 
	 */
	private int gameNumber;
	
	/**
	 * @return
	 */
	public int getGameNumber() {
		
		return gameNumber;
		
	}
	
	/**
	 * 
	 */
	private int roundNumber;
	
	/**
	 * @return
	 */
	public int getRoundNumber() {
		
		return roundNumber;
		
	}

	/**
	 * @param gameOrRound
	 * @param roundNumber
	 */
	public AttributeSetIdentifier(int gameNumber, int roundNumber) {
		
		this.gameNumber = gameNumber;
		
		this.roundNumber = roundNumber;
		
	}
	
	/**
	 * 
	 * @param roundNumber
	 */
	public AttributeSetIdentifier(int roundNumber) {
		
		this.gameNumber = -1;
		
		this.roundNumber = roundNumber;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof AttributeSetIdentifier)) return false;
		
		return ((AttributeSetIdentifier) obj).getGameNumber() == gameNumber && ((AttributeSetIdentifier) obj).getRoundNumber() == roundNumber;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		
		return gameNumber * roundNumber;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return "Game: " + gameNumber + " Round: " + roundNumber; 
		
	}

	@Override
	public int intValue() {
		return roundNumber;
	}

	@Override
	public long longValue() {
		return (long)roundNumber;
	}

	@Override
	public float floatValue() {
		return (float)roundNumber;
	}

	@Override
	public double doubleValue() {
		return (double)roundNumber;
	}
	
}
