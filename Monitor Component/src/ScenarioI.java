import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

/**
 * A Back-end Testing Base Class
 */
public class ScenarioI {
	
	private static List<User> users;
	private static List<friendMonitor> monitors;
	
	/**
	 * Create a logging file
	 * @param filename (String) 
	 * @return log object (Logger)
	 */
	private static Logger generateLogger(String name) {
		
		//Create Logger 
		String loggerName = "A1." + name;
		Logger log = Logger.getLogger(loggerName);

		//Create Logging File Appender
		RollingFileAppender fileApp = new RollingFileAppender();
		fileApp.setName("A1." + loggerName + "_FileAppender");
		fileApp.setFile("/Users/ShahrokhX/Desktop/log/"+ name+".txt");
		fileApp.setLayout(new PatternLayout("%m%n"));
		fileApp.setThreshold(Level.toLevel("WARN"));
		fileApp.setAppend(true);
		fileApp.activateOptions();
		log.addAppender(fileApp);
		return log;     
	}
	
	/**
	 * Main of Test Class
	 * It should provide the Monitor class with the user Access Token
	 * --> An Access Token should be obtained by Graph API Explorer
	 * @param args
	 * @throws NullPointerException
	 */
	public static void main (String[] args) throws NullPointerException {
		String filename = "DataSetHP"; 
		Logger logger = generateLogger(filename);
		String token = "EAACEdEose0cBABusWxgT6sJVINxoZAuThJPelDZC1iQWJIiNtHZCtWmDuNcaz8rPZAAkQoQNXuhOEWvo6QiyoyG3wG6tgBWbNyPJvVH25hXnPPUJrsleVvZAnPnbK0W8EtL18Xi3VZAAduOe7nKcbmYDHGj91pfbPx2Xrdx3i0ngZDZD";
		
	
		//lets the monitor access the posts
		users = MonitorI.setup(token);
		monitors = new ArrayList<friendMonitor>();
		
		for(User user : users) {
			friendMonitor temp = new friendMonitor(user, logger);
			monitors.add(temp);
			
			//start monitors for each friend, concurrently
			Thread tempthread = new Thread(temp);
			tempthread.start();
		}		
		
	}
}
