package Algorithm;

import java.util.LinkedList;

import CarCharge.CarData;

public class ChargeStation {

	private int chargerNumber = 0;
	public StationType stationType;
	
	public LinkedList<ScheduledCar> allotedCars = new LinkedList<ScheduledCar>();	
	
	public ChargeStation(int num, StationType type) {
		chargerNumber = num;
		stationType = type;
	}
	
	
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
