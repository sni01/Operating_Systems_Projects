import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/*
 * Initial processes and Main function of simulation here
 */


public class Application {
	/*
	 * Initial processes and execute simulation till all jobs are finished.
	 */
	public static void main(String[] args) {
		int number_of_jobs = 10;
		long time_slice = Long.MAX_VALUE; // FCFS without RR
		long IOTime = 60; // IO request time is constantly 60 ms
		long left_time_range_in_min = 2;
		long right_time_range_in_min = 4;
		double ratio = 1.0;
		
		JobList.JobListType type = JobList.JobListType.FCFS;
		// FCFS
		test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice, IOTime, 1, null);
		
		// SJF with ratio == 1.0
		type = JobList.JobListType.SJF;
		test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice, IOTime, 2, null);
        
		// SJF with ratio == 0.5
		ratio = 0.5;
		test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice, IOTime, 3, null);
		
		// SJF with ratio == 1/3
		ratio = (1.0) / (3.0);
		test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice, IOTime, 4, null);
		
		// RR Experiments
		// first experiment
		time_slice = 1;
		ratio = 1.0;
		type = JobList.JobListType.FCFS;
		
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("format_file.txt"), true)));
			for(int i = 0; i < 90; i++){
				test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice + i, IOTime, i + 5, bw);
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// second experiment
//		time_slice = 32;
//		type = JobList.JobListType.FCFS;
//		for(int i = 0; i < 3; i++){
//			test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice + 5 * i, IOTime, 11 + i * 2);
//			test(type, number_of_jobs, left_time_range_in_min, right_time_range_in_min, ratio, time_slice + 5 * i + 3, IOTime, 12 + i * 2);
//		}
	}
	
	public static void test(JobList.JobListType type, int number_of_jobs, long left_time_range_in_min, long right_time_range_in_min, double ratio, long time_slice, long IOTime, int cpu_id, BufferedWriter bw){
		// generate a list of jobs with interrupts mean values ranges from 30ms to 75ms
		List<Job> jobs = new LinkedList<Job>();
		long mean = 30;
		for(int i = 0; i < number_of_jobs; i++){
			long tmp = Job.getUniform(left_time_range_in_min, right_time_range_in_min);
			Job job = new Job(i, mean, tmp, type);
			if(type == JobList.JobListType.FCFS){
				job.setInterrupts(); // initial first interrupt time.
			}
			else{
				job.setRatio(ratio);
				job.setInterruptsSJF();
			}
			jobs.add(job);
			mean += 5;
		}
		
		CPU cpu = new CPU(time_slice, type, IOTime, cpu_id);
		cpu.init(jobs);
		cpu.run();
		
		
		
		// output
		// cpu.print();
		System.out.println("==============================");
		if(time_slice < Long.MAX_VALUE){
			System.out.println("Test " + type.toString() + " with quantum" + time_slice + " :");
		}
		else{
			System.out.println("Test " + type.toString() + " with ratio" + ratio + " :");
		}
		
		// collect data for question 3c
		if(bw != null){
			try {
				bw.write("" + cpu.avg_waiting_time());
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println();
		System.out.println("TurnAround Time :" + cpu.turnAround() + " minutes/job");
		System.out.println("Avg Waiting time : " + cpu.avg_waiting_time() + " minutes/job");
		System.out.println("CPU Uitilization: " + (cpu.utilized_rate() * 100) + "%");
		System.out.println("==============================");
	}
}
