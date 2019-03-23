package summerHW4;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;


// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.


public class Display extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;

	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cell = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 5;
	private final int CELL_HEIGHT = 5;

	public final int DISPLAY_WIDTH;   
	public final int DISPLAY_HEIGHT;
	private StartButton startStop;
	private NextButton next;
	private ClearButton clear;
	private boolean paintloop = false;
	
	public Display(int width, int height) 
	{
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}

	
	public void init() 
	{
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		
		//StartButton, NextButton, ClearButton classes nested below
		startStop = new StartButton(this);
		startStop.setBounds(100, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		
		next = new NextButton(this);
		next.setBounds(240, 550, 100, 36);
		add(next);
		next.setVisible(true);
		
		clear = new ClearButton(this);
		clear.setBounds(380, 550, 100, 36);
		add(clear);
		clear.setVisible(true);
		repaint();
	}


	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 100;

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		drawButtons();
		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			repaint();
		}
	}


	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
		cell[36][22].setAlive(true); // sample use of cell mutator method
		cell[36][23].setAlive(true); // sample use of cell mutator method
		cell[36][24].setAlive(true); // sample use of cell mutator method
	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}


	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}

	
	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}


	private void drawButtons() {
		startStop.repaint();
	}

	
	/**
	 * Changes 'cell' to the next generation
	 */
	private void nextGeneration() 
	{
		Cell[] neighbors;
		Cell[][] nextGen = this.getNewGrid(ROWS, COLS);
		int filledNeighbors;
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++)
			{
				cell[row][col].calcNeighbors(cell);
				neighbors = cell[row][col].getNeighbors();
				filledNeighbors = 0;
				for (int k = 0; k < neighbors.length; k++)
				{
					if (neighbors[k].getAlive())
						filledNeighbors++;
				}
				//exactly 3 cells around = born
				if (filledNeighbors == 3 && !cell[row][col].getAlive())
				{
					nextGen[row][col].setAlive(true);
				}
				//overcrowding = dead
				else if (filledNeighbors >= 4 && cell[row][col].getAlive())
				{
					nextGen[row][col].setAlive(false);
				}
				//loneliness = dead
				else if (filledNeighbors <= 1 && cell[row][col].getAlive())
				{
					nextGen[row][col].setAlive(false);
				}
				//otherwise leave as is
				else
				{
					nextGen[row][col].setAlive(cell[row][col].getAlive());
				}
			}
		}
		cell = nextGen;
	}
	
	private Cell[][] getNewGrid(int rows, int cols)
	{
		Cell[][] grid = new Cell[rows][cols];
		for (int row = 0; row < grid.length; row++)
		{
			for (int col = 0; col < grid[row].length; col++)
			{
				grid[row][col] = new Cell(row, col);
			}
		}
		return grid;
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		int mouseX = e.getX();
		int mouseY = e.getY();
		int gridX = (int) Math.floor((mouseX - X_GRID_OFFSET)/(CELL_WIDTH+1));
		int gridY = (int) Math.floor((mouseY - Y_GRID_OFFSET)/(CELL_HEIGHT+1));
		if (gridX < COLS && gridX >= 0 && gridY < ROWS && gridY >= 0)
		{
			cell[gridY][gridX].setAlive(!cell[gridY][gridX].getAlive());
			this.repaint();
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}


	private class StartButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		private Display display;
		StartButton(Display display) {
			super("Start");
			this.display = display;
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			nextGeneration(); // test the start button
			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
			} else {
				togglePaintLoop();
				setText("Start");
			}
			display.repaint();
		}
	}
	
	private class NextButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		private Display display;
		NextButton(Display display) {
			super("Step");
			this.display = display;
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			nextGeneration();
			display.repaint();
		}
	}
	
	private class ClearButton extends JButton implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		private Display display;
		ClearButton(Display display)
		{
			super("Clear");
			this.display = display;
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			Display.cell = getNewGrid(ROWS, COLS);
			display.repaint();
		}
	}
}
