package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Grid {
	/* Chose a 1d array instead of 2d so it'll be easy to forward and back
	 * without having to mess with columns and rows
	 * the 0 index is not used in any of the arrays in this code
	 */
	int[] finalGrid = new int[82];
	
	/*
	 * 2d arrays that hold the indices of rows, columns, and 3x3 blocks
	 * used to search through to determine which set of cells a given cell affects
	 */
	int[][] rowIndices = new int[10][10];
	int[][] colIndices = new int[10][10];
	int[][] blockIndices = {{},
							{ 0, 1, 2, 3,10,11,12,19,20,21},
							{ 0, 4, 5, 6,13,14,15,22,23,24},
							{ 0, 7, 8, 9,16,17,18,25,26,27},
							{ 0,28,29,30,37,38,39,46,47,48},
							{ 0,31,32,33,40,41,42,49,50,51},
							{ 0,34,35,36,43,44,45,52,53,54},
							{ 0,55,56,57,64,65,66,73,74,75},
							{ 0,58,59,60,67,68,69,76,77,78},
							{ 0,61,62,63,70,71,72,79,80,81}};
	
	/*
	 * an array of sets of each cell's exceptions:
	 */
	HashSet[] cellExceptions = new HashSet[82];
	
	/*
	 * Constructor
	 */
	public Grid() {
		
		// initialization
		int cellIndex = 1;
		for(int i = 1; i < 10; i++) {
			for(int j = 1; j < 10; j++) {
				rowIndices[i][j] = cellIndex;
				colIndices[j][i] = cellIndex;
				cellIndex++;
			}
		}
		
		for(int i = 1; i < 82; i++)
			cellExceptions[i] = new HashSet();
	}
	
	/*
	 * generates a list from 1 to 9 in random order
	 */
	private ArrayList getCellCandidates() {
		Random random = new Random();
		ArrayList list = new ArrayList();
		list.add(0);
		for(int i = 1; i < 10; i++) {
			int temp = random.nextInt(9) + 1;
			if(!list.contains(temp))
				list.add(temp);
			else
				i--;
		}
		
		return list;
	}
	
	/*
	 *  calls the recursive addNum method the first time
	 *  written this way for clarity: the main method will call this method
	 *  and this method will initiate the recursive addNum
	 */
	public void generateSolution() {
		int cellIndex = 1;
		addNum(cellIndex, cellExceptions);
	}
	
	/*
	 * addNum is where the recursive backtracking algorithm is implemented
	 */
	public void addNum(int cellIndex, HashSet[] cellExceptions) {
		
		if(cellIndex == 82) {
			printGrid(finalGrid);
			return;
		}
		
		ArrayList cellCandidates = getCellCandidates();
		
		for(int i = 1; i < cellCandidates.size(); i++) {
			
			int candidate = (int) cellCandidates.get(i);
			if(!cellExceptions[cellIndex].contains(candidate)) {
				// place in cell
				finalGrid[cellIndex] = candidate;

				// add to affected cells' exceptions
				addExceptions(cellIndex, candidate, cellExceptions);

				cellIndex+=1;
				// recursive call
				printGrid(finalGrid);
				addNum(cellIndex, cellExceptions);	
			}
			else if(i == cellCandidates.size()-1) {
				return;
			}
		}
	}
	
	public void addExceptions(int cellIndex, int exception, HashSet[] cellExceptions) {
		//find which row, col, 3x3 block a cell belongs to
		int row = findRow(cellIndex);
		int col = findCol(cellIndex);
		int block = findBlock(cellIndex);
		
		// exceptions are added here
		for(int i = 1; i < 10; i++) {
			
			cellExceptions[rowIndices[row][i]].add(exception);
			cellExceptions[colIndices[col][i]].add(exception);
			cellExceptions[blockIndices[block][i]].add(exception);
		}
	}
	
	/*
	 * find methods locate which row, col, and 3x3 block
	 * a cell belongs to
	 */
	private int findRow(int cellIndex) {
		for(int i = 1; i < 10; i++)
			for(int j = 1; j < 10; j++)
				if(rowIndices[i][j] == cellIndex)
					return i;
		return 0;
	}
	
	private int findCol(int cellIndex) {
		for(int i = 1; i < 10; i++)
			for(int j = 1; j < 10; j++)
				if(colIndices[i][j] == cellIndex)
					return i;
		return 0;
	}
	
	private int findBlock(int cellIndex) {
		for(int i = 1; i < 10; i++)
			for(int j = 1; j < 10; j++)
				if(blockIndices[i][j] == cellIndex)
					return i;
		return 0;
	}
	
	/*
	 * various print methods mostly for debugging purposes
	 * 	
	 */
	public void printGrid(int[][] grid) {
		
		for(int i = 1; i < 10; i++) {
			for(int j = 1; j < 10; j++) 
				System.out.print(grid[i][j] + " ");
			System.out.println();
		}
	}
	
	public void printGrid(int[] grid) {
		System.out.println("GRID\n=====================");
		for(int i = 1; i < 82; i ++) {
			System.out.print(grid[i] + " ");
			if(i%3==0) System.out.print("|");
			if(i%9 == 0) System.out.println();
			if(i%27==0)System.out.println("--------------------");
		}
	}

	public void printRow(int[] row) {
		for(int i = 1; i < 10; i++)
			System.out.print(row[i] + " ");
	}

	public void printCol(int[] col) {
		for(int i = 1; i < 10; i++) {
			System.out.println(col[i]);
		}
	}
	
	public void printBlock(int[] block) {
		for(int i = 1; i < 10; i++) {
			System.out.print(block[i] + " ");
			if(i%3 == 0) System.out.println();
		}
	}







}
