package jackie.learn.daily;

import java.util.Observer;

import jackie.learn.daily.MyAnnotation.ProcessType;

public interface IStudent<T extends Number> {

	T getId();

	void setId(T id);

	String getName();

	void setName(String name);

	String getPassword();

	void setPassword(String password);

	Teacher getTeacher();

	void setTeacher(Teacher teacher);

	Object clone() throws CloneNotSupportedException;

	String toString();

	public void addObserver(Observer o) ;
}