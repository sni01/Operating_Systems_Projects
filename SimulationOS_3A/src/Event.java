/*
 * Events that happened in the simulation
 * 1. Job Arrive Event
 * 2. Job goes to finished queue Event
 * 3. Job goes to I/O request queue Event
 * 4. Job goes to ready queue Event
 */
public class Event {
	public enum EventType{
		JobCPURequest, JobFinished, JobIORequest, JobReady
	}
	
	private EventType type;
	private long arrive_time;
	private int job_id;
	
	public Event(int job_id, long arrive_time, EventType type){
		this.arrive_time = arrive_time;
		this.type = type;
		this.job_id = job_id;
	}
	
	public int getJobID(){
		return this.job_id;
	}
	
	public EventType getEventType(){
		return type;
	}
	
	public long getArrivalTime(){
		return this.arrive_time;
	}
	
	@Override
	public String toString(){
		return "job:" + this.job_id + "arrive at: " + arrive_time + " for " + type.toString();
	}
}
