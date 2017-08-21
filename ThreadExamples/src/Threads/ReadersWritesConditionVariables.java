package Threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritesConditionVariables {
//	ReentrantLock readersLock = new ReentrantLock();
	ReentrantLock lock = new ReentrantLock();
	Condition noReaders = lock.newCondition();
	Condition notWriting = lock.newCondition();
	int nReaders;
	boolean writing;
	boolean reading;
	int nWriters;
	
	public void run() {
		Thread t1 = new Thread(()->read(), "Reader1"); // create thread using lambda expression instead of anonymous runnable
		Thread t2 = new Thread(()->read(), "Reader2");
		Thread t3 = new Thread(()->write(), "Writer1");
		Thread t4 = new Thread(()->write(), "Writer2");
		t1.start(); t2.start(); 
		t3.start(); t4.start();

	}
	
	public void read() {	
		try {  
			while (true) {

				// try always work with try and finally to not forget to unlock
				lock.lock();
				while (writing || nWriters > 0) { // nWriters is used to give advantage to writers
					notWriting.await();
				}
				nReaders++;
				if (nWriters > 0)
					System.out.println("There is a writer");
				lock.unlock();
				Thread.sleep((int) (Math.random() * 3000)); // simulating the reading
				System.out.println(Thread.currentThread().getName() +" is Reading");
				lock.lock();
				nReaders--;
				if (nReaders == 0) 
					noReaders.signalAll();
				lock.unlock();
				Thread.sleep((int) (Math.random() * 3000)); // doing something with the data
			}
		}		
		catch(InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
//		finally {
//			lock.unlock();
//		}	
	}
	public void write() {	
		try {  
			while (true) {
				Thread.sleep((int) (Math.random() * 3000)); // wait for new data

				lock.lock();
				while (nReaders > 0 || writing) { // we have the writing checking to prevent deadlock between a writer that is 
					// intent to write and a writer that is writing
					if (nReaders > 0)
						noReaders.await();
					else
						notWriting.await();
				}
				nWriters++;
				writing = true;
				if (nWriters > 1)
					System.out.println("Two writers !!!!!");
				if (nReaders > 0)
					System.out.println("There is a reader !!!!!!");
				lock.unlock();
				Thread.sleep((int) (Math.random() * 5000)); // simulating the writing 
				System.out.println("==="+Thread.currentThread().getName() +" is writing====");	
				lock.lock();
				writing = false;
				notWriting.signalAll();
				nWriters--;
				lock.unlock();
			}
		}		
		catch(InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
//		finally {
//			lock.unlock();
//		}	
	}
	
	//===========================================================================================================

	public class Reader extends Thread{
		ReadersWritesConditionVariables rw;
		
		
		@Override
		public void run() {
			
		}
	}
	
	public class Writer extends Thread{
		ReadersWritesConditionVariables rw;

		@Override
		public void run() {
			
		}
	}
	
}
