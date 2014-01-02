@echo off
rem ########################################################################################################
rem # Copyright 2013, J. Pol
rem #
rem # This file is part of free software: you can redistribute it and/or modify it under the terms of the
rem # GNU General Public License as published by the Free Software Foundation.
rem #
rem # This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
rem # the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
rem #
rem # See the GNU General Public License for more details. A copy of the GNU General Public License is
rem # available at <http://www.gnu.org/licenses/>.
rem ########################################################################################################
@echo on
java -jar ${project.artifactId}-${project.version}.jar 3;3^

tcptc;tc;tcptc^

tc;tcptc;tc^

tcptc;tc;tcptc 3;3^

(0,1,1,0,-1,-1);(1,0,1,0,-1,-1);(0,0,1,1,-1,-1)^

(0,1,1,-1,-1,0);(-1,1,0,1,0,-1);(0,1,1,0,-1,-1)^

(-1,1,1,0,-1,0);(0,0,0,0,0,0);(1,1,0,-1,0,-1)^

< input/PairTraversal/diamond/3x3.svg > output.svg
pause
