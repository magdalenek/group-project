package monitotTests;

import monitor.AdaptiveSharingAnalyser;
import monitor.Notification;
import monitor.friendMonitor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

import org.junit.Ignore;


public class friendTest {
	
	private static Logger logger;
	
	private static void setup() {
		String accessToken = "EAAIcsM18IZAsBAKq3ycfLKgrDsME7CJ8IZCtkoexsuHGl8LaMrQLQNA6ItfqxPQJWDetOZC5ibFe9SOwC5gSEk0rYBfbckajSZBLlSvDVNu9Hw4T6ZC8DVIOWnSMyErEzrqK4hYBDXoi7s4dIfZBLoDlKIkXk1MESKA2Yg1ezYnfY3hDZCiilBj"+"";
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		
		User me = fbClient.fetchObject("me", User.class);
		
		//Set userid for the monitor
		String userid = me.getId();
		friendMonitor.setId(userid);
		
		friendMonitor.setDate(new Date());

		Connection<Post> result = fbClient.fetchConnection("me/posts", Post.class);
		List<Notification> notifications = fbClient.fetchConnection("me/notifications", Notification.class).getData();
		
		
		friendMonitor.setNotifications(notifications);
		

		Post[] posts = getPosts(result);
		
		//lets the monitor access the posts
		friendMonitor.setPosts(posts);
	}


	private static Post[] getPosts(Connection<Post> result) {
		int count = 0;
		for(List<Post> page : result) {
			for(Post aPost : page) {
				if(aPost.getComments() != null || aPost.getLikes() != null || aPost.getShares() != null) {count++;}
			}
		}
		
		Post[] posts = new Post[count];
		count = 0;
		
		for(List<Post> page : result) {
			for(Post aPost : page) {
				if(aPost.getComments() != null || aPost.getLikes() != null || aPost.getShares() != null) {
					posts[count++] = aPost;
				}
			}
		}
		return posts;
	}
	
	//lets the monitor access the posts
	private static Logger generateLogger(String name) {

		//Create Logger 
		String loggerName = "A1." + name;
		Logger log = Logger.getLogger(loggerName);

		//Create Logging File Appender
		RollingFileAppender fileApp = new RollingFileAppender();
//		fileApp.setName("A1." + loggerName + "_FileAppender");
//		fileApp.setFile("/homes/beo12/Desktop/"+ name+".txt");
//		fileApp.setLayout(new PatternLayout("%m%n"));
//		fileApp.setThreshold(Level.toLevel("WARN"));
//		fileApp.setAppend(true);
//		fileApp.activateOptions();
//		log.addAppender(fileApp);
		return log;     
	}
	
	@Before
	public void initialize() {
		setup();
		logger = generateLogger("Blah");
	}
	
	
	@Test
	public void testMaliciousFriend() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		friendMonitor maliciousFriend = new friendMonitor("164221520611808", "Magdalena Sadowska", logger);
		maliciousFriend.setMalicious();
		maliciousFriend.run();
		Field field = maliciousFriend.getClass().getDeclaredField("AS_analyser");
		field.setAccessible(true); //override the access restriction of it being private
		AdaptiveSharingAnalyser analyser = (AdaptiveSharingAnalyser) field.get(maliciousFriend);
		assertEquals(1.0, analyser.utility_trade_off_calc(), 0.1); //test that the probability of sharing sensitive information is higher
	}
	
	@Test
	public void testNonMaliciousFriend() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		friendMonitor inactiveFriend = new friendMonitor("123456", "inactive", logger);
		//Monitor inactiveFriend = new Monitor("109339062792761", "Cece Fu", logger);	
		inactiveFriend.run();
		Field field = inactiveFriend.getClass().getDeclaredField("AS_analyser");
		field.setAccessible(true); //override the access restriction of it being private
		AdaptiveSharingAnalyser analyser = (AdaptiveSharingAnalyser) field.get(inactiveFriend);
		assertEquals(1.0, analyser.utility_trade_off_calc(), 0.1);
	}
	
	@Test
	public void testGoodFriend() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		friendMonitor goodFriend = new friendMonitor("109339062792761", "Cece Fu", logger);	
		goodFriend.run();
		Field field = goodFriend.getClass().getDeclaredField("AS_analyser");
		field.setAccessible(true); //override the access restriction of it being private
		AdaptiveSharingAnalyser analyser = (AdaptiveSharingAnalyser) field.get(goodFriend);
		assertEquals(1.0, analyser.utility_trade_off_calc(), 0.1);
	}
}
