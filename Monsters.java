import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Monsters extends JFrame {
  private GamePanel game;
  private MenuPanel menu;
  private JPanel rules;
  private JLabel rulesLabel;
  private CardLayout cards;

  Monsters(String name) {
    super(name);
    setSize(new Dimension(700,900));

    cards = new CardLayout();
    getContentPane().setLayout(cards);

    game = new GamePanel();
    menu = new MenuPanel(cards, getContentPane(), game);
    rules = new RulesPanel(cards, getContentPane());

    //setFocusable(true);
    //addKeyListener(game);
    
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
