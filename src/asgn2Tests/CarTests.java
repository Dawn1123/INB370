/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Vehicles.Car;

/**
 * @author hogan
 *
 */
public class CarTests {

	private Car testVehicle;
	
	private final String FIRST_CAR = "C1";		 
	
	private final int NORMAL_ARRIVAL = 1,
			  		  PARK_AFTER_QUEUE = 26,
			  		  NORMAL_DURATION = 100,
			  		  DEPART_QUEUE = NORMAL_ARRIVAL + 25,
			  		  DEPART_AFTER_JOURNEY = PARK_AFTER_QUEUE + NORMAL_DURATION;
	
	private final boolean IS_NOT_SMALL = false,
						  IS_SMALL = true;
	
	/*
	 * Before method constructing a new normal car
	 * @throws asgn2Exceptions.VehicleException
	 */
	@Before
	public void constructCar() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, NORMAL_ARRIVAL, IS_NOT_SMALL);
	}
	
	//---------------------Car constructor tests----------------------\\
	
	@Test
	/*
	 * check that the car is not small after false given in construct
	 */
	public void checkNotSmall() {
		assertEquals(testVehicle.isSmall(), false);
	}
	
	@Test
	/*
	 * check that the car is small after true given in construct
	 */
	public void checkIsSmall() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, NORMAL_ARRIVAL, IS_SMALL);
		assertEquals(testVehicle.isSmall(), true);
	}
	
	//---------------------toString car tests----------------------\\
	@Test
	/*
	 * check vehicle string is correct after a journey through car park
	 */
	public void checkStringCompleteJourney() throws VehicleException {
		String VEHICLE_LOG_FULL_JOURNEY = "Vehicle vehID: " + FIRST_CAR +
											"\nArrivalTime: " + NORMAL_ARRIVAL +
											"\nExit from Queue: " + DEPART_QUEUE +
											"\nQueuing Time: " + (DEPART_QUEUE - NORMAL_ARRIVAL) +
											"\nEntry to Car Park: " + PARK_AFTER_QUEUE +
											"\nExit from Car Park: " + DEPART_AFTER_JOURNEY +
											"\nParking Time: " + NORMAL_DURATION +
											"\nCustomer was satisfied" +
											"\nCar cannot use small parking space";
		
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE);
		testVehicle.enterParkedState(PARK_AFTER_QUEUE, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_AFTER_JOURNEY);
		assertEquals(testVehicle.toString(), VEHICLE_LOG_FULL_JOURNEY);
	}
	
	@Test
	/*
	 * check vehicle string is correct after a journey through car park
	 * without entering queue
	 */
	public void checkStringNoQueueJourney() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, NORMAL_ARRIVAL, IS_SMALL);
		String VEHICLE_LOG_NO_QUEUE = "Vehicle vehID: " + FIRST_CAR +
										"\nArrivalTime: " + NORMAL_ARRIVAL +
										"\nVehicle was not queued" +
										"\nEntry to Car Park: " + PARK_AFTER_QUEUE +
										"\nExit from Car Park: " + DEPART_AFTER_JOURNEY +
										"\nParking Time: " + NORMAL_DURATION +
										"\nCustomer was satisfied" +
										"\nCar can use small parking space";

		testVehicle.enterParkedState(PARK_AFTER_QUEUE, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_AFTER_JOURNEY);							
		assertEquals(testVehicle.toString(), VEHICLE_LOG_NO_QUEUE);
	}
}
