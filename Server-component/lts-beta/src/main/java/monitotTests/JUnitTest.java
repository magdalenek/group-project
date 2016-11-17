package monitotTests;

import static org.junit.Assert.*;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


public class JUnitTest {
	
	public static void main(String args[]) {

		Result result = JUnitCore.runClasses(JUnitTestSuite.class);
		for (Failure failure : result.getFailures()) {
	         System.out.println("Some failures");
	      }
	      System.out.println("ALL PASSED");
	   }
	
	
}
