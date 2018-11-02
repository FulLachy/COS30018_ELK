package CarCharge;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.*;

import Algorithm.ConstraintSatisfaction;
import Algorithm.Schedule;
import CarInformation.CarInformation;

public class MasterSchedulingAgent extends Agent{

	//private CyclicBehaviour reply;
	//private TickerBehaviour count;
	
	//Private Variables
	private LinkedList<ACLMessage> messageList = new LinkedList<ACLMessage>(); //Message buffering list
	private LinkedList<CarData> carList = new LinkedList<CarData>(); //Car list for sharing consistent information with Algorithms
	private ConstraintSatisfaction CS; // Algorithm used
	
	//Number of stations and station types
	private int NumberOfStations;
	private int numberOfSlow;
	private int numberOfMedium;
	private int numberOfFast;
	
	//Creates new behaviours and starts algorithm
	@Override
	protected void setup()
	{
		GetArguments();
		CS = new ConstraintSatisfaction(carList,NumberOfStations,numberOfSlow, numberOfMedium, numberOfFast);
		System.out.println(getLocalName() + ": Created");
		this.addBehaviour(new GetCarMessage());
	}
	
	//Parses in types of stations
	private void GetArguments()
	{
		Object[] args = getArguments();
		
		numberOfSlow = Integer.parseInt(args[0].toString());
		numberOfMedium = Integer.parseInt(args[1].toString());
		numberOfFast= Integer.parseInt(args[2].toString());	
		NumberOfStations = numberOfSlow + numberOfMedium + numberOfFast;
	}
	
	//Behaviour for sending and receiving messages from Car Agents
	private class GetCarMessage extends CyclicBehaviour
	{
		//Startup
		private GetCarMessage()
		{
			System.out.println(getLocalName() + ": Cyclic Behaviour Started");
		}
		
		//
		@Override
		public void action()
		{
			System.out.println(getLocalName() + ": Waiting for Message");
			//Doesn't do anything until it gets a message 
			ACLMessage message = blockingReceive();
			
			//adds messages to list
			if(message != null)
			{
				messageList.add(message);	
			}
			
			//Goes through list to extrapolate data
			if (messageList.size() > 0)
			{
				ACLMessage firstMessage = messageList.get(0);
				messageList.remove(firstMessage);
				
				System.out.println(getLocalName() + ": Received Message from" + firstMessage.getSender().getLocalName());
				
				//Gets read to reply
				ACLMessage reply = message.createReply();
				AID sender = (AID) reply.getAllReceiver().next();
				CarInformation preferenceMessage = null;
				String car = message.getSender().getLocalName();
				
				//Message Type dependant
				switch (message.getPerformative())
				{
					//Where the requests and information is extrapolated and organised
					case ACLMessage.REQUEST:
						String carID = null;
						
						//Gets preference data
						try
						{
							preferenceMessage = (CarInformation) message.getContentObject();
							carID = preferenceMessage.carId;
						} 
						catch(UnreadableException ue)
						{
							ue.printStackTrace();
						}
						
						//If the data makes sense
						if(carID != null && !DoesCarExist(carID))
						{
							boolean isInRange;
							isInRange = preferenceMessage.reqStartTime >=0.0;
							isInRange = isInRange && preferenceMessage.reqFinishTime <=48.00;
							
							//Makes sure the car can even be slotted 
							if(isInRange&&AddCar(preferenceMessage))
							{
								System.out.println(getLocalName() +": " + message.getSender());
								reply.setPerformative(ACLMessage.AGREE);
								reply.setContent("Scheduled");
							}
							else
							{
								reply.setPerformative(ACLMessage.REFUSE);
								reply.setContent("Cannot be Scheduled");
							}
						}
						//If the preference data doesn't work
						else
						{
							//Must try and update car preference
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setContent("Cannot be Scheduled");
						}
						reply.addReceiver(message.getSender());
						send(reply);
						
						System.out.println(getLocalName() + ": Sending Response to " + message.getSender().getLocalName());
						break;
					
					case ACLMessage.CONFIRM:
						//Just in Case
						System.out.println("Confirm");
						break;
					
					case ACLMessage.DISCONFIRM:
						//Just in Case
						System.out.println("Disconfirm");
						break;
						
					case ACLMessage.INFORM:
						//Just in Case
						System.out.println("Master knows that " + message.getSender().getLocalName() + " is in their spot");
						break;
				}
			
			}
		}
	}
	/*public void activateCounter()
	{
		count = new TickerBehaviour(this,1000)
		{
			public void onStart()
			{
				super.onStart();
			}
			
			@Override
			public void onTick()
			{
				
			}
			
			public int onEnd()
			{
				return super.onEnd();
			}
		};
	}*/
	
	/*public void deactivateCounter()
	{
		count.stop();
		this.removeBehaviour(count);
	}*/
	
	//Adds car preferences and transforms into car data for algorithm 
	public boolean AddCar(CarInformation preferences)
	{
		CarData cd = new CarData();
		
		cd.carId = preferences.carId;
		cd.type = preferences.type;
		cd.reqFinishTime = preferences.reqFinishTime;
		cd.reqMinCharge = preferences.reqMinCharge;
		cd.reqStartTime = preferences.reqStartTime;
		cd.FindPreferenceSlot();		
		
		carList.add(cd);
		
		//create schedule
		Schedule temp = CS.CreateSchedule();
		
		//If the algorithm is working loop
		while(!CS.ready) {}
		
		//If the car exists then great
		if (DoesCarExist(cd.carId))
		{
			CS.schedule = temp;
			return true;
		}
		
		return false;
	}
	
	//Checks if the preference data has been sent and already exists
	//Checks if the car exists in the list and therefore has been sorted
	public boolean DoesCarExist(String carID)
	{
		for (int i = 0; i< carList.size(); i++)
		{
			if (carList.get(i).carId == carID)
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	public boolean RemoveCar(int id)
	{
		return true;
	}
	*/
}
