
public class Process {
	private int id;
	private int priority;
	private double arrive_time;
	private double finish_time;
	private double execution_time;
	private double remain_execution_time;
	
	/*
	 * Constructor without priority
	 */
	public Process(int id, double arrive_time, double execution_time){
		this.id = id;
		this.arrive_time = arrive_time;
		this.finish_time = arrive_time; // init with first arrival time
		this.execution_time = execution_time;
		this.remain_execution_time = execution_time;
	}
	
	public Process(int id, int priority, double arrive_time, double execution_time){
		this(id, arrive_time, execution_time);
		this.priority = priority;
	}
	
	public double getExecutionTime(){
		return this.execution_time;
	}
	
	public double getTurnaroundTime(){
		return this.finish_time - this.arrive_time;
	}
	
	public double getFinishTime(){
		return this.finish_time;
	}
	
	public void setFinishTime(double finish_time){
		this.finish_time = finish_time;
	}
	
	public double getArrivalTime(){
		return this.arrive_time;
	}
	
	public double getRemainExecutionTime(){
		return this.remain_execution_time;
	}
	
	public void updateRemainExecutionTime(double forward){
		this.remain_execution_time -= forward;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public int getId(){
		return this.id;
	}
}
