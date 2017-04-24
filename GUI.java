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
	private static final int cardHeight = 120;
	private static final int cardWidth = 98;
	
	private JFrame frmTexasHoldem;
	private JTextArea textArea;
	
	private JLabel[] boardCardImages = new JLabel[5];
	private JLabel[] playerHoleCardImages = new JLabel[2];
	private JLabel[] botHoleCardImages = new JLabel[2];
	
	private JLabel playerChips;
	private JLabel playerChipsInPot;
	private JLabel botChips;
	private JLabel botChipsInPot;
	private JLabel pot;
	private JLabel chipsToCall;
	private JLabel userPrompt;
	
	private JButton btnFold;
	private JButton btnCall;
	private JButton btnRaise;
	private JButton btnCheck;
	private JButton btnBet;
	private JScrollPane scrollPane;
	
	private Game.Move userMove = Game.Move.NOMOVE;
	
	private Game game;

	/**
	 * Create the application.
	 * @param game 
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmTexasHoldem = new JFrame();
		frmTexasHoldem.setResizable(false);
		frmTexasHoldem.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmTexasHoldem.setTitle("Texas Hold 'Em");
		frmTexasHoldem.setBounds(100, 100, 800, 800);
		frmTexasHoldem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTexasHoldem.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(120, 400, 560, 200);
		frmTexasHoldem.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.BOLD, 14));
		textArea.setBounds(122, 409, 556, 196);
		scrollPane.setViewportView(textArea);
		textArea.setColumns(10);
		
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
		
		
		
		btnFold = new JButton("Fold");
		btnFold.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFold.setBounds(120, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnFold);
		
		btnBet = new JButton("Bet");
		btnBet.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBet.setBounds(235, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnBet);
		
		btnRaise = new JButton("Raise");
		btnRaise.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnRaise.setBounds(350, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnRaise);
		
		btnCall = new JButton("Call");
		btnCall.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCall.setBounds(465, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnCall);
		
		btnCheck = new JButton("Check");
		btnCheck.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCheck.setBounds(580, 620, 100, 30);
		frmTexasHoldem.getContentPane().add(btnCheck);
		
		JButton btnExit = new JButton("Quit");
		btnExit.setBounds(695, 10, 90, 25);
		frmTexasHoldem.getContentPane().add(btnExit);
		
		
		
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
		
		disableMoveButtons();
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int wantsToQuit = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Continue", 
						JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (wantsToQuit == 0) {
					System.exit(0); // Quit game
				}
			}
		});
		
		// Setup initial view
		setInitialView();
	}
	private void disableMoveButtons() {
		btnFold.setEnabled(false);
		btnCall.setEnabled(false);
		btnCheck.setEnabled(false);
		btnBet.setEnabled(false);
		btnRaise.setEnabled(false);
	}
	
	private void enableMoveButtons() {
		btnFold.setEnabled(true);
		btnCall.setEnabled(true);
		btnCheck.setEnabled(true);
		btnBet.setEnabled(true);
		btnRaise.setEnabled(true);
	}
	
	public Game.Move getUserMove() {
		enableMoveButtons();
		while (this.userMove.equals(Game.Move.NOMOVE)) {
			System.out.println("");
		}
		Game.Move moveMade = userMove;
		this.userMove = Game.Move.NOMOVE;
		disableMoveButtons();
		System.out.println("Waiting...");
		return moveMade;
	}
	
	private void hasCalled() {
		this.userMove = Game.Move.CALL;
	}
	private void hasBet() {
		this.userMove = Game.Move.BET;
	}
	private void hasFold() {
		this.userMove = Game.Move.FOLD;
	}
	private void hasCheck() {
		this.userMove = Game.Move.CHECK;
	}
	private void hasRaise(){
		this.userMove = Game.Move.RAISE;
	}
	
	public Game.Move getLastMove() {
		return this.userMove;
	}
	
	public void appendTextAreaLine(String text){
		textArea.append(text + "\n");

	}
	public void clearTextArea(){
		textArea.setText(null);
	}
	/**
	 * Display currently visible board cards. All non-visible board cards
	 * will be indicated by a turned over card
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
	 * Hides the bot's hole cards by displaying the standard back-of-card image
	 */
	public void hideBotCards() {
		this.botHoleCardImages[0].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
		this.botHoleCardImages[1].setIcon(scaleCardImage(new ImageIcon(Utilities.getCardFilePath(null))));
	}
	
	/**
	 * Display the bot's hole cards
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
	
	public void promptUser() {
		this.userPrompt.setText("Your Turn!");
	}
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
	 */
	public ImageIcon scaleCardImage(ImageIcon toScale) {
		Image image = toScale.getImage(); // transform it 
		Image newimg = image.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return new ImageIcon(newimg);  // return scaled image
	}
	
	public void updatePot() {
		this.pot.setText("Pot: " + this.game.getPot());
	}
	
	public void updateChipsToCall() {
		this.chipsToCall.setText("Chips to Call: " + this.game.getChipsToCall());
	}
	
	public void updatePlayerChips() {
		this.playerChips.setText("Your chips: " + this.game.getPlayerChips());
	}
	
	public void updateBotChips() {
		this.botChips.setText("Bot's chips: " + this.game.getBotChips());
	}
	
	public void updatePlayerInPot() {
		this.playerChipsInPot.setText("Chips in Pot: " + this.game.getPlayerChipsInPot());
	}
	
	public void updateBotInPot() {
		this.botChipsInPot.setText("Bot's Chips in Pot: " + this.game.getBotChipsInPot());
	}
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
}
