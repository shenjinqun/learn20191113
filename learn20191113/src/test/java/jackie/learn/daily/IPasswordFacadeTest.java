package jackie.learn.daily;
	
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jackie.learn.daily.IPasswordFacade;;

public class IPasswordFacadeTest {

	@Rule
	public ExpectedException thrown=ExpectedException.none();

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
	//测试是否含有 setName 方法
	public void setNameTest() throws NoSuchMethodException, SecurityException{
		IPasswordFacade.class.getMethod("setPassword", String.class);
	}
}