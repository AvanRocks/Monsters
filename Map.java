class Map {
	private int numRows = 10;
	private int numColumns = 10;
	private double blockWidth, blockHeight;
	private int level = 0;

	private int[][] map = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 0, 1, 1, 0, 3, 0, 0, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 1, 0, 0},
		{1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 1, 1, 1, 0, 0, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};

	public enum BlockType {
		WALL,
		OPEN,
		PLAYER_SPAWN
	}

	// For testing
	public Map(int blockWidth, int blockHeight) {
		this.blockWidth=blockWidth;
		this.blockHeight=blockHeight;

		level++;

		//numRows = 3 * level + 10;
		//numColumns = 3 * level + 10;
		//this.map = new int[numRows][numColumns];
	}

/*
	public Map(int level) {
		numRows = 10 * level + 3;
		numColumns = 10 * level + 3;
		this.map = new int[numRows][numColumns];
	}
*/

	public BlockType getBlock(int x, int y) {
		switch (map[y][x]) {
			case 0:
				return BlockType.OPEN;
			case 1:
				return BlockType.WALL;
			case 3:
				return BlockType.PLAYER_SPAWN;
		}
		return BlockType.OPEN;
	}

	public double getBlockWidth() {return blockWidth;}

	public double getBlockHeight() {return blockHeight;}

	public void updateBlockSize(double panelWidth, double panelHeight) {
		blockWidth = panelWidth / numColumns;
		blockHeight = panelHeight / numRows;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}
}
