import java.util.Random;

/*
 * Job describe details of each CPU job
 * @Param mean, inter-I/O interval average burst time
 * @Param length, execution time, uniformly distributed
 */
public class Job {
	public static long MS_PER_MINUTE = 60 * 1000;
	
	private long mean;
	
	private long execution_time;
	private long remain_execution_time;
	private long remain_io_request_time;
	private long start_time;
	private long end_time;
	private long waiting_time;
	
	private int ID;
	private JobList.JobListType type;
	private long predict_burst;
	private long last_burst;
	private double ratio;
	
	/*
	 * Constructor, each job has its average I/O request time, and how long it needs CPU
	 */
	public Job(int id, long mean, long execution_time, JobList.JobListType type){
		this.mean = mean;
		this.execution_time = execution_time;
		this.remain_execution_time = execution_time;
		this.predict_burst = execution_time; // init t0 number
		this.last_burst = execution_time; // init base 
		this.remain_io_request_time = 0;
		this.start_time = 0;
		this.end_time = 0;
		this.waiting_time = 0;
		this.ID = id;
		this.type = type;
	}
	
	/*
	 * Set predict ratio
	 */
	public void setRatio(double ratio){
		this.ratio = ratio;
	}
	
	/*
	 * Get Job ID
	 */
	public int getId(){
		return this.ID;
	}
	
	/*
	 * Find the poisson random number generator online
	 */
	public static long getRandomPoisson(long mean) {
		double L = Math.exp(-mean);
		double p = 1.0;
		int k = 0;

		do {
			k++;
			p *= Math.random();
		} while (p > L);

		return k - 1;
	}
	
	/*
	 * Set next interrupts for I/O happen interval
	 */
	public void setInterrupts(){
		this.remain_io_request_time = getRandomPoisson(this.mean);
	}
	
	public void setInterruptsSJF(){
		this.remain_io_request_time = getRandomPoisson(this.mean);
		this.predict_burst = (long) ((1.0 - ratio) * this.predict_burst + ratio * this.last_burst);
		this.last_burst = this.remain_execution_time;
	}
	
	/*
	 * Get uniform execution time
	 */
	public static long getUniform(long range_left_in_minute, long range_right_in_minute){
		long left_sec = range_left_in_minute * Job.MS_PER_MINUTE;
		long right_sec = range_right_in_minute * Job.MS_PER_MINUTE;
		Random r = new Random();
		return (long)(r.nextDouble() * (right_sec - left_sec)) + left_sec;
	}
	
	/*
	 * Update execution_time, execute some time in CPU
	 */
	public Event executed(long current_time, long time_slice){
		long executed_time;
		Event event;
		
		// request I/O while executing
		if(this.remain_execution_time > this.remain_io_request_time && time_slice >= this.remain_io_request_time){
			executed_time = this.remain_io_request_time;
			this.remain_execution_time -= this.remain_io_request_time;
			if(this.type == JobList.JobListType.FCFS){
				setInterrupts();
			}
			else{
				setInterruptsSJF();
			}
			this.end_time = executed_time + current_time;
			event = new Event(ID, this.end_time, Event.EventType.JobIORequest);
		}
		
		// interrupts while executing
		else if(this.remain_execution_time > this.remain_io_request_time && time_slice < this.remain_io_request_time){
			executed_time = time_slice;
			this.remain_execution_time -= time_slice;
			this.remain_io_request_time -= time_slice;
			this.end_time = executed_time + current_time;
			event = new Event(ID, this.end_time, Event.EventType.JobReady);
		}
		
		// half finished, goes to ready queue
		else if(this.remain_execution_time <= this.remain_io_request_time && time_slice >= this.remain_execution_time){
			executed_time = this.remain_execution_time;
			this.remain_execution_time = 0;
			this.end_time = executed_time + current_time;
			event = new Event(ID, this.end_time, Event.EventType.JobFinished);
		}
		
		// no more interrupts, but will not finished this iteration
		else{
			executed_time = time_slice;
			this.remain_execution_time -= time_slice;
			this.remain_io_request_time -= time_slice;
			this.end_time = executed_time + current_time;
			event = new Event(ID, this.end_time, Event.EventType.JobReady);
		}
		
		return event;
	}
	
	/*
	 * Get access to I/O
	 */
	public void IOAccess(long current_time, long IOTime){
		this.end_time = current_time + IOTime;
	}
	
	/*
	 * Get last execution finished time, it could be I/O finished input/output or CPU finishes execution
	 */
	public long getLastExecutionTime(){
		return this.end_time;
	}
	
	
	/*
	 * Waiting happens
	 */
	public void setLastExecutionTime(long update_end_time){
		this.end_time = update_end_time;
	}
	
	/*
	 * Get remaining execution time, for shortest job first algorithm
	 */
	public long getRemainExectionTime(){
		return this.remain_execution_time;
	}
	
	/*
	 * Get turnaround time
	 */
	public long getTurnaroundTime(){
		return this.end_time - this.start_time;
	}
	
	/*
	 * Get waiting time
	 */
	public long getWaitingTime(){
		return this.waiting_time;
	}
	
	public void updateWaitingTime(long added){
		this.waiting_time += added;
	}
	
	/*
	 * Get Execution time
	 */
	public long getExecuteTime(){
		return this.execution_time;
	}
	
	/*
	 * Get predict burst time
	 */
	public long getPredictTime(){
		return this.predict_burst;
	}
}
