
package snakegamen71;




import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageSplitter {

    public static void main(String[] args) {
        try {
            // Putanja do originalne slike
            BufferedImage originalImage = ImageIO.read(new File("C:/Users/Momcilo/Pictures/projekat zmijica/zmijica.webp"));
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // Pretpostavljamo da je slika podeljena na tri jednaka dela
            int partWidth = width / 3;

            BufferedImage dot = originalImage.getSubimage(0, 0, partWidth, height);
            BufferedImage head = originalImage.getSubimage(partWidth, 0, partWidth, height);
            BufferedImage apple = originalImage.getSubimage(2 * partWidth, 0, partWidth, height);

            // Putanje za čuvanje podeljenih slika
            ImageIO.write(dot, "png", new File("C:/Users/Momcilo/Pictures/projekat zmijica/dot.png"));
            ImageIO.write(head, "png", new File("C:/Users/Momcilo/Pictures/projekat zmijica/head.png"));
            ImageIO.write(apple, "png", new File("C:/Users/Momcilo/Pictures/projekat zmijica/apple.png"));

            System.out.println("Images split and saved successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
