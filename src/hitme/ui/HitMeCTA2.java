package hitme.ui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import hitme.audio.AudioPlayer;

public class HitMeCTA2 extends JFrame {

	private AudioPlayer musicClip;
	private AudioPlayer soundClip;
	private JTextField nameTextField;
	private JPanel cards;
	private final static String LOADING_CARD = "LoadingCard";
	public  final static String HOME_CARD = "HomeCard";
	private final static String ABOUT_CARD = "AboutCard";
	private final static String HELP_CARD = "HelpCard";
	private final static String DETAILS_CARD = "DetailsCard";
	private final static String MODE_TIMER_DETAILS_CARD = "ModeTimerCard";
	private final static String GAME_MODES_CARD = "GameModesCard";
	private final static String GAME_CARD = "GameCard"; // New game card
	private final static String SETTINGS_CARD = "SettingsCard";

	private int selectedTimeInSeconds;
	private int selectedMinutes;
	private int movementSpeed; // Add this variable to store the movement speed


	public HitMeCTA2() {
		cards = new JPanel(new CardLayout());
		createLoadingScreen();
		createHomePage();
		createAboutPage();
		ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("1.png"));
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("HIT ME");
		setIconImage(logoIcon.getImage());
		setContentPane(cards);
		showCard(LOADING_CARD);

		playMusic("resources/bgmusic.wav");

		Timer timer = new Timer(6200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard(HOME_CARD);
			}
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
	}

	private void createLoadingScreen() {
		JPanel loadingPanel = new JPanel(new GridBagLayout());
		loadingPanel.setBackground(Color.BLACK);

		JLabel loadingLabel = new JLabel();
		loadingLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loading.gif"))); // Replace with the path to your loading gif
//		loadingLabel.setIcon(getClass().getClassLoader().getResource(arg0));
		loadingPanel.add(loadingLabel);

		cards.add(loadingPanel, LOADING_CARD);
	}

	private void createHomePage() {
		// Create the mainHomePanel with a BorderLayout
		JPanel mainHomePanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("bg.gif"));
				Image image = imageIcon.getImage();
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		};

		JButton startButton = createButton("START");
		JButton aboutButton = createButton("ABOUT");
		JButton helpButton = createButton("HELP");
		JButton exitButton = createButton("EXIT");


		Font buttonFont = new Font("Minecraftia", Font.BOLD, 14);
		startButton.setFont(loadFont("resources/ARCADE_N.TTF", 12));
		aboutButton.setFont(loadFont("resources/ARCADE_N.TTF", 12));
		helpButton.setFont(loadFont("resources/ARCADE_N.TTF", 12));
		exitButton.setFont(loadFont("resources/ARCADE_N.TTF", 12));

		// Create the settingsButton and its panel, then add it to the NORTH of mainHomePanel
		JButton settingsButton = createButton("");
		settingsButton.setBackground(Color.black);
		ImageIcon settingsIcon = new ImageIcon(getClass().getClassLoader().getResource("settings2.gif")); // Replace with the path to your icon
		settingsButton.setIcon(settingsIcon);
		settingsButton.setFont(loadFont("resources/ARCADE_N.TTF", 8));
		JPanel settingsPanel = new JPanel(new BorderLayout());
		settingsPanel.setOpaque(false);
		settingsPanel.add(settingsButton, BorderLayout.EAST);
		mainHomePanel.add(settingsPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBackground(new Color(0, 0, 0, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);

		JLabel imageLabel = createImageLabel("text.gif");

//		ImageIcon imageLabel = new ImageIcon(getClass().getClassLoader().getResource("text.gif")); // Replace with the path to your icon
		buttonPanel.add(imageLabel, gbc);

		gbc.gridy++;
		buttonPanel.add(startButton, gbc);
		gbc.gridy++;
		buttonPanel.add(aboutButton, gbc);
		gbc.gridy++;
		buttonPanel.add(helpButton, gbc);
		gbc.gridy++;
		buttonPanel.add(exitButton, gbc);

		mainHomePanel.add(buttonPanel, BorderLayout.CENTER);

		cards.add(mainHomePanel, HOME_CARD);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				createDetailsPage();
				showCard(DETAILS_CARD);
			}
		});

		settingsButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        playSound("resources/button_click.wav");
		        createSettingsPage();
		        showCard(SETTINGS_CARD);
