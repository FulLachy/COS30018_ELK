package CarCharge;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.*;

public class MasterSchedulingAgent extends Agent{

	//private CyclicBehaviour reply;
	private TickerBehaviour count;
	
	@Override
	protected void setup()
	{
		//Print that the agent has been created
		this.addBehaviour(new GetCarMessage());
		
		//Algorithm 
		//
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
				System.out.println(getLocalName() + ": Received Message");
				ACLMessage reply = message.createReply();
				
				switch (message.getPerformative())
				{
					case ACLMessage.REQUEST:
						/*try
						{
							//obtain preferences here
						} catch(UnreadableException ue)
						{
							ue.printStackTrace();
						}*/	
						
						//Using Dummy as the Schedule is fine at the current time
						boolean dummy = true;
						
						reply.addReceiver(message.getSender());
						
						//if schedule is fine then proceed 
						if (dummy)
						{
							reply.setPerformative(ACLMessage.AGREE);
							reply.setContent("Scheduled");
						}
						
						//If the schedule is full or can't be moved, refuse
						else
						{
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setContent("Cannot be Scheduled");
						}
						
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
	public void AddCar()
	{
		
	}
	
	public void RemoveCar()
	{
		
	}
	
	public void CreateSchedule()
	{
		
	}
	
	public void AssignCarPlug(Map<AID,String> theAgents)
	{
		//Map<AID,String> theAgents = new HashMap<AID, String>();
		
		//return theAgents;
	}
	
}
