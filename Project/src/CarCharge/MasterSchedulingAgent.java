package CarCharge;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;

import java.util.*;

public class MasterSchedulingAgent extends Agent{

	private CyclicBehaviour reply;
	private TickerBehaviour count;
	
	public void activateCounter()
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
	}
	
	public void deactivateCounter()
	{
		count.stop();
		this.removeBehaviour(count);
	}
	
	public void createSchedule()
	{
		
	}
	
	public void getCarMessage()
	{
		reply = new CyclicBehaviour(this)
		{
			@Override
			public void action()
			{
				
			}
		};
		
		this.addBehaviour(reply);
	}
	
	public void assignCarPlug(Map<AID,String> theAgents)
	{
		//Map<AID,String> theAgents = new HashMap<AID, String>();
		
		//return theAgents;
	}
	
	@Override
	protected void setup()
	{
		
	}
}
