package Threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Lock l = new ReentrantLock();
//		l.lock();
//		System.out.println("sdaffads");
//		l.lock();
//		System.out.println("aaa");
		
//        ProducerConsumer pc = new ProducerConsumer();
//        pc.run();
//		ProducerConsumerSemaphore pc = new ProducerConsumerSemaphore();
//		pc.run();
		
//		ArrayList<Integer> mylist = new ArrayList<>();
//		mylist.remove(0);
		
//        IteratorFailFastTest tester = new IteratorFailFastTest();
//        
//        tester.runIteratorThread();
//        tester.runUpdateThread();
		
//		ReadersWritersFailed rw = new ReadersWritersFailed();
//		rw.run();
		
//		ReadersWriters rw = new ReadersWriters();
//		rw.run();
		
//		new FizzBuzz().run(50);
		
		ReadersWritesConditionVariables rw = new ReadersWritesConditionVariables();
		rw.run();
	}

}
