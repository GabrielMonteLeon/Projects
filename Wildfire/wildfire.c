///
/// @file    wildfire.c
/// @author Gabriel MonteLeon
//

#define _DEFAULT_SOURCE
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "display.h"
#include <unistd.h> 

static int Burning = 10;
static int Catching = 30;
static int Density = 50;
static int NeighborEffect = 25;
static int States = -1;
static int Size = 10;
static int Cycles = 0;
static int Changes = 0;
static int TotalChanges = 0;


///  Loops through and prints the grid
///
/// @param size - Size of the array
/// @param grid - The grid to be printed
void printGrid(int size, char grid[][size]) {
	for (int i = 0; i < Size; i++) {
		for (int j = 0; j < Size; j++) {
			printf("%c", grid[i][j]);
		}
		printf("\n");
	}
}
///  Checks if a tree is suseptible to burning
///
/// @param size - Size of the array
/// @param forest - The forest to be checked
/// @param row - The row to be checked
/// @param col - The col to be checked
/// @return Bool if the tree is sueptible

bool isSuseptible(int size, char forest[][size], int row, int col) {
	int neighborsBurning = 0;
	int totalNeighbors = 0;
	for (int i = -1; i < 2; i++)
	{
		for (int j = -1; j < 2; j++)
		{
			if (i != 0 || j != 0)
			{
				int row_check = row + i;
				int col_check = col + j;
				if (row_check == -1)
				{
					continue;
				}
				if (row_check == size)
				{
					continue;
				}
				if (col_check == -1)
				{
					continue;
				}
				if (col_check == size)
				{
					continue;
				}
				if (forest[row_check][col_check] == 'Y') {
					totalNeighbors++;
				}
				if (forest[row_check][col_check] == '*') {
					totalNeighbors++;
					neighborsBurning++;
				}
			}
		}
	}
	if (totalNeighbors == 0) {
		return false;
	}
	if ((100 * neighborsBurning) / totalNeighbors > NeighborEffect) {
		return true;
	}
	return false;
}

///  Spreads the fire based off the first array and creates the second array
///
/// @param size - Size of the array
/// @param forest - The forest to be checked
/// @param newForest - The forest made after changes
void spread(int size, char forest[][size], char newForest[][size]) {

	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			newForest[i][j] = forest[i][j];
			if (forest[i][j] == 'Y' && isSuseptible(size, forest, i, j)) {
				int potentialSpread = (random() % 101);
				if (potentialSpread < Catching) {
					newForest[i][j] = '*';
					Changes++;
				}
			}
		}
	}
}


///  Spreads the fire based off spread and then changes trees that have been burning for an extended amount of time into burnt trees
///
/// @param size - Size of the array
/// @param forest - The forest to be checked
/// @param burning - The array of every tree's time spent burning
void update(int size, char forest[][size], int burning[][size]) {
	Changes = 0;
	//spread
	char newForest[size][size];
	spread(size, forest, newForest);

	//copy spread and update burning age
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			forest[i][j] = newForest[i][j];
			if (forest[i][j] == '*') {
				burning[i][j]++;
				if (burning[i][j] > 3) {
					forest[i][j] = '.';
					Changes++;
				}
			}
		}
	}
	TotalChanges += Changes;
}

///  Checks if there are any remaining burning trees
///
/// @param size - Size of the array
/// @param forest - The forest to be checked
/// @return Bool if there is a burning tree
bool anyBurning(int size, char forest[][size]) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			if (forest[i][j] == '*') {
				return true;
			}
		}
	}
	return false;
}

/// prints the usage of the file
void PrintUsage() {
	fprintf(stderr,
		"usage: wildfire [options]\n"
		"By default, the simulation runs in overlay display mode.\n"
		"The -pN option makes the simulation run in print mode for up to N states.\n\n"
		"Simulation Configuration Options:\n"
		" -H  # View simulation options and quit.\n"
		" -bN # proportion of trees that are already burning. 0 < N < 101.\n"
		" -cN # probability that a tree will catch fire. 0 < N < 101.\n"
		" -dN # density: the proportion of trees in the grid. 0 < N < 101.\n"
		" -nN # proportion of neighbors that influence a tree catching fire. -1 < N < 101.\n"
		" -pN # number of states to print before quitting. -1 < N < ...\n"
		" -sN # simulation grid size. 4 < N < 41.\n\n"
	);
}

