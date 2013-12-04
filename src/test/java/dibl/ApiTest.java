package dibl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dibl.diagrams.PTP;
import dibl.diagrams.SM;
import dibl.diagrams.Template;
import dibl.math.Matrix;
import dibl.math.ShortTupleFlipper;

public class ApiTest
{
    private static final String OUTPUT_FOLDER = "target/" + ApiTest.class.getSimpleName() + "/";
    private static final String INPUT_FOLDER = "src/test/resources/";
    private final List<Closeable> closeables = new ArrayList<Closeable>();
    private final String[][] stitches = new String[][] { //
    {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}, //
            {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}};

    @BeforeClass
    public static void createFolder()
    {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @After
    public void clearClosables() throws IOException
    {
        for (final Closeable closeable : closeables)
            closeable.close();
        closeables.clear();
    }

    @Test
    public void all() throws Exception
    {
        loop("3x3_", 8, new Template(INPUT_FOLDER + "/3x3.svg"));
        loop("4x4_", 222, new Template(INPUT_FOLDER + "/4x4.svg"));
    }

    private void loop(final String dimensions, final int n, final Template template) throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream(OUTPUT_FOLDER + dimensions + "flip.txt"));
        for (int i = 1; i < n; i++)
        {
            final String[][] tuples = Matrix.read(openInput(INPUT_FOLDER + dimensions + i + ".txt"));
            template.replaceBoth(stitches, tuples).write(openOutput(OUTPUT_FOLDER + dimensions + i + "_both.svg"));
            out.println((dimensions + i));
            out.println("  " + Arrays.deepToString(tuples));
            out.println("X " + Arrays.deepToString(new PTP(tuples).flipBottomUp()));
            out.println("Y " + Arrays.deepToString(new PTP(tuples).flipLeftRight()));
        }
        out.close();
    }

    @Test
    public void flipOldAlongX() throws Exception
    {
        final String[][] input = Matrix.read(openInput("src/main/assembly/input/4x4_1.txt"));
        final String[][] tuples = new Matrix<ShortTupleFlipper>(input, new ShortTupleFlipper()).flipNW2SE();
        final Template template = new Template(openInput("src/main/assembly/cfg/4x4.svg"));
        template.replaceBoth(stitches, tuples);
        template.write(openOutput(OUTPUT_FOLDER + "4x4_1_flippedOldAlongX.svg"));
    }

    @Test
    public void flipOldwithNew() throws Exception
    {
        final String[][] input = Matrix.read(openInput("src/main/assembly/input/4x4_1.txt"));
        final String[][] tuples = new PTP(input).flipNW2SE();
        final Template template = new Template(openInput("src/main/assembly/cfg/4x4.svg"));
        template.replaceBoth(stitches, tuples);
        template.write(openOutput(OUTPUT_FOLDER + "4x4_1_flippedOlWithNew.svg"));
    }

    @Test
    public void colorCoded() throws Exception
    {
        String[][] ccStitches = new String[][] { //
        {"-tc", "-ctc", "-tctc"}, //
                {"-tctc", "-tc", "-ctc"}, //
                {"-tcptc", "-ctcpctc", "-tctcptctc",}};
        final String[][] tuples = PTP.read(openInput(INPUT_FOLDER + "3x3_1.txt"));
        Template template = new Template(openInput(INPUT_FOLDER + "3x3.svg"));
        template.replaceBoth(ccStitches, tuples);
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccNormal.svg"));
        template.replaceBoth(//
                new SM(ccStitches).flipBottomUp(),//
                new PTP(tuples).flipBottomUp());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccBottomUp.svg"));
        template.replaceBoth(//
                new SM(ccStitches).flipLeftRight(), //
                new PTP(tuples).flipLeftRight());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccLeftRight.svg"));
        template.replaceBoth(//
                new SM(new SM(ccStitches).flipBottomUp()).flipLeftRight(),//
                new PTP(new PTP(tuples).flipBottomUp()).flipLeftRight());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccRotated.svg"));
    }

    @Test
    public void flipNewAlongX() throws Exception
    {
        for (int n = 1; n < 20; n++)
        {
            final FileInputStream input = openInput(INPUT_FOLDER + "4x4_" + n + ".txt");
            final String[][] tuples = new PTP(input).flipBottomUp();
            final Template template = new Template(openInput(INPUT_FOLDER + "4x4.svg"));
            template.replaceBoth(stitches, tuples);
            template.write(openOutput(OUTPUT_FOLDER + "4x4_" + n + "_flippedNewAlongX.svg"));
        }
    }

    @Test
    public void flipNewAlongY() throws Exception
    {
        for (int n = 1; n < 20; n++)
        {
            final FileInputStream input = openInput(INPUT_FOLDER + "4x4_" + n + ".txt");
            final String[][] tuples = new PTP(input).flipLeftRight();
            final Template template = new Template(openInput(INPUT_FOLDER + "4x4.svg"));
            template.replaceBoth(stitches, tuples);
            template.write(openOutput(OUTPUT_FOLDER + "4x4_" + n + "_flippedNewAlongY.svg"));
        }
    }

    @Test
    public void rotateNew() throws Exception
    {
        final FileInputStream input = openInput(INPUT_FOLDER + "4x4_1.txt");
        final String[][] bottomUp = new PTP(input).flipBottomUp();
        final String[][] rotated = new PTP(bottomUp).flipLeftRight();
        final Template template = new Template(openInput(INPUT_FOLDER + "4x4.svg"));
        template.replaceBoth(stitches, rotated);
        template.write(openOutput(OUTPUT_FOLDER + "4x4_1_newRotated.svg"));
    }

    private FileInputStream openInput(final String file) throws Exception
    {
        final FileInputStream inputStream = new FileInputStream(file);
        closeables.add(inputStream);
        return inputStream;
    }

    private FileOutputStream openOutput(final String file) throws Exception
    {
        final FileOutputStream outputStream = new FileOutputStream(file);
        closeables.add(outputStream);
        return outputStream;
    }
}
