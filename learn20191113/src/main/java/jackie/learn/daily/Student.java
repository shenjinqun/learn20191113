package jackie.learn.daily;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Observable;

import jackie.learn.daily.MyAnnotation.ProcessType;

public class Student<T extends Number> extends Observable
		implements Serializable, Cloneable, IStudent<T>, IPasswordFacade {
	
	private static final long serialVersionUID = 1L;

	T id;
	String name;
	String password;
	Teacher Teacher = new Teacher();
	
	public Student(){
	}

	public Student(T id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	@MyAnnotation(process = ProcessType.JUMP)
	public T getId() {
		return id;
	}

	public void setId(T id) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			try {
				StackTraceElement ste = stackTrace[i];
				if ("jackie.learn.daily.Student".equals(ste.getClassName()) && "setId".equals(ste.getMethodName())) {
					Class<?> forName = Class.forName(stackTrace[i + 1].getClassName());
					Method[] methods = forName.getMethods();
					for (int j = 0; j < methods.length; j++) {
						Method method = methods[j];
						MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
						if (method.getName().equals(stackTrace[i+1].getMethodName()) 
								&& annotation.process() == ProcessType.EXECUTE) {
							System.out.println("annotation execute");
							this.id = id;
							return;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("注解触发了annotation jump");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		setChanged();
		notifyObservers();
		return password;
	}

	public void setPassword(String password) {
		if ("123".equals(password)) {
			throw new MyRuntimeException("密码太简单了");
		}
		this.password = password;
	}

	public Teacher getTeacher() {
		return Teacher;
	}

	public void setTeacher(Teacher teacher) {
		Teacher = teacher;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		IStudent<T> clone = (IStudent<T>) super.clone();
		clone.setTeacher((jackie.learn.daily.Teacher) this.getTeacher().clone());
		return clone;
	}

	@Override
	public String toString() {
		return "\nStudent [id=" + id + ", name=" + name + ", password=" + password + ", Teacher=" + Teacher + "]";
	}

}

/**
 * @author Jackie
 *
 */
class Teacher implements Serializable, Cloneable, IPasswordFacade {
	private static final long serialVersionUID = 1L;
	int id;
	String name;
	String password;
	String subject;

	public Teacher() {
	}

	public Teacher(int id, String name, String password, String subject) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.subject = subject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", password=" + password + ", subject=" + subject + "]";
	}
}

class DescStrategy implements Comparator<IStudent<Number>> {
	public int compare(IStudent<Number> o1, IStudent<Number> o2) {
		return (int) (o2.getId().doubleValue() - o1.getId().doubleValue());
	}
}

interface IPasswordFacade {
	void setPassword(String password);
}