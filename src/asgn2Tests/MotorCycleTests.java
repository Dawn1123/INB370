/**
 * 
 * This file is part of the motorcycleParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * motorcycleParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Vehicles.MotorCycle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {

	private MotorCycle testVehicle;
	
	private final String MOTORCYCLE = "MC1";		 
	
	private final int NORMAL_ARRIVAL = 1,
					  TOO_LOW_ARRIVAL = 0,
					  PARK_ON_ARRIVAL_TIME = NORMAL_ARRIVAL,
					  PARK_LATER = PARK_ON_ARRIVAL_TIME + 1,
					  PARK_TIME_BELOW_ZERO = -1,
					  PARK_AFTER_QUEUE = 26,
					  NORMAL_DURATION = 100,
					  TOO_LOW_DURATION = Constants.MINIMUM_STAY - 5,
					  DEPART_NORMAL_DURATION = NORMAL_ARRIVAL + NORMAL_DURATION,
					  DEPART_EARLY = NORMAL_DURATION / 2,
					  DEPART_BAD_DEPARTURE_TIME = -1,
					  DEPART_QUEUE = NORMAL_ARRIVAL + 25,
					  DEPART_QUEUE_BAD_EXIT_TIME = NORMAL_ARRIVAL - 1,
					  DEPART_LONG_QUEUE = NORMAL_ARRIVAL + 26,
					  DEPART_AFTER_JOURNEY = PARK_AFTER_QUEUE + NORMAL_DURATION;
	
	/*
	 * Before method constructing a new motorcycle
	 * @throws asgn2Exceptions.VehicleException
	 */
	@Before
	public void constructMotorCycle() throws VehicleException {
		testVehicle = new MotorCycle(MOTORCYCLE, NORMAL_ARRIVAL);
	}
	
	//---------------------motorcycle constructor tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * throws exception when arrival time is lower than 0
	 */
	public void tooLowArrival() throws VehicleException {
		testVehicle = new MotorCycle(MOTORCYCLE, TOO_LOW_ARRIVAL);
	}
	
	@Test
	/*
	 * check vehID after constructing
	 */
	public void getVehID() {
		assertEquals(testVehicle.getVehID(), MOTORCYCLE);
	}
	
	@Test
	/*
	 * check arrival time after constructing
	 */
	public void getArrivalTime() {
		assertEquals(testVehicle.getArrivalTime(), NORMAL_ARRIVAL);
	}
	
	
	//---------------------Parked State tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if motorcycle
	 * is already parked
	 */
	public void alreadyParkedParkedTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if motorcycle
	 * has not left the queue yet
	 */
	public void stillInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.enterParkedState(PARK_LATER, NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if intended duration
	 * is lower than minimum
	 */
	public void parkDurationTooLow() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, TOO_LOW_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if the parking time
	 * is below 0
	 */
	public void parkArrivalBelowZero() throws VehicleException {
		testVehicle.enterParkedState(PARK_TIME_BELOW_ZERO, NORMAL_DURATION);
	}
	
	@Test
	/*
	 * check parking time after vehicle has parked
	 */
	public void vehicleParkTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.getParkingTime(), PARK_ON_ARRIVAL_TIME);
	}
	
	@Test
	/*
	 * Test if vehicle is in parked state after enterParkedState() func is called
	 */
	public void vehicleParked() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.isParked(), true);
	}
	
	@Test
	/*
	 * Test if vehicle was parked after enterParkedState() func is called
	 */
	public void vehicleWasParked() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.wasParked(), true);
	}
	
	@Test
	/*
	 * Test if intended departure time is returned after parking
	 */
	public void vehicleIntendedDepartureTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.getDepartureTime(), PARK_ON_ARRIVAL_TIME + NORMAL_DURATION);
	}
	
	@Test
	/*
	 * Test that customer is satisfied after parking
	 */
	public void vehicleParkedCustomerSatisfied() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	//---------------------Queued State tests----------------------\\

	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to queue if motorcycle
	 * is already queued
	 */
	public void alreadyQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to queue if motorcycle
	 * is already parked
	 */
	public void alreadyParkedQueueTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.enterQueuedState();
	}
	
	@Test
	/*
	 * check that vehicle is in queue after enterQueuedState() func is called
	 */
	public void vehicleQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.isQueued(), true);
	}
	
	@Test
	/*
	 * check that vehicle was in queue after enterQueuedState() func is called
	 */
	public void vehicleWasQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.wasQueued(), true);
	}
	
	@Test
	/*
	 * check that customer is satisfied during the queue
	 */
	public void customerSatisfiedInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	//---------------------exit parked state tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is not parked
	 */
	public void notParkedExitParkTest() throws VehicleException {
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is in a queued state
	 */
	public void departParkStateInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when departure time is less than parking time
	 */
	public void departParkStateBadDeparture() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_BAD_DEPARTURE_TIME);
	}
	
	@Test
	/*
	 * check departure time after leaving the park
	 */
	public void vehicleDepartureTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, DEPART_NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.getDepartureTime(), DEPART_NORMAL_DURATION);
	}
	
	@Test
	/*
	 * check that motorcycle is no longer parked after exiting
	 */
	public void departedParkTrue() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.isParked(), false);
	}
	
	@Test
	/*
	 * check that motorcycle was parked after exiting parked state
	 */
	public void departedParkWasParkTrue() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.wasParked(), true);
	}
	
	@Test
	/*
	 * check customer sattisfied after leaving park
	 */
	public void customerSattisfied() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	@Test
	/*
	 * check vehicle is able to leave parked state early
	 * and departure time is still correct
	 */
	public void departParkEarlyDepartureTimeAdjusted() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_EARLY);
		assertEquals(testVehicle.getDepartureTime(), DEPART_EARLY);
	}
	
	//---------------------exit queued state tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is not in the queue
	 */
	public void notQueuedExitQueueTest() throws VehicleException {
		testVehicle.exitQueuedState(DEPART_QUEUE);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is parked
	 */
	public void vehicleParkedExitQueueTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitQueuedState(DEPART_QUEUE);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to exit queue if exitTime
	 * is not later than arrivalTime
	 */
	public void departQueueBadExitTime() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE_BAD_EXIT_TIME);
	}
	
	@Test
	/*
	 * check that motorcycle is no longer queued after exiting
	 */
	public void departedQueueTrue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE);
		assertEquals(testVehicle.isQueued(), false);
	}
	
	@Test
	/*
	 * check that motorcycle was queued after leaving queue
	 */
	public void departedQueueWasQueuedTrue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE);
		assertEquals(testVehicle.wasQueued(), true);
	}
	
	@Test
	/*
	 * customer dissatisfied after being in queue too long
	 */
	public void dissatisfiedCustomerLongQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_LONG_QUEUE);
		assertEquals(testVehicle.isSatisfied(), false);
	}
	
	//---------------------toString motorcycle tests----------------------\\
	@Test
	/*
	 * check vehicle string is correct after a journey through car park
	 */
	public void checkStringCompleteJourney() throws VehicleException {
		String VEHICLE_LOG_FULL_JOURNEY = "Vehicle vehID: " + MOTORCYCLE +
											"\nArrivalTime: " + NORMAL_ARRIVAL +
											"\nExit from Queue: " + DEPART_QUEUE +
											"\nQueuing Time: " + (DEPART_QUEUE - NORMAL_ARRIVAL) +
											"\nEntry to Car Park: " + PARK_AFTER_QUEUE +
											"\nExit from Car Park: " + DEPART_AFTER_JOURNEY +
											"\nParking Time: " + NORMAL_DURATION +
											"\nCustomer was satisfied";								
		
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
		String VEHICLE_LOG_NO_QUEUE = "Vehicle vehID: " + MOTORCYCLE +
										"\nArrivalTime: " + NORMAL_ARRIVAL +
										"\nVehicle was not queued" +
										"\nEntry to Car Park: " + PARK_AFTER_QUEUE +
										"\nExit from Car Park: " + DEPART_AFTER_JOURNEY +
										"\nParking Time: " + NORMAL_DURATION +
										"\nCustomer was satisfied";								

		testVehicle.enterParkedState(PARK_AFTER_QUEUE, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_AFTER_JOURNEY);							
		assertEquals(testVehicle.toString(), VEHICLE_LOG_NO_QUEUE);
	}
	
	@Test
	/*
	 * check vehicle string is correct after a journey through car park
	 * after exiting queue from maximum wait time and not parking
	 */
	public void checkStringNoParkJourney() throws VehicleException {
		String VEHICLE_LOG_NO_PARK = "Vehicle vehID: " + MOTORCYCLE +
										"\nArrivalTime: " + NORMAL_ARRIVAL +
										"\nExit from Queue: " + DEPART_LONG_QUEUE +
										"\nQueuing Time: " + (DEPART_LONG_QUEUE - NORMAL_ARRIVAL) +
										"\nExceeded maximum acceptable queuing time by: " + (DEPART_LONG_QUEUE - Constants.MAXIMUM_QUEUE_TIME) +
										"\nVehicle was not parked" +
										"\nCustomer was not satisfied";
		
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_LONG_QUEUE);
		assertEquals(testVehicle.toString(), VEHICLE_LOG_NO_PARK);
	}
	
	@Test
	/*
	 * check vehicle string is correct after being turned away
	 * immediately from the car park
	 */
	public void checkStringTurnedAway() throws VehicleException {
		String VEHICLE_TURNED_AWAY = "Vehicle vehID: " + MOTORCYCLE +
										"\nArrivalTime: " + NORMAL_ARRIVAL +
										"\nVehicle was not queued" +
										"\nVehicle was not parked" +
										"\nCustomer was not satisfied";
		
		assertEquals(testVehicle.toString(), VEHICLE_TURNED_AWAY);
	}
}
