import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

class MenuPanel extends JPanel implements ActionListener {
	private final CardLayout cards;
	private final Container pane;
	private final GamePanel game;

	MenuPanel(CardLayout cards, Container pane, GamePanel game, double width, double height) {
		this.cards = cards;
		this.pane = pane;
		this.game = game;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.blue);

		JPanel playBtnPnl = new PlayButton();
		JPanel rulesBtnPnl = new RulesButton();
		TitlePanel titlePanel = new TitlePanel();

		add(Box.createVerticalGlue());
		add(titlePanel);
    add(playBtnPnl);
    add(rulesBtnPnl);
		add(Box.createVerticalGlue());
	}

	public static JButton makeButton(String text, ActionListener listener) {
		JButton btn = new JButton(text);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.setBackground(Color.black);
		btn.setForeground(Color.white);
		btn.setOpaque(true);
		btn.addActionListener(listener);

		return btn;
	}

	@Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    playBtn.setFont(new Font("Dialog", Font.PLAIN, Math.max(getWidth()/40, getHeight()/25)));
    rulesBtn.setFont(new Font("Dialog", Font.PLAIN, Math.max(getWidth()/40, getHeight()/25)));
  }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("PLAY")) {
			game.start();
			cards.show(pane, "game");
			game.requestFocusInWindow();
		}
		else {
			cards.show(pane, "rules");
		}
	}
}

class TitlePanel extends JPanel {
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(800, 400);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(800, 400);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 400);
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			Image image = ImageIO.read(new File("images/title.png"));
			int x = (this.getWidth() - image.getWidth(null)) / 2;
			int y = (this.getHeight() - image.getHeight(null)) / 2;
			g.drawImage(image, x, y, null);
		} catch (Exception ignored) {}
	}
}

class PlayButton extends JPanel {
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(400, 200);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(400, 200);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			Image image = ImageIO.read(new File("images/play.png"));
			int x = (this.getWidth() - image.getWidth(null)) / 2;
			int y = (this.getHeight() - image.getHeight(null)) / 2;
			g.drawImage(image, x, y, null);
		} catch (Exception ignored) {}
	}
}

class RulesButton extends JPanel {
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(400, 200);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(400, 200);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 200);
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			Image image = ImageIO.read(new File("images/rules.png"));
			int x = (this.getWidth() - image.getWidth(null)) / 2;
			int y = (this.getHeight() - image.getHeight(null)) / 2;
			g.drawImage(image, x, y, null);
		} catch (Exception ignored) {}
	}
}
