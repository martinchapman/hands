package Utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Utility.gametheoretic.ApproximatePayoffMatrix;
import Utility.output.Datafile;
import Utility.output.HiderRecord;
import Utility.output.OutputManager;
import Utility.output.TraverserRecord;
import bsh.EvalError;
import bsh.Interpreter;


/**
 * @author Martin
 *
 */
public class Runner extends JFrame {
	
	/**
	 * 
	 */
	public static ArrayList<TraverserRecord> selectedSeekers;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private static String[] hiderTypes = 
		{ 
		
		  "StaticLocations",
		  
		  "Random",
		  "RandomFixedStart",
		  "RandomVariableHidePotential",
		  "RandomStaticBetween",
		  
		  "RandomSet",
		  "GreedyRandomSet",
		  "GreedyRandomSetStaticBetween",
		  "RandomSetStaticBetween",
		  "UniqueRandomSet",
		  "UniqueRandomSetRepeat",
		  "UniqueRandomSetRepeatRandomNodes",
		  "AutomaticUniqueRandomSetRepeat",
		  
		  "FirstK",
		  //"RandomDirection",
		  "FirstKMinus1",
		  "NotConnected",
		  "FirstKFixedStart",
		  "FirstKStaticBetween",
		  
		  "RandomFixedDistance",
		  "RandomFixedDistanceFixedStart",
		  "RandomFixedDistanceStaticBetween",
		  "GreedyRandomFixedDistance",
		  "GreedyRandomFixedDistanceStaticBetween",
		  
		  
		  "VariableFixedDistance",
		  "VariableFixedDistanceFixedStart",
		  "GreedyVariableFixedDistance",
		  "VariableFixedDistanceStaticBetween",
		  
		  "LeastConnected",
		  "LeastConnectedLeastConnectedFirst",
		  "LeastConnectedStaticBetween",
		  "VariableGraphKnowledgeLeastConnected",
		  "VariableGraphKnowledgeLeastConnectedDFS",
		  
		  "MaxDistance",
		  "MaxDistanceStaticBetween",
		  "VariableGraphKnowledgeMaxDistance",
		  
		  "Greedy",
		  "GreedyStaticBetween",
		  "EqualEdgeCost",
		  "FixedStartEqualEdgeCost",
		  "VariableGreedy",
		  "FixedStartVariableGreedy",
		  
		  "FullyBias",
		  "FullyBiasStaticBetween",
		  "FullyExplorative",
		  "LooselyBias",
		  "VariableBias",
		  "FixedStartVariableBias",
		  "FixedStartFullyBias",
		  "FixedStartFullyExplorative",
		  
		  "VariableBiasLocations",
		  
		  ////
		  
		  "DeceptiveNew",
		  "VariableDeceptiveNew",
		  "SetDeceptiveNodes",
		  "VariableDeceptiveNodes",
		  
		  "SetDeceptionDuration",
		  "VariableDeceptionDuration",
		  
		  "SetDeceptionDurationVariableDeceptiveNodes",
		  "VariableDeceptionDurationVariableDeceptiveNodes",
		  
		  //
		  
		  "SetDeceptionDurationSetDeceptionIntervalSetRepeatDuration",
		  "SetDeceptionDurationVariableDeceptionIntervalSetRepeatDuration",
		  "VariableDeceptionDurationSetDeceptionIntervalSetRepeatDuration",
		  "VariableDeceptionDurationVariableDeceptionIntervalSetRepeatDuration",
		  
		  "VariableDeceptionDurationVariableDeceptionIntervalSetRepeatDurationRefreshDeceptiveNodes",
		  
		  
		  "SetDeceptionDurationSetDeceptionIntervalVariableRepeatDuration",
		  //"VariableDeceptionDurationVariableDeceptionIntervalVariableRepeatDuration",
		  "VariableDeceptiveNodesSetDeceptionDurationSetDeceptionIntervalSetRepeatDuration",
		  
		  "GroupedDeceptiveSetDuration",
		  "GroupedDeceptive",
		  "GroupedDeceptiveVariableDeceptionDuration",
		  
		  "VariableDeceptiveSets",
		  "VariableGroupedDeceptiveSets",
		  "EpsilonDeceptive",
		  "LeastConnectedDeceptive",
		  
		  //
		  
		  "UnknownRandom",
		  "AdaptiveRandomSet",
		  "AdaptiveLeastConnected"
		  
		};
	
	/**
	 * 
	 */
	private static String[] seekerTypes = 
		{ 
		
		  "RandomWalk",
		  "SelfAvoidingRandomWalk",
		  "SelfAvoidingRandomWalkGreedy",
		  "RepeatGreedy",
		  "Greedy",
		  "DepthFirstSearch",
          "DepthFirstSearchGreedy",
	      "BreadthFirstSearch",
	      "BreadthFirstSearchGreedy",
	      /*"BacktrackPath",
	      "VariableBacktrackPath",
	      "OptimalBacktrackPath",*/
	      "BacktrackGreedy",
	      "NearestNeighbour",
	      "RandomTarry",
	      
	      "LeastConnectedFirst",
	      "VariableLeastConnectedFirst",
	      "MostConnectedFirst",
	      "ApproximateLeastConnectedNodes",
	      "MaxDistanceFirst",
	      
	      "LinkedPath",
	      
          "HighProbability",
          "VariableNodesHighProbability",
          "VariableHistoryHighProbability",
          "HighProbabilityRepetitionCheck",
          
          "InverseHighProbability",
          
          // 
          
          "AdaptiveHighProbability"
          
		};
	
