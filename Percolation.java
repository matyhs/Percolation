import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private WeightedQuickUnionUF grid;
    private boolean[][] openSites;
    private boolean[][] percolate;
    private int count;
    private int size;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        size = n;
        grid = new WeightedQuickUnionUF(n*n);
        openSites = new boolean[n][n];
        percolate = new boolean[n][n];

        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                openSites[i][j] = false;
                percolate[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (!isOpen(row, col))
        {
            openSites[row][col] = true;
            count++;

            if (row - 1 >= 0 && isOpen(row-1, col))
            {
                int parent = translate(row-1) + col;
                int child = translate(row) + col;
                grid.union(parent, child);
            }

            if (row + 1 < translate(grid.count()) && isOpen(row + 1, col))
            {
                int parent = translate(row+1) + col;
                int child = translate(row) + col;
                grid.union(parent, child);
            }

            if (col - 1 >= 0 && isOpen(row, col - 1))
            {
                int parent = translate(row) + col - 1;
                int child = translate(row) + col;
                grid.union(parent, child);
            }

            if (col + 1 < translate(grid.count()) && isOpen(row, col + 1))
            {
                int parent = translate(row) + col + 1;
                int child = translate(row) + col;
                grid.union(parent, child);
            }

            percolate[row][col] = isFull(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        return openSites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        return grid.find(translate(row)+col) / size < 1;
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return count;
    }

    // does the system percolate?
    public boolean percolates()
    {
        boolean isPercolated = false;

        for(int i = 0; i < size; i++)
        {
            isPercolated = isPercolated && percolate[size][i];
        }

        return isPercolated;
    }

    private int translate(int index)
    {
        return index * size;
    }

    // test client (optional)
    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
            StdOut.println(p + " " + q);
            StdOut.println(percolation.numberOfOpenSites() + " components");
            StdOut.println(percolation.percolates());
        }
    }
}