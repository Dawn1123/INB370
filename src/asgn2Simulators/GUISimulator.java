/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {

	private final Integer WIDTH = 800,
						  HEIGHT = 600;
	
	private CarPark cp;
	private Simulator s;
	private Log l;
	private SimulationRunner sr;
	
	private int maxCarSpaces,
				smallCarSpaces,
				motorCycleSpaces,
				maxQueueSize,
				simSeed;
	
	private double carProb,
				   smallCarProb,
				   motoProb,
				   intendedStayMean,
				   intendedStaySD;
	
	JButton startButton;
	
	JTextField fieldMaxCarSpaces,
     		   fieldSmallCarSpaces,
     		   fieldMotoSpaces,
     		   fieldMaxQueueSize,
     		   fieldSimSeed,
     		   fieldCarProb,
     		   fieldSmallCarProb,
     		   fieldMotoProb,
     		   fieldIntendedStayMean,
     		   fieldIntendedStaySD; 
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String[] args) throws HeadlessException {
		super("Car Park Simulator");		
		createAndShowGUI();
		
		if (args.length == 0) {
			fieldMaxCarSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_CAR_SPACES));
			fieldSmallCarSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_SMALL_CAR_SPACES));
			fieldMotoSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_MOTORCYCLE_SPACES));
			fieldMaxQueueSize.setText(Integer.toString(Constants.DEFAULT_MAX_QUEUE_SIZE));
			fieldSimSeed.setText(Integer.toString(Constants.DEFAULT_SEED));
			fieldCarProb.setText(Double.toString(Constants.DEFAULT_CAR_PROB));
			fieldSmallCarProb.setText(Double.toString(Constants.DEFAULT_SMALL_CAR_PROB));
			fieldMotoProb.setText(Double.toString(Constants.DEFAULT_MOTORCYCLE_PROB));
			fieldIntendedStayMean.setText(Double.toString(Constants.DEFAULT_INTENDED_STAY_MEAN));
			fieldIntendedStaySD.setText(Double.toString(Constants.DEFAULT_INTENDED_STAY_SD));
			
		} else {
			fieldMaxCarSpaces.setText(args[0]);
			fieldSmallCarSpaces.setText(args[1]);
			fieldMotoSpaces.setText(args[2]);
			fieldMaxQueueSize.setText(args[3]);
			fieldSimSeed.setText(args[4]);
			fieldCarProb.setText(args[5]);
			fieldSmallCarProb.setText(args[6]);
			fieldMotoProb.setText(args[7]);
			fieldIntendedStayMean.setText(args[8]);
			fieldIntendedStaySD.setText(args[9]);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

	}
	
	private void runSimulation() {
		maxCarSpaces = Integer.parseInt(fieldMaxCarSpaces.getText());
		smallCarSpaces = Integer.parseInt(fieldSmallCarSpaces.getText());
		motorCycleSpaces = Integer.parseInt(fieldMotoSpaces.getText());
		maxQueueSize = Integer.parseInt(fieldMaxQueueSize.getText());
		simSeed = Integer.parseInt(fieldSimSeed.getText());
		
		carProb = Double.parseDouble(fieldCarProb.getText());
		smallCarProb = Double.parseDouble(fieldSmallCarProb.getText());
		motoProb = Double.parseDouble(fieldMotoProb.getText());
		intendedStayMean = Double.parseDouble(fieldIntendedStayMean.getText());
		intendedStaySD = Double.parseDouble(fieldIntendedStaySD.getText());		
		
		cp = new CarPark(maxCarSpaces, smallCarSpaces, motorCycleSpaces, maxQueueSize);
		s = null;
		l = null; 
		try {
			s = new Simulator(simSeed, intendedStayMean, intendedStaySD, carProb, smallCarProb, motoProb);
			l = new Log();
		} catch (IOException | SimulationException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}		
	
		//Run the simulation 
		sr = new SimulationRunner(cp,s,l);
		try {
			sr.runSimulation();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void createAndShowGUI() {
		startButton = new JButton("Start Simulation");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				runSimulation();
			}
		});
        
        fieldMaxCarSpaces = new JTextField("", 5);
        fieldSmallCarSpaces = new JTextField("", 5);
        fieldMotoSpaces = new JTextField("", 5);
        fieldMaxQueueSize = new JTextField("", 5);
        JLabel maxCarSpacesLBL = new JLabel("Max Parking Spaces:");
        JLabel smallCarSpacesLBL = new JLabel("Small Car Spaces:");
        JLabel motoSpacesLBL = new JLabel("Motorcycle Spaces:");
        JLabel maxQueueSizeLBL = new JLabel("Max Queue Size:");
        
        fieldSimSeed = new JTextField("", 5);
        fieldCarProb = new JTextField("", 5);
        fieldSmallCarProb = new JTextField("", 5);
        fieldMotoProb = new JTextField("", 5);
        fieldIntendedStayMean = new JTextField("", 5);
        fieldIntendedStaySD = new JTextField("", 5);        
        JLabel simSeedLBL = new JLabel("Simulation Seed:");
        JLabel carProbLBL = new JLabel("Car Prob:");
        JLabel smallCarProbLBL = new JLabel("Small Car Prob:");
        JLabel motoProbLBL = new JLabel("Motorcycle Prob:");
        JLabel intendedStayMeanLBL = new JLabel("Intended Stay Mean:");
        JLabel intendedStaySDLBL = new JLabel("Intended Stay SD:");
        
        JPanel optionsOuterPanel = new JPanel();

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(BorderFactory.createTitledBorder("User Inputs"));
		optionsPanel.setPreferredSize(new Dimension(390, 250));
		optionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		JTabbedPane tabbedData = new JTabbedPane();
		
        JPanel logOutput = new JPanel();
        logOutput.add(new JLabel("Tab 1"));
        JPanel plotSeries = new JPanel();
        plotSeries.add(new JButton("Tab 2"));
        JPanel summary = new JPanel();
        summary.add(new JButton("Tab 2"));

        tabbedData.add("Log Output", logOutput);
        tabbedData.add("Plot Series", plotSeries);
        tabbedData.add("Summary", summary);
        
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
		optionsPanel.add(maxCarSpacesLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldMaxCarSpaces, c);
		
		c.gridx = 1;
		c.gridy = 0;
		optionsPanel.add(simSeedLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldSimSeed, c);
		
		c.gridx = 2;
		c.gridy = 0;
		optionsPanel.add(carProbLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldCarProb, c);
		
        c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 2;
		optionsPanel.add(smallCarSpacesLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldSmallCarSpaces, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 2;
		optionsPanel.add(smallCarProbLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldSmallCarProb, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 2;
		optionsPanel.add(motoProbLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldMotoProb, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 4;
		optionsPanel.add(motoSpacesLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldMotoSpaces, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 4;
		optionsPanel.add(intendedStayMeanLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldIntendedStayMean, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 4;
		optionsPanel.add(intendedStaySDLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldIntendedStaySD, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 6;
		optionsPanel.add(maxQueueSizeLBL, c);		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 7;
		optionsPanel.add(fieldMaxQueueSize, c);		
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 8;
		optionsPanel.add(startButton, c);
		
		optionsOuterPanel.add(optionsPanel);

		Box contentPanel = new Box(BoxLayout.X_AXIS);
		
		contentPanel.add(tabbedData);

		this.add(optionsOuterPanel, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocation(new Point(125, 50));
		pack();
		setVisible(true);
	}
	
	private static void checkCommandLineParams(String[] args) {
		if ((args.length > 0 && args.length < 10) || args.length > 10) {
			System.err.println("Program terminated: if providing command line arguments there needs to be 10, you provided: " + args.length);
			System.exit(1);
			
		} else if (args.length == 10) {
			for (int i = 0; i < 5; i++) {
				try {
					Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					System.err.println("Program terminated, First 5 command line arguments must be of type Integer, value that threw this exception: " + args[i]);
					System.exit(1);
				}
			}
			
			for (int i = 5; i < 10; i++) {
				try {
					Double.parseDouble(args[i]);
				} catch (NumberFormatException e) {
					System.err.println("Program terminated, last 5 command line arguments must be of type Double, value that threw this exception: " + args[i]);
					System.exit(1);
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		checkCommandLineParams(args);
		JFrame.setDefaultLookAndFeelDecorated(true);
		new GUISimulator(args);
	}

}
