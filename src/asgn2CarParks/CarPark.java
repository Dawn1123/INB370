/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * The CarPark class provides a range of facilities for working with a car park in support 
 * of the simulator. In particular, it maintains a collection of currently parked vehicles, 
 * a queue of vehicles wishing to enter the car park, and an historical list of vehicles which 
 * have left or were never able to gain entry. 
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars and motorcycles 
 * and their access to the car park. See the method javadoc for details. 
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a series of reports 
 * used by the logger. 
 * 
 * @author hogan
 *
 */
public class CarPark {

	private int carSpacesMax;
    private int smallCarSpacesMax; 
    private int motorCycleSpacesMax;
    private int carSpacesEmpty;
    private int smallCarSpacesEmpty;
    private int motorCycleSpacesEmpty;
    private int totalSpaces;
    private int queueSpacesMax;
    private int vehicleCount = 1;
    private Vehicle currentVehicle;
    private ArrayList<Vehicle> spaces; 
    private ArrayList<Vehicle> vehicleArchive;
    private ArrayBlockingQueue<Vehicle> vehicleQueue;
    private ArrayList<Vehicle> vehiclesToModify;
	
    /**
	 * CarPark constructor sets the basic size parameters. 
	 * Uses default parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,Constants.DEFAULT_MAX_QUEUE_SIZE);
	}
	
	/**
	 * CarPark constructor sets the basic size parameters. 
	 * @param maxCarSpaces maximum number of spaces allocated to cars in the car park 
	 * @param maxSmallCarSpaces maximum number of spaces (a component of maxCarSpaces) 
	 * 						 restricted to small cars
	 * @param maxMotorCycleSpaces maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces,int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		carSpacesMax = maxCarSpaces;
		smallCarSpacesMax = maxSmallCarSpaces;
		motorCycleSpacesMax = maxMotorCycleSpaces;
		queueSpacesMax = maxQueueSize;
		vehiclesToModify = new ArrayList<Vehicle>();
		
	    carSpacesEmpty = maxCarSpaces;
	    smallCarSpacesEmpty = maxSmallCarSpaces;
	    motorCycleSpacesEmpty = maxMotorCycleSpaces;
		
		//calculate total number of spaces
		totalSpaces = carSpacesMax + smallCarSpacesMax + motorCycleSpacesMax;
		
		//initialise ArrayList of Vehicles for carpark called spaces
		spaces = new ArrayList<Vehicle>(totalSpaces);
		
		//initialise ArrayList of Vehicles for archive called vehicleArchive
		vehicleArchive = new ArrayList<Vehicle>();
		
		//initialise ArrayList of Vehicles for archive called vehicleQueue
		vehicleQueue = new ArrayBlockingQueue<Vehicle>(queueSpacesMax);
	}

	/**
	 * Archives vehicles exiting the car park after a successful stay. Includes transition via 
	 * Vehicle.exitParkedState(). 
	 * @param time int holding time at which vehicle leaves
	 * @param force boolean forcing departure to clear car park 
	 * @throws VehicleException if vehicle to be archived is not in the correct state 
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied
	 */
	public void archiveDepartingVehicles(int time,boolean force) throws VehicleException, SimulationException {
			int departureTime;
			int currentTime = time;
			
			//if carpark is empty throw simulationException
			if (spaces.isEmpty()) { 
				throw new SimulationException("no vehicles in carpark"); 
			} else {
				//if force is true add all vehicles in carpark, to list to be removed
				if (force) {
					for(Vehicle currentVehicle : spaces){ 
						vehiclesToModify.add(currentVehicle);
					}
					
				} else {
					//if force is false for every vehicle over time, add to list to remove
					for(Vehicle currentVehicle : spaces){ 
						departureTime = currentVehicle.getDepartureTime();
						if (currentTime >= departureTime) {
							vehiclesToModify.add(currentVehicle);
						}
					}
				}
				//remove all vehicles identified as being due to depart
				for(Vehicle overDueVehicle : vehiclesToModify){
					departureTime = overDueVehicle.getDepartureTime();
					unparkVehicle(overDueVehicle, departureTime);
				}	
			}
	}
		
