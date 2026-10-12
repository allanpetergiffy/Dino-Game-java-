import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DinoGame extends JPanel implements ActionListener, KeyListener {
    private final int BOARD_WIDTH = 800;
    private final int BOARD_HEIGHT = 300;
    
    // Dino properties
    private int dinoX = 50;
    private int dinoY = 200;
    private int dinoWidth = 30;
    private int dinoHeight = 40;
    private int velocityY = 0;
    private int gravity = 1;
    private boolean isJumping = false;
    
    // Cactus properties
    private int cactusX = 800;
    private int cactusY = 200;
    private int cactusWidth = 20;
    private int cactusHeight = 40;
    private int cactusSpeed = 6;
    
    private Timer timer;
    private int score = 0;
    private boolean gameOver = false;

    public DinoGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        
        // Game loop timer (~50 FPS)
        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw Ground Line
        g.setColor(Color.BLACK);
        g.drawLine(0, 240, BOARD_WIDTH, 240);

        // Draw Dino
        g.setColor(new int[]{80, 80, 80}[0] == 80 ? Color.DARK_GRAY : Color.DARK_GRAY);
        g.fillRect(dinoX, dinoY, dinoWidth, dinoHeight);

        // Draw Cactus Obstacle
        g.setColor(new Color(46, 139, 87)); // Sea Green
        g.fillRect(cactusX, cactusY, cactusWidth, cactusHeight);

        // Draw Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, BOARD_WIDTH - 120, 30);

        // Game Over Screen
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("Game Over! Press 'R' to Restart", BOARD_WIDTH / 2 - 180, BOARD_HEIGHT / 2);
        }
    }

    public void move() {
        if (gameOver) return;

        // Apply gravity & handle jump
        velocityY += gravity;
        dinoY += velocityY;
        if (dinoY > 200) { // ground limit
            dinoY = 200;
            velocityY = 0;
            isJumping = false;
        }

        // Move Cactus
        cactusX -= cactusSpeed;
        if (cactusX < -cactusWidth) {
            cactusX = BOARD_WIDTH;
            score += 10;
            // Gradually increase difficulty
            if (cactusSpeed < 12 && score % 50 == 0) {
                cactusSpeed++;
            }
        }

        // Collision Detection (Bounding Box)
        if (dinoX < cactusX + cactusWidth &&
            dinoX + dinoWidth > cactusX &&
            dinoY < cactusY + cactusHeight &&
            dinoY + dinoHeight > cactusY) {
            gameOver = true;
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!isJumping && !gameOver) {
                velocityY = -15; // Jump strength
                isJumping = true;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            // Reset Game States
            dinoY = 200;
            velocityY = 0;
            isJumping = false;
            cactusX = BOARD_WIDTH;
            score = 0;
            cactusSpeed = 6;
            gameOver = false;
            timer.start();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Dino Game");
        DinoGame game = new DinoGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}