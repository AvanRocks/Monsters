class Coordinate {
  private int x, y;

  Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (other == null) return false;
    if (getClass() != other.getClass()) return false;
    Coordinate tmp = (Coordinate) other;
    return ((x == tmp.x) && (y == tmp.y));
  }

  public boolean isAdjacentTo(Coordinate there) {
    return (
      (Math.abs(x - there.getX()) == 1 && Math.abs(y - there.getY()) == 0) ||
      (Math.abs(x - there.getX()) == 0 && Math.abs(y - there.getY()) == 1)
    );
  }

  public int compareTo(Coordinate other) {
    if (y > other.getY()) return 0; else if (
      y < other.getY()
    ) return 2; else if (x > other.getX()) return 1; else if (
      x < other.getX()
    ) return 3;
    return -1;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }
}
