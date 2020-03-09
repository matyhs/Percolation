import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private final WeightedQuickUnionUF grid;
    private boolean[][] openSites;
    private boolean[][] fullSites;
    private boolean[][] visitedSites;
    private int count;
    private final int size;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException();
        }

        size = n;
        grid = new WeightedQuickUnionUF(n*n);
        openSites = new boolean[n][n];
        fullSites = new boolean[n][n];
        visitedSites = new boolean[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                openSites[i][j] = false;
                fullSites[i][j] = false;
                visitedSites[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (!validateArguments(rowConvert, colConvert))
        {
            throw new IllegalArgumentException("Row: " + row + " Col: " + col + " Size: " + size);
        }

        if (!isOpen(row, col))
        {
            openSites[rowConvert][colConvert] = true;
            count++;
            connectSites(row, col);
            connectFullSites(row, col, 0);
            visitedSites = new boolean[size][size];
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (!validateArguments(rowConvert, colConvert))
        {
            throw new IllegalArgumentException("Row: " + row + " Col: " + col + " Size: " + size);
        }

        return openSites[rowConvert][colConvert];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (!validateArguments(rowConvert, colConvert))
        {
            throw new IllegalArgumentException("Row: " + row + " Col: " + col + " Size: " + size);
        }

       return fullSites[rowConvert][colConvert];
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return count;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return isFullSite(size, 1);
    }

    private int translate(int index)
    {
        return index * size;
    }

    private void connectSites(int row, int col)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (rowConvert - 1 >= 0 && isOpen(row-1, col))
        {
            int parent = translate(rowConvert-1) + colConvert;
            int child = translate(rowConvert) + colConvert;

            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row-1, col);
            }
        }

        if (rowConvert + 1 < size && isOpen(row + 1, col))
        {
            int parent = translate(rowConvert+1) + colConvert;
            int child = translate(rowConvert) + colConvert;
            
            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row+1, col);
            }
        }

        if (colConvert - 1 >= 0 && isOpen(row, col - 1))
        {
            int parent = translate(rowConvert) + colConvert - 1;
            int child = translate(rowConvert) + colConvert;

            if (!connected(parent, child))
            {
                grid.union(parent, child);
                connectSites(row, col - 1);
            }
        }

        if (colConvert + 1 < size && isOpen(row, col + 1))
        {
            int parent = translate(rowConvert) + colConvert + 1;
            int child = translate(rowConvert) + colConvert;
            
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

    private void connectFullSites(int row, int col, int topSiteIndex)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (!validateArguments(rowConvert, colConvert)) return;
        if (topSiteIndex >= size) return;
        if (!isOpen(row, col)) return;
        if (isFull(row, col)) return;
        if (visitedSites[rowConvert][colConvert]) return;

        fullSites[rowConvert][colConvert] = connected(topSiteIndex, translate(rowConvert)+colConvert);

        if (!fullSites[rowConvert][colConvert])
        {
            connectFullSites(row, col, topSiteIndex+1);
        }
        
        visitedSites[rowConvert][colConvert] = true;
        connectFullSites(row-1, col, topSiteIndex);
        connectFullSites(row+1, col, topSiteIndex);
        connectFullSites(row, col-1, topSiteIndex);
        connectFullSites(row, col+1, topSiteIndex);
    }

    private boolean validateArguments(int row, int col)
    {
        if (row >= size || row < 0 || col >= size || col < 0)
        {
            return false;
        }

        return true;
    }

    private int indexConvert(int index)
    {
        return index - 1;
    }

    private boolean isFullSite(int row, int col)
    {
        int rowConvert = indexConvert(row);
        int colConvert = indexConvert(col);

        if (!validateArguments(rowConvert, colConvert))
        {
            return false;
        }

        if (isFull(row, col))
        {
            return true;
        }

        return isFullSite(row, col+1);
    }

    // test client (optional)
    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        for (int i = 0; i < n; i++) 
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
            StdOut.println(p + " " + q);
        }
        
        StdOut.println(percolation.numberOfOpenSites() + " components");
        StdOut.println(percolation.percolates());
    }
}