import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private double[] openSites;
    private int _trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException();
        }

        openSites = new double[trials];
        _trials = trials;

        for(int i = 0; i < trials; i++)
        {
            Percolation percolation = new Percolation(n);
            
            while(!percolation.percolates())
            {
                percolation.open(StdRandom.uniform(n), StdRandom.uniform(n));
            }

            openSites[i] = percolation.numberOfOpenSites() * 1.0 / (n*n);
        }

    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(openSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(openSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - (1.96 * stddev()/Math.sqrt(_trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + (1.96 * stddev()/Math.sqrt(_trials));
    }

   // test client (see below)
   public static void main(String[] args)
   {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0])
                                                        , Integer.parseInt(args[1]));

        StdOut.println("mean                        = " + stats.mean());
        StdOut.println("stddev                      = " + stats.stddev());
        StdOut.println("95% confidence interval     = [" + stats.confidenceLo() + ", " + stats.confidenceHi());
   }
}