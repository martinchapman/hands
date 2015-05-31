package HideAndSeek;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.hider.AdaptiveHider;
import HideAndSeek.hider.AdaptiveHidingAgent;
import HideAndSeek.hider.Hider;
import HideAndSeek.hider.repeatgame.bias.FixedStartVariableBias;
import HideAndSeek.hider.repeatgame.bias.VariableBias;
import HideAndSeek.hider.repeatgame.bias.VariableBiasLocations;
import HideAndSeek.hider.repeatgame.bias.VariableBiasStaticBetween;
import HideAndSeek.hider.repeatgame.deceptive.Deceptive;
import HideAndSeek.hider.repeatgame.deceptive.DeceptiveNew;
import HideAndSeek.hider.repeatgame.deceptive.EpsilonDeceptive;
import HideAndSeek.hider.repeatgame.deceptive.GroupedDeceptive;
import HideAndSeek.hider.repeatgame.deceptive.LeastConnectedDeceptive;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSet;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeat;
import HideAndSeek.hider.repeatgame.random.UniqueRandomSetRepeatRandomNodes;
import HideAndSeek.hider.repeatgame.random.automatic.AutomaticUniqueRandomSetRepeat;
import HideAndSeek.hider.singleshot.cost.FixedStartVariableGreedy;
import HideAndSeek.hider.singleshot.cost.VariableGreedy;
import HideAndSeek.hider.singleshot.cost.VariableGreedyStaticBetween;
import HideAndSeek.hider.singleshot.distance.GreedyRandomFixedDistance;
import HideAndSeek.hider.singleshot.distance.GreedyRandomFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.distance.GreedyVariableFixedDistance;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistance;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.distance.RandomFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistance;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistanceFixedStart;
import HideAndSeek.hider.singleshot.distance.VariableFixedDistanceStaticBetween;
import HideAndSeek.hider.singleshot.preference.LeastConnected;
import HideAndSeek.hider.singleshot.preference.LeastConnectedLeastConnectedFirst;
import HideAndSeek.hider.singleshot.preference.LeastConnectedStaticBetween;
import HideAndSeek.hider.singleshot.preference.MaxDistance;
import HideAndSeek.hider.singleshot.preference.MaxDistanceStaticBetween;
import HideAndSeek.hider.singleshot.preference.NotConnected;
import HideAndSeek.hider.singleshot.random.GreedyRandomSet;
import HideAndSeek.hider.singleshot.random.GreedyRandomSetStaticBetween;
import HideAndSeek.hider.singleshot.random.Random;
import HideAndSeek.hider.singleshot.random.RandomFixedStart;
import HideAndSeek.hider.singleshot.random.RandomSet;
import HideAndSeek.hider.singleshot.random.RandomSetStaticBetween;
import HideAndSeek.hider.singleshot.random.RandomStaticBetween;
import HideAndSeek.hider.singleshot.random.RandomVariableHidePotential;
import HideAndSeek.hider.singleshot.staticlocations.StaticLocations;
import HideAndSeek.seeker.AdaptiveSeeker;
import HideAndSeek.seeker.AdaptiveSeekingAgent;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.probability.HighProbability;
import HideAndSeek.seeker.repeatgame.probability.HighProbabilityRepetitionCheck;
import HideAndSeek.seeker.repeatgame.probability.InverseHighProbability;
import HideAndSeek.seeker.repeatgame.probability.VariableHistoryHighProbability;
import HideAndSeek.seeker.repeatgame.probability.adaptable.HighProbabilityAdaptable;
import HideAndSeek.seeker.repeatgame.probability.adaptable.InverseHighProbabilityAdaptable;
import HideAndSeek.seeker.singleshot.cost.Greedy;
import HideAndSeek.seeker.singleshot.coverage.BacktrackGreedy;
import HideAndSeek.seeker.singleshot.coverage.BacktrackPath;
import HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearch;
import HideAndSeek.seeker.singleshot.coverage.BreadthFirstSearchGreedy;
import HideAndSeek.seeker.singleshot.coverage.DepthFirstSearch;
import HideAndSeek.seeker.singleshot.coverage.DepthFirstSearchGreedy;
import HideAndSeek.seeker.singleshot.coverage.DepthFirstSearchMechanism;
import HideAndSeek.seeker.singleshot.coverage.RandomTarry;
import HideAndSeek.seeker.singleshot.coverage.VariableBacktrackPath;
import HideAndSeek.seeker.singleshot.preference.ApproximateLeastConnectedNodes;
import HideAndSeek.seeker.singleshot.preference.LinkedPath;
import HideAndSeek.seeker.singleshot.preference.MostConnectedFirst;
import HideAndSeek.seeker.singleshot.random.FixedStartRandomWalk;
import HideAndSeek.seeker.singleshot.random.RandomWalk;
import HideAndSeek.seeker.singleshot.random.SelfAvoidingRandomWalk;
import HideAndSeek.seeker.singleshot.random.SelfAvoidingRandomWalkGreedy;
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
	private final boolean OUTPUT_JS = false;
	
	/**
	 * 
	 */
	private String hiderList;
	
	/**
	 * 
	 */
	private String seekerList;
	
	/**
	 * 
	 */
	private int numberOfHideLocations;
	
	/**
	 * 
	 */
	private boolean mixHiders;
	
	/**
	 * 
	 */
	private boolean mixSeekers;
	
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
		
		numberOfVertices = numberOfVertices == 0 ? 1 : numberOfVertices;
		
		String fixedOrUpperBound = args[9];
		
		double fixedOrUpperValue = Double.parseDouble(args[8]);
		
		int edgeTraversalDecrement = Integer.parseInt(args[10]);
		
		initGraph(topology, numberOfVertices, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
		mixHiders = Boolean.parseBoolean(args[11]);
		
		mixSeekers = Boolean.parseBoolean(args[12]);
		
		boolean resetPerRound = Boolean.parseBoolean(args[13]);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[7]);
		
		this.totalRounds = rounds;
		
		hiderList = args[2];
		
		seekerList = args[3];
		
		numberOfHideLocations = Integer.parseInt(args[6]);
		
		startRounds(initHiders(hiderList, numberOfHideLocations, mixHiders), initSeekers(seekerList, numberOfHideLocations, mixSeekers), rounds, true, resetPerRound);
		
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
			
			if (hiderType.getElement0().equals("FirstK")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, "FirstK", numberOfHideLocations, 0));
				
			}
			
			if (hiderType.getElement0().equals("FirstKMinus1")) {
				
				allHidingAgents.add(new VariableFixedDistance(graphController, "FirstKMinus1", numberOfHideLocations, 0) {
					
					public boolean hideHere(StringVertex vertex) {
						
						if ( hideLocations().size() < numberOfHideLocations - 1) {
							
							return super.hideHere(vertex);
							
						} else {
							
							if ( currentNode == vertex ) {
								
								for ( StringVertex hideLocation : hideLocations() ) {
									
									for ( StringEdge edge : graphController.edgesOf(responsibleAgent, vertex) ) {
										
										if ( edgeToTarget(edge, vertex).equals(hideLocation) ) {
											
											return false;
											
										}
										
									}
									
								}
							
								Utils.talk(toString(), "Disconnected node: " + vertex +  " " + graphController.edgesOf(responsibleAgent, vertex));
								
								return true;
								
							} else {
								
								return false;
								
							}
							
						}
						
					}
					
				});
				
			}
			
			if (hiderType.getElement0().equals("NotConnected")) {
				
				allHidingAgents.add(new NotConnected(graphController, numberOfHideLocations));
				
				
			}
			if (hiderType.getElement0().equals("FirstKFixedStart")) {
				
				allHidingAgents.add(new VariableFixedDistanceFixedStart(graphController, "FirstKFixedStart", numberOfHideLocations, 0));
				
			
			}
			
			if (hiderType.getElement0().equals("FirstNStaticBetween")) {
				
				allHidingAgents.add(new VariableFixedDistanceStaticBetween(graphController, "FirstNStaticBetween", numberOfHideLocations, 0));

			}
			
			//
			
			if (hiderType.getElement0().equals("RandomSet")) {
				
				allHidingAgents.add(new RandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("RandomSetStaticBetween")) {
				
				allHidingAgents.add(new RandomSetStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("GreedyRandomSet")) {
				
				allHidingAgents.add(new GreedyRandomSet(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("GreedyRandomSetStaticBetween")) {
				
				allHidingAgents.add(new GreedyRandomSetStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("UniqueRandomSet")) {
				
				allHidingAgents.add(new UniqueRandomSet(graphController, numberOfHideLocations));
			
			}
			
			if (hiderType.getElement0().equals("UniqueRandomSetRepeat")) {
				
				allHidingAgents.add(new UniqueRandomSetRepeat(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("UniqueRandomSetRepeatRandomNodes")) {
				
				allHidingAgents.add(new UniqueRandomSetRepeatRandomNodes(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("AutomaticUniqueRandomSetRepeat")) {
				
				allHidingAgents.add(new AutomaticUniqueRandomSetRepeat(graphController, numberOfHideLocations, 3));
			
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
			
			if (hiderType.getElement0().equals("GreedyRandomFixedDistance")) {
				
				allHidingAgents.add(new GreedyRandomFixedDistance(graphController, numberOfHideLocations));
			
			}
			
			if (hiderType.getElement0().equals("GreedyRandomFixedDistanceStaticBetween")) {
				
				allHidingAgents.add(new GreedyRandomFixedDistanceStaticBetween(graphController, numberOfHideLocations));
			
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
			
			if (hiderType.getElement0().equals("GreedyVariableFixedDistance")) {
				
				allHidingAgents.add(new GreedyVariableFixedDistance(graphController, numberOfHideLocations, gameNumber));
			
			} 
			
			//
			
			if (hiderType.getElement0().equals("LeastConnected")) {
				
				allHidingAgents.add(new LeastConnected(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("VariableGraphKnowledgeLeastConnectedDFS")) {
				
				final class VariableGraphKnowledgeLeastConnectedDFS extends LeastConnected {
					
					public VariableGraphKnowledgeLeastConnectedDFS(GraphController <StringVertex, StringEdge> graphController, int numberOfHideLocations, double graphPortion) {
					
						super(graphController, numberOfHideLocations, graphPortion);
				
					}
					
					public OpenTraverserStrategy getExplorationMechanism(GraphTraverser responsibleAgent) {
						
						return new DepthFirstSearchMechanism(graphController, responsibleAgent);
						
					}
					
				}
				
				allHidingAgents.add(new VariableGraphKnowledgeLeastConnectedDFS(graphController, numberOfHideLocations, gameNumber / (double)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("LeastConnectedLeastConnectedFirst")) {
				
				allHidingAgents.add(new LeastConnectedLeastConnectedFirst(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("LeastConnectedStaticBetween")) {
				
				allHidingAgents.add(new LeastConnectedStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("VariableGraphKnowledgeLeastConnected")) {
				
				allHidingAgents.add(new LeastConnected(graphController, "VariableGraphKnowledgeLeastConnected", numberOfHideLocations, gameNumber / (double)totalGames));
				
			} 

			if (hiderType.getElement0().equals("MaxDistance")) {
				
				allHidingAgents.add(new MaxDistance(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("MaxDistanceStaticBetween")) {
				
				allHidingAgents.add(new MaxDistanceStaticBetween(graphController, numberOfHideLocations));
			
			} 
			
			if (hiderType.getElement0().equals("VariableGraphKnowledgeMaxDistance")) {
				
				allHidingAgents.add(new MaxDistance(graphController, "VariableGraphKnowledgeMaxDistance", numberOfHideLocations, gameNumber / (double)totalGames, -1));
				
			} 
			
			if (hiderType.getElement0().equals("Greedy")) {
				
				allHidingAgents.add(new VariableGreedy(graphController, numberOfHideLocations, 1.0));
			
			} 
			
			if (hiderType.getElement0().equals("GreedyStaticBetween")) {
				
				allHidingAgents.add(new VariableGreedyStaticBetween(graphController, numberOfHideLocations, 1.0));
			
			}
			
			if (hiderType.getElement0().equals("EqualEdgeCost")) {
				
				allHidingAgents.add(new VariableGreedy(graphController, numberOfHideLocations, 0.0));
			
			}
			
			if (hiderType.getElement0().equals("FixedStartEqualEdgeCost")) {
				
				allHidingAgents.add(new FixedStartVariableGreedy(graphController, numberOfHideLocations, 0.0));
			
			} 

			if (hiderType.getElement0().equals("VariableGreedy")) {
				
				allHidingAgents.add(new VariableGreedy(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartVariableGreedy")) {
				
				allHidingAgents.add(new FixedStartVariableGreedy(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			// Repeat-game:
			
			if (hiderType.getElement0().equals("FullyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, "FullyBias", numberOfHideLocations, 1.0));
			
			}
			
			if (hiderType.getElement0().equals("FullyBiasStaticBetween")) {
				
				allHidingAgents.add(new VariableBiasStaticBetween(graphController, "FullyBiasStaticBetween", numberOfHideLocations, 1.0));
				
			}
			
			if (hiderType.getElement0().equals("FullyExplorative")) {
				
				allHidingAgents.add(new VariableBias(graphController, "FullyExplorative", numberOfHideLocations, 0.0));
			
			}
			
			if (hiderType.getElement0().equals("LooselyBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, "LooselyBias", numberOfHideLocations, 0.5));
			
			} 
			
			if (hiderType.getElement0().equals("VariableBias")) {
				
				allHidingAgents.add(new VariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartVariableBias")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, numberOfHideLocations, gameNumber / (float)totalGames));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartFullyBias")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, "FixedStartFullyBias", numberOfHideLocations, 1.0));
			
			} 
			
			if (hiderType.getElement0().equals("FixedStartFullyExplorative")) {
				
				allHidingAgents.add(new FixedStartVariableBias(graphController, "FixedStartFullyExplorative", numberOfHideLocations, 0.0));
			
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
			
			if (hiderType.getElement0().equals("DeceptiveNew")) {
				
				allHidingAgents.add(new DeceptiveNew(graphController, "Deceptive", numberOfHideLocations, (int)(totalRounds / 2)));
			
			}
			
			if (hiderType.getElement0().equals("VariableDeceptiveNew")) {
				
				allHidingAgents.add(new DeceptiveNew(graphController, "Deceptive", numberOfHideLocations, gameNumber) {
					
					public boolean strategyOverRounds() {
						
						return true;
						
					}
					
				});
			
			}
			
			if (hiderType.getElement0().equals("SetDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptiveNodes", numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2)));
			
			}
	
			if (hiderType.getElement0().equals("VariableDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, "VariableDeceptiveNodes", numberOfHideLocations, gameNumber, (int)(Math.random() * totalGames)));
				
			}
			
			//
			
			if (hiderType.getElement0().equals("SetDeceptionDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDuration", numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2)));
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "VariableDeceptionDuration", numberOfHideLocations, numberOfHideLocations, gameNumber));
			
			}

			if (hiderType.getElement0().equals("SetDeceptionDurationVariableDeceptiveNodes")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationVariableDeceptiveNodes", numberOfHideLocations, gameNumber, (int)(totalRounds / 2)));
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationVariableDeceptiveNodes")) {
				
				for ( int a = 1; a <= numberOfHideLocations; a++) {
					
					allHidingAgents.add(new Deceptive(graphController, "VariableDeceptionDurationVariableDeceptiveNodes-" + a, numberOfHideLocations, a, gameNumber));
					
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
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationSetDeceptionIntervalSetRepeatDuration", numberOfHideLocations, numberOfHideLocations, 1, 0, (int)(totalRounds / 2), refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("SetDeceptionDurationVariableDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationVariableDeceptionIntervalSetRepeatDuration", numberOfHideLocations, numberOfHideLocations, 1, gameNumber, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationSetDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "VariableDeceptionDurationSetDeceptionIntervalSetRepeatDuration", numberOfHideLocations, numberOfHideLocations, gameNumber, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptionDurationVariableDeceptionIntervalSetRepeatDuration")) {
				
				final int MAXINTERVAL = totalGames;
				
				for ( int interval = 0; interval < MAXINTERVAL; interval++) {
					
					allHidingAgents.add(new Deceptive(graphController, "VariableDeceptionDurationVariableDeceptionInterval-" + interval, numberOfHideLocations, numberOfHideLocations, gameNumber, MAXINTERVAL, totalRounds, refreshDeceptiveNodes));
					
				}
				
			}

			// ~MDC Expanded as necessary by the results above
			
			if (hiderType.getElement0().equals("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration", numberOfHideLocations, numberOfHideLocations, 1, 0, gameNumber, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration-NonUniqueRandom")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration", numberOfHideLocations, numberOfHideLocations, 1, 0, gameNumber, refreshDeceptiveNodes, false));
				
			}

			if (hiderType.getElement0().equals("VariableDeceptiveNodesSetDeceptionDurationSetDeceptionIntervalSetRepeatDuration")) {
				
				allHidingAgents.add(new Deceptive(graphController, "SetDeceptionDurationSetDeceptionIntervalVariableDeceptiveNodes", numberOfHideLocations, gameNumber, 1, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			// ~Misc.

			if (hiderType.getElement0().equals("EpsilonDeceptive")) {
				
				allHidingAgents.add(new EpsilonDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 0, (double)(gameNumber / 100.0)));
				
			}
			
			if (hiderType.getElement0().equals("LeastConnectedDeceptive")) {
				
				allHidingAgents.add(new LeastConnectedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptiveSetDuration")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, "GroupedDeceptiveSetDuration", numberOfHideLocations, numberOfHideLocations,  (int)(totalRounds / 2)));
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptive")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, numberOfHideLocations, numberOfHideLocations, 1, 0, totalRounds, refreshDeceptiveNodes));
				
			}
			
			if (hiderType.getElement0().equals("GroupedDeceptiveVariableDeceptionDuration")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, "GroupedDeceptiveVariableDeceptionDuration", numberOfHideLocations, numberOfHideLocations, 11));
				
			}
			
			if (hiderType.getElement0().equals("VariableDeceptiveSets")) {
				
				// ~MDC Choice of repeat duration here is arbitrary
				allHidingAgents.add(new Deceptive(graphController, "VariableDeceptiveSets", numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2), 0, (int)(totalRounds / 2), 10));
				
			}
			
			if (hiderType.getElement0().equals("VariableGroupedDeceptiveSets")) {
				
				allHidingAgents.add(new GroupedDeceptive(graphController, "VariableDeceptiveSets", numberOfHideLocations, numberOfHideLocations, (int)(totalRounds / 2), 0, (int)(totalRounds / 2), 10));
				
			}
			
			// Unknown:
			
			ArrayList<AdaptiveHider> strategyPortfolio = new ArrayList<AdaptiveHider>();
			
			if (hiderType.getElement0().equals("UnknownRandom")) {
				
				strategyPortfolio.clear();
				
				abstract class RandomSetAnonymous extends RandomSet implements AdaptiveHider {
					public RandomSetAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
						super(graphController, name, numberOfHideLocations);
					} 
					
				}
				
				strategyPortfolio.add(new RandomSetAnonymous(graphController, "RandomSet", numberOfHideLocations) {
					public double relevanceOfStrategy() { return -1; }
					public double performanceOfOpponent() { return -1; }
					public double performanceOfSelf() { return -1; }
					public void stopStrategy() {}
				});
				
				abstract class AutomaticUniqueRandomSetRepeatAnonymous extends AutomaticUniqueRandomSetRepeat implements AdaptiveHider {
					public AutomaticUniqueRandomSetRepeatAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int goodPerformanceRounds) {
						super(graphController, name, numberOfHideLocations, goodPerformanceRounds);
					}
				}
				
				strategyPortfolio.add(new AutomaticUniqueRandomSetRepeatAnonymous(graphController, "AutomaticUniqueRandomSetRepeat", numberOfHideLocations, 3) {
					public double relevanceOfStrategy() { return -1; }
					public double performanceOfOpponent() { return -1; }
					public double performanceOfSelf() { return -1; }
					public void stopStrategy() {}
				});
				
				allHidingAgents.add(new AdaptiveHidingAgent<AdaptiveHider>(graphController, "UnknownRandom", strategyPortfolio, totalRounds));
				
			}
			
		}
		
		if (mixHiders) {
			
			Collections.shuffle(allHidingAgents);
			
			allHidingAgents = allHidingAgents.subList(0, 1);
			
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
			
			if (seekerType.getElement0().equals("SelfAvoidingRandomWalk")) {
				
				allSeekingAgents.add(new SelfAvoidingRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("SelfAvoidingRandomWalkGreedy")) {
				
				allSeekingAgents.add(new SelfAvoidingRandomWalkGreedy(graphController));
				
			}
			
			if (seekerType.getElement0().equals("FixedStartRandomWalk")) {
				
				allSeekingAgents.add(new FixedStartRandomWalk(graphController));
				
			}
			
			if (seekerType.getElement0().equals("Greedy")) {
				
				allSeekingAgents.add(new Greedy(graphController));
				
			}
			
			if (seekerType.getElement0().equals("RepeatGreedy")) {
				
				allSeekingAgents.add(new Greedy(graphController) {
					
					public StringVertex nextNode(StringVertex currentNode) {
						
						uniquelyVisitNodes = false;
						
						return super.nextNode(currentNode);
						
					}
					
				});
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearch")) {
				
				allSeekingAgents.add(new DepthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("DepthFirstSearchGreedy")) {
				
				allSeekingAgents.add(new DepthFirstSearchGreedy(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearch")) {
				
				allSeekingAgents.add(new BreadthFirstSearch(graphController));
				
			}
			
			if (seekerType.getElement0().equals("BreadthFirstSearchGreedy")) {
				
				allSeekingAgents.add(new BreadthFirstSearchGreedy(graphController));
				
			}
			
			/*if (seekerType.getElement0().equals("LeastConnectedFirst")) {
				
				allSeekingAgents.add(new LeastConnectedFirst(graphController));
				
			}*/
			// Instead:
			if (seekerType.getElement0().equals("LeastConnectedFirst")) {
				
				allSeekingAgents.add(new HideAndSeek.seeker.singleshot.preference.LeastConnected(graphController, "LeastConnectedFirst", 1.0));
			
			}
			
			if (seekerType.getElement0().equals("VariableLeastConnectedFirst")) {
			
				allSeekingAgents.add(new HideAndSeek.seeker.singleshot.preference.LeastConnected(graphController, "VariableLeastConnectedFirst", gameNumber / (float)totalGames));
			
			}
			
			if (seekerType.getElement0().equals("MostConnectedFirst")) {
				
				allSeekingAgents.add(new MostConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("ApproximateLeastConnectedNodes")) {
				
				allSeekingAgents.add(new ApproximateLeastConnectedNodes(graphController));
				
			}
			
			if (seekerType.getElement0().equals("MaxDistanceFirst")) {
				
				allSeekingAgents.add(new HideAndSeek.seeker.singleshot.preference.MaxDistance(graphController, "MaxDistanceFirst", 1.0));
			
			}
			
			if (seekerType.getElement0().equals("LinkedPath")) {
				
				allSeekingAgents.add(new LinkedPath(graphController, 0));
				
			}
			
			if (seekerType.getElement0().equals("BacktrackPath")) {
				
				allSeekingAgents.add(new BacktrackPath(graphController));
				
			}
			
			if (seekerType.getElement0().equals("VariableBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, gameNumber));
				
			}
			
			// Optimal backtrack path -- found by experimentation
			if (seekerType.getElement0().equals("OptimalBacktrackPath")) {
				
				allSeekingAgents.add(new VariableBacktrackPath(graphController, "OptimalBacktrackPath", 1));
				
			}
			
			if (seekerType.getElement0().equals("BacktrackGreedy")) {
				
				allSeekingAgents.add(new BacktrackGreedy(graphController));
			
			}
			
			if (seekerType.getElement0().equals("NearestNeighbour")) {
		
				final class BacktrackGreedyWithoutBacktracking extends BacktrackGreedy {
					public BacktrackGreedyWithoutBacktracking(GraphController<StringVertex, StringEdge> graphController) { super(graphController); }
					public boolean backtracks() { return false; }
				}
				
				allSeekingAgents.add(new BacktrackGreedyWithoutBacktracking(graphController));
			
			}
			
			if (seekerType.getElement0().equals("RandomTarry")) {
				
				allSeekingAgents.add(new RandomTarry(graphController));
			
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
			
			ArrayList<AdaptiveSeeker> strategyPortfolio = new ArrayList<AdaptiveSeeker>();
			
			if (seekerType.getElement0().equals("AdaptiveHighProbability")) {
				
				strategyPortfolio.clear();
				
				strategyPortfolio.add(new InverseHighProbabilityAdaptable(graphController, "Inverse High Probability", Integer.MAX_VALUE));
				
				strategyPortfolio.add(new HighProbabilityAdaptable(graphController, "High Probability"));
				
				allSeekingAgents.add(new AdaptiveSeekingAgent<AdaptiveSeeker>(graphController, totalRounds, strategyPortfolio.get(1), strategyPortfolio, 0.5, 0.5, 0.5, false));
				
			}
			
		}
		
		if (mixSeekers) {
			
			Collections.shuffle(allSeekingAgents);
			
			allSeekingAgents = allSeekingAgents.subList(0, 1);
			
		}
		
		return allSeekingAgents;
		
	}
	
	/**
	 * Rounds are designed to re-test the same parameter configurations (which may vary between games)
	 * multiples times AND to allow for patterns or histories to develop
	 * 
	 * @param hiders
	 * @param seekers
	 * @param rounds
	 * @param recordPerRound
	 */
	private void startRounds(List<Hider> hiders, List<Seeker> seekers, int rounds, boolean recordPerRound, boolean resetPerRound) {
		
		// Pre-round outputting
		
		FileWriter mainOutputWriter = null, outputJavascript = null, outputHTML = null;
		
		try {
			
			mainOutputWriter = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", true);
		
			if (OUTPUT_JS) {
				
				outputJavascript = new FileWriter(Utils.FILEPREFIX + "/data/js/data/" + currentSimulationIdentifier + "-vis.js", true);
				
				outputHTML = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + "-vis.html", true);
			
			}
        
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
		if (OUTPUT_JS) Utils.writeToFile(outputJavascript, "var graphNodes = \"" + graphController.edgeSet(this) + "\"; \n var hidden = new Array(); \n var path = new Array(); \n");
		
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
			if (hider.strategyOverRounds()) repeatAllRounds = 10; //rounds;
			
		}
		
		for ( Seeker  seeker : seekers ) {
			
			if (seeker.strategyOverRounds()) repeatAllRounds = 10; //rounds;
			
		}
		
		// Run rounds and record output per hider
		for ( Hider hider : hiders ) {
			
			Utils.talk("Main", hiders.toString());
			
			/* If changes occur over a set of rounds (over a game), by nature of the strategy,
			 * this process must repeat (i.e. to check how a strategy evolves over different 
			 * rounds under a given parameter).
			 */
			for (int roundRepeat = 0; roundRepeat < repeatAllRounds; roundRepeat++) {
				
				hider.startPlaying();
				
				boolean lastRoundRepeat = false;
				
				if (roundRepeat == (repeatAllRounds - 1)) lastRoundRepeat = true;
				
				for (int i = 0; i < rounds; i++) {
		        	
		        	System.out.println("Game " + gameNumber + " Round " + i + ": " + ( ( i / ( ( (float) rounds * hiders.size() ) ) ) * totalGames ) + "%");
		        	
		        	/* 
		        	 * Allows rounds to act as individual games. Useful for varying parameters per a set
		        	 * of games, automatically
		        	 */
		        	if ( resetPerRound ) {
		        	
		        		graphController.generateGraph(graphController.getTopologyProperties().getType(), graphController.vertexSet().size());
		        		
		        		Utils.talk("Main", "Resetting " + hider);
		        		
		        		for ( Hider newHiderObject : initHiders(hiderList, numberOfHideLocations, mixHiders) ) {
		        			
		        			if ( newHiderObject.toString().equals(hider.toString()) ) {
		        				
		        				hider = newHiderObject;
		        				
		        			}
		        			
		        		}
		        		
		        		hider.startPlaying();
		        	
		        	}
		        	
					hider.run();
					
					ArrayList<Seeker> newSeekers = new ArrayList<Seeker>();
					
					if ( resetPerRound ) { 
						
						for ( Seeker seeker : seekers ) {
							
							Utils.talk("Main", "Resetting " + seeker);
							
							for ( Seeker newSeekerObject : initSeekers(seekerList, numberOfHideLocations, mixSeekers) ) {
			        			
			        			if ( newSeekerObject.toString().equals(seeker.toString()) ) {
			        				
			        				seeker = newSeekerObject;
			        				
			        			}
			        			
			        		}
							
							newSeekers.add(seeker);
							
						}
					
						seekers = newSeekers;
						
					}
					
					for ( Seeker seeker : seekers ) {
						
						seeker.run();
						
					}
					
					graphController.clearHideLocations(this);
		    		
		    		if (OUTPUT_JS) {
		    			
		    			// Visualise first hider and first seeker, for novelty, mainly.
		    			
		    			Utils.writeToFile(outputJavascript, "hidden[" + i + "] = \"" + hiders.get(0).requestHideLocations(hiders.get(0)) + "\"; \n");
		    		
		    			Utils.writeToFile(outputJavascript, "path[" + i + "] = \"" + graphController.latestRoundPaths(this, seekers.get(0)) + "\"; \n");
			    		
		    		}
		    		
		    		//
		    		
		    		if (recordPerRound) {
		        		
		    			Utils.writeToFile(mainOutputWriter, "R, " + hider.toString() + "," + hider.printRoundStats() + ",");
		    			
		    			Utils.talk("Main", hider.toString() + "," + hider.printRoundStats());
		    			
		    			for( Seeker seeker : seekers ) {
		    				
		    				Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printRoundStats() + ",");
		    				
		    				Utils.talk("Main", seeker.toString() + "," + seeker.printRoundStats());
		    				
		    			}
		    			
		    			Utils.writeToFile(mainOutputWriter, "\n");
		    			
		        	}
		    		
		    		hider.endOfRound();
		    		
		    		for (Seeker seeker : seekers) {
		    			
		    			seeker.endOfRound();
		    			
		    		}
		    		
		    		// Must be notified before hider and seeker are if these two agents wish to print latest payoff information
		    		graphController.notifyEndOfRound(this);
		    		
				}
				
				//
		    	
				if (OUTPUT_JS) {
					
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
					
					Utils.talk("Main", hider.toString() + "," + hider.printGameStats());
				
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
			
			if (OUTPUT_JS) {
				
				outputJavascript.close();
				
				outputHTML.close();
			
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param originalHider
	 * @return
	 */
	private Hider newHiderObject(Hider originalHider) {
		
		String hiderName = originalHider.toString().substring(1);
		
		try {
			
			Constructor<?> ctor = originalHider.getClass().getConstructor(GraphController.class, int.class);
			
			Object object = ctor.newInstance(new Object[] { graphController, originalHider.numberOfHideLocations() });
		
			return (Hider)object;
			
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		
		} catch (SecurityException e) {

			e.printStackTrace();
		
		}
		
		return null;
		
	}
	
	/**
	 * @param originalHider
	 * @return
	 */
	private Seeker newSeekerObject(Seeker originalSeeker) {
		
		String seekerName = originalSeeker.toString().substring(1);
			
		try {
			
			Constructor<?> ctor = originalSeeker.getClass().getConstructor(GraphController.class);
			
			Object object = ctor.newInstance(new Object[] { graphController });
		
			return (Seeker)object;
			
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		
		} catch (SecurityException e) {

			e.printStackTrace();
		
		}
		
		return null;
		
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
