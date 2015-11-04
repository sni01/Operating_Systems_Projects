import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/*
 * Process logic, receive job and calculate next I/O request time(exponential)
 * Results of one iteration of process job will be;
 * 1. finished
 * 2. goes to ready queue
 * 3. goes to I/O queue
 * 
 * @Param utilized_time, how long it takes to execute the job
 * @Param time_slice, time quantum. Round Robin uses it, other models set it to 0.
 */

public class CPU {
	private long time_slice;
	private long current_time;
	private long IOTime;
	private BufferedWriter bw;
	
	private EventList events;
	private JobList readyJobs; // ready for CPU to run
	private JobList requestJobs; // ready to get access to I/O resource
	private JobList finishJobs;
	
	private JobList.JobListType type;
	private int id;
	
	public CPU(long time_slice, JobList.JobListType type, long IOTime, int id){
		this.time_slice = time_slice;
		this.IOTime = IOTime;
		
		events = new EventList();
		this.type = type;
		this.id = id;
		
		readyJobs = new JobList(type);
		requestJobs = new JobList(JobList.JobListType.FCFS); //IO is always FCFS
		finishJobs = new JobList(JobList.JobListType.FCFS);
	}
	
	/*
	 * Init the position when jobs are received,
	 * Can be any initialization strategy.
	 * Here we send one job to CPU, half of the remaining to readyJobs list, the other half to requestJobs list
	 */
	public void init(List<Job> jobs){
		try {
			String filename = "output_" + this.id + ".txt";
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), true)));
			for(int i = 0; i < 3; i++){
				try {
					bw.write("==========================================================================");
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < jobs.size() / 2; i++){
			readyJobs.insertJob(jobs.get(i));
		}
		for(int i = jobs.size() / 2; i < jobs.size() - 1; i++){
			requestJobs.insertJob(jobs.get(i));
		}
		JobArrival(jobs.get(jobs.size() - 1));
	}
	
	/*
	 * Running CPU until nothing is in the queues or CPU
	 */
	public void run(){
		try {		
			// first finishes requestJobs
			while(!requestJobs.isEmpty()){
				IOArrival(requestJobs.getJob());
			}
			
			// execute one by one
			while(!readyJobs.isEmpty() || !requestJobs.isEmpty()){
				// finish all possible CPU burst
				JobArrival(readyJobs.getJob());			
			}
			
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*
	 * Receive Job from I/O or ready queue
	 */
	public void JobArrival(Job job){
 		//System.out.println(job.getId() + " : " + job.getRemainExectionTime()); // check remaining execution time.
		// update waiting time
		job.updateWaitingTime(Math.max(0, current_time - job.getLastExecutionTime()));
		/*
		 * Sync time line in job and CPU
		 */
		if(job.getLastExecutionTime() > current_time){
			// CPU is waiting for job to come in
			current_time = job.getLastExecutionTime();
		}
		else{
			// job is waiting for CPU to be free
			job.setLastExecutionTime(current_time);
		}
		
		// update readyJobs queue's time line, this time is the same as CPU time.
		readyJobs.setCurrentTime(current_time);
		
		try {
			bw.write(String.format("Process %d left ready queue: %d", job.getId(), this.current_time));
			bw.newLine();
			bw.write(String.format("Process %d arrived at CPU: %d", job.getId(), this.current_time));
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// arrival event inserted into event list.
		Event jobArrivalEvent = new Event(job.getId(), job.getLastExecutionTime(), Event.EventType.JobCPURequest);
		//System.out.println(jobArrivalEvent.toString());
		events.insertEvent(jobArrivalEvent);
		
		// executes the job and return event with related event that happens.
		Event event = job.executed(current_time, time_slice);
		
		// if job is finished, push it to finished queue
		if(event.getEventType() == Event.EventType.JobFinished){
			finishJobs.insertJob(job);
		}
		
		// if job is interrupted, push it to ready queue
		else if(event.getEventType() == Event.EventType.JobReady){
			readyJobs.insertJob(job);
		}
		
		// if job is requesting I/O resources, push it to I/O request queue
		else{
			requestJobs.insertJob(job);
			IOArrival(requestJobs.getJob());
		}
		
		current_time = event.getArrivalTime(); // precede the time line in CPU
		events.insertEvent(event);
		//System.out.println(event.toString());
		
		try {
			bw.write(String.format("Process %d left CPU: %d", job.getId(), this.current_time));
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * I/O request
	 */
	public void IOArrival(Job job){
		// update requestJobs current time
		if(job.getLastExecutionTime() > requestJobs.getCurrentTime()){
			// I/O is waiting for incoming job
			requestJobs.setCurrentTime(job.getLastExecutionTime());
		}
		else{
			// job is waiting for I/O availability
			job.setLastExecutionTime(requestJobs.getCurrentTime());
		}
		
		try {
			bw.write("Process " + job.getId() + " arrived I/O queue: " + job.getLastExecutionTime());
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		job.IOAccess(requestJobs.getCurrentTime(), IOTime);
		requestJobs.precedeCurrentTime(IOTime);
		readyJobs.insertJob(job);
		
		Event event = new Event(job.getId(), requestJobs.getCurrentTime(), Event.EventType.JobReady);
		events.insertEvent(event);
		//System.out.println(event.toString());
		try {
			bw.write("Process " + job.getId() + " left I/O queue: " + job.getLastExecutionTime());
			bw.newLine();
			bw.write("Process " + job.getId() + " arrived ready queue: " + job.getLastExecutionTime());
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Calculate CPU utilization rate
	 */
	public double utilized_rate(){
		return (double) (finishJobs.getUtilizedTime()) / (double) (this.current_time);
	}
	
	public double throughput(){
		return (double) (finishJobs.getSize() * Job.MS_PER_MINUTE) / (double) (finishJobs.getFinishTime());
	}
	
	public double turnAround(){
		return (double) (finishJobs.getTurnAround()) / (double) (finishJobs.getSize() * Job.MS_PER_MINUTE);
	}
	
	public double avg_waiting_time(){
		return (double) (finishJobs.getWaitingTime()) / (double) (finishJobs.getSize() * Job.MS_PER_MINUTE);
	}
	
	public void print(){
		this.events.printEventList();
	}
}
