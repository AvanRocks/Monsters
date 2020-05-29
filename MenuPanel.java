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

	MenuPanel(CardLayout cards, Container pane, GamePanel game) {
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

        add(Box.createRigidArea(new Dimension(500,250)));
        add(playBtnPnl);
        add(Box.createRigidArea(new Dimension(500,100)));
        add(rulesBtnPnl);
        add(Box.createRigidArea(new Dimension(500,250)));
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


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("PLAY")) {
			game.start();
			cards.show(pane, "game");
		}
		else
			cards.show(pane, "rules");
	}
}
