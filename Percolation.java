import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private WeightedQuickUnionUF grid;
    private boolean[][] openSites;
    private boolean[][] fullSites;
    private int count;
    private int size;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        size = n;
        grid = new WeightedQuickUnionUF(n*n);
        openSites = new boolean[n][n];
        fullSites = new boolean[n][n];

        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                openSites[i][j] = false;
                fullSites[i][j] = false;
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
            connectSites(row, col);
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
       return fullSites[row][col];
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return count;
    }

    // does the system percolate?
    public boolean percolates()
    {
        for (int i = 0; i < size; i++)
        {
            connectFullSites(0, i);

            if(isFull(size-1, i))
            {
                return true;
            }
        }

        fullSites = new boolean[size][size];
        return false;
    }

    private int translate(int index)
    {
        return index * size;
    }

    private void connectSites(int row, int col)
    {
        if (row - 1 >= 0 && isOpen(row-1, col))
        {
            int parent = translate(row-1) + col;
            int child = translate(row) + col;

            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row-1, col);
            }
        }

        if (row + 1 < size && isOpen(row + 1, col))
        {
            int parent = translate(row+1) + col;
            int child = translate(row) + col;
            
            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row+1, col);
            }
        }

        if (col - 1 >= 0 && isOpen(row, col - 1))
        {
            int parent = translate(row) + col - 1;
            int child = translate(row) + col;

            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row, col - 1);
            }
        }

        if (col + 1 < size && isOpen(row, col + 1))
        {
            int parent = translate(row) + col + 1;
            int child = translate(row) + col;
            
            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row, col+1);
            }
        }
    }

    private boolean connected(int parent, int child)
    {
        return grid.find(parent) == grid.find(child);
    }

    private void connectFullSites(int row, int col)
    {
        if (row >= size) return;
        if (row < 0) return;
        if (col >= size) return;
        if (col < 0) return;
        if (!isOpen(row, col)) return;
        if (isFull(row, col)) return;

        fullSites[row][col] = true;
        connectFullSites(row-1, col);
        connectFullSites(row+1, col);
        connectFullSites(row, col-1);
        connectFullSites(row, col+1);
    }

    // test client (optional)
    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        for (int i = 0; i < n; i++) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
            StdOut.println(p + " " + q);
        }
        
        StdOut.println(percolation.numberOfOpenSites() + " components");
        StdOut.println(percolation.percolates());
    }
}