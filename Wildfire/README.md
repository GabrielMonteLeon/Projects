A terminal-based wildfire spread simulation written in C. The program models how fire spreads through a forest using probability rules, neighborhood effects, and time based burning stages. Includes both a print mode and an interactive overlay display mode.

Run Options
-H       Show help  
-bN      % of trees that start burning  
-cN      Probability a tree catches fire  
-dN      Tree density  
-nN      Neighbor influence threshold  
-pN      Number of states to print (enables print mode)  
-sN      Grid size  

Example run: ./wildfire -s20 -d50 -b10 -c30 -n25 -p50
