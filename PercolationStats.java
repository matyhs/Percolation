import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private static final double CONFIDENCE_LEVEL = 1.96;
    private final double[] openSites;
    private final int trials;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException();
        }

        openSites = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++)
        {
            Percolation percolation = new Percolation(n);
            
            while (!percolation.percolates())
            {
                percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }

            openSites[i] = percolation.numberOfOpenSites() * 1.0 / (n*n);
        }

    }

    // sample mean of percolation threshold
    public double mean()
    {
        mean = StdStats.mean(openSites);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        stddev = StdStats.stddev(openSites);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean - (CONFIDENCE_LEVEL * stddev/Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean + (CONFIDENCE_LEVEL * stddev/Math.sqrt(trials));
    }

   // test client (see below)
   public static void main(String[] args)
   {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.println("mean                        = " + stats.mean());
        StdOut.println("stddev                      = " + stats.stddev());
        StdOut.println("95% confidence interval     = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
   }
}