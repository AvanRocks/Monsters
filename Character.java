import java.awt.image.BufferedImage;
import java.awt.*;

abstract class Character {
	private final int POS_INCREMENT = 5;
	public final static int UP = 0;
	public final static int LEFT = 1;
	public final static int DOWN = 2;
	public final static int RIGHT = 3;
	private int xPos, yPos;
	private int xVel, yVel;
	private int prevDir = DOWN;
	private int prevStep = 0;
	private BufferedImage[][] walk;

	Character(int x, int y, BufferedImage spriteSheet) {
		xPos=x;
		yPos=y;

		walk = new BufferedImage[4][9];
		for (int i=0;i<4;++i) {
			for (int j=0;j<9;++j) {
				walk[i][j] = spriteSheet.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}

	public void walk(int dir) {
		switch (dir) {
			case UP:   yVel=-POS_INCREMENT; prevDir=UP; break;
			case DOWN: yVel=POS_INCREMENT;  prevDir=DOWN; break;
			case LEFT: xVel=-POS_INCREMENT; prevDir=LEFT; break;
			case RIGHT:xVel=POS_INCREMENT;  prevDir=RIGHT; break;
		}
	}

	public void stop(int dir) {
		switch (dir) {
			case UP:
			case DOWN: yVel=0; break;
			case LEFT: 
			case RIGHT:xVel=0; break;
		}
	}
	
	public void reverseDir() {
		xVel=-xVel;
		yVel=-yVel;
	}

	public BufferedImage getImage() {
		if (xVel==0 && yVel==0) return walk[prevDir][0];
		if (prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, 64, 64);
	}

	public void updatePos() {
		xPos+=xVel;
		yPos+=yVel;
	}

	public int[] getPos() {
		return new int[] {xPos, yPos};
	}

}
