package dibl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.BeforeClass;
import org.junit.Test;

import dibl.matrix.MatrixReader;
import dibl.p2t.TemplateDoc;

public class ApiTest
{
    private static final String OUTPUT_FOLDER = "target/" + ApiTest.class.getSimpleName() + "/";
    private static final String INPUT_FOLDER = "src/test/resources/";
    private String[][] stitches = new String[][] { //
    {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}, //
            {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}};

    @BeforeClass
    public static void createFolder()
    {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void all() throws Exception
    {
        loop("3x3_", 8,INPUT_FOLDER+"/3x3.svg");
        loop("4x4_", 222,INPUT_FOLDER+"/4x4.svg");
    }

    private void loop(String dim, int n, String template) throws IOException, FileNotFoundException, JDOMException
    {
        for (int i = 1; i < n; i++)
        {
            String[][] tuples = MatrixReader.read(new FileInputStream(INPUT_FOLDER + dim + i + ".txt"));
            new TemplateDoc(template).replaceBoth(stitches, tuples).write(new FileOutputStream(OUTPUT_FOLDER + dim + i + "_both.svg"));
            new TemplateDoc(template).replaceTuples(tuples,"tc").write(new FileOutputStream(OUTPUT_FOLDER + dim + i + "_tuples.svg"));
        }
    }
}
