import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 *  Created by Arya Agiwal and Oscar Goes 2022
 */

public class Manager {

	// constants
	private static final String WORD_LIST = "possible.txt";
	private static final Set<String> dictionary = getDictionary();
	// Letter position frequencies obtained by Rameez Kakodker (GitHub User: medicantBias117)
	private static final int[][] POS_FREQ = {
			{737,  2263,  1236,  1074,  680 },
			{909,  81,	  335,   243,   59  },
			{922,  176,   392,   411,   127 },
			{685,  84,    390,   471,   823 },
			{303,  1628,  882,   2327,  1522},
			{598,  24,    178,   233,   82  },
			{638,  76,    364,   423,   143 },
			{489,  546,   120,   235,   370 },
			{165,  1383,  1051,  880,   280 },
			{202,  11,    46,    29,    3   },
			{376,  95,    272,   503,   259 },
			{577,  699,   848,   771,   476 },
			{693,  188,   511,   402,   182 },
			{325,  345,   964,   788,   530 },
			{262,  2096,  993,   698,   389 },
			{859,  231,   364,   418,   147 },
			{78,   15,    13,    2,     4   },
			{628,  940,   1198,  719,   673 },
			{1565, 93,    533,   516,   3958},
			{815,  239,   616,   898,   727 },
			{189,  1187,  667,   401,   67  },
			{242,  52,    240,   156,   4   },
			{413,  163,   271,   128,   64  },
			{16,   57,    133,   12,    70  },
			{181,  271,   213,   108,   1301},
			{105,  29,    142,   126,   32  }
			};
	
	// instance variables
	TreeMap<String, Integer> currentWords;
	int guessesDone;
	String lastGuessed;

	// constructor
	public Manager() {
		currentWords = getWeights(dictionary);
		guessesDone = 0;
		lastGuessed = "";
	}

	// helper methods
	
	/*
	 * Creates the initial word dictionary used by the program
	 */
	private static Set<String> getDictionary() {
		Set<String> dictionary = new TreeSet<>();
		try {
			Scanner input = new Scanner(new File(WORD_LIST));
			Scanner inputTwo = new Scanner(new File("wordle-answers.txt"));
			while (input.hasNext())
				dictionary.add(input.next().toLowerCase());
			input.close();
			while (inputTwo.hasNext())
				dictionary.add(inputTwo.next().toLowerCase());
			inputTwo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to find this file: " + WORD_LIST);
		}
		return Collections.unmodifiableSet(dictionary);
	}

	/*
	 * Assigns a weight to each word in the dictionary
	 */
	private static TreeMap<String, Integer> getWeights(Set<String> dict) {
		TreeMap<String, Integer> currentList = new TreeMap<>();
		for (String temp : dict) {
			currentList.put(temp, calcWeight(temp));
		}
		return currentList;
	}

	/*
	 * Calculates the weight of a word based on letter position frequencies
	 */
	private static int calcWeight(String word) {
		int weight = 0;
		Set<Character> chars = new HashSet<>();
		for (int i = 0; i < 5; i++) {
			weight += POS_FREQ[word.charAt(i) - 'a'][i];
			chars.add(word.charAt(i));
		}
		weight *= chars.size();
		weight /= 5;
		return weight;
	}

	/*
	 * Removes all words that do not have the correct character at the inputed index
	 */
	private void updateGreen(int index) {
		char c = lastGuessed.charAt(index);
		TreeMap<String, Integer> updated = new TreeMap<>();
		for (String temp : currentWords.keySet()) {
			if (temp.charAt(index) == c) {
				updated.put(temp, currentWords.get(temp));
			}
		}
		currentWords = updated;
	}

	/*
	 * Removes all words that contain invalid characters
	 */
	private void updateBlack(int index) {
		char c = lastGuessed.charAt(index);
		TreeMap<String, Integer> updated = new TreeMap<>();
		for (String temp : currentWords.keySet()) {
			if (!(temp.charAt(0) == c || temp.charAt(1) == c || temp.charAt(2) == c || temp.charAt(3) == c
					|| temp.charAt(4) == c)) {
				updated.put(temp, currentWords.get(temp));
			}
		}
		currentWords = updated;
	}

	/*
	 * Removes words that do not contain a valid character or contain it at an invalid position
	 */
	private void updateYellow(int index) {
		char c = lastGuessed.charAt(index);
		TreeMap<String, Integer> updated = new TreeMap<>();
		for (String temp : currentWords.keySet()) {
			if ((temp.charAt(index) != c && (temp.charAt(0) == c || temp.charAt(1) == c || temp.charAt(2) == c
					|| temp.charAt(3) == c || temp.charAt(4) == c))) {
				updated.put(temp, currentWords.get(temp));
			}
		}
		currentWords = updated;
	}

