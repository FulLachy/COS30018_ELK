package Algorithm;

import java.util.LinkedList;

import CarCharge.CarData;

public class ConstraintSatisfaction {

	private int numStations;
	private boolean firstCarSlotted;
	
	private LinkedList<Schedule> population;
	
	private LinkedList<CarData> CarList;
	//private Schedule schedule = null;

	private LinkedList<CarData> unusedCars;
	
	
	public ConstraintSatisfaction(LinkedList<CarData> list, int stations)
	{
		CarList = list;
		numStations = stations;
	}
	
	private void PopulateSchedule(LinkedList<Schedule> schedule)
	{
		if(schedule == null)
		{
			population = new LinkedList<Schedule>();
			
			//for(int i = 0; i<size; i++)
			//{
				//population.add(CreateSchedule());
			//}
		}
	}
	
	private ScheduledCar GetScheduledCar(int i)
	{
		CarData cd = CarList.get(i);
		ScheduledCar sc = new ScheduledCar();
		
		sc.id = cd.carId;
		sc.type = cd.type;
		sc.startRequested = cd.reqStartTime;
		sc.endRequested = cd.reqFinishTime;
		sc.preferredStationSlot = cd.preferredStationSlot;
		
		return sc;
	}
	
	public void OrderCarsByStartTime()
	{
		for (int i = 0; i < CarList.size(); i++)
		{
			int count = CarList.size() - 1;
			while(count > 0)
			{
				for (int a = 0; a < count; a++)
				{
					CarData test1 = CarList.get(a);
					CarData test2 = CarList.get(a+1); 
					
					if(test1.reqStartTime > test2.reqStartTime)
					{
						CarData swap = test1;
						test1 = test2;
						test2 = swap;
					}
					
					CarList.set(i,test1);
					CarList.set(i+1, test2);
				}
				
				count--;
			}
		}
	}
	
	private Schedule CreateSchedule()
	{
		ChargeStation bestStation = null;
		Schedule s = new Schedule(numStations);
		OrderCarsByStartTime();
		
		for(int i=0; i< CarList.size(); i++)
		{
			ScheduledCar sc = GetScheduledCar(i);
			
			if (firstCarSlotted == false)
			{
				boolean clash = false;
				boolean correctStationType = true;
				
				for(int a=0; a<numStations; a++)
				{
					ChargeStation cs = s.chargeStations.get(a);
					if (cs.allotedCars.size()!= 0)
					{
						if(cs.stationType != sc.preferredStationSlot)
						{
							correctStationType = false;
						}
						else
						{
							for(int e = 0; e < cs.allotedCars.size();e++)
							{
								ScheduledCar other = cs.allotedCars.get(e);
								if(CheckClash(sc, other,cs.stationType))
								{
									clash = true;
									break;
								}
							}
							
							if (!clash)
							{
								sc.startTime = sc.startRequested;
								//cs.allotedCars.add(sc);
								correctStationType = true;
								
								if(bestStation == null)
								{
									bestStation = cs;
								}
								else
								{
									if(ReturnGap(bestStation,sc) > ReturnGap(cs,sc))
									{
										bestStation = cs;
									}
								}
								//break;
							}
						}
						
					}
					else
					{
						correctStationType = true;
						sc.startTime = sc.startRequested;
						cs.allotedCars.add(sc);
					}
					
					if(!correctStationType)
					{
						unusedCars.add(CarList.get(i));
					}
				}
				
				if(clash)
				{
					unusedCars.add(CarList.get(i));				
				}
				else
				{
					for(int a = 0; a < numStations; a++)
					{
						if (bestStation == s.chargeStations.get(a))
						{
							s.chargeStations.get(a).allotedCars.add(sc);
						}
					}
				}
			}	
		}
		
		boolean clash = false;
		
		for(int o =0; o < unusedCars.size(); o++)
		{
			ScheduledCar sc = GetScheduledCar(o);
			for(int a=0; a<numStations; a++)
			{
				ChargeStation cs = s.chargeStations.get(a);
				if (cs.allotedCars.size()!= 0)
				{
					for(int e = 0; e < cs.allotedCars.size();e++)
					{
						ScheduledCar other = cs.allotedCars.get(e);
						if(CheckClash(sc, other,cs.stationType))
						{
							clash = true;
							break;
						}
					}
					
					if (!clash)
					{
						sc.startTime = sc.startRequested;
						cs.allotedCars.add(sc);
						break;
					}
				}
			}
		}
	
		if(clash)
		{
			//int count = 0;
			//while (!)
			//Try and add to another car slot
			
		}
		return s;
	}
		
	private double ReturnGap(ChargeStation cs, ScheduledCar sc)
	{
		ChargeStation temp = cs;
		
		temp.allotedCars.add(sc);
		temp.OrderByStartTime();
		
		for(int i = 0; i< temp.allotedCars.size(); i++)
		{
			if(temp.allotedCars.get(i) == sc)
			{
				ScheduledCar tempCar = temp.allotedCars.get(i-1);
				return (sc.duration - tempCar.duration);
			}
		}
		return 0;
	}
	
	private LinkedList<CarData> OrganiseCarData()
	{
		LinkedList<CarData> tempList = new LinkedList<CarData>();
		//CarList.a
		
		for (int i = 0; i < CarList.size(); i++)
		{
			if (tempList.size() == 0)
			{
				tempList.add(CarList.get(i));
			}
			
		}
		
		return tempList;
	}
	
	private boolean CheckClash(ScheduledCar newCar, ScheduledCar comparingCar, StationType station)
	{
		if(newCar.startRequested == comparingCar.startRequested)
		{
			return true;
		}
		
		double start,end,middleTest;
		
		if(comparingCar.startRequested >= newCar.startRequested)
		{
			start = newCar.startRequested;
			end = newCar.startRequested + newCar.CalculateTime(station, newCar.startRequested);
			if(newCar.endRequested < end)
			{
				end = newCar.endRequested;
			}
			middleTest = comparingCar.startRequested;
		}
		else
		{
			start = comparingCar.startRequested;
			end = comparingCar.startRequested + comparingCar.CalculateTime(station, comparingCar.startRequested);
			if(newCar.endRequested < end)
			{
				end = comparingCar.endRequested;
			}
			middleTest = newCar.startRequested;
		}
		
		
		return (middleTest<end);
	}
	/*
	private ScheduledCar CreateCarSlot(int i)
	{
		CarData cd = CarList.get(i);
		ScheduledCar car = new ScheduledCar();
		
		car.id = cd.carId;
		car.type = cd.type;
		car.startRequested = cd.reqStartTime;
		car.endRequested = cd.reqFinishTime;
		//Fill in the actual start time
		
		return car;
	}
	*/
	
}
