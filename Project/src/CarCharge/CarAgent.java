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
		CyclicBehaviour messageListeningBehaviour = new CyclicBehaviour(this)	
				{
					public void action() {
						ACLMessage msg = receive();
						if (msg != null) {
							System.out.println(getLocalName() + ": Received response" + msg.getContent() + " from " + msg.getSender().getLocalName());
						}
						block();
					}
				};
				addBehaviour(messageListeningBehaviour);
				
				//Send request to MSAAgent schedule spot for now 
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setContent("Request Schedule");
				msg.addReceiver(new AID("MSAAgent", AID.ISLOCALNAME));
			
			//Send Message (only once)
			System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to ");
			Iterator receivers = msg.getAllIntendedReceiver();
			while(receivers.hasNext()) {
				System.out.println(((AID)receivers.next()).getLocalName());
			}
			send(msg);
				
	}
	
	//function for request of scheduling  
	/**
	 * Car requests schedule spot now - add in time request later 
	 * msa reponds with yes (you can charge now) or no and agent dies
	 */
	
	
	protected void chargeCar() {
		System.out.println("Agent " + getLocalName() + " started.");
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