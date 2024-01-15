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

public class HitMeTest extends JPanel {
    public static int selectedTimeInMinutes = 3;
    public static int selectedMode = 0;
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
    private static int movementSpeed; // Add this variable for movement speed
//    private int selectedTimeInMinutes = 1;
    private int boxPositionX;
    private int boxPositionY;
    private int hitCount;
    private int missCount;
    private JLabel hitMissLabel;
    private JLabel timerLabel;
    private int selectedTimeInSeconds = 1;
    private Component textField;
    private boolean isPaused = false;
    private static JFrame frame;
    public HitMeTest(int selectedTimeInMinutes,int selectedMode) {
        this.selectedTimeInSeconds = selectedTimeInMinutes * 60;
//        this.movementSpeed = 1000;
        setBackground(Color.PINK);
        setLayout(null);
        frame = new JFrame("HIT ME");
    	ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("1.png"));
    	frame.setIconImage(logoIcon.getImage());
	    ImageIcon backgroundIcon = new ImageIcon("resources/maingame_bg.gif"); // Replace with the path to your background GIF image
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
                // Draw the GIF image
                ImageIcon imageIcon = new ImageIcon("resources/1.png"); // Replace with the path to your GIF image
                Image image = imageIcon.getImage();
                int imageWidth = BOX_SIZE * 4; // Specify the desired width for the image
                int imageHeight = BOX_SIZE * 4; // Specify the desired height for the image
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background GIF image
        ImageIcon backgroundIcon = new ImageIcon("resources/about_bg.gif"); // Replace with the path to your background GIF image
        Image background = backgroundIcon.getImage();
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // Draw the GIF image of the box
        ImageIcon imageIcon = new ImageIcon("9.gif"); // Replace with the path to your GIF image
        Image image = imageIcon.getImage();
        int imageWidth = BOX_SIZE * 4; // Specify the desired width for the image
        int imageHeight = BOX_SIZE * 4; // Specify the desired height for the image
        g.drawImage(image, boxPositionX, boxPositionY, imageWidth, imageHeight, this);
    }

    private void updateHitMissLabel() {
        hitMissLabel.setText("Hits: " + hitCount + "   Misses: " + missCount);
    }

    public void startGame() {
        // Start game thread
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
                // Game over, show the final score or take appropriate action
                // For now, we'll just display a message in the console
                System.out.println("Game Over");
            }
        }

        // Use selectedTimeInSeconds for the game duration
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
//        add(resumeBtn);

        JButton pauseButton = new JButton("");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        JButton homeButton = new JButton();
        homeButton.setBackground(Color.WHITE);
        homeButton.setIcon(new ImageIcon("resources/home.png"));
        homeButton.setBounds(692, 324, 70, 59);
        add(homeButton);
        
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	HitMeCTA2 hitmeinstance = new HitMeCTA2();
            	
            	hitmeinstance.showCard(HitMeCTA2.HOME_CARD);
            	frame.dispose();
            	
            }
        });
        textField = new JTextField();
        textField.setBounds(640, 63, 154, 392);
        add(textField);
        textField.setBackground(Color.BLACK);
        ((JTextField) textField).setColumns(10);
    }

    public static void main(String[] args) {

        final HitMeTest gamePanel = new HitMeTest(selectedTimeInMinutes,selectedMode);
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
                        Thread.sleep(selectedMode);
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
