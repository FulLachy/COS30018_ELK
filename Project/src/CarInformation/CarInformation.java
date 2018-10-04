package CarInformation;

import CarCharge.CarAgent;
//import car stuff some how
/*
 * This class contains the details of the car that are needed by the MSA for scheduling
 */

public class CarInformation {
	public carType type;  //enum stuff
	public int carId;
	public double reqStartTime = 0; //preferred start time to start charging from car 
	public double reqFinishTime = 0; //time car needs to be finished by 
	public double reqMinCharge = 0; //minimum charge for car to have before it can leave
	//type of car also depends on car, add this in later
	
	public CarInformation(carType t, int id, double startTime, double finishTime, double minCharge) {
		type = t;
		carId = id;
		reqStartTime = startTime;
		reqFinishTime = finishTime;
		reqMinCharge = minCharge;
	}

}
