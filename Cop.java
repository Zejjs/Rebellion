import java.util.ArrayList;

// concrete implementaiton of generic Agent
// describes a Cop on the lookout for rebellious civilians
public class Cop extends Agent{

	public Cop(int x, int y, World w) {
		super(x, y, w);
	}

	// cop logic is very simple
	// first they move to a random visible tile
	// then they try to catch a visible rebel
	public void update() {
		move();
		enforce();
	}
	
	// enforce checks whether there's an active rebel in the Cop's sight range
	// then moves the Cop to that rebel's tile, and arrests the rebel
	private void enforce() {

		// get a list of all visible coordinates from Cop's location
		ArrayList<int[]> visibleCoords = world.getVisibleCoordinates(posX, posY);
		
		// create a new list to store all visible rebels
		ArrayList<Civilian> activeRebels = new ArrayList<Civilian>();
		
		// iterate through visible coordinates, if the coordinate holds a rebel, store him in the list declared above
		for(int[] coords : visibleCoords) {
			if(world.getGrid()[coords[0]][coords[1]] instanceof Civilian) {
				Civilian civ = (Civilian) world.getGrid()[coords[0]][coords[1]];
				if (civ.isActive())
					activeRebels.add(civ);
			}
		}
		
		// if we found no rebels we're done
		if(activeRebels.size() == 0)
			return;
		
		// if we found a rebel, pick one at random and arrest them
		// method could be simplified by grabbing the first rebel found
		// but that'd make the arrests deterministic (always arresting the bottom-left most rebel from the cop)
		// hence the additional randomization
		Civilian target = activeRebels.get((int) Math.random() * activeRebels.size());
		arrest(target);
	}
	
	private void arrest(Civilian civ) {
		// get the rebel's position
		int civX = civ.getPosX();
		int civY = civ.getPosY();
		
		// empty the tile occupied by the cop
		world.getGrid()[posX][posY] = null;
		// put the cop where the rebel used to be
		world.getGrid()[civX][civY] = this;
		// update the Cop's coordinates with the civilians previous coordinates
		// the civilian coordinates remain the same, but he's no longer present in the grid
		// once his jail term ends he'll attempt to make a move from where he got arrested
		posX = civX;
		posY = civY;
		
		// set jail term to a random value between 1 and MAX JAIL TERM
		civ.setJailTerm((int) (Math.random() * Config.MAX_JAIL_TERM + 1));
		// deactivate the agent
		civ.setActive(false);
	}
	
	// overrides the toString method, prints out the cops current location
	public String toString() {
		return String.format("Cop at (%d, %d)", posX, posY);
	}
}
