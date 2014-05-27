/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Vehicles 
 * 19/04/2014
 * 
 */
package asgn2Vehicles;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;

/**
 * Vehicle is an abstract class specifying the basic state of a vehicle and the
 * methods used to set and access that state. A vehicle is created upon arrival,
 * at which point it must either enter the car park to take a vacant space or
 * become part of the queue. If the queue is full, then the vehicle must leave
 * and never enters the car park. The vehicle cannot be both parked and queued
 * at once and both the constructor and the parking and queuing state transition
 * methods must respect this constraint.
 * 
 * Vehicles are created in a neutral state. If the vehicle is unable to park or
 * queue, then no changes are needed if the vehicle leaves the carpark
 * immediately. Vehicles that remain and can't park enter a queued state via
 * {@link #enterQueuedState() enterQueuedState} and leave the queued state via
 * {@link #exitQueuedState(int) exitQueuedState}. Note that an exception is
 * thrown if an attempt is made to join a queue when the vehicle is already in
 * the queued state, or to leave a queue when it is not.
 * 
 * Vehicles are parked using the {@link #enterParkedState(int, int)
 * enterParkedState} method and depart using {@link #exitParkedState(int)
 * exitParkedState}
 * 
 * Note again that exceptions are thrown if the state is inappropriate: vehicles
 * cannot be parked or exit the car park from a queued state.
 * 
 * The method javadoc below indicates the constraints on the time and other
 * parameters. Other time parameters may vary from simulation to simulation and
 * so are not constrained here.
 * 
 * @author hogan
 * 
 */
public abstract class Vehicle {

	//private class variables to hold data on each object made
	private String vehID;

	private int arrivalTime, 
				parkingTime = 0, 
				departureTime = 0,
				totalParkingTime = 0, 
				queueExitTime = 0, 
				queuingTime = 0,
				maximumQueueTime;

	private Boolean isParked = false, 
					isQueued = false, 
					wasParked = false,
					wasQueued = false, 
					isSatisfied = false;

	/**
	 * Vehicle Constructor
	 * 
	 * @param vehID
	 *            String identification number or plate of the vehicle
	 * @param arrivalTime
	 *            int time (minutes) at which the vehicle arrives and is either
	 *            queued, given entry to the car park or forced to leave
	 * @throws VehicleException
	 *             if arrivalTime is <= 0
	 */
	public Vehicle(String vehID, int arrivalTime) throws VehicleException {
		// determine if arrivalTime is correct
		if (arrivalTime > 0) {
			// store object variables within class
			this.arrivalTime = arrivalTime;
			this.vehID = vehID;
		} else {
			// throw exception if not with value provided shown to user
			throw new VehicleException(
					"Vehicle arrival time cannot be less than 0"
							+ ", value provided: " + arrivalTime);
		}
	}

	/**
	 * Transition vehicle to parked state (mutator) Parking starts on arrival or
	 * on exit from the queue, but time is set here
	 * 
	 * @param parkingTime
	 *            int time (minutes) at which the vehicle was able to park
	 * @param intendedDuration
	 *            int time (minutes) for which the vehicle is intended to remain
	 *            in the car park. Note that the parkingTime + intendedDuration
	 *            yields the departureTime
	 * @throws VehicleException
	 *             if the vehicle is already in a parked or queued state, if
	 *             parkingTime < 0, or if intendedDuration is less than the
	 *             minimum prescribed in asgnSimulators.Constants
	 */
	public void enterParkedState(int parkingTime, int intendedDuration)
			throws VehicleException {
		
		//check for current vehicle state
		if (this.isParked() || this.isQueued()) {
			throw new VehicleException(
					"Vehicle cannot enter parked state when already parked, or still in the queue");
		}
		
		//check parkingTime is not negative
		if (parkingTime < 0) {
			throw new VehicleException(
					"Vehicles parking time cannot be less than 0, value provided: "
							+ parkingTime);
		}
		
		//check duration is longer than minimum duration
		if (intendedDuration < Constants.MINIMUM_STAY) {
			throw new VehicleException(
					"Vehicles intended duration cannot be less than 20 minutes after parking time, parking time: "
							+ parkingTime
							+ "intended duration: "
							+ intendedDuration);
		}
		
		//store object variables if all criteria are met and transition
		//vehicle to parked state
		this.isSatisfied = true;
		this.parkingTime = parkingTime;
		this.isParked = true;
		this.wasParked = true;
		this.departureTime = parkingTime + intendedDuration;
	}

