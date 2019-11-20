package jackie.learn.daily;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jackie.learn.daily.MyAnnotation.ProcessType;

public class ClientMain {
	public static List<IStudent<Number>> studentList = new ArrayList<IStudent<Number>>();
	public static IStudent<Number> student = null;
	volatile static boolean completedFlag = false;
	static {
		try {
			Class<?> forName = Class.forName( "jackie.learn.daily.Student"	);
			Constructor<?> constructor = forName.getConstructor(Number.class, String.class, String.class);
			student = ((IStudent<Number>) constructor.newInstance(1, "zhangsan", "123456"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, Exception {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		new Thread( new Runnable() {
			public void run() {
				while( !completedFlag ) {}
				countDownLatch.countDown();
			}
		}).start();
		
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);
		newFixedThreadPool.execute(new StudentThread(0, student));
		newFixedThreadPool.execute(new StudentThread(1, student));
		newFixedThreadPool.execute(new StudentThread(2, student));

		countDownLatch.await();
		Collections.sort(ClientMain.studentList, new DescStrategy());
		System.out.println(ClientMain.studentList);

		Socket socket = new Socket("localhost",5000);
		OutputStream outputStream = socket.getOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
		objectOutputStream.writeObject(ClientMain.studentList);
		bufferedOutputStream.flush();
	}
}

class StudentThread implements Runnable {
	int mode;
	IStudent<Number> student;
	
	public StudentThread(int mode, IStudent<Number> student) {
		this.mode = mode;
		this.student = student;
	}

	public void run() {
		for( int i = 0; i <2; i++ ) {
			synchronized (ClientMain.studentList) {
				while( ClientMain.studentList.size() % 3 != mode ) {
					try {
						ClientMain.studentList.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				StudentProcess.newStudent(ClientMain.student);
				ClientMain.studentList.notifyAll();
			}
		}
		if( mode == 2 ) {
			ClientMain.completedFlag = true;
		}
	}
}


class StudentProcess{
	@MyAnnotation(process=ProcessType.EXECUTE)
	public static void newStudent( IStudent<Number> student) {
		try {
			IStudent<Number> clone = (IStudent<Number>) student.clone();
			int id = 1 + ClientMain.studentList.size();
			clone.setId(id);
			clone.setName( "xm_" + id);
			clone.setPassword( "mm_" + id);
			ClientMain.studentList.add(clone);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}