//		    	System.out.println("hii");
		    }
		});

		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(ABOUT_CARD);
			}
		});

		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				createHelpPage(); // Create the "Help" page dynamically
				showCard(HELP_CARD);
			}
		});

		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				exitGame();
			}
		});
	}
	
	private Font loadFont(String fontPath, int size) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
			return font.deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			// If the font loading fails, use a fallback font
			return new Font("Arial", Font.PLAIN, size);
		}
	}

	private void createAboutPage() {

		JPanel aboutPanel = new JPanel(new CardLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
				Image bgImage = bgIcon.getImage();
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

		Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 12);

		JLabel aboutLabel = new JLabel("<html><center><h1><font color='green'>ABOUT</font></h1></center></html>");
		aboutLabel.setFont(arcadeFont);
		aboutLabel.setForeground(Color.WHITE); // Set the text color to white

		JLabel developedByLabel = new JLabel("<html><center><h2><font color='white'>Developed by Team QUIVER</font></h2></center></html>");
		developedByLabel.setFont(arcadeFont);
		developedByLabel.setForeground(Color.WHITE); // Set the text color to white

		JLabel developersLabel = new JLabel("<html><center><h2><font color='green'>--DEVELOPERS--</font></h2><br><font color='green'>Abhisheksingh</font><br><font color='green'>Mallikarjun</font><br><font color='green'>Shridhari</font><br><font color='green'>Sahitya</font></center></html>");
		developersLabel.setFont(arcadeFont);
		developersLabel.setForeground(Color.GREEN); // Set the text color to green

		JLabel enjoyLabel = new JLabel("<html><center><h2><font color='white'>Enjoy the Game!!</font></h2></center></html>");
		enjoyLabel.setFont(arcadeFont);
		enjoyLabel.setForeground(Color.WHITE); // Set the text color to white

		JButton backButton = createButton("Back");
		backButton.setFont(arcadeFont);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPanel.add(aboutLabel, gbc);

		gbc.gridy++;
		contentPanel.add(developedByLabel, gbc);

		gbc.gridy++;
		contentPanel.add(developersLabel, gbc);

		gbc.gridy++;
		contentPanel.add(enjoyLabel, gbc);

		gbc.gridy++;
		contentPanel.add(backButton, gbc);

		contentPanel.setOpaque(false); // Make the panel transparent
		aboutPanel.add(contentPanel);

		cards.add(aboutPanel, ABOUT_CARD);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(HOME_CARD);
			}
		});
	}

	private void createHelpPage() {
		JPanel helpPanel = new JPanel(new CardLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
				Image bgImage = bgIcon.getImage();
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

		Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 12);

		JLabel helpLabel = new JLabel("<html><center><h1><font color='green'>HELP</font></h1></center></html>");
		helpLabel.setFont(arcadeFont);
		helpLabel.setForeground(Color.WHITE); // Set the text color to white

		JLabel instructionsLabel = new JLabel("<html><center><h2><font color='white'>How to Play:</font></h2><p>Click on the boxes when they appear to score hits.</p><p>Do not click on the empty space or you'll get a miss!</p><p>You'll earn points for every successful hit.</p><p>Be quick and accurate to achieve high scores!</p><br><p>Have fun playing!</p></center></html>");
		instructionsLabel.setFont(arcadeFont);
		instructionsLabel.setForeground(Color.WHITE); // Set the text color to white

		JButton backButton = createButton("Back");
		backButton.setFont(arcadeFont);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPanel.add(helpLabel, gbc);

		gbc.gridy++;
		contentPanel.add(instructionsLabel, gbc);

		gbc.gridy++;
		contentPanel.add(backButton, gbc);

		contentPanel.setOpaque(false); // Make the panel transparent
		helpPanel.add(contentPanel);

		cards.add(helpPanel, HELP_CARD);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(HOME_CARD);
			}
		});
	}
	private void createDetailsPage() {
		JPanel detailsPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
				Image bgImage = bgIcon.getImage();
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

		Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 12);

		JLabel titleLabel = new JLabel("<html><center><h1><font color='green'>ENTER DETAILS</font></h1></center></html>");
		titleLabel.setFont(arcadeFont);
		titleLabel.setForeground(Color.WHITE); // Set the text color to white

		JLabel nameLabel = new JLabel("<html><h2><font color='white'>Enter Player Name :</font></h2></html>");
		nameLabel.setFont(arcadeFont);
		nameLabel.setForeground(Color.WHITE); // Set the text color to white

		nameTextField = new JTextField(20);

		JButton backButton = createButton("Back");
		backButton.setFont(arcadeFont);

		JButton nextButton = createButton("Next");
		nextButton.setFont(arcadeFont);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPanel.add(titleLabel, gbc);

		gbc.gridy++;
		contentPanel.add(nameLabel, gbc);	

		gbc.gridy++;
		contentPanel.add(nameTextField, gbc);

		gbc.gridy++;
		contentPanel.add(nextButton, gbc);

		gbc.gridy++;
		contentPanel.add(backButton, gbc);

		contentPanel.setOpaque(false); // Make the panel transparent
		detailsPanel.add(contentPanel);

		cards.add(detailsPanel, DETAILS_CARD);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(HOME_CARD);
			}
		});
