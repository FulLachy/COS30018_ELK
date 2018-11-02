package Algorithm;

import java.util.LinkedList;

import CarCharge.CarData;

public class ConstraintSatisfaction {

	//Private
	private int numStations; //Total number of stations
	private boolean firstCarSlotted = true; //Is it the first car slotted
	private LinkedList<CarData> CarList; //Linked list which is identical to MSA's 
	private LinkedList<ScheduledCar> unusedCars = new LinkedList<ScheduledCar>(); //The cars that don't hit their soft constraints
	
	//Number of station types
	private int slow;
	private int medium; 
	private int fast;
	
	//Public
	public Schedule schedule = null; //A new schedule
	public boolean ready = false; //For the MSA so multiple CAs don't access the same variables
	
	//Defines the different variables for the constraint algorithm
	public ConstraintSatisfaction(LinkedList<CarData> list, int stations,int numberOfSlow, int numberOfMedium, int numberOfFast)
	{
		CarList = list;
		numStations = stations;
		CreateSchedule(numberOfSlow, numberOfMedium, numberOfFast);
	}
	
	//creates a schedule, assigns variables
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
	
	//Gets a scheduled car based on its position in the Car List
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
	
	//Orders Cars by Start Time for Optimal Constraints
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
	
	//Does the car Exist within any station 
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
	
	//Used for testing
	public void PrintNames()
	{
		for (int y = 0; y < CarList.size(); y++)
		{
			System.out.println(CarList.get(y).carId);
		}
	}
	
	//The creation of a schedule using constraint satisfaction 
	public Schedule CreateSchedule()
	{
		//Makes sure nothing else can access it and initialises variabels
		firstCarSlotted = true;
		ready = false;
		ChargeStation bestStation = null;
		
		//new Schedule so alterations don't occur
		Schedule s = new Schedule(slow, medium, fast);
		
		
		//For Testing
		////PrintNames();
		////System.out.println(CarList.size());
		
		////System.out.println(slow + " " + medium + " " + fast);
		
		//Goes through the Car List
		for(int i=0; i< CarList.size(); i++)
		{
			//Initialises Clash	
			boolean clash = false;
			ScheduledCar sc = new ScheduledCar();
			sc = GetScheduledCar(i);
			
			//If it's the first car slotted immediately put it in the list
			if (firstCarSlotted == false)
			{
				//Turned off Ordering to remove errors
				//OrderCarsByStartTime();
				clash = false;
				boolean correctStationType = true;
				
				//Goes through all stations
				for(int a=0; a<numStations; a++)
				{
					//Assigns assumptions for each station: If they are set to false after
					//the loop then they are added to the other car list
					clash = false;
					correctStationType = true;
					
					//Assigns new station based on the current station in the loop
					ChargeStation cs = s.chargeStations.get(a);
					
					//If a car slot does not have any slots
					if (cs.allotedCars.size()!= 0)
					{
						//More Assumptions
						clash = false;
						
						//Testing
						////System.out.println(cs.allotedCars.size() + " What?");
						
						if(cs.stationType != sc.preferredStationSlot)
						{
							//Testing
							////System.out.println("Not Right Type");
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
									//Testing
									//System.out.println(clash);
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
									//Testing
									////System.out.println("Best Station");
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
							//Testing
							//System.out.println("Not Right Type 2");
							correctStationType = false;
							break;
						}
						else
						{
							clash = false;
							correctStationType = true;
							sc.startTime = sc.startRequested;
							
							//Testing
							//System.out.println("It Got Here");
			
							//Assignment using match Charge Station for Safety
							for(int u = 0; u < s.chargeStations.size(); u++)
							{
								if (s.chargeStations.get(u).chargerNumber == cs.chargerNumber)
								{
									s.chargeStations.get(u).allotedCars.add(sc);
								}
							}
							break;
						}
						
					}
					
				}
				
				
				//If it clashes or is wrong type without having a best station
				if((!correctStationType||clash) && bestStation == null)
				{
					//Testing
					//System.out.println("Final Clash");
					unusedCars.add(sc);
				}
				
				//Assigns station
				else
				{
					//Testing
					//System.out.println("There's a New Sheriff");
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
			//Happens when first car is slotted
			else
			{
				//tempBool is for safety
				boolean tempBool = true;
				firstCarSlotted = false;
				
				//Assigning the first car
				for(int i1=0; i1 < s.chargeStations.size(); i1++)
				{
					tempBool = true;
					if(s.chargeStations.get(i1).stationType == CarList.get(0).preferredStationSlot)
					{
						s.chargeStations.get(i1).allotedCars.add(GetScheduledCar(0));
						//Leaving in for clarity
						System.out.println("Successful First Placement");
						break;
					}
					else
					{
						tempBool = false;
					}
				}
				//if it can't find the correct station adds to the very first station
				if (tempBool = false)
				{
					s.chargeStations.get(0).allotedCars.add(GetScheduledCar(0));
				}	
			}
		}
		
		//Re instantiating for unused cars
		boolean clash = false;
		
		// GOes through the unused cars to try and assign them anywhere
		if (unusedCars.size() > 0)
		{
			//Testing
			//System.out.println("Is it here too?");
			for(int o =0; o < unusedCars.size(); o++)
			{		
				ScheduledCar sc = unusedCars.get(o);
				for(int a=0; a<numStations; a++)
				{
					ChargeStation cs = s.chargeStations.get(a);
					if (cs.allotedCars.size()!= 0)
					{
						clash = false;
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
							System.out.println("Assigned Non Preferred");
							break;
						}
					}
				}
			}	
		}
		
		/*for (int i = 0; i < CarList.size(); i++)
		{
			System.out.println(CarList.get(i).carId + " " + CarList.);
		}*/
		
		for (int o =0; o < unusedCars.size();o++)
		{
			for(int e=0; e<CarList.size(); e++)
			{
				if (CarList.get(e).carId == unusedCars.get(o).id)
				{
					//Commented out due to bugs
					//CarList.remove(e);			
				}
			}
		}
		
		ready = true;
		return s;
		
	}
		
	//The Gap between two cars for checking which of multiple stations is more appropriate 
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
	
/*	private LinkedList<CarData> OrganiseCarData()
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
	}*/
	
	//Checks if two cars clash
	private boolean CheckClash(ScheduledCar newCar, ScheduledCar comparingCar, StationType station)
	{
		//Automatically clash if starting times are the same
		if(newCar.startRequested == comparingCar.startRequested)
		{
			return true;
		}
		
		double start,end,middleTest;
		
		//If the car that is trying to get into the slot starts after
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
		
		//If the car that is trying to get into the slot starts before
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
