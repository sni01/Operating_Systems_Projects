import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


public class Queue {
	public enum Mode{
		FCFS, SJF, PRIORITY, RR
	}
	private double time;
	private double quantum;
	private List<Process> processes;
	private Queue.Mode mode;
	private int size;
	
	/*
	 * Constructor with Comparator
	 */
	public Queue(Queue.Mode mode, int size){
		this.time = 0;
		this.quantum = Integer.MAX_VALUE;
		this.mode = mode;
		this.size = size;
		this.processes = new LinkedList<Process>();
	}
	
	public Queue(Queue.Mode mode, int size, double quantum){
		this(mode, size);
		this.quantum = quantum;
	}
	
	/*
	 * Idle for while
	 */
	public void setTime(double time){
		this.time += time;
	}
	
	/*
	 * Main Logic here
	 */
	public void run(){
		System.out.println("==================");
		double SumTAT = 0.0; // sum of Turn Around Time
		double SumWT = 0.0; // sum of Waiting time
		while(!isEmpty()){
			Process p = this.get();
			// update under waiting situation
			if(p.getFinishTime() < this.time){
				p.setFinishTime(this.time);
			} else{
				this.time = p.getFinishTime();
			}
			
			// how long to execute
			double execute_time = p.getRemainExecutionTime() < this.quantum ? p.getRemainExecutionTime() : this.quantum;
			p.updateRemainExecutionTime(execute_time);
			
			// update time
			System.out.println("Process " + p.getId() + ": " + time + " " + (time + execute_time));
			this.time += execute_time;
			
			p.setFinishTime(time);
			
			if(p.getRemainExecutionTime() <= 0){
				// store turn around time
				SumTAT += p.getTurnaroundTime();
				SumWT += p.getTurnaroundTime() - p.getExecutionTime();
				
				// average waiting time
				System.out.println("Process " + p.getId() + " waiting time :" + (p.getTurnaroundTime() - p.getExecutionTime()));
				System.out.println("Process " + p.getId() + " turnaround time :" + (p.getTurnaroundTime()));
			}
			else{
				insert(p);
			}
			
		}
		
		System.out.println("average turnAround time :" + SumTAT / (double) this.size);
		System.out.println("average waiting time :" + SumWT / (double) this.size);
	}
	
	
	/*
	 * Insert process by arriving time
	 */
	public void insert(Process process){
		int pos = 0;
		boolean added = false;
		while(pos < processes.size()){
			if(isFront(process, processes.get(pos))){
				processes.add(pos, process);
				added = true;
				break;
			}
			pos++;
		}
		if(!added){
			processes.add(process);
		}
	}
	
	/*
	 * Comparator
	 */
	public boolean isFront(Process p1, Process p2){
		if(this.mode == Queue.Mode.FCFS || this.mode == Queue.Mode.RR){
			return p1.getArrivalTime() < p2.getArrivalTime() ? true : false;
		}
		else if(this.mode == Queue.Mode.SJF){
			return p1.getRemainExecutionTime() < p2.getRemainExecutionTime() ? true : false;
		}
		else{
			return p1.getPriority() < p2.getPriority() ? true : false;
		}
	}
	
	
	/*
	 * Get next process
	 */
	public Process get(){
		if(this.mode == Queue.Mode.FCFS || this.mode == Queue.Mode.RR){
			return FCFS();
		}
		else{
			return SJF();
		}
	}
	
	/*
	 * FCFS & RR
	 */
	public Process FCFS(){
		return processes.remove(0);
	}
	
	/*
	 * SFJ & Priority
	 */
	public Process SJF(){
		int pos = 0;
		while(pos < processes.size()){
			if(processes.get(pos).getFinishTime() <= this.time) return processes.remove(pos);
			pos++;
		}
		return processes.remove(0);
	}
	
	
	public boolean isEmpty(){
		return processes.isEmpty();
	}
}
