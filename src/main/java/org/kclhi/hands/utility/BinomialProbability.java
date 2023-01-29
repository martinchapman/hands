package org.kclhi.hands.utility;

import java.math.BigInteger;
import java.util.ArrayList;

import org.kclhi.hands.utility.output.gnuplot.GNUGraph;
import org.kclhi.hands.utility.output.gnuplot.GNULineGraph;

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
	 * @param minus_r (n or 'number of trials')
	 * @param r (k or 'number of successes')
	 * @param numberOfNodes
	 * @param numberOfHideLocations (taken with the above to calculate a probability of success in each trial, e.g. 1 (hide location) / 100 (nodes).)
	 */
	public BinomialProbability(int minus_r, int r, int numberOfNodes, int numberOfHideLocations) {
		
		this.minus_r = minus_r;
		
		this.r = r;
		
		this.failures = minus_r - r;
		
		setNumberOfHideLocations(numberOfHideLocations);
		
		this.p = numberOfHideLocations / (double)numberOfNodes;
		
		this.failure_in_one_trial = 1 - p;
		
	}
	
	/**
	 * @param minus_r (n or 'number of trials')
	 * @param r (k or 'number of successes')
	 * @param p
	 */
	public BinomialProbability(int minus_r, int r, double p) {
		
		this.minus_r = minus_r;
		
		this.r = r;
		
		this.failures = minus_r - r;
		
		this.p = p;
		
		this.failure_in_one_trial = 1 - p;
		
	}
	
	private double calculateBinomialProbability(int r) {
		
		return ( nChoosesK(minus_r, r).doubleValue() * Math.pow(p, r) ) * Math.pow(failure_in_one_trial, minus_r - r);
		
	}
	/**
	 * @return
	 */
	public double calculateRSuccesses() {
		
		return calculateBinomialProbability(r);
		
	}
	
	/**
	 * Or greater = cumulative binomial
	 * @return
	 */
	public double calculateRorGreaterSuccesses() {
		
		double cumulativeLessThanR = 0.0;
		
		for ( int i = 0; i < r; i++ ) {
			
			cumulativeLessThanR += calculateBinomialProbability(i);
			
		}
		
		return 1 - cumulativeLessThanR;
		
	}
	
	/**
	 * Probability of r - 1 successes in n - 1 trials, times by p (the probability in the current nth trial).
	 * 
	 * @param nth trial 
	 * @param rth sucesses
	 * @return
	 */
	public double calculateRthInNth(int nth, int rth) {
		
		return ( ( nChoosesK(nth - 1, rth - 1).doubleValue() * Math.pow(p, r - 1) ) * Math.pow(failure_in_one_trial, ( ( nth - 1 ) - ( rth - 1) )) ) * p;
		
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
	
	private void generateForDeceptive() {
		
		// http://www.mathwords.com/b/binomial_probability_formula.htm
		// BinomialProbability bp = new BinomialProbability(10, 7, 0.25);
		
		GNULineGraph graph = new GNULineGraph("", false);
		
		BinomialProbability bp = new BinomialProbability(99, 1, 95, 5);
		
		ArrayList<Double> values = new ArrayList<Double>();
		
		for (int i = 1; i <= 100; i++) {
			
			int deceptionDuration = i;
			
			int remainingRounds = 100 - deceptionDuration;
			
			bp.setMinus_r(remainingRounds);
			
			bp.setR(deceptionDuration);
			
			values.add(bp.calculateRorGreaterSuccesses());
			
			System.out.println("Deception Duration: " + i + " " + bp.calculateRorGreaterSuccesses());
			
		}
		
		//Deprecated
		//graph.setXStart(1);
		
		graph.startsAtOne();
		
		graph.addDataset("", values);
		
		graph.styleGraph();
		
		graph.createChart("", "$\\delta$ ($K$)", "$P$");
		
		graph.exportChartAsEPS(Utils.FILEPREFIX + "data/charts/binomial.eps");
		
		graph.exportChartAsTikz(Utils.FILEPREFIX + "data/charts/binomial.tex");
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BinomialProbability bp = new BinomialProbability(2, 2, 100, 5);
		
		bp.generateForDeceptive();
		
		System.out.println(bp.calculateRthInNth(2, 2));
		
		System.out.println(nChoosesK(10, 1).doubleValue());
		
	}
	
}
