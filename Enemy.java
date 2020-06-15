import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;

class Enemy extends Character {
	private BufferedImage[][] attack = new BufferedImage[4][6];
	private int attackStep = 0;
	private int steps = 0;
	private int blocksTravelled = 0;
	private ArrayList<ArrayList<Boolean>> visited;
	private ArrayList<ArrayList<Integer>> direction;
	private ArrayList<Integer> path;
	private boolean isAttacking;

	Enemy(int x, int y, BufferedImage spriteSheet, Map map) {
    super(x,y,spriteSheet,map);

		// load attacking animation of enemy
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 6; ++j) {
        attack[i][j] = spriteSheet.getSubimage(576 + j*192, i*192, 192, 192);
      }
    }

		resetArrays(map);
    calcPath();
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

		System.out.println("enemyX: " + startX + ", enemyY: " + startY);
		queue.add(new Coordinate(startX, startY));
		visited.get(startY).set(startX, true);

		Coordinate s;
		while ((s = queue.poll()) != null) {
			if (s.equals(map.getPlayerPos())) break;
			if (visit(s.getX()+1, s.getY(), Direction.RIGHT, map)) queue.add( new Coordinate(s.getX()+1, s.getY()));
			if (visit(s.getX()-1, s.getY(), Direction.LEFT, map)) queue.add( new Coordinate(s.getX()-1, s.getY()));
			if (visit(s.getX(), s.getY()+1, Direction.DOWN, map)) queue.add( new Coordinate(s.getX(), s.getY()+1));
			if (visit(s.getX(), s.getY()-1, Direction.UP, map)) queue.add( new Coordinate(s.getX(), s.getY()-1));
		}

		path = new ArrayList<>();
		int x = map.getPlayerPos().getX();
		int y = map.getPlayerPos().getY();
		System.out.println("playerX: " + x + ", playerY: " + y);

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
		System.out.println(blocksTravelled);
		Map map = getMap();
		double speed = getSpeed();

		System.out.println("x: " + getX() + " y: " + getY());
		if (steps == (int)(1/speed)) {
			blocksTravelled++;
			System.out.println("Travelled a block");

			// this is needed if speed does not evenly divide 1
			if (getX() % 1 > 0.999) setX((int)getX()+1);
			if (getY() % 1 > 0.999) setY((int)getY()+1);

			steps=0;
		if (steps == (int)(1 / speed)) {
			blocksTravelled++;
			steps = 0;
			if (blocksTravelled == 5 || blocksTravelled >= path.size() || path.size() == 0) {
				calcPath();
				blocksTravelled=0;
			}
		}

		Coordinate pos = new Coordinate((int)getX(), (int)getY());
		if (pos.equals(map.getPlayerPos())) {
			isAttacking = true;
		} else {
			isAttacking = false;

			walk(path.get(blocksTravelled));
			checkCollision(path.get(blocksTravelled));
			steps++;
		}
	}

	@Override
	public BufferedImage getImage() {
		if (!isAttacking)
			return super.getImage();
		else {
			if (attackStep == 5) attackStep = 0;
			return attack[getPrevDir()][attackStep++];
		}
	}

	@Override
	public void drawImage(Graphics g) {
		if (!isAttacking)
			super.drawImage(g);
		else {
			Map map = getMap();
			g.drawImage(getImage(), (int)((getX()-1) * map.getBlockWidth()), (int)((getY()-1) * map.getBlockHeight()), (int)(3*map.getBlockWidth()), (int)(3*map.getBlockHeight()), null);
		}

	}

}
