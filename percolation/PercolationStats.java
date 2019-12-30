/* *****************************************************************************
 *  Name: PercolationStats.java
 *  Date: 2019-Dec-29
 *  Description: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trial;
    private double thMean, thStddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        Percolation perco;
        trial = trials;
        double[] thresh = new double[trials];
        int tmpI, tmpJ;
        boolean isOpen;
        for (int i = 0; i < trials; i++) {
            perco = new Percolation(n);
            while (!perco.percolates()) {
                do { // get a pair of random index at which the element is closed
                    tmpI = StdRandom.uniform(n) + 1; // convert 0~n-1 to 1~n
                    tmpJ = StdRandom.uniform(n) + 1;
                    isOpen = perco.isOpen(tmpI, tmpJ);
                } while (isOpen);
                perco.open(tmpI, tmpJ); // open a random closed element
            }
            thresh[i] = perco.numberOfOpenSites() / (double) (n * n);
        }
        thMean = StdStats.mean(thresh);
        thStddev = StdStats.stddev(thresh);
    }

    // sample mean of percolation threshold
    public double mean() {
        return thMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return thStddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return thMean - (1.96 * thStddev / Math.sqrt(trial));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return thMean + (1.96 * thStddev / Math.sqrt(trial));
    }

    // test client (see below)
    public static void main(String[] args) {
        int nN = Integer.parseInt(args[0]);
        int tT = Integer.parseInt(args[1]);
        PercolationStats percoSt = new PercolationStats(nN, tT);
        System.out.println("mean = " + percoSt.mean());
        System.out.println("stddev = " + percoSt.stddev());
        System.out.println("95% confidence interval = [ " + percoSt.confidenceLo() + ", " + percoSt
                .confidenceHi() + " ]");
    }

}
