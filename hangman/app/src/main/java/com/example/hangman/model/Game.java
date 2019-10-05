package com.example.hangman.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Game {

    public MutableLiveData<Integer> winner = new MutableLiveData<>();

    private Alphabet alphabet;
    private String word;
    private SecretKeeper secretKeeper;

    public int guesses = 0;
    private int wordLength = 0;
    private int numLettersGuessed = 0;

    public static final int SECRET_KEEPER = 1;
    public static final int PLAYER = 2;
    public static final int NO_WINNER = 0;
    public static final int MAX_GUESSES = 6;

    public Game(String wordData) {

        secretKeeper = new SecretKeeper(wordData);
        word = secretKeeper.pickWord();
        wordLength = word.length();
        alphabet = new Alphabet(word);
        winner.setValue(NO_WINNER);
    }

    public Occurrence pickLetter(char letter) {

        int index;
        Letter pickedLetter;

        index = Alphabet.getIndex(letter);
        pickedLetter = alphabet.alphabet[index];
        if (pickedLetter.isPicked())
            return null;
        pickedLetter.setPicked();

        if (pickedLetter.isContained()) {
            numLettersGuessed += pickedLetter.getNumOccurrence();
            if (numLettersGuessed == wordLength) {
                winner.setValue(PLAYER);
            }
            return pickedLetter.getOccurrence();
        } else {
            guesses++;
            if (guesses == MAX_GUESSES) {
                winner.setValue(SECRET_KEEPER);
            }
            return null;
        }

    }

    public String getWord() {
        return word;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    /* For Testing */
    public void printWord() {
       Log.i("Our word is ", word);
    }


    public void printAlphabet() {
        for(int i = 0; i < 26; i++) {
            int num = alphabet.alphabet[i].getNumOccurrence();
            ArrayList<Integer> occurrence = alphabet.alphabet[i].getOccurList();
            char letter = alphabet.alphabet[i].getLetter();

            Log.i(String.valueOf(i),  letter + " number of Occurrence " + num);
            for (int j = 0; j < num; j++) {
                String occur = occurrence.get(j).toString();
                Log.i("Occur of letter " + letter, occur);
            }
        }
    }

    public void newGame() {
        word = secretKeeper.pickWord();
        wordLength = word.length();
        alphabet = new Alphabet(word);
        guesses = 0;
        numLettersGuessed = 0;
        winner.setValue(NO_WINNER);
    }
}


