/**
 * Transition Probability class is based on the formulae introduced in my paper 
 * titled: Adaptive Model Learning for Continual Verification of Non-Functional Properties.
 * It may also help to first read my paper titled: Using Observation Ageing to Improve
 * Markovian Model Learning in QoS Engineering
 * 
 */
public class TransitionProbability {	
	private double c0;            // smoothing parameter
	private double alpha;         // observation ageing coefficient; 
	private double tk;            // current time-stamp in seconds
	private int k;                // number of invocations
	private double fk;            // function 
	private double gk;            // function
	private double p0;            // prior estimate
	private double pk;            // probability estimate after k-th invocation 


	private int N;                // No. of observations used in the sliding window
	private double prevTimestamp; // the timestamp of the last observation
	private double[] durations;   // used to calculate the time lapsed since last observation 
	private int dIndex;           // index used for the number of observations
	private boolean dFull;        // used to check if the sliding window is full  
	private double totalDistance; // used to calculate the distance between consecutive observation
	private double p_max;         // threshold for the alpha value which is used as the forgetting coefficient  
	private double epsilon;       // used as the inequality in Chebyshev's equation see my paper (Adaptive Model Learning...)



	public TransitionProbability(double p0) {
		// 1. Initialise learning parameters

		this.c0 = 50;
		this.alpha = 1.0001;


		// 2. Initialise frequency monitoring fields
		N = 200; 
		durations = new double[N]; 
		for(int i=0;i<durations.length;i++){
			durations[i] = 0;
		}
		prevTimestamp = 0; //System.currentTimeMillis() / 1000;
		dIndex = 0;
		dFull = false;
		totalDistance = 0; 
		p_max = 0.69; 
		epsilon = 0.05; 

		// 3. Initialise time and invocation number
		tk =  0; 
		k = 0;

		// 4. Initialise probabilities
		this.p0 = this.pk = p0;
	}

	public void stoppedMonitoring() {
		k = 0;
	}

	public void successfulInvocation(double timestamp) {
		recordInvocation(1.0, timestamp);
	}

	public void unsuccessfulInvocation(double timestamp){
		recordInvocation(0.0, timestamp); 
	}

	public void recordInvocation(double sigmak, double timestamp) {

		double minusObservationAge = 0;
		// 1. Increment the number of invocations.
		k++; 

		// 2. Obtain the current time
		double t = (double)timestamp;

		double overlineT = 0; 

		// 3. Calculate fk, gk
		if (k==1) {
			fk = sigmak;
			gk = 1.0;
		}

		else {

			//4. sliding window for N observations
			totalDistance -= durations[dIndex];
			durations[dIndex] = t - this.prevTimestamp;
			totalDistance += durations[dIndex];
			if(++dIndex == N){
				dIndex = 0;
				dFull = true;
			}
			this.prevTimestamp = tk;


			if (dFull) { // N or more observations have been made -> time to check alpha
				overlineT = (double) totalDistance / (double) N;

				// calculate new alpha
				double cEpsilon = epsilon * epsilon; 
				double cAlpha = (1 + 4*cEpsilon * p_max) / (1 - 4*cEpsilon * p_max); 
				double minusOverlineT = 1/overlineT; 
				alpha = Math.pow(cAlpha, minusOverlineT); 

				// calculate new c0

				double logAlpha = Math.log10(alpha); 
				c0 = 1 / (2 * logAlpha); 

			}

			minusObservationAge = tk - t;
			double weight = Math.pow(alpha, minusObservationAge);
			fk = sigmak + fk * weight;
			gk = 1 + gk * weight;
		}

		// 5. Calculate new pk
		pk = (c0 * p0 + k * (fk / gk)) / (c0 + k); 

		// 6. Update tk
		tk = t;
	}



	public double get_tk(){
		return tk;
	}

	public double get_DeltaT(){
		return tk-prevTimestamp; 
	}

	public double get_pk(){
		return pk; 
	}
	public void setAlpha(double alpha){
		this.alpha = alpha; 
	}

	public void setSmoothingPrameter(double c0){
		this.c0 = c0; 
	}

	public void setPriorValue(double pPrior){
		this.p0 = pPrior; 
	}
}