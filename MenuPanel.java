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
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.blue);

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

    playButton.getButton().addMouseListener(
      new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
          game.start();
          cards.show(pane, "game");
          game.requestFocusInWindow();
        }
      }
    );

    rulesButton.getButton().addMouseListener(
      new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
          cards.show(pane, "rules");
        }
      }
    );

    add(Box.createVerticalGlue());
    add(titlePanel);
    add(Box.createVerticalGlue());
    add(playButton);
    add(Box.createVerticalGlue());
    add(rulesButton);
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
}
