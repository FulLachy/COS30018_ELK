package CarCharge;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
//import java.lang.String;

import CarInformation.CarInformation;
import CarInformation.carType;
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
	//Private variables
	private carType cType = carType.Large; //Car Type
	private double currentCharge = 0; //percentage
	private double minCharge = 0.5; //Minimum Requested Charge Total 
	private double maxCharge = 3.0; //Maximum Charge A Car Can Take 
	private double chargeRate = 0.25; //How much a car charges every half-hour
	private double startTime = 0.0; //What time the car wants to start at from 0-48.00 (48 Hours)
	private double endTime = 12.0; //The time the car wants to be finished by
	private String carID = UUID.randomUUID().toString(); //A unique ID for each car
	private CarInformation prefMessage; //For converting and sending to the Master Scheduling Agent
	
	//Removed to the errors it produced
	/*public CarAgent(carType type, double minimumCharge, double curCharge, double start) 
	{
		currentCharge = curCharge;
		cType = type;
		minCharge = minimumCharge;
		startTime = start;
		switch(cType)
		{
			case Large:
				maxCharge = 3.0;
				chargeRate = 0.25;
				break;
			case Medium:
				maxCharge = 2.0;
				chargeRate = 0.125;
				break;
			case Small:
				maxCharge = 1.0;
				chargeRate = 0.1;
				break;
		}
	}*/

	//Gets all arguments that are filled in on agent launch
	private void GetArguments()
	{
		Object[] args = getArguments();
		
		if (args!= null)
		{
			switch(args[0].toString())
			{
				case "Large":
					maxCharge = 3.0;
					chargeRate = 0.25;
					cType = carType.Large;
					break;
				case "Medium":
					maxCharge = 2.0;
					chargeRate = 0.125;
					cType = carType.Medium;
					break;
				case "Small":
					maxCharge = 1.0;
					chargeRate = 0.1;
					cType = carType.Small;
					break;
			}
			
			//Assigning values from parsed information
			minCharge = Double.parseDouble(args[1].toString());
			currentCharge = Double.parseDouble(args[2].toString());
			startTime = Double.parseDouble(args[3].toString());
			endTime = Double.parseDouble(args[4].toString());
			
		}
	}
	
	protected void setup() {
		
		GetArguments();
		
		
		//Creates a message via an object to send to the Master Agent
		prefMessage = new CarInformation(cType, carID, chargeRate, chargeRate, chargeRate);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		
		msg.setContent("Request Schedule");
		
		//Tries to set content with the preference object
		try {
			msg.setContentObject(prefMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to Attach the Preference Data");
			e.printStackTrace();
		}
		
		//Send message to MSA
		msg.addReceiver(new AID("MasterSchedulingAgent", AID.ISLOCALNAME));
			
		//Print to message to screen and Send Message (only once) 
		//System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to Master");
		Iterator receivers = msg.getAllIntendedReceiver();
		while(receivers.hasNext()) {
			System.out.println(getLocalName() + ": Sending message to " + ((AID)receivers.next()).getLocalName());
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

		//Upon receiving a reply it chooses an option based on said reply. 
		@Override
		public void action() {
			ACLMessage msg = receive();
			if (msg!= null)
			{
				if (msg.getPerformative() == ACLMessage.AGREE)
				{
					System.out.println(getLocalName() + ": Received message - accepting spot");
					
					//Informs MSA that it has received the message and started charging
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(getLocalName() + ": Place accepted");
					reply.addReceiver(msg.getSender());
					send(reply);
					chargeCar();
					
					//No longer received message
					block();
				}
				
				else if (msg.getPerformative() == ACLMessage.REFUSE)
				{
					
					System.out.println(getLocalName() + ": Received message - no spot, agent dying now");
					myAgent.doDelete(); //kill agent??
					//Killing agent to be changed for a waiting list
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
				
				//Removed ticking print for clarity and cleanup
				//System.out.println("Agent " + myAgent.getLocalName() + ": current charge =" + getTickCount());
			}
		};
			
		//Behaviour that is called after an elasped timeout of 3 seconds = "every half an hour", check charge, act accordingly
		w = new WakerBehaviour(this, 3000)
		{
			protected void handleElapsedTimeout() 
			{
				//system print out of what the cars current charge is
				System.out.println(myAgent.getLocalName() + " current charge is " + currentCharge/maxCharge + " %");
					
				//if min charge is hit inform MSA
				if (minCharge > currentCharge) 
				{
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setContent("Minimum charge reached");
					msg.addReceiver(new AID("MasterSchedulingAgent", AID.ISLOCALNAME));
					send(msg);
				}
				
				//if max charge hit, send message to MSA, stop ticker, kill agent
				if (maxCharge >= currentCharge) 
				{
					//If it has hit full charge
					ACLMessage maxMsg = new ACLMessage(ACLMessage.INFORM);
					maxMsg.setContent("Maximum charge reached, agent leaving");
					maxMsg.addReceiver(new AID("MasterSchedulingAgent", AID.ISLOCALNAME));
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