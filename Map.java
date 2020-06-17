import java.util.*;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;

class Map {
	private int numRows;
	private int numColumns;
	private double blockWidth, blockHeight;
	private int level = 0;
	private Coordinate playerPos = new Coordinate(0,0);
	private ArrayList<Character> characters;
	private int[] link;
	private int[] size;
	private ArrayList<Line2D.Double> walls = null;

	/*
	private int[][] map = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 2, 1, 1, 0, 3, 0, 0, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 1, 0, 4},
		{1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 1, 1, 1, 0, 0, 1, 1},
		{1, 2, 0, 0, 0, 0, 2, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	*/

	private int[][] map;

	// these are actually inverse edges
	// they determine where there are NO walls
	private int[][] edges;

	public enum BlockType {
		EMPTY,
		ENEMY_SPAWN,
		PLAYER_SPAWN
	}

	// For testing
	public Map(double panelWidth, double panelHeight, CardLayout cards, Container pane, MutableBoolean gameIsActive) {
		updateBlockSize(panelWidth, panelHeight);

		characters = new ArrayList<Character>();
		generateMap();

		level++;

		// create player
		for (int y=0;y<getNumRows();++y)
			for (int x=0;x<getNumColumns();++x)
				if (getBlock(x,y) == BlockType.PLAYER_SPAWN) {
					playerPos = new Coordinate(x,y);
  			  try { characters.add( new Player(playerPos.getX(), playerPos.getY(), ImageIO.read(new File("images"+File.separator+"player.png")), this) ); }
					catch (IOException e) { e.printStackTrace();}
				}

    // create Enemies at their specified starting positions
    for (int y = 0; y < numRows; ++y) {
      for (int x = 0; x < numColumns; ++x) {
				// create enemy
				if (getBlock(x,y) == Map.BlockType.ENEMY_SPAWN) {
          try { characters.add( new Enemy(x, y, ImageIO.read(new File("images"+File.separator+"enemy-orc.png")), this, cards, pane, gameIsActive) ); }
					catch (IOException e) { e.printStackTrace();}
        }
      }
		}


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
				return BlockType.EMPTY;
			case 1:
				return BlockType.ENEMY_SPAWN;
			case 2:
				return BlockType.PLAYER_SPAWN;
		}
		return BlockType.EMPTY;
	}

	private int find(int x) {
		while (x != link[x])
			x=link[x];
		return x;
	}

	private boolean same(int a, int b) {
		return find(a)==find(b);
	}

	private void unite(int a, int b) {
		a=find(a);
		b=find(b);
		if (size[a]>size[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		link[a]=b;
		size[b]+=size[a];
	}

	private void generateMap() {
		numColumns = (int)(Math.random()*7)+7;
		numRows = (int)(Math.random()*7)+7;

		edges = new int[numRows][numColumns];
		map = new int[numRows][numColumns];

		// Eller's algorithm for maze generation

		int[][] set = new int[2][numColumns];
		link = new int[numColumns*(numRows+1) + 1];
		size = new int[numColumns*(numRows+1) + 1];

		// initialize link and size
		for (int i=1;i<numColumns*numRows + 1;++i) link[i]=i;
		for (int i=1;i<numColumns*numRows + 1;++i) size[i]=1;

		// initialize first row
		for (int x=0;x<numColumns;++x)
			set[0][x]=x+1;

		// set number : ArrayList of indices
		HashMap<Integer,ArrayList<Integer>> sets = new HashMap<>();

		for (int y=0;y<numRows;++y) {
			// randomly merge adjacent cells which are in different sets
			for (int x=0;x<numColumns;++x) {
				if (x+1<numColumns && !same(set[0][x],set[0][x+1]) && ((int)(Math.random()*2) == 0)) {
					unite(set[0][x],set[0][x+1]);
					edges[y][x] = (1<<Direction.RIGHT);
				}
			}
		
			// find all sets
			for (int x=0;x<numColumns;++x) {
				int tmp = find(set[0][x]);
				sets.putIfAbsent(tmp, new ArrayList<>());
				sets.get(tmp).add(x);
			}

			// randomly add vertical connections (at least one per set)
			for (Entry<Integer,ArrayList<Integer>> entry : sets.entrySet()) {
				int numOfConnections = (int)(Math.random()*entry.getValue().size())+1;
				for (int i=0;i<numOfConnections;++i) {
					int randIdx = (int)(Math.random()*entry.getValue().size());
					set[1][entry.getValue().get(randIdx)] = set[0][entry.getValue().get(randIdx)];
					edges[y][entry.getValue().get(randIdx)] |= (1<<Direction.DOWN);
					entry.getValue().remove(randIdx);
				}
			}

			// put untouched 2nd row cells in their own set
			for (int x=0;x<numColumns;++x)
				if (set[1][x] == 0)
					set[1][x] = (y+1)*getNumColumns() + x+1;

			// move 2nd row up, and make a new 2nd row
			set[0] = set[1];
			set[1] = new int[numColumns];
		
			sets.clear();
		}

		// for the last row, merge all distinct adjacent sets
		for (int x=0;x<numColumns-1;++x) {
			if (!same(set[0][x],set[0][x+1])) {
				unite(set[0][x],set[0][x+1]);
				edges[numRows-1][x] |= (1<<Direction.RIGHT);
			}
		}

		// place the player and enemies' spawns
		int playerX = (int)(Math.random()*numColumns);
		int playerY = (int)(Math.random()*numRows);
		map[playerY][playerX] = 2;

		int numEnemies = (int)(Math.random()*3)+1;
		for (int i=0;i<numEnemies;++i) {
			int enemyX, enemyY;
			do {
				enemyX = (int)(Math.random()*numColumns);
				enemyY = (int)(Math.random()*numRows);
			} while (Math.abs(enemyX-playerX) + Math.abs(enemyY-playerY) < 5);
			map[enemyY][enemyX] = 1;
		}
	}

	public ArrayList<Character> getCharacters() { return characters; }

	public int getEdge(int x, int y) { return edges[y][x]; }

	public Line2D.Double[] getWalls() { return (walls == null) ? null : walls.toArray(new Line2D.Double[0]); }
	public void setWalls(ArrayList<Line2D.Double> walls) { this.walls = walls; }

	public void setPlayerPos(int x, int y) {
		playerPos.setX(x);
		playerPos.setY(y);
	}

	public Coordinate getPlayerPos() { return playerPos; }

	public double getBlockWidth() { return blockWidth; }
	public double getBlockHeight() {return blockHeight;}

	public void updateBlockSize(double panelWidth, double panelHeight) {
		blockWidth = panelWidth / numColumns;
		blockHeight = panelHeight / numRows;
	}

	public int getNumRows() { return numRows; }
	public int getNumColumns() { return numColumns; }
}