package Utility.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
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
		
		attributeToValue = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>>();
		
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
	public TraverserRecord(String traverser, LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue, HashSet<String> attributes) {
		
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
	protected LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue;
	
	/**
	 * @return
	 */
	public LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> getAttributeToValue() {
	
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

	private int currentGameNumber = -1;
	
	private int currentRoundNumber = -1;
	
	/**
	 * @param attribute
	 * @param value
	 */
	public void attribute(String gameOrRound, String attribute, double value) {
	   
		// To maintain list of individual attributes
		attributes.add(attribute);
		
		//value = Math.log(value)/Math.log(2);
		
		// If we already have an entry for this game / round, and no entry for the given attribute, add it to this entry. 
		if (attributeToValue.containsKey(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)) && 
			!attributeToValue.get(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)).containsKey(attribute) ) {
			
			attributeToValue.get(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)).put(attribute, value);
		
		// If there is no entry for this game / round then add it.
		} else {
			
			if ( gameOrRound.equals("Game") ) {
				
				currentGameNumber++;
				
				currentRoundNumber = -1;
				
			} else if ( gameOrRound.equals("Round") ) {
				
				currentRoundNumber++;
				
			}
			
			Hashtable<String, Double> newGameRoundTable = new Hashtable<String, Double>();
			
			newGameRoundTable.put(attribute, value);
			
			attributeToValue.put(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber), newGameRoundTable);
			
		}
		
	}
	
	/**
	 * @return
	 */
	protected Hashtable<String, Double> calculateGameAverage(Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> gameEntries = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
			
			if ( attributeEntry.getKey().getGameOrRound().equals("Game") ) {
				
				gameEntries.put(attributeEntry.getKey(), attributeEntry.getValue());
				
			}
			
		}
		
		return calculateAverage(gameEntries, minAttributeToValueAllSeries, maxAttributeToValueAllSeries);
		
	}
	
	/**
	 * @return
	 */
	protected Hashtable<String, Double> calculateGameAverage() {
		
		return calculateGameAverage(null, null);
		
	}
	
	/**
	 * 
	 * Match each measure to an average value over all games
	 * 
	 * @param attributeToValue - a subset of interaction records that we want to take an average of
	 * @return
	 */
	protected Hashtable<String, Double> calculateAverage(LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> attributeToValue, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		Hashtable<String, Double> cumulativeAttributeToValue = new Hashtable<String, Double>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
		
			for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
				
				if (cumulativeAttributeToValue.containsKey(attributeToValueEntry.getKey())) {
					  
					if ( minAttributeToValueAllSeries != null && maxAttributeToValueAllSeries != null ) {
						
						cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), cumulativeAttributeToValue.get(attributeToValueEntry.getKey()) + ( (attributeToValueEntry.getValue() - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey())) / (maxAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey())) ));
						
					} else {
						
						cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), cumulativeAttributeToValue.get(attributeToValueEntry.getKey()) + attributeToValueEntry.getValue());
					
					}
					
				} else {
					
					if ( minAttributeToValueAllSeries != null && maxAttributeToValueAllSeries != null ) {
						
						cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), ( attributeToValueEntry.getValue() - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) ) / ( maxAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) ) );
						
					} else {
						
						cumulativeAttributeToValue.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
					}
					
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
	 * @param maxAttributeToValueAllSeries
	 * @param minAttributeToValueAllSeries
	 * @param attribute
	 * @return
	 */
	public double getAverageGameAttributeValue(String attribute, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		return calculateGameAverage(minAttributeToValueAllSeries, maxAttributeToValueAllSeries).get(attribute);
		
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
	 * Allow for multiple games with multiple rounds to be run, and the average of all rounds
	 * over those games to be viewed.
	 * 
	 * For example, I run 100 games and want to plot the average from round 1, 2, 3 etc. on a graph,
	 * with the number of points still being equal to the number of games.
	 * 
	 * ~MDC 31/7/14 This could be much neater.
	 * 
	 * @return
	 */
	public ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> getRoundSeries() {
	
		// Create map to hold round numbers to average values for all attributes within that round
		Hashtable<AttributeSetIdentifier, Hashtable<String, Double>> cumulativeRoundsAttributeToValue = new Hashtable<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		int attributeAdditions = 0; 
		
		// For each record 
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet() ) {
			
			// If this is a round entry
			if ( attributeEntry.getKey().getGameOrRound().equals("Round") ) {
			
				// For each attribute entry within that round
				for ( Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet() ) {
					
					// If an entry for the round doesn't exist within our cumulative map...
					if ( !cumulativeRoundsAttributeToValue.containsKey( attributeEntry.getKey().getRoundNumber() ) ) {
					
						// Create it
						cumulativeRoundsAttributeToValue.put(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber()), new Hashtable<String, Double>());
					
					}
						
					// See if an entry for this attribute already exists at this round number, if it does, add it on 
					if ( cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).containsKey(attributeToValueEntry.getKey()) ) {
						
						cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getRoundNumber()).put(attributeToValueEntry.getKey(), cumulativeRoundsAttributeToValue.get(attributeEntry.getKey().getRoundNumber()).get(attributeToValueEntry.getKey()) + attributeToValueEntry.getValue() );
					
						attributeAdditions++;
						
					// Otherwise create this entry, at that round number
					} else {
						
						cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
						attributeAdditions++;
						
					}
					
				}
			
			}
		
		}
		
		/* We now have a map from each round number to a map from each attribute to their cumulative values.
		Complete by creating a new map to the average value: */
		
		Hashtable<AttributeSetIdentifier, Hashtable<String, Double>> averageRoundAttributeToValue = new Hashtable<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attribute : cumulativeRoundsAttributeToValue.entrySet()) {
			
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
		
		ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>>(averageRoundAttributeToValue.entrySet());
		
		Collections.reverse(series);
		
		return series;
		
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> getNormalisedGameSeries() {
		
		return gameSeries(true);
		
	}
	
	/**
	 * @return
	 */
	public ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> getGameSeries() {
		
		return gameSeries(false);
		
	}
	
	/**
	 * Return the series for each attribute
	 * 
	 * @param attribute
	 * @return
	 */
	private ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> gameSeries(boolean normalise) {

		ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>>();
		
		// For each record 
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet() ) {
			
			// If this is a game entry
			if ( attributeEntry.getKey().getGameOrRound().equals("Game") ) {
			
				// Add it to the series
				series.add(attributeEntry);
				
			}
		
		}
		
		if (normalise) {
			
			Hashtable<String, Double> minAttributeInSeries = new Hashtable<String, Double>();
			
			Hashtable<String, Double> maxAttributeInSeries = new Hashtable<String, Double>();
		
			for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : series ) {
				
				for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
					
					if ( !minAttributeInSeries.containsKey(attributeToValueEntry.getKey()) && !maxAttributeInSeries.containsKey(attributeToValueEntry.getKey()) ) {
						
						minAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
						maxAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
					} else {
						
						if ( attributeToValueEntry.getValue() < minAttributeInSeries.get(attributeToValueEntry.getKey())) {
							
							minAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
							
						}
						
						if ( attributeToValueEntry.getValue() > maxAttributeInSeries.get(attributeToValueEntry.getKey())) {
							
							maxAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
							
						}
						
					}
					
				}
			
			}
			
			ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>> newSeries = 
					new ArrayList<Entry<AttributeSetIdentifier, Hashtable<String,Double>>>();
			
			for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : series ) {
				
				Hashtable<String, Double> normalisedAttributeEntry = new Hashtable<String, Double>();
				
				for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
					
					normalisedAttributeEntry.put(attributeToValueEntry.getKey(), ( (attributeToValueEntry.getValue() - minAttributeInSeries.get(attributeToValueEntry.getKey())) / (maxAttributeInSeries.get(attributeToValueEntry.getKey()) - minAttributeInSeries.get(attributeToValueEntry.getKey())) ));
					
				}
				
				attributeEntry.setValue(normalisedAttributeEntry);
				
				newSeries.add(attributeEntry);
				
			}
			
			series = newSeries;
		
		}
		
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