	/**
	 * Transition vehicle to queued state (mutator) Queuing formally starts on
	 * arrival and ceases with a call to {@link #exitQueuedState(int)
	 * exitQueuedState}
	 * 
	 * @throws VehicleException
	 *             if the vehicle is already in a queued or parked state
	 */
	public void enterQueuedState() throws VehicleException {
		//check vehicle states
		if (this.isQueued()) {
			throw new VehicleException(
					"Vehicle cannot enter a queued state when already in the queue");
		}

		if (this.isParked()) {
			throw new VehicleException(
					"Vehicle cannot enter a queued state when already parked");
		}
		
		//if vehicle states are satisfied transition vehicle to queued state
		//and store object variables
		this.isSatisfied = true;
		this.isQueued = true;
		this.wasQueued = true;
		this.maximumQueueTime = this.arrivalTime + Constants.MAXIMUM_QUEUE_TIME;
	}

	/**
	 * Transition vehicle from parked state (mutator)
	 * 
	 * @param departureTime
	 *            int holding the actual departure time
	 * @throws VehicleException
	 *             if the vehicle is not in a parked state, is in a queued state
	 *             or if the revised departureTime < parkingTime
	 */
	public void exitParkedState(int departureTime) throws VehicleException {
		//check vehicle current state
		if (!this.isParked()) {
			throw new VehicleException(
					"Vehicle cannot leave a parked state if it is not in a parked state");
		}

		if (this.isQueued()) {
			throw new VehicleException(
					"Vehicle cannot leave a parked state if it's in a queued state");
		}
		
		//check departureTime is not lower than parkingTime
		if (this.parkingTime > departureTime) {
			throw new VehicleException(
					"Vehicles departure time cannot be less than the time it parked, parking time: "
							+ this.parkingTime
							+ "departure time: "
							+ departureTime);
		}
		
		//transition vehicle out of parked state and store object variables
		this.isParked = false;
		this.departureTime = departureTime;
		this.totalParkingTime = departureTime - this.parkingTime;
	}

	/**
	 * Transition vehicle from queued state (mutator) Queuing formally starts on
	 * arrival with a call to {@link #enterQueuedState() enterQueuedState} Here
	 * we exit and set the time at which the vehicle left the queue
	 * 
	 * @param exitTime
	 *            int holding the time at which the vehicle left the queue
	 * @throws VehicleException
	 *             if the vehicle is in a parked state or not in a queued state,
	 *             or if exitTime is not later than arrivalTime for this vehicle
	 */
	public void exitQueuedState(int exitTime) throws VehicleException {
		//check for vehicles current state
		if (this.isParked()) {
			throw new VehicleException(
					"Vehicle cannot be parked and leave the queue");
		}

		if (!this.isQueued()) {
			throw new VehicleException(
					"Vehicle must be in the queue before it can leave the queue");
		}
		
		//check exitTime is later than arrival time
		if (this.arrivalTime > exitTime) {
			throw new VehicleException(
					"Vehicle exit time cannot be earlier than the arrival time, exit time: "
							+ exitTime + "arrival time: " + this.arrivalTime);
		}
		
		if (exitTime > this.maximumQueueTime) {
			//change customers state to dissatisfied
			this.isSatisfied = false;
			this.departureTime = exitTime;
		}
		
		//transition vehicle out of queue and store object variables
		this.isQueued = false;
		this.queueExitTime = exitTime;
		this.queuingTime = exitTime - this.arrivalTime;
	}

