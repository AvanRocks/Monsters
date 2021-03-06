import java.util.*;

class GameThread implements Runnable {
	private final ArrayList<Character> characters;
	private final MutableBoolean gameIsActive;
	private final GamePanel gamePanel;
	private final Map map;

	GameThread(
		ArrayList<Character> characters,
		MutableBoolean gameIsActive,
		GamePanel gamePanel,
		Map map
	) {
		this.characters = characters;
		this.gameIsActive = gameIsActive;
		this.gamePanel = gamePanel;
		this.map = map;
	}

	@Override
	public void run() {
		long prevTime, diffTime;

		// measure how long updating and repaint() will take
		prevTime = System.currentTimeMillis();

		while (gameIsActive.getVal()) {
			// Move the characters
			for (Character c : characters) {
				c.updatePos();
				c.updateImage();
			}

			// tell map the player's new position
			map.setPlayerPos(
				(int) Math.round(characters.get(0).getX()),
				(int) Math.round(characters.get(0).getY())
			);

			map.setExactPlayerPos(characters.get(0).getX(), characters.get(0).getY());

			gamePanel.repaint();

			diffTime = System.currentTimeMillis() - prevTime;

			// Sleep for 75 milliseconds with accomodation for the time that updating and repaint() took
			try {
				Thread.sleep(75 - diffTime);
			} catch (InterruptedException ignored) {}

			// measure how long updating and repaint() will take
			prevTime = System.currentTimeMillis();
		}
	}
}
