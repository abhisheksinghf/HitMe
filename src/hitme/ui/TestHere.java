package hitme.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TestHere extends JPanel {
    public static int selectedTimeInMinutes = 3;
    public static int selectedMode = 2; // Default to Medium

    private static final int FRAME_WIDTH = 1000; // in pixels
    private static final int FRAME_HEIGHT = 700; // in pixels
    private static final int BOX_SIZE = 40; // in pixels
    private static final int PANEL_WIDTH = FRAME_WIDTH; // in pixels
    private static final int PANEL_HEIGHT = FRAME_HEIGHT - 50; // in pixels
    private static final int HIT_MISS_LABEL_WIDTH = 150; // in pixels
    private static final int HIT_MISS_LABEL_HEIGHT = 30; // in pixels
    private static final int TOP_PANEL_HEIGHT = 50; // in pixels
    private static final int RIGHT_PANEL_WIDTH = 150; // in pixels
    private static final int MAX_BOX_POSITION_X = PANEL_WIDTH - BOX_SIZE * 15;
    private static final int MAX_BOX_POSITION_Y = PANEL_HEIGHT - BOX_SIZE * 4 - 180;
    private static final Color BOX_BORDER_COLOR = Color.BLACK;
    private static final Random RANDOM = new Random();
    private int objectX;
    private int movementSpeed; 

    private int boxPositionX;
    private int boxPositionY;
    private int hitCount;
    private int missCount;
    private JLabel hitMissLabel;
    private JLabel timerLabel;
    private int selectedTimeInSeconds = 1;
    private Component textField;
    private boolean isPaused = false;

    public TestHere(int selectedTimeInMinutes, int selectedMode) {
        this.selectedTimeInSeconds = selectedTimeInMinutes * 60;

        setBackground(Color.PINK);
        setLayout(null);

        ImageIcon backgroundIcon = new ImageIcon("resources/maingame_bg.gif");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Top Panel
        JPanel topPanel = new JPanel(null);
        topPanel.setBounds(0, 0, PANEL_WIDTH, TOP_PANEL_HEIGHT);
        topPanel.setBackground(Color.BLACK);

        JLabel hitMeLabel = new JLabel("HITME");
        hitMeLabel.setBounds(10, 0, 100, TOP_PANEL_HEIGHT);
        hitMeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        hitMeLabel.setForeground(Color.WHITE);
        topPanel.add(hitMeLabel);

        timerLabel = new JLabel("00:00");
        timerLabel.setBounds(377, 0, 100, TOP_PANEL_HEIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.WHITE);
        topPanel.add(timerLabel);

        hitMissLabel = new JLabel("Hits: 0   Misses: 0");
        hitMissLabel.setBounds(650, 13, HIT_MISS_LABEL_WIDTH, HIT_MISS_LABEL_HEIGHT);
        hitMissLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        hitMissLabel.setForeground(Color.WHITE);
        topPanel.add(hitMissLabel);

        add(topPanel);

        // Game Panel
        final JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("resources/1.png"); // Replace with the path to your GIF image
                Image image = imageIcon.getImage();
                int imageWidth = BOX_SIZE * 4;
                int imageHeight = BOX_SIZE * 4;
                g.drawImage(image, boxPositionX, boxPositionY, imageWidth, imageHeight, this);
            }
        };
        gamePanel.setBounds(12, 63, 570, 400);
        gamePanel.setBackground(Color.YELLOW);
        gamePanel.add(backgroundLabel);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isPaused) {
                    Point point = e.getPoint();
                    if (point.x >= boxPositionX && point.x <= boxPositionX + BOX_SIZE * 4
                            && point.y >= boxPositionY && point.y <= boxPositionY + BOX_SIZE * 4) {
                        hitCount++;
                    } else {
                        missCount++;
                    }
                    updateHitMissLabel();
                    gamePanel.repaint();
                }
            }
        });

        add(gamePanel);

        // ... (Rest of the constructor code)

        this.movementSpeed = calculateMovementSpeed(selectedMode);
    }

    private int calculateMovementSpeed(int selectedMode) {
        switch (selectedMode) {
            case 1: // Easy
                return 1;
            case 2: // Medium
                return 2;
            case 3: // Hard
                return 3;
            default:
                return 1; // Default to Easy
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundIcon = new ImageIcon("resources/about_bg.gif"); // Replace with the path to your background GIF image
        Image background = backgroundIcon.getImage();
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        ImageIcon imageIcon = new ImageIcon("9.gif"); // Replace with the path to your GIF image
        Image image = imageIcon.getImage();
        int imageWidth = BOX_SIZE * 4;
        int imageHeight = BOX_SIZE * 4;
        g.drawImage(image, boxPositionX, boxPositionY, imageWidth, imageHeight, this);
    }

    private void updateHitMissLabel() {
        hitMissLabel.setText("Hits: " + hitCount + "   Misses: " + missCount);
    }

    public void startGame() {
        class GameRunnable implements Runnable {
            private final JLabel timerLabel;
            private int timeLeft;

            public GameRunnable(JLabel timerLabel, int timeLeft) {
                this.timerLabel = timerLabel;
                this.timeLeft = timeLeft;
            }

            @Override
            public void run() {
                while (timeLeft >= 0) {
                    if (!isPaused) {
                        timerLabel.setText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));
                        timeLeft--;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Game Over");
            }
        }

        Thread gameThread = new Thread(new GameRunnable(timerLabel, selectedTimeInSeconds));
        gameThread.start();

        JButton playButton = new JButton("");
        playButton.setBackground(Color.BLACK);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                isPaused = !isPaused;
            }
        });
        playButton.setVerticalAlignment(SwingConstants.TOP);
        playButton.setIcon(new ImageIcon("resources/pause1.png"));
        playButton.setBounds(692, 122, 70, 67);
        add(playButton);

        JButton resumeBtn = new JButton("");
        resumeBtn.setBackground(Color.BLACK);
        resumeBtn.setIcon(new ImageIcon("resources/retry.png"));
        resumeBtn.setBounds(692, 220, 70, 59);
        add(resumeBtn);

        JButton pauseButton = new JButton("");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        pauseButton.setBackground(Color.WHITE);
        pauseButton.setIcon(new ImageIcon("resources/home.png"));
        pauseButton.setBounds(692, 324, 70, 59);
        add(pauseButton);

        textField = new JTextField();
        textField.setBounds(640, 63, 154, 393);
        add(textField);
        textField.setBackground(Color.BLACK);
        ((JTextField) textField).setColumns(10);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HIT ME");

        final TestHere gamePanel = new TestHere(selectedTimeInMinutes, selectedMode);
        gamePanel.setBounds(0, 0, PANEL_WIDTH - RIGHT_PANEL_WIDTH, PANEL_HEIGHT);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(gamePanel);
        frame.setSize(850, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!gamePanel.isPaused) {
                        gamePanel.boxPositionX = RANDOM.nextInt(MAX_BOX_POSITION_X + 1);
                        gamePanel.boxPositionY = RANDOM.nextInt(MAX_BOX_POSITION_Y + 1);
                        gamePanel.repaint();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        gameThread.start();

        gamePanel.startGame();
    }
}
