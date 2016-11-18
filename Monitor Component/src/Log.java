
/**
 * This class outputs the information required for experiments to the log file 
 */
public class Log {
	
	
	public String logger(String friendname, String friendid, double t, double infoType, 
			double expected_SB, double expected_PR,
			int isR1_satisfied, int isR2_satisfied, 
			double recommend,
			double infoLeaked, double  V, double[] learntTPs) {
		return friendname + " " + friendid +" Sen "+ infoType + " :  "
				+ t + " :  "
				+ learntTPs[0] + " "
				+ learntTPs[1] + " "
				+ learntTPs[2] + " "
				+ learntTPs[3] + " "
				+ learntTPs[4] + " "
				+ learntTPs[5] + " "
				+ learntTPs[6] + " "
				+ learntTPs[7] + " "
				+ learntTPs[8] + " "
				+ learntTPs[9] + " "
				+ learntTPs[10] + " "
				+ learntTPs[11] + " "
				+ recommend;/*
				+  learntProbs[0]+ " :  " 
				+  learntProbs[1]+ " :  " 
				+  learntProbs[2]+ " :  " 
				+  learntProbs[3]+ " :  " 
				+  learntProbs[4]+ " :  " 
				+  learntProbs[5]+ " :  " 
				+  learntProbs[6]+ " :  " 
				+  learntProbs[7]+ " :  " 
				+  expected_SB + " :  " 
				+  expected_PR + " :  " 
				+  infoType  + " :  " 
				+  r3 + " :  " 
				+  isR1_satisfied + " :  " 
				+  isR2_satisfied + " :  " 
				+  recommend + " :  " 
				+  learntTPs[2]+ " :  " 
				+  learntTPs[3]+ " :  " 
				+  learntTPs[4]+ " :  " 
				+  learntTPs[5]+ " :  " 
				+  learntTPs[6]+ " :  " 
				+  infoLeaked + " :  "
				+  V;*/

	}

	
	
}
