package Algorithm;

import java.util.LinkedList;

import CarCharge.CarData;

public class ChargeStation {

	//Public Variables
	public int chargerNumber = 0; //Unique Identifier
	public StationType stationType; //Station type
	public LinkedList<ScheduledCar> allotedCars = new LinkedList<ScheduledCar>();	//List of cars that will be or have been assigned 
	
	//Instantiation 
	public ChargeStation(int num, StationType type) {
		chargerNumber = num;
		stationType = type;
	}
	
	//Was to be used for consisntency
	public void OrderByStartTime()
	{
		for (int i = 0; i < allotedCars.size(); i++)
		{
			int count = allotedCars.size() - 1;
			while(count > 0)
			{
				for (int a = 0; a < count; a++)
				{
					ScheduledCar test1 = allotedCars.get(a);
					ScheduledCar test2 = allotedCars.get(a+1); 
					
					if(test1.startRequested > test2.startRequested)
					{
						ScheduledCar swap = test1;
						test1 = test2;
						test2 = swap;
					}
					
					allotedCars.set(i,test1);
					allotedCars.set(i+1, test2);
				}
				
				count--;
			}
		}
	}
	
	//For finding the highest possible starting time dependant on station type and car type
	public double GetMaxStartTime(CarData cd)
	{
		double hourlyCharge = 0;
		switch(stationType)
		{
			case Slow:
				hourlyCharge = 0.2;
			case Medium:
				hourlyCharge = 0.25;
			case Fast:
				hourlyCharge = 0.5;
		}
		
		switch(cd.type)
		{
			case Small:
				if (hourlyCharge >= 0.2)
					hourlyCharge = 0.2;
			case Medium:
				if (hourlyCharge >= 0.25)
					hourlyCharge = 0.25;
			case Large:
				if (hourlyCharge >= 0.5)
					hourlyCharge = 0.5;
		}
		
		hourlyCharge = cd.reqMinCharge / hourlyCharge;
		return cd.reqFinishTime - hourlyCharge;
		
	}
	
	//Cloning function for PSO or possible Genetic Algorithm
	public ChargeStation Clone()
	{
		ChargeStation clone = new ChargeStation(chargerNumber, stationType);
		
		for (int i = 0; i< allotedCars.size(); i++)
		{
			//clone.allotedCars.add(this.allotedCars.get(i).Clone());
		}
		return clone;
	}

}
