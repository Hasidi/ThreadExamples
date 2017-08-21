package Threads;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
//import java.util.concurrent.locks.*;


public class ProducerConsumerSemaphore {
	public static int MAX_SIZE = 10;
	public static Random RAND = new Random();
	private int i;
	public Semaphore sFull;
	public Semaphore sEmpty;
	private ArrayList<Integer> elements;
	
	public ProducerConsumerSemaphore() {
		sFull = new Semaphore(MAX_SIZE);
		sEmpty = new Semaphore(0);
		i = 0;
		elements = new ArrayList<Integer>();
	}
	public void run() {
        new Producer(this);
        new Consumer(this);
        new Consumer(this);
        new Consumer(this);
	}
	
	private int Produce() {
		try {
			Thread.sleep(RAND.nextInt(3000)+1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i++;
		
	}
	private void Consume() {
		try {
			Thread.sleep(RAND.nextInt(3000)+1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void Append(int x) {
//		boolean b = lock.tryLock();
//		if (!b) {
//			System.out.println(Thread.currentThread().getName() + " is waiting for the lock");
//			lock.lock();
//		}
		elements.add(x);
		System.out.println(Thread.currentThread().getName() + " puts: " + x);
//		System.out.println(Thread.holdsLock(lock));
//		lock.unlock();
	}
	private void Take() {
//		lock.lock();
		int x = elements.remove(0);
		System.out.println(Thread.currentThread().getName() + " removed: " + x);
//		lock.unlock();
	}
	
	static //===============================================================================================================
	class Producer extends Thread{
		static int N = 1;
		ProducerConsumerSemaphore pc;
		
		public Producer(ProducerConsumerSemaphore pc) {
			this.pc = pc;
			new Thread(this, "tProducer"+N++).start();
		}
		@Override
		public void run() {
			while (true) {
				int x = pc.Produce();
				try {
					boolean b = pc.sFull.tryAcquire();
					if (!b) {
	            		System.out.println("Queue is full, Thread: "+
	            				Thread.currentThread().getName()+ " is waiting");
	            		pc.sFull.acquire();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pc.Append(x);
				pc.sEmpty.release();		
			}
		}
	}
	//===============================================================================================================
	
	static class Consumer extends Thread{
		static int N = 1;
		ProducerConsumerSemaphore pc;
		
		public Consumer(ProducerConsumerSemaphore pc) {
			this.pc = pc;
			new Thread(this, "tConsumer"+N++).start();
		}
		@Override
		public void run() {
			while (true) {
				try {
					boolean b = pc.sEmpty.tryAcquire();
					if (!b) {
	            		System.out.println("Queue is empty, Thread: "+
	            				Thread.currentThread().getName()+ " is waiting");
	            		pc.sEmpty.acquire();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pc.Take();
				pc.sFull.release();
				pc.Consume();
			}
		}
	}
	//===============================================================================================================

}
