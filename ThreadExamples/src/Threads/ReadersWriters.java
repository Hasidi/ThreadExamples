package Threads;

import java.util.concurrent.Semaphore;

public class ReadersWriters {
	static int NreaderThreads = 0;
	static int NWriterThreads = 0;

	Semaphore _readLock;
	Semaphore _writeLock;
	int _nReaders;
	boolean _writing;
//	Condition notFull = _readLock.newCondition(); 
	
	public ReadersWriters() {
		_readLock = new Semaphore(1); // it means DB can be read
		_writeLock = new Semaphore(1); // it means DB can be written
	}
	
	public void run() {
        new Reader(); new Reader(); new Reader(); new Reader();
        new Writer(); new Writer();


	}
	//------------------------------------------------------------------------------------------------------------
	public void read() throws Exception {
		while (true) {	
			try {
				_readLock.acquire();
				_nReaders++; // it has to be after we acquire the writeLock
				if (_nReaders == 1) {
					_writeLock.acquire();
//					_nReaders++; 
				}
				_readLock.release();			
				if (_writing)
					throw new Exception("There is a writer in the Database, can't read");
				else {
					Thread.sleep((int) (Math.random() * 3000)); // simulating the reading
					System.out.println(Thread.currentThread().getName() +" is Reading");
				}			
				_readLock.acquire();		
				_nReaders--;
				if (_nReaders == 0) {
					//it was the last reader
					_writeLock.release();
				}				
				_readLock.release();			
				Thread.sleep((int) (Math.random() * 3000)); // doing something with the data
			} 
			catch (InterruptedException e) {
				e.printStackTrace();		
			}
		}
	}
	//------------------------------------------------------------------------------------------------------------
	public void write() throws Exception {
		while (true) {
			try {
				Thread.sleep((int) (Math.random() * 3000)); // wait for new data
			
				_writeLock.acquire();
				int x = 0;
				if (_nReaders > 0)
//					throw new Exception("There is a reader in the Database, can't write");
					x=0;
				else {
					_writing = true;
					Thread.sleep((int) (Math.random() * 5000)); // simulating the writing 
					System.out.println("==="+Thread.currentThread().getName() +" is writing====");					
				}
				_writing = false;
				_writeLock.release();			
			}	
			catch (InterruptedException e) {
				e.printStackTrace();		
			}
		}
	}
	//===========================================================================================================
	class Reader extends Thread{
		public Reader() {
			new Thread(this, "tReader"+ ++NreaderThreads).start();
		}
		@Override
		public void run() {
				try {
					read();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	//===========================================================================================================

	class Writer extends Thread{
		public Writer() {
			new Thread(this, "tWriter"+ ++NWriterThreads).start();
		}
		@Override
		public void run() {
				try {
					write();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
