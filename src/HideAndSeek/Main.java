package HideAndSeek;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.AdaptiveHiderStrategy;
import HideAndSeek.hider.Hider;
import HideAndSeek.hider.repeatgame.bias.FixedStartVariableBias;
import HideAndSeek.hider.repeatgame.bias.VariableBias;
import HideAndSeek.hider.repeatgame.bias.VariableBiasLocations;
import HideAndSeek.hider.repeatgame.bias.VariableBiasStaticBetween;
import HideAndSeek.hider.repeatgame.deceptive.Deceptive;
import HideAndSeek.hider.repeatgame.deceptive.EpsilonDeceptive;
import HideAndSeek.hider.repeatgame.deceptive.GroupedDeceptive;
import HideAndSeek.hider.repeatgame.deceptive.LeastConnectedDeceptive;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSet;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat;
import HideAndSeek.hider.repeatgame.random.adaptable.RandomSetAdaptable;
import HideAndSeek.hider.repeatgame.random.adaptable.UniqueRandomSetRepeatAdaptable;
import HideAndSeek.hider.singleshot.cost.FixedStartVariableLowEdgeCost;
import HideAndSeek.hider.singleshot.cost.VariableLowEdgeCost;
import HideAndSeek.hider.singleshot.cost.VariableLowEdgeCostStaticBetween;
import HideAndSeek.hider.singleshot.distance.LowEdgeCostRandomFixedDistance;
import HideAndSeek.hider.singleshot.distance.LowEdgeCostRandomFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.distance.LowEdgeCostVariableFixedDistance;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistance;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistance;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.preference.LeastConnected;
import HideAndSeek.hider.singleshot.preference.LeastConnectedStaticBetween;
import HideAndSeek.hider.singleshot.preference.MaxDistance;
import HideAndSeek.hider.singleshot.random.LowEdgeCostRandomSet;
import HideAndSeek.hider.singleshot.random.LowEdgeCostRandomSetStaticBetween;
import HideAndSeek.hider.singleshot.random.Random;
import HideAndSeek.hider.singleshot.random.RandomFixedStart;
import HideAndSeek.hider.singleshot.random.RandomSet;
import HideAndSeek.hider.singleshot.random.RandomSetStaticBetween;
import HideAndSeek.hider.singleshot.random.RandomStaticBetween;
import HideAndSeek.hider.singleshot.random.RandomVariableHidePotential;
import HideAndSeek.hider.singleshot.staticlocations.StaticLocations;
import HideAndSeek.seeker.AdaptiveSeeker;
import HideAndSeek.seeker.AdaptiveSeekerStrategy;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.probability.HighProbability;
import HideAndSeek.seeker.repeatgame.probability.HighProbabilityRepetitionCheck;
import HideAndSeek.seeker.repeatgame.probability.InverseHighProbability;
import HideAndSeek.seeker.repeatgame.probability.VariableHistoryHighProbability;
import HideAndSeek.seeker.repeatgame.probability.adaptable.HighProbabilityAdaptable;
import HideAndSeek.seeker.repeatgame.probability.adaptable.InverseHighProbabilityAdaptable;
import HideAndSeek.seeker.singleshot.cost.LowEdgeCost;
import HideAndSeek.seeker.singleshot.coverage.BacktrackPath;
import HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearch;
import HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.coverage.DepthFirstSearch;
import HideAndSeek.seeker.singleshot.coverage.DepthFirstSearchLowCost;
import HideAndSeek.seeker.singleshot.coverage.VariableBacktrackPath;
import HideAndSeek.seeker.singleshot.preference.LeastConnectedFirst;
import HideAndSeek.seeker.singleshot.preference.MostConnectedFirst;
import HideAndSeek.seeker.singleshot.random.ConstrainedRandomWalk;
import HideAndSeek.seeker.singleshot.random.FixedStartRandomWalk;
import HideAndSeek.seeker.singleshot.random.RandomWalk;
import Utility.Pair;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class Main {
	
	/**
	 * 
	 */
	private int gameNumber;
	
	/**
	 * 
	 */
	private int totalGames;
	
	/**
	 * 
	 */
	private int totalRounds;
	
	/**
	 * 
	 */
	private String currentSimulationIdentifier = "";
	
	/**
	 * Graph 
	 */
	private GraphController<StringVertex, StringEdge> graphController;
	
	/**
	 * Whether to output a corresponding animation of the search process
	 */
	private final boolean OUTPUTJS = false;
	
	/**
	 * @param args
	 */
	public Main(String[] args) {
		
		currentSimulationIdentifier = Utils.readFromFile(Utils.FILEPREFIX + "simRecordID.txt").get(0);
		
		System.out.println(currentSimulationIdentifier);
		
		Utils.talk("Main", "Simulation parameters " + Arrays.toString(args));
		
		gameNumber = Integer.parseInt(args[0]);
		
		totalGames = Integer.parseInt(args[1]);
		
		//
		
		String topology = args[4];
		
		int numberOfVertices = Integer.parseInt(args[5]);
		
		String fixedOrUpperBound = args[9];
		
		double fixedOrUpperValue = Double.parseDouble(args[8]);
		
		int edgeTraversalDecrement = Integer.parseInt(args[10]);
		
		initGraph(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
		boolean mixHiders = Boolean.parseBoolean(args[11]);
		
		boolean mixSeekers = Boolean.parseBoolean(args[12]);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[7]);
		
		this.totalRounds = rounds;
		
		String hiderList = args[2];
		
		String seekerList = args[3];
		
		int numberOfHideLocations = Integer.parseInt(args[6]);
		
		
		startRounds(initHiders(hiderList, numberOfHideLocations, mixHiders), initSeekers(seekerList, numberOfHideLocations, mixSeekers), rounds, true);
		
	}
	
	/**
	 * @param args
	 */
	private void initGraph(String topology, int numberOfVertices, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		graphController = new GraphController<StringVertex, StringEdge>(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
	}
	
	/**
	 * @param agentList
	 * @param numberOfHideLocations
	 * @return
	 */
	private List<Hider> initHiders(String agentList, int numberOfHideLocations, boolean mixHiders) {
		
		/**************************
    	 * 
    	 * Set up hiding agents
    	 * 
    	 * * * * * * * * * * * * */
		 
		List<Hider> allHidingAgents = new ArrayList<Hider>();
		
		for( Pair<String, String> hiderType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			// Single-shot:
			
			if (hiderType.getElement0().equals("StaticLocations")) {
				
				allHidingAgents.add(new StaticLocations(graphController, numberOfHideLocations));
			
			} 

			if (hiderType.getElement0().equals("Random")) {
				
				allHidingAgents.add(new Random(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedStart")) {
				
				allHidingAgents.add(new RandomFixedStart(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomStaticBetween")) {
				
				allHidingAgents.add(new RandomStaticBetween(graphController, numberOfHideLocations));
			
			}
			
			if (hiderType.getElement0().equals("RandomVariableHidePotential")) {
				
				allHidingAgents.add(new RandomVariableHidePotential(graphController, numberOfHideLocations, gameNumber / ((float)totalGames)));
			
			} 
			
			//
			
			if (hiderType.getElement0().equals("FirstN")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, numberOfHideLocations, 0));
				
				// Have to set ID manually as identifier and class used are different
				// allHidingAgents.get(allHidingAgents.size() - 1).setName("RandomDirection");
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FirstN");
			
			}
			
			if (hiderType.getElement0().equals("FirstNStaticBetween")) {
				
				allHidingAgents.add(new VariableFixedDistanceStaticBetween(graphController, numberOfHideLocations, 0));
				
				// Have to set ID manually as identifier and class used are different
				// allHidingAgents.get(allHidingAgents.size() - 1).setName("RandomDirection");
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FirstNStaticBetween");
			
			}
			
			//
			
			if (hiderType.getElement0().equals("RandomSet")) {
				
				allHidingAgents.add(new RandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomSetStaticBetween")) {
				
				allHidingAgents.add(new RandomSetStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomSet")) {
				
				allHidingAgents.add(new LowEdgeCostRandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomSetStaticBetween")) {
				
				allHidingAgents.add(new LowEdgeCostRandomSetStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("UniqueRandomSet")) {
				
				allHidingAgents.add(new UniqueRandomSet(graphController, numberOfHideLocations));
			
			}
			
			if (hiderType.getElement0().equals("UniqueRandomSetRepeat")) {
				
				allHidingAgents.add(new UniqueRandomSetRepeat(graphController, numberOfHideLocations));
			
			} 
			
			//
			
			if (hiderType.getElement0().equals("RandomFixedDistance")) {
				
				allHidingAgents.add(new RandomFixedDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedDistanceFixedStart")) {
				
				allHidingAgents.add(new RandomFixedDistanceFixedStart(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomFixedDistanceStaticBetween")) {
				
				allHidingAgents.add(new RandomFixedDistanceStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostRandomFixedDistance(graphController, numberOfHideLocations));
			
			}
			
			if (hiderType.getElement0().equals("LowEdgeCostRandomFixedDistanceStaticBetween")) {
				
				allHidingAgents.add(new LowEdgeCostRandomFixedDistanceStaticBetween(graphController, numberOfHideLocations));
			
			}
			
			
			
			if (hiderType.getElement0().equals("VariableFixedDistance")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("VariableFixedDistanceFixedStart")) {
				
				allHidingAgents.add(new VariableFixedDistanceFixedStart(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("VariableFixedDistanceStaticBetween")) {
				
				allHidingAgents.add(new VariableFixedDistanceStaticBetween(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostVariableFixedDistance")) {
				
				allHidingAgents.add(new LowEdgeCostVariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			//
			
			if (hiderType.getElement0().equals("LeastConnected")) {
				
				allHidingAgents.add(new LeastConnected(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LeastConnectedStaticBetween")) {
				
				allHidingAgents.add(new LeastConnectedStaticBetween(graphController, numberOfHideLocations));
			
			} 

			if (hiderType.getElement0().equals("MaxDistance")) {
				
				allHidingAgents.add(new MaxDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, 1.0));
			
			} 
			
			if (hiderType.getElement0().equals("LowEdgeCostStaticBetween")) {
				
				allHidingAgents.add(new VariableLowEdgeCostStaticBetween(graphController, numberOfHideLocations, 1.0));
			
			}
			
			if (hiderType.getElement0().equals("EqualEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, 0.0));
			
			}
			
			if (hiderType.getElement0().equals("FixedStartEqualEdgeCost")) {
				
				allHidingAgents.add(new FixedStartVariableLowEdgeCost(graphController, numberOfHideLocations, 0.0));
			
			} 

			if (hiderType.getElement0().equals("VariableLowEdgeCost")) {
				
				allHidingAgents.add(new VariableLowEdgeCost(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartVariableLowEdgeCost")) {
				
				allHidingAgents.add(new FixedStartVariableLowEdgeCost(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			// Repeat-game:
			
			if (hiderType.getElement0().equals("FullyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 1.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FullyBias");
			
			}
			
			if (hiderType.getElement0().equals("FullyBiasStaticBetween")) {
				
				allHidingAgents.add(new VariableBiasStaticBetween(graphController, numberOfHideLocations, 1.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FullyBiasStaticBetween");
			
			}
			
			if (hiderType.getElement0().equals("FullyExplorative")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 0.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FullyExplorative");
			
			}
			
			if (hiderType.getElement0().equals("LooselyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 0.5));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("LooselyBias");
			
			} 
			
			if (hiderType.getElement0().equals("VariableBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartVariableBias")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartFullyBias")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, numberOfHideLocations, 1.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FixedStartFullyBias");
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartFullyExplorative")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, numberOfHideLocations, 0.0));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("FixedStartFullyExplorative");
			
			} 
			
			// Discovered through experimentation (~MDC 24/2/15 ?)
			if (hiderType.getElement0().equals("OptimalBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, 0.6));
			
			}

			//
			
			if (hiderType.getElement0().equals("VariableBiasLocations")) {
				
				allHidingAgents.add(new VariableBiasLocations(graphController, numberOfHideLocations, gameNumber));
			
			}
			
			////
			
			if (hiderType.getElement0().equals("SetDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2)));
			
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptiveNodes");
				
			}
	
			if (hiderType.getElement0().equals("VariableDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, gameNumber, (int)(Math.random() * totalGames)));
			
				allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptiveNodes");
				
			}
			
			//
			
			if (hiderType.getElement0().equals("SetDeceptionDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2)));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDuration");
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, gameNumber));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptionDuration");
			
			}

			if (hiderType.getElement0().equals("SetDeceptionDurationVariableDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, gameNumber, (int)(totalRounds / 2)));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationVariableDeceptiveNodes");
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationVariableDeceptiveNodes")) {
				
				for ( int a = 1; a <= numberOfHideLocations; a++) {
					
					allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, a, gameNumber));
					
					allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptionDurationVariableDeceptiveNodes-" + a);
					
				}
				
			}
			
			//
			
			boolean refreshDeceptiveNodes = false;
			
			if (hiderType.getElement0().contains("RefreshDeceptiveNodes")) { 
				
				refreshDeceptiveNodes = true;
				
				hiderType = Pair.createPair(hiderType.getElement0().substring(0, hiderType.getElement0().indexOf("RefreshDeceptiveNodes")), hiderType.getElement1());
			
			}
			
			// ~MDC Values (deception duration and repeat interval) considered to be 'optimal' via experimentation
			if (hiderType.getElement0().equals("SetDeceptionDurationSetDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, (int)(totalRounds / 2), refreshDeceptiveNodes));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationSetDeceptionIntervalSetRepeatDuration");
				
			}
			
			if (hiderType.getElement0().equals("SetDeceptionDurationVariableDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, gameNumber, totalRounds, refreshDeceptiveNodes));
			
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationVariableDeceptionIntervalSetRepeatDuration");
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationSetDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, gameNumber, 0, totalRounds, refreshDeceptiveNodes));
			
				allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptionDurationSetDeceptionIntervalSetRepeatDuration");
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationVariableDeceptionIntervalSetRepeatDuration")) {
				
				final int MAXINTERVAL = totalGames;
				
				for ( int interval = 0; interval < MAXINTERVAL; interval++) {
					
					allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, gameNumber, MAXINTERVAL, totalRounds, refreshDeceptiveNodes));
					
					allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptionDurationVariableDeceptionInterval-" + interval);
					
				}
				
			}

			// ~MDC Expanded as necessary by the results above
			
			if (hiderType.getElement0().equals("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, gameNumber, refreshDeceptiveNodes));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration");
				
			}
			
			if (hiderType.getElement0().equals("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration-NonUniqueRandom")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, gameNumber, refreshDeceptiveNodes, false));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration");
				
			}

			if (hiderType.getElement0().equals("VariableDeceptiveNodesSetDeceptionDurationSetDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, gameNumber, 1, 0, totalRounds, refreshDeceptiveNodes));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("SetDeceptionDurationSetDeceptionIntervalVariableDeceptiveNodes");
				
			}
			
			// ~Misc.

			if (hiderType.getElement0().equals("EpsilonDeceptive")) {
				
				allHidingAgents.add(new EpsilonDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 0, (double)(gameNumber / 100.0)));
				
			}
			
			if (hiderType.getElement0().equals("LeastConnectedDeceptive")) {
				
				allHidingAgents.add(new LeastConnectedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptiveSetDuration")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations,  (int)(totalRounds / 2)));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("GroupedDeceptiveSetDuration");
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptive")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptiveVariableDeceptionDuration")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 11));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("GroupedDeceptiveVariableDeceptionDuration");
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptiveSets")) {
				
				// ~MDC Choice of repeat duration here is arbitrary
				allHidingAgents.add(new Deceptive(graphController, numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2), 0, (int)(totalRounds / 2), 10));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptiveSets");
				
			}
			
			if (hiderType.getElement0().equals("VariableGroupedDeceptiveSets")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2), 0, (int)(totalRounds / 2), 10));
				
				allHidingAgents.get(allHidingAgents.size() - 1).setName("VariableDeceptiveSets");
				
			}
			
			// Adaptive:
			
			ArrayList<AdaptiveHiderStrategy> strategyPortfolio = new ArrayList<AdaptiveHiderStrategy>();
			
			if (hiderType.getElement0().equals("AdaptiveRandom")) {
				
				strategyPortfolio.clear();
				
				strategyPortfolio.add(new RandomSetAdaptable(graphController, numberOfHideLocations));
				
				strategyPortfolio.add(new UniqueRandomSetRepeatAdaptable(graphController, numberOfHideLocations));
				
				allHidingAgents.add(new AdaptiveHider<AdaptiveHiderStrategy>(graphController, totalRounds, strategyPortfolio.get(1), strategyPortfolio, 0.5, 0.5, 0.5, false));
				
			}
			
		}
		
		if (mixHiders) {
			
			Collections.shuffle(allHidingAgents);
			
			allHidingAgents = allHidingAgents.subList(0, 1);
			
			System.out.println("Strategy: " + allHidingAgents);
			
		}
		
		return allHidingAgents;
		
	}
	
	/**
	 * @param agentList
	 * @return
	 */
	private List<Seeker> initSeekers(String agentList, int numberOfHideLocations, boolean mixSeekers) {
		
		/**************************
    	 * 
    	 * Set up seeking agents
    	 * 
    	 * * * * * * * * * * * * */
		
		List<Seeker> allSeekingAgents = new ArrayList<Seeker>();
		 
		for( Pair<String, String> seekerType : Utils.stringToArray(agentList, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])") ) {
			
			// Single-shot:
			
			if (seekerType.getElement0().equals("RandomWalk")) {
				
				allSeekingAgents.add(new RandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("ConstrainedRandomWalk")) {
				
				allSeekingAgents.add(new ConstrainedRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("FixedStartRandomWalk")) {
				
				allSeekingAgents.add(new FixedStartRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("LowEdgeCost")) {
				
				allSeekingAgents.add(new LowEdgeCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearch")) {
				
				allSeekingAgents.add(new DepthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearchLowCost")) {
				
				allSeekingAgents.add(new DepthFirstSearchLowCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearch")) {
				
				allSeekingAgents.add(new BreadthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearchLowCost")) {
				
				allSeekingAgents.add(new BreadthFirstSearchLowCost(graphController));
				
			}
			
			if (seekerType.getElement0().equals("LeastConnectedFirst")) {
				
				allSeekingAgents.add(new LeastConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("MostConnectedFirst")) {
				
				allSeekingAgents.add(new MostConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BacktrackPath")) {
				
				allSeekingAgents.add(new BacktrackPath(graphController));
				
			}
			
			if (seekerType.getElement0().equals("VariableBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, gameNumber));
				
			}
			
			// Optimal backtrack path -- found by experimentation
			if (seekerType.getElement0().equals("OptimalBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, 1));
				
				allSeekingAgents.get(allSeekingAgents.size() - 1).setName("OptimalBacktrackPath");
				
			}

			// Repeat-game: 
			
			if (seekerType.getElement0().equals("HighProbability")) {
				
				allSeekingAgents.add(new HighProbability(graphController));
				
			}
			
			if (seekerType.getElement0().equals("VariableHistoryHighProbability")) {
				
				allSeekingAgents.add(new VariableHistoryHighProbability(graphController, gameNumber));
				
			}
			
			if (seekerType.getElement0().equals("HighProbabilityRepetitionCheck")) {
				
				allSeekingAgents.add(new HighProbabilityRepetitionCheck(graphController, 2, numberOfHideLocations));
				
			}
			
			if (seekerType.getElement0().equals("InverseHighProbability")) {
				
				allSeekingAgents.add(new InverseHighProbability(graphController));
				
			}
			
			// Adaptive:
			
			ArrayList<AdaptiveSeekerStrategy> strategyPortfolio = new ArrayList<AdaptiveSeekerStrategy>();
			
			if (seekerType.getElement0().equals("AdaptiveHighProbability")) {
				
				strategyPortfolio.clear();
				
				strategyPortfolio.add(new InverseHighProbabilityAdaptable(graphController, Integer.MAX_VALUE));
				
				strategyPortfolio.add(new HighProbabilityAdaptable(graphController));
				
				allSeekingAgents.add(new AdaptiveSeeker<AdaptiveSeekerStrategy>(graphController, totalRounds, strategyPortfolio.get(1), strategyPortfolio, 0.5, 0.5, 0.5, false));
				
			}
			
		}
		
		if (mixSeekers) {
			
			Collections.shuffle(allSeekingAgents);
			
			allSeekingAgents = allSeekingAgents.subList(0, 1);
			
			System.out.println("Strat: " + allSeekingAgents);
			
		}

		return allSeekingAgents;
		
	}
	
	/**
	 * Rounds are designed to re-test the same parameter configurations (which may vary between games)
	 * multiples times AND to allow for patterns or histories to develop
	 * 
	 * @param list2
	 * @param list
	 * @param rounds
	 * @param recordPerRound
	 */
	private void startRounds(List<Hider> hiders, List<Seeker> seekers, int rounds, boolean recordPerRound) {
		
		// Pre-round outputting
		
		FileWriter mainOutputWriter = null, outputJavascript = null, outputHTML = null;
		
		try {
			
			mainOutputWriter = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", true);
		
			if (OUTPUTJS) {
				
				outputJavascript = new FileWriter(Utils.FILEPREFIX + "/data/js/data/" + currentSimulationIdentifier + "-vis.js", true);
				
				outputHTML = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + "-vis.html", true);
			
			}
        
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
		if (OUTPUTJS) Utils.writeToFile(outputJavascript, "var graphNodes = \"" + graphController.edgeSet(this) + "\"; \n var hidden = new Array(); \n var path = new Array(); \n");
		
		/**************************
    	 * 
    	 * Main rounds loop
    	 * 
    	 * * * * * * * * * * * * */
		// Default repeat.
		int repeatAllRounds = 1;
		
		// Pre-checks for presence of strategy which is defined by a sequence of rounds,
		// not individual ones (e.g. deceptive), and thus must be tested multiple times, as sets of rounds, in addition.
		for ( Hider hider : hiders ) {
			
			// Dramatically affects the size of the output files
			if (hider.getStrategyOverRounds()) repeatAllRounds = 10; //rounds;
			
		}
		
		for ( Seeker  seeker : seekers ) {
			
			if (seeker.getStrategyOverRounds()) repeatAllRounds = 10; //rounds;
			
		}
		
		// Run rounds and record output per hider
		for ( Hider hider : hiders ) {
			
			Utils.talk("Main", hiders.toString());
			
			/* If changes occur over a set of rounds (over a game), by nature of the strategy,
			 * this process must repeat.
			 */
			for (int roundRepeat = 0; roundRepeat < repeatAllRounds; roundRepeat++) {
				
				hider.startPlaying();
				
				boolean lastRoundRepeat = false;
				if (roundRepeat == (repeatAllRounds - 1)) lastRoundRepeat = true;
				
				for (int i = 0; i < rounds; i++) {
		        	
		        	Utils.talk("Main", "Game " + gameNumber + " Round " + i);
		        	
		        	System.out.println( "Game " + gameNumber + " Round " + i + ": " + ( ( i / ( ( (float) rounds * hiders.size() ) ) ) * 100 ) + "%" );
		        	
					hider.run();
					
					for ( Seeker seeker : seekers ) {
						
						seeker.run();
						
					}
					
					graphController.clearHideLocations(this);
		    		
		    		if (OUTPUTJS) {
		    			
		    			// Visualise first hider and first seeker, for novelty, mainly.
		    			
		    			Utils.writeToFile(outputJavascript, "hidden[" + i + "] = \"" + hiders.get(0).getHideLocations() + "\"; \n");
		    		
		    			Utils.writeToFile(outputJavascript, "path[" + i + "] = \"" + graphController.latestRoundPaths(this, seekers.get(0)) + "\"; \n");
			    		
		    		}
		    		
		    		//
		    		
		    		if (recordPerRound) {
		        		
		    			Utils.writeToFile(mainOutputWriter, "R, " + hider.toString() + "," + hider.printRoundStats() + ",");
		    			
		    			Utils.talk("Main", hider.toString() + "," + hider.printRoundStats());
		    			
		    			for( Seeker seeker : seekers ) {
		    				
		    				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printRoundStats() + ",");
		    				
		    				Utils.talk("Main ", seeker.toString() + "," + seeker.printRoundStats());
		    				
		    			}
		    			
		    			Utils.writeToFile(mainOutputWriter, "\n");
		    			
		        	}
		    		
		    		hider.endOfRound();
		    		
		    		for (Seeker seeker : seekers) {
		    			
		    			seeker.endOfRound();
		    			
		    		}
		    		
		    		// Must be notified before hider and seeker are if these two agents wish to print latest score information
		    		graphController.notifyEndOfRound(this);
		    		
				}
				
				//
		    	
				if (OUTPUTJS) {
					
			    	ArrayList<String> javascriptOutputTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/js/vis-template.js");
			    	
			    	for ( String line : javascriptOutputTemplate ) {
			    		
			    		Utils.writeToFile(outputJavascript, line + "\n");
			    		
			    	}
			    			
					ArrayList<String> firstHalfHTMLTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/template/vis-template-1.html");
					
					for (String line : firstHalfHTMLTemplate) {
					
						Utils.writeToFile(outputHTML, line + "\n");
						
					}
					
					Utils.writeToFile(outputHTML, "<script type=\"text/javascript\" src=\"js/data/" + currentSimulationIdentifier + "-vis.js\"></script>");
			    	
					ArrayList<String> secondHalfHTMLTemplate = Utils.readFromFile(Utils.FILEPREFIX + "data/template/vis-template-2.html");
					
					for (String line : secondHalfHTMLTemplate) {
						
						Utils.writeToFile(outputHTML, line + "\n");
						
					}
				
				}
				
				hider.endOfGame();
				
				// Output hider stats
				
				if (lastRoundRepeat) {
					
					Utils.talk("Main", "End of game \n------------------------------------------");
					
					System.out.println("Main" + hider.toString() + "," + hider.printGameStats());
				
				}
				
				//if ( !recordPerRound ) {
				
					if (lastRoundRepeat) Utils.writeToFile(mainOutputWriter, "G, " + hider.toString() + "," + hider.printGameStats() + ",");
					
			    	// Output costs for Seekers
				
					for ( Seeker seeker : seekers ) {
					
						seeker.endOfGame();
						
						if (lastRoundRepeat) {
						
							Utils.talk("Main", seeker.toString() + "," + seeker.printGameStats());
						
							// Cost per round
						
							Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printGameStats() + ",");
						
						}
						
					}
					
					if (lastRoundRepeat) Utils.writeToFile(mainOutputWriter, "\n");
				
				//}
				
				graphController.resetGameEnvironment(this);
				
			} // End of hider loop
		
		} // End of repeat loop
		
		try {
			
			mainOutputWriter.close();
			
			if (OUTPUTJS) {
				
				outputJavascript.close();
				
				outputHTML.close();
			
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param hiders
	 * @param seekers
	 * @return
	 */
	private ArrayList<GraphTraverser> allTraversers(ArrayList<Hider> hiders, ArrayList<Seeker> seekers) {
		
		ArrayList<GraphTraverser> traversers = new ArrayList<GraphTraverser>();
		
		for (Hider hider : hiders) {
			
			traversers.add(hider);
			
		}
		
		for (Seeker seeker : seekers) {
			
			traversers.add(seeker);
			
		}
		
		return traversers;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Main(args);

	}

}
