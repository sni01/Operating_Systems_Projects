import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;


public class Simulation {
	@SuppressWarnings("resource")
	public static void main(String [] args) throws FileNotFoundException, IOException{
		// read parameter from file
		int bus_station_number = -1;
		int bus_number = -1;
		long drive_time_in_ms = -1;
		long passenger_mean_arrival_time_per_ms = -1;
		double passenger_mean_arrival_time_cost_in_sec = -1;
		long boarding_time_in_sec = -1;
		long total_simulation_time_in_hrs = -1;
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		String line;
		int count = 1;
		while ((line = br.readLine()) != null) {
			switch (count) {
			case 1:
				bus_station_number = Integer.parseInt(line);
				break;
			case 2:
				bus_number = Integer.parseInt(line);
				break;
			case 3:
				drive_time_in_ms = Integer.parseInt(line);
				break;
			case 4:
				passenger_mean_arrival_time_per_ms = Integer.parseInt(line);
				passenger_mean_arrival_time_cost_in_sec =  (double) TimeConstant.SECONDS_IN_MINUTE / (double) passenger_mean_arrival_time_per_ms;
				break;
			case 5:
				boarding_time_in_sec = Integer.parseInt(line);
				break;
			case 6:
				total_simulation_time_in_hrs = Integer.parseInt(line);
				break;
			}
			count++;
		}
		br.close();
		
		// clear output.txt firstly
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"))));
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output_format.txt"))));
		bw1.write("");
		bw1.flush();
		bw1.close();
		bw2.write("");
		bw2.flush();
		bw2.close();
		
		// add station event lists to the station list.
	    List<Station> stations = new LinkedList<Station>();
	    for(int i = 0; i < bus_station_number; i++){
	    	stations.add(new Station(i, bus_station_number, passenger_mean_arrival_time_cost_in_sec));
	    }
	    
	    // Queue for bus arrival information, initial with evenly buses
	    EventList busArrivalEvents = new EventList(-1);
	    int bus_distance = bus_station_number / bus_number;
	    int bus_id = 1;
	    for(int i = 0; i < bus_station_number; i++){
	    	if(i % bus_distance == 0){
	    		Event bus_arrival_event = new Event(drive_time_in_ms * TimeConstant.SECONDS_IN_MINUTE, EventType.BUS_ARRIVAL, -1, bus_id, i);
	    		busArrivalEvents.addEvent(bus_arrival_event);
	    		bus_id++;
	    	}
	    }
	    
	    // loop in limited time to run the system
	    long simulation_time_in_sec = total_simulation_time_in_hrs * TimeConstant.MINUTES_IN_HOUR * TimeConstant.SECONDS_IN_MINUTE;
	    while(busArrivalEvents.getTime() < simulation_time_in_sec){
			Event bus_arrival_event = busArrivalEvents.removeEvent();
			Station station = stations.get(bus_arrival_event.station_id);
			station.busArrival(bus_arrival_event, boarding_time_in_sec);
			busArrivalEvents.addEvent(station.busDeparture(drive_time_in_ms * TimeConstant.SECONDS_IN_MINUTE));
	    }
	    
	    // gather all events from each stations and sort them by time
	    EventList systemEvents = new EventList(-1);
	    for(Station s : stations){
	    	systemEvents.addEventList(s.getEventList());
	    }
	    systemEvents.printAll();
	    
	    // calculate each station min/max/avg queue length
	    for(Station s : stations){
	    	int[] res = s.calQueue(); // res[0]: min, res[1] max, res[2] avg
	    	System.out.println(String.format("station: %d min: %d max: %d avg: %d.", s.getId(), res[0], res[1], res[2]));
	    }
	}
}
