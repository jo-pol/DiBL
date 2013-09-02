package dibl.matrix;

public class Extractor
{
    public static String[][] shift(String[][] mat, int row, int col)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[r][c] = mat[(r + row) % M][(c + col) % N];
        return ret;
    }

    public static String[][] skewDown(String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[r][c] = mat[(r + c) % M][c];
        return ret;
    }

    public static String[][] skewUp(String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[r][c] = mat[(r + M - c) % M][c];
        return ret;
    }
}
