package com.movietone;

import java.util.Arrays;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class YahtzeeTest extends TestCase{

	private Yahtzee yg = new Yahtzee();

	int[] dice_example1 = {3,2,4,4,1};
	int[] dice_example2 = {1,2,3,4,5};
	int[] dice_example3 = {1,1,1,1,1};
	int[] dice_example4 = {2,2,3,3,3};
	int[] dice_example5 = {6,6,6,6,5};

	@Test
	public void sumDiceTest() {
		int[] dice = {3,2,4,4,1};

		int dieValueRequirement = 0;
		Assert.assertEquals(14,yg.sumDice(dice,dieValueRequirement));
		dieValueRequirement = 4;
		Assert.assertEquals(8,yg.sumDice(dice,dieValueRequirement));
	}
	@Test
	public void isStraightTest() {
		int[] dice = {3,2,4,4,1};

		Assert.assertEquals(true,yg.isStraight(4,dice));
		Assert.assertEquals(false,yg.isStraight(5,dice));

		dice[3] = 5; // now is {3,2,4,5,1};
		Assert.assertEquals(true,yg.isStraight(5,dice));
		Assert.assertEquals(true,yg.isStraight(4,dice));

		int[] noStraight = {4,3,2,2,2};
		Assert.assertEquals(false,yg.isStraight(5,noStraight));
		Assert.assertEquals(false,yg.isStraight(4,noStraight));
	}

	@Test
	public void diceValueFrequencyTest(){
		int[] dice = {3,2,4,4,1};

		int[] arr = {1,1,1,2,0,0};
		Assert.assertArrayEquals(arr ,yg.diceValueFrequency(dice));

		int[] allOnes = {1,1,1,1,1};
		int[] allOnesCount = {5,0,0,0,0,0};
		Assert.assertArrayEquals(allOnesCount ,yg.diceValueFrequency(allOnes));
	}

	@Test
	public void isNOfAKindTest() {
		int[] dice = {3,2,4,4,1};

		Assert.assertEquals(true ,yg.isNOfAKind(2,dice,false));
		Assert.assertEquals(true ,yg.isNOfAKind(2,dice,true));

		int[] fourOfAKind = {3,3,3,3,1};
		int[] threeOfAKind = {4,3,3,3,1};
		Assert.assertEquals(true ,yg.isNOfAKind(3,fourOfAKind,false));
		Assert.assertEquals(false ,yg.isNOfAKind(3,fourOfAKind,true));
		Assert.assertEquals(true ,yg.isNOfAKind(4,fourOfAKind,true));
		Assert.assertEquals(true ,yg.isNOfAKind(4,fourOfAKind,false));
		Assert.assertEquals(true ,yg.isNOfAKind(3,threeOfAKind,true));
		Assert.assertEquals(true ,yg.isNOfAKind(3,threeOfAKind,false));	
		Assert.assertEquals(false ,yg.isNOfAKind(4,threeOfAKind,true));
		Assert.assertEquals(false ,yg.isNOfAKind(4,threeOfAKind,false));
	}

	@Test 
	public void isDiceValidForCategoryTest() {
		// there are 16 categories where a player can score into 

		// dice_example1 :should be valid for category : 1,2,3,4,12,15
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,1));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,2));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,3));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,4));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,5));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,6));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,15));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,14));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,10));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,11));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,9));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,8));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,7));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,13));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example1,12));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,16));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,17));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example1,-200));

		// dice_example2 :should be valid for category : 1,2,3,4,5,12,13,15
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,1));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,2));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,3));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,4));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,5));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,6));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,15));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,14));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,10));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,11));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,9));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,8));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,7));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,13));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example2,12));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,16));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,17));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example2,-200));

		// dice_example3 :should be valid for category : 1,9,10,11,14,15
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example3,1));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,2));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,3));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,4));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,5));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,6));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,15));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,14));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example3,10));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example3,11));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,9));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,8));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,7));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example3,13));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example3,12));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,16));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,17));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example3,-200));

		// dice_example4 :should be valid for category : 2,3,9,11,15
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,1));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example4,2));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example4,3));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,4));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,5));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,6));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,15));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,14));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example4,10));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,11));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example4,9));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,8));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,7));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,13));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example4,12));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,16));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,17));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example4,-200));

		// dice_example5 :should be valid for category : 1,9,10,11,14,15
		// {6,6,6,6,5};
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,1));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,2));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,3));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,4));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example5,5));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example5,6));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,15));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,14));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example5,10));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example5,11));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,9));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,8));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,7));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,13));
		Assert.assertEquals(true,yg.isDiceValidForCategory(dice_example5,12));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,16));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,17));
		Assert.assertEquals(false,yg.isDiceValidForCategory(dice_example5,-200));
	}

	@Test 
	public void TestIsUpperScoreComplete() {
		// fill an arbitrary score card
		yg.scoreCard = new int[17][3];
		// complete upper score with player 1
		yg.scoreCard[1][1] = 20;
		yg.scoreCard[2][1] = 30;
		yg.scoreCard[3][1] = 40;
		yg.scoreCard[4][1] = 50;
		yg.scoreCard[5][1] = 60;
		yg.scoreCard[6][1] = 60;
		Assert.assertEquals(true,yg.isUpperScoreComplete(1));

		// not complete upper score with player 2 
		yg.scoreCard[1][2] = 20;
		yg.scoreCard[2][2] = 0;
		yg.scoreCard[3][2] = 40;
		yg.scoreCard[4][2] = 0;
		yg.scoreCard[5][2]= 60;
		yg.scoreCard[6][2] = 60;
		Assert.assertEquals(false,yg.isUpperScoreComplete(2));
	}

	@Test
	public void TestUpdateScore() {
		yg.scoreCard = new int[17][3];
		yg.updateScore(1, 1, 20);  // ACES
		yg.updateScore(2, 1, 30);  // TWOS
		Assert.assertEquals(20,yg.scoreCard[1][1]);
		Assert.assertEquals(30,yg.scoreCard[1][2]);


		yg.updateScore(1, 13, 20); // YAHTZEE for player1
		Assert.assertEquals(20,yg.scoreCard[13][1]);

		yg.additionalYahtzee = true; // additional yahtze scored
		// score in category CHANCE and add 100 for YAHTZEE for player 2
		yg.updateScore(1, 12, 10); 
		Assert.assertEquals(120,yg.scoreCard[13][1]);
	}

	@Test
	public void TestCalculateCategoryScore() {
		// dice_example 1 will go into category 4 (fours)
		Assert.assertEquals(8,yg.calculateCategoryScore(4, dice_example1));
		// dice_example 2 will go into category 13 (large straight)
		Assert.assertEquals(40,yg.calculateCategoryScore(7, dice_example2));
		// dice_example 3 will go into category 14 (yahtzee)
		Assert.assertEquals(50,yg.calculateCategoryScore(13, dice_example3));
		// dice_example 4 will go into category 11 (full house)
		Assert.assertEquals(25,yg.calculateCategoryScore(9, dice_example4)); //25
		// dice_example 5 will go into category 10 (four of a kind) 
		Assert.assertEquals(29,yg.calculateCategoryScore(11, dice_example5));

		// if a dice has isValid = false 
		// for example : a large straight can not get into full house
		Assert.assertEquals(0,yg.calculateCategoryScore(9, dice_example2));
	}


	/**
	 * Sums a set of scores on the scorecard.
	 */
	@Test
	public void TestSumScores() {
		yg.scoreCard = new int[17][3];
		// complete upper score with player 1
		yg.scoreCard[1][1] = 20;
		yg.scoreCard[2][1] = 30;
		yg.scoreCard[3][1] = 40;
		yg.scoreCard[4][1] = 50;
		yg.scoreCard[5][1] = 60;
		yg.scoreCard[6][1] = 60;
		yg.scoreCard[9][1] = 72;
		yg.scoreCard[10][1] = 22;
		yg.scoreCard[11][1] = 12;
		yg.scoreCard[12][1] = 43;
		yg.scoreCard[13][1] = 11;
		yg.scoreCard[14][1] = 72;
		yg.scoreCard[15][1] = 20;

		// upper score 
		Assert.assertEquals(260,yg.sumScores(1, 1, 6));

		// lower score 
		Assert.assertEquals(252,yg.sumScores(1, 9, 15));
	}
	
	/**
	 * tests if upper bonus is added when the sum of upper section scores is more than 63
	 */
	@Test
	public void testAddUpperBonus() {
		yg = new Yahtzee();
		int category = YahtzeeConstants.ONES;
		int player = yg.createPlayer("Moe");
		
		yg.updateScore(player, category, 10);
		yg.addUpperBonus(player, category);
		assertEquals(0, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
		
		category = YahtzeeConstants.TWOS;
		yg.updateScore(player, category, 10);
		yg.addUpperBonus(player, category);
		assertEquals(0, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
		
		category = YahtzeeConstants.THREES;
		yg.updateScore(player, category, 10);
		yg.addUpperBonus(player, category);
		assertEquals(0, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
		
		category = YahtzeeConstants.FOURS;
		yg.updateScore(player, category, 10);
		yg.addUpperBonus(player, category);
		assertEquals(0, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
		
		category = YahtzeeConstants.FIVES;
		yg.updateScore(player, category, 10);
		yg.addUpperBonus(player, category);
		assertEquals(0, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
		
		category = YahtzeeConstants.SIXES;
		yg.updateScore(player, category, 13);
		yg.addUpperBonus(player, category);
		assertEquals(35, yg.sumScores(player, YahtzeeConstants.UPPER_BONUS, YahtzeeConstants.UPPER_BONUS));
	}
	
	/**
	 * tests if reroll all dice returns another dice
	 */
	@Test
	public void testRerollAll() {

		int[] dice_example1Copy = Arrays.copyOf(dice_example1, 5);
		int[] dice_example2Copy = Arrays.copyOf(dice_example2, 5);
		int[] dice_example3Copy = Arrays.copyOf(dice_example3, 5);
		int[] dice_example4Copy = Arrays.copyOf(dice_example4, 5);
		int[] dice_example5Copy = Arrays.copyOf(dice_example5, 5);
		
		assertNotSame(dice_example1Copy, yg.rerollAll(dice_example1));
		assertNotSame(dice_example2Copy, yg.rerollAll(dice_example2));
		assertNotSame(dice_example3Copy, yg.rerollAll(dice_example3));
		assertNotSame(dice_example4Copy, yg.rerollAll(dice_example4));
		assertNotSame(dice_example5Copy, yg.rerollAll(dice_example5));
	}
	
	/**
	 * tests if reroll some dice works correctly
	 */
	@Test
	public void testRerollSome() {
		int[] dice_example1Copy = Arrays.copyOf(dice_example1, 5);
		int[] dice = yg.rerollSome("1", dice_example1);
		for (int i = 0; i < 5; i++ ) {
			System.out.println(i + ": " + dice_example1Copy[i]);
		}
		for (int i = 0; i < 5; i++ ) {
			System.out.println(i + ": " + dice[i]);
		}
		assertEquals(dice_example1Copy[0], dice[0]);
		assert(!dice_example1Copy.equals(dice));
		
		int[] dice_example2Copy = Arrays.copyOf(dice_example2, 5);
		dice = yg.rerollSome("1 3 5", dice_example2);
		assertEquals(dice_example2[0], dice[0]);
		assertEquals(dice_example2[2], dice[2]);
		assertEquals(dice_example2[4], dice[4]);
		assert(!dice_example2Copy.equals(dice));
		
		dice = yg.rerollSome("1 2 3 4 5", dice_example3);
		assertEquals(dice_example3, dice);
	}
}
