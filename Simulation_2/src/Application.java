
public class Application {
	public static void main(String[] args) {
		// For question 5.3
		// FCFS
		System.out.println("Question 5.3 FCFS:");
		Process p1 = new Process(1, 0.0, 8.0);
		Process p2 = new Process(2, 0.4, 4.0);
		Process p3 = new Process(3, 1.0, 1.0);
		Queue queue1 = new Queue(Queue.Mode.FCFS, 3);
		queue1.insert(p1);
		queue1.insert(p2);
		queue1.insert(p3);
		queue1.run();
		
		// SJF without idle
		System.out.println("Question 5.3 SJF without idle:");
		p1 = new Process(1, 0.0, 8.0);
		p2 = new Process(2, 0.4, 4.0);
		p3 = new Process(3, 1.0, 1.0);
		Queue queue2_1 = new Queue(Queue.Mode.SJF, 3);
		queue2_1.insert(p1);
		queue2_1.insert(p2);
		queue2_1.insert(p3);
		queue2_1.run();

		// SJF with idle
		System.out.println("Question 5.3 SJF with idle:");
		p1 = new Process(1, 0.0, 8.0);
		p2 = new Process(2, 0.4, 4.0);
		p3 = new Process(3, 1.0, 1.0);
		Queue queue2_2 = new Queue(Queue.Mode.SJF, 3);
		queue2_2.setTime(1.0);
		queue2_2.insert(p1);
		queue2_2.insert(p2);
		queue2_2.insert(p3);
		queue2_2.run();
		
		
		// For question 5.12
		// FCFS
		System.out.println("Question 5.12 FCFS:");
		p1 = new Process(1, 3, 0.0, 10.0);
		p2 = new Process(2, 1, 0.0, 1.0);
		p3 = new Process(3, 3, 0.0, 2.0);
		Process p4 = new Process(4, 4, 0.0, 1.0);
		Process p5 = new Process(5, 2, 0.0, 5.0);
		Queue queue3 = new Queue(Queue.Mode.FCFS, 5);
		queue3.insert(p1);
		queue3.insert(p2);
		queue3.insert(p3);
		queue3.insert(p4);
		queue3.insert(p5);
		queue3.run();
		
		// SJF
		System.out.println("Question 5.12 SJF:");
		p1 = new Process(1, 3, 0.0, 10.0);
		p2 = new Process(2, 1, 0.0, 1.0);
		p3 = new Process(3, 3, 0.0, 2.0);
		p4 = new Process(4, 4, 0.0, 1.0);
		p5 = new Process(5, 2, 0.0, 5.0);
		Queue queue4 = new Queue(Queue.Mode.SJF, 5);
		queue4.insert(p1);
		queue4.insert(p2);
		queue4.insert(p3);
		queue4.insert(p4);
		queue4.insert(p5);
		queue4.run();
		
		// Priority
		System.out.println("Question 5.12 PRIORITY:");
		p1 = new Process(1, 3, 0.0, 10.0);
		p2 = new Process(2, 1, 0.0, 1.0);
		p3 = new Process(3, 3, 0.0, 2.0);
		p4 = new Process(4, 4, 0.0, 1.0);
		p5 = new Process(5, 2, 0.0, 5.0);
		Queue queue5 = new Queue(Queue.Mode.PRIORITY, 5);
		queue5.insert(p1);
		queue5.insert(p2);
		queue5.insert(p3);
		queue5.insert(p4);
		queue5.insert(p5);
		queue5.run();
		
		// RR with time interval = 1
		System.out.println("Question 5.12 RR:");
		p1 = new Process(1, 3, 0.0, 10.0);
		p2 = new Process(2, 1, 0.0, 1.0);
		p3 = new Process(3, 3, 0.0, 2.0);
		p4 = new Process(4, 4, 0.0, 1.0);
		p5 = new Process(5, 2, 0.0, 5.0);
		Queue queue6 = new Queue(Queue.Mode.RR, 5, 1.0);
		queue6.insert(p1);
		queue6.insert(p2);
		queue6.insert(p3);
		queue6.insert(p4);
		queue6.insert(p5);
		queue6.run();
	}
}
