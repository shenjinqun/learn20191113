package jackie.learn.daily;

public class MyRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public MyRuntimeException() {
	}

	public MyRuntimeException(String message) {
		super(message);
	}
}