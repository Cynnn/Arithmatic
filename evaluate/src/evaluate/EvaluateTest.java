package evaluate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EvaluateTest {

	@Before
	public void setUp() throws Exception {
	} 
	
	@Test
	public void testEvaluateAlgorithm() {
		assertEquals("83",Evaluate.evaluateAlgorithm("4*1/2+9¡Â1/9"));
		assertEquals("19/12",Evaluate.evaluateAlgorithm("2+1/3-3/4"));
		assertEquals("226/9",Evaluate.evaluateAlgorithm("4/6-(2/3-8)¡Â(6¡Â5*1/4)"));
	}
	@Test
	public void testJudgePriority() {		
		assertTrue(Evaluate.JudgePriority('+', '*'));
	}

	@Test
	public void testCaculate() {
		int result1[] = Evaluate.caculate('+', 2, 3, 1, 6);
		assertEquals(15,result1[0]);
		assertEquals(18,result1[1]);
		int result2[] = Evaluate.caculate('-', 1, 2, 2, 3);
		assertEquals(1,result2[0]);
		assertEquals(6,result2[1]);
		int result3[] = Evaluate.caculate('*', 3, 5, 2, 9);
		assertEquals(6,result3[0]);
		assertEquals(45,result3[1]);
		int result4[] = Evaluate.caculate('¡Â', 3, 4, 6, 8);
		assertEquals(24,result4[0]);
		assertEquals(24,result4[1]);
	}

	@Test
	public void testGCD() {
		assertEquals(15,Evaluate.GCD(45,15));
		assertEquals(4,Evaluate.GCD(-8,12));
		assertEquals(5,Evaluate.GCD(-10,-25));
	}
	


}
