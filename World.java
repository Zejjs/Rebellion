import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// keeps track of the simulation's world.
// the main parts are the 2d array of Agents keeping track of their positional data
// and the civilians and cops array lists used to quickly iterate through all Agents in the simulation
public class World {
	private int gridWidth;
	private int gridHeight;
	
	// stores the Agents' positional data
	private Agent[][] grid;
	// keeps track of all civilians in the simulation
	private ArrayList<Civilian> civilians;
	// keeps track of all cops in the simulation
	private ArrayList<Cop> cops;
	
	// all the initial parameters for world building are taken from the Config class
	// will need to be passed as parameters into the constructor if they're made more dynamic
	public World() {
		gridWidth = Config.WORLD_WIDTH;
		gridHeight = Config.WORLD_HEIGHT;
		grid = new Agent[gridWidth][gridHeight];
		civilians = new ArrayList<Civilian>();
		cops = new ArrayList<Cop>();

		// populate the world with civilians and cops
		int quietCount = (int) (gridWidth * gridHeight * Config.INITIAL_CIVILIAN_DENSITY);
		int copCount = (int) (gridWidth * gridHeight * Config.INITIAL_COP_DENSITY);
		// first enumerate all the possible spaces in the grid
		ArrayList<Integer> possibleCoordinates = new ArrayList<Integer>();
		for (int i = 0; i < gridWidth*gridHeight; i++) {
			possibleCoordinates.add(i);
		}
		// then shuffle them to get a random ordering
		Collections.shuffle(possibleCoordinates);
		// iterate thorugh the first n possible tiles equal to sum of cops and civilians 
		for (int i = 0; i < quietCount + copCount; i++) {
			// translate the tile number to x and y
			int x = possibleCoordinates.get(i) % gridWidth;
			int y = possibleCoordinates.get(i) / gridWidth;
			
			// create the cops and civilians at translated coordinates
			if (i < quietCount) {
				Civilian civ = new Civilian(x, y, this);
				grid[x][y] = civ;
				civilians.add(civ);
			}
			else {
				Cop cop = new Cop(x, y, this);
				grid[x][y] = cop;
				cops.add(cop);
			}
		}

	}
	
	// runs the update method for each Civilian and Cop in the simulation
	// called by Simulation every tick
	public void tick() {
		civilians.forEach(Civilian::update);
		cops.forEach(Cop::update);
		
	}
	
	// function returns a list of coordinates visible from the tile whose coordinates are passed as arguments
	public ArrayList<int[]> getVisibleCoordinates(int x, int y) {
		// store sight range as a local variable for brevity
		int d = Config.SIGHT_RANGE;
		
		// declare an arraylist to store visible coordinates in
		ArrayList<int[]> visibleCoordinates = new ArrayList<int[]>();
		
		// iterate through all the x and y pairs that are within d distance of x and y passed as arguments
		// that's (d+1) * (d+1) pairs to iterate through
		for (int i = x-d; i <= x+d; i++) {
			for(int j = y-d; j <= y+d; j++) {
				
				// check whether the (x, y) pair currently being iterated is within sight range 
				if (Math.abs(x-i) + Math.abs(y - j) <= Config.SIGHT_RANGE) {
					
					// adjust the (x, y) values to account for world wrapping around
					int visX = ( i>=0 ) ? i % gridWidth : i + gridWidth;
					int visY = ( j>=0 ) ? j % gridHeight : j + gridHeight;
					
					// check whether the (x, y) coordinates are positive
					// if the agent's sight range is greater than the World's gridWidth
					// we may have produce negative coordinates - ignore those
					if (visX >= 0 && visY >= 0) {
						int visCoord[] = {visX, visY};
						
						// check whether we already stored the given coordinate
						// it's possible to have visible tiles overlap because the world wraps around
						// arrays objects don't override the equals method, so we have to iterate manually
						// to check whether the coordinates have already been stored
						boolean duplicate = false;
						for (int[] arr : visibleCoordinates) {
							if (Arrays.equals(arr, visCoord)) {
								duplicate = true;
								break;
							}
						}
						
						// if this is the first time we see this coordinate, store it
						if(!duplicate)
							visibleCoordinates.add(visCoord);
					}
				}
			}
		}
		return visibleCoordinates;
	}
	
	// function returns a list of empty coordinates visible from the tile whose coordinates are passed as arguments
	// it does so by first finding the visible coordinates, and then copying the empty ones to a new arraylist
	// uses a new arrayList, because removing objects from a list we're iterating through is a recipe for trouble
	public ArrayList<int[]> getVisibleEmptyCoordinates(int x, int y) {
		ArrayList<int[]> visible = getVisibleCoordinates(x, y);
		ArrayList<int[]> emptyVisible = new ArrayList<int[]>();
		
		for (int[] arr : visible) {
			if (isEmpty(arr[0], arr[1]))
				emptyVisible.add(arr);
		}
		
		return emptyVisible;
	}
	
	
	// checks whether a tile in the grid is empty
	public boolean isEmpty(int x, int y) {
		return grid[x][y] == null;
	}
	
	// getters
	public Agent[][] getGrid(){
		return grid;
	}
	
	public int getQuietCount() {
		int quietCount = 0;
		
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridWidth; y++) {
				if (grid[x][y] instanceof Civilian) {
					Civilian civ = (Civilian) grid[x][y];
					if (!civ.isActive())
						quietCount++;
				}
			}
		}
		
		return quietCount;
	}
	
	public int getActiveCount() {
		int activeCount = 0;
		
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridWidth; y++) {
				if (grid[x][y] instanceof Civilian) {
					Civilian civ = (Civilian) grid[x][y];
					if (civ.isActive())
						activeCount++;
				}
			}
		}

		return activeCount;
	}
	
	public int getInJailCount() {
		int inJailCount = 0;
		for (Civilian civ : civilians) {
			if (civ.isInPrison())
				inJailCount++;
		}
		return inJailCount;
	}
	
	public int getCopCount() {
		return cops.size();
	}
	
	public int getCivCount() {
		return civilians.size();
	}
	
	public ArrayList<Cop> getCops() {
		return cops;
	}
	
	public ArrayList<Civilian> getCivilians() {
		return civilians;
	}
	
	// prints out the coordinates of every agent in the grid
	// was only used for debugging - we need something prettier if we're to graphically display the world
 	public void printAgnetCoords() {
		for(int x = 0; x < gridWidth; x++) {
			for(int y = 0; y < gridHeight; y++) {
				if (grid[x][y] == null)
					continue;
				System.out.println(grid[x][y]);
			}
		}
	}
}
