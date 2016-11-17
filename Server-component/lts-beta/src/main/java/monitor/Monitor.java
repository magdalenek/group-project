package monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;


public class Monitor {

	public static String getUserId(String accessToken){
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		User me = fbClient.fetchObject("me", User.class);
		return me.getId();
	}

	public static List<String[]> setup() {
		String accessToken = "CAACEdEose0cBANxUB3pcnZB4aSumE68PpsLJI7Q61KfAnpdZC3uKZAJph5iZCWygRX7D1y0iygoHYxzc970rAKZBGPpSpWVTPLQ5RGzY1raN5oTptygIplhLj37D369g2y1bi4OJsAzEA1jOex6MhAO6gwDQfVHmZAbRJuSoZCCc9eyd22f4jxUWhPCudjZCcgbtIAfuthrd3QZDZD"+"";

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
		
		return getID_name(posts);	
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
}
