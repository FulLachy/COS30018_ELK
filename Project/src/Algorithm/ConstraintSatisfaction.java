package Algorithm;

import java.util.LinkedList;

import CarCharge.CarData;

public class ConstraintSatisfaction {

	private int numStations;
	private boolean firstCarSlotted = true;
	
	//private LinkedList<Schedule> schedule;
	
	private LinkedList<CarData> CarList;
	public Schedule schedule = null;

	private LinkedList<ScheduledCar> unusedCars = new LinkedList<ScheduledCar>();
	public boolean ready = false;
	
	int slow;
	int medium; 
	int fast;
	//int numberOfMedium; 
	//int numberOfFast;
	
	
	public ConstraintSatisfaction(LinkedList<CarData> list, int stations,int numberOfSlow, int numberOfMedium, int numberOfFast)
	{
		CarList = list;
		numStations = stations;
		CreateSchedule(numberOfSlow, numberOfMedium, numberOfFast);
	}
	
	private void CreateSchedule(int numberOfSlow, int numberOfMedium, int numberOfFast)
	{
		if(schedule == null)
		{
			//schedule = new LinkedList<Schedule>();
			schedule = new Schedule(numberOfSlow, numberOfMedium, numberOfFast);
			slow = numberOfSlow;
			medium = numberOfMedium;
			fast = numberOfFast;
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
		if(CarList.size() > 1)
		{
			for (int i = 0; i < CarList.size() - 1; i++)
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
	}
	
	public boolean DoesCarExist(String id)
	{
		for (int i = 0; i < schedule.chargeStations.size(); i++)
		{
			for (int o = 0; i < schedule.chargeStations.get(i).allotedCars.size(); o++)
			{
				if(schedule.chargeStations.get(i).allotedCars.size() != 0)
				{
					if(schedule.chargeStations.get(i).allotedCars.get(o).id == id)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void PrintNames()
	{
		for (int y = 0; y < CarList.size(); y++)
		{
			System.out.println(CarList.get(y).carId);
		}
	}
	
	public Schedule CreateSchedule()
	{
		firstCarSlotted = true;
		ready = false;
		
		PrintNames();
		System.out.println(CarList.size());
		
		System.out.println(slow + " " + medium + " " + fast);
		ChargeStation bestStation = null;
		Schedule s = new Schedule(slow, medium, fast);
		
		
		for(int i=0; i< CarList.size(); i++)
		{
			boolean clash = false;
			ScheduledCar sc = new ScheduledCar();
			sc = GetScheduledCar(i);
			
			if (firstCarSlotted == false)
			{
				//OrderCarsByStartTime();
				clash = false;
				boolean correctStationType = true;
				
				for(int a=0; a<numStations; a++)
				{
					clash = false;
					correctStationType = true;
					
					ChargeStation cs = s.chargeStations.get(a);
					
					if (cs.allotedCars.size()!= 0)
					{
						clash = false;
						System.out.println(cs.allotedCars.size() + " What?");
						if(cs.stationType != sc.preferredStationSlot)
						{
							System.out.println("Not Right Type");
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
									System.out.println(clash);
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
									System.out.println("Best Station");
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
						
						if(cs.stationType != sc.preferredStationSlot)
						{
							System.out.println("Not Right Type 2");
							correctStationType = false;
							break;
						}
						else
						{
							clash = false;
							correctStationType = true;
							sc.startTime = sc.startRequested;
							
							System.out.println("It Got Here");
							
							for(int u = 0; u < s.chargeStations.size(); u++)
							{
								if (s.chargeStations.get(u).chargerNumber == cs.chargerNumber)
								{
									s.chargeStations.get(u).allotedCars.add(sc);
								}
							}
						}
						
					}
					
				}
				
				if(!correctStationType)
				{
					unusedCars.add(sc);
				}
				
				if(clash)
				{
					System.out.println("Hello");
					unusedCars.add(sc);				
				}
				else
				{
					for(int a = 0; a < numStations; a++)
					{
						if (bestStation == s.chargeStations.get(a))
						{
							s.chargeStations.get(a).allotedCars.add(sc);
							break;
						}
					}
				}
			}	
			else
			{
				//int tempBool = true;
				firstCarSlotted = false;
				for(int i1=0; i1 < s.chargeStations.size(); i1++)
				{
					if(s.chargeStations.get(i1).stationType == CarList.get(0).preferredStationSlot)
					{
						s.chargeStations.get(i1).allotedCars.add(GetScheduledCar(0));
						System.out.println("Help");
						break;
					}
				}
				
				//tempBool = false;
				
				
				//s.chargeStations.get(0).allotedCars.add(GetScheduledCar(0));
				
			}
		}
		
		boolean clash = false;
		
		
		if (unusedCars.size() > 0)
		{
			System.out.println("Is it here too?");
			for(int o =0; o < unusedCars.size(); o++)
			{		
				ScheduledCar sc = unusedCars.get(o);
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
		}
		
		if(clash)
		{
			
			
		}
		
		for (int o =0; o < unusedCars.size();o++)
		{
			for(int e=0; e<CarList.size(); e++)
			{
				if (CarList.get(e).carId == unusedCars.get(o).id)
				{
					CarList.remove(e);			
				}
			}
		}
		
		ready = true;
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
