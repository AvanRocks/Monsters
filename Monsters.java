import java.awt.*;
import javax.swing.*;

class Monsters extends JFrame {
	GamePanel game;
	
	Monsters(String name) {
		super(name);
		game = new GamePanel();
		add(game);
	}
	
	public static void main(String[] args) {
		JFrame frame = new Monsters("Monsters");
		frame.setSize(700,900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
