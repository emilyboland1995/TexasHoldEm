/**
 * This class defines the Graphical User Interface (GUI) for 
 * the game. At this stage, it is designed for simplicity. It
 * works with the Game class to handle user input and display
 * appropriate information.
 * 
 * Requirement Sets: 3.0.0
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Image;
import java.awt.event.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.SwingConstants;

public class GUI {
	private static String[] startMenuOptions = {"Start Game", "Set Starting Chips", "Adjust Rounds Per Blind Level", "Quit Game"};
	
	// Default sizes of the cards
	private static final int cardHeight = 120;
	private static final int cardWidth = 98;
	
	private JFrame frmTexasHoldem; // Main frame for the whole game
	
	// JLabel[]s for holding the images of playing cards
	private JLabel[] boardCardImages = new JLabel[5];
	private JLabel[] playerHoleCardImages = new JLabel[2];
	private JLabel[] botHoleCardImages = new JLabel[2];
	
	// JLabel objects to reflect key values
	// associated with the Game's current state
	private JLabel playerChips;
	private JLabel playerChipsInPot;
	private JLabel botChips;
	private JLabel botChipsInPot;
	private JLabel pot;
	private JLabel chipsToCall;
	private JLabel userPrompt;
	
	// Buttons that allow the user to
	// perform all available betting actions
	private JButton btnFold;
	private JButton btnCall;
	private JButton btnRaise;
	private JButton btnCheck;
	private JButton btnAllIn;
	private JButton btnBet;
	
	// Text area for game updates and key pieces
	// of information not otherwise present
	private JScrollPane scrollPane; 	
	private JTextArea textArea;
	
	private Game.Move userMove = Game.Move.NOMOVE; // Player move tracker
	
	private Game game; // The instance of Game from which GUI will update

	/**
	 * Create the application.
	 * @param game 		The instance of Game
	 * 					this GUI will be tied
	 * 					to
	 */
	public GUI(Game game) {
		this.game = game;
		initialize();
		try {
			frmTexasHoldem.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame. Creates all
	 * components found in the GUI, including betting action
	 * buttons, a text area, a quit button, and various text
	 * labels for key information.
	 * 
	 * Requirement Sets: 3.1.0, 3.2.0
	 */
	private void initialize() {
		
		frmTexasHoldem = new JFrame();
		frmTexasHoldem.setResizable(false);
		frmTexasHoldem.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmTexasHoldem.setTitle("Texas Hold 'Em");
		frmTexasHoldem.setBounds(100, 100, 800, 710);
		frmTexasHoldem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTexasHoldem.getContentPane().setLayout(null);
		
		// Requirement 3.2.1
		scrollPane = new JScrollPane();
		scrollPane.setBounds(120, 400, 560, 200);
		frmTexasHoldem.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.BOLD, 14));
		textArea.setBounds(122, 409, 556, 196);
		scrollPane.setViewportView(textArea);
		textArea.setColumns(10);
		
		// Requirement 3.3.1
		JPanel playerCard1 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) playerCard1.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		playerCard1.setBackground(Color.WHITE);
		playerCard1.setBorder(new LineBorder(new Color(0, 0, 0)));
		playerCard1.setBounds(60, 90, 100, 122);
		frmTexasHoldem.getContentPane().add(playerCard1);
		
		JPanel playerCard2 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) playerCard2.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		playerCard2.setBackground(Color.WHITE);
		playerCard2.setBorder(new LineBorder(new Color(0, 0, 0)));
		playerCard2.setBounds(175, 90, 100, 122);
		frmTexasHoldem.getContentPane().add(playerCard2);
		
		playerHoleCardImages[0] = new JLabel();
		playerCard1.add(playerHoleCardImages[0]);
		playerHoleCardImages[1] = new JLabel();
		playerCard2.add(playerHoleCardImages[1]);
		
		JPanel botCard2 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) botCard2.getLayout();
		flowLayout_5.setHgap(0);
		flowLayout_5.setVgap(0);
		botCard2.setBackground(Color.WHITE);
		botCard2.setBorder(new LineBorder(new Color(0, 0, 0)));
		botCard2.setBounds(640, 90, 100, 122);
		frmTexasHoldem.getContentPane().add(botCard2);
		
		JPanel botCard1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) botCard1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignOnBaseline(true);
		botCard1.setBackground(Color.WHITE);
		botCard1.setBorder(new LineBorder(new Color(0, 0, 0)));
		botCard1.setBounds(525, 90, 100, 122);
		frmTexasHoldem.getContentPane().add(botCard1);
		
		botHoleCardImages[0] = new JLabel();
		botCard1.add(botHoleCardImages[0]);
		botHoleCardImages[1] = new JLabel();
		botCard2.add(botHoleCardImages[1]);
		
		JPanel boardCard1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) boardCard1.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		boardCard1.setBackground(Color.WHITE);
		boardCard1.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardCard1.setBounds(120, 260, 100, 122);
		frmTexasHoldem.getContentPane().add(boardCard1);
		
		boardCardImages[0] = new JLabel();
		boardCard1.add(boardCardImages[0]);
		
		JPanel boardCard2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) boardCard2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		boardCard2.setBackground(Color.WHITE);
		boardCard2.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardCard2.setBounds(235, 260, 100, 122);
		frmTexasHoldem.getContentPane().add(boardCard2);
		
		boardCardImages[1] = new JLabel();
		boardCard2.add(boardCardImages[1]);
		
		JPanel boardCard3 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) boardCard3.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		boardCard3.setBackground(Color.WHITE);
		boardCard3.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardCard3.setBounds(350, 260, 100, 122);
		frmTexasHoldem.getContentPane().add(boardCard3);
		
		boardCardImages[2] = new JLabel();
		boardCard3.add(boardCardImages[2]);
		
		JPanel boardCard4 = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) boardCard4.getLayout();
		flowLayout_7.setVgap(0);
		flowLayout_7.setHgap(0);
		boardCard4.setBackground(Color.WHITE);
		boardCard4.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardCard4.setBounds(465, 260, 100, 122);
		frmTexasHoldem.getContentPane().add(boardCard4);
		
		boardCardImages[3] = new JLabel();
		boardCard4.add(boardCardImages[3]);
		
		JPanel boardCard5 = new JPanel();
		FlowLayout flowLayout_8 = (FlowLayout) boardCard5.getLayout();
		flowLayout_8.setHgap(0);
		flowLayout_8.setVgap(0);
		boardCard5.setBackground(Color.WHITE);
		boardCard5.setBorder(new LineBorder(new Color(0, 0, 0)));
		boardCard5.setBounds(580, 260, 100, 122);
		frmTexasHoldem.getContentPane().add(boardCard5);
		
		boardCardImages[4] = new JLabel();
		boardCard5.add(boardCardImages[4]);
		
		
		// Requirements: 3.3.2, 3.3.3, 3.3.4, 3.3.5
		playerChips = new JLabel("Your Chips:");
		playerChips.setBounds(60, 40, 220, 14);
		frmTexasHoldem.getContentPane().add(playerChips);
		
		playerChipsInPot = new JLabel("Chips in Pot: ");
		playerChipsInPot.setBounds(60, 60, 227, 14);
		frmTexasHoldem.getContentPane().add(playerChipsInPot);
		
		botChips = new JLabel("Bot's Chips:");
		botChips.setBounds(525, 40, 227, 14);
		frmTexasHoldem.getContentPane().add(botChips);
		
		botChipsInPot = new JLabel("Bot's Chips in Pot: ");
		botChipsInPot.setBounds(525, 60, 227, 14);
		frmTexasHoldem.getContentPane().add(botChipsInPot);
		
		pot = new JLabel("Pot: ");
		pot.setHorizontalAlignment(SwingConstants.CENTER);
		pot.setBounds(340, 90, 120, 14);
		frmTexasHoldem.getContentPane().add(pot);
		
		chipsToCall = new JLabel("Chips to Call: ");
		chipsToCall.setHorizontalAlignment(SwingConstants.CENTER);
		chipsToCall.setBounds(340, 120, 120, 14);
		frmTexasHoldem.getContentPane().add(chipsToCall);
		
		userPrompt = new JLabel("");
		userPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		userPrompt.setFont(new Font("Tahoma", Font.BOLD, 14));
		userPrompt.setBounds(340, 150, 120, 14);
		frmTexasHoldem.getContentPane().add(userPrompt);
		
		
		// Requirement: 3.1.2
		btnFold = new JButton("Fold");
		btnFold.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFold.setBounds(70, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnFold);
		
		btnBet = new JButton("Bet");
		btnBet.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBet.setBounds(185, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnBet);
		
		btnRaise = new JButton("Raise");
		btnRaise.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnRaise.setBounds(300, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnRaise);
		
		btnCall = new JButton("Call");
		btnCall.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCall.setBounds(415, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnCall);
		
		btnCheck = new JButton("Check");
		btnCheck.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCheck.setBounds(530, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnCheck);
		
		btnAllIn = new JButton("All In");
		btnAllIn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAllIn.setBounds(645, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnAllIn);

		btnFold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasFold();
			}
		});
		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasBet();
			}
		});
		btnRaise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasRaise();
			}
		});
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasCheck();
			}
		});
		btnCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasCalled();
			}
		});
		
		btnAllIn.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 hasAllIn();
			 }
		});
		
		disableMoveButtons();
		
		
		// Requirement: 3.1.1
		JButton btnExit = new JButton("Quit");
		btnExit.setBounds(695, 10, 90, 25);
		frmTexasHoldem.getContentPane().add(btnExit);
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (confirmQuit()) { // If user wants to quit
					System.exit(0); 
				}
			}
		});
		
		// Setup initial view
		setInitialView();
	}
	/**
	 * Prompts the user to confirm whether or not they
	 * wish to fully exit the game.
	 * @return		True if the user requests to quit, false
	 * 				if otherwise.
	 * 
	 * Requirement: 3.1.1
	 */
	public boolean confirmQuit() {
		return JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Continue", 
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE) == 0;
	}
	/**
	 * Disables all betting action buttons
	 * 
	 * Requirement: 3.1.2
	 */
	public void disableMoveButtons() {
		btnFold.setEnabled(false);
		btnCall.setEnabled(false);
		btnCheck.setEnabled(false);
		btnBet.setEnabled(false);
		btnRaise.setEnabled(false);
		btnAllIn.setEnabled(false);
	}
	/**
	 * Enables all betting action buttons
	 * 
	 * Requirement: 3.1.2
	 */
	public void enableMoveButtons() {
		btnFold.setEnabled(true);
		btnCall.setEnabled(true);
		btnCheck.setEnabled(true);
		btnBet.setEnabled(true);
		btnRaise.setEnabled(true);
		btnAllIn.setEnabled(true);
	}
	/**
	 * Waits for the user to select one of the betting
	 * action buttons and then returns the move type
	 * of the button selected
	 * @return		The Game.Move enum representing the
	 * 				move selected by the user
	 * 
	 * Requirement: 3.1.2
	 */
	public Game.Move getUserMove() {
		while (this.userMove.equals(Game.Move.NOMOVE)) {
			System.out.println("");
		}
		Game.Move moveMade = userMove;
		this.userMove = Game.Move.NOMOVE;
		return moveMade;
	}
	/**
	 * Disable only the Fold button
	 */
	public void disableFold() {
		btnFold.setEnabled(false);
	}
	/**
	 * Disable only the Call button
	 */
	public void disableCall() {
		btnCall.setEnabled(false);
	}
	/**
	 * Disable only the Check button
	 */
	public void disableCheck() {
		btnCheck.setEnabled(false);
	}
	/**
	 * Disable only the Bet button
	 */
	public void disableBet() {
		btnBet.setEnabled(false);
	}
	/**
	 * Disable only the Raise button
	 */
	public void disableRaise() {
		btnRaise.setEnabled(false);
	}
	/**
	 * Disable only the All In button
	 */
	public void disableAllIn() {
		btnAllIn.setEnabled(false);
	}
	/**
	 * Set the userMove to the type
	 * Call to indicate that the user's
	 * action was to call
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasCalled() {
		this.userMove = Game.Move.CALL;
	}
	/**
	 * Set the userMove to the type
	 * Bet to indicate that the user's
	 * action was to bet
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasBet() {
		this.userMove = Game.Move.BET;
	}
	/**
	 * Set the userMove to the type
	 * Fold to indicate that the user's
	 * action was to fold
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasFold() {
		this.userMove = Game.Move.FOLD;
	}
	/**
	 * Set the userMove to the type
	 * Check to indicate that the user's
	 * action was to check
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasCheck() {
		this.userMove = Game.Move.CHECK;
	}
	/**
	 * Set the userMove to the type
	 * Raise to indicate that the user's
	 * action was to raise
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasRaise(){
		this.userMove = Game.Move.RAISE;
	}
	/**
	 * Set the userMove to the type
	 * AllIn to indicate that the user's
	 * action was to go all in
	 * 
	 * Requirement: 3.1.2
	 */
	private void hasAllIn(){
		this.userMove = Game.Move.ALLIN;
	}
	/**
	 * Adds a new String of text to the text
	 * area in the GUI. Note: If the String
	 * text parameter contains a string with
	 * one or more newline (\n) characters,
	 * then multiple lines will be added.
	 * @param text		A String containing
	 * 					the text to be added
	 * 					to the text area
	 * 
	 * Requirement: 3.2.1
	 */
	public void appendTextAreaLine(String text){
		textArea.append(text + "\n");

	}
	/**
	 * Clears all text from the text area
	 * 
	 * Requirement: 3.2.2
	 */
	public void clearTextArea(){
		textArea.setText(null);
	}
	/**
	 * Display currently visible board cards. All non-visible board cards
	 * will be indicated by a turned over card
	 * 
	 * Requirement: 3.3.1
	 */
	public void updateBoardCards() {
		if (!game.hasFlopOccured()) {
			ImageIcon boardCardImage = scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null)));
			for (int i = 0; i < 5; i++) {
				boardCardImages[i].setIcon(boardCardImage);
			}
		} else {
			ArrayList<Card> boardCards = game.getVisibleBoardCardsInArrayList();
			if (boardCards.size() >= 3) {
				for (int i = 0; i < 3; i++) {
						boardCardImages[i].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(boardCards.get(i)))));
				}
			}
			if (boardCards.size() >= 4) { // Turn has occurred
				boardCardImages[3].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(boardCards.get(3)))));
			}
			if (boardCards.size() == 5) { // River has occurred
				boardCardImages[4].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(boardCards.get(4)))));
			}
		}
	}
	
	/**
	 * Display the player's current hole cards
	 * 
	 * Requirement: 3.3.1
	 */
	public void setPlayerCards() {
		Card[] holeCards = this.game.getPlayerHoleCards();
		if (holeCards != null) { // Hole cards have been set
			this.playerHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(holeCards[0]))));
			this.playerHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(holeCards[1]))));
		} else { // Player's hole cards have not been set yet, display backs of cards
			this.playerHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
			this.playerHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
		}
	}
	
	/**
	 * Hides the bot's hole cards by displaying the standard 
	 * back-of-card image
	 * 
	 * Requirement: 3.3.1
	 */
	public void hideBotCards() {
		this.botHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
		this.botHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
	}
	
	/**
	 * Display the bot's hole cards
	 * 
	 * Requirement: 3.3.1
	 */
	public void showBotCards() {
		Card[] holeCards = this.game.getBotHoleCards();
		if (holeCards != null) { // Hole cards have been set
			this.botHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(holeCards[0]))));
			this.botHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(holeCards[1]))));
		} else { // Bot's hole cards have not been set yet, display backs of cards
			this.botHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
			this.botHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
		}
	}
	/**
	 * Provides a prompt to inform the user that
	 * it is currently their turn.
	 * 
	 * Requirement: 3.3.5
	 */
	public void promptUser() {
		this.userPrompt.setText("Your Turn!");
	}
	/**
	 * Removes the user prompt, if present.
	 * 
	 * Requirement: 3.3.5
	 */
	public void removeUserPrompt() {
		this.userPrompt.setText("");
	}
	
	/**
	 * Scales the ImageIcon passed and returns a scaled version
	 * @param toScale		The ImageIcon that will be scaled
	 * @return				An ImageIcon representing the one
	 * 						passed scaled smoothly to the
	 * 						width and height specified by
	 * 						the constants cardWidth and cardHeight
	 * 
	 * Requirement: 3.3.1
	 */
	public ImageIcon scaleCardImage(ImageIcon toScale) {
		Image image = toScale.getImage(); // transform it 
		Image newimg = image.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return new ImageIcon(newimg);  // return scaled image
	}
	/**
	 * Updates the size of the pot displayed
	 * through the gui.
	 * 
	 * Requirement: 3.3.2
	 */
	public void updatePot() {
		this.pot.setText("Pot: " + this.game.getPot());
	}
	/**
	 * Updates the number of chips required to
	 * call
	 * 
	 * Requirement: 3.3.3
	 */
	public void updateChipsToCall() {
		int chips_to_call;
		 if(this.game.getChipsToCall() > this.game.getPlayerChipsInPot()){
			 chips_to_call = this.game.getChipsToCall() - this.game.getPlayerChipsInPot();
		 } else {
			 chips_to_call = 0;
		 }
		 this.chipsToCall.setText("Chips to Call: " + chips_to_call);
		 btnCall.setText("Call (" + chips_to_call + ")");
	}
	/**
	 * Updates the number of chips available to the player
	 * 
	 * Requirement: 3.3.4
	 */
	public void updatePlayerChips() {
		this.playerChips.setText("Your chips: " + this.game.getPlayerChips());
	}
	/**
	 * Updates the number of chips available to the bot
	 * 
	 * Requirement: 3.3.4
	 */
	public void updateBotChips() {
		this.botChips.setText("Bot's chips: " + this.game.getBotChips());
	}
	/**
	 * Updates the number of chips the player has in
	 * the pot
	 * 
	 * Requirement: 3.3.4
	 */
	public void updatePlayerInPot() {
		this.playerChipsInPot.setText("Chips in Pot: " + this.game.getPlayerChipsInPot());
	}
	/**
	 * Updates the number of chips the bot has in
	 * the pot
	 * 
	 * Requirement: 3.3.4
	 */
	public void updateBotInPot() {
		this.botChipsInPot.setText("Bot's Chips in Pot: " + this.game.getBotChipsInPot());
	}
	/**
	 * Sets the initial GUI view at the start of a game
	 * 
	 * Requirement Sets: 3.3.0
	 */
	public void setInitialView() {
		updatePot();
		updateChipsToCall();
		updatePlayerChips();
		updateBotChips();
		updateBotInPot();
		updateBoardCards();
		hideBotCards();
		setPlayerCards();
		updatePlayerInPot();
	}
	/**
	 * Updates the GUI components to reflect the
	 * current state of the game
	 * 
	 * Requirement Sets: 3.3.0
	 */
	public void updateView(){
		int x;
		if (this.game.botRevealed()) {
			showBotCards();
		} else {
			hideBotCards();
		}
		updatePot();
		updateChipsToCall();
		updatePlayerChips();
		updateBotChips();
		updateBotInPot();
		updateBoardCards();
		updatePlayerInPot();
		setPlayerCards();
		textArea.selectAll();
		 x = textArea.getSelectionEnd();
		 textArea.select(x,x);
	}
	/**
	 * Displays a simple start menu from which the user
	 * can 
	 * @return
	 * 
	 * Requirement: 2.1.1, 2.1.2, 3.1.1
	 */
	public int displayStartMenu() {
		int selectedOption = 0;
		selectedOption = JOptionPane.showOptionDialog(frmTexasHoldem, "Please Select One of the Following Options:", 
				"Start Menu", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, null, startMenuOptions, null);
		return selectedOption;
	}
}
