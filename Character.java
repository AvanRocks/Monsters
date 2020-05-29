import java.awt.image.BufferedImage;

abstract class Character {
	final int POS_INCREMENT = 5;
	final static int UP = 0;
	final static int LEFT = 1;
	final static int DOWN = 2;
	final static int RIGHT = 3;
	int xPos, yPos;
	int xVel, yVel;
	int prevDir = DOWN;
	int prevStep = 0;
	BufferedImage[][] walk;

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

	public void move(int dir) {
		switch (dir) {
			case UP:   yVel=-5; prevDir=UP; break;
			case DOWN: yVel=5;  prevDir=DOWN; break;
			case LEFT: xVel=-5; prevDir=LEFT; break;
			case RIGHT:xVel=5;  prevDir=RIGHT; break;
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

	public BufferedImage getImage() {
		if (xVel==0 && yVel==0) return walk[prevDir][0];
		if (prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public int[] getPos() {
		xPos+=xVel;
		yPos+=yVel;
		return new int[] {xPos, yPos};
	}
	

}
