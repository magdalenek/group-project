//package fbMonitor;
import java.util.List;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import com.restfb.Parameter;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;

/**
 * A general class for testing the monitoring behaviors
 * Using Facebook Graph API and specifically the RestFB library
 * (this class is based on the first monitor mock-up: Fetching information as much as possible) 
 * 
 * @author Shahrokh Shahi
 * @email ss6515@ic.ac.uk
 * @version 2.1.1
 *
 */
public class MonitorX1 {

	public static void main (String[] args){
		//====================================================================================
		System.out.println("-------------<START MONITOR TESTING>-------------");
		String accessToken = "EAACEdEose0cBAOBxu6qedo23ANYpNlsHOHVbXIIH8x48fEQYSr4sR8tzBH1nCzqDKS80N5fmswfZA2BZCo4PNGgxp89QvZA4bRezIjNHBu6e3IVMoLdppFitL6eZAHVVnHkeNWOZAOxN8Qaudf23WM5L4M5khSxOGU31X9ZA9lbAZDZD ";
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		//====================================================================================
		System.out.println("**************** The User Info *******************");
		User me = fbClient.fetchObject("me", User.class);
		System.out.println(me.getName());
		System.out.println(me.getId());
		System.out.println(me.getAbout());
		//====================================================================================		
		System.out.println("***************** FriendsList ********************");
		List<User> myFriends = fbClient.fetchConnection("me/friends", User.class).getData();
		for(User friend : myFriends) {
			System.out.println(friend.getName() + "  " + friend.getId());
		}
		//====================================================================================
		System.out.println("**************** Notifications *******************");
		List<Notification> notifications = fbClient.fetchConnection(
		                       "me/notifications", Notification.class).getData();
		System.out.println("Number of Unread Notification= "+notifications.size()); 
		System.out.println(" ");
		for (Notification notif: notifications){
			System.out.println(notif.getId());
			System.out.println(notif.getLink());
			System.out.println(notif.getType());
			System.out.println("FRND NAME--> "+notif.getFrom().getName());
			System.out.println("FRND ID  --> "+notif.getFrom().getId());
			System.out.println("USER ID  --> "+notif.getTo().getName());
			
			System.out.println(notif.getApplication().getId());
			
			String notif_msg = notif.getTitle();
			System.out.println("--> Notification Message:");
			System.out.println(notif_msg);
			
			System.out.println("--> Parsing Notification: ");
			if (notif_msg.contains("commented")){
				System.out.println("TYPE= COMMENT");
			}else if(notif_msg.contains("mentioned")){
				System.out.println("TYPE= COMMENT (& MENTIONED)");
			}else if(notif_msg.contains("shared")){
				System.out.println("TYPE= SHARED (RE-SHARE)");
			}else if(notif_msg.contains("reacted")){
				System.out.println("TYPE= REACTED (LIKE)");
			}else{
				System.out.println("TYPE = /UNKNOWN/");
			}
			System.out.println("--> Time of Creation= "+notif.getCreatedTime());
			//System.out.println(notif.getUpdatedTime());
			//System.out.println(notif.getFlag());
			System.out.println("------------------");
			
		}
		//====================================================================================
		System.out.println("******************** Posts ***********************");
		Connection <Post> posts = fbClient.fetchConnection("me/feed", Post.class);
		int postCounter=0;
loop:	for (List<Post> page : posts){
			for (Post aPost : page){
				System.out.println(aPost.getMessage());
				System.out.println("ID = "+aPost.getId());
				System.out.println("Address: fb.com/"+aPost.getId());
				
				System.out.println("#Likes= "+aPost.getLikesCount() + "#Comments= "+aPost.getCommentsCount()+"#Shares= "+aPost.getSharesCount());
				//System.out.println("Likes = "+aPost.getLikes().getData());
				System.out.println(aPost.getShares());
				System.out.println("-----------------------------------------");

				postCounter++;
				if (postCounter == 50){
					break loop;
				}
			}
		}
		System.out.println("Total Number of Posts= "+postCounter);
		//====================================================================================		
		/*
		System.out.println("----------->A Page Searching ");
		Connection<Page> result_n = fbClient.fetchConnection("me/accounts", Page.class);
		for (List<Page> feedPage:result_n){
			for(Page page : feedPage){
				System.out.println(page.getName());
			}
		}
		*/
		System.out.println("-------------<END OF MONITOR TESTING>-------------");
	}
}
