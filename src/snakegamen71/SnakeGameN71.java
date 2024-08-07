
package snakegamen71;




import javax.swing.JFrame;

public class SnakeGameN71 extends JFrame {

    public SnakeGameN71() {
        add(new GameBoard());
        setResizable(false);
        pack();

        setTitle("Snake Game");
        setSize(320, 340); // Postavi fiksne dimenzije
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame frame = new SnakeGameN71();
        frame.setVisible(true);
    }
}

