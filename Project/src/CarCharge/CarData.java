package CarCharge;

import java.io.Serializable;

import Algorithm.StationType;
import CarInformation.carType;

public class CarData{
	public carType type;  //enum stuff
	public String carId;//Unique ID
	public StationType preferredStationSlot; //preferred station slot
	public double reqStartTime = 0; //preferred start time to start charging from car 
	public double reqFinishTime = 0; //time car needs to be finished by 
	public double reqMinCharge = 0; //minimum charge for car to have before it can leave

	//assignment of a station slot just in case
	public void FindPreferenceSlot()
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
