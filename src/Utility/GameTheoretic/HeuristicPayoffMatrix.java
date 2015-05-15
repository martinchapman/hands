package Utility.GameTheoretic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import Utility.ComparatorResult;
import Utility.Utils;

/**
 * NB: Relies on the lrs_nash C++ library.
 * 
 * @author Martin
 *
 */
public class HeuristicPayoffMatrix {

	/**
	 * @author Martin
	 *
	 */
	private class StrategyPayoff implements Comparable<StrategyPayoff> {
		
		/**
		 * 
		 */
		private String strategyPlayed;
		
		/**
		 * @return
		 */
		public String getStrategyPlayed() {
			
			return strategyPlayed;
		
		}

		/**
		 * 
		 */
		private TreeMap<String, Double> payoffAgainstOpponent;
		
		/**
		 * 
		 */
		public final TreeMap<String, Double> opponentPayoffs() {
			
			return payoffAgainstOpponent;
			
		}
		
		/**
		 * @param opponent
		 */
		public double getOpponentPayoff(String opponent) {
			
			return payoffAgainstOpponent.get(opponent);
			
		}
		
		public StrategyPayoff(String strategyPlayed) {
			
			this(strategyPlayed, "", 0.0);
			
		}
		
		/**
		 * @param strategyPlayed
		 * @param opponentStrategy
		 * @param payoff
		 */
		public StrategyPayoff(String strategyPlayed, String opponentStrategy, double payoff) {
			
			this.strategyPlayed = strategyPlayed;
			
			payoffAgainstOpponent = new TreeMap<String, Double>();
			
			addPayoff(opponentStrategy, payoff);
			
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime
					* result
					+ ((strategyPlayed == null) ? 0 : strategyPlayed.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StrategyPayoff other = (StrategyPayoff) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (strategyPlayed == null) {
				if (other.strategyPlayed != null)
					return false;
			} else if (!strategyPlayed.equals(other.strategyPlayed))
				return false;
			return true;
		}

		/**
		 * @param opponentStrategy
		 * @param payoff
		 */
		public void addPayoff(String opponentStrategy, double payoff) {
			
			payoffAgainstOpponent.put(opponentStrategy, payoff);
			
		}
		
		public String toString() {
			
			return strategyPlayed;
			
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(StrategyPayoff o) {
			
			if ( getStrategyPlayed().compareTo(o.getStrategyPlayed() ) > ComparatorResult.EQUAL ) {
				
				return ComparatorResult.AFTER;
				
			} else if ( getStrategyPlayed().compareTo(o.getStrategyPlayed() ) < ComparatorResult.EQUAL ) {
				
				return ComparatorResult.BEFORE;
				
			} else {
				
				return ComparatorResult.EQUAL;
				
			}
					
		}

		private HeuristicPayoffMatrix getOuterType() {
			return HeuristicPayoffMatrix.this;
		}
		
	}
	
	/**
	 * 
	 */
	private String title;
	
	/**
	 * 
	 */
	private TreeMap<String, ArrayList<StrategyPayoff>> playerToStrategies;
	
	/**
	 * @param title
	 */
	public HeuristicPayoffMatrix(String title) {
		
		this.title = title;
		
		playerToStrategies = new TreeMap<String, ArrayList<StrategyPayoff>>();
		
	}
	
	/**
	 * @param player
	 * @param strategyPlayed
	 * @param opponentStrategy
	 * @param payoff
	 */
	public void addPayoff(String player, String strategyPlayed, String opponentStrategy, double payoff) {
		
		if ( !player.equals("Hider") && !player.equals("Seeker") ) { 
			
			System.out.println("Only Hider or Seeker entries in payoff matrix.");
			
			return;
			
		}

		if ( playerToStrategies.containsKey(player) ) {
			
			if ( playerToStrategies.get(player).contains(new StrategyPayoff(strategyPlayed)) ) {
				
				playerToStrategies.get(player).get(playerToStrategies.get(player).indexOf(new StrategyPayoff(strategyPlayed))).addPayoff(opponentStrategy, payoff);
			
			} else {
				
				playerToStrategies.get(player).add(new StrategyPayoff(strategyPlayed, opponentStrategy, payoff));
				
			}
		
		} else {
			
			ArrayList<StrategyPayoff> playerPayoffList = new ArrayList<StrategyPayoff>();
			
			playerPayoffList.add(new StrategyPayoff(strategyPlayed, opponentStrategy, payoff));
			
			playerToStrategies.put(player, playerPayoffList);
		
		}
		
	}
	
	public ArrayList<String> GTAnalysis() {
		
		for ( Entry<String, ArrayList<StrategyPayoff>> player : playerToStrategies.entrySet() ) {
			
			if ( player.getValue().size() < 2 ) System.err.println("Each player must have at least two strategies in order to analyse matrix.");
			
			return new ArrayList<String>();
			
		}
		
		formatForLRS();
		
		return getGTData();
		
	}

	/**
	 * 
	 */
	private  void formatForLRS() {
		
		Utils.clearFile(Utils.FILEPREFIX + "GTData");

		FileWriter nashOutputWriter = null;
		
		try {
			nashOutputWriter = new FileWriter(Utils.FILEPREFIX + "/GTData", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ( !playerToStrategies.containsKey("Hider") || !playerToStrategies.containsKey("Seeker") ) return;
		
		ArrayList<StrategyPayoff> hiderStrategies = playerToStrategies.get("Hider");
		
		ArrayList<StrategyPayoff> seekerStrategies = playerToStrategies.get("Seeker");
		
		Collections.sort(hiderStrategies);
		
		Collections.sort(seekerStrategies);
		
		Utils.writeToFile(nashOutputWriter, hiderStrategies.size() + " " + seekerStrategies.size() + "\n");
		
		Utils.writeToFile(nashOutputWriter, "\n");
		
		for ( StrategyPayoff hiderPayoff : hiderStrategies ) {
		
			for ( Entry<String, Double> payoff : hiderPayoff.opponentPayoffs().entrySet() ) {
				
				Utils.writeToFile(nashOutputWriter, payoff.getValue() + " ");
				
			}
			
			Utils.writeToFile(nashOutputWriter, "\n");
			
		}
		
		Utils.writeToFile(nashOutputWriter, "\n");
		
		for ( int i = 0; i < hiderStrategies.size(); i++ ) {
			
			for ( StrategyPayoff seekerPayoff : seekerStrategies ) {
				
				Utils.writeToFile(nashOutputWriter, new ArrayList<Entry<String, Double>>(seekerPayoff.opponentPayoffs().entrySet()).get(i).getValue() + " ");
				
			}
			
			Utils.writeToFile(nashOutputWriter, "\n");
			
		}
		
		try {
			nashOutputWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private ArrayList<String> getGTData() {
		
		Utils.runCommand("/usr/local/bin/setnash " + Utils.FILEPREFIX + "GTData" + " " + Utils.FILEPREFIX + "Player1 " + Utils.FILEPREFIX + "Player2");
		
		ArrayList<String> GTData = Utils.runCommandWithReturn("/usr/local/bin/nash " + Utils.FILEPREFIX + "Player1 " + Utils.FILEPREFIX + "Player2 | tee");
	
		ArrayList<String> formattedData = new ArrayList<String>();
		
		String hider = "";
		
		double seekerUtility = 0;
		
		String seeker = "";
		
		double hiderUtility = 0;
		
		for ( String line : GTData ) {
			
			if ( line.contains("*") || line.length() == 0 ) continue;
			
			//Utils.talk(toString(), "Data: " + line);
			System.out.println("Data: " + line);
			
			String[] lineSplitArray = line.split(" ");
		
			ArrayList<String> lineSplit = new ArrayList<String>();
			
			for ( String split : lineSplitArray ) {
				
				if ( split.length() == 0  ) continue;
					
				lineSplit.add(split);
				
			}
			
			if ( lineSplit.get(0).equals("1") ) {
				
				for ( int i = 1; i < lineSplit.size() - 1; i++ ) {
					
					String pureStrategy = playerToStrategies.get("Hider").get( i - 1 ).toString();
					
					if ( lineSplit.get(i).equals("1") ) {
						
						hider = pureStrategy;
						
						seekerUtility = Double.parseDouble(lineSplit.get(lineSplit.size() - 1));
						
						break;
						
					} else if ( lineSplit.get(i).contains("/") ) {
						
						String[] fractionSplit = lineSplit.get(i).split("/");
						
						double percentage = ( (double)(Integer.parseInt(fractionSplit[0].trim())) / (double)(Integer.parseInt(fractionSplit[1].trim())) ) * 100;
						
						hider += pureStrategy + " (" + percentage + ") ";
						
						if (!lineSplit.get(lineSplit.size() - 1).contains("/")) {
							
							seekerUtility = Double.parseDouble(lineSplit.get(lineSplit.size() - 1));
							
						} else {
							
							String[] seekerUtlitySplit = lineSplit.get(lineSplit.size() - 1).split("/");
							
							seekerUtility = ( (double)(Integer.parseInt(seekerUtlitySplit[0].trim())) / (double)(Integer.parseInt(seekerUtlitySplit[1].trim())) );
						
						}
						
					}
					
				}
				
			} else if ( lineSplit.get(0).equals("2") ) {
				
				for ( int i = 1; i < lineSplit.size() - 1; i++ ) {
					
					String pureStrategy = playerToStrategies.get("Seeker").get( i - 1 ).toString();
					
					if ( lineSplit.get(i).equals("1") ) {
						
						seeker = pureStrategy;
							
						hiderUtility = Double.parseDouble(lineSplit.get(lineSplit.size() - 1));
						
						break;
						
					} else if ( lineSplit.get(i).contains("/") ) {
						
						String[] fractionSplit = lineSplit.get(i).split("/");
						
						double percentage = ( (double)Integer.parseInt(fractionSplit[0].trim()) / (double)Integer.parseInt(fractionSplit[1].trim()) ) * 100;
						
						seeker += pureStrategy + " (" + percentage + ") ";
						
						if (!lineSplit.get(lineSplit.size() - 1).contains("/")) {
							
							hiderUtility = Double.parseDouble(lineSplit.get(lineSplit.size() - 1));
							
						} else {
						
							String[] hiderUtilitySplit = lineSplit.get(lineSplit.size() - 1).split("/");
						
							hiderUtility = ( (double)(Integer.parseInt(hiderUtilitySplit[0].trim())) / (double)(Integer.parseInt(hiderUtilitySplit[1].trim())) );
						
						}
						
					}
					
				}
				
			}
			
			if (!hider.equals("") && !seeker.equals("") ) { 
				
				formattedData.add("Hider: " + hider + " (Payoff: " + hiderUtility + ") | Seeker: " + seeker + " (Payoff: " + seekerUtility + ")");
				
				hider = "";
				
				seeker = "";
				
			}
			
		}
		
		return formattedData;
		
	}
	
	public String toString() {
		
		ArrayList<StrategyPayoff> hiderStrategies = playerToStrategies.get("Hider");
		
		ArrayList<StrategyPayoff> seekerStrategies = playerToStrategies.get("Seeker");
		
		Collections.sort(hiderStrategies);
		
		Collections.sort(seekerStrategies);
		
		String topRow = "	 ";
		
		boolean emptyTop = true;
		
		ArrayList<String> rows = new ArrayList<String>();
		
		for ( StrategyPayoff strategy : hiderStrategies ) {
			
			String row = strategy.toString();
			
			for ( Entry<String, Double> payoff : strategy.opponentPayoffs().entrySet() ) {
				
				if ( emptyTop ) topRow += payoff.getKey() + "   ";
				
				row += "   " + payoff.getValue() + "  " + ( seekerStrategies.get(seekerStrategies.indexOf(new StrategyPayoff(payoff.getKey()))).opponentPayoffs().get(strategy.toString()) );
				
			}
			
			rows.add(row);
			
			emptyTop = false;
			
		}
		
		String returner = topRow;
		
		for ( String row : rows ) returner += "\n" + row;
		
		return returner;
		
	}
	
	public static void main(String[] args) {
		
		HeuristicPayoffMatrix HPM = new HeuristicPayoffMatrix("");
		
		HPM.addPayoff("Hider", "Up", "Left", 2);
		HPM.addPayoff("Hider", "Up", "Right", 1);
		HPM.addPayoff("Hider", "Down", "Left", 3);
		HPM.addPayoff("Hider", "Down", "Right", 1);
		HPM.addPayoff("Hider", "Bob", "Left", 1);
		HPM.addPayoff("Hider", "Bob", "Right", 1);
		
		HPM.addPayoff("Seeker", "Left", "Up", 5);
		HPM.addPayoff("Seeker", "Right", "Up", 4);
		HPM.addPayoff("Seeker", "Left", "Down", 0);
		HPM.addPayoff("Seeker", "Right", "Down", 5);
		HPM.addPayoff("Seeker", "Left", "Bob", 0);
		HPM.addPayoff("Seeker", "Right", "Bob", 5);
		
		System.out.println(HPM);
		
		for ( String line : HPM.GTAnalysis() ) System.out.println(line);
	
	}
	
}
