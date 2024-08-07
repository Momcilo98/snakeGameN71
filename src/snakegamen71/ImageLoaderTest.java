
package snakegamen71;

import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageLoaderTest {

    public static void main(String[] args) {
        try {
            // Putanje do slika
            File dotFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/dot.png");
            File appleFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/apple.png");
            File headFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/head.png");

            // Učitavanje slika
            Image dot = ImageIO.read(dotFile);
            Image apple = ImageIO.read(appleFile);
            Image head = ImageIO.read(headFile);

            // Provera učitanih slika
            if (dot != null) {
                System.out.println("Dot image loaded successfully with dimensions: " + dot.getWidth(null) + "x" + dot.getHeight(null));
            } else {
                System.out.println("Dot image not loaded");
            }

            if (apple != null) {
                System.out.println("Apple image loaded successfully with dimensions: " + apple.getWidth(null) + "x" + apple.getHeight(null));
            } else {
                System.out.println("Apple image not loaded");
            }

            if (head != null) {
                System.out.println("Head image loaded successfully with dimensions: " + head.getWidth(null) + "x" + head.getHeight(null));
            } else {
                System.out.println("Head image not loaded");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


