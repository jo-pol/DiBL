java -jar dibl-${pom.version}.jar \
'3;3
tcptc;tc;tcptc
tc;tcptc;tc
tcptc;tc;tcptc' \
'3;3
(0,1,1,0,-1,-1);(1,0,1,0,-1,-1);(0,0,1,1,-1,-1)
(0,1,1,-1,-1,0);(-1,1,0,1,0,-1);(0,1,1,0,-1,-1)
(-1,1,1,0,-1,0);(0,0,0,0,0,0);(1,1,0,-1,0,-1)' \
< input/PairTraversal/diamond/3x3.svg \
> output.svg