//
//		nextButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				playSound("resources/button_click.wav");
//				System.out.println("jdsd");
//				// Create the HitMeTest game card
//				gameModes();
//				showCard(GAME_MODES_CARD); // Show the game card
//
//
//			}
//		});

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("resources/button_click.wav");

                String username = nameTextField.getText();
                playSound("resources/button_click.wav");
                // You can get the entered name using nameTextField.getText()
                // Add your logic here to handle the name input and proceed to the next page
                // For now, we will show the "About" page as an example
                 createModeTimerDetails();
                showCard(MODE_TIMER_DETAILS_CARD);
               gameModes();
               showCard(GAME_MODES_CARD);
                if (storeUserDetails(username)) {
                    JOptionPane.showMessageDialog(null, "User details stored successfully");
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to store user details");
                }
            }
        });
    }

    private boolean storeUserDetails(String username) {
    	return true;
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hitme", "root", "");
//
//            PreparedStatement statement = connection.prepareStatement("INSERT INTO account (name) VALUES (?)");
//            statement.setString(1, username);
//           // statement.setString(2, password); // Remember to hash and salt password in a real-world application
//
//            int rowsAffected = statement.executeUpdate();
//
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
    
	//--------------------end of the connection  -------------------->    
	}
    private void createGameCard(int selectedTimeInSeconds, int movementSpeed) {
        JPanel gamePanel = new JPanel(new BorderLayout());

        JButton backButton = createButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard(HOME_CARD);
            }
        });
        
        // Update the selected time and movement speed in the HitMeTest class
        HitMeTest.selectedTimeInMinutes = selectedTimeInSeconds;
        HitMeTest.selectedMode = movementSpeed;
