package com.example.hangman.model;

public class Alphabet {

    public Letter[] alphabet;
    private final static int ASCII_DIFF = 97;

    public Alphabet(String word) {

        char letter;

        letter = 'a';
        alphabet = new Letter[26];
        for (int i = 0; i < 26; i++) {
            char currentLetter = (char)(letter + i);
            alphabet[i] = new Letter(currentLetter);
        }
        addOccurrence(word.toCharArray());
    }

    private void addOccurrence(char[] word) {

        for(int i = 0; i < word.length; i++) {
            int index = (int)(word[i] - ASCII_DIFF);
            alphabet[index].addOccurrence(i);
        }
    }

    public static int getIndex(char letter) {
        return (letter - ASCII_DIFF);
    }
}
