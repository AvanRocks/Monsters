class Map {
	private int[][] map;

	public enum BlockType {
		WALL,
		OPEN,
	}

	public Map(int level) {
		int numRows = 10 * level + 3;
		int numColumns = 10 * level + 3;
		this.map = new int[numRows][numColumns];
	}

	public BlockType getBlock(int x, int y) {
		switch (map[y][x]) {
			case 0:
				return BlockType.WALL;
			case 1:
				return BlockType.OPEN;
		}
		return BlockType.OPEN;
	}
}
