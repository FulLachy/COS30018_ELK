package Algorithm;

import CarCharge.CarData;

import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.List; 

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Swarm; 
import net.sourceforge.jswarm_pso.Neighborhood; 
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.example_2.SwarmShow2D; 

import Algorithm.MyFitnessFunction;
import Algorithm.MyParticle; 

public class ParticleSwarm  {
	
	public static void main() {
	
		// Create a swarm (using 'MyParticle' as sample particle 
		// and 'MyFitnessFunction' as fitness function)
		// Amount of particles is now amount of cars 
		Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES
				, new MyParticle() //car agent 
				, new MyFitnessFunction()); //constraints
	
	 	// Use neighborhood 
	 	Neighborhood neigh = new Neighborhood1D(Swarm.DEFAULT_NUMBER_OF_PARTICLES / 5, true);
	 	swarm.setNeighborhood(neigh); 
	 	swarm.setNeighborhoodIncrement(0.9); 
	   
	 	// Tune swarm's update parameters (if needed) 
	 	swarm.setInertia(0.95);
	 	swarm.setParticleIncrement(0.8); 
	 	swarm.setGlobalIncrement(0.8); 
	   
	  
	 	// Set position (and velocity) constraints. 
	 	// i.e.: where to look for solutions
	 	swarm.setMaxPosition(48.00);
		swarm.setMinPosition(0.00);
	
		//show a 2D graph 
		int numberOfIterations = 5000;
		int displayEvery = 1;
		SwarmShow2D ss2d = new SwarmShow2D(swarm, numberOfIterations, displayEvery, true);
		ss2d.run();
	
		//Show best position
		//double bestPosition[] = ss2d.getSwarm().getBestPosition();
		//System.out.println("Best position: [" + bestPosition[0] + ", " + bestPosition[1] + " ]\nBest fitness: " + ss2d.getSwarm().getBestFitness() + "\Known Solution: [0.0, 0.0]");
	}
	
}

