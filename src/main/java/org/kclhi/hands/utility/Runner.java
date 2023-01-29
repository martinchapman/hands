package org.kclhi.hands.utility;

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
import java.util.Scanner;

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
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.mapdblocal.BTreeMap;

import org.kclhi.hands.utility.GameTheoretic.ApproximatePayoffMatrix;
import org.kclhi.hands.utility.output.Datafile;
import org.kclhi.hands.utility.output.GroupedHiderRecords;
import org.kclhi.hands.utility.output.HiderRecord;
import org.kclhi.hands.utility.output.OutputManager;
import org.kclhi.hands.utility.output.OutputManagerOffHeap;
import org.kclhi.hands.utility.output.TraverserRecord;
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
	private static boolean OFF_HEAP = true;
	
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
		  "UniqueRandomSetRepeatStrategyOver",
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
		  "DeceptiveTemp",
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
		  "MetaRandom",
		  "MetaRandomStrategyOver",
		  "MetaConnected",
		  "MetaConnectedStrategyOver"
		  
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
          "HighProbabilityK",
          "VariableNodesHighProbability",
          "VariableHistoryHighProbability",
          "HighProbabilityRepetitionCheck",
          
          "InverseHighProbability",
          
          // 
          
          "MetaProbability",
          "MetaProbabilityStrategyOver"
          
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
		
		new Runner(args);
		
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
	
	private JCheckBox strategyOverRounds;
	
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
		
		if (!textBased) setVisible(true);
		
	}
	
	/**
	 * 
	 */
	private OutputManager outputManager;
	
	/**
	 * @param outputFeedback
	 * @param hiderRecord
	 * @param hidersSeeker
	 */
	private void outputFeedback(DefaultListModel<HiderRecord> outputFeedback, HiderRecord hiderRecord, TraverserRecord hidersSeeker) {
		
		HiderRecord hiderCopy;
		
		if ( hiderRecord instanceof GroupedHiderRecords ) {
			
			hiderCopy = new GroupedHiderRecords("");
			
		} else {
			
			hiderCopy = new HiderRecord("");

		}
		
		hiderCopy.duplicateRecord(hiderRecord);
		
		hiderCopy.clearSeekersAndAttributes();
		
		hiderCopy.addSeeker(hidersSeeker);
		
		hiderCopy.setOpponents(hidersSeeker.getTraverser());
		
		outputFeedback.addElement(hiderCopy);
		
		if ( hiderRecord instanceof GroupedHiderRecords ) {
			
			((GroupedHiderRecords)hiderCopy).clearAllHiders();
			
			for ( HiderRecord innerHider : ((GroupedHiderRecords)hiderRecord).allHidersNoOuter() ) {
				
				HiderRecord innerHiderCopy = new HiderRecord("");
				
				innerHiderCopy.duplicateRecord(innerHider);
				
				for ( TraverserRecord innerHiderSeeker : innerHider.getSeekersAndAttributes() ) {
					
					if ( !innerHiderSeeker.getTraverser().equals(hidersSeeker.getTraverser()) ) {
						
						innerHiderCopy.getSeekersAndAttributes().remove(innerHiderSeeker);
						
					} else {
					
						innerHiderCopy.setOpponents(innerHiderSeeker.getTraverser());
						
					}
					
				}
				
				((GroupedHiderRecords)hiderCopy).addHider(innerHiderCopy);
				
			}
			
		}
		
	}
	
	private void outputFeedbackEnd(DefaultListModel<HiderRecord> outputFeedback) {
		
		// Mark end of Hiders in that game
		
		outputFeedback.addElement(new HiderRecord("") {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String toString() {
				
				return "-----";
				
			}
			
		});
		
	}
	
	/**
	 * 
	 */
	private void refreshMeasureList() {
		
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
	}
	
	/**
	 * @param tabbedPane
	 */
	private void outputTab(JTabbedPane tabbedPane) {
		
		if ( OFF_HEAP ) {
			
			outputManager = new OutputManagerOffHeap(DATA_INPUT, recursiveSub);
			
		} else {
			
			outputManager = new OutputManager(DATA_INPUT, recursiveSub);
		
		}
		
		//////
		
		JPanel outputTab = new JPanel();
		
		outputTab.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Output", outputTab);
		
		////
		
		JPanel northPane = new JPanel();
		
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		outputTab.add(northPane, BorderLayout.NORTH);
		
		//
		
		collateOutput = new JButton("Process Output");
		
		northPane.add(collateOutput);
		
		final DefaultListModel<HiderRecord> outputFeedback = new DefaultListModel<HiderRecord>();

		final JButton showTextStats = new JButton("Show text stats");
		
		showTextStats.setEnabled(false);
		
		files = new JComboBox<Datafile>();
		
		files.setEnabled(false);
		
		collateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ( files.isEnabled() ) {
					
					outputManager.processIndividualOutput(((Datafile)files.getSelectedItem()));
					
				} else {
					
					outputManager.processAllOutput();
					
				}
				
				outputFeedback.clear();
				
				if ( OFF_HEAP ) {
				    
					if ( Runner.this.textBased ) System.out.println("\nWriting processed results:");
					
					for (Entry<Integer, BTreeMap<Integer, HiderRecord>> fileHiderRecord : ((OutputManagerOffHeap)outputManager).getOffHeapCache().entrySet()) {
	
						Utils.printSystemStats();
						
						for (Entry<Integer, HiderRecord> hiderRecord : fileHiderRecord.getValue().entrySet()) {
							
							Utils.printSystemStats();
							
							for ( TraverserRecord hidersSeeker : hiderRecord.getValue().getSeekersAndAttributes() ) {
								
								Utils.printSystemStats();
								
								outputFeedback(outputFeedback, hiderRecord.getValue(), hidersSeeker);
								
							}
							
						}
						
						outputFeedbackEnd(outputFeedback);
						
					}
				
				} else {
					
					for (ArrayList<HiderRecord> fileHiderRecord : outputManager.getCache() ) {
						
						Utils.printSystemStats();
						
						for (HiderRecord hiderRecord : fileHiderRecord) {
							
							Utils.printSystemStats();
							
							for ( TraverserRecord hidersSeeker : hiderRecord.getSeekersAndAttributes() ) {
								
								Utils.printSystemStats();
								
								outputFeedback(outputFeedback, hiderRecord, hidersSeeker);
								
							}
							
						}
						
						outputFeedbackEnd(outputFeedback);
						
					}
					
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
		
		showFiles = new JButton("Show files");
		
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
		
		outputFeedbackList = new JList<HiderRecord>(outputFeedback);
		
		JScrollPane outputFeedbackListScroll = new JScrollPane(outputFeedbackList);
		
		measure = new JComboBox<String>();
		
		final JLabel simulationParameters = new JLabel();
		
		seekers = new JRadioButton("Seekers");
		
		outputFeedbackList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (e.getValueIsAdjusting()) return;
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				refreshMeasureList();
				
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
		
		hiders = new JRadioButton("Hiders");
		
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
		
		graphTypesCombo = new JComboBox<String>();

		graphTypesCombo.addItem("Bar");
		
		graphTypesCombo.addItem("Line");
		
		graphTypesCombo.addItem("LineOne");
		
		graphTypesCombo.addItem("3D");
		
		graphTypesCombo.addItem("Payoff Matrix");
		
		centerPaneRightCenter.add(new JLabel("Graph types:"));
		
		centerPaneRightCenter.add(graphTypesCombo);
		
		//
		
		categories = new JComboBox<String>();
		
		categories.addItem("Opponent");
		
		categories.addItem("Topology");
		
		centerPaneRightCenter.add(new JLabel("Bar graph categories:"));
		
		centerPaneRightCenter.add(categories);
		
		//
		
		gameOrRound = new JComboBox<String>();
		
		gameOrRound.addItem("Game");
		
		gameOrRound.addItem("Round");
		
		centerPaneRightCenter.add(new JLabel("Average over Game or Round:"));
		
		centerPaneRightCenter.add(gameOrRound);
		
		//
		
		outputEnabled = new JCheckBox("Output enabled");
		
		centerPaneRightCenter.add(outputEnabled);
		
		//
		
		generateGraph = new JButton("Generate graph");
		
		generateGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				// Allows for selection of multiple hiders or multiple seekers from list
				ArrayList<TraverserRecord> selectedHiders = new ArrayList<TraverserRecord>();
				
				selectedSeekers = new ArrayList<TraverserRecord>();
				
				String title = "";
				
				ArrayList<HiderRecord> recordsFromList = new ArrayList<HiderRecord>(outputFeedbackList.getSelectedValuesList());
				
				ArrayList<HiderRecord> recordsForGraph = new ArrayList<HiderRecord>();
				
				for ( HiderRecord record : recordsFromList ) {
					
					if ( record instanceof GroupedHiderRecords ) { 
						
						recordsForGraph.addAll(((GroupedHiderRecords)record).allHiders());
						
					} else {
						
						recordsForGraph.add(record);
						
					}
					
				}
				
				for ( HiderRecord hider : recordsForGraph ) {
					
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
						
						selectedSeekers.add(selectedSeekers.size(), hidersSeeker);
							
						//}
						
					}
					
				}
				
				ArrayList<TraverserRecord> allPlayers = new ArrayList<TraverserRecord>(selectedHiders);
				allPlayers.addAll(selectedSeekers);
				
				if ( OVERWRITE_FIGURE && figureToOverwrite != null) {
					
					if (seekers.isSelected()) {
						
						if (graphTypesCombo.getSelectedItem().equals("Line")) {
							
							outputManager.showLineGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
					
						} else if (graphTypesCombo.getSelectedItem().equals("LineOne")) {
							
							outputManager.showLineOneGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
							
						} else if (graphTypesCombo.getSelectedItem().equals("Bar")) {
							
							outputManager.showBarGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
							
						} else if (graphTypesCombo.getSelectedItem().equals("3D")) {
							
							outputManager.show3DGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
							
						}
						
					} else if (hiders.isSelected()) {
						
						if (graphTypesCombo.getSelectedItem().equals("Line")) {
							
							outputManager.showLineGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
						
						} else if (graphTypesCombo.getSelectedItem().equals("LineOne")) {
								
							outputManager.showLineOneGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
								
						} else if (graphTypesCombo.getSelectedItem().equals("Bar")) {
							
							outputManager.showBarGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
							
						} else if (graphTypesCombo.getSelectedItem().equals("3D")) {
							
							outputManager.show3DGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected(), figureToOverwrite);
							
						}
						
					}
					
				} else {
					
					if (seekers.isSelected()) {
						
						if (graphTypesCombo.getSelectedItem().equals("Line")) {
							
							outputManager.showLineGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected());
					
						} else if (graphTypesCombo.getSelectedItem().equals("Bar")) {
							
							outputManager.showBarGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
							
						} else if (graphTypesCombo.getSelectedItem().equals("3D")) {
							
							outputManager.show3DGraphForAttribute(allPlayers, selectedSeekers, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
							
						}
						
					} else if (hiders.isSelected()) {
						
						if (graphTypesCombo.getSelectedItem().equals("Line")) {
							
							outputManager.showLineGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), outputEnabled.isSelected());
							
						} else if (graphTypesCombo.getSelectedItem().equals("Bar")) {
							
							outputManager.showBarGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
							
						} else if (graphTypesCombo.getSelectedItem().equals("3D")) {
							
							outputManager.show3DGraphForAttribute(allPlayers, selectedHiders, (String)gameOrRound.getSelectedItem(), title, (String)measure.getSelectedItem(), (String)categories.getSelectedItem(), outputEnabled.isSelected());
							
						}
						
					}
				
				}
				
				if (graphTypesCombo.getSelectedItem().equals("Payoff Matrix")) {
					
					ApproximatePayoffMatrix HPM = new ApproximatePayoffMatrix("");
					
					for ( Pair<TraverserRecord, Double> seekerPayoff : outputManager.matrixPayoff(selectedSeekers, allPlayers) ) {
						
						HPM.addPayoff("Seeker", seekerPayoff.getElement0().toString().split(" vs ")[0], seekerPayoff.getElement0().toString().split(" vs ")[1], seekerPayoff.getElement1());
						
					}
					
					for ( Pair<TraverserRecord, Double> hiderPayoff : outputManager.matrixPayoff(selectedHiders, allPlayers) ) {
						
						HPM.addPayoff("Hider", hiderPayoff.getElement0().toString().split(" vs ")[0], hiderPayoff.getElement0().toString().split(" vs ")[1], hiderPayoff.getElement1());
						
						
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
		
		simulationParameters.setLayout(new GridLayout(4, 2));
		
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
		
		simulationParameters.add(new JPanel());
		
		strategyOverRounds = new JCheckBox("Strategy over rounds");
		
		simulationParameters.add(strategyOverRounds);
		
		
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
					
					writer = new FileWriter(Utils.FILEPREFIX + SIMULATION_SCHEDULE);
					
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
	
	/**
	 * 
	 */
	private ArrayList<String> simulations;
	
	/**
	 * @param input
	 * @return
	 */
	private boolean checkForBack(String input) {
		
		if ( input.contains("back") ) return true;
		
		return false;
		
	}
	
	/**
	 * @param input
	 * @return
	 */
	private boolean checkForBlank(String input) {
		
		if ( input.trim().length() == 0 ) return true;
		
		return false;
		
	}
	
	/**
	 * @param question
	 * @return
	 */
	private String askQuestion(String question, Scanner in) {
		
		System.out.println(question);
		
		String response = "";
		
		while(true) {
			
			try {
				
				response = in.nextLine();
				
				break;
				
			} catch ( java.util.NoSuchElementException e ) {
				
				continue;
				
			}
		
		}
		
		return response;
		
	}
	
	/**
	 * @param listModel
	 * @return
	 */
	private <E> ArrayList<Object> itemsInList(ListModel<E> listModel) {
		
		ArrayList<Object> itemsInList = new ArrayList<Object>();
		
		for ( int i = 0; i < listModel.getSize(); i++ ) {
			
			itemsInList.add(listModel.getElementAt(i));
			
		}
		
		return itemsInList;
		
	}
	
	private JButton showFiles;
	
	private JComboBox<Datafile> files;
	
	private JButton collateOutput;
	
	private JList<HiderRecord> outputFeedbackList;
	
	private JRadioButton seekers;
	
	private JRadioButton hiders;
	
	private JComboBox<String> measure;
	
	private JComboBox<String> graphTypesCombo;
	
	private JComboBox<String> categories;
	
	private JComboBox<String> gameOrRound;
	
	private JCheckBox outputEnabled;
	
	private JButton generateGraph;
	
	private final static boolean SHORT_TEXT_UI = true;
	
	private boolean textBased = false;
	
	private boolean killAfterSim = false;
	
	public void simulationSchedule(Scanner in) throws NumberFormatException {
		
		String response;
		
		while (true) {
			
			// ~MDC Casting things as Objects :-/
			for ( Object item : itemsInList(queueListModel) ) {
				
				String itemText;
				
				if ( SHORT_TEXT_UI ) itemText = item.toString().substring(0,  item.toString().indexOf("EdgeWeight") - 2);
				
				System.out.println("("+ itemsInList(queueListModel).indexOf(item) + ") " + itemText);
				
				System.out.println("------------------------------------------------------------------------------------");
				
			}
			
			response = askQuestion("Enter single number to start simulation, or list of sims to queue, or (back)", in);
			
			if ( !checkForBack(response) ) {
				
				queueList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				
				if ( !response.trim().contains(" ") ) {
					
					queueList.setSelectedIndex(Integer.parseInt(response));
					
				} else {
					
					ArrayList<Integer> indices = new ArrayList<Integer>();
					
					for ( String index : response.trim().split(" ") ) {
						
						indices.add(Integer.parseInt(index));
						
					}
					
					queueList.setSelectedIndices(Utils.convertIntegers(indices));
					
				}
				
				startSelected.doClick();
				
			} else {
				
				break;
				
			}
		
		}
		
	}
	
	public void plotGraph(Scanner in, boolean deleting) throws NumberFormatException {
		
		while ( true ) {
			
			String response = "";
			
			if ( !deleting ) System.out.println("");
			
			if ( !deleting ) System.out.println("------------------------------------------");
			
			outputFeedbackList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			int outputIndex = 0;
			
			for ( Object item : itemsInList(outputFeedbackList.getModel()) ) {
				
				if ( !deleting ) System.out.println("("+ (outputIndex++) + ") " + item);
				
			}
			
			if ( deleting ) {
				
				outputFeedbackList.setSelectedIndex(0);
				
				if (!outputFeedbackList.getSelectedValue().toString().equals("-----")) {
					
					outputManager.deleteFile(outputFeedbackList.getSelectedValue().getFileRelatingTo());
					
					outputManager.removeOrphaned();
				
					showFiles.doClick();
				
					return;
				
				}
				
			}
			
			while ( response.equals("") || ( response.trim().split(" ").length > ( outputFeedbackList.getModel().getSize() - 1 ) ) ) {
			
				response = askQuestion("Enter pairs of player numbers (all), (spaced list) or (back)", in);
				
				if ( checkForBack(response) ) break;
				
				if ( response.contains("all") ) {
					
					response = "";
					
					for ( int i = 0; i < itemsInList(outputFeedbackList.getModel()).size() - 1; i++ ) {
						
						response += i + " ";
						
					}
					
					response = response.substring(0, response.length() - 1);
					
				}
				
				for ( String pair : response.trim().split(" ") ) {
					
					if (Integer.parseInt(pair) < 0 || Integer.parseInt(pair) > outputIndex ) {
						
						response = "";
						
					}
					
				}
			
			}
			
			if ( !checkForBack(response) ) {
				
				if ( !response.trim().contains(" ") ) {
					
					outputFeedbackList.setSelectedIndex(Integer.parseInt(response));
					
				} else {
					
					ArrayList<Integer> indices = new ArrayList<Integer>();
					
					for ( String index : response.trim().split(" ") ) {
						
						if ( index.contains("-----") ) continue;
						
						indices.add(Integer.parseInt(index));
						
					}
					
					outputFeedbackList.setSelectedIndices(Utils.convertIntegers(indices));
					
				}
				
				while (true) {
					
					try {
						
						configureGraph(in);
						
						break;
						
					} catch ( NumberFormatException e ) {
						
						System.out.println("Input error, restarting...");
						
					}
					
				}
				
			} else {
				
				return;
				
			}
		
		}
		
	}
	
	/**
	 * 
	 */
	private boolean ASK_FOR_CATEGORY = false;
	
	/**
	 * 
	 */
	private String figureToOverwrite;
	
	/**
	 * @param in
	 * @throws NumberFormatException
	 */
	private void configureGraph(Scanner in) throws NumberFormatException {
		
		String response = askQuestion("Enter hiders or seekers. (Enter) for default: " + ( hiders.isSelected() ? "hiders" : "seekers" ) + " or (back).", in);
		
		if ( !checkForBack(response) ) {
			
			if ( !checkForBlank(response) ) {
				
				if ( response.toLowerCase().contains("hider") ) {
					
					hiders.setSelected(true);
					
				} else {
					
					seekers.setSelected(true);
					
				}
				
			}
			
			refreshMeasureList();
			
			while ( !itemsInList(measure.getModel()).contains(response) ) {
			
				response = askQuestion("Enter measure (" + itemsInList(measure.getModel()) + "). (Enter) for default: " + measure.getModel().getElementAt(measure.getSelectedIndex()) + " or (back).", in);
			
				if ( response.equals("") || checkForBack(response) ) break;
				
			}
			
			if ( !checkForBack(response) ) {
				
				if ( !checkForBlank(response) ) {
					
					response = response.trim();
					
					measure.getModel().setSelectedItem(measure.getModel().getElementAt(itemsInList(measure.getModel()).indexOf(response)));
					
				}
				
				while ( !itemsInList(graphTypesCombo.getModel()).contains(response) ) {
				
					response = askQuestion("Enter graph type (" + itemsInList(graphTypesCombo.getModel()) + "). (Enter) for default: " + graphTypesCombo.getModel().getElementAt(graphTypesCombo.getSelectedIndex()) + " or (back).", in);
				
					if ( response.equals("") || checkForBack(response) ) break;
					
				}
				
				if ( !checkForBack(response) ) {
					
					if ( !checkForBlank(response) ) {
						
						response = response.trim();
						
						graphTypesCombo.getModel().setSelectedItem(graphTypesCombo.getModel().getElementAt(itemsInList(graphTypesCombo.getModel()).indexOf(response)));
						
					}
					
					if ( ASK_FOR_CATEGORY ) {
						
						while ( !itemsInList(categories.getModel()).contains(response) ) {
					
							response = askQuestion("Enter bar category (" + itemsInList(categories.getModel()) + "). (Enter) for default: " + categories.getModel().getElementAt(categories.getSelectedIndex()) + " or (back).", in);
					
							if ( response.equals("") || checkForBack(response) ) break;
						
						}
					
					} else {
						
						response = "";
						
					}
					
					if ( !checkForBack(response) ) {
						
						if ( !checkForBlank(response) ) {
							
							response = response.trim();
							
							categories.getModel().setSelectedItem(categories.getModel().getElementAt(itemsInList(categories.getModel()).indexOf(response)));
							
						}
						
						while ( !itemsInList(gameOrRound.getModel()).contains(response) ) {
						
							response = askQuestion("Enter game or round (" + itemsInList(gameOrRound.getModel()) + "). (Enter) for default: " + gameOrRound.getModel().getElementAt(gameOrRound.getSelectedIndex()) + " or (back).", in);
						
							if ( response.equals("") || checkForBack(response) ) break;
							
						}
					
						if ( !checkForBack(response) ) {
							
							if ( !checkForBlank(response) ) {
								
								response = response.trim();
								
								gameOrRound.getModel().setSelectedItem(gameOrRound.getModel().getElementAt(itemsInList(gameOrRound.getModel()).indexOf(response)));
								
							}
							
						} else {
							
							return;
							
						}
						
						if ( OVERWRITE_FIGURE ) {
							
							response = askQuestion("Enter figure to overwrite: ", in);
							
							if ( !checkForBack(response) ) {
								
								if ( !checkForBlank(response) ) {
									
									figureToOverwrite = response;
									
								}
								
							} else {
								
								return;
								
							}
						
						}
						
						response = "";
						
						while ( response.equals("") ) {
							
							response = askQuestion("Confirm graph output (y/n)", in);
							
						}
						
						if ( response.contains("y")) {
							
							outputEnabled.setSelected(true);
							
							generateGraph.doClick();
						
						}
						
						response = askQuestion("(replot), (exit) or (back)", in);
						
						if ( !checkForBack(response) ) {
							
							if ( response.contains("replot") ) {
								
								return;
								
							} else {
								
								System.exit(0);
								
							}
							
						}
						
					} else {
						
						return;
						
					}
					
				} else {
					
					return;
					
				}
				
			} else {
				
				return;
				
			}
			
		} else {
			
			return;
			
		}
		
	}
	
	/**
	 * @param in
	 * @throws NumberFormatException
	 */
	private void completeSimulations(Scanner in) throws NumberFormatException {
		
		String response = "";
		
		while (true) {
			
			if ( files.getModel().getSize() == 0 ) showFiles.doClick();
			
			for ( Object item : itemsInList(files.getModel()) ) {
			
				String itemText = "";
				
				if ( SHORT_TEXT_UI && item.toString().contains("EdgeWeight") ) itemText = item.toString().substring(0,  item.toString().indexOf("EdgeWeight") - 2);
				
				System.out.println("("+ itemsInList(files.getModel()).indexOf(item) + ") " + itemText);
				
				System.out.println("------------------------------------------------------------------------------------");
				
			}
			
			response = "";
			
			boolean deleting = false;
			
			while ( response == "") {
			
				response = askQuestion("Enter number to process, (all) to process all or (back). D<N> deletes.", in);
				
				if ( response.contains("D") ) { 
					
					response = response.replace("D", "");
				
					deleting = true;
					
				}
				
				if ( Integer.parseInt(response) < 0 || Integer.parseInt(response) > ( itemsInList(files.getModel()).size() - 1 ) ) response = "";
				
			}
			
			if ( !checkForBack(response) ) {
			
				if ( response.contains("all") ) {
				
					showFiles.doClick();
					
				} else {
					
					files.getModel().setSelectedItem(files.getModel().getElementAt(Integer.parseInt(response)));
				
				}
				
				collateOutput.doClick();
				
				while ( true ) {
					
					try {
						
						plotGraph(in, deleting);
						
						break;
					
					} catch (NumberFormatException e ) { 
						
						System.out.println("Input error, restarting...");
						
					} catch (Exception e) {
						
						System.out.println("Restarting...");
						
					}
				
				}
			
			} else {
				
				break;
				
			}
			
		}
		
	}
	
	public void textMenu(Scanner in) throws NumberFormatException {
		
		while ( true ) {
			
			if ( Utils.DEBUG == true ) {
			
				String response = askQuestion("\nWARNING: DEBUG ENABLED \nQuit (q) or Continue (c)", in);
				
				if ( response.equals("q") ) {
					
					System.exit(0);

				} 
			
			}
			
			String response = askQuestion("\nMain menu :) \n(1) List simulation schedule \n(2) List complete simulations", in);
			
			if ( response.equals("1") ) {
				
				simulationSchedule(in);
				
			} else if ( response.equals("2") ) {
				
				completeSimulations(in);
				
			}
			
		}
		
	}
	
	/**
	 * 
	 */
	private boolean generateOutput = true;
	
	/**
	 * 
	 */
	private String SIMULATION_SCHEDULE = "simulationSchedule.txt";
	
	/**
	 * 
	 */
	private String DATA_INPUT = "data";
	
	/**
	 * 
	 */
	private boolean OVERWRITE_FIGURE = false;
	
	/**
	 * 
	 */
	private boolean recursiveSub = false;
	
	/**
	 * @param args
	 */
	public Runner(String[] args) {
		
		super("HANDS");
		
		if ( args.length > 0 ) {
			
			ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
			
			if ( argsList.contains("-k") ) {
				
				killAfterSim = true;
				
			}
			
			if ( argsList.contains("-t") ) {
				
				textBased = true;
				
			}
			
			if ( argsList.contains("-nop")) {
				
				generateOutput = false;
				
			}
			
			if ( argsList.contains("-di")) {
				
				DATA_INPUT = argsList.get(argsList.indexOf("-di") + 1);;
				
			}
			
			if ( argsList.contains("-ss")) {
				
				SIMULATION_SCHEDULE = argsList.get(argsList.indexOf("-ss") + 1);
			
			}
			
			if ( argsList.contains("-overwrite")) {
				
				OVERWRITE_FIGURE = true;
			
			}
			
			if ( argsList.contains("-r") ) {
				
				recursiveSub = true;
				
			}
			
		}
		
		init();
		
		if ( textBased ) {
			
			Scanner in = new Scanner(System.in);
			
			outputManager.setTextBased(true);
			
			while ( true ) {
				
				try {
				
					textMenu(in);
				
					break;
					
				} catch (NumberFormatException e) {
					
					System.out.println("Input error, restarting...");
				
				}
				
			}
			
			in.close();
			
		}
		
	}
	
	private void init() {
	
		generateGUI();
		
		// Collect list of simulations
        simulations = Utils.readFromFile(Utils.FILEPREFIX + SIMULATION_SCHEDULE);
        
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
				
				if (killAfterSim) System.exit(0);
				
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
				
				if (killAfterSim) System.exit(0);
				
			}
        	
        });
        
        //
        
        queue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!queueListModel.contains(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1))) {
				
					queueListModel.addElement(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1));
				
					try {
						
						Utils.writeToFile(new FileWriter(Utils.FILEPREFIX + SIMULATION_SCHEDULE, true), Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1) + "\n");
						
						simulations = Utils.readFromFile(Utils.FILEPREFIX + SIMULATION_SCHEDULE);
						
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
				
				if (killAfterSim) System.exit(0);
				
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
				
				Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z.+\\*\\(\\)]+)\\})").get(0);
  
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
					
				} else if (paramPair.getElement0().equals("StrategyOverRounds")) {
					
					if ( Boolean.parseBoolean(paramPair.getElement1()) ) { 
						
						strategyOverRounds.setSelected(true); 
						
					} else { 
						
						strategyOverRounds.setSelected(false); 
					
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
			
			"{ResetPerRound," + resetPerRound.isSelected() + "}",
			
			"{StrategyOverRounds," + strategyOverRounds.isSelected() + "}"
			
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
		
		/*for ( int paramID = 0; paramID < simulationParameters.length; paramID++ ) {
			
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
			
		}*/
		
		// Group ID to use if j and there are multiple files output from one simulation
		String groupID = Utils.timestamp();
		
		String[] simulationParametersUnchanged = Arrays.copyOf(simulationParameters, simulationParameters.length);
		
		boolean containsJ = false;
		
		for ( int j = 0; j < GAMES; j++ ) {
		
			containsJ = false;
			
			for (int k = 0; k < simulationParameters.length; k++) {
	  		  
	    	    if (simulationParametersUnchanged[k].contains("j*") || simulationParametersUnchanged[k].contains("j+")) { 
	    	    	
	    	    	containsJ = true;
	    	    	
	    	    	String parsed = parseExpression(simulationParametersUnchanged[k].replace("j", j + "").substring(simulationParametersUnchanged[k].replace("j", j + "").indexOf(",") + 1, simulationParametersUnchanged[k].replace("j", j + "").length() - 1));
	    	    	
	    	    	if ( parsed.equals("-1") ) throw new UnsupportedOperationException("Failed to parse I expression");
	    	    	
					simulationParameters[k] = "" + simulationParametersUnchanged[k].substring(0, simulationParametersUnchanged[k].indexOf(",") + 1) + parsed + "}";
	
	    	    
	    	    } else if (simulationParametersUnchanged[k].contains(",j")) { 
	    	  		
	    	    	containsJ = true;
	    	    	
	    	  		simulationParameters[k] = simulationParametersUnchanged[k].replaceAll("(\\,j)", "," + j); 
	    	  		
	    	  	}
		   
		    }
			
			if ( containsJ ) { 
				
				runSimulationX(GAMES, simulationParameters, "" + groupID);
			
			} else {
				
				break;
				
			}
			
		}
		
		if (!containsJ) runSimulationX(GAMES, simulationParameters);
		
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
	
	/**
	 * @param GAMES
	 * @param simulationParameters
	 */
	private void runSimulationX(int GAMES, String[] simulationParameters) {
		
		runSimulationX(GAMES, simulationParameters, "");
		
	}
	
	private void runSimulationX(int GAMES, String[] simulationParameters, String namePrefix) {
		
		/***********/
		
		if ( !namePrefix.equals("") ) namePrefix = namePrefix + "-";
		
		// Generate ID For this simulation
		String currentSimulationIdentifier = namePrefix + ( Utils.timestamp() );
		
		if ( generateOutput ) {
			
			Utils.writeToFile(Utils.FILEPREFIX + "simRecordID.txt", currentSimulationIdentifier);
		
			Utils.writeToFile(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", "");
		
			Utils.writeToFile(Utils.FILEPREFIX + "/data/" + currentSimulationIdentifier + ".csv", Arrays.toString(simulationParameters) + "\n");
		
		}
		
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
  				  "ResetPerRound", // Whether players knowledge should persist through rounds
  				  "StrategyOverRounds", // Whether to double the number of round sets, in order to test strategies that evolve over all rounds
  				  "GenerateOutputFiles" // Whether to log results to file
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
				 "true", // Whether players knowledge should persist through rounds
				 "false", // Whether to double the number of round sets, in order to test strategies that evolve over all rounds
				  (generateOutput + "")
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
			
			Utils.runCommand("java -cp target/hands-1.0-SNAPSHOT.jar org.kclhi.hands.Main " + i + " " + GAMES + " " + paramString);
			 
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
