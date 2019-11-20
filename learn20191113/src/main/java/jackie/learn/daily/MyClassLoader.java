package jackie.learn.daily;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;

public class MyClassLoader extends ClassLoader{
	

	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		String path = Class.class.getResource("/").getPath().replace("test-", "");
		path += name.replace(".", "/") + ".class";
		System.out.println(path);
		
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(path));
			byte[] bs = new byte[fileInputStream.available()];
			int read = fileInputStream.read(bs);
			Class<?> defineClass = defineClass(name, bs, 0, bs.length);
			return defineClass;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		MyClassLoader myClassLoader = new MyClassLoader();
		myClassLoader.findClass("jackie.learn.daily.IPasswordFacade");
		myClassLoader.findClass("jackie.learn.daily.IStudent");
		myClassLoader.findClass("jackie.learn.daily.Teacher");
		Class<?> findClass = myClassLoader.findClass("jackie.learn.daily.Student");
		Constructor<?> constructor = findClass.getConstructor(Number.class,String.class, String.class);
		Object newInstance = constructor.newInstance(1,"张三","123456");
		System.out.println(newInstance);
	}
}