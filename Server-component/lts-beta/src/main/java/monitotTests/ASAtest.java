package monitotTests;

import monitor.AdaptiveSharingAnalyser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.restfb.types.User;

import org.junit.Ignore;


public class ASAtest {

	AdaptiveSharingAnalyser maliciousFriend;
	AdaptiveSharingAnalyser nonMaliciousFriend;
	AdaptiveSharingAnalyser goodFriend;
	

	@Test
	public void testMalicious() {
		//create adaptive sharing analyser using malicious probabilities
		//create tests with different info sensitivity levels
		double[] TPs = {0.97, 0.01, 0.01, 0.01, 0.03, 0.07, 0.29, 0.01, 0.01, 0.26, 0.01, 0.01};
		
		maliciousFriend = new AdaptiveSharingAnalyser(TPs, 1);
		assertEquals(0.0, maliciousFriend.utility_trade_off_calc(), 0.1);

		maliciousFriend = new AdaptiveSharingAnalyser(TPs, 0.75);
		assertEquals(0.5, maliciousFriend.utility_trade_off_calc(), 0.1);
		
		maliciousFriend = new AdaptiveSharingAnalyser(TPs, 0.5);
		assertEquals(0.5, maliciousFriend.utility_trade_off_calc(), 0.1);
		
		maliciousFriend = new AdaptiveSharingAnalyser(TPs, 0.25);
		assertEquals(0.5, maliciousFriend.utility_trade_off_calc(), 0.1);
	
		maliciousFriend = new AdaptiveSharingAnalyser(TPs, 0.0);
		assertEquals(1.0, maliciousFriend.utility_trade_off_calc(), 0.1);
	}
	
	
	
	@Test
	public void testGoodFriend() {
		//create adaptive sharing analyser with high probability of good interactions
		double[] TPs = {0.5, 0.1, 0.1, 0.01, 0.01, 0.01, 0.01, 0.01, 0.23, 0.25, 0.1, 0.01};
		
		goodFriend = new AdaptiveSharingAnalyser(TPs, 1);
		assertEquals(1.0, goodFriend.utility_trade_off_calc(), 0.1);
		
		goodFriend = new AdaptiveSharingAnalyser(TPs, 0.75);
		assertEquals(1.0, goodFriend.utility_trade_off_calc(), 0.1);

		goodFriend = new AdaptiveSharingAnalyser(TPs, 0.5);
		assertEquals(1.0, goodFriend.utility_trade_off_calc(), 0.1);
		
		goodFriend = new AdaptiveSharingAnalyser(TPs, 0.25);
		assertEquals(1.0, goodFriend.utility_trade_off_calc(), 0.1);
		
		goodFriend = new AdaptiveSharingAnalyser(TPs, 0.0);
		assertEquals(1.0, goodFriend.utility_trade_off_calc(), 0.1);
	}
	

	
}
