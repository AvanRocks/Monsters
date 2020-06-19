import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

class GameOverPanel extends JPanel {
  CardLayout cards;
  Container pane;
  GamePanel game;
  JButton restartBtn;
  JPanel btnPanel;
  JLabel levelTxt;

	GameOverPanel(CardLayout cards, GamePanel game, Container pane, double width, double height) {
		this.cards = cards;
    this.pane = pane;
    this.game = game;

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


		CustomImagePanel restartPanel = new CustomImagePanel("images/restart.png"
			, 250, 100);
		restartPanel.getButton().addMouseListener(
			new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					cards.show(pane, "menu");
				}
			}
		);

		btnPanel = new JPanel();
		btnPanel.setBackground(Color.black);
		btnPanel.add(restartPanel);

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
    levelTxt.setText("You reached level " + Integer.toString(game.getLevel()));
  }
}
