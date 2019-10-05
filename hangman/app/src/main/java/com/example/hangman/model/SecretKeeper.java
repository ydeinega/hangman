package com.example.hangman.model;

import java.util.Random;

public class SecretKeeper {

    private String[] wordData = null;

    public SecretKeeper(String wordData) {
        this.wordData = wordData.split("\n");
    }

    public String pickWord() {

        Random rand = new Random();
        int i = rand.nextInt(wordData.length - 1);
        return wordData[i];
    }
}
