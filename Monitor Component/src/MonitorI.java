import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

/**
 * Monitor Class for Fetching information from User's Facebook profile
 * This class is responsible for creating a list of notifications, posts, and,
 * friends
 */
public class MonitorI {
	
	private static String userid;
	private static Post[] posts;
	private static Notification[] notif;
	
	/**
	 * MonitorI Setup 
	 * @param accessToken: Facebook "User" Access Token (String)
	 * @return a list of friends (List<User>)
	 */
	public static List<User> setup(String accessToken) {	
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		
		User me = fbClient.fetchObject("me", User.class);
		//Set userid for the monitor
		userid = me.getId();
		friendMonitor.setId(userid);
		//----------------Getting Post for Level of Sensitivity----------------//
		Connection<Post> result = fbClient.fetchConnection("me/posts", Post.class);
		posts = getPosts(result);		
		Date current_date = new Date();
		
		List<SL> sen_info = new ArrayList<SL>();
		for(Post p : posts) {
			SL temp = new SL();
			temp.id = p.getId();
			if(p.getMessage().equals("0.0")){
				temp.sen = 2;
			}
			else if(p.getMessage().equals("0.25")){
				temp.sen = 3;
			}
			else if(p.getMessage().equals("0.5")){
				temp.sen = 4;
			}
			else if(p.getMessage().equals("0.75")){
				temp.sen = 5;
			}
			else if(p.getMessage().equals("1.0")){
				temp.sen = 6;
			}
			
			Date d = p.getCreatedTime();
			temp.hours = hoursFrom(d, current_date);
			sen_info.add(temp);
		}
		
		friendMonitor.setPosts(sen_info);
		
		//--------------------Getting Notifications-----------------------//
		List<Notification> notifications = fbClient.fetchConnection("me/notifications", Notification.class).getData();
		friendMonitor.setNotifications(notifications);

		//---------------Preparing Friends List to Return-----------------//
		List<User> myFriends = fbClient.fetchConnection("me/friends", User.class).getData();		
		return myFriends;
	}
	
	/**
	 * Get the user's posts 
	 * @return an array of posts (Post[])
	 */
	public static Post[] get_Posts() {
		return posts;
	}
	
	/**
	 * Get the user's ID
	 * @return user_ID
	 */
	public static String get_userID() {
		return userid;
	}
	
	/**
	 * Creating a list of friends who put a comment or like on the user's posts
	 * @param posts
	 * @return an array of the active friends' information [name,ID]
	 */
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

	/**
	 * Getting all the user's post that is including at least one interaction
	 * @param result
	 * @return an array of Post
	 */
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
	
	/**
	 * Calculating time-lapse (between posting an information and getting a notification) 
	 * @param last_date (Date)
	 * @param current_date (Date)
	 * @return time in hours (double)
	 */
	private static double hoursFrom(Date last_date, Date current_date) {
		long ms1 = last_date.getTime();
		long ms2 = current_date.getTime();
		return ((double)ms2 - ms1)/(1000 * 60 * 60);
	}
	
}


