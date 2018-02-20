package hideandseek;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Utility.Pair;
import Utility.Utils;
import Utility.adaptive.AdaptiveMeasure;
import Utility.adaptive.AdaptiveWeightings;
import hideandseek.graph.GraphController;
import hideandseek.graph.StringEdge;
import hideandseek.graph.StringVertex;
import hideandseek.hider.AdaptiveHider;
import hideandseek.hider.AdaptiveHidingAgent;
import hideandseek.hider.Hider;
import hideandseek.hider.repeatgame.bias.FixedStartVariableBias;
import hideandseek.hider.repeatgame.bias.VariableBias;
import hideandseek.hider.repeatgame.bias.VariableBiasLocations;
import hideandseek.hider.repeatgame.bias.VariableBiasStaticBetween;
import hideandseek.hider.repeatgame.deceptive.Deceptive;
import hideandseek.hider.repeatgame.deceptive.DeceptiveNew;
import hideandseek.hider.repeatgame.deceptive.EpsilonDeceptive;
import hideandseek.hider.repeatgame.deceptive.GroupedDeceptive;
import hideandseek.hider.repeatgame.deceptive.LeastConnectedDeceptive;
import hideandseek.hider.repeatgame.random.UniqueRandomSet;
import hideandseek.hider.repeatgame.random.UniqueRandomSetRepeat;
import hideandseek.hider.repeatgame.random.UniqueRandomSetRepeatRandomNodes;
import hideandseek.hider.repeatgame.random.adaptable.RandomSetAdaptable;
import hideandseek.hider.repeatgame.random.adaptable.UniqueRandomSetRepeatAdaptable;
import hideandseek.hider.repeatgame.random.automatic.AutomaticUniqueRandomSetRepeat;
import hideandseek.hider.singleshot.cost.FixedStartVariableGreedy;
import hideandseek.hider.singleshot.cost.VariableGreedy;
import hideandseek.hider.singleshot.cost.VariableGreedyStaticBetween;
import hideandseek.hider.singleshot.distance.GreedyRandomFixedDistance;
import hideandseek.hider.singleshot.distance.GreedyRandomFixedDistanceStaticBetween;
import hideandseek.hider.singleshot.distance.GreedyVariableFixedDistance;
import hideandseek.hider.singleshot.distance.RandomFixedDistance;
import hideandseek.hider.singleshot.distance.RandomFixedDistanceFixedStart;
import hideandseek.hider.singleshot.distance.RandomFixedDistanceStaticBetween;
import hideandseek.hider.singleshot.distance.VariableFixedDistance;
import hideandseek.hider.singleshot.distance.VariableFixedDistanceFixedStart;
import hideandseek.hider.singleshot.distance.VariableFixedDistanceStaticBetween;
import hideandseek.hider.singleshot.preference.LeastConnected;
import hideandseek.hider.singleshot.preference.LeastConnectedLeastConnectedFirst;
import hideandseek.hider.singleshot.preference.LeastConnectedStaticBetween;
import hideandseek.hider.singleshot.preference.MaxDistance;
import hideandseek.hider.singleshot.preference.MaxDistanceStaticBetween;
import hideandseek.hider.singleshot.preference.NotConnected;
import hideandseek.hider.singleshot.preference.adaptable.LeastConnectedAdaptable;
import hideandseek.hider.singleshot.random.GreedyRandomSet;
import hideandseek.hider.singleshot.random.GreedyRandomSetStaticBetween;
import hideandseek.hider.singleshot.random.Random;
import hideandseek.hider.singleshot.random.RandomFixedStart;
import hideandseek.hider.singleshot.random.RandomSet;
import hideandseek.hider.singleshot.random.RandomSetStaticBetween;
import hideandseek.hider.singleshot.random.RandomStaticBetween;
import hideandseek.hider.singleshot.random.RandomVariableHidePotential;
import hideandseek.hider.singleshot.staticlocations.StaticLocations;
import hideandseek.seeker.AdaptiveSeeker;
import hideandseek.seeker.AdaptiveSeekingAgent;
import hideandseek.seeker.Seeker;
import hideandseek.seeker.repeatgame.preference.ApproximateLeastConnectedNodes;
import hideandseek.seeker.repeatgame.probability.HighProbability;
import hideandseek.seeker.repeatgame.probability.HighProbabilityRepetitionCheck;
import hideandseek.seeker.repeatgame.probability.InverseHighProbability;
import hideandseek.seeker.repeatgame.probability.VariableHistoryHighProbability;
import hideandseek.seeker.repeatgame.probability.VariableNodesHighProbability;
import hideandseek.seeker.repeatgame.probability.adaptable.HighProbabilityAdaptable;
import hideandseek.seeker.repeatgame.probability.adaptable.InverseHighProbabilityAdaptable;
import hideandseek.seeker.singleshot.cost.Greedy;
import hideandseek.seeker.singleshot.coverage.BacktrackGreedy;
import hideandseek.seeker.singleshot.coverage.BacktrackPath;
import hideandseek.seeker.singleshot.coverage.BreadthFirstSearch;
import hideandseek.seeker.singleshot.coverage.BreadthFirstSearchGreedy;
import hideandseek.seeker.singleshot.coverage.DepthFirstSearch;
import hideandseek.seeker.singleshot.coverage.DepthFirstSearchGreedy;
import hideandseek.seeker.singleshot.coverage.DepthFirstSearchMechanism;
import hideandseek.seeker.singleshot.coverage.RandomTarry;
import hideandseek.seeker.singleshot.coverage.VariableBacktrackPath;
import hideandseek.seeker.singleshot.preference.LinkedPath;
import hideandseek.seeker.singleshot.preference.MostConnectedFirst;
import hideandseek.seeker.singleshot.random.FixedStartRandomWalk;
import hideandseek.seeker.singleshot.random.RandomWalk;
import hideandseek.seeker.singleshot.random.SelfAvoidingRandomWalk;
import hideandseek.seeker.singleshot.random.SelfAvoidingRandomWalkGreedy;

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
	 * 
	 */
	private boolean generateOutput;
	
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
		
		numberOfHideLocations = Integer.parseInt(args[6]);
		
		if ( numberOfVertices < numberOfHideLocations ) {
			
			throw new UnsupportedOperationException("More objects to hide than there are vertices.");
			
		}
		
		initGraph(topology, numberOfVertices, numberOfHideLocations, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
		mixHiders = Boolean.parseBoolean(args[11]);
		
		mixSeekers = Boolean.parseBoolean(args[12]);
		
		boolean resetPerRound = Boolean.parseBoolean(args[13]);
		
		boolean strategyOverRounds = Boolean.parseBoolean(args[14]);
		
		generateOutput = Boolean.parseBoolean(args[15]);
		
		//
		
		String agentList;
		
		int rounds = Integer.parseInt(args[7]);
		
		this.totalRounds = rounds;
		
		hiderList = args[2];
		
		seekerList = args[3];
		
		startRounds(initHiders(hiderList, numberOfHideLocations, mixHiders), initSeekers(seekerList, mixSeekers), rounds, true, resetPerRound, strategyOverRounds);
		
	}
	
	/**
	 * @param args
	 */
	private void initGraph(String topology, int numberOfVertices, int numberOfHideLocations, String fixedOrUpperBound, double fixedOrUpperValue, int edgeTraversalDecrement) {
		
		graphController = new GraphController<StringVertex, StringEdge>(topology, numberOfVertices, numberOfHideLocations, fixedOrUpperBound, fixedOrUpperValue, edgeTraversalDecrement);
		
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
			
			if (hiderType.getElement0().equals("DeceptiveTemp")) {
				
				allHidingAgents.add(new DeceptiveNew(graphController, "Deceptive", numberOfHideLocations, totalRounds - ( totalRounds / 10 )));
			
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
			if (hiderType.getElement0().equals("UnknownRandom")) {
				
				ArrayList<Pair<AdaptiveHider, Double>> strategyPortfolio = new ArrayList<Pair<AdaptiveHider, Double>>();
				
				abstract class RandomSetAnonymous extends RandomSet implements AdaptiveHider {
					public RandomSetAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
						super(graphController, name, numberOfHideLocations);
					} 
					
				}
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new RandomSetAnonymous(graphController, "RandomSet", numberOfHideLocations) {
					public AdaptiveMeasure environmentalMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure socialMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure internalMeasure(ArrayList<Double> strategyPerformance) { return new AdaptiveMeasure(0.0); }
					public AdaptiveWeightings getAdaptiveWeightings() { return new AdaptiveWeightings(0.33, 0.33, 0.33); }
					public void stopStrategy() {}
				
				}, 0.0));
				
				abstract class UniqueRandomSetRepeatAnonymous extends UniqueRandomSetRepeat implements AdaptiveHider {
					public UniqueRandomSetRepeatAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int goodPerformanceRounds) {
						super(graphController, name, numberOfHideLocations);
					}
				}
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new UniqueRandomSetRepeatAnonymous(graphController, "UniqueRandomSetRepeat", numberOfHideLocations, 3) {
					public AdaptiveMeasure environmentalMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure socialMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure internalMeasure(ArrayList<Double> strategyPerformance) { return new AdaptiveMeasure(0.0); }
					public AdaptiveWeightings getAdaptiveWeightings() { return new AdaptiveWeightings(0.33, 0.33, 0.33); }
					public void stopStrategy() {}
				}, 0.0));
				
				/*abstract class AutomaticUniqueRandomSetRepeatAnonymous extends AutomaticUniqueRandomSetRepeat implements AdaptiveHider {
					public AutomaticUniqueRandomSetRepeatAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations, int goodPerformanceRounds) {
						super(graphController, name, numberOfHideLocations, goodPerformanceRounds);
					}
				}
				
				strategyPortfolio.add(new AutomaticUniqueRandomSetRepeatAnonymous(graphController, "AutomaticUniqueRandomSetRepeat", numberOfHideLocations, 3) {
					public double environmentalMeasure() { return 0; }
					public double socialMeasure() { return 0; }
					public double internalMeasure(ArrayList<Double> strategyPerformance) { return 0; }
					public void stopStrategy() {}
				});*/
				
				allHidingAgents.add(new AdaptiveHidingAgent<AdaptiveHider>(graphController, "UnknownRandom", strategyPortfolio, totalRounds));
				
			}
			
			if (hiderType.getElement0().contains("MetaRandom")) {
				
				ArrayList<Pair<AdaptiveHider, Double>> strategyPortfolio = new ArrayList<Pair<AdaptiveHider, Double>>();
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new RandomSetAdaptable(graphController, numberOfHideLocations), 0.84));
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new UniqueRandomSetRepeatAdaptable(graphController, numberOfHideLocations), 0.16));
				
				allHidingAgents.add(new AdaptiveHidingAgent<AdaptiveHider>(graphController, "MetaRandom", strategyPortfolio, totalRounds));
				
			}
			
			if (hiderType.getElement0().contains("MetaConnected")) {
				
				ArrayList<Pair<AdaptiveHider, Double>> strategyPortfolio = new ArrayList<Pair<AdaptiveHider, Double>>();
				
				abstract class RandomSetAnonymous extends RandomSet implements AdaptiveHider {
					public RandomSetAnonymous(GraphController<StringVertex, StringEdge> graphController, String name, int numberOfHideLocations) {
						super(graphController, name, numberOfHideLocations);
					} 
					
				}
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new RandomSetAnonymous(graphController, "RandomSet", numberOfHideLocations) {
					public AdaptiveMeasure environmentalMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure socialMeasure() { return new AdaptiveMeasure(0.0); }
					public AdaptiveMeasure internalMeasure(ArrayList<Double> strategyPerformance) { return new AdaptiveMeasure(0.0); }
					public AdaptiveWeightings getAdaptiveWeightings() { return new AdaptiveWeightings(0.33, 0.33, 0.33); }
					public void stopStrategy() {}
				
				}, 0.0));
				
				strategyPortfolio.add(new Pair<AdaptiveHider, Double>(new LeastConnectedAdaptable(graphController, numberOfHideLocations), 0.0));
					
				allHidingAgents.add(new AdaptiveHidingAgent<AdaptiveHider>(graphController, "MetaConnected", strategyPortfolio, totalRounds, "LeastConnectedAdaptable"));
					
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
	private List<Seeker> initSeekers(String agentList, boolean mixSeekers) {
		
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
				
				allSeekingAgents.add(new hideandseek.seeker.singleshot.preference.LeastConnected(graphController, "LeastConnectedFirst", 1.0));
			
			}
			
			if (seekerType.getElement0().equals("VariableLeastConnectedFirst")) {
			
				allSeekingAgents.add(new hideandseek.seeker.singleshot.preference.LeastConnected(graphController, "VariableLeastConnectedFirst", gameNumber / (float)totalGames));
			
			}
			
			if (seekerType.getElement0().equals("MostConnectedFirst")) {
				
				allSeekingAgents.add(new MostConnectedFirst(graphController));
				
			}
			
			if (seekerType.getElement0().equals("ApproximateLeastConnectedNodes")) {
				
				allSeekingAgents.add(new ApproximateLeastConnectedNodes(graphController));
				
			}
			
			if (seekerType.getElement0().equals("MaxDistanceFirst")) {
				
				allSeekingAgents.add(new hideandseek.seeker.singleshot.preference.MaxDistance(graphController, "MaxDistanceFirst", 1.0));
			
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
			
			if (seekerType.getElement0().equals("HighProbabilityK")) {
				
				allSeekingAgents.add(new VariableNodesHighProbability(graphController, "HighProbabilityK", numberOfHideLocations, false));
				
			}
			
			if (seekerType.getElement0().equals("VariableNodesHighProbability")) {
				
				allSeekingAgents.add(new VariableNodesHighProbability(graphController, gameNumber, true));
				
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
			
			ArrayList<Pair<AdaptiveSeeker, Double>> strategyPortfolio = new ArrayList<Pair<AdaptiveSeeker, Double>>();
			
			if (seekerType.getElement0().contains("MetaProbability")) {
				
				strategyPortfolio.clear();
				
				strategyPortfolio.add(new Pair<AdaptiveSeeker, Double>(new InverseHighProbabilityAdaptable(graphController, Integer.MAX_VALUE), 0.24));
				
				strategyPortfolio.add(new Pair<AdaptiveSeeker, Double>(new HighProbabilityAdaptable(graphController), 0.76));
				
				allSeekingAgents.add(new AdaptiveSeekingAgent<AdaptiveSeeker>(graphController, "MetaProbability", strategyPortfolio, totalRounds, 0.5, false) {
					
					/* ~MDC Should be moved into the actual strategy
					 * (non-Javadoc)
					 * @see HideAndSeek.AdaptiveGraphTraversingAgent#confidenceLevel()
					 */
					protected double confidenceLevel() {
						
						return uniqueHideLocations().size() / (double)graphController.vertexSet().size();
					
					}
					
				});
				
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
	private void startRounds(List<Hider> hiders, List<Seeker> seekers, int rounds, boolean recordPerRound, boolean resetPerRound, boolean strategyOverRounds) {
		
		// Pre-round outputting
		
		FileWriter mainOutputWriter = null, outputJavascript = null, outputHTML = null;
		
		try {
			
		    if ( generateOutput ) mainOutputWriter = new FileWriter(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", true);
		
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
		
		int REPEAT_CONSTANT = rounds;
		
		if ( strategyOverRounds ) repeatAllRounds = REPEAT_CONSTANT;
		
		// Pre-checks for presence of strategy which is defined by a sequence of rounds,
		// not individual ones (e.g. deceptive), and thus must be tested multiple times, as sets of rounds, in addition.
		for ( Hider hider : hiders ) {
			
			// ~MDC Dramatically affects the size of the output files
			if (hider.strategyOverRounds()) repeatAllRounds = REPEAT_CONSTANT; 
			
		}
		
		for ( Seeker  seeker : seekers ) {
			
			if (seeker.strategyOverRounds()) repeatAllRounds = REPEAT_CONSTANT;
			
		}
		
		// Run rounds and record output per hider
		for ( Hider hider : hiders ) {
			
			Utils.talk("Main", hiders.toString());
			
			/* If changes occur over a set of rounds (over a game), by nature of the strategy,
			 * this process must repeat (i.e. to check how a strategy evolves over different 
			 * rounds under a given parameter).
			 * 
			 * ~MDC This should occur for all multiple round games that aren't reset per round
			 */
			for (int roundRepeat = 0; roundRepeat < repeatAllRounds; roundRepeat++) {
				
				hider.startPlaying();
				
				boolean lastRoundRepeat = false;
				
				if (roundRepeat == (repeatAllRounds - 1)) lastRoundRepeat = true;
				
				for (int i = 0; i < rounds; i++) {
		        	
		        	Utils.talk("", "Game " + gameNumber + " Round " + i + ": " + ( ( i / ( ( (float) rounds * hiders.size() ) ) ) * totalGames ) + "%");
		        	
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
							
							for ( Seeker newSeekerObject : initSeekers(seekerList, mixSeekers) ) {
			        			
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
		    		
		    		/*if ( recordPerRound) {
		    			
		    			Utils.talk("Main", hider.toString() + "," + hider.printRoundStats());
		    			
		    			for( Seeker seeker : seekers ) {
		    				
		    				Utils.talk("Main", seeker.toString() + "," + seeker.printRoundStats());
		    				
		    			}
		    			
		    		}*/
		    		
		    		if (generateOutput && recordPerRound) {
		        		
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
				
					if (lastRoundRepeat) if ( generateOutput ) Utils.writeToFile(mainOutputWriter, "G, " + hider.toString() + "," + hider.printGameStats() + ",");
					
			    	// Output costs for Seekers
				
					for ( Seeker seeker : seekers ) {
					
						seeker.endOfGame();
						
						if (lastRoundRepeat) {
						
							Utils.talk("Main", seeker.toString() + "," + seeker.printGameStats());
						
							// Cost per round
						
							if ( generateOutput ) Utils.writeToFile(mainOutputWriter, seeker.toString() + "," + seeker.printGameStats() + ",");
						
						}
						
					}
					
					if (lastRoundRepeat) if ( generateOutput ) Utils.writeToFile(mainOutputWriter, "\n");
				
				//}
				
				graphController.resetGameEnvironment(this);
				
			} // End of hider loop
		
		} // End of repeat loop
		
		try {
			
			if ( generateOutput ) mainOutputWriter.close();
			
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
		
		Logger logger = Logger.getLogger(Main.class.toString());  
	    FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler(Utils.FILEPREFIX + "exceptions.log", true);  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { 
            public void uncaughtException(Thread t, Throwable e) { 
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String stacktrace = sw.toString();
                logger.info(stacktrace); 
            }
        });  
		
		new Main(args);

	}

}
