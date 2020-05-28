import java.awt.*;
import javax.swing.*;

class GamePanel extends JPanel {
	double blockWidth;
	double blockHeight;

	private int[][] map = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
		{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
		{ 1, 1, 1, 1, 0, 0, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 3, 0, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 1, 1, 0, 0, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	GamePanel() {
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		blockWidth=(double)getWidth()/map[0].length;
		blockHeight=(double)getHeight()/map.length;
		
		g.setColor(Color.black);

		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[i].length;++j) {
				if (map[i][j]==1) {
					//System.out.println("Drawing Rect at: "+(int)(j*blockWidth)+","+(int)(i*blockHeight)+","+(int)blockWidth+","+(int)blockHeight);
					g.fillRect((int)(j*blockWidth), (int)(i*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
				}
			}
		}
	}
}