	/**
	 * Simple getter for the arrival time
	 * 
	 * @return the arrivalTime
	 */
	public int getArrivalTime() {
		//return current objects arrivalTime
		return this.arrivalTime;
	}

	/**
	 * Simple getter for the departure time from the car park Note: result may
	 * be 0 before parking, show intended departure time while parked; and
	 * actual when archived
	 * 
	 * @return the departureTime
	 */
	public int getDepartureTime() {
		//return current objects departureTime
		return this.departureTime;
	}

	/**
	 * Simple getter for the parking time Note: result may be 0 before parking
	 * 
	 * @return the parkingTime
	 */
	public int getParkingTime() {
		//return current objects parkingTime
		return this.parkingTime;
	}

	/**
	 * Simple getter for the vehicle ID
	 * 
	 * @return the vehID
	 */
	public String getVehID() {
		//return current objects vehicleID
		return this.vehID;
	}

	/**
	 * Boolean status indicating whether vehicle is currently parked
	 * 
	 * @return true if the vehicle is in a parked state; false otherwise
	 */
	public boolean isParked() {
		//return if vehicle is in a parked state
		return this.isParked;
	}

	/**
	 * Boolean status indicating whether vehicle is currently queued
	 * 
	 * @return true if vehicle is in a queued state, false otherwise
	 */
	public boolean isQueued() {
		//return if vehicle is in a queued state
		return this.isQueued;
	}

	/**
	 * Boolean status indicating whether customer is satisfied or not Satisfied
	 * if they park; dissatisfied if turned away, or queuing for too long Note
	 * that calls to this method may not reflect final status
	 * 
	 * @return true if satisfied, false if never in parked state or if queuing
	 *         time exceeds max allowable
	 */
	public boolean isSatisfied() {
		//return if vehicle is satisfied or not
		return this.isSatisfied;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//create vehicle log of journey through car park
		//add vehicleID and arrivalTime to string
		String vehicleString = "Vehicle vehID: " + this.vehID
								+ "\nArrivalTime: " + this.arrivalTime;
		
		//if statement to determine how vehicles journey went and
		//add appropriate information to the log
		
		if (!this.isSatisfied()) {//if vehicle was dissatisfied
			if (!this.wasQueued()) {//if vehicle was not queued
				vehicleString += "\nVehicle was not queued";
			} else {//vehicle dissatisfied from leaving queue after max queue time
				vehicleString += "\nExit from Queue: " + this.queueExitTime
								+ "\nQueuing Time: " + this.queuingTime
								+ "\nExceeded maximum acceptable queuing time by: " +
								((this.queueExitTime - Constants.MAXIMUM_QUEUE_TIME) - this.arrivalTime);
			}
			//vehicle dissatisfied so was not parked
			vehicleString += "\nVehicle was not parked"
							+ "\nCustomer was not satisfied";
		} else {//vehicle satisfied
			if (!this.wasQueued()) {//vehicle was not queued
				vehicleString += "\nVehicle was not queued";
			} else {
				vehicleString += "\nExit from Queue: " + this.queueExitTime
								+ "\nQueuing Time: " + this.queuingTime;
			}
			//vehicles entry into car park
			vehicleString += "\nEntry to Car Park: " + this.parkingTime
							+ "\nExit from Car Park: " + this.departureTime
							+ "\nParking Time: " + this.totalParkingTime
							+ "\nCustomer was satisfied";
		}
		//return vehicle log
		return vehicleString;
	}

	/**
	 * Boolean status indicating whether vehicle was ever parked Will return
	 * false for vehicles in queue or turned away
	 * 
	 * @return true if vehicle was or is in a parked state, false otherwise
	 */
	public boolean wasParked() {
		//return if vehicle was ever in a parked state or currently in one
		return this.wasParked;
	}

	/**
	 * Boolean status indicating whether vehicle was ever queued
	 * 
	 * @return true if vehicle was or is in a queued state, false otherwise
	 */
	public boolean wasQueued() {
		//return if vehicle was ever in a queued state or currently in one
		return this.wasQueued;
	}
}
