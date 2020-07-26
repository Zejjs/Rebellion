import java.util.ArrayList;

// abstract class representing generic agents in the simulation
// each agent has a handle on the world within which it operates
// in order to be able to update the agent grid there whenever it moves
public abstract class Agent {
	protected int posX;
	protected int posY;
	protected World world;
	
	public Agent (int x, int y, World w) {
		posX = x;
		posY = y;
		world = w;
	}
	
	// called by World on every tick to make the Agents act
	public abstract void update();
	
	// moves the agent to the designated coordinate
	// not used anywhere in the current implementation, but left it here in case it's useful for extension
	// the only check if performs is whether the agent is moving to an empty space
	// so make sure to add any additional necessary safety checks if extending
	protected void move(int x, int y) {
		if(!world.isEmpty(x, y)) {
			System.err.println("Agent attempted to move to a non-empty coordinate");
			return;
		}
		world.getGrid()[posX][posY] = null;
		posX = x;
		posY = y;
		world.getGrid()[posX][posY] = this;
	};
	
	// makes the agent move to a random visible coordinate
	protected void move() {
		ArrayList<int[]> moveTargets = world.getVisibleEmptyCoordinates(posX, posY);
		
		if(moveTargets.size() == 0) {
			//System.out.println("NO VIABLE MOVE");
			return;
		}
		
		int[] moveTarget = moveTargets.get((int) (Math.random() * moveTargets.size()));
		
		if (world.getGrid()[posX][posY] == this)
			world.getGrid()[posX][posY] = null;
		
		posX = moveTarget[0];
		posY = moveTarget[1];
		world.getGrid()[posX][posY] = this;
		
	};
	
}
