package Algorithm;

import java.util.LinkedList;
import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class Schedule {
	
	//List of ChargeStations
	private int totalStations = 0;
	public LinkedList<ChargeStation> chargeStations = new LinkedList<ChargeStation>();
	
	public Schedule(int numStations)
	{
		totalStations = numStations; 
		
		for (int i = 0; i<totalStations; i++)
		{
			chargeStations.add(new ChargeStation(i+1, StationType.Fast));
		}
	}
	
	public Schedule Clone()
	{
		Schedule clone = new Schedule(totalStations);
		
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
