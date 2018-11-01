package Algorithm;

import java.util.LinkedList;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class Schedule {
	
	//List of ChargeStations
	private int totalStations = 0;
	
	public int slow;
	public int medium;
	public int fast;
	
	public LinkedList<ChargeStation> chargeStations = new LinkedList<ChargeStation>();
	
	public Schedule(int numberOfSlow, int numberOfMedium, int numberOfFast)
	{
		totalStations = numberOfSlow + numberOfMedium + numberOfFast;; 
		slow = numberOfSlow;
		medium = numberOfMedium;
		fast = numberOfFast;
		
		int id = 0;
		
		for (int i = 0; i<slow; i++)
		{
			chargeStations.add(new ChargeStation(id, StationType.Slow));
			id++;
		}
		
		for (int o = 0; o<medium;o++)
		{
			chargeStations.add(new ChargeStation(id, StationType.Medium));
			id++;
		}
		
		for (int u = 0; u<fast; u++)
		{
			chargeStations.add(new ChargeStation(id, StationType.Fast));
			id++;
		}
	}
	
	public Schedule Clone()
	{
		Schedule clone = new Schedule(slow,medium,fast);
		
		for (int i = 0; i < totalStations; i++)
		{
			//insert regular stations into clone stations
		}
		
		return clone;
	}
	
	public void RemoveCar(String carID)
	{
		for(ChargeStation charge : chargeStations)
		{
			ScheduledCar removeCar = null;
			for (ScheduledCar car : charge.allotedCars)
			{
				if (car.id == carID)
				{
					removeCar = car;
				}
			}
			if (removeCar != null)
			{
				charge.allotedCars.remove(removeCar);
			}
		}
	}
}
