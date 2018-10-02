package CarCharge;

import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

//a basic car agent, the car agent will park and start charging and notify the MSA when min charge has 
//been hit (this will hopefully be more useful later). When max charge is reached the MSA is notified, charging will stop 
//and the car will leave
public class CarAgent extends Agent {
	double currentCharge = 0; //percentage
	double minCharge = 0.5;
	double maxCharge = 1;
	
	protected void setup() {
		//First set-up message receiving behaviour
		/*CyclicBehaviour messageListeningBehaviour = new CyclicBehaviour(this)	{
				public void action() {
					ACLMessage msg = receive();
						if (msg != null) {
							System.out.println(getLocalName() + ": Received response" + msg.getContent() + " from " + msg.getSender().getLocalName());
						}
						block();
					}
				};*/
				//addBehaviour(messageListeningBehaviour);
				
				//Set up request to be sent to MSA
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
					block();
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
	
	
	protected void chargeCar() {
		System.out.println("Agent " + getLocalName() + " started charging");
		//calculate time until minCharge = minChargeTime
		//count until minChargeTime
		//notify MSA min charge has been reached 
		//calculate time until max charge 
		//count until maxChargeTime 
		//notify MSA max charge reached and kill agent
	}
}



//3 different types of car agents can be made later

//cyclic behaviour - getting replies from the MSA and wait for replies and we do a particular thing depending on the reply
//do not need behaviour for sending things
//ticker behaviour - timing 