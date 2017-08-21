
import java.util.ArrayList;
import java.util.Random;

public class ProducerConsumer {
	 public static int MAX_SIZE = 10;
 	 public static int i = 0;
 	 public static Random RAND = new Random();

	 public void run() {
	        ArrayList<Integer> elements = new ArrayList<Integer>();
	        new Producer(elements);
	        new Producer(elements);
	        new Producer(elements);
	        new Producer(elements);
	        new Consumer(elements);
	        new Consumer(elements);

    }

	//========================================================================================================================
    class Producer extends Thread{
    	ArrayList<Integer> _list;
    	//we can lock this list cause all threads get the same object of _list. they just use reference to this _list
        public Producer(ArrayList<Integer> list) {
            _list = list;
            new Thread(this, "tProducer").start();
        }
        @Override
        public void run() {
        	while (true) {
	            try {
					Thread.sleep(RAND.nextInt(3000)+1000); // produce the item takes some time
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
	            synchronized(_list) {
		            while (_list.size() == MAX_SIZE){
		            		System.out.println("Queue is full, Size="+_list.size()+", Thread: "+
		            				Thread.currentThread().getName()+ " is waiting");
		            		try {
								_list.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	}
	
		            _list.add(i);
		            System.out.println("Thread- " +Thread.currentThread().getName()+ " produce the item: "+i);
		            i++;
		            _list.notifyAll();	            
	            }
	        }
        }
    }
    //========================================================================================================================

    class Consumer extends Thread{
    	ArrayList<Integer> _list;
        public Consumer(ArrayList<Integer> list) {
            _list = list;
            new Thread(this, "tConsumer").start();
        }
        @Override
        public void run() {
        	while(true) {
	            try {
	            	
					Thread.sleep(RAND.nextInt(3000)+1000); // produce the item takes some time
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
	            synchronized(_list) {
		            while (_list.size() == 0){
		            		System.out.println("Queue is empty, Thread: "+
		            				Thread.currentThread().getName()+ " is waiting");
		            		try {
								_list.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            	}
	
		            Integer elem = (Integer)_list.remove(0);
		            System.out.println("Thread- " +Thread.currentThread().getName()+ " removed the item: "+elem);
		            _list.notifyAll();	            
	            }
	        }
        }
    }
    //========================================================================================================================

}
