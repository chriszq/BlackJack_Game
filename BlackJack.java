// project black jack

// this project was done with no knowledge of ArrayList/Collections and using more than 1 class

// revised version
// 1. player card dealt face up
// 2. dealer card dealt one face down one face down
// 3. if dealer has 21 then
// 	- if player has 21 it is a draw
// 	- if player does not have 21 then player loses
// 4. if #3 is passed then it's the player's turn first. player can hit until bust or stay. if bust then game ends
// 5. if player does not bust then it's the dealer's turn
// 6. dealer must hit on < 17 regardless of whether they already have the greater hand. No hit on soft 17 rule
// 7. result is decided on logically.
// 8. tracks the Aces on a player's hand and decides when it should be worth 1 or 11 (There can only be at most one Ace worth 11 per hand)
// 9. deck is shuffled at the beginning
// 10. clears the case where player/dealer gets 2 aces on starting hand
// 11. player can hit on 21 if they want to

//completed on: 13/03/2018

import java.util.Random;
import java.util.Scanner;

public class BlackJack
{
//----Fisher Yates Shuffle method
	static void Shuffle(int[] array)
	{
		Random r = new Random();
		int n = array.length;

		for (int i = 0; i < n; i++)
		{
			int randIndex = i + r.nextInt(n-i);	//selects a random index. it is setup with n-i because it is picking w/o replacement
			int randElt = array[randIndex];	//the random index determines the random element. This element value is stored for later use

			array[randIndex] = array[i];	//swaps the value with the first element of the array. Hence why we had to previously store the random element value
			array[i] = randElt;	//the second part of the swap. making use of the stored value.
		}
	}

//---drawing a card method
	static int nextCard(int[] array)
	{
		int k = 0;
		while (array[k] == 0)	//searches for the next card in the deck which is non-zero
		{
			k++;
		}
		int card = array[k];
		array[k] = 0;	//after dealing the card, the value in the array is replaced with zero to mark it as a drawn card
		return card;
	}

//----card face method
	static String cardFace(int N)
	{
		int Y = N % 13;

		if (Y == 0)	//King
		{
			return "K";
		}
		else if (Y == 1)	//Ace
		{
			return "A";
		}
		else if (Y < 11)	//numbers < 11 but greater than 0 will return their actual value.
		{
			return String.valueOf(Y);
		}
		else if (Y == 11)	//Jack and Queen
		{
			return "J";
		}
		else
		{
			return "Q";
		}
	}

//----card value method
	static int cardVal(int N)
	{
		int Y = N % 13;

		if (Y == 0 || Y == 11 || Y == 12)	//King, Jack, Queen
		{
			return 10;
		}
		else if (Y == 1)	//Ace
		{
			return 11;
		}
		else //numbers < 11 but greater than 0 will return their actual value.
		{
			return Y;
		}
	}

//----main method
	public static void main(String[] args)
	{
		Scanner kb = new Scanner(System.in);

		System.out.println("WELCOME TO BLACK JACK PROGRAM");

		int[] Deck = new int[52];	//initiate deck of cards

		for (int i = 0; i < Deck.length; i++ )//confirm order of cards after shuffle
		{
			Deck[i] = 1 + i;
			// System.out.println(Deck[i]);
		}

		Shuffle(Deck);

		System.out.println("-------------after Shuffle:");	//
		for (int x: Deck)
		{
			System.out.println(x);
		}

		int AcePlayer = 0;	//tracks aces for player
		int AceDealer = 0;	//tracks aces for dealer

		int card1 = nextCard(Deck);	//this mess is hard coding for deciding when Ace cards are 1 or 11
		if (cardVal(card1) == 11)
		{
			AcePlayer++;
		}
		int hand = cardVal(card1);
		int card2 = nextCard(Deck);
		if (cardVal(card2) == 11)
		{
			AcePlayer++;
		}
		hand += cardVal(card2);
		if (hand > 21 && AcePlayer > 0)
		{
			hand -= 10;
			AcePlayer--;
		}

		int card3 = nextCard(Deck);
		if (cardVal(card3) == 11)
		{
			AceDealer++;
		}
		int dealer = cardVal(card3);
		int card4 = nextCard(Deck);
		if (cardVal(card4) == 11)
		{
			AceDealer++;
		}
		dealer += cardVal(card4);
		if (dealer > 21 && AceDealer > 0)
		{
			dealer -= 10;
			AceDealer--;
		}

		// System.out.println("-------------after dealing 4 cards:");
		// for (int x: Deck)
		// {
		// 	System.out.println(x);
		// }

		// System.out.println("numbers drawn: " + card1 + " " + card2 + " " + card3 + " " + card4);
		// System.out.println("cards drawn: " + cardFace(card1) + " " + cardFace(card2) + " " + cardFace(card3) + " " + cardFace(card4));

		System.out.println("\nYou are dealt " + cardFace(card1) + " and " + cardFace(card2) + ".\nYour total is " + hand + ".");
		System.out.println("\nThe dealer has " + cardFace(card3) + " showing and a face down card");

		// System.out.println("AcePlayer = " + AcePlayer);
		// System.out.println("AceDealer = " + AceDealer);

//----black jack on dealer's initial hand
		if (dealer == 21 && hand != 21)	//dealer checks his initial hand for black jack
		{
			System.out.println("Dealer's face down card is " + cardFace(card4) + ". Dealer's total is " + dealer +".\nResult: you lose");	//dealer has black jack. player has no black jack. result: lose. program ends
			System.exit(0);
		}
		else if (dealer == 21 && hand == 21)
		{
			System.out.println("Dealer's face down card is " + cardFace(card4) + ". Dealer's total is " + dealer +".\nResult: it's a draw");	//dealer and player has black jack. result: tie. program ends
			System.exit(0);
		}

//----no black jack on dealer's initial hand. player's turn
		while (hand < 22)
		{
			System.out.print("Would you like to hit or stay?");
			String choice = kb.nextLine();
			if (choice.equals("hit"))	//hit
			{
				int draw = nextCard(Deck);
				if (cardVal(draw) == 11)	//keep track of aces
				{
					AcePlayer++;
				}
				hand += cardVal(draw);
				if (hand > 21 && AcePlayer > 0)	//if hand > 21 then ace becomes 1
				{
					hand -= 10;
					AcePlayer--;
				}
				System.out.println("You drew a " + cardFace(draw) + ".\nYour new total is " + hand + ".");
			}
			else
			{
				break;	//exit while loop if player does not hit
			}
		}
		if (hand > 21)	//player bust. program ends
		{
			System.out.println("You busted!");
			System.exit(0);
		}
//----player does not bust. dealer's turn
		else
		{
			System.out.println("\nDealer's turn. The face down card is " + cardFace(card4) + " Dealer current total is " + dealer);	//dealer reveals face down card before playing their hand
			while (dealer < 17)	//dealer always hits until at least 17
			{
				int drawdealer = nextCard(Deck);
				if(cardVal(drawdealer) == 11)	//keeping track of aces
				{
					AceDealer++;
				}
				dealer += cardVal(drawdealer);
				if (dealer > 21 && AceDealer > 0)	//if hand > 21 then ace becomes 1
				{
					dealer -= 10;
					AceDealer--;
				}
				System.out.println("\nDealer drew a " + cardFace(drawdealer) + ". Dealer's new total is " + dealer);
			}
		}
//----Comparing hand sizes		
		if (dealer > 21)	//dealer busts. result: player wins
		{
			System.out.println("Result: dealer busts. You win.");
		}
		else if (hand < dealer)	//result: dealer wins
		{
			System.out.println("Result: dealer is higher. You lose.");
		}
		else if (hand == dealer)	//result: tied
		{
			System.out.println("Result: it's a tie");
		}
		else	//result: player win
		{
			System.out.println("Result: you win.");
		}
	}
}