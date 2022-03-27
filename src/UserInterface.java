import java.util.*;

//Created by Arya Agiwal and Oscar Goes 2022

public class UserInterface {
	
	public static void main(String[] args) {
		Manager game = new Manager();
		System.out.println("Welcome to the wordle solver.\n");
		
		//DEBUG
		//System.out.println(game);
		//System.out.println("Best guess: " + game.attemptGuess());
		//System.out.println(game);
		//game.updateManager("BGYGB");
		//System.out.println(game);
		
		int tries = 0;
		Scanner in = new Scanner(System.in);  
		while (tries < 6) {
			System.out.println("Best guess: " + game.attemptGuess());
			System.out.println("What did wordle return? Enter 5 character string with G for green, Y for yellow, B for black.");
			String current = in.next().toUpperCase();
			while (!isValid(current)) {
				System.out.println("What did wordle return? Enter 5 character string with G for green, Y for yellow, B for black.");
				current = in.next().toUpperCase();
			}
			int gCount = 0;
			for (int i = 0; i < 5; i++) {
				if (current.charAt(i) == 'G') {
					gCount++;
				}
			}
			if (gCount == 5) {
				System.out.println("What's there to be happy about?");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Job's not finished. Job finished?");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("I don't think so.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Cheater.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Made by Arya Agiwal and Oscar Goes :)");
				System.exit(0);
			}
			game.updateManager(current);
			
			//DEBUGGING
			//System.out.println(game);
			tries++;
		}
		if (tries == 6) {
			System.out.println("Do you want to imagine something?");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Imagine losing with the help of a bot.");
		}
		in.close();
	}
	
	private static boolean isValid(String current) {
		if (current.length() != 5) {
			return false;
		}
		for (int i = 0; i < 5; i++) {
			if (current.charAt(i) != 'G' && current.charAt(i) != 'B' && current.charAt(i) != 'Y') {
				return false;
			}
		}
		return true;
	}
	
}
