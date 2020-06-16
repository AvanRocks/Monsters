import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class GameOverPanel extends JPanel implements ActionListener {
  CardLayout cards;
  Container pane;
	JButton restartBtn;
	JPanel btnPanel;

  GameOverPanel(CardLayout cards, Container pane, double width, double height) {
		this.cards=cards;
    this.pane=pane;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.black);

		// Game Over img
		Icon gameOverImg = new ImageIcon(getClass().getResource("images" + File.separator + "gameOver.gif"));
		JLabel imgLabel = new JLabel(gameOverImg);
		JPanel imgPanel = new JPanel();
		imgPanel.setBackground(Color.black);
		imgPanel.add(imgLabel);

		// restart button
		restartBtn = MenuPanel.makeButton("Restart", this);
		restartBtn.setBackground(Color.blue);
		btnPanel = new JPanel();
		btnPanel.setBackground(Color.black);
		btnPanel.add(restartBtn);

		add(Box.createVerticalGlue());
		add(imgPanel);
		add(Box.createVerticalGlue());
		add(btnPanel);
		add(Box.createVerticalGlue());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		restartBtn.setFont(new Font("Dialog", Font.PLAIN, Math.max(getWidth()/40, getHeight()/25)));
	}

	@Override
  public void actionPerformed(ActionEvent e) {
		cards.show(pane, "menu");
  }
}

