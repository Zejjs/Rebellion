import java.util.ArrayList;

// concrete implementation of generic Agent
// describes a civilian, with behaviour as fateful as I could get it to the NetLogo model
public class Civilian extends Agent{
	
	// describes whether the agent is rebelling or quiet
	private boolean active;
	// jail term left for the agent, if set to 0 means the agent is not in jail
	private int jailTerm;
	// a random value between 0 and 1 determining how unhappy the agent is with the world
	// set at creation and constant throughout the simulation
	private double perceivedHardship;
	// a random value between 0 and 1 determining how much the agent's behaviour is affected by cop presence
	// set at creation and constant throughout the simulation 
	private double riskAversion;
	
	public Civilian(int x, int y, World w) {
		super(x, y, w);
		active = false;
		jailTerm = 0;
		perceivedHardship = Math.random();
		riskAversion = Math.random();
	}
	

	public void update() {
		
		// if the agent is in prison, reduce sentence by 1
		if (isInPrison())
			jailTerm--;
		
		
		// if agent is not in prison move to a random visible tile
		// two checks are made, because the agent might have just gotten released and is now eager to go home
		// while agent is in prison it's not stored in the World object's grid
		// so we want them to return to the grid asap
		// if movement is disabled this section will need to be reworked to still allow a jailed agent to return to the grid somehow
		// the NetLogo spec is ambiguous wrt how to handle an Agent returning from prison to an already occupied cell
		// so we need to make a decision on that and code it here
		if (!isInPrison()) {
			move();
			
		// determine whether the agent rebels
			determineBehaviour();
		}
	}
	
	// sets the Civilian's active flag according to the NetLogo model logic
	public void determineBehaviour() {
		double grievance = perceivedHardship * (1 - Config.GOVERNMENT_LEGITIMACY);
		
		// the block below counts visible cops and active agents
		// visibleActiveAgent count is initialised to 1 to avoid divide by zero errors
		ArrayList<int[]> visibleCoords = world.getVisibleCoordinates(posX, posY);
		double visibleCopCount = 0;
		double visibleActiveAgentCount = 1;
		for (int[] coords : visibleCoords) {
			if (world.getGrid()[coords[0]][coords[1]] instanceof Cop)
				visibleCopCount++;
			else if (world.getGrid()[coords[0]][coords[1]] instanceof Civilian) {
				Civilian c = (Civilian) world.getGrid()[coords[0]][coords[1]];
				if (c.active)
					visibleActiveAgentCount++;
			}
		}
		
		double estimatedArrestProbability = 1 - Math.exp(-Config.K * Math.floor(visibleCopCount/visibleActiveAgentCount));
		
		active = grievance - riskAversion * estimatedArrestProbability > Config.REBEL_THRESHOLD;
	}
	
	// overrides the toString method, making the civilian print out its coordinates.
	// if need be could be expanded to also print out active status, jail term left etc
	public String toString() {
		return String.format("Civilian at (%d, %d)", posX, posY);
	}
	
	// getters and setters needed by the simulation
	public void setJailTerm(int i) {
		jailTerm = i;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public boolean isInPrison() {
		return jailTerm > 0;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getJailTerm() {
		return jailTerm;
	}
	
	public boolean isActive() {
		return active;
	}
}
