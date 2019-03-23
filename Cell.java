package summerHW4;

import java.awt.Color;
import java.awt.Graphics;

/**
 * http://paleyontology.com/AP_CS/entrance.html
 */
public class Cell {
	private int row, col;
	private boolean myAlive; // alive (true) or dead (false)
	private Cell[] myNeighbors; // count of neighbors with respect to x,y
	private Color myColor; // Based on alive/dead rules
	private final Color DEFAULT_ALIVE = Color.ORANGE;
	private final Color DEFAULT_DEAD = Color.GRAY;

	public Cell(int row, int col) {
		this(row, col, false, Color.GRAY);
	}

	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		this.row = row;
		this.col = col;
	}

	public boolean getAlive() {
		return myAlive;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Color getColor() {
		return myColor;
	}

	public void setAlive(boolean alive) {
		if (alive) {
			setAlive(true, DEFAULT_ALIVE);
		} else {
			setAlive(false, DEFAULT_DEAD);
		}
	}

	public void setAlive(boolean alive, Color color) {
		myColor = color;
		myAlive = alive;
	}

	public void setColor(Color color) {
		myColor = color;
	}

	public Cell[] getNeighbors() {
		return myNeighbors;
	}

	public void calcNeighbors(Cell[][] cells) {
		myNeighbors = new Cell[8];
		for (int i = 0; i < 8; i++)
		{
			myNeighbors[i] = getNeighborCell(i);
		}
	}
	
	/**
	 * 0 1 2 
	 * 7 & 3
	 * 6 5 4
	 * @param cell the cell with the neighbor
	 * @param index the neighbor number, from 0 to 7
	 * @return neighborCell the neighbor cell
	 */
	private Cell getNeighborCell(int index)
	{
		int nRow;
		int nCol;
		// checking top and bottom
		if (index <= 2 && row == 0)
			nRow = Display.ROWS-1;
		else if (index >= 4 && index <= 6 && row == Display.ROWS-1)
			nRow = 0;
		else if (index == 7 || index == 3)
			nRow = row;
		else if (index <= 2)
			nRow = row - 1;
		else 
			nRow = row + 1;
		// checking left and right
		if ((index == 7 || index == 6 || index == 0) && col == 0)
			nCol = Display.COLS-1;
		else if (index >= 2 && index <= 4 && col == Display.COLS-1)
			nCol = 0;
		else if (index == 5 || index == 1)
			nCol = col;
		else if (index == 7 || index == 6 || index == 0)
			nCol = col - 1;
		else 
			nCol = col + 1;
		return Display.cell[nRow][nCol];
	}

	public void draw(int x_offset, int y_offset, int width, int height,
			Graphics g) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (col * (width + 1));
		int ytop = y_offset + 1 + (row * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);
		g.setColor(temp);
	}
}