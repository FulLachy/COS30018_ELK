package Algorithm;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Swarm; 
//import net.sourceforge.jswarm_pso.example_2.SwarmShow2D; 

import Algorithm.MyFitnessFunction;
import Algorithm.MyParticle; 

public class ParticleSwarm  {
	// Create a swarm (using 'MyParticle' as sample particle 
	// and 'MyFitnessFunction' as finess function)
	Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES
			, new MyParticle()
			, new MyFitnessFunction());
	// Set position (and velocity) constraints. 
	// i.e.: where to look for solutions
	swarm.setMaxPosition(1);
	swarm.setMinPosition(0);
	// Optimize a few times
	for( int i = 0; i < 20; i++ ) swarm.evolve();
	// Print en results
	System.out.println(swarm.toStringStats())
}

