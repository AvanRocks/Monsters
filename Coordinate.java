class Coordinate {
	private int x, y;

	Coordinate(int x, int y) {
		this.x=x;
		this.y=y;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (getClass() != other.getClass()) return false;
		Coordinate tmp = (Coordinate) other;
		return ((x==tmp.x) && (y==tmp.y));
	}

	public int getX() {return x;}
	public int getY() {return y;}

	public void setX(int x) { this.x=x; }
	public void setY(int y) { this.y=y; }
}
