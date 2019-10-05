package com.example.hangman.model;

import java.util.ArrayList;

public class Letter extends Occurrence {

    private char letter;
    private boolean isPicked;

    public Letter(char letter) {
        super();
        this.letter = letter;
        this.isPicked = false;
    }

    public char getLetter() {
        return letter;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked() {
        isPicked = true;
    }

    public Occurrence getOccurrence() {
        return this;
    }
}
