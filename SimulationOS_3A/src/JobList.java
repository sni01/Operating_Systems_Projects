import java.util.LinkedList;
import java.util.List;

/*
 * Job list
 * FCFS or SJF
 */
public class JobList {
	public enum JobListType{
		FCFS, SJF
	}
	
	private List<Job> jobs;
	private JobListType mode;
	private long current_time;
	
	public JobList(JobListType mode){
		this.mode = mode;
		this.current_time = 0;
		this.jobs = new LinkedList<Job>();
	}
	
	public boolean isEmpty(){
		return jobs.isEmpty();
	}
	
	public long getCurrentTime(){
		return this.current_time;
	}
	
	public void precedeCurrentTime(long forward_time){
		this.current_time += forward_time;
	}
	
	public void setCurrentTime(long current_time){
		this.current_time = current_time;
	}
	
	public Job getJob(){
		if(jobs.size() == 0) return null;
		
		if(this.mode == JobListType.FCFS){
			return jobs.remove(0);
		}
		else if(this.mode == JobListType.SJF){
			// find shortest waiting job
			int pos = 0;
			while(pos < jobs.size()){
				if(jobs.get(pos).getLastExecutionTime() <= current_time) return jobs.remove(pos);
				pos++;
			}
			
			// if no waiting happens in SJF, find the first arrived job
			pos = 0;
			long tmp = Long.MAX_VALUE;
			int index = 0;
			while(pos < jobs.size()){
				if(tmp > jobs.get(pos).getLastExecutionTime()){
					tmp = jobs.get(pos).getLastExecutionTime();
					index = pos;
				}
				pos++;
			}
			return jobs.remove(index);
		}
		else{
			throw new Error("not valid job list mode");
		}
	}
	
	public Job peek(){
		if(jobs.size() == 0) return null;
		
		if(this.mode == JobListType.FCFS){
			return jobs.get(0);
		}
		else if(this.mode == JobListType.SJF){
			// find shortest waiting job
			int pos = 0;
			while(pos < jobs.size()){
				if(jobs.get(pos).getLastExecutionTime() <= current_time) return jobs.get(pos);
				pos++;
			}
			
			// if no waiting happens in SJF, find the first arrived job
			pos = 0;
			long tmp = Long.MAX_VALUE;
			int index = 0;
			while(pos < jobs.size()){
				if(tmp > jobs.get(pos).getLastExecutionTime()){
					tmp = jobs.get(pos).getLastExecutionTime();
					index = pos;
				}
				pos++;
			}
			return jobs.get(index);
		}
		else{
			throw new Error("not valid job list mode");
		}
	}
	
	public void insertJob(Job job){
		// FCFS strategy
		if(this.mode == JobListType.FCFS){
			FCFS(job);
		}
		// SJF strategy
		else if(this.mode == JobListType.SJF){
			SJF(job);
		}
		// invalid strategy input
		else{
			throw new Error("strategy choice is wrong");
		}
	}
	
	/*
	 * First come first serve, so no need to change anything, just sort by time
	 */
	private void FCFS(Job job){
		int pos = 0;
		boolean added = false;
		while(pos < jobs.size()){
			if(jobs.get(pos).getLastExecutionTime() > job.getLastExecutionTime()){
				jobs.add(pos, job);
				added = true;
				break;
			}
			pos++;
		}
		if(!added){
			jobs.add(job);
		}
	}
	
	/*
	 * Shortest Job First Served strategy, sorted by the remaining execution time.
	 */
	private void SJF(Job job){
		int pos = 0;
		boolean added = false;
		while(pos < jobs.size()){
			if(jobs.get(pos).getPredictTime() > job.getPredictTime()){
				jobs.add(pos, job);
				added = true;
				break;
			}
			pos++;
		}
		
		if(!added){
			jobs.add(job);
		}
		
		return;
	}
	
	public long getUtilizedTime(){
		long sum = 0;
		for(Job job : this.jobs){
			sum += job.getExecuteTime();
		}
		return sum;
	}
	
	public long getWaitingTime(){
		long sum = 0;
		for(Job job : this.jobs){
			sum += job.getWaitingTime();
		}
		return sum;
	}
	
	public long getFinishTime(){
		long largest = 0;
		for(Job job : this.jobs){
			if(largest < job.getLastExecutionTime()) largest = job.getTurnaroundTime();
		}
		return largest;
	}
	
	public long getTurnAround(){
		long sum = 0;
		for(Job job : this.jobs){
			sum += job.getTurnaroundTime();
		}
		return sum;
	}
	
	public int getSize(){
		return this.jobs.size();
	}
}
