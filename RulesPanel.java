import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class RulesPanel extends JPanel {
  CardLayout cards;
  Container pane;
  BackBtnPanel backBtn;
  JTextArea rulesTextArea;

  RulesPanel(CardLayout cards, Container pane) {
    this.cards = cards;
    this.pane = pane;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.blue);

    backBtn = new BackBtnPanel();
    backBtn.setBounds(10, 10, 80, 40);
    backBtn.addMouseListener(
      new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
          cards.show(pane, "menu");
        }
      }
    );

    String rulesText = "";
    try {
      rulesText = String.join("\n", Files.readAllLines(Paths.get("rules.txt")));
    } catch (Exception ignored) {}

    rulesTextArea = new JTextArea(rulesText);
    rulesTextArea.setLineWrap(true);
    rulesTextArea.setEditable(false);
    rulesTextArea.setBounds(0, 0, getWidth(), getHeight());

    rulesTextArea.setBackground(Color.blue);
    rulesTextArea.setForeground(Color.white);
    rulesTextArea.setFont(new Font("Lucida Console", Font.PLAIN, 20));
    rulesTextArea.setWrapStyleWord(true);
    rulesTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    add(backBtn);
    add(rulesTextArea);
  }
}

class BackBtnPanel extends JPanel {

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(450, 100);
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(450, 100);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(450, 100);
  }

  BackBtnPanel() {
    this.setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    try {
      Image image = ImageIO.read(new File("images/back.png"));
      int x = (this.getWidth() - image.getWidth(null)) / 2;
      int y = (this.getHeight() - image.getHeight(null)) / 2;
      g.drawImage(image, x, y, null);
    } catch (Exception ignored) {}
  }
}
