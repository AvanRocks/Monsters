import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CustomImagePanel extends JPanel {
  private final CustomButton button;

  public CustomButton getButton() {
    return this.button;
  }

  CustomImagePanel(String imagePath, int width, int height) {
    this.button = new CustomButton(imagePath);
    this.button.setPreferredSize(new Dimension(width, height));
    this.button.setMaximumSize(new Dimension(width, height));
    this.button.setPreferredSize(new Dimension(width, height));
    this.add(this.button);
    this.setOpaque(false);
  }
}

class CustomButton extends JPanel {

  String imagePath;

  CustomButton(String imagePath) {
    this.imagePath = imagePath;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    try {
      Image image = ImageIO.read(new File(imagePath));
      int x = (this.getWidth() - image.getWidth(null)) / 2;
      int y = (this.getHeight() - image.getHeight(null)) / 2;
      g.drawImage(image, x, y, null);
    } catch (Exception ignored) {}
  }
}
