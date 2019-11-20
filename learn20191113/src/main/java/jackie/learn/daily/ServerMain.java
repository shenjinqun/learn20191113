package jackie.learn.daily;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jackie.learn.daily.MyAnnotation.ProcessType;

public class ServerMain {
	public static List<IStudent<Number>> studentList = new ArrayList<IStudent<Number>>();
	
	public static void main(String[] args) throws InterruptedException {
		ReceiveInfoThread receiveInfoThread = new ReceiveInfoThread();
		receiveInfoThread.start();
		receiveInfoThread.join();
		parseStudent(studentList.get(0));
	}
	
	@MyAnnotation(process=ProcessType.JUMP)
	public static void parseStudent( IStudent<Number> student) {
		
		IPasswordFacade passwordFacade = (IPasswordFacade) student;
		passwordFacade.setPassword("abcdef");
		passwordFacade = student.getTeacher();
		passwordFacade.setPassword("abcdef");
		System.out.println("门面模式触发了");
		
		student.addObserver(new MyConsole());
		student.addObserver(new MyLog());
		student.getPassword();
		
		IStudent<Number> s2 = (IStudent<Number>) MyProxy.factory(student);
		s2.setPassword("123");
		
		student.setId(111);
	}
}

class ReceiveInfoThread extends Thread{
	@Override
	public void run() {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(5000));
			Selector selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			ByteBuffer byteBuffer = ByteBuffer.allocate(50);
			
			while(true) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				for (Iterator iterator = selectedKeys.iterator(); iterator.hasNext();) {
					SelectionKey selectionKey = (SelectionKey) iterator.next();
				
					if( selectionKey.isAcceptable()) {
						SelectableChannel selectableChannel = serverSocketChannel.accept();
						selectableChannel.configureBlocking(false);
						selectableChannel.register(selector, SelectionKey.OP_READ);
					}
					
					if( selectionKey.isReadable()) {
						SocketChannel channel = (SocketChannel) selectionKey.channel();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while( channel.read(byteBuffer) > 0 ) {
							baos.write(byteBuffer.array());
							byteBuffer.flip();
						}
						ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
						Object readObject = objectInputStream.readObject();
						ServerMain.studentList = (List<IStudent<Number>>) readObject;
						System.out.println(readObject);
						return;
					}
				}
				selectedKeys.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}