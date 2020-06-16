import java.util.*;

class GameThread implements Runnable {
    private ArrayList<Character> characters;
    private MutableBoolean gameIsActive;
    private GamePanel gamePanel;

    GameThread(ArrayList<Character> characters, MutableBoolean gameIsActive, GamePanel gamePanel) {
        this.characters=characters;
        this.gameIsActive=gameIsActive;
        this.gamePanel=gamePanel;
    }

    @Override
    public void run() {
        long prevTime, diffTime;

		// measure how long repaint() will take
        prevTime = System.currentTimeMillis();

        while (gameIsActive.getVal()) {
            // Move the characters
	    	for (Character c : characters)
		        c.updatePos();

            gamePanel.repaint();
    
            diffTime = System.currentTimeMillis() - prevTime;
    
            // Sleep for 75 milliseconds with accomodation for the time repaint() took
            try { Thread.sleep(75 - diffTime); }
            catch (InterruptedException e) {}
    
            // measure how long repaint() will take
            prevTime = System.currentTimeMillis();
        }
    }
}