	/*
	 * Updates word list to contain words with enough repeating characters if the word contains repeated characters
	 */
	private void updateRepeat(int count, char ch) {
		if (count <= 1) {
			return;
		}
		// DEBUG: System.out.println("Running updateRepeat for char " + ch + " with
		// count " + count);
		TreeMap<String, Integer> updated = new TreeMap<>();
		for (String word : currentWords.keySet()) {
			int curCount = 0;
			for (int c = 0; c < 5; c++) {
				if (word.charAt(c) == ch) {
					curCount++;
				}
			}
			if (curCount >= count) {
				updated.put(word, currentWords.get(word));
			}
		}
		currentWords = updated;
	}

	// methods
	public String attemptGuess() {
		if (guessesDone == 0) {
			Scanner first = new Scanner(System.in);
			System.out.println("Pick a first guess. To get the best results, we highly recommend the word \"SOARE\":");
			String firstPick = first.next().toLowerCase();
			currentWords.remove(firstPick);
			lastGuessed = firstPick;
			guessesDone++;
			return firstPick;
		} else {
			int max = 0;
			String bestGuess = "";
			for (String word : currentWords.keySet()) {
				int wordWeight = currentWords.get(word);
				if (wordWeight > max) {
					max = wordWeight;
					bestGuess = word;
				}
			}
			lastGuessed = bestGuess;
			currentWords.remove(bestGuess);
			guessesDone++;
			return bestGuess;
		}
	}

	public void updateManager(String result) {
		HashMap<Character, Integer> duplicates = new HashMap<>();
		System.out.println("\nUpdating and last guessed is: " + lastGuessed + " while result is: " + result + "\n");
		for (int c = 0; c < 5; c++) {
			if (result.charAt(c) == 'G') {
				updateGreen(c);
				if (duplicates.get(lastGuessed.charAt(c)) == null) {
					duplicates.put(lastGuessed.charAt(c), 1);
				} else {
					duplicates.put(lastGuessed.charAt(c), duplicates.get(lastGuessed.charAt(c)) + 1);
				}
			}
		}

		for (int c = 0; c < 5; c++) {
			if (result.charAt(c) == 'Y') {
				updateYellow(c);
				if (duplicates.get(lastGuessed.charAt(c)) == null) {
					duplicates.put(lastGuessed.charAt(c), 1);
				} else {
					duplicates.put(lastGuessed.charAt(c), duplicates.get(lastGuessed.charAt(c)) + 1);
				}
			}
		}

		for (int c = 0; c < 5; c++) {
			if (result.charAt(c) == 'B' && duplicates.get(lastGuessed.charAt(c)) == null) {
				// DEBUG: System.out.println("removing character " + lastGuessed.charAt(c));
				updateBlack(c);
			}
		}

		// DEBUG: System.out.println(duplicates);

		for (char c : duplicates.keySet()) {
			updateRepeat(duplicates.get(c), c);
		}
	}

	public String toString() {
		StringBuilder print = new StringBuilder("");
		for (String word : currentWords.keySet()) {
			print.append(word);
			print.append(": ");
			print.append(currentWords.get(word));
			print.append("\n");
		}
		return print.toString();
	}

	public boolean guessAble() {
		return currentWords.size() > 0;
	}

	public String specialUpdateManager(String result) {
		updateManager(result);
		if (currentWords.size() <= 2) {
			return attemptGuess();
		}
		int varChar = 0;
		for (int c = 0; c < 5; c++) {
			if (result.charAt(c) == 'B') {
				varChar = c;
			}
		}

		// DEBUG: System.out.println(currentWords);
		Set<Character> temp = new HashSet<>();
		for (String word : currentWords.keySet()) {
			temp.add(word.charAt(varChar));
		}
		// DEBUG: System.out.println(temp);
		// Making a new set of all words
		TreeMap<String, Integer> emergencySet = getWeights(dictionary);
		TreeMap<String, Integer> specialSet = new TreeMap<>();
		for (String word : emergencySet.keySet()) {
			int weight = 0;
			Set<Character> localTemp = new HashSet<>();
			for (int i = 0; i < 5; i++) {
				if (temp.contains(word.charAt(i)) && !localTemp.contains(word.charAt(i))) {
					weight++;
					localTemp.add(word.charAt(i));
				}
			}
			if (weight != 0) {
				specialSet.put(word, weight);
			}
		}
		// DEBUG: System.out.println(specialSet);

		int max = 0;
		String bestGuess = "";
		for (String word : specialSet.keySet()) {
			int wordWeight = specialSet.get(word);
			if (wordWeight > max) {
				max = wordWeight;
				bestGuess = word;
			}
		}
		lastGuessed = bestGuess;
		currentWords.remove(bestGuess);
		guessesDone++;
		System.out.println(bestGuess);
		return bestGuess;
	}
}
