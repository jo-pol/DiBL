package dibl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import dibl.matrix.MatrixReader;
import dibl.matrix.MatrixTransformer;
import dibl.p2t.TemplateDoc;
import dibl.p2t.TupleTransformer;

public class ApiTest
{
    private static final String OUTPUT_FOLDER = "target/" + ApiTest.class.getSimpleName() + "/";
    private static final String INPUT_FOLDER = "src/test/resources/";
    private final MatrixTransformer<TupleTransformer> transformer = new MatrixTransformer<TupleTransformer>(new TupleTransformer());
    private List<Closeable> closeables=new ArrayList<Closeable>();
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
        for (Closeable closeable : closeables)
            closeable.close();
        closeables.clear();
    }

    @Test
    public void all() throws Exception
    {
        loop("3x3_", 8, new TemplateDoc(INPUT_FOLDER + "/3x3.svg"));
        loop("4x4_", 222, new TemplateDoc(INPUT_FOLDER + "/4x4.svg"));
    }

    private void loop(final String dimensions, final int n, final TemplateDoc template) throws IOException, FileNotFoundException, JDOMException
    {
        for (int i = 1; i < n; i++)
        {
            final String[][] tuples = MatrixReader.read(openInputStream(INPUT_FOLDER + dimensions + i + ".txt"));
            template.replaceBoth(stitches, tuples).write(openOutputStream(OUTPUT_FOLDER + dimensions + i + "_both.svg"));
        }
    }

    @Test
    public void flipOldAlongX() throws Exception
    {
        final MatrixTransformer<TupleTransformer> transformer = new MatrixTransformer<TupleTransformer>(new TupleTransformer());
        final String[][] tuples = MatrixReader.read(openInputStream("src/main/assembly/input/4x4_1.txt"));
        final TemplateDoc template = new TemplateDoc(openInputStream("src/main/assembly/cfg/4x4.svg"));
        template.replaceBoth(stitches, transformer.flipNW2SE(tuples));
        template.write(openOutputStream(OUTPUT_FOLDER + "4x4_1_flippedOldAlongX.svg"));
    }

    @Ignore("static initializer of TupleTransformer does not accommodate longer tuples")
    @Test
    public void flipNewAlongX() throws Exception
    {
        final String[][] tuples = transformer.flipBottomUp(MatrixReader.read(openInputStream(INPUT_FOLDER + "4x4_1.txt")));
        final TemplateDoc template = new TemplateDoc(openInputStream(INPUT_FOLDER + "4x4.svg"));
        template.replaceBoth(stitches, tuples);
        template.write(openOutputStream(OUTPUT_FOLDER + "4x4_1_flippedNewAlongX.svg"));
    }

    @Ignore("static initializer of TupleTransformer does not accommodate longer tuples")
    @Test
    public void rotateNew() throws Exception
    {
        final String[][] tuples = rotate(MatrixReader.read(openInputStream(INPUT_FOLDER + "4x4_1.txt")));
        final TemplateDoc template = new TemplateDoc(openInputStream(INPUT_FOLDER + "4x4.svg"));
        template.replaceBoth(stitches, tuples);
        template.write(openOutputStream(OUTPUT_FOLDER + "4x4_1_newRotated.svg"));
    }

    private String[][] rotate(final String[][] tuples)
    {
        return transformer.flipLeftRight(transformer.flipBottomUp(tuples));
    }

    private FileInputStream openInputStream(String file) throws FileNotFoundException
    {
        FileInputStream inputStream = new FileInputStream(file);
        closeables.add(inputStream);
        return inputStream;
    }

    private FileOutputStream openOutputStream(String file) throws FileNotFoundException
    {
        FileOutputStream outputStream = new FileOutputStream(file);
        closeables.add(outputStream);
        return outputStream;
    }
}
