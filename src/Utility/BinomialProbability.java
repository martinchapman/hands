package Utility;

import java.math.BigInteger;

/**
 * http://www.mathwords.com/b/binomial_probability_formula.htm
 * @author Martin
 */
public class BinomialProbability {

	/**
	 * 'Number of trials'
	 *	Number of remaining rounds, after the deceptive period
	 */
	private int minus_r;

	/**
	 * 'Number of successes'
	 * Number of rounds played deceptively for (and thus, 
	 * the number of times a single other node would need to 
	 * appear in order to be more highly weighted
	 * than any element of the deceptive set).
	 */
	private int r; 
	
	/**
	 * 'Number of failures'
	 */
	private int failures;
	
	/**
	 * 'Probability of success in one trial'
	 */
	private double p;
	
	/**
	 * 'Probability of failure in one trial'
	 */
	private double failure_in_one_trial;
	
	/**
	 * 
	 */
	private int numberOfHideLocations;
	
	/**
	 * @param numberOfHideLocations
	 */
	public void setNumberOfHideLocations(int numberOfHideLocations) {
		
		this.numberOfHideLocations = numberOfHideLocations;
	
	}
	
	/**
	 * @param minus_r (n)
	 * @param r (k)
	 * @param numberOfNodes
	 * @param numberOfHideLocations
	 */
	public BinomialProbability(int minus_r, int r, int numberOfNodes, int numberOfHideLocations) {
		
		this.minus_r = minus_r;
		
		this.r = r;
		
		this.failures = minus_r - r;
		
		setNumberOfHideLocations(numberOfHideLocations);
		
		this.p = numberOfHideLocations / (double)numberOfNodes;
		
		System.out.println(p);
		
		this.failure_in_one_trial = 1 - p;
		
	}
	
	/**
	 * @param minus_r (n)
	 * @param r (k)
	 * @param p
	 */
	public BinomialProbability(int minus_r, int r, double p) {
		
		this.minus_r = minus_r;
		
		this.r = r;
		
		this.failures = minus_r - r;
		
		this.p = p;
		
		this.failure_in_one_trial = 1 - p;
		
	}
	
	/**
	 * @return
	 */
	public double calculateBinomialProbability() {
		
		return nChoosesK(minus_r, r).doubleValue() * Math.pow(p, r) * Math.pow(failure_in_one_trial, minus_r - r);
		
	}
	
	/**
	 * Set number of remaining rounds (trials)
	 * @param minus_r (n)
	 */
	public void setMinus_r(int minus_r) {
		
		this.minus_r = minus_r;
		
		this.failures = minus_r - r;
		
	}
	
	/**
	 * Number of deceptive rounds (desired successes)
	 * @param r (k)
	 */
	public void setR(int r) {
		
		this.r = r;
		
		this.failures = minus_r - r;
		
	}
	
	/**
	 * @param numberOfNodes
	 */
	public void setP(int numberOfNodes) {
		
		this.p = (double)(numberOfHideLocations / numberOfNodes);
		
		this.failure_in_one_trial = 1 - p;
	
	}
	
	/**
	 * @param p
	 */
	public void setP(double p) {
		
		this.p = p;
		
		this.failure_in_one_trial = 1 - p;
		
	}
	
	// http://stackoverflow.com/questions/2201113/combinatoric-n-choose-r-in-java-math 
	/**
	 * @param N
	 * @param K
	 * @return
	 */
	static BigInteger nChoosesK(final int N, final int K) {
		
	    BigInteger ret = BigInteger.ONE;
	    
	    for (int k = 0; k < K; k++) {
	    
	    	ret = ret.multiply(BigInteger.valueOf(N-k)).divide(BigInteger.valueOf(k+1));
	    
	    }
	    
	    return ret;
	
	}
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// http://www.mathwords.com/b/binomial_probability_formula.htm
		// BinomialProbability bp = new BinomialProbability(10, 7, 0.25);
		
		BinomialProbability bp = new BinomialProbability(99 * 5, 1, 95, 1);
		
		for (int i = 1; i <= 100; i++) {
			
			int deceptionDuration = i;
			
			int remainingRounds = 100 - deceptionDuration;
			
			bp.setMinus_r(remainingRounds * 5);
			
			bp.setR(deceptionDuration);
			
			System.out.println("Deception Duration: " + i + " " + bp.calculateBinomialProbability());
			
		}
		
	}
	
}