package Threads;

import java.util.concurrent.locks.*;

// fails because a reader can not wake up writer(unlock writeLock), cause if it is not the one who locked it before, it
// can not unlock this lock
public class ReadersWritersFailed {
	Lock _readLock;
	Lock _writeLock;
	int _nReaders;
	int _nWriters;
//	Condition notFull = _readLock.newCondition(); 
	
	public ReadersWritersFailed() {
		_readLock = new ReentrantLock();
		_writeLock = new ReentrantLock();
	}
	
	public void run() {
		Thread tReader1 = new Thread(()->read(), "reader1");		
		Thread tReader2 = new Thread(()->read(), "reader2");
		Thread tReader3 = new Thread(()->read(), "reader3");
		Thread tReader4 = new Thread(()->read(), "reader4");
		Thread tWriter1 = new Thread(()->{
			try {
				write();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, "writer1");
		Thread tWriter2 = new Thread(()->{
			try {
				write();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, "writer2");

		tReader1.start(); tReader2.start(); tReader3.start(); tReader4.start();
		tWriter1.start(); tWriter2.start();


	}
	//------------------------------------------------------------------------------------------------------------
	public void read() {
		while (true) {	
			_readLock.lock();
				_nReaders++; // it has to be after the acquiration of the writeLock
				if (_nReaders == 1) {
					_writeLock.lock();
				}
			_readLock.unlock();
			try {
				Thread.sleep((int) (Math.random() * 3000)); // simulating the reading
				System.out.println(Thread.currentThread().getName() +" is Reading");
			} catch (InterruptedException e) {System.out.println("reader cant sleep");} 
			_readLock.lock();
				if (_nReaders == 1) {
					//last reader
					_writeLock.unlock();
				}
			_readLock.unlock();
			try {
				Thread.sleep((int) (Math.random() * 3000)); // doing something with the data
			} catch (InterruptedException e) {e.printStackTrace();} 
		}
	}
	//------------------------------------------------------------------------------------------------------------
	public void write() throws Exception {
		while (true) {
			try {
				Thread.sleep((int) (Math.random() * 3000)); // wait for new data
			} catch (InterruptedException e) {e.printStackTrace();} 
			_writeLock.lock();
				if (_nReaders > 0)
					throw new Exception("There is a reader in the Database, can't write");
				try {
					Thread.sleep((int) (Math.random() * 5000)); // simulating the writing 
				} catch (InterruptedException e) {System.out.println("writer cant sleep");} 
			_writeLock.unlock();
		}
	}
	//------------------------------------------------------------------------------------------------------------
	
}
