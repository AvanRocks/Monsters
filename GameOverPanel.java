import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

class GameOverPanel extends JPanel implements ActionListener {
  CardLayout cards;
  Container pane;
  GamePanel game;
  JButton restartBtn;
  JPanel btnPanel;
  JLabel levelTxt;

  GameOverPanel(CardLayout cards, GamePanel game, Container pane, double width, double height) {
    this.cards = cards;
    this.game=game;
    this.pane = pane;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.black);

    // Game Over img
    Icon gameOverImg = new ImageIcon(
      getClass().getResource("images" + File.separator + "gameOver.gif")
    );
    JLabel imgLabel = new JLabel(gameOverImg);
    JPanel imgPanel = new JPanel();
    imgPanel.setBackground(Color.black);
    imgPanel.add(imgLabel);

    // level text
    JPanel levelPnl = new JPanel();
    levelTxt = new JLabel();
    levelTxt.setFont(new Font(levelTxt.getFont().getName(), Font.BOLD, 30));
    levelTxt.setForeground(Color.green);
    levelPnl.add(levelTxt);
    levelPnl.setBackground(Color.black);

    // restart button
    restartBtn = MenuPanel.makeButton("Restart", this);
    restartBtn.setBackground(Color.blue);
    btnPanel = new JPanel();
    btnPanel.setBackground(Color.black);
    btnPanel.add(restartBtn);

    add(Box.createVerticalGlue());
    add(imgPanel);
    add(Box.createVerticalGlue());
    add(levelPnl);
    add(Box.createVerticalGlue());
    add(btnPanel);
    add(Box.createVerticalGlue());
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    restartBtn.setFont(
      new Font(
        "Dialog",
        Font.PLAIN,
        Math.max(getWidth() / 40, getHeight() / 25)
      )
    );
    levelTxt.setText("You reached level " + Integer.toString(game.getLevel()));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    cards.show(pane, "menu");
  }
}
