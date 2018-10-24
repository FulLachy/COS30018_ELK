package Algorithm;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class MyFitnessFunction extends FitnessFunction {
	public double evaluate(double position[]) {
		return position[0] + position[1];
	}
}
