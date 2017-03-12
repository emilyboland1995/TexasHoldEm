import java.util.Scanner; 

public class CLI {
	
	int moveOption = 0; 
	
	
	   public static void main(String[] args) {
		   
		   //initialize variables 
		   char wantsToPlay = 'y';
	
		   //Initialize the classes 
		   GameState game = new GameState(); 
		   Deck deckCards = new Deck();
		   Player humanUser = new Player();
		   Bot bot = new Bot(); 
		   
		   //initialize input scanner		   
		   Scanner userInput = new Scanner(System.in);
		   
		   System.out.println("Welcome to Texas Hold'Em 1.0.0 by Team ETCetera");
		   

	   	   while (wantsToPlay == 'y') 
	   			   {
	   		   			
				   		/*
				   		if ( user.chips > 0 &&  bot.chips > 0)
				   		            playHand()
				   		    
				   		Else
				   		System.out.println("The winner is: " + game.getWinner(); //GameState.getWinner(); 

				   		 */ 
	   		   
	   		   
	   		   
	   		   			System.out.println("Would you like to play again? (y/n)");	   		
	   		   			wantsToPlay = userInput.next().charAt(0); 
	   		   			wantsToPlay = Character.toLowerCase(wantsToPlay);
	   		   			
	   		   			while (wantsToPlay != 'y' && wantsToPlay != 'n')
	   		   			{
	   		   				System.out.println("INVALID CHARACTER!");
	   		   				System.out.println("Would you like to play again? (y/n)");	   		
	   		   				wantsToPlay = userInput.next().charAt(0); 
	   		   				wantsToPlay = Character.toLowerCase(wantsToPlay);
	   		   			}
	   		   			if (wantsToPlay=='y')
	   		   			{
	   		   				System.out.println(); 
	   		   				System.out.println("---------------------"); 
	   		   			}
	   		   			else if (wantsToPlay=='n')
	   		   			{
	   		   				System.exit(0);  
	   		   			}
	   			   }
	   }
	   
	   	   private void getBotMove()
	   	   {
	   		   //Bot.getMove();
	   	   }

	   	   private void getPlayerMove()
	   	   {
	   		   Scanner playerOptionIn = new Scanner(System.in);
	   		
	   		   	System.out.println("---------------------"); 
	   		   	System.out.println("AVAILABLE MOVES");
	   		    System.out.println("1. Bid");
	   		    System.out.println("2. Check");
	   		    System.out.println("3. Fold");
	   		    System.out.println("4. Raise");
	   		    System.out.println("---------------------"); 
	   		    System.out.println("Please choose your move: ");
	   		    
	   		    moveOption = playerOptionIn.nextInt(); 
	   		    
	   		    while ((moveOption <= 0) || (moveOption >=5 ) )
	   		    {
	   		    	System.out.println("INVALID INPUT"); 
	   		    	System.out.println("Please choose your move: ");
	   		    	moveOption = playerOptionIn.nextInt(); 
	   		    }
	   		    
   		    	if (moveOption == 1)
   		    	{
   		    		//game.bid();  //GameState.bid()
   		    	}
   		    	else if (moveOption == 2)
   		    	{
   		    		//game.check(); //GameState.check()
   		    	}
   		    	else if (moveOption == 3)
   		    	{
   		    		//game.fold(); //GameState.fold()
   		    	}
   		    	else
   		    	{
   		    		//game.raise(); 	//GameState.raise()
   		    	}
	   	   }
}