int main(int argc, char* argv[]) {
	srandom(41);
	int opt;
	opterr = 0;
	//checks comand line inputs
	while ((opt = getopt(argc, argv, "Hb:c:d:n:p:s:")) != -1) {
		switch (opt) {
		case 'H':
			PrintUsage();
			exit(0);
			break;
		case 'b':
			Burning = atoi(optarg);
			if (Burning < 1 || Burning>100) {
				fprintf(stderr, "(-bN) proportion already burning must be an integer in [1...100].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		case 'c':
			Catching = atoi(optarg);
			if (Catching < 1 || Catching>100) {
				fprintf(stderr, "(-cN) probability a tree will catch fire must be an integer in [1...100].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		case 'd':
			Density = atoi(optarg);
			if (Density < 1 || Density>100) {
				fprintf(stderr, "(-dN) density of trees in the grid must be an integer in [1...100].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		case 'n':
			NeighborEffect = atoi(optarg);
			if (NeighborEffect < 0 || NeighborEffect>100) {
				fprintf(stderr, "(-nN) %%neighbors influence catching fire must be an integer in [0...100].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		case 'p':
			States = atoi(optarg);
			if (States < 0 || States>10000) {
				fprintf(stderr, "(-pN) number of states to print must be an integer in [0...10000].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		case 's':
			Size = atoi(optarg);
			if (Size < 5 || Size>40) {
				fprintf(stderr, "(-sN) simulation grid size must be an integer in [5...40].\n");
				PrintUsage();
				exit(EXIT_FAILURE);
			}
			break;
		default:
			fprintf(stderr, "Unknown option: -%c\n", optopt);
			PrintUsage();
			exit(EXIT_FAILURE);
		}
	}

	//initializes forest and burning age
	char forest[Size][Size];
	int burningAge[Size][Size];
	for (int i = 0; i < Size; i++) {
		for (int j = 0; j < Size; j++) {
			forest[i][j] = ' ';
			burningAge[i][j] = 0;
		}
	}

	//randomizes trees and burning trees based off density and burning
	int startingTrees = (Size * Size) / (100 / Density);
	int remainingBurningTrees = (Size * Size) / (100 / Burning);
	int treesAdded = 0;
	while (treesAdded < startingTrees) {
		int x = (random() % Size);
		int y = (random() % Size);
		if (forest[x][y] == ' ') {
			treesAdded++;
			forest[x][y] = 'Y';
			if (remainingBurningTrees > 0) {
				forest[x][y] = '*';
				burningAge[x][y] = 0;
				remainingBurningTrees--;
			}
		}
	}

	//Print Mode
	if (States != -1) {
		printf("===========================\n======== Wildfire =========\n===========================\n=== Print %d Time Steps ===\n===========================\n", States);
		while (Cycles <= States) {
			update(Size, forest, burningAge);
			printGrid(Size, forest);
			printf("size %d, pCatch %.2f, density %.2f, pBurning %.2f, pNeighbor %.2f\ncycle %d, current changes %d, cumulative changes %d.", Size, Catching / 100.0, Density / 100.0, Burning / 100.0, NeighborEffect / 100.0, Cycles, Changes, TotalChanges);
			printf("\n");
			Cycles++;
			if (!anyBurning(Size, forest)) {
				printf("Fires are out.\n");
				break;
			}
		}
	}

	//Overlay Display Mode
	else {
		clear();
		while (1) {
			set_cur_pos(1, 1);
			for (int i = 0; i < Size; i++) {
				for (int j = 0; j < Size; j++) {
					put(forest[i][j]);
				}
				put('\n');
			}
			printf("size %d, pCatch %.2f, density %.2f, pBurning %.2f, pNeighbor %.2f\ncycle %d, current changes %d, cumulative changes %d.", Size, Catching / 100.0, Density / 100.0, Burning / 100.0, NeighborEffect / 100.0, Cycles, Changes, TotalChanges);
			usleep(75000);
			update(Size, forest, burningAge);
			Cycles++;
			if (!anyBurning(Size, forest)) {
				set_cur_pos(Size + 1, 1);
				printf("Fires are out.\n\n");
					break;
			}
		}
	}
	return 0;
}

