// Used for global game states that need to be shared across different
// classes. For example, a MutableBoolean "isGameActive" is needed by
// multiple classes, such as the enemy setting isGameActive to false when
// they strike the player and the game over screen reading the value in order
// to display itself
class MutableBoolean {
	private boolean value;

	MutableBoolean(boolean value) {
		setVal(value);
	}

	public boolean getVal() {
		return value;
	}

	public void setVal(boolean value) {
		this.value = value;
	}
}
