import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;


public class EventList {
	private List<Event> events;
	private long time;
	
	public EventList(int station_id){
		events = new LinkedList<Event>();
		time = 0;
	}
	
	// Add event sorted by time
	public void addEvent(Event event){
		// update time
		if(time < event.time) time = event.time;
		
		int pos = 0;
		boolean finish = false;
		while(pos < events.size()){
			if(events.get(pos).time > event.time){
				events.add(pos, event);
				finish = true;
				break;
			}
			pos++;
		}
		if(!finish){
			events.add(event);
		}
		return;
	}
	
	// Remove first element, oldest one
	public Event removeEvent(){
		if(events.size() <= 0) return null;
		return events.remove(0);
	}
	
	// print last bus arrival time interval events
	public void printInterval(long previous_bus_leave_time, long current_bus_leave_time){
		BufferedWriter bw1;
		BufferedWriter bw2;
		try {
			bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"), true)));
			bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output_format.txt"), true)));
			boolean bus_arrived = false;
			for(Event event : this.events){
				if(event.time > previous_bus_leave_time && event.time < current_bus_leave_time){
					if(event.type == EventType.BUS_ARRIVAL){
						bus_arrived = true;
					}
					bw1.write(event.toString());
					bw1.newLine();
					if(bus_arrived){
						bw2.write(event.bus_id + " " + event.station_id + " " + event.time);
						bw2.newLine();
					}
				}
			}
			bw1.newLine();
			bw1.flush();
			bw1.close();
			bw2.newLine();
			bw2.flush();
			bw2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printEvent(Event event){
		BufferedWriter bw1;
		BufferedWriter bw2;
		try {
			bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"), true)));
			bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output_format.txt"), true)));
			
			bw1.write(event.toString());
			bw1.newLine();
			bw2.write(event.bus_id + " " + event.station_id + " " + event.time);
			bw2.newLine();
			
			bw1.newLine();
			bw1.flush();
			bw1.close();
			bw2.newLine();
			bw2.flush();
			bw2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printAll(){
		BufferedWriter bw1;
		BufferedWriter bw2;
		try {
			bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"), true)));
			bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output_format.txt"), true)));
			for(Event event : this.events){
				bw1.write(event.toString());
				bw1.newLine();
				if(event.type == EventType.BUS_ARRIVAL || event.type == EventType.PERSON_BOARD){
					bw2.write(event.bus_id + " " + event.station_id + " " + event.time);
					bw2.newLine();
				}
			}
			bw1.newLine();
			bw1.flush();
			bw1.close();
			bw2.newLine();
			bw2.flush();
			bw2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Event> getEventList(){
		return events;
	}
	
	// get time
	public long getTime(){
		return this.time;
	}
	
	public void addEventList(EventList eventList){
		List<Event> otherEvents = eventList.getEventList();
		if(events.size() == 0){
			for(Event e : otherEvents){
				events.add(e);
			}
			return;
		}
		else{
			int p = 0;
			int q = 0;
			while(p < events.size() && q < otherEvents.size()){
				if(events.get(p).time < otherEvents.get(q).time) p++;
				else{
					events.add(p, otherEvents.get(q));
					p++;
					q++;
				}
			}
			if(q < otherEvents.size()){
				while(q < otherEvents.size()){
					events.add(otherEvents.get(q));
					q++;
				}
			}
		}
	}
}
