/*
Names: Avaneesh Kulkarni & Leon Si
Date: Jun 20th
Final Project: Escaping Monsters Game (using GUI)
ICS3U7-02 Ms. Strelkovska
*/

import java.awt.*;
import javax.swing.*;

class Monsters extends JFrame {
	Monsters(String name) {
		super(name);
		Dimension size = new Dimension(550, 550);
		setSize(size);

		// Using a card layout to display only one game scene at a time
		CardLayout cards = new CardLayout();
		getContentPane().setLayout(cards);

		// Creating an instance of all the game scenes
		GamePanel game = new GamePanel(cards, getContentPane());
		MenuPanel menu = new MenuPanel(
			cards,
			getContentPane(),
			game,
			(int) size.getWidth(),
			(int) size.getHeight()
		);
		JPanel rules = new RulesPanel(cards, getContentPane());
		GameOverPanel gameOver = new GameOverPanel(
			cards,
			getContentPane(),
			size.getWidth(),
			size.getHeight()
		);

		// Adding the game scenes to the card layout
		getContentPane().add("menu", menu);
		getContentPane().add("rules", rules);
		getContentPane().add("game", game);
		getContentPane().add("game-over", gameOver);
	}

	// Creating and initializing the game window
	public static void main(String[] args) {
		JFrame frame = new Monsters("Monsters");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(450, 350));
		frame.setVisible(true);
	}
}
