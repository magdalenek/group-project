
/**
 *    This class constructs the monitoring agent that monitors the transition probabilities between 
 *    each states in the DTMC model. For each transition between the states, a TransitionProbability
 *    java object is created, which assigns the learning algorithm to learn the specific transition
 *    probability between state sx and sy.  
 */
public class MonitoringAgent {

	// this class takes as input a set of priors that are provided by either the user 
	// or the system developer. 
	public  TransitionProbability [] creatAgent(double [] priors){

		// create agent operations for each OSN operation and pass the prior value
		TransitionProbability agent1_p_ignore = new TransitionProbability(priors[0]);
		TransitionProbability agent1_p_seen = new TransitionProbability(priors[1]);
		TransitionProbability agent1_p_ij_1 = new TransitionProbability(priors[2]);
		TransitionProbability agent1_p_ij_2 = new TransitionProbability(priors[3]);
		TransitionProbability agent1_p_ij_3 = new TransitionProbability(priors[4]);
		TransitionProbability agent1_p_ij_4 = new TransitionProbability(priors[5]);
		TransitionProbability agent1_p_ij_5 = new TransitionProbability(priors[6]);
		TransitionProbability agent1_p_userComment = new TransitionProbability(priors[7]);
		TransitionProbability agent1_p_friendComment = new TransitionProbability(priors[8]); 
		TransitionProbability agent1_p_friendLike = new TransitionProbability(priors[9]); 
		TransitionProbability agent1_p_reply = new TransitionProbability(priors[10]); 
		TransitionProbability agent1_p_again = new TransitionProbability(priors[11]); 

		// synthesis all agent-operations as a single Agent 
		TransitionProbability [] agent = {agent1_p_ignore,agent1_p_seen, agent1_p_ij_1,agent1_p_ij_2,agent1_p_ij_3, agent1_p_ij_4,agent1_p_ij_5,
				agent1_p_userComment,agent1_p_friendComment, agent1_p_friendLike,agent1_p_reply, agent1_p_again };  
		return agent; 
	}
}