	/**
	 * Method to archive new vehicles that don't get parked or queued and are turned 
	 * away
	 * @param v Vehicle to be archived
	 * @throws SimulationException if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException {
		v.isSatisfied();//change to unsatisfied
		vehicleArchive.add(v);//add vehicle to archive
	}
	
	/**
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 */
	public void archiveQueueFailures(int time) throws VehicleException {
	}
	
	/**
	 * @author Lucas
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() {
		boolean carparkEmpty;
		carparkEmpty = spaces.isEmpty();
		return carparkEmpty;
	}
	
	/**
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		boolean motorCycleSpacesFull = false;
		boolean carSpacesFull = false;
		boolean smallCarSpacesFull = false;
		boolean full = false;
		
		//check if each category of carpark is full
		if (carSpacesEmpty == 0) {
			carSpacesFull = true;
		}
		if (smallCarSpacesEmpty == 0) {
			smallCarSpacesFull = true;
		}
		if (motorCycleSpacesEmpty == 0) {
			motorCycleSpacesFull = true;
		}
		if (carSpacesFull && smallCarSpacesFull && motorCycleSpacesFull) {
			full = true;
		}
		
		return full;
	}
	
	/**
	 * Method to add vehicle successfully to the queue
	 * Precondition is a test that spaces are available
	 * Includes transition through Vehicle.enterQueuedState 
	 * @param v Vehicle to be added 
	 * @throws SimulationException if queue is full  
	 * @throws VehicleException if vehicle not in the correct state 
	 */
	public void enterQueue(Vehicle v) throws SimulationException, VehicleException {
	}
	
	
	/**
	 * Method to remove vehicle from the queue after which it will be parked or 
	 * removed altogether. Includes transition through Vehicle.exitQueuedState.  
	 * @param v Vehicle to be removed from the queue 
	 * @param exitTime int time at which vehicle exits queue
	 * @throws SimulationException if vehicle is not in queue 
	 * @throws VehicleException if the vehicle is in an incorrect state or timing 
	 * constraints are violated
	 */
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException {
	}
	
	/**
	 * State dump intended for use in logging the final state of the carpark
	 * All spaces and queue positions should be empty and so we dump the archive
	 * @return String containing dump of final carpark state 
	 */
	public String finalState() {
		String str = "Vehicles Processed: count:" + 
				this.count + ", logged: " + this.past.size() 
				+ "\nVehicle Record: \n";
		for (Vehicle v : this.past) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}
	
	/**
	 * Simple getter for number of cars in the car park 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		int carCount = 0; 
		for(Vehicle currentVehicle : spaces){
		    if (currentVehicle instanceof Car) {
		    	carCount += 1;
		    } 		
		}
		return carCount;
	}
	
	/**
	 * Simple getter for number of motorcycles in the car park 
	 * @return number of MotorCycles in car park, including those occupying 
	 * 			a small car space
	 */
	public int getNumMotorCycles() {
		int MotorCycleCount = 0; 
		for(Vehicle currentVehicle : spaces){
			if (currentVehicle instanceof MotorCycle) {
				MotorCycleCount += 1;
			} 		
		}
		return MotorCycleCount;
	}
	
	
	/**
	 * Simple getter for number of small cars in the car park 
	 * @return number of small cars in car park, including those 
	 * 		   not occupying a small car space. 
	 */
	public int getNumSmallCars() {
		int SmallCarCount = 0; 
		for(Vehicle currentVehicle : spaces){
			if (currentVehicle instanceof Car) {
				if(((Car) currentVehicle).isSmall()){
					SmallCarCount += 1;
				}
			} 		
		}
		return SmallCarCount;
	}
	