//        HitMeTest.movementSpeed = movementSpeed;

        // Call the main method of HitMeTest
        HitMeTest.main(new String[0]);

        gamePanel.add(backButton, BorderLayout.SOUTH);

        cards.add(gamePanel, GAME_CARD);
    }
    private void createModeTimerDetails() {
		JPanel modeTimerPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
				Image bgImage = bgIcon.getImage();
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

		Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 12);

		JLabel modeLabel = new JLabel("<html><center><h1><font color='green'>SELECT MODE</font></h1></center></html>");
		modeLabel.setFont(arcadeFont);
		modeLabel.setForeground(Color.WHITE); // Set the text color to white

		JLabel timerLabel = new JLabel("<html><center><h1><font color='green'>SELECT TIMER</font></h1></center></html>");
		timerLabel.setFont(arcadeFont);
		timerLabel.setForeground(Color.WHITE); // Set the text color to white

		String[] modes = { "EASY", "MEDIUM", "HARD" };
	    JComboBox<String> modeDropdown = new JComboBox<>(modes);
	    modeDropdown.setFont(arcadeFont);
	    modeDropdown.setPreferredSize(new Dimension(150, 30));

		String[] timers = { "1", "2", "3", "4", "5" };
		JComboBox<String> timerDropdown = new JComboBox<>(timers);
		timerDropdown.setFont(arcadeFont);
		timerDropdown.setPreferredSize(new Dimension(150, 30));

		JButton backButton = createButton("Back");
		backButton.setFont(arcadeFont);

		JButton nextButton = createButton("Next");
		nextButton.setFont(arcadeFont);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPanel.add(modeLabel, gbc);

		gbc.gridy++;
		contentPanel.add(modeDropdown, gbc);

		gbc.gridy++;
		contentPanel.add(timerLabel, gbc);

		gbc.gridy++;
		contentPanel.add(timerDropdown, gbc);

		gbc.gridy++;
		contentPanel.add(nextButton, gbc);

		gbc.gridy++;
		contentPanel.add(backButton, gbc);

		contentPanel.setOpaque(false); // Make the panel transparent
		modeTimerPanel.add(contentPanel);

		cards.add(modeTimerPanel, MODE_TIMER_DETAILS_CARD);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(GAME_MODES_CARD);
			}
		});

//		nextButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				playSound("resources/button_click.wav");
//
//				// Get the selected timer value from the dropdown
//				String selectedTimer = (String) timerDropdown.getSelectedItem();
//				selectedMinutes = Integer.parseInt(selectedTimer);
//				selectedTimeInSeconds = selectedMinutes * 1;
//
//				// Create the HitMeTest game card with the selected time
//				createGameCard(selectedTimeInSeconds);
//
//				// Show the game card
//				showCard(GAME_CARD);
//			}
//		});

	    nextButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            playSound("resources/button_click.wav");

	            // Get the selected timer value from the dropdown
	            String selectedTimer = (String) timerDropdown.getSelectedItem();
	            selectedMinutes = Integer.parseInt(selectedTimer);
	            selectedTimeInSeconds = selectedMinutes;

	            // Get the selected difficulty mode
	            String selectedMode = (String) modeDropdown.getSelectedItem();

	            // Set movement speed based on the selected mode
	            if (selectedMode.equals("EASY")) {
	                movementSpeed = 1000;
	            } else if (selectedMode.equals("MEDIUM")) {
	                movementSpeed = 700;
	            } else if (selectedMode.equals("HARD")) {
	                movementSpeed = 450;
	            }

	            // Create the HitMeTest game card with the selected time and movement speed
	            createGameCard(selectedTimeInSeconds, movementSpeed);

	            // Show the game card
	            showCard(GAME_CARD);
	        }
	    });
	}
		

		private void createSettingsPage() {
	    JPanel settingsPanel = new JPanel(new GridBagLayout()) {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
	            Image bgImage = bgIcon.getImage();
	            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
	        }
	    };

	    Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 12);

	    JLabel settingsLabel = new JLabel("<html><center><h1><font color='green'>SETTINGS</font></h1></center></html>");
	    settingsLabel.setFont(arcadeFont);
	    settingsLabel.setForeground(Color.WHITE); // Set the text color to white

	    JToggleButton musicToggleButton = new JToggleButton("Music: ON");
	    musicToggleButton.setFont(arcadeFont);
	    musicToggleButton.setSelected(true); // Default state is ON
	    musicToggleButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (musicToggleButton.isSelected()) {
	                musicToggleButton.setText("Music: ON");
	                playSound("resources/bgmusic.wav");
	            } else {
	                musicToggleButton.setText("Music: OFF");
	                stopMusic();
	            }
	        }
	    });

	    JToggleButton soundToggleButton = new JToggleButton("Sound: ON");
	    soundToggleButton.setFont(arcadeFont);
	    soundToggleButton.setSelected(true); // Default state is ON
	    soundToggleButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (soundToggleButton.isSelected()) {
	                soundToggleButton.setText("Sound: ON");
	                playSound("resources/button_click.wav");
	            } else {
	                soundToggleButton.setText("Sound: OFF");
	                stopSound();
	            }
	        }
	    });

	    JButton backButton = createButton("Back");
	    backButton.setFont(arcadeFont);

	    JPanel contentPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(10, 0, 10, 0);
	    contentPanel.add(settingsLabel, gbc);

	    gbc.gridy++;
	    contentPanel.add(musicToggleButton, gbc);

