import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// The screen containing a description and the rules of the game

class RulesPanel extends JPanel {

	RulesPanel(CardLayout cards, Container pane) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.blue);

		String rulesText = "";
		try {
			rulesText = String.join("\n", Files.readAllLines(Paths.get("rules.txt")));
		} catch (Exception ignored) {}

		JTextArea rulesTextArea;
		rulesTextArea = new JTextArea(rulesText);
		rulesTextArea.setLineWrap(true);
		rulesTextArea.setEditable(false);
		rulesTextArea.setBounds(0, 0, getWidth(), getHeight());

		rulesTextArea.setBackground(Color.blue);
		rulesTextArea.setForeground(Color.white);
		rulesTextArea.setFont(new Font("Lucida Console", Font.PLAIN, 20));
		rulesTextArea.setWrapStyleWord(true);
		rulesTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

		CustomImagePanel backBtn = new CustomImagePanel("images/back.png", 120, 80);
		backBtn.setBounds(10, 10, 80, 40);
		backBtn
			.getButton()
			.addMouseListener(
				new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						cards.show(pane, "menu");
					}
				}
			);

		add(backBtn);
		add(rulesTextArea);
	}
}
