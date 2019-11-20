package jackie.learn.daily;
	
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jackie.learn.daily.IStudent;
import jackie.learn.daily.MyAnnotation;
import jackie.learn.daily.ServerMain;
import jackie.learn.daily.Student;
import jackie.learn.daily.MyAnnotation.ProcessType;

public class MyAnnotationTest {

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

	@Test
	@MyAnnotation(process=ProcessType.JUMP)
	public void MyAnnotationTest() {
		IStudent<Integer> student = new Student(1,"zhangsan","123456");
		student.setId(1234);
		Assert.assertNotEquals(new Integer(1234), student.getId());
	}
	
	/**
	 * 
	 */
	@Test
	@MyAnnotation(process=ProcessType.JUMP)
	public void ServerMainAnnotationTest() {
		MyAnnotation annotation;
		try {
			annotation = ServerMain.class.getMethod("parseStudent", IStudent.class).getAnnotation(MyAnnotation.class);
			System.out.println(annotation.process());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}