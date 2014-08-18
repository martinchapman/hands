package Utility.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * @author Martin
 *
 */
public class TraverserRecord {
	
	/**
	 * 
	 */
	protected String traverser;
	
	/**
	 * @param traverser
	 */
	public void setTraverser(String traverser) {
		
		this.traverser = traverser;
	
	}

	/**
	 * 
	 */
	private String parameters;
	
	/**
	 * @return
	 */
	public String getParameters() {
		
		return parameters;
	
	}

	/**
	 * @param parameters
	 */
	public void setParameters(String parameters) {
	
		this.parameters = parameters;
	
	}
	
	/**
	 * 
	 */
	private String category;
	
	/**
	 * @return
	 */
	public String getCategory() {
		
		return category;
		
	}
	
	/**
	 * @param category
	 */
	public void setCategory(String category) {
		
		this.category = category;
		
	}

	/**
	 * 
	 */
	private String topology;
	
	/**
	 * @param parameters
	 */
	public void setTopology(String topology) {
		
		this.topology = topology;
		
	}
	
	/**
	 * @return
	 */
	public String getTopology() {
		
		return topology;
		
	}
	
	/**
	 * @param traverser
	 */
	public TraverserRecord(String traverser) {
	
		this.traverser = traverser;
		
		attributeToValue = new Hashtable<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		//gameNumber = 1;
		
		//attributeToValue.put(gameNumber, new Hashtable<String, Double>());
		
		attributes = new HashSet<String>();
		
		category = "";
		
	}
	
	/**
	 * @param traverser
	 * @param attributeToValue
	 * @param attributes
	 */
	public TraverserRecord(String traverser, Hashtable<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue, HashSet<String> attributes) {
		
		this.traverser = traverser;
		
		this.attributeToValue = attributeToValue;
		
		this.attributes = attributes;
		
	}
	
