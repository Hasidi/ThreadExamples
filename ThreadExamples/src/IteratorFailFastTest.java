

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IteratorFailFastTest {
	private List<Integer> list = new ArrayList<>();
	private static Lock lock = new ReentrantLock();
//    public IteratorFailFastTest() {
//        for (int i = 0; i < 10_000; i++) {
//            list.add(i);
//        }
//    }
 
    public void runUpdateThread() {
        Thread thread1 = new Thread(new Runnable() {
 
            public void run() {
                for (int i = 0; i < 100; i++) {
                    list.add(i);
                    System.out.println("item "+ i + " was added");
                }
            }
        });
 
        thread1.start();
    }
 
 
    public void runIteratorThread() {
        Thread thread2 = new Thread(new Runnable() {
 
            public void run() {
                for (int i = 0; i < 100; i++) {
                	Object o = null;
                	lock.lock();
                	if (list.size() > 0) {
                		o = list.remove(0);
                        System.out.println("item "+ o + " was deleted");
                	}
            		lock.unlock();

                	
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
        	 
            public void run() {
                for (int i = 0; i < 100; i++) {
                	Object o = null;
                	lock.lock();
                	if (list.size() > 0) {
                		o = list.remove(0);
                        System.out.println("item "+ o + " was deleted");
                	}
            		lock.unlock();

                	
                }
            }
        });
        thread2.start();
        thread3.start();
    }
 
    public static void main(String[] args) {
        IteratorFailFastTest tester = new IteratorFailFastTest();
 
        tester.runIteratorThread();
        tester.runUpdateThread();
    }
}