	/**
	 * Method used to provide the current status of the car park. 
	 * Uses private status String set whenever a transition occurs. 
	 * Example follows (using high probability for car creation). At time 262, 
	 * we have 276 vehicles existing, 91 in car park (P), 84 cars in car park (C), 
	 * of which 14 are small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D),
	 * 176 archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have 
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to Parked (P),
	 * and small car S arrive (new N) and go straight into the car park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||S:N>P|
	 * @return String containing current state 
	 */
	public String getStatus(int time) {
		String str = time +"::"
		+ this.count + "::" 
		+ "P:" + this.spaces.size() + "::"
		+ "C:" + this.numCars + "::S:" + this.numSmallCars 
		+ "::M:" + this.numMotorCycles 
		+ "::D:" + this.numDissatisfied 
		+ "::A:" + this.past.size()  
		+ "::Q:" + this.queue.size(); 
		for (Vehicle v : this.queue) {
			if (v instanceof Car) {
				if (((Car)v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		str += this.status;
		this.status="";
		return str+"\n";
	}
	

	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters. 
	 * @return String containing dump of initial carpark state 
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.carSpacesMax
				+ " maxSmallCarSpaces: " + this.smallCarSpacesMax 
				+ " maxMotorCycleSpaces: " + this.motorCycleSpacesMax 
				+ " maxQueueSize: " + this.queueSpacesMax + "]";
	}

	/**
	 * Simple status showing number of vehicles in the queue 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
	
	}
	
	/**
	 * Method to add vehicle successfully to the car park store. 
	 * Precondition is a test that spaces are available. 
	 * Includes transition via Vehicle.enterParkedState.
	 * @param v Vehicle to be added 
	 * @param time int holding current simulation time
	 * @param intendedDuration int holding intended duration of stay 
	 * @throws SimulationException if no suitable spaces are available for parking 
	 * @throws VehicleException if vehicle not in the correct state or timing constraints are violated
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration) throws SimulationException, VehicleException {
		boolean emptySuitableSpaces = true;
		emptySuitableSpaces = spacesAvailable(v);
		
		if (!emptySuitableSpaces) { //check if carpark contains no spaces for the current vehicle
			throw new SimulationException("parkvehicle called when carpark full"); //if full display error message
		} else { //otherwise park vehicle	
			//check status of car
			//remove from current list
			spaces.add(v); //add to spaces
			v.enterParkedState(time, intendedDuration);//update status to is parked
			parkedVehicleUpdateCount(v);//update parked vehicle count
		}
	}  

	/**
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException, SimulationException {
	
	}

	/**
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
	}

	/**
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
	}
	
	/**
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) {
		int spacesCount = 0;
		boolean spacesRemaining = false;
		if (v instanceof Car) {
			if(((Car) v).isSmall()){
				spacesCount = carSpacesEmpty + smallCarSpacesEmpty;
			} else {
				spacesCount = carSpacesEmpty;	
			}
		} else if (v instanceof MotorCycle) {
				spacesCount = motorCycleSpacesEmpty + smallCarSpacesEmpty;
		}
		if (spacesCount > 0) {
			spacesRemaining = true;
		}
		return spacesRemaining;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	}

	/**
	 * Method to try to create new vehicles (one trial per vehicle type per time point) 
	 * and to then try to park or queue (or archive) any vehicles that are created 
	 * @param sim Simulation object controlling vehicle creation 
	 * @throws SimulationException if no suitable spaces available when operation attempted 
	 * @throws VehicleException if vehicle creation violates constraints 
	 */
	public void tryProcessNewVehicles(int time,Simulator sim) throws VehicleException, SimulationException {
		boolean createCar = false;
		boolean createSmallCar = false;
		boolean createMotorCycle = false;
		String vehicleID;
		int durationOfStay;
		
		//run trials to see what vehicles are created
		createCar = sim.newCarTrial();
		createSmallCar = sim.smallCarTrial();
		createMotorCycle = sim.motorCycleTrial();
		
		//check for car creation
		if (createCar) {
			vehicleID = Integer.toString(vehicleCount);
			vehicleCount++;
			durationOfStay = sim.setDuration();
			
			//create a small car
			//park vehicle if spaces available, if not archive vehicle
			if (createSmallCar) {
				currentVehicle = new Car(vehicleID, time, true); 
				if (spacesAvailable(currentVehicle)) {
					parkVehicle(currentVehicle, time, durationOfStay);
				} else {
					archiveNewVehicle(currentVehicle);
				}
			
			//create a normal car
			//park vehicle if spaces available, if not archive vehicle
			} else {
				currentVehicle = new Car(vehicleID, time, false);
				if (spacesAvailable(currentVehicle)) {
					parkVehicle(currentVehicle, time, durationOfStay);
				} else {
					archiveNewVehicle(currentVehicle);
				}
			}
		}
		
		//check for motorcycle creation
		if (createMotorCycle) {
			//create a motorCycle
			//park vehicle if spaces available, if not archive vehicle
			vehicleID = Integer.toString(vehicleCount);
			vehicleCount++;
			durationOfStay = sim.setDuration();
			currentVehicle = new MotorCycle(vehicleID, time); //create a motorcycle
			if (spacesAvailable(currentVehicle)) {
				parkVehicle(currentVehicle, time, durationOfStay);
			} else {
				archiveNewVehicle(currentVehicle);
			}
		}
		
	}

	/**
	 * Method to remove vehicle from the carpark. 
	 * For symmetry with parkVehicle, include transition via Vehicle.exitParkedState.  
	 * So vehicle should be in parked state prior to entry to this method. 
	 * @param v Vehicle to be removed from the car park 
	 * @throws VehicleException if Vehicle is not parked, is in a queue, or violates timing constraints 
	 * @throws SimulationException if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v,int departureTime) throws VehicleException, SimulationException {
		boolean vehicleInCarpark = false;
		vehicleInCarpark = spaces.contains(v);
		
		//throw a simulation exception if vehicle isnt present in carpark
		if (!vehicleInCarpark) { 
			throw new SimulationException("unparkVehicle called when vehicle is not present in carpark"); 
		} else { 
			spaces.remove(v); //remove from spaces
			v.isSatisfied();//change to unsatisfied
			vehicleArchive.add(v); //add to vehicleArchive
			v.exitParkedState(departureTime); //update status to unparked
			UnparkedVehicleUpdateCount(v); //update parked vehicle count
		}
	}
	

	
	
	/**
	 * Helper to set vehicle message for transitions 
	 * @param v Vehicle making a transition (uses S,C,M)
	 * @param source String holding starting state of vehicle (N,Q,P,A) 
	 * @param target String holding finishing state of vehicle (Q,P,A) 
	 * @return String containing transition in the form: |(S|C|M):(N|Q|P|A)>(Q|P|A)| 
	 */
	private String setVehicleMsg(Vehicle v,String source, String target) {
		String str="";
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				str+="S";
			} else {
				str+="C";
			}
		} else {
			str += "M";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
	
	/**
	 * Simple status showing whether vehicle can be parked
	 * @return true if vehicle can be parked in suitable space
	 */
	private boolean checkSpacesAvailable() {
	return true;
	}
	
	/**
	 * @author Lucas
	 * Method used to update number of parks available when vehicle parked
	 */
	private void parkedVehicleUpdateCount(Vehicle v) {
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				smallCarSpacesEmpty--;
			} else {
				carSpacesEmpty--;
			}
		} else {
			motorCycleSpacesEmpty--;
		}
	}
	
	/**
	 * @author Lucas
	 * Method used to update number of parks available when vehicle unparked
	 */
	private void UnparkedVehicleUpdateCount(Vehicle v) {
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				smallCarSpacesEmpty--;
			} else {
				carSpacesEmpty--;
			}
		} else {
			motorCycleSpacesEmpty--;
		}
	}
}

