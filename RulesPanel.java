import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class RulesPanel extends JPanel implements ActionListener {
    CardLayout cards;
    Container pane;
	JButton backBtn;
	JLabel rulesLbl;
	JPanel rulesPnl;

    RulesPanel(CardLayout cards, Container pane) {
        this.cards=cards;
        this.pane=pane;

		setLayout(null);
		setBackground(Color.blue);

		backBtn = MenuPanel.makeButton("Back", this);
		backBtn.setBounds(10,10,80,40);

		rulesLbl = new JLabel("These are the rules. These are the rules. These are the rules. These are the rules. " +
		"These are the rules. These are the rules. These are the rules. These are the rules. These are the rules. " +
		"These are the rules. These are the rules. These are the rules. These are the rules. These are the rules. " +
		"These are the rules. These are the rules. These are the rules. These are the rules. These are the rules. ");
		rulesPnl = new JPanel();
		rulesPnl.setBounds(0,400,700,900);
		rulesPnl.add(rulesLbl);

		add(backBtn);
		add(rulesPnl);
	}

    public void actionPerformed(ActionEvent e) {
		cards.show(pane, "menu");
    }
}

