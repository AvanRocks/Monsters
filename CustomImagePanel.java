import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CustomImagePanel extends JPanel {
  private final String imagePath;
  private final int width;
  private final int height;

  CustomImagePanel(String imagePath, int width, int height) {
    this.imagePath = imagePath;
    this.width = width;
    this.height = height;
    this.setOpaque(false);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(width, height);
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(width, height);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(width, height);
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
