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
import com.restfb.types.User;

/**
 * Multithreading monitoring: Creating a monitor thread per each friend
 * These monitor threads will be run concurrently
 * 
 * @author Oh Boon Eng, Shahrokh Shahi
 * @email beo12@ic.ac.uk, ss6515@ic.ac.uk,
 * @version 2.0.0
 */
public class friendMonitor implements Runnable {
	//private static Post[] posts;
	private static List<SL> posts;
	private static String userid;
	private static List<Notification> notifications;
	
	private static double t;
	//private static Date current_date;
	private static Logger logger;
	
	private double infoType;
	private boolean malicious;
	private String friendid;
	private String write;
	private String friendname;
	private JSD util = new JSD(); 
	private AdaptiveSharingAnalyser AS_analyser; 
	private TransitionProbability [] Agent;
	private double [] learntTPs = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,  
			                       0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
	/**
	 * Set Posts: a static data structure including post IDs and their level of
	 *            sensitivity (List<SL> posts)
	 * @param s a list of post_ID/Sensitivity level structure (List<SL>)
	 */
	public static void setPosts(List<SL> s) {
		posts = s;
	}
	
	/**
	 * Set userid: a static variable indicating user ID
	 * @param id (String)
	 */
	public static void setId(String id) {
		userid = id;
	}

	/**
	 * Set notifications: a static list of the user's notifications
	 * @param n (List<Notification>)
	 */
	public static void setNotifications(List<Notification> n) {
		notifications = n;
	}
	
	/**
	 * Set the boolean malicious switch = true
	 */
	public void setMalicious() {
		malicious = true;
	}
	
	/**
	 * Set Logger sensitivity level InfoType 
	 * @param sl (double)
	 */
	public void setInfoType(double sl) {
		infoType = sl;
	}
	
	/**
	 * Compute Jensen-Shannons divergence between two learnt probabilities
	 * @return divergence (double)
	 */
	private double getJSD() {		
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

	/**
	 * This method invokes the runtime analysis for each level of sensitivity 
	 * @param t time (double)
	 * @param infoType sensitivity level (double)
	 */
	private void predictor(double t, double infoType) {//, Logger logger) {
		runtimeAnalysis(t, infoType);//, logger);
	}
	
	/**
	 * Calculating time-lapse (between posting an information and getting a notification) 
	 * @param last_date (Date)
	 * @param current_date (Date)
	 * @return time in hours (double)
	 */
	private double hoursFrom(Date last_date, Date current_date) {
		long ms1 = last_date.getTime();
		long ms2 = current_date.getTime();
		return ((double)ms2 - ms1)/(1000 * 60 * 60);
	}

	
	/**
	 * this method does the runtime analysis and outputs the results to a log file:
	 * 1. learn the transition probabilities
	 * 2. get the expected privacy risk of sharing 
	 * 3. get the expected social benefit of sharing 
	 * 4. do a trade-off between the privacy risk and social benefit 
	 * 5. calculate the Jensen-Shannon divergence between two re-share probabilities
	 * 
	 * @param t time (double)
	 * @param infoType sensitivity level (double)
	 */	
	private void runtimeAnalysis(double t, double infoType) {
		
		AS_analyser = new AdaptiveSharingAnalyser(learntTPs, infoType);
		
		double V = getJSD(); 
		Log log = new Log(); 

			 write = log.logger(friendname, friendid, t, infoType,
					AS_analyser.getExpectedSB(), AS_analyser.getExpectedPR(),
					AS_analyser.getIsR1_Satisified(), AS_analyser.getIsR2_Satisfied(), 
					AS_analyser.utility_trade_off_calc(),
					AS_analyser.getExpectedInfoLeakage(), V, learntTPs); 

	}
	
	/**
	 * a synchronized method to write the log 
	 * (synchronization lock included)
	 */
	public synchronized void logging() {
		logger.warn(write);
	}
	
	/**
	 * Class Constructor
	 * Initializing values to start concurrent friend monitoring
	 * @param user (User)
	 * @param logger2 (Logger)
	 */
	public friendMonitor(User user, Logger logger2) {
		friendid = user.getId();
		friendname = user.getName();
		logger = logger2;
		// 1. Initial prior values for each p_x transition probabilities in the DTMC model 
		double[] p_priors = {0.7, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.25, 0.25, 0.25, 0.01, 0.01};
		// 2. Create monitoring Agent for each contact 
		MonitoringAgent monitor = new MonitoringAgent(); 
		Agent = monitor.creatAgent(p_priors);
		malicious = false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	/**
	 * Starting a monitor thread
	 * for each notification finds the friends who has interaction and reflect
	 * the interaction in transition probabilities (learning procedure)
	 * if friend ignore j=0
	 * if friend seen j=1
	 * if friend re-share check the level of sensitivity (j ranges from 2-6) (p_ij transition probabilities)
	 * if userComment j=7
	 * if friendComment j=8
	 * if friendLike j=9
	 * if reply j=10
	 * if again j=11
	 * for each interaction do Agents[j].successfulInvocation
	 * then do predictor
	 */
	public void run() {
		if(userid.equals(friendid)) {return;}

		for (Notification notif: notifications){
			boolean friend_commented = false;
			boolean user_commented = false;
			boolean replied = false;
			boolean shared = false;
			boolean liked = false;
			
			String notif_id    = notif.getId();
			String notif_link  = notif.getLink();
			String notif_type  = notif.getType();
			String friend_name = notif.getFrom().getName();
			String friend_id   = notif.getFrom().getId();
			String receiver    = notif.getTo().getName();
			String notif_msg   = notif.getTitle();
			Date   notif_date  = notif.getCreatedTime();
			
			int start=notif_link.indexOf("id=");
			int end  =notif_link.indexOf("&");
			
			String post_id = (String) notif_link.subSequence(start+3, end);
			String post_id_comp = userid + "_"+ post_id;
			
			//System.out.println("------");
			//System.out.println(post_id_comp);
			if(friend_name.equals(friendname)) {
				for(SL s : posts) {
					//System.out.println(s.id);
					if(post_id_comp.equals(s.id)) {
						//System.out.println("Match found!");
						if (notif_msg.contains("commented")){
							friend_commented = true;
						}else if(notif_msg.contains("mentioned")){
							friend_commented = true;
						}else if(notif_msg.contains("shared")){
							shared = true;
						}else if(notif_msg.contains("reacted")){
							liked = true;
						}else{
							System.out.println("TYPE = /UNKNOWN/");
						}
						
						double hours = s.hours;
						int ij = s.sen;
						infoType = (ij-2)*0.25;
						
						if(friend_commented) {
							Agent[8].successfulInvocation(hours);
						}
						
						if(user_commented) {
							Agent[7].successfulInvocation(hours);
						}
						
						if(replied) {
							Agent[10].successfulInvocation(hours);
						}
						
						if(shared) {
							Agent[ij].successfulInvocation(hours);
						}
					}
				}
			}	
		}
		
		for(int i=0; i < 12; i++) {
			learntTPs[i] = Agent[i].get_pk();
		}
		predictor(t, infoType);//, logger);
		logging();
	}
}
