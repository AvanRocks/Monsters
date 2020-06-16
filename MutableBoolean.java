class MutableBoolean {
	private boolean value;

	MutableBoolean(boolean value) {
		setVal(value);
	}

	public boolean getVal() { return value; }
	public void setVal(boolean value) { this.value=value; }
}
