package Algorithm;

import CarInformation.carType;

public class ScheduledCar 
{
	public String id;
	public carType type;
	public StationType preferredStationSlot;
	public float duration = 0;
	public double startRequested =0;
	public double endRequested = 0;
	public double startTime = 0;
	
	public double timeOnCharge;
	
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
	
	public double CalculateTime(StationType sType, double tempStartTime)
	{
		
		
		switch(type)
		{
			case Small:
				//Need to fill information out
				
				
			case Medium:
				
			case Large:
		}
			
		
		return timeOnCharge;
		
	}
}
