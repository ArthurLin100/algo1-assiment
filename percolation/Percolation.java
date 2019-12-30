/* *****************************************************************************
 *  Name: Percolation.java
 *  Date: 2019-Dec-28
 *  Description: https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private byte[][] site; // 0:blocked, 1:open
    private int N, vrtTopIdx, vrtBotIdx, openSites;
    private WeightedQuickUnionUF uf;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        N = n;
        site = new byte[N][N];
        vrtTopIdx = N * N;
        vrtBotIdx = N * N + 1;
        uf = new WeightedQuickUnionUF(N * N
                                              + 2); // 0 ~ (N*N-1)th  maps to the site. (N*N)th and (N*N-1)threfers to the top and the bottrom virtual node

        // link the top/bottom row to the virtual nodes
        for (int i = 0; i < N; i++) {
            uf.union(i, vrtTopIdx); // top row
            uf.union(N * N - 1 - i, vrtBotIdx); // bottom row
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isValidIdx(row, col);
        row = row - 1; // The caller is passing 0 ~ N. Change to array index
        col = col - 1; // change to array index
        int up, dwn, lft, rit, ctr;
        ctr = row * N + col;
        up = ctr - N;
        dwn = ctr + N;
        lft = ctr - 1;
        rit = ctr + 1;
        if (site[row][col] == 0) // if it's not open
        {
            openSites++;
            site[row][col] = 1; // mark it as open

            if (row > 0) { // only from the 2nd row there has a up
                if (site[row - 1][col] == 1) { // if the up is open
                    uf.union(up, ctr);
                }
            }
            if (row < N - 1) {
                if (site[row + 1][col] == 1) { // if the down is open
                    uf.union(dwn, ctr);
                }
            }
            if (col > 0) {
                if (site[row][col - 1] == 1) { // if the left is open
                    uf.union(lft, ctr);
                }
            }
            if (col < N - 1) {
                if (site[row][col + 1] == 1) { // if the right is open
                    uf.union(rit, ctr);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValidIdx(row, col);
        row = row - 1; // change to array index
        col = col - 1; // change to array index
        return ((site[row][col] == 1));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isValidIdx(row, col);
        if (isOpen(row, col)) {
            int rootTop, rootThis;
            row = row - 1; // change to array index
            col = col - 1; // change to array index
            rootThis = uf.find(row * N + col);
            rootTop = uf.find(vrtTopIdx);
            return (rootThis == rootTop);
        }
        else {
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (uf.find(vrtTopIdx) == uf.find(vrtBotIdx));
    }

    private void isValidIdx(int i, int j) {
        if (!(i > 0 && i <= N && j > 0 && j <= N)) {
            throw new IndexOutOfBoundsException("i=" + i + " j=" + j);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int nN = Integer.parseInt(args[0]);
        int row = Integer.parseInt(args[1]);
        int col = Integer.parseInt(args[2]);
        Percolation perco = new Percolation(nN);
        perco.open(row, col);
    }


}
