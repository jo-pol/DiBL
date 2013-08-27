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
package dibl;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import dibl.Main;

public class MainTest
{
    @Test
    public void noArgs() throws Exception
    {
        Whitebox.setInternalState(Main.class,"README", "src/main/assembly/README.txt");
        Main.main();
    }
}
