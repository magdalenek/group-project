package monitotTests;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

import org.junit.Ignore;


public class RestFBTest {
	
	private static Post[] posts;
	private static String userid;
	//Monitor.setNotifications(notifications);
	
	private static List<String[]> users;
	private static List<String> ids = new ArrayList<String>();
	private static List<String> names = new ArrayList<String>();
	
	private static void setup() {
		String accessToken = "CAACEdEose0cBAMFvRKazVrOm5jQhEF2SDUKct681cq59E3jGgw163Hh4kK2lfrdsbZC9oSc1BD6ZBA0SVlPpE6EarNLXpvD9vtsJlvB4wWxtYLEamxCboYNWCJB3N8rgazFWmSir5o5JjJ8cloKzU4Yt2kKPBMjRKTpim1t6ZBdzJRCsLvpU8a31jU26ygN2zZBiKla2agZDZD"+"";
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		
		User me = fbClient.fetchObject("me", User.class);
		
		//Set userid for the monitor
		userid = me.getId();
		System.out.println(userid);
		Connection<Post> result = fbClient.fetchConnection("me/posts", Post.class);
		posts = getPosts(result);
		
		users = getID_name(posts);
		
		
		for(String[] user : users) {
			ids.add(user[0]);
			names.add(user[1]);
		}
	}
	

	
	private static List<String[]> getID_name(Post[] posts) {
		List<String[]> users = new ArrayList<String[]>();
		for(Post aPost : posts) {
			if(aPost.getComments() != null) {
				List<Comment> comments = aPost.getComments().getData();
				for(Comment com : comments) {
					String[] user = new String[2];
					user[0] = com.getFrom().getId();
					user[1] = com.getFrom().getName();
					boolean is=true;
					for(String[] check : users) {
						if(check[0].equals(user[0])) {is=false; break;}
					}
					if(is){users.add(user);}
				}
			}
			
			if(aPost.getLikes() != null) {
				List<NamedFacebookType> likes = aPost.getLikes().getData();
				for(NamedFacebookType like : likes) {
					String[] user = new String[2];
					user[0] = like.getId();
					user[1] = like.getName();
					boolean is=true;
					for(String[] check : users) {
						if(check[0].equals(user[0])) {is=false; break;}
					}
					if(is){users.add(user);}
				}
			}
		}
		return users;
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
	

	@BeforeClass
	public static void initialize() {
		setup();
	}
	
	/*
	 * check userid is correct
	 */
	@Test
	public void userNotNull() {
		assertEquals("121622944896037", userid);
	}
	
	/*
	 * check posts are not null
	 */
	@Test
	public void testPostsNotNull() {
		for(Post post : posts) {
			assertNotNull(post);
		}
	}
	
	/*
	 * check that friend names are retrieved
	 */
	@Test
	public void testFriendName1() {
		assertTrue(names.contains("Cece Fu"));
	}
	
	@Test
	public void testFriendName2() {
		assertTrue(names.contains("Magdalena Sadowska"));
	}
	
	@Test
	public void testFriendID1() {
		
	}

	@Test
	public void testFriendID2() {
		
	}
}