	/**
	 * @param record
	 */
	public void integrateRecord(TraverserRecord record) {
		
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> gameToAttributes : record.getAttributeToValue().entrySet() ) {
			
			for ( Entry<String,Double> attributeToValue : gameToAttributes.getValue().entrySet() ) {
				
				this.attributeToValue.get(gameToAttributes.getKey()).put(attributeToValue.getKey(), this.attributeToValue.get(gameToAttributes.getKey()).get(attributeToValue.getKey()) + attributeToValue.getValue());
				
			}
			
		}
		
	}
		
	/**
	 * @return
	 */
	public String getTraverser() {
		
		return traverser;
	
	}
		
	/**
	 * Game Number to a list of the attributes and their values,
	 * for this traverser, in that game.
	 */
	protected Hashtable<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue;
	
	/**
	 * @return
	 */
	public Hashtable<AttributeSetIdentifier, Hashtable<String, Double>> getAttributeToValue() {
	
		return attributeToValue;
	
	}

	/**
	 * 
	 */
	protected HashSet<String> attributes;
	
	/**
	 * @return
	 */
	public HashSet<String> getAttributes() {
		
		return attributes;
	
	}

	/**
	 * @param attribute
	 * @param value
	 */
	public void attribute(String gameOrRound, int number, String attribute, double value) {
	    
		// To maintain list of individual attributes
		attributes.add(attribute);
		
		//value = Math.log(value)/Math.log(2);
		
		// If we already have an entry for this game / round, and no entry for the given attribute, add it to this entry. 
		if (attributeToValue.containsKey(new AttributeSetIdentifier(gameOrRound, number)) && 
			!attributeToValue.get(new AttributeSetIdentifier(gameOrRound, number)).containsKey(attribute) ) {
			
			attributeToValue.get(new AttributeSetIdentifier(gameOrRound, number)).put(attribute, value);
		
		// If there is no entry for this game / round then add it.
		} else {
			
			Hashtable<String, Double> newGameTable = new Hashtable<String, Double>();
			
			newGameTable.put(attribute, value);
			
			attributeToValue.put(new AttributeSetIdentifier(gameOrRound, number), newGameTable);
			
		}
		
	}
	
	/**
	 * @return
	 */
	protected Hashtable<String, Double> calculateGameAverage() {
		
		Hashtable<AttributeSetIdentifier, Hashtable<String,Double>> gameEntries = new Hashtable<AttributeSetIdentifier, Hashtable<String,Double>>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
			
			if ( attributeEntry.getKey().getGameOrRound().equals("Game") ) {
				
				gameEntries.put(attributeEntry.getKey(), attributeEntry.getValue());
				
			}
			
		}
		
		return calculateAverage(gameEntries);
		
	}
	
	/**
	 * 
	 * Match each measure to an average value over all games
	 * 
	 * @param attributeToValue - a subset of interaction records that we want to take an average of
	 * @return
	 */
	protected Hashtable<String, Double> calculateAverage(Hashtable<AttributeSetIdentifier, Hashtable<String,Double>> attributeToValue) {
		
		Hashtable<String, Double> cumulativeAttributeToValue = new Hashtable<String, Double>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
		
			for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
				
				if (cumulativeAttributeToValue.containsKey(attributeToValueEntry.getKey())) {
					
					cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), cumulativeAttributeToValue.get(attributeToValueEntry.getKey()) + attributeToValueEntry.getValue());
					
				} else {
					
					cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
					
				}
				
			}
		
		}
		
		Hashtable<String, Double> averageAttributeToValue = new Hashtable<String, Double>();
		
		for (Entry<String, Double> attribute : cumulativeAttributeToValue.entrySet()) {
			
			averageAttributeToValue.put(attribute.getKey(), attribute.getValue() / attributeToValue.size());
			
		}
		
		return averageAttributeToValue;
		
	}
	
	/**
	 * Using calculateAverage(), select a single passed measure
	 * 
	 * @param attribute
	 * @return
	 */
	public double getAverageGameAttributeValue(String attribute) {
		
		return calculateGameAverage().get(attribute);
		
	}
	
	/**
	 * @return
	 */
	protected String showGameSeries() {
		
		String returner = "";
		
		ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>>(getGameSeries());
		
		Collections.reverse(series);
		
		for ( Entry<AttributeSetIdentifier, Hashtable<String,Double>> entry : series ) {
			
			returner += "\n" + entry; 
			
		}
		
		return returner;
		
	}
	
	/**
	 * 
	 * Allow for multiple games with multiple rounds to be run, and the average of all rounds
	 * over those games to be viewed.
	 * 
	 * For example, I run 100 games and want to plot the average from round 1, 2, 3 etc. on a graph,
	 * with the number of points still being equal to the number of games.
	 * 
	 * ~MDC 31/7/14 This could be much neater.
	 * 
	 * @return
	 * 
	 * 
	 */
	public ArrayList<Entry<Integer, Hashtable<String,Double>>> getRoundSeries() {
	
		// Create map to hold round numbers to average values for all attributes within that round
		Hashtable<Integer, Hashtable<String, Double>> cumulativeRoundsAttributeToValue = new Hashtable<Integer, Hashtable<String, Double>>();
		
		int attributeAdditions = 0; 
		
		// For each record 
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet() ) {
			
			// If this is a round entry
			if ( attributeEntry.getKey().getGameOrRound().equals("Round") ) {
			
				// For each attribute entry within that round
				for ( Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet() ) {
					
					// If an entry for the round doesn't exist within our cumulative map...
					if ( !cumulativeRoundsAttributeToValue.containsKey( attributeEntry.getKey().getNumber() ) ) {
					
						// Create it
						cumulativeRoundsAttributeToValue.put(attributeEntry.getKey().getNumber(), new Hashtable<String, Double>());
					
					}
						
					// See if an entry for this attribute already exists at this round number, if it does, add it on
					if ( cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getNumber()).containsKey(attributeToValueEntry.getKey()) ) {
						
						cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getNumber()).put(attributeToValueEntry.getKey(), cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getNumber()).get(attributeToValueEntry.getKey()) + attributeToValueEntry.getValue() );
					
						attributeAdditions++;
						
					// Otherwise create this entry, at that round number
					} else {
						
						cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getNumber()).put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
						attributeAdditions++;
						
					}
					
				}
			
			}
		
		}
		
		/* We now have a map from each round number to a map from each attribute to their cumulative values.
		Complete by creating a new map to the average value: */
		
		Hashtable<Integer, Hashtable<String, Double>> averageRoundAttributeToValue = new Hashtable<Integer, Hashtable<String, Double>>();
		
		for (Entry<Integer, Hashtable<String, Double>> attribute : cumulativeRoundsAttributeToValue.entrySet()) {
			
			for ( Entry<String, Double> attributeCumulative: attribute.getValue().entrySet()) {
				
				// No entry in the average mapping from round to attribute:value exists
				if ( !averageRoundAttributeToValue.containsKey( attribute.getKey() ) ) {
				
					// Create it
					averageRoundAttributeToValue.put(attribute.getKey(), new Hashtable<String, Double>());
				
				}
				
				/* Get the existing key, be it previously created, and put the key of the current attribute, plus the value
				   of the current attribute divided by the number of rounds, as shown in the cumulative mapping. 
				   
				   To work out `how many round 1s' etc (how many games), we priorly calculate the number of round attribute
				   additions for this traverser. Taking this number, divided by the number of attributes, we work out the number
				   of attributes within one column. Dividing this number by the number of rounds in a game gives the number of games.
				   */
				averageRoundAttributeToValue.get(attribute.getKey()).put(attributeCumulative.getKey(), cumulativeRoundsAttributeToValue.get(attribute.getKey()).get(attributeCumulative.getKey()) / ( ( attributeAdditions / attribute.getValue().entrySet().size() ) / cumulativeRoundsAttributeToValue.entrySet().size()) ); 
			
			}
			
		}
		
		ArrayList<Entry<Integer, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<Integer, Hashtable<String,Double>>>(averageRoundAttributeToValue.entrySet());
		
		Collections.reverse(series);
		
		return series;
		
	}
	
	/**
	 * 
	 * Return the series for each attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> getGameSeries() {

		ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>>(attributeToValue.entrySet());
		
		// For each record 
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet() ) {
			
			// If this is a game entry
			if ( attributeEntry.getKey().getGameOrRound().equals("Game") ) {
			
				// Add it to the series
				series.add(attributeEntry);
			
			}
		
		}
		
		Collections.reverse(series);
		
		return series;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String printGameAverage() {
	
		return traverser + " " + calculateGameAverage();
		
	}
	
	/**
	 * @return
	 */
	public String printSeries() {
		
		return traverser + " " + showGameSeries();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		return getTraverser().equals(((TraverserRecord) obj).getTraverser());
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return traverser;
		
	}
		
}

