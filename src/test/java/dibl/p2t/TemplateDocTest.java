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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;

import org.junit.Test;

import dibl.p2t.TemplateDoc;

public class TemplateDocTest
{
    @Test
    public void test() throws Exception
    {
        final TemplateDoc t = new TemplateDoc(new FileInputStream("src/main/assembly/cfg/3x3.svg"));
        assertThat(t.getStitchTypes("(0,1,1,0,-1,-1)").toArray().length,is(2));
        assertThat(t.getStitchTypes("(0,1,1,0,-1,-1)").contains("tcptc"),is(true));
    }
}
