import java.util.Arrays;

/**
 *
 *  @author Meng Wang
 *  @author Yuhan Xiao
 */
public class DynamicProgramming {

    /**
     * Finds a min-cost vertical cut on the input matrix.
     *
     * @param M the input matrix.
     * @return the min-cost vertical cut found.
     */
    public static int[] minCostVC(int[][] M) {
        int n = M.length;
        int m = M[0].length;
        int[][] previous = new int[n][m];
        int[] cut = new int[2 * n];

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int min = M[i - 1][j];
                previous[i][j] = j;
                if (j > 0 && min > M[i - 1][j - 1]) {
                    min = M[i - 1][j - 1];
                    previous[i][j] = j - 1;
                }
                if (j < m - 1 && min > M[i - 1][j + 1]) {
                    min = M[i - 1][j + 1];
                    previous[i][j] = j + 1;
                }
                M[i][j] += min;
            }
        }

        int minCost = 0, minCostColumn = 0;
        for (int j = 0; j < m; j++) {
            if (j == 0) {
                minCost = M[n - 1][0];
                minCostColumn = 0;
            } else if (minCost > M[n - 1][j]) {
                minCost = M[n - 1][j];
                minCostColumn = j;
            }
        }
        cut[(n - 1) * 2] = n - 1;
        cut[(n - 1) * 2 + 1] = minCostColumn;
        for (int i = n - 2; i >= 0; i--) {
            cut[i * 2] = i;
            cut[i * 2 + 1] = previous[i + 1][cut[(i + 1) * 2 + 1]];
        }
        return cut;
    }

    /**
     * Finds a string z (obtained by inserting $ at n − m indices in y)
     * such that AlignCost(x, z) <= AlignCost(x, y) over all possible y.
     *
     * @param x the string x.
     * @param y the string y.
     * @return the string z found.
     */
    public static String stringAlignment(String x, String y) {
        int[][] alignCost = new int[x.length() + 1][y.length() + 1];
        String[][] alignment = new String[x.length() + 1][y.length() + 1];
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (j == 0) {
                    alignCost[i][j] = 4 * i;
                    alignment[i][j] = "";
                    for (int k = 0; k < i; k++) {
                        alignment[i][j] += '$';
                    }

                } else if (i == 0) {
                    alignCost[i][j] = Short.MAX_VALUE;
                    alignment[i][j] = "";

                } else if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    alignCost[i][j] = alignCost[i - 1][j - 1];
                    alignment[i][j] = alignment[i - 1][j - 1] + y.charAt(j - 1);

                } else {
                    int cost1 = alignCost[i - 1][j - 1] + 2;
                    int cost2 = alignCost[i - 1][j] + 4;
                    if (cost1 < cost2) {
                        alignCost[i][j] = cost1;
                        alignment[i][j] = alignment[i - 1][j - 1] + y.charAt(j - 1);
                    } else {
                        alignCost[i][j] = cost2;
                        alignment[i][j] = alignment[i - 1][j] + "$";
                    }
                }
            }
        }
        return alignment[x.length()][y.length()];
    }

    public static void main(String[] args) {

        int[][] M = { { 1, 3, 2, 4 }, { 0, 1, 2, 5 }, { 1, 1, 0, 6 }, { 2, 1, 3, 5 } };
        System.out.println(Arrays.toString(minCostVC(M)));

        System.out.println(stringAlignment("abcd", "abcd"));
        System.out.println(stringAlignment("aaaa", "adcd"));
        System.out.println(stringAlignment("abcdefg", "aef"));
        System.out.println(stringAlignment("aabbcc", "bac"));
    }
}
