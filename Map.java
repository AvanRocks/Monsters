class Map {
	private int map[][];

	public enum BlockType {
		WALL,
		OPEN,
	};

	public Map(int numRows, int numColumns) {
		this.map = new int[numRows][numColumns];
	}

	public int getBlock(int x, int y) {
		return map[y][x];
	}
}
