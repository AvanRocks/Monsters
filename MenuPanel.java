import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// The Start Screen of the Monsters game
class MenuPanel extends JPanel {

	MenuPanel(
		CardLayout cards,
		Container pane,
		GamePanel game,
		int width,
		int height
	) {
		// Using a box layout for a vertical column of panels
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.blue);

		// Creating an instance of the panels (buttons & title) needed for the
		// main menu screen
		CustomImagePanel playButton = new CustomImagePanel(
			"images/play.png",
			120,
			80
		);
		CustomImagePanel rulesButton = new CustomImagePanel(
			"images/rules.png",
			120,
			80
		);
		CustomImagePanel titlePanel = new CustomImagePanel(
			"images/title.png",
			450,
			100
		);

		// Adding the mousePressed listener to the play button to start the game
		// when it is clicked
		playButton
			.getButton()
			.addMouseListener(
				new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						game.start();
						cards.show(pane, "game");
						game.requestFocusInWindow();
					}
				}
			);

		// Adding a mouseListener to the rules button to display the rules screen
		// when the button is pressed
		rulesButton
			.getButton()
			.addMouseListener(
				new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						cards.show(pane, "rules");
					}
				}
			);

		// Adding all the buttons/title to the main menu panel
		add(Box.createVerticalGlue());
		add(titlePanel);
		add(Box.createVerticalGlue());
		add(playButton);
		add(Box.createVerticalGlue());
		add(rulesButton);
		add(Box.createVerticalGlue());
	}
}
