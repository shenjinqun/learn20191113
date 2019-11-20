package jackie.learn.daily;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Observable;
import java.util.Observer;

public class MyProxy implements InvocationHandler{
	Object object;

	public MyProxy( Object object) {
		this.object = object;
	}

	public static Object factory( Object o ) {
		return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), new MyProxy(o));
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			System.out.println("before proxy");
			Object invoke = method.invoke(object, args);
			System.out.println("after proxy");
			return invoke;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("代理模式触发了-" + e.getCause().getMessage());
		}
		return null;
	}
}

class MyConsole implements Observer{
	public static void Info( String s) {
		System.out.println("日志输出" + s);
	}

	public void update(Observable o, Object arg) {
		Info("观察者模式触发了");
	}
}

class MyLog implements Observer{
	static BufferedWriter bufferedWriter=null;
	static {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("d://log.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Info( String s) {
		try {
			bufferedWriter.write("输出到文件：" + s );
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Observable o, Object arg) {
		Info("观察者模式触发了");
	}
}