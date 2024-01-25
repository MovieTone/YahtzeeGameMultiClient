package com.movietone;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Yahtzee implements YahtzeeConstants {

    /**
     * input console scanner
     **/
    private static final Scanner in = new Scanner(System.in);

    private final Random random = new Random();

    /**
     * The number of players
     */
    private volatile int playersNumber = 0;

    /**
     * The names of the players, index base 1
     */
    private final String[] playerNames;

    /**
     * The names of the players, index base 1
     */
    private final int[] playerRounds;

    private final DataOutputStream[] playerDataOutput;
    private final DataInputStream[] playerDataInput;

    /**
     * The scorecard for all players (index base 1, so row and column [0] are empty)
     */
    volatile int[][] scoreCard;

    /**
     * Table of whether a category has been selected by a player
     */
    private volatile boolean[][] categoryHasBeenChosen;

    /**
     * whether an additional yahtzee is rolled or not
     */
    volatile boolean additionalYahtzee = false;

    private volatile boolean started;

    public Yahtzee() {
        playerNames = new String[100];
        scoreCard = new int[TOTAL + 1][100];
        playerRounds = new int[100];
        playerDataOutput = new DataOutputStream[100];
        playerDataInput = new DataInputStream[100];
        Arrays.fill(playerRounds, 1);
    }

    public void print(DataOutputStream dos, String line) throws IOException {
        dos.writeUTF(line);
    }

    public String read(DataInputStream dis) throws IOException {
        String line = null;
        while ((line = dis.readUTF()) == null)
            ;
        return line;
    }

    public void start(DataOutputStream dos, DataInputStream dis, int playerNumber) throws IOException {
        for (int i = 1; i <= playersNumber; i++) {
            displayScoreboard(dos, dis, i);
        }
    }

    public synchronized int createPlayer(DataOutputStream dos, DataInputStream dis) throws IOException {
        print(dos, "Welcome player " + playersNumber + ", please enter your name: ");
        String name = read(dis);
        System.out.println(name + " has entered the game lobby!");
        createPlayer(name);
        playerDataInput[playersNumber] = dis;
        playerDataOutput[playersNumber] = dos;
        return playersNumber;
    }

    public synchronized int createPlayer(String name) {
        playersNumber++;
        playerNames[playersNumber] = name;
        return playersNumber;
    }

    public synchronized int nextRound(DataOutputStream dataOutput, DataInputStream dataInput, int playerNumber) throws IOException {
        print(dataOutput, "Press <<ENTER>> to roll the dice...");
        read(dataInput);

        int[] dice = new int[5];
        for (int i = 0; i < 5; i++) {
            dice[i] = random.nextInt(6) + 1;
        }

        int numRerolls = 0;
        while (true) {
            int res = displayMenu(dataOutput, dataInput, dice, playerNumber, numRerolls);
            if (res == 0) {
                numRerolls++;
            } else if (res == 1) {
                break;
            }
        }
        ++playerRounds[playerNumber];

        for (int i = 1; i <= playersNumber; i++) {
            clearScreen(playerDataOutput[i]);
            for (int j = 1; j <= playersNumber; j++) {
                displayScoreboard(playerDataOutput[i], playerDataInput[i], j);
            }
        }

        System.out.println("Player " + playerNumber + " has completed their turn.");

        return playerRounds[playerNumber];
    }

    public int displayMenu(DataOutputStream dos, DataInputStream dis, int[] dice, int playerNumber, int numRerolls) throws IOException {
        String text = "";
        text += "            \t-----   -----   -----   -----   -----\n";
        text += "You rolled: \t"
            + "| " + dice[0] + " |   "
            + "| " + dice[1] + " |   "
            + "| " + dice[2] + " |   "
            + "| " + dice[3] + " |   "
            + "| " + dice[4] + " |\n";
        text += "            \t-----   -----   -----   -----   -----\n";
        print(dos, text);

        String choice = "3";
        if (numRerolls < 2) {
            displayOptions(dos);
            choice = read(dis);
        }

        if (choice.contains("1")) {
            print(dos, "Please enter in the dice position that you want to hold. Please separate each number with a <<SPACE>>:");
            String numbersLine = read(dis).trim();
            dice = rerollSome(numbersLine, dice);

            return 0;
        } else if (choice.contains("2")) {
            dice = rerollAll(dice);

            return 0;
        } else if (choice.contains("3")) {
            print(dos, "What category do you want to score this round against? (Please enter the category number)");
            int category = Integer.parseInt(read(dis).trim());
            scoreRound(playerNumber, category, dice);
        } else {
            return -1;
        }

        return 1;
    }

    public void scoreRound(int playerNumber, int category, int[] dice) {
        int score = calculateCategoryScore(category, dice);
        updateScore(playerNumber, category, score);
        addUpperBonus(playerNumber, category);
    }

    public int[] rerollAll(int[] dice) {
        for (int i = 0; i < 5; i++) {
            dice[i] = random.nextInt(6) + 1;
        }

        return dice;
    }

    public int[] rerollSome(String numbersLine, int[] dice) {
        String[] numbers = numbersLine.split(" ");
        for (int i = 0; i < 5; i++) {
            boolean found = false;
            for (int j = 0; j < numbers.length; j++) {
                if (Integer.parseInt(numbers[j]) - 1 == i) {
                    found = true;
                    continue;
                }
            }
            if (!found) {
                dice[i] = random.nextInt(6) + 1;
            }
        }

        return dice;
    }

    /**
     * add upper bonus if category is in upper section and the section has a score more than required
     *
     * @param playerNumber number of player
     * @param category     category to check
     */
    public void addUpperBonus(int playerNumber, int category) {
        if (category >= ONES && category <= SIXES) {
            if (sumScores(playerNumber, ONES, SIXES) >= UPPER_SECTION_SUM_BONUS) {
                if (scoreCard[UPPER_BONUS][playerNumber] == 0) {
                    scoreCard[UPPER_BONUS][playerNumber] = UPPER_BONUS_SCORE;
                }
            }
        }
    }

    public void displayOptions(DataOutputStream dos) throws IOException {
        print(dos, "What action would you like to perform next?");
        print(dos, " (1) Select dice to hold, and then re-roll the other dice?");
        print(dos, " (2) Re-roll all the dice?");
        print(dos, " (3) Score this round?");
    }

    public void displayScoreboard(DataOutputStream dos, DataInputStream dis, int playerNumber) throws IOException {
        String name = playerNames[playerNumber];
        int currentScore = sumScores(playerNumber, ONES, YAHTZEE);
        int round = playerRounds[playerNumber];

        // scores
        int ones = scoreCard[1][playerNumber];
        int twos = scoreCard[2][playerNumber];
        int threes = scoreCard[3][playerNumber];
        int fours = scoreCard[4][playerNumber];
        int fives = scoreCard[5][playerNumber];
        int sixes = scoreCard[6][playerNumber];
        int largeStraight = scoreCard[7][playerNumber];
        int smallStraight = scoreCard[8][playerNumber];
        int fullHouse = scoreCard[9][playerNumber];
        int threeOfKind = scoreCard[10][playerNumber];
        int fourOfKind = scoreCard[11][playerNumber];
        int chance = scoreCard[12][playerNumber];
        int yahtzee = scoreCard[13][playerNumber];
        int bonus = scoreCard[14][playerNumber];

        String text = "";
        // display a single scoreboard
        text += displayHeaderLine(dos);

        text += String.format("%s%s", "| Name: ", name);
        text += String.format("%20s%s%1s", "|", "  Current Score: ", currentScore);
        text += String.format("%4s%s%37s", "|", "  Current Round: " + round, "|\n");

        text += displayLineBreak(dos);

        // display upper section
        text += String.format("%s%s", "| (1) Ones: ", ones);
        text += String.format("%3s%s%1s", "|", " (2) Twos: ", twos);
        text += String.format("%3s%s%1s", "|", " (3) Threes: ", threes);
        text += String.format("%3s%s%1s", "|", " (4) Fours: ", fours);
        text += String.format("%3s%s%1s", "|", " (5) Fives: ", fives);
        text += String.format("%3s%s%1s", "|", " (6) Sixes: ", sixes);
        text += String.format("%3s%s%1s%4s", "|", " Bonus: ", bonus, "|\n");

        text += displayLineBreak(dos);

        // display lower section
        text += String.format("%s%s", "| (7) Large Straight: ", largeStraight);
        text += String.format("%3s%s%1s", "|", " (8) Small Straight: ", smallStraight);
        text += String.format("%3s%s%1s", "|", " (9) Full House: ", fullHouse);
        text += String.format("%3s%s%1s%13s", "|", " (10) Three of a Kind: ", threeOfKind, "|\n");

        text += displayLineBreak(dos);

        text += String.format("%s%s", "| (11) Four of a Kind: ", fourOfKind);
        text += String.format("%3s%s%1s", "|", " (12) Chance: ", chance);
        text += String.format("%3s%s%1s%47s", "|", " (13) Yahtzee!: ", yahtzee, "|\n");

        text += displayHeaderLine(dos);

        print(dos, text);
    }

    public String displayHeaderLine(DataOutputStream dos) throws IOException {
        String line = "";
        for (int i = 0; i < 108; i++) {
            line += "-";

        }
        line += "\n";
        return line;
    }

    public String displayLineBreak(DataOutputStream dos) throws IOException {
        String line = "| ";
        for (int i = 0; i < 104; i++) {
            line += "-";
        }
        line += " |\n";
        return line;
    }

    /**
     * Determines whether the dice fulfill the requirements of a category.
     *
     * @param dice     The set of dice
     * @param category The category in question
     * @return Whether the dice fulfill the requirements of a category
     */
    public boolean isDiceValidForCategory(int[] dice, int category) {
        if (category >= ONES && category <= SIXES) {
            for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == category) {
					return true;
				}
            }
        }
        switch (category) {
            case THREE_OF_A_KIND:
                return isNOfAKind(3, dice, false);
            case FOUR_OF_A_KIND:
                return isNOfAKind(4, dice, false);
            case FULL_HOUSE:
                return (isNOfAKind(3, dice, true) && isNOfAKind(2, dice, true));
            case SMALL_STRAIGHT:
                return isStraight(4, dice);
            case LARGE_STRAIGHT:
                return isStraight(5, dice);
            case YAHTZEE:
                return isNOfAKind(5, dice, false);
            case CHANCE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Determines whether there are N of the same die value showing
     *
     * @param n     The number of dice with the same value
     * @param dice  The set of dice
     * @param exact Whether it must be exactly N or at least N
     * @return Whether there are N of a kind showing
     */
    public boolean isNOfAKind(int n, int[] dice, boolean exact) {
        boolean result = false;
        int[] frequency = diceValueFrequency(dice);
        for (int i = 0; i < frequency.length; i++) {
            if (exact) {
				if (frequency[i] == n) {
					return true;
				}
            } else {
				if (frequency[i] >= n) {
					return true;
				}
            }
        }
        return result;
    }

    /**
     * Creates an array listing the frequency of each possible die value for the set of dice
     *
     * @param dice The set of dice
     * @return An ordered array listing the frequency of each die value
     */
    public int[] diceValueFrequency(int[] dice) {
        int[] result = new int[6];
        for (int i = 0; i < N_DICE; i++) {
            result[dice[i] - 1]++;
        }
        return result;
    }

    /**
     * Sums the dice.
     *
     * @param dice                The set of dice
     * @param dieValueRequirement Whether only a specific value should be summed. Enter '0' for no requirement (sum all)
     * @return The sum of the dice
     */
    public int sumDice(int[] dice, int dieValueRequirement) {
        int result = 0;
        for (int i = 0; i < N_DICE; i++) {
            if (dieValueRequirement == 0) {
                // Sum all dice
                result += dice[i];
            } else {
                // Add die if it matches the required value
                if (dice[i] == dieValueRequirement) {
                    result += dice[i];
                }
            }
        }
        return result;
    }

    /**
     * Determines whether the dice contain a straight (sequential) of a specific length.
     *
     * @param n    The length of the straight
     * @param dice The set of dice
     * @return Whether the dice contain a straight
     */
    public boolean isStraight(int n, int[] dice) {
        int[] frequency = diceValueFrequency(dice);
        for (int i = 0; i < (frequency.length - n + 1); i++) {
            int nInARow = 0;
            for (int j = 0; j < n; j++) {
                if (frequency[i + j] > 0) {
                    nInARow++;
                }
            }
            if (nInARow == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sums a set of scores on the scorecard.
     *
     * @param player        The player number (index base 1)
     * @param startCategory The starting category (inclusive)
     * @param endCategory   The ending category (inclusive)
     * @return The sum of the scores
     */
    public int sumScores(int player, int startCategory, int endCategory) {
        int result = 0;
        for (int i = startCategory; i <= endCategory; i++) {
            result += scoreCard[i][player];
        }
        return result;
    }

    /**
     * Determines if all categories in the upper score have been filled.
     */
    public boolean isUpperScoreComplete(int player) {
        for (int i = ONES; i <= SIXES; i++) {
            if (scoreCard[i][player] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the players score on the scorecard and the display.
     */
    public void updateScore(int player, int category, int score) {
        if (additionalYahtzee) {
            scoreCard[YAHTZEE][player] += YAHTZEE_BONUS_SCORE;
            additionalYahtzee = false;
        }
        // check if the box is not scored yet
        if (scoreCard[category][player] == 0) {
            scoreCard[category][player] = score;
        }
    }


    /**
     * Calculates the score for a category.
     */
    public int calculateCategoryScore(int category, int[] dice) {
        if (!isDiceValidForCategory(dice, category)) {
            return 0;
        }

        switch (category) {
            case ONES:
            case TWOS:
            case THREES:
            case FOURS:
            case FIVES:
            case SIXES:
                return sumDice(dice, category);
            case THREE_OF_A_KIND:
                sumDice(dice, 0);
            case FOUR_OF_A_KIND:
                return sumDice(dice, 0);
            case FULL_HOUSE:
                return FULL_HOUSE_SCORE;
            case SMALL_STRAIGHT:
                return SMALL_STRAIGHT_SCORE;
            case LARGE_STRAIGHT:
                return LARGE_STRAIGHT_SCORE;
            case YAHTZEE:
                return YAHTZEE_SCORE;
            case CHANCE:
                return sumDice(dice, 0);
            default:
                return 0;
        }
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted() {
        System.out.println("The game is starting!");
        started = true;
    }

    public String getName(int playerNumber) {
        return playerNames[playerNumber];
    }

    public void clearScreen(DataOutputStream dos) throws IOException {
        dos.writeUTF("CLEAR_SCREEN");
    }

}