package dibl.matrix;

public class MatrixTransformer<H extends Transformer<String>>
{
    private H helper;

    public MatrixTransformer(H helper)
    {
        this.helper = helper;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] rotate180(String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[M - 1 - r][N - 1 - c] = helper.rotate180(mat[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipLeftRight(String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[r][N - 1 - c] = helper.flipLeftRight(mat[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipBottomUp(String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[M - 1 - r][c] = helper.flipBotomUp(mat[r][c]);
        return ret;
    }
}
