import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/*
 * EventList, sorted by time.
 */

public class EventList {
	private List<Event> queue;
	
	public EventList(){
		queue = new LinkedList<Event>();	
	}
	
	/*
	 * Because of time consumption, we do not sort it by time, but each of the job in the list is sorted by time.
	 */
	public void insertEvent(Event event){
		queue.add(event);
	}
	
	public void printEventList(){
		BufferedWriter[] bws = new BufferedWriter[10];
		try {
			for(int i = 0; i < bws.length; i++){
				String fileName = "output" + i + ".txt";
				bws[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName), true)));
			}
			for(Event event : this.queue){
				int job_id = event.getJobID();
				bws[job_id].write(event.getJobID() + " " + event.getArrivalTime() + " " + event.getEventType().toString());
				bws[job_id].newLine();
			}
			for(int i = 0; i < bws.length; i++){
				bws[i].newLine();
				bws[i].flush();
				bws[i].close();
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"), true)));
			for(Event event : this.queue){
				bw.write(event.getJobID() + " " + event.getArrivalTime() + " " + event.getEventType().toString());
				bw.newLine();
			}
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
