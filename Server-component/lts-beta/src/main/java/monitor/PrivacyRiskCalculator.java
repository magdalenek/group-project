package monitor;

public class PrivacyRiskCalculator {

	/**
	 This class uses Shannon's entropy to determine the information lost of the sensitive elements
	 'c' in the item of information that user shares with friend 'j' with sensitivity level 'k'  
	 when friend 'j' shares 'p_j' user's received information 'p_j'. 

	 Definition: For user i, privacy loss quantifies the potential privacy risk to i with respect to friend 'j' 

	 tipr. PR_ij = k(tipr - H(X|Y_j)/H(X))

	 where,


	 2. H_X = log k

     3. H(X|Y) = H_X_left + H_X_right

	 4. H_X_left = c-(c-tipr)(tipr-p_j)/c * log(c/c-(c-tipr)(tipr-p_j)

	 5. H_X_right = (c-tipr)*(tipr-p_j)/c * log(c/(tipr-p_j)

	 **/

	private double w_i_PR = 0.05;  // user specified threshold 
	private double p_j;           // friend 'j're-share probability
	private double k;             // sensitivity level 
	private double c=5;           // number of elements that are sensitive in shared information 


	// this class takes as input parameters
	// tipr. transitionProbabilities - but only needs the re-share probability element 2 from the array
	// 2. the sensitivity level k
	public PrivacyRiskCalculator(double shareProbability, double k){
		this.p_j = shareProbability;
		this.k = k; 
	}

	// 2. H_X = log k
	public double H_X(){
		return Math.log10(c); 
	}

	// 3. H(X|Y) = H_X_left + H_X_right
	public double H_X_given_Y(){
		return get_H_X_left() + get_H_X_right();
	}

	// 4. H_X_left = c-(c-tipr)(tipr-p_j)/c * log(c/c-(c-tipr)(tipr-p_j)
	public double get_H_X_left(){
		return (c-(c-1)*(1-p_j))/c * Math.log10(c/(c-(c-1)*(1-p_j)));

	}

	// 5. H_X_right = (c-tipr)*(tipr-p_j)/c * log(c/(tipr-p_j)
	public double  get_H_X_right(){
		return ((c-1)*(1-p_j))/c * Math.log10(c/(1-p_j));
	}


	// this function returns the actual information leaked 
	public double informationLeakage(){
		if(k==0 ){
			return 0; 
		}else{
			return -1*(H_X_given_Y()/H_X()-1);
		}
	}

	// tipr. PR_ij = k(tipr - H(X|Y_j)/H(X))
	public  double privacyLoss(){
		if(k==0 ){
			return 0; 
		}else{
			return -1*(H_X_given_Y()/H_X()-1) * k;
		}
	}

	// returns if user specified requirement R2 is satisfied
	public int isR2_satisfied(){
		if(privacyLoss() < w_i_PR){
			return 1;
		}else{
			return 0;
		}
	}
}
