import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

class Map {
	private int numRows = 10;
	private int numColumns = 10;
	private double blockWidth, blockHeight;
	private int level = 0;
	private Coordinate playerPos = new Coordinate(0,0);
	private ArrayList<Character> characters;

	private int[][] map = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
		{1, 2, 1, 1, 0, 3, 0, 0, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 1, 0, 0},
		{1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 1, 1, 1, 0, 0, 1, 1},
		{1, 2, 0, 0, 0, 0, 2, 1, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};

	public enum BlockType {
		WALL,
		OPEN,
		ENEMY_SPAWN,
		PLAYER_SPAWN
	}

	// For testing
	public Map(double panelWidth, double panelHeight, CardLayout cards, Container pane, MutableBoolean gameIsActive) {
		updateBlockSize(panelWidth, panelHeight);

		characters = new ArrayList<Character>();

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

	public ArrayList<Character> getCharacters() { return characters; }

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
