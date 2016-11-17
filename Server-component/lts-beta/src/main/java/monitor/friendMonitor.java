package monitor;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;


public class friendMonitor implements Runnable {
	private static Post[] posts;
	private static String userid;
	private static List<Notification> notifications;
	private static double t;
	private static Date current_date;
	private static Logger logger;
	
	private double infoType;
	private boolean malicious;
	private String friendid;
	private String write;
	private String friendname;
	private JSD util = new JSD(); 
	private AdaptiveSharingAnalyser AS_analyser; 
	private TransitionProbability [] Agent;
	private double [] learntTPs = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,  0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
	public static void setDate(Date date) {
		current_date = date;
	}
	
	public static void setPosts(Post[] P) {
		posts = P;
	}
	
	public static void setId(String id) {
		userid = id;
	}

	public static void setNotifications(List<Notification> n) {
		notifications = n;
	}
	
	public void setMalicious() {
		malicious = true;
	}
	
	private double getJSD() {
		// tipr. Compute Jensen-Shannons divergence between two learnt probabilities
		// see section 4.5 in paper
		
		double [] jsd1 = {0,0,0,0,0}; 
		double  V = 0; 
		int sign = 0;  

		for(int j=2; j< 7; j++){
			double [] p1 = {learntTPs[j], 1-learntTPs[j]}; 
			double [] p2 = {learntTPs[j+1], 1-learntTPs[j+1]}; 
			jsd1[j-2] = util.jensenShannonDivergence(p1,p2);
			if(learntTPs[j+1] - learntTPs[j] < 0){
				sign = -1; 
			}else{
				sign = 1;
			}
			V += util.jensenShannonDivergence(p1,p2) * sign;
		}
		return V; 
	}

	// this method invokes the runtime analysis for each level of sensitivity 
	private void predictor(double t, double infoType) {//, Logger logger) {
		runtimeAnalysis(t, infoType);//, logger);
	}
	
	private double daysFrom(Date last_date, Date current_date) {
		long ms1 = last_date.getTime();
		long ms2 = current_date.getTime();
		return ((double)ms2 - ms1)/(1000 * 60 * 60);
	}

	// this method does the runtime analysis and outputs the results to a log file:
	// tipr. learn the transition probabilities
	// 2. get the expected privacy risk of sharing 
	// 3. get the expected social benefit of sharing 
	// 4. do a trade-off between the privacy risk and social benefit 
	// 5. calculate the Jensen-Shannon divergence between two re-share probabilities
	
	private void runtimeAnalysis(double t, double infoType) {

		//double [] p_ijs = {learntTPs[friendIs][2],learntTPs[friendIs][3],learntTPs[friendIs][4],learntTPs[friendIs][5],learntTPs[friendIs][6]};
		
		AS_analyser = new AdaptiveSharingAnalyser(learntTPs, infoType);
		
		double V = getJSD(); 
		Log log = new Log(); 

			 write = log.logger(friendname, friendid, t, infoType,
					AS_analyser.getExpectedSB(), AS_analyser.getExpectedPR(),
					AS_analyser.getIsR1_Satisified(), AS_analyser.getIsR2_Satisfied(), 
					AS_analyser.utility_trade_off_calc(),
					AS_analyser.getExpectedInfoLeakage(), V, learntTPs); 

			//logger.warn(write);
		
	}
	
	public synchronized void logging() {
		logger.warn(write);
	}
	
	public friendMonitor(String f_id, String f_name, Logger logger2) {
		friendid = f_id;
		friendname = f_name;
		logger = logger2;
		// tipr. Initial prior values for each p_x transition probabilities in the DTMC model
		double[] p_priors = {0.97, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.25, 0.25, 0.25, 0.01, 0.01};
		infoType = 1.0;
		// 2. Create monitoring Agent for each contact 
		MonitoringAgent monitor = new MonitoringAgent(); 
		Agent = monitor.creatAgent(p_priors);
		malicious = false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// for each post check if friendid made any interactions
		/*
		 * if friend ignore j=0
		 * if friend seen j=tipr
		 * if friend reshare
		 * 		check level of sensitivity (j ranges from 2-6) (p_ij transition probabilities)
		 * if userComment j=7
		 * if friendComment j=8
		 * if friendLike j=9
		 * if reply j=10
		 * if again j=11
		 * for each interaction do Agents[j].successfulInvocation
		 * then do predictor
		 */

		//for(Post aPost : posts)
		Date[] dates = new Date[posts.length];
		int no_posts = posts.length;
		
		for(int i=0; i < no_posts; i++) {
			dates[i] = posts[i].getCreatedTime();
		}
		
		//dates[0] = current_date;
		t = daysFrom(dates[0], current_date);
		
		for (int count=0; count < no_posts; count++) {
			boolean friend_commented = false;
			boolean user_commented = false;
			boolean replied = false;
			boolean shared = false;
			
			//Date current_date = dates[count];
			Date last_date = dates[count];
			
			System.out.println(posts[count].getMessage());
			double days = daysFrom(last_date, current_date);
			System.out.println(days);
			if(posts[count].getComments() != null) {
				List <Comment> comments = posts[count].getComments().getData();
				for(Comment comment : comments) {
					if(comment.getId().equals(friendid)) {
						friend_commented = true;
						if(comment.getParent() != null) {
							replied = true;
						}
					}
					else if(comment.getId().equals(userid)) {
						user_commented = true;
					}
				}
			}
			
			if(posts[count].getLikes() != null) {
				List <NamedFacebookType> likes = posts[count].getLikes().getData();
				for(NamedFacebookType like : likes) {
					
					if(like.getId().equals(friendid)) {
						Agent[9].successfulInvocation(days);
						break;
					}
				}		
			}
			
			
			
			if(friend_commented) {
				Agent[8].successfulInvocation(days);
			}
			
			if(user_commented) {
				Agent[7].successfulInvocation(days);
			}
			
			if(replied) {
				Agent[10].successfulInvocation(days);
			}
			
			if(malicious) {
				Agent[6].successfulInvocation(days);
			}
			
			for(int i=0; i < 12; i++) {
				learntTPs[i] = Agent[i].get_pk();
			}
			
			/*
			for (Notification notif: notifications){
				System.out.println(notif.getId());
				System.out.println(notif.getLink());
				System.out.println(notif.getType());
				System.out.println("--> "+notif.getFrom().getName());
				System.out.println("--> "+notif.getFrom().getId());
				//System.out.println("--> "+notif.getFrom().getType());
				System.out.println("--> "+notif.getTo().getName());
				System.out.println(notif.getFlag());
				System.out.println(notif.getTitle());
				//System.out.println(notif.getTitle());
				System.out.println("########");
				String notif_msg = notif.getTitle();
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
				System.out.println(notif.getCreatedTime());
				System.out.println(notif.getUpdatedTime());
				System.out.println("------------------");
				
			}
			

			
			System.out.println(aPost.getMessage());
			System.out.println("Address: fb.com/"+aPost.getId());
			System.out.println(likes);
			System.out.println(comments);
			System.out.println(aPost.getShares());
				
			//Counters Testing:
			//System.out.println("#Likes= "+aPost.getLikesCount() + "#Comments= "+aPost.getCommentsCount()+"#Shares= "+aPost.getSharesCount());
			//System.out.println("Likes= "+aPost.getLikes().getData());
			System.out.println("-----------------------------------------");
			*/
		}
		predictor(t, infoType);//, logger);
		System.out.println(friendname + " " + AS_analyser.utility_trade_off_calc() +  " " + ((malicious) ? "malicious" : "not malicious"));
		logging();
	}
}
