// class used to provide access to static constants governing the behaviour of the simulation
// if in later extensions these need to be made non-final they should be moved to a different class
// possibly Simulation or World, whichever makes more sense for the given variable

public class Config {
	
	// how many ticks should the simulation run for
	public static final int SIMULATION_LENGTH = 100;
	// factor for determining arrest probability
	public static final double K = 2.3;
	// by how much must agent's grievance exceed net risk to cause rebellion
	public static final double REBEL_THRESHOLD = 0.1;
	// how far (in tiles) does an agent see
	public static final int SIGHT_RANGE = 7;
	
	// total densities for all types of agents need to be below 1.
	// some room should be left open for the agents to be able to move about
	// percentage of tiles occupied by cops
	public static final double INITIAL_COP_DENSITY = 0.04;
	// percentage of tiles occupied by civilians
	public static final double INITIAL_CIVILIAN_DENSITY = 0.7;
	
	// world width in tiles
	public static final int WORLD_WIDTH = 40;
	// world height in tiles
	public static final int WORLD_HEIGHT = 40;
	// global constant affecting likelihood of rebellion for all agents: higher legitimacy = less rebellion
	public static final double GOVERNMENT_LEGITIMACY = 0.82;
	// up to how long to arrested agents get sent to jail for (in ticks)
	public static final int MAX_JAIL_TERM = 30;
}