	private static String[] graphTypes = 
		{
		
		  "random",
		  "ring",
		  "scalefree",
		  "complete"
		  
		};
	
	// 
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) {
		
		new Runner();

	}
	
	private JComboBox<String> topologies;
	
	private JTextField numberOfNodes;
	
	private JTextField numberOfHiddenItems;
	
	private JTextField costOfEdgeTraversal;
	
	private JComboBox<String> fixedOrRandom;
	
	private JTextField edgeTraversalDecrement;
	
	private JTextField numberOfRounds;
	
	private JTextField numberOfGames;
	
	private DefaultListModel<String> simulationHidersModel;
	
	private DefaultListModel<String> simulationSeekersModel;
	
	private DefaultListModel<String> queueListModel;
	
	private JButton start;
	
	private JButton queue;
	
	private JButton startQueue;
	
	private JButton startSelected;
	
	private JList<String> queueList;

	private JCheckBox mixSeekers;

	private JCheckBox mixHiders;
	
	private JCheckBox resetPerRound;
	
	/**
	 * Helper interface for specifying different actions
	 * to take place once something has been deleted
	 * from a JList. 
	 * 
	 * @author Martin
	 *
	 */
	public interface PostDelete 
    {
		
		public void postDelete(Object object);
		
    }

	/**
	 * @param list
	 * @param model
	 * @param deleted
	 */
	private void deleteOnClick(final JList<?> list, final DefaultListModel<?> model, final PostDelete deleted) {
		
		list.addMouseListener(new MouseAdapter() {
		    
			public void mouseClicked(MouseEvent evt) {
		        
		        if (evt.getClickCount() == 2) {
		       
		        	int dialogResult = JOptionPane.showConfirmDialog (null, "Delete record?", "!", JOptionPane.YES_NO_OPTION);
		        	
		        	if ( dialogResult == JOptionPane.YES_OPTION ) {
		        	  
			        	/* After something has been deleted, pass what has been deleted to the 'post delete' method
			        	 * for processing
			        	 */
			        	deleted.postDelete(model.getElementAt(list.locationToIndex(evt.getPoint())));
			        	
			        	model.remove(list.locationToIndex(evt.getPoint()));
		        	
		        	}
		        	
		        } 
		        
		    }
			
		});
		
	}
	
	/**
	 * 
	 */
	private void generateGUI() {
		
		setPreferredSize(new Dimension(800, 1800));
		
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		add(tabbedPane);
		
		//
		
		setupTab(tabbedPane);
		
		simulationsTab(tabbedPane);
		
		outputTab(tabbedPane);
		
		//
		
		pack();
		
		setVisible(true);
		
	}
	
	/**
	 * @param tabbedPane
	 */
	private void outputTab(JTabbedPane tabbedPane) {
		
		final OutputManager outputManager = new OutputManager();
		
		//////
		
		JPanel outputTab = new JPanel();
		
		outputTab.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Output", outputTab);
		
		////
		
		JPanel northPane = new JPanel();
		
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		outputTab.add(northPane, BorderLayout.NORTH);
		
		//
		
		final JButton collateOutput = new JButton("Process Output");
		
		northPane.add(collateOutput);
		
		final DefaultListModel<HiderRecord> outputFeedback = new DefaultListModel<HiderRecord>();

		final JButton showTextStats = new JButton("Show text stats");
		
		showTextStats.setEnabled(false);
		
		final JComboBox<Datafile> files = new JComboBox<Datafile>();
		
		files.setEnabled(false);
		
		collateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( files.isEnabled() ) {
					
					outputManager.processIndividualOutput(((Datafile)files.getSelectedItem()).getPath());
					
				} else {
					
					outputManager.processAllOutput();
					
				}
				
				outputFeedback.clear();
				
				for (ArrayList<HiderRecord> fileHiderRecord : outputManager.getFileHiderRecords()) {

					Utils.printSystemStats();
					
					for (HiderRecord hiderRecord : fileHiderRecord) {
						
						Utils.printSystemStats();
						
						for ( TraverserRecord hidersSeeker : hiderRecord.getSeekersAndAttributes() ) {
							
							Utils.printSystemStats();
							
							HiderRecord hiderCopy = new HiderRecord("");
							
							hiderCopy.duplicateRecord(hiderRecord);
							
							hiderCopy.clearSeekersAndAttributes();
							
							hiderCopy.addSeeker(hidersSeeker);
							
							hiderCopy.setOpponents(hidersSeeker.getTraverser());
							
							outputFeedback.addElement(hiderCopy);
							
						}
						
					}
					
					// Mark end of Hiders in that game
					
					outputFeedback.addElement(new HiderRecord("") {
						
						public String toString() {
							
							return "-----";
							
						}
						
					});
					
				}
				
				showTextStats.setEnabled(true);
				
			}
			
		});
		
		//
		
		JButton deleteOutputFiles = new JButton("Delete output files");
		
		deleteOutputFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				outputManager.removeAllOutputFiles();
				
				outputFeedback.clear();
				
			}
			
		});
		
		northPane.add(deleteOutputFiles);
		
		//
		
		showTextStats.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(outputManager.printAllStats());
				
			}
			
		});
		
		northPane.add(showTextStats);
		
		// 
		
		final JButton showFiles = new JButton("Show files");
		
		showFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( showFiles.getText().equals("Show files") ) {
			
					showFiles.setText("Hide files");
					
					for ( Datafile path : outputManager.availableFiles() ) {
						
						files.addItem(path);
						
					}
					
					files.setPreferredSize(new Dimension(150, 50));
					
					files.setEnabled(true);
				
				} else if ( showFiles.getText().equals("Hide files") ) {
					
					showFiles.setText("Show files");
					
					files.setEnabled(false);
					
					files.removeAllItems();
					
				}
				
			}
		
		});
		
		northPane.add(showFiles);
		
		//
		
		/*files.addMouseListener(new MouseAdapter() {
		    
			public void mouseClicked(MouseEvent evt) {
		        
		        if (evt.getClickCount() == 2) {
		       
		        	outputManager.processOutput((Path)files.getSelectedItem());
		        	
		        } 
		        
		    }
			
		});*/
		
		northPane.add(files);
		
		////
		
		JPanel centerPane = new JPanel();
		
		centerPane.setLayout(new BorderLayout());
		
		outputTab.add(centerPane, BorderLayout.CENTER);
		
		//
		
		final JList<HiderRecord> outputFeedbackList = new JList<HiderRecord>(outputFeedback);
		
		JScrollPane outputFeedbackListScroll = new JScrollPane(outputFeedbackList);
		
		final JComboBox<String> measure = new JComboBox<String>();
		
		final JLabel simulationParameters = new JLabel();
		
		final JRadioButton seekers = new JRadioButton("Seekers");
		
		outputFeedbackList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (e.getValueIsAdjusting()) return;
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				measure.removeAllItems();
				
				measure.addItem("Payoff");
				
				if ( seekers.isSelected() ) {
				
					for ( String attribute : outputFeedbackList.getSelectedValue().getSeekerAttributes() ) {
						
						measure.addItem(attribute);
						
					}
				
				} else {
					
					for ( String attribute : outputFeedbackList.getSelectedValue().getAttributes() ) {
						
						measure.addItem(attribute);
						
					}
					
				}
				
				simulationParameters.setText("<html><body style='width: 100px;'>Simulation parameters: " + outputFeedbackList.getSelectedValue().getParameters() + "</body></html>");
				
			}
			
		});
		
		deleteOnClick(outputFeedbackList, outputFeedback, new PostDelete() {

			@Override
			public void postDelete(Object deleted) {
				
				if (!outputFeedbackList.getSelectedValue().toString().equals("-----")) {
					
					outputManager.deleteFile(((HiderRecord)deleted).getFileRelatingTo());
					
					outputManager.removeOrphaned();
					
				}
				
			}
			
		});
		
		centerPane.add(simulationParameters, BorderLayout.NORTH);
		
		centerPane.add(outputFeedbackListScroll, BorderLayout.CENTER);
		
		////
		
		JPanel centerPaneRight = new JPanel();
		
		centerPaneRight.setLayout(new BorderLayout());
		
		centerPane.add(centerPaneRight, BorderLayout.EAST);
		
		//
		
		JPanel centerPaneRightCenter = new JPanel(new GridLayout(12, 4));
		
		centerPaneRight.add(centerPaneRightCenter, BorderLayout.CENTER);
		
		//
		
		centerPaneRightCenter.add(new JLabel("Data for:"));
		
		centerPaneRightCenter.add(seekers);
		
		seekers.setSelected(true);
		
		final JRadioButton hiders = new JRadioButton("Hiders");
		
		centerPaneRightCenter.add(hiders);
		
		ButtonGroup hidersSeekers = new ButtonGroup();
		
		hidersSeekers.add(hiders);
		
		hidersSeekers.add(seekers);
	    
		//
		
		seekers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				measure.removeAllItems();
				
				measure.addItem("Payoff");
				
				for ( String attribute : outputFeedbackList.getSelectedValue().getSeekerAttributes() ) {
					
					measure.addItem(attribute);
					
				}
				
			}
			
		});
		
		hiders.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				measure.removeAllItems();
				
				measure.addItem("Payoff");
				
				for ( String attribute : outputFeedbackList.getSelectedValue().getAttributes() ) {
					
					measure.addItem(attribute);
					
				}
				
			}
			
		});
		
		//
		
		centerPaneRightCenter.add(new JLabel("Selected measure:"));
		
		centerPaneRightCenter.add(measure);
		
		//
		
		final JComboBox<String> graphTypes = new JComboBox<String>();

		graphTypes.addItem("Bar");
		
		graphTypes.addItem("Line");
		
		graphTypes.addItem("3D");
		
		graphTypes.addItem("Payoff Matrix");
		
		centerPaneRightCenter.add(new JLabel("Graph types:"));
		
		centerPaneRightCenter.add(graphTypes);
		
		//
		
		final JComboBox<String> categories = new JComboBox<String>();
		
		categories.addItem("Opponent");
		
		categories.addItem("Topology");
		
		centerPaneRightCenter.add(new JLabel("Bar graph categories:"));
		
		centerPaneRightCenter.add(categories);
		
		//
		
		final JComboBox<String> gameOrRound = new JComboBox<String>();
		
		gameOrRound.addItem("Game");
		
		gameOrRound.addItem("Round");
		
		centerPaneRightCenter.add(new JLabel("Average over Game or Round:"));
		
		centerPaneRightCenter.add(gameOrRound);
		
		//
		
		final JCheckBox outputEnabled = new JCheckBox("Output enabled");
		
		centerPaneRightCenter.add(outputEnabled);
		
		//
		
		JButton generateGraph = new JButton("Generate graph");
		
		generateGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				// Allows for selection of multiple hiders or multiple seekers from list
				ArrayList<TraverserRecord> selectedHiders = new ArrayList<TraverserRecord>();
				
				selectedSeekers = new ArrayList<TraverserRecord>();
				
				String title = "";
				
				for ( HiderRecord hider : outputFeedbackList.getSelectedValuesList()) {
					
					if (hider.toString().equals("-----")) continue;
					
					title += "Topology: " + hider.getTopology() + " Hider: " + hider;
					
					for ( TraverserRecord hidersSeeker : hider.getSeekersAndAttributes() ) {
						
						//if ( hidersSeekers.toString().contains("(")) hidersSeekers.setTraverser(hidersSeekers.toString().substring(0, hidersSeekers.toString().indexOf(" ")));
						
						hidersSeeker.setTraverser(hidersSeeker.getTraverser());
						
						hidersSeeker.setOpponents(hider.getTraverser());
						
						selectedHiders.add(hider);
						
						//if (selectedSeekers.contains(hidersSeekers)) {
							
							//selectedSeekers.get(selectedSeekers.indexOf(hidersSeekers)).integrateRecord(hidersSeekers);
							
						//} else {
							
						selectedSeekers.add(hidersSeeker);
							
						//}
						
					}
					
				}
				
				ArrayList<TraverserRecord> allPlayers = new ArrayList<TraverserRecord>(selectedHiders);
				allPlayers.addAll(selectedSeekers);
				
				if (seekers.isSelected()) {
					
					if (graphTypes.getSelectedItem().equals("Line")) {
						
						outputManager.showLineGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected());
				
					} else if (graphTypes.getSelectedItem().equals("Bar")) {
						
						outputManager.showBarGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
						
					} else if (graphTypes.getSelectedItem().equals("3D")) {
						
						outputManager.show3DGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
						
					}
					
				} else if (hiders.isSelected()) {
					
					if (graphTypes.getSelectedItem().equals("Line")) {
						
						outputManager.showLineGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected());
						
					} else if (graphTypes.getSelectedItem().equals("Bar")) {
						
						outputManager.showBarGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
						
					} else if (graphTypes.getSelectedItem().equals("3D")) {
						
						outputManager.show3DGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
						
					}
					
				}
				
				if (graphTypes.getSelectedItem().equals("Payoff Matrix")) {
					
					ApproximatePayoffMatrix HPM = new ApproximatePayoffMatrix("");
					
					for ( Entry<TraverserRecord, Double> seekerPayoff : outputManager.matrixPayoff(selectedSeekers, allPlayers).entrySet() ) {
						
						HPM.addPayoff("Seeker", seekerPayoff.getKey().toString().split(" vs ")[0], seekerPayoff.getKey().toString().split(" vs ")[1], seekerPayoff.getValue());
						
					}
					
					for ( Entry<TraverserRecord, Double> hiderPayoff : outputManager.matrixPayoff(selectedHiders, allPlayers).entrySet() ) {
						
						HPM.addPayoff("Hider", hiderPayoff.getKey().toString().split(" vs ")[0], hiderPayoff.getKey().toString().split(" vs ")[1], hiderPayoff.getValue());
						
						
					}
					
					System.out.println(HPM);
					
					for ( String line : HPM.GTAnalysis() ) { 
						
						System.out.println(line);
					
					}
					
					if ( outputEnabled.isSelected() ) {
						
						HPM.printTikzMatrixToFile();
						
					}
				
				}
				
				showFiles.doClick();
				
			}
		
		});
		
		centerPaneRight.add(generateGraph, BorderLayout.SOUTH);
		
	}
	
	private void setupTab(JTabbedPane tabbedPane) {
		
		//////
		
		JPanel setupTab = new JPanel();
		
		setupTab.setLayout(new GridLayout(2, 2));
		
		tabbedPane.addTab("Setup", setupTab);
		
		////
		
		JPanel parameters = new JPanel();
		
		parameters.setLayout(new GridLayout(6, 2));
		
		parameters.setBorder(new TitledBorder("Parameters"));
		
		//
		
		parameters.add(new JLabel("Topology:"));
		
		topologies = new JComboBox<String>();
		
		for ( String topology : graphTypes ) {
			
			topologies.addItem(topology);
			
		}
		
		parameters.add(topologies);
		
		//
		
		parameters.add(new JLabel("Number of nodes:"));
		
		numberOfNodes = new JTextField("100");
		
		parameters.add(numberOfNodes);
		
		//
		
		parameters.add(new JLabel("Number of hide locations:"));
		
		numberOfHiddenItems = new JTextField("5");
		
		parameters.add(numberOfHiddenItems);
		
		// 
		
		parameters.add(new JLabel("Cost of traversing an edge:"));
		
		costOfEdgeTraversal = new JTextField("10.0");
		
		parameters.add(costOfEdgeTraversal);
		
		//
		
		parameters.add(new JLabel("Fixed or random cost:"));
		
		fixedOrRandom = new JComboBox<String>();
		
		fixedOrRandom.addItem("random");
		fixedOrRandom.addItem("fixed");
		
		parameters.add(fixedOrRandom);
		
		//
		
		parameters.add(new JLabel("Edge traversal decrement:"));
		
		edgeTraversalDecrement = new JTextField("0");
		
		parameters.add(edgeTraversalDecrement);
		
		//
		
		setupTab.add(parameters);
		
		////
		
		JPanel hiders = new JPanel();
		
		hiders.setLayout(new GridLayout(3, 1));
		
		hiders.setBorder(new TitledBorder("Hiders"));
		
		//
		
		hiders.add(new JLabel("Hider types:"));
		
		//
		
		JPanel hiderListAndButton = new JPanel();
		
		hiderListAndButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JComboBox<String> hiderList = new JComboBox<String>();
		
		ComboboxToolTipRenderer hiderListRenderer = new ComboboxToolTipRenderer();
		
		hiderList.setRenderer(hiderListRenderer);
		
		ArrayList<String> hiderTooltips = new ArrayList<String>();
		
		for ( String hiderType : hiderTypes ) {
			
			hiderList.addItem(hiderType);
			
			hiderTooltips.add(" ");
			
		}
		
		hiderListRenderer.setTooltips(hiderTooltips);
		
		hiderListAndButton.add(hiderList);
		
		//
		
		mixHiders = new JCheckBox("Mix hiders");
		
		hiderListAndButton.add(mixHiders);
		
		//
		
		JButton addHider = new JButton("Add hider");
		
		hiderListAndButton.add(addHider);
		
		hiders.add(hiderListAndButton);
		
		//

		simulationHidersModel = new DefaultListModel<String>();
		
		simulationHidersModel.addElement("RandomSet");
		
		JList<String> simulationHiders = new JList<String>(simulationHidersModel);
		
		JScrollPane simulationHidersPane = new JScrollPane(simulationHiders);
		
		simulationHidersPane.setPreferredSize(new Dimension(100, 100));
		
		deleteOnClick(simulationHiders, simulationHidersModel, new PostDelete() {

			@Override
			public void postDelete(Object deleted) {
				
				if (simulationHidersModel.toArray().length == 1) { 
					
					start.setEnabled(false);
					
					queue.setEnabled(false);
					
				}
				
			}
			
		});
		
		hiders.add(simulationHidersPane);
		
		addHider.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationHidersModel.contains(hiderList.getSelectedItem().toString())) simulationHidersModel.addElement(hiderList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
				queue.setEnabled(true);
				
			}
			
		});
		
		//
		
		setupTab.add(hiders);
		
		////
		
		JPanel simulationParameters = new JPanel();
		
		simulationParameters.setLayout(new GridLayout(3, 2));
		
		simulationParameters.setBorder(new TitledBorder("Simulation Parameters"));
		
		//
		
		simulationParameters.add(new JLabel("Number of rounds:"));
		
		numberOfRounds = new JTextField("1");
		
		numberOfRounds.setPreferredSize(new Dimension(100, 10));
		
		simulationParameters.add(numberOfRounds);
		
		//
		
		// Blank space in grid layout
		simulationParameters.add(new JPanel());
		
		resetPerRound = new JCheckBox("Reset per round");
		
		simulationParameters.add(resetPerRound);
		
		
		//
		
		simulationParameters.add(new JLabel("Number of games:"));
		
		numberOfGames = new JTextField("1");
		
		simulationParameters.add(numberOfGames);
		
		//
		
		setupTab.add(simulationParameters);
		
		////
		
		JPanel seekers = new JPanel();
		
		seekers.setLayout(new GridLayout(3, 1));
		
		seekers.setBorder(new TitledBorder("Seekers"));
		
		//
		
		seekers.add(new JLabel("Seeker types:"));
		
		//
		
		JPanel seekerListAndButton = new JPanel();
		
		seekerListAndButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JComboBox<String> seekerList = new JComboBox<String>();
		
		ComboboxToolTipRenderer seekerListRenderer = new ComboboxToolTipRenderer();
		
		seekerList.setRenderer(seekerListRenderer);
		
		ArrayList<String> seekerTooltips = new ArrayList<String>();
		
		for ( String seekerType : seekerTypes ) {
			
			seekerList.addItem(seekerType);
			
			seekerTooltips.add(" ");
			
		}
		
		seekerListRenderer.setTooltips(seekerTooltips);
		
		seekerListAndButton.add(seekerList);
		
		//
		
		mixSeekers = new JCheckBox("Mix seekers");
		
		seekerListAndButton.add(mixSeekers);
		
		//
		
		JButton addSeeker = new JButton("Add seeker");
		
		seekerListAndButton.add(addSeeker);
		
		seekers.add(seekerListAndButton);
		
		//

		simulationSeekersModel = new DefaultListModel<String>();
		
		simulationSeekersModel.addElement("ConstrainedRandomWalk");
		
		JList<String> simulationSeekers = new JList<String>(simulationSeekersModel);
		
		JScrollPane simulationSeekersPane = new JScrollPane(simulationSeekers);
		
		simulationSeekersPane.setPreferredSize(new Dimension(100, 100));
		
		deleteOnClick(simulationSeekers, simulationSeekersModel, new PostDelete() {

			@Override
			public void postDelete(Object deleted) {
				
				if (simulationSeekersModel.toArray().length == 1) { 
					
					start.setEnabled(false);
					
					queue.setEnabled(false);
					
				}
				
			}
			
		});
		
		seekers.add(simulationSeekersPane);
		
		addSeeker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationSeekersModel.contains(seekerList.getSelectedItem().toString())) simulationSeekersModel.addElement(seekerList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
				queue.setEnabled(true);
				
			}
			
		});
		
		//
		
		setupTab.add(seekers);
		
		////////
		
	}
	
	private void simulationsTab(JTabbedPane tabbedPane) {
		
		//////
		
		JPanel simulationsTab = new JPanel();
		
		simulationsTab.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Simulations", simulationsTab);
		
		////
			
		queueListModel = new DefaultListModel<String>();
		
		queueList = new JList<String>(queueListModel);
		
		JScrollPane queueListScroll = new JScrollPane(queueList);
		
		queueListScroll.setPreferredSize(new Dimension(800, 200));
		
		queueList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
			
				updateUISettings(queueList.getSelectedValue());
				
				startSelected.setEnabled(true);
				
			}
			
		});
		
		deleteOnClick(queueList, queueListModel, new PostDelete() {

			@Override
			public void postDelete(Object deleted) {
				
				if (queueListModel.toArray().length == 1) { 
					
					startQueue.setEnabled(false);
					
					startSelected.setEnabled(false);
					
				}
				
				simulations.remove(deleted);
				
				// Empty the file
				FileWriter writer;
				
				try {
					
					writer = new FileWriter(Utils.FILEPREFIX + "simulationSchedule.txt");
					
					writer.write("");
					
					// ~MDC Note: this removing process may lead to duplicate errors i.e. may not be order proof
					for (String simulation : simulations) { 
						
						writer.append(simulation + "\n");
						
					}
					
					writer.close();
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		});
		
		simulationsTab.add(queueListScroll, BorderLayout.CENTER);
		
		////////
		
		JPanel controls = new JPanel();
		
		controls.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton resetUI = new JButton("Reset");
		
		resetUI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				topologies.setSelectedIndex(0);
				
				numberOfNodes.setText("100");
				
				numberOfHiddenItems.setText("5");
				
				costOfEdgeTraversal.setText("10.0");
				
				fixedOrRandom.setSelectedIndex(0);
				
				edgeTraversalDecrement.setText("0.0");
				
				numberOfRounds.setText("1");
				
				numberOfGames.setText("1");
				
				simulationHidersModel.clear();
				
				simulationHidersModel.addElement("Random");
				
				simulationSeekersModel.clear();
				
				simulationSeekersModel.addElement("Random Walk");
				
			}
			
		});
		
		controls.add(resetUI);
		
		startSelected = new JButton("Start selected");
		
		startSelected.setEnabled(false);
		
		controls.add(startSelected);
		
		start = new JButton("Start setup");
		
		controls.add(start);
		
		queue = new JButton("Queue simulation");
		
		controls.add(queue);
		
		startQueue = new JButton("Start queue");
		
		startQueue.setEnabled(false);
		
		controls.add(startQueue);
		
		//
		
		simulationsTab.add(controls, BorderLayout.SOUTH);
		
		////

	}
	
	private ArrayList<String> simulations;
	
	/**
	 * 
	 */
	public Runner() {
		
		super("HANDS");
		
		generateGUI();
		
		// Collect list of simulations
        simulations = Utils.readFromFile(Utils.FILEPREFIX + "simulationSchedule.txt");
        
        for (String simulation : simulations) { 
        	
        	queueListModel.addElement(simulation);
        	
        	startQueue.setEnabled(true);
        
        }
        
        //
        
        start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(Arrays.toString(getUISettings()));
				
				runSimulation(Integer.parseInt(numberOfGames.getText()), getUISettings());
				
			}
        	
        });
        
        //
        
        startSelected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (String simulationFromList : queueList.getSelectedValuesList() ) {
				
					String[] simulation = simulationFromList.split(",\\s");
					
		        	runSimulation(Integer.parseInt(simulation[0]), simulation);
	        	
				}
				
			}
        	
        });
        
        //
        
        queue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!queueListModel.contains(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1))) {
				
					queueListModel.addElement(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1));
				
					try {
						
						Utils.writeToFile(new FileWriter(Utils.FILEPREFIX + "simulationSchedule.txt", true), Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1) + "\n");
						
						simulations = Utils.readFromFile(Utils.FILEPREFIX + "simulationSchedule.txt");
						
					} catch (IOException e1) {
					
						e1.printStackTrace();
					
					}
					
					startQueue.setEnabled(true);
					
				}
				
			}
        	
        });
        
        //
        
        startQueue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				runSimulationList(simulations);
				
			}
        	
        });
       
	}
	
	private void updateUISettings(String settings) {
		
		if (settings == null) return;
		
		String[] simulationParameters = settings.split(",\\s");
		
		int arrayPosition = 0;
		
		simulationHidersModel.clear();
		
		simulationSeekersModel.clear();
		
		for (String param : simulationParameters) {
      	  
			if (param.indexOf('{') != -1) {
				
				Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z.+\\*]+)\\})").get(0);
  
				if (paramPair.getElement0().equals("Topology")) {
		  
					topologies.setSelectedItem(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("NumberOfNodes")) { 
		  
					numberOfNodes.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("NumberOfHideLocations")) { 
		  
					numberOfHiddenItems.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("Rounds")) { 
		  
					numberOfRounds.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("EdgeWeight")) { 
		  
					costOfEdgeTraversal.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("FixedOrUpperWeight")) { 
		  
					fixedOrRandom.setSelectedItem(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("EdgeTraversalDecrement")) { 
			  
					edgeTraversalDecrement.setText(paramPair.getElement1());
			  
				} else if (paramPair.getElement0().equals("MixHiders")) {
					
					if ( Boolean.parseBoolean(paramPair.getElement1()) ) { 
						
						mixHiders.setSelected(true); 
						
					} else { 
						
						mixHiders.setSelected(false); 
					
					} 
					
				} else if (paramPair.getElement0().equals("MixSeekers")) {
					
					if ( Boolean.parseBoolean(paramPair.getElement1()) ) { 
						
						mixSeekers.setSelected(true); 
						
					} else { 
						
						mixSeekers.setSelected(false); 
					
					} 
					
				} else if (paramPair.getElement0().equals("ResetPerRound")) {
					
					if ( Boolean.parseBoolean(paramPair.getElement1()) ) { 
						
						resetPerRound.setSelected(true); 
						
					} else { 
						
						resetPerRound.setSelected(false); 
					
					} 
					
				}
	  
			} else if (param.indexOf('[') != -1) {
  
				ArrayList<Pair<String, String>> types = Utils.stringToArray(param, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])");
  
				for (Pair<String, String> traverser : types) {
	  
					// Is Hider 
					if (arrayPosition == 1)  {
	  
						simulationHidersModel.addElement(traverser.getElement0());
	  
					// Is Seeker  
					} else {
			  
						simulationSeekersModel.addElement(traverser.getElement0());
			  
					}
		  
				}
	  
			} else {
	  
				numberOfGames.setText(param);
	  
			}
  
			arrayPosition++;
			  
		}
		
	}
	
	private String[] getUISettings() {
		
		String hiderParameter = "";
		
		for (Object hider : simulationHidersModel.toArray()) {
			
			hiderParameter += "[" + hider + ",1]";
			
		}
		
		String seekerParameter = "";
		
		for (Object seeker : simulationSeekersModel.toArray()) {
			
			seekerParameter += "[" + seeker + ",1]";
			
		}
		
		return new String[]{ 
					 
			numberOfGames.getText(),
			
			hiderParameter,
			
			seekerParameter,
			
			"{Topology," + topologies.getSelectedItem().toString() + "}", // Topology
			
			"{NumberOfNodes," + numberOfNodes.getText() + "}", // Number of nodes in graph
			
			"{NumberOfHideLocations," + numberOfHiddenItems.getText() + "}", // Number of hide locations
			
			"{Rounds," + numberOfRounds.getText() + "}", // rounds
			
			"{EdgeWeight," + costOfEdgeTraversal.getText() + "}", // cost of traversing an edge
			
			"{FixedOrUpperWeight," + fixedOrRandom.getSelectedItem().toString() + "}", // whether cost supplied is static value or the upper bound of a distribution
			
			"{EdgeTraversalDecrement," + edgeTraversalDecrement.getText() + "}", // % discount gained by an agent for having traversed an edge before (100 = no discount; < 100 = discount)
		  	
			"{MixHiders," + mixHiders.isSelected() + "}",
			
			"{MixSeekers," + mixSeekers.isSelected() + "}",
			
			"{ResetPerRound," + resetPerRound.isSelected() + "}"
			
		};  
		
	}
	
	/**
	 * 
	 */
	private void runSimulationList(ArrayList<String> simulations) {
		
		for (String currentSimulation : simulations) {
			
			String[] simulationParameters = currentSimulation.split(",\\s");
			
			// If the simulation is commented out, do not run it.
			if(		simulationParameters[0].equals("//")	) { continue; }
			
			runSimulation(Integer.parseInt(simulationParameters[0]), simulationParameters);
			
		}
		
	}
	
	/**
	 * ~MDC 8/8 Messy and inefficient
	 * 
	 * @param GAMES
	 * @param simulationParameters
	 */
	private void runSimulation(int GAMES, String[] simulationParameters) {
		
		for ( int paramID = 0; paramID < simulationParameters.length; paramID++ ) {
			
			if ( simulationParameters[paramID].contains("j") ) {
				
				String jExpression = simulationParameters[paramID];
				
				for ( int j = 0; j < GAMES; j++ ) {
					
					String parsed = parseExpression(jExpression.replace("j", j + "").substring(jExpression.replace("j", j + "").indexOf(",") + 1, jExpression.replace("j", j + "").length() - 1));
					
					if ( parsed.equals("-1") ) throw new UnsupportedOperationException("Failed to parse J expression");
					
					simulationParameters[paramID] = jExpression.substring(0, jExpression.indexOf(",") + 1) + parsed + "}";
	    	    	 
					runSimulationX(GAMES, simulationParameters);
					
				}
				
				return;
				
			}
			
		}
		
		runSimulationX(GAMES, simulationParameters);
		
	}
	
	/**
	 * @param expression
	 * @return
	 */
	private String parseExpression(String expression) {
		
		Interpreter interpreter = new Interpreter();
    	
    	try {
    		
    		interpreter.eval("result = " +  expression);
    		
    		return "" + interpreter.get("result");
    		
		} catch (EvalError e) {
			
			e.printStackTrace();
			
			return "-1";
			
		}
	
	}
	
	private void runSimulationX(int GAMES, String[] simulationParameters) {
		
		/***********/
		
		// Generate ID For this simulation
		
		String currentSimulationIdentifier = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		Utils.writeToFile(Utils.FILEPREFIX + "simRecordID.txt", currentSimulationIdentifier);
		
		Utils.writeToFile(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", "");
		
		Utils.writeToFile(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", Arrays.toString(simulationParameters) + "\n");
		
		/***********/
		
		String[] parameters = { "Hiders", 
				  "Seekers",
  				  "Topology", // Topology
  				  "NumberOfNodes", // Number of nodes in graph
  				  "NumberOfHideLocations", // Number of hide locations
  				  "Rounds", // rounds 
  				  "EdgeWeight", // cost of traversing an edge
  				  "FixedOrUpperWeight", // whether cost supplied is static value or the upper bound of a distribution
  				  "EdgeTraversalDecrement", // % discount gained by an agent for having traversed an edge before (100 = no discount; < 100 = discount)
  				  "MixHiders", // Mix equally between the hide strategies
  				  "MixSeekers", // Mix equally between the search strategies
  				  "ResetPerRound" // Whether players knowledge should persist through rounds
				  };
  				  
		String[] defaultParameters = { simulationParameters[1],
				 simulationParameters[2],
				 "random", // Topology
				 "100", // Number of nodes in graph
				 "5", // Number of hide locations
				 "120", // rounds
				 "100.0", // cost of traversing an edge
				 "upper", // whether cost supplied is static value or the upper bound of a distribution
				 "0",// % discount gained by an agent for having traversed an edge before (1.0 = no discount; < 1.0 = discount),
				 "false", // Mix equally between the hide strategies
				 "false", // Mix equally between the search strategies
				 "true" // Whether players knowledge should persist through rounds
		  		  };
			
		/***********/
		
		/* As we are replacing variables in our simulation parameters with concrete values, 
		 * we must retain the original variables for future games, or they will be lost
		 * i.e. if we receive i+1 as a parameter, this will need to be changed to '1' for the
		 * first game, but a copy of i+1 will need to be retained for the second, third games etc.
		 */
		String[] simulationParametersUnchanged = Arrays.copyOf(simulationParameters, simulationParameters.length);
		
		// Run 'games' of simulation by repeat running program
		
		String startTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		System.out.println("-----------------------------------------------------------------");
		
	    for(int i = 0; i < GAMES; i++) {
		     	
			System.out.println("Run: " + (i + 1));
			System.out.println("-----------------------------------------------------------------");
			
			System.out.println( ( ( i / ((float)GAMES) ) * 100 ) + "%" );
			
			// Remove wildcards from param string and replace with values
			
			for (int j = 0; j < simulationParameters.length; j++) {
	    		  
	    	    if (simulationParametersUnchanged[j].contains("*") || simulationParametersUnchanged[j].contains("+")) { 
	    	    	
	    	    	String parsed = parseExpression(simulationParametersUnchanged[j].replace("i", i + "").substring(simulationParametersUnchanged[j].replace("i", i + "").indexOf(",") + 1, simulationParametersUnchanged[j].replace("i", i + "").length() - 1));
	    	    	
	    	    	if ( parsed.equals("-1") ) throw new UnsupportedOperationException("Failed to parse I expression");
	    	    	
					simulationParameters[j] = "" + simulationParametersUnchanged[j].substring(0, simulationParametersUnchanged[j].indexOf(",") + 1) + parsed + "}";
					
	    	    		
	    	     /*
	    	         simulationParametersUnchanged[j].replaceAll("(i\\*([0-9]+))", 
	    											   "" + (i * Integer.parseInt(
	    													     simulationParametersUnchanged[j].substring( 
														            		   Utils.startIndexOf(simulationParametersUnchanged[j], "(i\\*([0-9]+))") + 2, 
														            		   Utils.endIndexOf(simulationParametersUnchanged[j], "(i\\*([0-9]+))")
														            		   )
	    													         	  
	    													      		)
	    													)
	    	    									   );
	    	    
	    	    } else if (simulationParametersUnchanged[j].contains("i+")) { 
	    	    	
	    	    	simulationParameters[j] = 
	    	    	
	    	        simulationParametersUnchanged[j].replaceAll("(i\\+([0-9]+))", 
	    	        								   "" + (i + Integer.parseInt(
	    	        										     simulationParametersUnchanged[j].substring(
	    	        										   					Utils.startIndexOf(simulationParametersUnchanged[j], "(i\\+([0-9]+))") + 2, 
	    	        										   					Utils.endIndexOf(simulationParametersUnchanged[j], "(i\\+([0-9]+))")
	    	        										   			)
	    	        										   	  )
	    	        										)
	    	        								   );
	    	    
	    	    */
	    	    
	    	    } else if (simulationParametersUnchanged[j].contains(",i")) { 
	    	  		
	    	  		simulationParameters[j] = 
	    	  				
	    	  		simulationParametersUnchanged[j].replaceAll("(\\,i)", "," + i); 
	    	  		
	    	  	}
    	   
    	    }
		    
			// Alter the default parameters to reflect those that have been input
			
            for (String param : simulationParameters) {
        	  
				  if (param.indexOf('{') != -1) {
				  
					  Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z.]+)\\})").get(0);
					  
					  defaultParameters[Arrays.asList(parameters).indexOf(paramPair.getElement0())] = paramPair.getElement1(); 
					  
				  }
        	  
            }
		    
            // Construct the param string to supply to the program
            
			String paramString = "";
			  
			for (int k = 0; k < defaultParameters.length; k++) {
				  
				paramString += defaultParameters[k] + " ";
				  
			}
			
			System.out.println(paramString);
		    
			/***********/
			
			Utils.runCommand("java -classpath bin:lib/jgrapht-core-0.9.0.jar:lib/epsgraphics.jar:lib/jcommon-1.0.21.jar:lib/jfreechart-1.0.17.jar HideAndSeek.Main " + i + " " + GAMES + " " + paramString);
			 
			System.out.println("-----------------------------------------------------------------");
			
			if ( i < GAMES - 1); {
			
				Utils.runCommand("clear");
			
				Utils.runCommand("printf '\\\\e[3J'");
				
				Utils.runCommand("printf '\\\\ec'");
			
			}
			
	    } // End of game run loop
		
	    System.out.println("Started: " + startTime + " and ended: " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
	    
	}
	
	/**
	 * From: http://stackoverflow.com/questions/480261/java-swing-mouseover-text-on-jcombobox-items
	 * 
	 * @author Martin
	 */
	private class ComboboxToolTipRenderer extends DefaultListCellRenderer {
	    
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 
		 */
		ArrayList<String> tooltips;

	    /* (non-Javadoc)
	     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	     */
	    @Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

	        JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	        if (-1 < index && null != value && null != tooltips) list.setToolTipText(tooltips.get(index));
	          
	        return comp;
	        
	    }

	    /**
	     * @param tooltips
	     */
	    public void setTooltips(ArrayList<String> tooltips) {
	        
	    	this.tooltips = tooltips;
	        
	    }
	    
	}
	
}
