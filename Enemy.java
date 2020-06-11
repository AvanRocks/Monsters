import java.awt.image.BufferedImage;
import java.util.*;

class Enemy extends Character {
	private BufferedImage[][] attack = new BufferedImage[4][6];
	private int steps = -1;
	private int blocksTravelled = 0;
	private ArrayList<ArrayList<Boolean>> visited;
	private ArrayList<ArrayList<Integer>> direction;
	private ArrayList<Integer> path;

	Enemy(int x, int y, BufferedImage spriteSheet, Map map) {
    super(x,y,spriteSheet,map);

		// load attacking animation of enemy
    for (int i=0;i<4;++i) {
      for (int j=0;j<6;++j) {
        attack[i][j] = spriteSheet.getSubimage(576 + j*192, i*192, 192, 192);
      }
    }

		resetArrays(map);
  }

	private void resetArrays(Map map) {
		// reset direction
		visited = new ArrayList<ArrayList<Boolean>>();
		for (int i=0;i<map.getNumRows();++i) {
			visited.add(new ArrayList<Boolean>(map.getNumColumns()));
			for (int j=0;j<map.getNumColumns();++j)
				visited.get(i).add(false);
		}

		// reset direction
		direction = new ArrayList<ArrayList<Integer>>();
		for (int i=0;i<map.getNumRows();++i) {
			direction.add(new ArrayList<Integer>(map.getNumColumns()));
			for (int j=0;j<map.getNumColumns();++j)
				direction.get(i).add(0);
		}
	}

	private boolean visit(int x, int y, int dir, Map map) {
		if (x<0 || x>=map.getNumColumns() || y<0 || y>=map.getNumRows()) return false;
		if (visited.get(y).get(x)) return false;
		if (map.getBlock(x,y) == Map.BlockType.WALL) {
			return false;
		}

		visited.get(y).set(x, true);
		direction.get(y).set(x, dir);


		return true;
	}

	// find shortest path to player using breadth-first search
	private void calcPath() {
		Map map = getMap();
		resetArrays(map);

		Queue<Coordinate> queue = new LinkedList<>();
		int startX = (int) getX();
		int startY = (int) getY();
		queue.add(new Coordinate(startX, startY));
		visited.get(startY).set(startX, true);

		System.out.println(startX + ", " + startY);
		Coordinate s;
		while ((s = queue.poll()) != null) {
			if (s == map.getPlayerPos()) break;
			if (visit(s.getX()+1, s.getY(), Direction.RIGHT, map)) queue.add( new Coordinate(s.getX()+1, s.getY()));
			if (visit(s.getX()-1, s.getY(), Direction.LEFT, map)) queue.add( new Coordinate(s.getX()-1, s.getY()));
			if (visit(s.getX(), s.getY()+1, Direction.DOWN, map)) queue.add( new Coordinate(s.getX(), s.getY()+1));
			if (visit(s.getX(), s.getY()-1, Direction.UP, map)) queue.add( new Coordinate(s.getX(), s.getY()-1));
		}

		path = new ArrayList<>();
		int x = map.getPlayerPos().getX();
		int y = map.getPlayerPos().getY();

		while (!(x == startX && y == startY)) {
			path.add(direction.get(y).get(x));
			switch (direction.get(y).get(x)) {
				case Direction.UP:    ++y; break;
				case Direction.DOWN:  --y; break;
				case Direction.LEFT:  ++x; break;
				case Direction.RIGHT: --x; break;
			}
		}

		Collections.reverse(path);
		System.out.println(path);
	}

	@Override
	public void updatePos() {
		Map map = getMap();
		double speed = getSpeed();

		if (steps == -1) {
			calcPath();
			steps=0;
		}

		System.out.println(getX() + ", " + getY());
		if (steps == (int)(1/speed)) {
			blocksTravelled++;
			if (getX() % 1 > 0.999) setX((int)getX()+1);
			if (getY() % 1 > 0.999) setY((int)getY()+1);
			steps=0;
			System.out.println(blocksTravelled);
			if (blocksTravelled == 5 || blocksTravelled >= path.size()) {
				calcPath();
				blocksTravelled=0;
			}
		}

		walk(path.get(blocksTravelled));
		//checkCollision(direction.get((int)(getY())).get((int)(getX())));
		steps++;
	}

}
