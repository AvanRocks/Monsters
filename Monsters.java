import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Monsters extends JFrame {
  private GamePanel game;
  private MenuPanel menu;
  private JPanel rules;
  private JLabel rulesLabel;
  private CardLayout cards;
	private Dimension size = new Dimension(550,550);

  Monsters(String name) {
    super(name);
    setSize(size);

    cards = new CardLayout();
    getContentPane().setLayout(cards);

    game = new GamePanel();
    menu = new MenuPanel(cards, getContentPane(), game, size.getWidth(), size.getHeight());
    rules = new RulesPanel(cards, getContentPane());

    getContentPane().add("menu", menu);
    getContentPane().add("rules", rules);
    getContentPane().add("game", game);
  }

  public static void main(String[] args) {
    JFrame frame = new Monsters("Monsters");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
