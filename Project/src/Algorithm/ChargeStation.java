package Algorithm;

import java.util.LinkedList;

public class ChargeStation {

	public int chargerNumber = 0;
	public LinkedList<ScheduledCar> allotedCars = new LinkedList<ScheduledCar>();
	
	
	public ChargeStation(int num) {
		chargerNumber = num;
	}
	
	public ChargeStation Clone()
	{
		ChargeStation clone = new ChargeStation(chargerNumber);
		
		for (int i = 0; i< allotedCars.size(); i++)
		{
			//clone.allotedCars.add(this.allotedCars.get(i).Clone());
		}
		return clone;
	}

}
