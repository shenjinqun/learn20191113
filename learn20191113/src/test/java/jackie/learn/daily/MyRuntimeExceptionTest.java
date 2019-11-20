package jackie.learn.daily;
	
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jackie.learn.daily.MyRuntimeException;

public class MyRuntimeExceptionTest {
 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//测试构造方法
	@Test
	public void MyRuntimeExceptionMethodTest() throws NoSuchMethodException, SecurityException{
		MyRuntimeException.class.getConstructor(String.class);
	}
}
