
import com.restfb.Facebook;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import static com.restfb.util.DateUtils.toDateFromLongFormat;

import java.util.Date;
import com.restfb.types.NamedFacebookType;

/**
 * This is a subclass of FacebookType
 * It has been created to parse the data in a Graph API Notification object 
 * @author Shahrokh Shahi 
 * @email ss6515@ic.ac.uk
 * @version 1.3.0
 */
@SuppressWarnings("serial")
public class Notification extends FacebookType { 
		@Facebook 
		private String title; 
	
		@Facebook
		private NamedFacebookType from;

		@Facebook
		private NamedFacebookType to;

		@Facebook("created_time")
		private String createdTime;
		

		@Facebook("updated_time")
		private String updateTime;
		
		@Facebook
		private String message;

		@Facebook
		private String link;

		@Facebook
		private NamedFacebookType application;

		private int unread;
		
		
		/**
		 * User who posted the comment.
		 * 
		 * @return User who posted the comment.
		 */
		public NamedFacebookType getFrom() {
			return from;
		}

		/**
		 * The user who receives the comment.
		 * 
		 * @return User who receives the comment.
		 */
		public NamedFacebookType getTo() {
			return to;
		}

		/**
		 * Date on which the comment was created.
		 * 
		 * @return Date on which the comment was created.
		 */
		public Date getCreatedTime() {
			return toDateFromLongFormat(createdTime);
		}

		
		
		public Date getUpdatedTime() {
			return toDateFromLongFormat(updateTime);
		}
		
		/**
		 * Text contents of the comment.
		 * 
		 * @return Text contents of the comment.
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * The link attached to this post.
		 * 
		 * @return The link attached to this post.
		 */
		public String getLink() {
			return link;
		}

		/**
		 * The application used to create this post.
		 * 
		 * @return The application used to create this post.
		 */
		public NamedFacebookType getApplication() {
			return application;
		}
		
		/**
		 * This shows the read/unread flag
		 * @return 0 = unread  1 = read
		 */
		public int getFlag(){
			return unread;
		}
		
		/**
		 * Showing the notification messages
		 * @return String including the notification message
		 */
		public String getTitle(){ 
			  return title; 
		}
} 

	