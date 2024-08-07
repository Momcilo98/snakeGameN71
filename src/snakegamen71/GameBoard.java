
package snakegamen71;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class GameBoard extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int score; // Dodato za praćenje skora

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = false; // Inicijalno postavljeno na false

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image background; // Dodato za pozadinu
    private JButton startButton; // Dodato dugme za pokretanje igre
    private JButton restartButton; // Dodato dugme za ponovno pokretanje igre
    private AdvancedPlayer mp3Player; // Dodato za reprodukciju muzike
    private Thread musicThread; // Nit za muziku

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();

        startButton = new JButton("Pokreni igru");
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setVisible(false);
                inGame = true;
                initGame();
                startMusic(); // Pokreni muziku
            }
        });

        restartButton = new JButton("Pokreni ponovo");
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartButton.setVisible(false);
                inGame = true;
                initGame();
                startMusic(); // Pokreni muziku
            }
        });
        restartButton.setVisible(false);

        setLayout(null);
        startButton.setBounds((B_WIDTH - 150) / 2, B_HEIGHT / 2 - 20, 150, 40); // Prošireno dugme
        restartButton.setBounds((B_WIDTH - 150) / 2, B_HEIGHT / 2 + 20, 150, 40); // Dugme za ponovno pokretanje
        add(startButton);
        add(restartButton);
    }

    private void loadImages() {
        try {
            // Postavi putanje do slika
            File headImageFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/zmijicaGlava.jpg"); // Putanja do slike glave zmijice
            File bodyImageFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/zmijicaTelo.jpg"); // Putanja do slike tela zmijice
            File appleImageFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/zmijicajabuka.jpg"); // Putanja do slike jabuke
            File backgroundImageFile = new File("C:/Users/Momcilo/Pictures/projekat zmijica/zmijicaPozadina.jpg"); // Putanja do slike pozadine

            System.out.println("Trying to load: " + headImageFile.getAbsolutePath());
            BufferedImage headImage = ImageIO.read(headImageFile);
            head = headImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

            System.out.println("Trying to load: " + bodyImageFile.getAbsolutePath());
            BufferedImage bodyImage = ImageIO.read(bodyImageFile);
            ball = bodyImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

            System.out.println("Trying to load: " + appleImageFile.getAbsolutePath());
            BufferedImage appleImage = ImageIO.read(appleImageFile);
            apple = appleImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);

            System.out.println("Trying to load: " + backgroundImageFile.getAbsolutePath());
            BufferedImage backgroundImage = ImageIO.read(backgroundImageFile);
            background = backgroundImage.getScaledInstance(B_WIDTH, B_HEIGHT, Image.SCALE_SMOOTH);

            System.out.println("Images loaded successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGame() {
        dots = 3;
        score = 0; // Inicijalizacija skora

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        System.out.println("Snake initialized at:");
        for (int z = 0; z < dots; z++) {
            System.out.println("x[" + z + "] = " + x[z] + ", y[" + z + "]");
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void startMusic() {
        try {
            FileInputStream fis = new FileInputStream("C:/Users/Momcilo/Pictures/projekat zmijica/zmijicamuzika.mp3");
            mp3Player = new AdvancedPlayer(fis);
            musicThread = new Thread(() -> {
                try {
                    mp3Player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            });
            musicThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (mp3Player != null) {
            mp3Player.close();
        }
        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        System.out.println("Drawing on the panel...");

        if (background != null) {
            g.drawImage(background, 0, 0, this);
        }

        if (inGame) {
            if (apple != null) {
                g.drawImage(apple, apple_x, apple_y, this);
                System.out.println("Drawing apple at: x = " + apple_x + ", y = " + apple_y);
            } else {
                System.out.println("Apple image is null");
            }

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    if (head != null) {
                        g.drawImage(head, x[z], y[z], this);
                        System.out.println("Drawing head at: x = " + x[z] + ", y = " + y[z]);
                    } else {
                        System.out.println("Head image is null");
                    }
                } else {
                    if (ball != null) {
                        g.drawImage(ball, x[z], y[z], this);
                        System.out.println("Drawing body part at: x = " + x[z] + ", y = " + y[z]);
                    } else {
                        System.out.println("Body part image is null");
                    }
                }
            }

            // Prikaz trenutnog skora
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", Font.PLAIN, 14));
            g.drawString("Score: " + score, 10, 20);

            Toolkit.getDefaultToolkit().sync();

        } else {
            if (!startButton.isVisible()) {
                gameOver(g);
            }
        }
    }

    private void gameOver(Graphics g) {
        stopMusic(); // Zaustavi muziku kada se igra završi

        String msg = "Game Over";
        String scoreMsg = "Score: " + score; // Poruka za prikaz skora
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 - 20);
        g.drawString(scoreMsg, (B_WIDTH - metr.stringWidth(scoreMsg)) / 2, B_HEIGHT / 2);

        restartButton.setVisible(true); // Prikaz dugmeta za ponovno pokretanje igre
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++; // Uvećavanje skora za 1
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }

        System.out.println("Snake moved to:");
        for (int z = 0; z < dots; z++) {
            System.out.println("x[" + z + "] = " + x[z] + ", y[" + z + "]");
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                System.out.println("Collision detected: x[0] = " + x[0] + ", y[0] = " + y[0] + " with x[" + z + "] = " + x[z] + ", y[" + z + "]");
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
            System.out.println("Collision with bottom boundary detected: y[0] = " + y[0]);
        }

        if (y[0] < 0) {
            inGame = false;
            System.out.println("Collision with top boundary detected: y[0] = " + y[0]);
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
            System.out.println("Collision with right boundary detected: x[0] = " + x[0]);
        }

        if (x[0] < 0) {
            inGame = false;
            System.out.println("Collision with left boundary detected: x[0] = " + x[0]);
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));

        System.out.println("Apple located at: x = " + apple_x + ", y = " + apple_y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}