//	    gbc.gridy++;
//	    contentPanel.add(soundToggleButton, gbc);

	    gbc.gridy++;
	    contentPanel.add(backButton, gbc);

	    contentPanel.setOpaque(false); // Make the panel transparent
	    settingsPanel.add(contentPanel);

	    cards.add(settingsPanel, SETTINGS_CARD);

	    backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            playSound("resources/button_click.wav");
	            showCard(HOME_CARD);
	        }
	    });
	}

	private void gameModes() {
		JPanel gameModesPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("about_bg.gif")); // Replace with the path to your background image
				Image bgImage = bgIcon.getImage();
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

		Font arcadeFont = loadFont("resources/ARCADE_N.TTF", 8);
		Font arcadeFont2 = loadFont("resources/ARCADE_N.TTF", 12);

		JLabel modeLabel = new JLabel("<html><center><h1><font color='green'>SELECT GAME MODE</font></h1></center></html>");
		modeLabel.setFont(arcadeFont);
		modeLabel.setForeground(Color.WHITE); // Set the text color to white

		JButton classicButton = createButton("Classic");

		classicButton.setFont(arcadeFont);

		JButton campaignButton = createButton("Campaign");
		campaignButton.setFont(arcadeFont);


		JButton backButton = createButton("BACK");

		backButton.setFont(arcadeFont2);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPanel.add(modeLabel, gbc);

		gbc.gridy++;
		contentPanel.add(classicButton, gbc);

//		gbc.gridy++;
//		contentPanel.add(campaignButton, gbc);

		gbc.gridy++;
		contentPanel.add(backButton, gbc);


		contentPanel.setOpaque(false); // Make the panel transparent
		gameModesPanel.add(contentPanel);

		cards.add(gameModesPanel, GAME_MODES_CARD);

		classicButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				createModeTimerDetails();
				showCard(MODE_TIMER_DETAILS_CARD);
			}
		});

		campaignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				// Add your logic here to handle the "Campaign" game mode and proceed accordingly
				// For now, we will show the "About" page as an example
				//            showCard(ABOUT_CARD);
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("resources/button_click.wav");
				showCard(DETAILS_CARD);
			}
		});

	}

	public void showCard(String cardName) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, cardName);
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(Color.WHITE);
		button.setPreferredSize(new Dimension(100, 30));
		return button;
	}

	private JLabel createImageLabel(String imagePath) {
	    JLabel label = new JLabel();
//	    System.out.println("Trying to load image from path: " + imagePath);
	    ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
	    label.setIcon(icon);
	    return label;
	}

	private void playSound(String filePath) {
		AudioPlayer soundPlayer = new AudioPlayer(filePath);
		soundClip = soundPlayer; 
		soundPlayer.play();
	}
	private void stopSound() {
		if (soundClip != null) {
			soundClip.stop();
			soundClip.close();
		}
	}
	private void playMusic(String filePath) {
		AudioPlayer audioPlayer = new AudioPlayer(filePath);
		musicClip = audioPlayer;
		audioPlayer.loop(Clip.LOOP_CONTINUOUSLY);
	}

//private void playMusic(String musicPath) {
//    try {
//        // Load music resource
//        InputStream musicStream = getClass().getResourceAsStream(musicPath);
//        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicStream);
//
//        // Get the clip
//        Clip clip = AudioSystem.getClip();
//        clip.open(audioInputStream);
//
//        // Play the music
//        clip.start();
//
//        // Loop the music
//        clip.loop(Clip.LOOP_CONTINUOUSLY);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
	private void stopMusic() {
		if (musicClip != null) {
			musicClip.stop();
			musicClip.close();
		}
	}

	private void exitGame() {
		stopMusic();
		System.exit(0);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new HitMeCTA2();
			}
		});
	}
}
