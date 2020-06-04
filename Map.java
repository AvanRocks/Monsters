class Map {
	private int numRows;
	private int numColumns;

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
	public Map() {}

	public Map(int level) {
		numRows = 10 * level + 3;
		numColumns = 10 * level + 3;
		this.map = new int[numRows][numColumns];
	}

	public BlockType getBlock(int x, int y) {
		switch (map[y][x]) {
			case 0:
				return BlockType.WALL;
			case 1:
				return BlockType.OPEN;
			case 3:
				return BlockType.PLAYER_SPAWN;
		}
		return BlockType.OPEN;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}
}
