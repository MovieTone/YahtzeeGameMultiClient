package com.movietone;

/*
 * File: YahtzeeConstants.java
 * ---------------------------
 * This file declares several constants that are shared by the
 * different modules in the Yahtzee game.
 */

public interface YahtzeeConstants {

    /**
     * The number of dice in the game
     */
    int N_DICE = 5;

    /**
     * The maximum number of players
     */
    int MAX_PLAYERS = 3;

    /**
     * The number of times a player is allowed to roll the dice
     */
    int MAX_ROLLS = 3;

    /**
     * The total number of categories
     */
    int N_CATEGORIES = 17;

    /**
     * The number of categories in which the player can score
     */
    int N_SCORING_CATEGORIES = 13;

    /**
     * The constants that specify categories on the scoresheet
     */
    int ONES = 1;
    int TWOS = 2;
    int THREES = 3;
    int FOURS = 4;
    int FIVES = 5;
    int SIXES = 6;
    int LARGE_STRAIGHT = 7;
    int SMALL_STRAIGHT = 8;
    int FULL_HOUSE = 9;
    int THREE_OF_A_KIND = 10;
    int FOUR_OF_A_KIND = 11;
    int CHANCE = 12;
    int YAHTZEE = 13;
    int UPPER_BONUS = 14;
    int UPPER_SCORE = 15;
    int LOWER_SCORE = 16;
    int TOTAL = 17;

    /**
     * The score values for certain categories that have static values
     */
    int FULL_HOUSE_SCORE = 25;
    int SMALL_STRAIGHT_SCORE = 30;
    int LARGE_STRAIGHT_SCORE = 40;
    int YAHTZEE_SCORE = 50;
    int YAHTZEE_BONUS_SCORE = 100;
    int UPPER_BONUS_SCORE = 35;
    int UPPER_SECTION_SUM_BONUS = 63;

    int NUMBER_OF_ROUNDS = 13;

}