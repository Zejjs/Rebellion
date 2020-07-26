// data type used by the Statistics object.
// Stores all the relevant numerical data representing the world state at a point in time
// the NetLogo model doesn't save positional data, so neither does our simulation

public class TickRecord {
	private int tickNumber;
	private int totalCivilianCount;
	private int copCount;
	private int quietCivilians;
	private int activeRebels;
	private int jailedRebels;
	
	public TickRecord(int tickNumber, World world) {
		this.tickNumber = tickNumber;
		totalCivilianCount = world.getCivCount();
		copCount = world.getCopCount();
		quietCivilians = world.getQuietCount();
		activeRebels = world.getActiveCount();
		jailedRebels = world.getInJailCount();
	}
	
	// getters for all the variables
	public int getTickNumber() {
		return tickNumber;
	}
	
	public int getTotalCivilianCount() {
		return totalCivilianCount;
	}
	
	public int getCopCount() {
		return copCount;
	}
	
	public int getQuietCivilianCount() {
		return quietCivilians;
	}
	
	public int getActiveRebelCount() {
		return activeRebels;
	}
	
	public int getJailedRebelCount() {
		return jailedRebels;
	}
}
