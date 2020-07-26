// main class of the simulation

public class Simulation {
	
	public static void main(String[] args) {
		
		World world = new World();
		Statistics stats = new Statistics(world);
		
		// store and print the initial world state
		stats.storeTickData();
		stats.printLatestWorldState();
		
		// tick the world and store its state up to SIMULATION_LENGTH times
		// starts at 1 because tick 0 represents the initial world state
		for (int i = 1; i <= Config.SIMULATION_LENGTH; i++) {
			world.tick();
			stats.storeTickData();
			
			// prints out the world statistics every tick
			stats.printLatestWorldState();
		}
	}
}
