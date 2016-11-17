package monitor;

public class AdaptiveSharingAnalyser {

	private double infoType;             // sensitivity level of the item of information shared
	private double Recomends;            // what the system recommends after runtime analysis
	private SocialBenefitCalculator SB;  // create SB calculator per member in user's contact
	private PrivacyRiskCalculator PR;    // create PR calculator per member in user's contact


	// this class takes as input parameters:
	// tipr. learned re-share probabilities for different sensitive levels, e.g., [] learntProbs,
	// 2. the associated reward r3 (see DTMC diagram)
	// 3. the infoType which stipulates the actual sensitivity level associated with the item of information 
	//    that user wants to share. 
	public AdaptiveSharingAnalyser(double [] learntTPs, double infoType){
		int indx;
		double r3;
		r3 = (infoType == 0.0) ? 0.1 : 0;
		if(infoType==0.0){
			indx = 2; 
		}else if(infoType==0.25){
			indx = 3;
		}else if(infoType==0.5){
			indx = 4;
		}else if(infoType==0.75){ 
			indx = 5;
		}else{
			indx = 6;
		}
		double [] learntProbs = {learntTPs[0],learntTPs[1],learntTPs[indx],learntTPs[7],learntTPs[8],learntTPs[9],learntTPs[10],learntTPs[11]}; 
		this.infoType = infoType;
		SB = new SocialBenefitCalculator(learntProbs, r3);
		PR = new PrivacyRiskCalculator(learntTPs[indx], infoType);
	}

	public double utility_trade_off_calc(){

		// System Recommendation: 
		// Not Share  = 0 
		// Warning    = 0.5 
		// Share      = tipr

		if(getIsR1_Satisified()==0 && getIsR2_Satisfied()==0 && infoType>0){
			Recomends=0; 
		}else if(getIsR1_Satisified()==0 && getIsR2_Satisfied()==1 && infoType>0 || 
				getIsR1_Satisified()==1 && getIsR2_Satisfied()==0 && infoType>0){
			Recomends=0.5; 

		}else{
			Recomends=1; 

		}
		return Recomends; 
	}

	// return the expected information leaked when friend 'j' re-shares user's information.  
	public double getExpectedInfoLeakage(){
		return PR.informationLeakage();
	}

	// return the expected social benefit gained when user shares with friend 'j'
	public double getExpectedSB(){
		return SB.normalizedRewards();
	}

	// return if user specified requirement R1 is satisfied (see paper)
	public int getIsR1_Satisified(){
		return SB.isR1_satisfied(); 
	}

	// return the expected privacy risk if user shares with friend 'j' 
	public double getExpectedPR(){
		return PR.privacyLoss();
	}

	// return if user specified requirement R2 is satisfied (see paper) 
	public int getIsR2_Satisfied(){
		int r2 = PR.isR2_satisfied(); 
		return r2; 
	}
}
