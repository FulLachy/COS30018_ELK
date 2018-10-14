package CarCharge;

import Algorithm.StationType;
import CarInformation.carType;

public class CarData {
	public carType type;  //enum stuff
	public int carId;
	public StationType preferredStationSlot;
	public double reqStartTime = 0; //preferred start time to start charging from car 
	public double reqFinishTime = 0; //time car needs to be finished by 
	public double reqMinCharge = 0; //minimum charge for car to have before it can leave

	public CarData()
	{
		if(type == carType.Small)
		{
			preferredStationSlot = StationType.Slow;
		}
		else if(type == carType.Medium)
		{
			preferredStationSlot = StationType.Medium;
		}
		else if(type == carType.Large)
		{
			preferredStationSlot = StationType.Fast;
		}
	}
}
