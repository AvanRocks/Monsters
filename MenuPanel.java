import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class MenuPanel extends JPanel implements ActionListener {
	private CardLayout cards;
	private Container pane;
	private GamePanel game;
	private JPanel playBtnPnl;
	private JButton playBtn;
	private JPanel rulesBtnPnl;
	private JButton rulesBtn;

	MenuPanel(CardLayout cards, Container pane, GamePanel game, double width, double height) {
		this.cards=cards;
		this.pane=pane;
		this.game=game;
		
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.blue);

    playBtnPnl = new JPanel();
		playBtnPnl.setBackground(Color.blue);
		playBtn = makeButton("PLAY", this);
		playBtnPnl.add(playBtn);

    rulesBtnPnl = new JPanel();
		rulesBtnPnl.setBackground(Color.blue);
		rulesBtn = makeButton("RULES", this);
		rulesBtnPnl.add(rulesBtn);

		add(Box.createVerticalGlue());
    add(playBtnPnl);
		add(Box.createVerticalGlue());
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
		else
			cards.show(pane, "rules");
	}
}
