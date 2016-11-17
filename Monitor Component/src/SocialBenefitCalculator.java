/**
 * This class is responsible for calculating the Social Benefit
 * 
 */
public class SocialBenefitCalculator {

	private double r1 = 0.2;   // reward for seen 
	private double r3;         // reward for re-share not sensitive information 
	private double r4 = 0.9;   // reward for userComment
	private double r5 = 0.9;   // reward for friendComment
	private double r6 = 0.8;   // reward userLike
	private double r7  = 0.8;  // reward friendLike 

	// Learnt transition probabilities 
	private double p_ignore; 
	private double p_seen;  
	private double p_ij; 
	private double p_userComment; 
	private double p_friendComment; 
	private double p_friendLike; 
	private double p_reply; 
	private double p_again; 

	// User specified social benefit threshold 
	private double w_i_SB =0.25;

	// This class takes as input parameters:
	// 1. the learnt transition probabilities,
	// 2. the social reward r3, r3 > 0 when the shared item of information is not sensitive at all
	//    and r3=0 otherwise. 
	public SocialBenefitCalculator (double [] transitionProbabilites, double r3){

		this.p_ignore = transitionProbabilites[0];
		this.p_seen = transitionProbabilites[1];
		this.p_ij = transitionProbabilites[2];
		this.p_userComment = transitionProbabilites[3];
		this.p_friendComment = transitionProbabilites[4];
		this.p_friendLike = transitionProbabilites[5];
		this.p_reply = transitionProbabilites[6];
		this.p_again = transitionProbabilites[7];
		this.r3 = r3; 

	}

	// this method returns the expected social benefit gained, using the algebraic expression generated
	// by PRISM model checker for the DTMC model in our paper. 
	public double expectedRewards(){

		double sb =  (- r4 * p_again * p_reply * p_userComment * p_ij + r6 * p_again * p_reply * 
				p_userComment * p_ij - r5 * p_again * p_reply * p_friendComment * p_ij + r6 * p_again * 
				p_reply * p_friendComment * p_ij + r6 * p_again * p_reply * p_friendLike * p_ij - r7 * 
				p_again * p_reply * p_friendLike * p_ij + r1 * p_again * p_reply * p_seen + r3 * p_again * 
				p_reply * p_ij - r6 * p_again * p_reply * p_ij + r4 * p_userComment * p_ignore - r6 * 
				p_userComment * p_ignore + r5 * p_friendComment * p_ignore - r6 * p_friendComment *
				p_ignore - r6 * p_friendLike * p_ignore + r7 * p_friendLike * p_ignore + r4 * p_userComment * 
				p_seen - r6 * p_userComment * p_seen + r5 * p_friendComment * p_seen - r6 * p_friendComment * 
				p_seen - r6 * p_friendLike * p_seen + r7 * p_friendLike * p_seen + r4 * p_userComment * 
				p_ij - r6 * p_userComment * p_ij + r5 * p_friendComment * p_ij - r6 * p_friendComment *
				p_ij - r6 * p_friendLike * p_ij + r7 * p_friendLike * p_ij + r6 * p_ignore - r1 * p_seen + 
				r6 * p_seen - r3 * p_ij + r6 * p_ij - r4 * p_userComment + r6 * p_userComment - r5 * 
				p_friendComment + r6 * p_friendComment + r6 * p_friendLike - r7 * 
				p_friendLike - r6 )/ (p_again * p_reply - 1); 

		if(sb<=0){
			return 0; 
		}else{
			return sb; 
		}
	}


	// normalize the social benefit between [0 1] 
	public double normalizedRewards(){
		double min = 0; 
		double max = 1;
		double  sb = expectedRewards();
		if(sb<0){
			sb=0; 
		}if(sb>1){
			sb=1; 
		}
		return (sb - min)/(max - min); 
	}


	// return if user specified requirement R1 (see paper) is satisfied
	public  int isR1_satisfied(){
		if(expectedRewards() <= w_i_SB){
			return 0; 
		}else{
			return 1; 
		}
	}
}
