package CarCharge;

import jade.core.Agent;

//a basic car agent, the car agent will park and start charging and notify the MSA when min charge has 
//been hit (this will hopefully be more useful later). When max charge is reached the MSA is notified, charging will stop 
//and the car will leave
public class CarAgent extends Agent {
	double currentCharge = 0; //percentage
	double minCharge = 0.5;
	double maxCharge = 1;
	
	//another function for what the car does before getting spot 
	
	
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