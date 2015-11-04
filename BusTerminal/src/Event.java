
public class Event{
	long time;
	EventType type;
	long person_id;
	int bus_id;
	int station_id;
	
	public Event(long time, EventType type, long person_id, int bus_id, int station_id){
		this.time = time;
		this.type = type;
		this.person_id = person_id;
		this.bus_id = bus_id;
		this.station_id = station_id;
	}
	
	@Override
	public String toString(){
		switch(this.type){
		case PERSON_ARRIVAL:
			return String.format("Event Type: %s + station id: %d ＋ at time: %d + person: %d + arrived.", EventType.PERSON_ARRIVAL, station_id, time, person_id);
		case BUS_ARRIVAL:
			return String.format("Event Type: %s + station id: %d ＋ at time: %d + bus: %d + arrived.", EventType.BUS_ARRIVAL, station_id, time, bus_id);
		case PERSON_BOARD:
			return String.format("Event Type: %s + station id: %d ＋ at time: %d + person: %d + boarded.", EventType.PERSON_BOARD, station_id, time, person_id);
		}
		return "Wrong Event Type";
	}
}
