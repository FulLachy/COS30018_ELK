package CarCharge;

import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

//a basic car agent, the car agent will park and start charging and notify the MSA when min charge has 
//been hit (this will hopefully be more useful later). When max charge is reached the MSA is notified, charging will stop 
//and the car will leave
public class CarAgent extends Agent 
{
	double currentCharge = 0; //percentage
	double minCharge = 0.5;
	double maxCharge = 1.0;
	double chargeRate = 0.125; //later we would need to import in value depending on point car is connected

	public CarAgent(double curCharge) 
	{
		currentCharge = curCharge;
		
	}

	protected void setup() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent("Request Schedule");
				
		//Send message to MSA
		msg.addReceiver(new AID("MasterSchedulingAgent", AID.ISLOCALNAME));
			
		//Print to message to screen and Send Message (only once) 
		//System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to Master");
		Iterator receivers = msg.getAllIntendedReceiver();
		while(receivers.hasNext()) {
			System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to " + ((AID)receivers.next()).getLocalName());
		}
		send(msg);
			
		//set up to recieve reply message from MSA
		this.addBehaviour(new WaitForReply());
	}
	
	private class WaitForReply extends CyclicBehaviour
	{
		private WaitForReply()
		{
			System.out.println(getLocalName() + ": Waiting for Reply");
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg = receive();
			if (msg!= null)
			{
				if (msg.getPerformative() == ACLMessage.AGREE)
				{
					System.out.println(getLocalName() + ": Received message - accepting spot");
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(getLocalName() + ": Place accepted");
					reply.addReceiver(msg.getSender());
					send(reply);
					chargeCar();
					block();
				}
				
				else if (msg.getPerformative() == ACLMessage.REFUSE)
				{
					
					System.out.println(getLocalName() + ": Received message - no spot, agent dying now");
					myAgent.doDelete(); //kill agent??
				}
				
				//block(); //change later, otherwise kill agent	
			}			
		};
	}
	
	
	//function for request of scheduling  
	/**
	 * Car requests schedule spot now - add in time request later 
	 * msa reponds with yes (you can charge now) or no and agent dies
	 */
	
	
	//uses ticker and waker behaviour for car charge timing, 1 tick = 10 minutes
	protected void chargeCar()
	{

		TickerBehaviour t;
		WakerBehaviour w;
		
		System.out.println("Agent " + getLocalName() + " started charging");
		
		//Behaviour that is called every 0.1s = "every minute", every tick charge increases
		t = new TickerBehaviour(this, 100) {
			protected void onTick() {
				//every tick increase charge by charge rate
				currentCharge = currentCharge + chargeRate;
				//System.out.println("Agent " + myAgent.getLocalName() + ": current charge =" + getTickCount());
			}
		};
			
		//Behaviour that is called after an elasped timeout of 3 seconds = "every half an hour", check charge, act accordingly
		w = new WakerBehaviour(this, 3000)
		{
			protected void handleElapsedTimeout() 
			{
				//system print out of what the cars current charge is
				System.out.println(myAgent.getLocalName() + " current charge is " + currentCharge + " %");
					
				//if min charge is hit inform MSA
				if (minCharge > currentCharge) 
				{
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setContent("Minimum charge reached");
					msg.addReceiver(new AID("MSAAgent", AID.ISLOCALNAME));
					send(msg);
				}
				
				//if max charge hit, send message to MSA, stop ticker, kill agent
				if (maxCharge >= currentCharge) 
				{
					ACLMessage maxMsg = new ACLMessage(ACLMessage.INFORM);
					maxMsg.setContent("Maximum charge reached, agent leaving");
						
					maxMsg.addReceiver(new AID("MSAAgent", AID.ISLOCALNAME));
					send(maxMsg);
					t.stop(); //stops the ticker after wake up time without deleting the agent
					myAgent.doDelete(); //This will delete the agent after the ticker has finished, if not here ticker will continue after waker time
					//otherwise do nothing, continue ticker
				}
			}
		};
			
		//Add the TickerBehaviour
		addBehaviour(t);
			
		//Add the WakerBehaviour
		addBehaviour(w);
	}
		
}

//cyclic behaviour - getting replies from the MSA and wait for replies and we do a particular thing depending on the reply
//ticker behaviour - timing and keep track of charge 