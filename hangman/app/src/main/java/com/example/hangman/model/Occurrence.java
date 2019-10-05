package com.example.hangman.model;

import java.util.ArrayList;

public class Occurrence {

    private boolean isContained;
    private int numOccurrence;
    private ArrayList<Integer> occurList;

    public Occurrence() {

        this.isContained = false;
        this.numOccurrence = 0;
        this.occurList = null;
    }

    public void addOccurrence(int index) {

        if (!this.isContained) {
            this.isContained = true;
            this.occurList = new ArrayList<>();
        }
        this.numOccurrence++;
        this.occurList.add(index);
    }

    public boolean isContained() {
        return isContained;
    }

    public int getNumOccurrence() {
        return numOccurrence;
    }

    public ArrayList<Integer> getOccurList() {
        return occurList;
    }

}
