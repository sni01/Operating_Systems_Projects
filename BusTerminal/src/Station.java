import java.util.LinkedList;
import java.util.List;

public class Station {
	private final int TOTAL_STATIONS;
	private final double MEAN;
	
	private EventList events;
	private double seed;
	private int station_id;
	private long arrive_time;
	private long leave_time;
	private long last_person_arrival_time;
	private int bus_id;
	private long person_id;
	
	private List<Integer> queue_lengths;

	/*
	 * Constructor
	 * @Param events
	 * @Param seed, for random next arrival time cost, start from 1000
	 * @Param arrive_time, last bus arrive time, start from 0
	 * @Param leave_time, last bus leaving time, start from 0
	 * @Param last_person_arrival_time, start from 0
	 * @Param bus_id, current in station bus_id, initial with 0
	 * @Param person_id, current arrival person id, initial with 0
	 */
	public Station(int id, int stations_number, double mean) {
		events = new EventList(id);
		seed = 1000;
		arrive_time = 0;
		leave_time = 0;
		last_person_arrival_time = 0; 
		station_id = id;
		bus_id = 0;
		person_id = 0;

		TOTAL_STATIONS = stations_number;
		MEAN = mean;
		queue_lengths = new LinkedList<Integer>();
	}

	/*
	 * Calculate next person arrival time cost
	 */
	public long random() {
		return 12;
	}
	
	/*
	 * Find the posisson random number generator online
	 */
	public long getRandomPoisson() {
		double L = Math.exp(-MEAN);
		double p = 1.0;
		int k = 0;

		do {
			k++;
			p *= Math.random();
		} while (p > L);

		return k - 1;
	}
	
	public EventList getEventList(){
		return this.events;
	}
	
	public int getId(){
		return station_id;
	}

	/*
	 * Send BUS_ARRIVAL event to next bus station Set last_bus_leave_time
	 */
	public Event busDeparture(long bus_travel_time_cost) {
		long arrival_time = leave_time + bus_travel_time_cost;
		int next_station_id = (station_id + 1) % TOTAL_STATIONS;
		return new Event(arrival_time, EventType.BUS_ARRIVAL, -1, bus_id,
				next_station_id); // person_id set to -1, since this is not a
									// person event
	}

	/*
	 * Main logic of bus arrival in station.
	 * @Param busArrival, bus arrival event.
	 * @Param board_time_cost, boarding time cost in the station.
	 */
	public void busArrival(Event busArrival, long board_time_cost) {
		if(busArrival.time < arrive_time){
			// catch up happens on the road
			busArrival.time = arrive_time;
		}
		// add to event list
		events.addEvent(busArrival);
		
		// set bus_id to new arrival bus's id
		bus_id = busArrival.bus_id;
		
		// prevent from surpassing
		if(busArrival.time <= leave_time){
			//events.printEvent(busArrival);
			return;
		}

		/*
		 *  Accumulate passengers since last bus left
		 */
		long interval_start = leave_time;
		long interval_end = busArrival.time;
		long passenger_arrival_time = 0;

		// Get First Person arrival time
		passenger_arrival_time = getRandomPoisson();
		interval_start = last_person_arrival_time + passenger_arrival_time;
		
		int passengers = 0;
		while (interval_start <= interval_end) {
			// Send person arrival event to event list
			Event person_arrival_event = new Event(interval_start, EventType.PERSON_ARRIVAL, person_id, bus_id, station_id);
			events.addEvent(person_arrival_event);
			// Get Person arrival time
			passenger_arrival_time = getRandomPoisson();
			interval_start += passenger_arrival_time;

			person_id++;
			passengers++;
		}
		
		queue_lengths.add(passengers);

		/*
		 *  Accumulated Passengers get onBoard
		 */
		long boarding_time = interval_end;
		
		// reset person_id to first passenger
		person_id -= passengers;
		
		while (passengers > 0) {
			// Send onBoard event to event list
			Event on_board_event = new Event(boarding_time, EventType.PERSON_BOARD, person_id, bus_id, station_id);
			events.addEvent(on_board_event);
			boarding_time += board_time_cost;
			
			person_id++;
			passengers--;
		}
		
		/*
		 *  Arriving passengers while bus is in station, so boarding and arrival happen at the same time.
		 */
		while(boarding_time >= interval_start){
			Event person_arrival_event = new Event(interval_start, EventType.PERSON_ARRIVAL, person_id, bus_id, station_id);
			events.addEvent(person_arrival_event);
			Event on_board_event = new Event(boarding_time, EventType.PERSON_BOARD, person_id, bus_id, station_id);
			events.addEvent(on_board_event);
			person_id++;
			
			boarding_time += board_time_cost;
			long tmp = getRandomPoisson();
			interval_start += tmp;
			if(boarding_time < interval_start) last_person_arrival_time = interval_start - tmp;
		}
		
		// print this bus stop events
		//events.printInterval(leave_time, boarding_time);
		
		// set leave time of last bus
		leave_time = boarding_time;
	}

	public int[] calQueue(){
		int count = 0;
		int sum = 0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < queue_lengths.size(); i++){
			int tmp = queue_lengths.get(i);
			if(tmp > max) max = tmp;
			if(tmp < min) min = tmp;
			count++;
			sum += tmp;
		}
		int[] results = new int[3];
		results[0] = min;
		results[1] = max;
		results[2] = (sum / count);
		return results;
	}
}
