import java.util.ArrayList;

// simulation data storage and analysis class


public class Statistics {
	// the World object being tracked, passed on creation
	private World world;
	// uses TickRecord objects to store world states in an array list
	private ArrayList<TickRecord> tickDataList;
	
	public Statistics(World world) {
		this.world = world;
		tickDataList = new ArrayList<TickRecord>();
	}
	
	
	public void storeTickData() {
		int index = tickDataList.size();
		TickRecord tickData = new TickRecord(index, world);
		tickDataList.add(tickData);
	}
	
	// a simple method printing out the latest world state
	public void printLatestWorldState() {		
		printWorldState(tickDataList.size()-1);

	}
	
	// prints out the world state for a requested tick
	// uses indexes in tickDataList to identify which tickRecord to fetch
	// relies on the assumption that all World ticks are saved and they're saved sequentially 
	public void printWorldState(int tick) {
		if (tickDataList.size() == 0) {
			System.err.println("No ticks have been stored yet.");
			return;
		}
		if (tickDataList.size() < tick) {
			System.err.println("No data for tick requested.");
			return;
		}
		
		TickRecord t = tickDataList.get(tick);
		
		int totalCivs = t.getTotalCivilianCount();
		int quietCivs = t.getQuietCivilianCount();
		int rebels = t.getActiveRebelCount();
		int jailed = t.getJailedRebelCount();
		int cops = t.getCopCount();
		
		System.out.println(String.format("Tick: %d", t.getTickNumber()));
		System.out.println(String.format("Total civilian population: %4d", totalCivs));
		System.out.println(String.format("    Quiet civilians:       %4d (%d%%)", quietCivs, 100*quietCivs/totalCivs));
		System.out.println(String.format("    Active rebels:         %4d (%d%%)", rebels, 100*rebels/totalCivs));
		System.out.println(String.format("    Jailed rebels:         %4d (%d%%)", jailed, 100*jailed/totalCivs));
		System.out.println(String.format("Total cop population:      %4d", cops));
		System.out.println();
	}
	
	
	//TODO : Data analysis methods
}
