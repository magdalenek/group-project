package monitotTests;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import monitor.Monitor;
import monitor.friendMonitor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.FacebookType;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.Post.Likes;
import com.restfb.types.User;

public class SimpleTest2 {
	
	/*
	private static List<String> getids(Post[] posts) {
		List<String> ids = new ArrayList<String>();
		for(Post aPost : posts) {
			if(aPost.getComments() != null) {
				List<Comment> comments = aPost.getComments().getData();
				for(Comment com : comments) {
					String id = com.getFrom().getId();
					if(!ids.contains(id)) {ids.add(id);}
				}
			}
			 
			if(aPost.getLikes() != null) {
				List<NamedFacebookType> likes = aPost.getLikes().getData();
				for(NamedFacebookType like : likes) {
					String id = like.getId();
					if(!ids.contains(id)) {ids.add(id);}
				}
			}
		}
		return ids;
	}*/
	private static List<String[]> users;
	private static List<friendMonitor> monitors;
	
 	/*
	private static List<CategorizedFacebookType> getUsers(Post[] posts) {
		List<CategorizedFacebookType> users = new ArrayList<CategorizedFacebookType>();
		for(Post aPost : posts) {
			if(aPost.getComments() != null) {
				List<Comment> comments = aPost.getComments().getData();
				for(Comment com : comments) {
					CategorizedFacebookType user = com.getFrom();
					if(!users.contains(user)) {users.add(user);}
				}
			}
			
			if(aPost.getLikes() != null) {
				List<NamedFacebookType> likes = aPost.getLikes().getData();
				for(NamedFacebookType like : likes) {
					CategorizedFacebookType user = like.getFrom();
					if(!users.contains(user)) {users.add(user);}
				}
			}
		}
		return users;
	}*/

	
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
	
	
	
	public static void main (String[] args) throws NullPointerException {
		String filename = "DataSetHP"; 
		Logger logger = generateLogger(filename);
		
	
		//lets the monitor access the posts
		users = Monitor.setup();
		monitors = new ArrayList<friendMonitor>();
		
		for(String[] user : users) {
			System.out.println(user[0] + " " + user[1]);
			friendMonitor temp = new friendMonitor(user[0], user[1], logger);
			monitors.add(temp);
			//start monitors for each friend
			Thread tempthread = new Thread(temp);
			tempthread.start();
		}
		/*
		for(String id : ids) {
			Monitor temp = new Monitor(id);
			monitors.add(temp);
			//start monitors for each friend
			Thread tempthread = new Thread(temp);
			tempthread.start();
		}*/
		
	}
}
