// @formatter:off
/*
 * Copyright 2013, J. Pol
 *
 * This file is part of free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation.
 *
 * This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details. A copy of the GNU General Public License is
 * available at <http://www.gnu.org/licenses/>.
 */
// @formatter:on
package dibl.p2t;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class TemplateDocTest
{
    private static final String FOLDER = "target/TemplateDocTest/";

    @BeforeClass
    public static void createFolder()
    {
        new File(FOLDER).mkdirs();
    }

    @Test
    public void mix3x3() throws Exception
    {
        final TemplateDoc t = new TemplateDoc(new FileInputStream("src/main/assembly/cfg/3x3.svg"));
        final String[][] stitches = { {"tc", "tcptc", "tc"}, {"tc", "tcptc", "tc"}, {"tcptc", "tc", "tcptc"}};
        t.replaceClonesInBaseTile(stitches);
        t.write(new FileOutputStream(FOLDER + "3x3.svg"));
    }

    @Test
    public void mix4x4() throws Exception
    {
        final TemplateDoc t = new TemplateDoc(new FileInputStream("src/main/assembly/cfg/4x4.svg"));
        final String[][] stitches = { {"tcptc", "tc", "tcptc", "tc"}, {"tc", "tcptc", "tc", "tcptc"}, {"tcptc", "tc", "tcptc", "tc"},
                {"tc", "tcptc", "tc", "tcptc"}};
        t.replaceClonesInBaseTile(stitches);
        t.write(new FileOutputStream(FOLDER + "4x4.svg"));
    }
}
