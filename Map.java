class Map {
	private int numRows = 10;
	private int numColumns = 10;
	private double blockWidth, blockHeight;
	private int level = 0;
	private Coordinate playerPos = new Coordinate(0,0);

	private int[][] map = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 0, 1, 1, 0, 3, 0, 0, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 1, 0, 0},
		{1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 1, 1, 1, 0, 0, 1, 1},
		{1, 2, 0, 0, 0, 0, 0, 1, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};

	public enum BlockType {
		WALL,
		OPEN,
		ENEMY_SPAWN,
		PLAYER_SPAWN
	}

	// For testing
	public Map(int blockWidth, int blockHeight) {
		this.blockWidth=blockWidth;
		this.blockHeight=blockHeight;

		level++;

		for (int y=0;y<getNumRows();++y)
			for (int x=0;x<getNumColumns();++x)
				if (getBlock(x,y) == BlockType.PLAYER_SPAWN) playerPos = new Coordinate(x,y);

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
			case 2:
				return BlockType.ENEMY_SPAWN;
			case 3:
				return BlockType.PLAYER_SPAWN;
		}
		return BlockType.OPEN;
	}

	public void setPlayerPos(int x, int y) {
		playerPos.setX(x);
		playerPos.setY(y);
	}

	public Coordinate getPlayerPos() { return playerPos; }

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
