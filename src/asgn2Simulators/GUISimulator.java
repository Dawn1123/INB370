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

import javax.swing.*;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {

	private final Integer WIDTH = 800,
						  HEIGHT = 450;
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String[] args) throws HeadlessException {
		super("Car Park Simulator");
		
		JTabbedPane tabbedData = new JTabbedPane();
		
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Tab 1"));
        JPanel panel2 = new JPanel();
        panel2.add(new JButton("Tab 2"));

        tabbedData.add("Printed Log", panel1);
        tabbedData.add("Graph", panel2);
		
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        
        JTextField maxCarSpaces = new JTextField();
        JTextField smallCarSpaces = new JTextField();
        JTextField motoSpaces = new JTextField();
        JTextField maxQueueSize = new JTextField();
        JLabel carParkSize = new JLabel("Max Parking Spaces:");
        JLabel smallCarParks = new JLabel("Small Car Spaces:");
        JLabel motorcycleParks = new JLabel("Motorcycle Spaces:");
        JLabel carParkQueueSize = new JLabel("Max Queue Size:");
        
        JTextField simSeed = new JTextField();
        JTextField carProb = new JTextField();
        JTextField smallCarProb = new JTextField();
        JTextField motoProb = new JTextField();
        JTextField intendedStayMean = new JTextField();
        JTextField intendedStaySD = new JTextField();        
        JLabel carParkSimSeed = new JLabel("Simulation Seed:");
        JLabel simCarProb = new JLabel("Car Prob:");
        JLabel simSmallProb = new JLabel("Small Car Prob:");
        JLabel simMotoProb = new JLabel("Motorcycle Prob:");
        JLabel simStayMean = new JLabel("Intended Stay Mean:");
        JLabel simStaySD = new JLabel("Intended Stay SD:");

		Box optionsPanel = new Box(BoxLayout.Y_AXIS);
		Box startPanel = new Box(BoxLayout.X_AXIS);
		Box contentPanel = new Box(BoxLayout.X_AXIS);
		
		contentPanel.add(tabbedData);
	
		optionsPanel.add(carParkSize);
		optionsPanel.add(maxCarSpaces);
		
		optionsPanel.add(smallCarParks);
		optionsPanel.add(smallCarSpaces);
		
		optionsPanel.add(motorcycleParks);
		optionsPanel.add(motoSpaces);
		
		optionsPanel.add(carParkQueueSize);
		optionsPanel.add(maxQueueSize);
		
		optionsPanel.add(carParkSimSeed);
		optionsPanel.add(simSeed);
		
		optionsPanel.add(simCarProb);
		optionsPanel.add(carProb);
		
		optionsPanel.add(simSmallProb);
		optionsPanel.add(smallCarProb);
		
		optionsPanel.add(simMotoProb);
		optionsPanel.add(motoProb);
		
		optionsPanel.add(simStayMean);
		optionsPanel.add(intendedStayMean);
		
		optionsPanel.add(simStaySD);
		optionsPanel.add(intendedStaySD);
			
		startPanel.add(Box.createHorizontalGlue());
		startPanel.add(startButton);
		startPanel.add(stopButton);
		startPanel.add(Box.createHorizontalGlue());

		this.getContentPane().add(optionsPanel);
		this.getContentPane().add(contentPanel);

		this.add(startPanel, BorderLayout.SOUTH);
		this.add(optionsPanel, BorderLayout.EAST);
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocation(new Point(125, 50));
		pack();
		setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		JFrame.setDefaultLookAndFeelDecorated(true);
		new GUISimulator(args);
		// TODO Auto-generated method stub
	}

}
