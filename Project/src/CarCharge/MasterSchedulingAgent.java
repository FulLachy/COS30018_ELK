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
	private TickerBehaviour count;
	
	private LinkedList<ACLMessage> messageList = new LinkedList<ACLMessage>();
	private LinkedList<CarData> carList = new LinkedList<CarData>();
	
	private ConstraintSatisfaction CS;
	
	private int NumberOfStations;
	private int numberOfSlow;
	private int numberOfMedium;
	private int numberOfFast;
	
	
	@Override
	protected void setup()
	{
		GetArguments();
		CS = new ConstraintSatisfaction(carList,NumberOfStations,numberOfSlow, numberOfMedium, numberOfFast);
		System.out.println(getLocalName() + ": Created");
		this.addBehaviour(new GetCarMessage());
	}
	
	private void GetArguments()
	{
		Object[] args = getArguments();
		
		numberOfSlow = Integer.parseInt(args[0].toString());
		numberOfMedium = Integer.parseInt(args[1].toString());
		numberOfFast= Integer.parseInt(args[2].toString());	
		NumberOfStations = numberOfSlow + numberOfMedium + numberOfFast;
	}
	
	private class GetCarMessage extends CyclicBehaviour
	{
		private GetCarMessage()
		{
			System.out.println(getLocalName() + ": Cyclic Behaviour Started");
		}
		
		@Override
		public void action()
		{
			System.out.println(getLocalName() + ": Waiting for Message");
			ACLMessage message = blockingReceive();
			
			if(message != null)
			{
				messageList.add(message);	
			}
			
			if (messageList.size() > 0)
			{
				ACLMessage firstMessage = messageList.get(0);
				messageList.remove(firstMessage);
				
				System.out.println(getLocalName() + ": Received Message from" + firstMessage.getSender().getLocalName());
				
				ACLMessage reply = message.createReply();
				AID sender = (AID) reply.getAllReceiver().next();
				CarInformation preferenceMessage = null;
				String car = message.getSender().getLocalName();
				
				switch (message.getPerformative())
				{
					case ACLMessage.REQUEST:
						String carID = null;
						try
						{
							preferenceMessage = (CarInformation) message.getContentObject();
							carID = preferenceMessage.carId;
						} 
						catch(UnreadableException ue)
						{
							ue.printStackTrace();
						}
						
						if(carID != null && !DoesCarExist(carID))
						{
							boolean isInRange;
							isInRange = preferenceMessage.reqStartTime >=0.0;
							isInRange = isInRange && preferenceMessage.reqFinishTime <=48.00;
							
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
						System.out.println("Confirm");
						break;
					
					case ACLMessage.DISCONFIRM:
						System.out.println("Disconfirm");
						break;
						
					case ACLMessage.INFORM:
						System.out.println("Master knows that " + message.getSender().getLocalName() + " is in their spot");
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
		Schedule temp = CS.CreateSchedule();
		
		while(!CS.ready) {}
		
		if (DoesCarExist(cd.carId))
		{
			CS.schedule = temp;
			return true;
		}
		
		return false;
	}
	
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
	
	public boolean RemoveCar(int id)
	{
		return true;
	}
	
}
