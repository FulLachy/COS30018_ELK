package Algorithm;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class MyFitnessFunction extends FitnessFunction {
	
	//Constraint count
	
	public MyFitnessFunction() 
	{
		super(false); // Minimize this function
	}

	@Override
	public double evaluate(double[] position) {
		//Does it each of the soft contraints
		//for each constraint met
			//add to constraint count
		
		return 0;
	}
}
