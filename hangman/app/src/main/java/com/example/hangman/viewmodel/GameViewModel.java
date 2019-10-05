package com.example.hangman.viewmodel;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hangman.R;
import com.example.hangman.model.Game;
import com.example.hangman.model.Occurrence;
import com.example.hangman.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class GameViewModel extends AndroidViewModel {

    private WordLiveData wordData;

    public ObservableArrayMap<String, Integer> cells = new ObservableArrayMap<>();
    public ObservableField<String> word = new ObservableField<>();
    public ObservableField<String> guesses = new ObservableField<>();
    public ObservableField<Integer> image = new ObservableField<>();
    public ObservableField<Integer> alphabetVisibility = new ObservableField<>();
    public ObservableField<Integer> messageVisibility = new ObservableField<>();
    public ObservableField<String> message = new ObservableField<>();

    private Game game;
    private char[] wordToShow;

    private final int COLOR_NOT_PICKED;
    private final int COLOR_PICKED;
    private final Integer[] IMAGE = {
            R.drawable.hangman_0,
            R.drawable.hangman_1,
            R.drawable.hangman_2,
            R.drawable.hangman_3,
            R.drawable.hangman_4,
            R.drawable.hangman_5,
            R.drawable.hangman_6
    };
    private final int VISIBLE = 0;
    private final int INVISIBLE = 4;

    private final String MESSAGE_WIN = "You won!";
    private final String MESSAGE_LOSE = "You lost!";
    private final String MESSAGE_ERROR = "Couldn't load data from the Internet.";

    public boolean errorOccurred = false;


    public GameViewModel(Application application) {
        super(application);
        wordData = new WordLiveData(application);
        COLOR_NOT_PICKED = application.getColor(R.color.colorPrimary);
        COLOR_PICKED = application.getColor(R.color.colorGray);
    }

    public LiveData<String> getWordData() {
        return wordData;
    }

    public class WordLiveData extends LiveData<String> {
        private final Context context;

        public WordLiveData(Context context) {
            this.context = context;
            loadData();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadData() {
            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... voids) {
                    URL url = NetworkUtilities.getURL();
                    try {
                        String wordData = NetworkUtilities.getResponseFromHttp(url);
                        return wordData;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {

                    Log.i("ON POST EXECUTE", "I AM HERE");
                    if (s == null) {
                        showError();
                        setValue(null);
                    }
                    else
                        setValue(s);
                }
            }.execute();
        }
    }

    public void init(String wordData) {

        game = new Game(wordData);
        initWord(game.getWord());
        initCells();
        initGuesses();
        setImage(0);
        initVisibility();
    }

    private void initWord(String word) {

        int length;

        length = word.length() * 2;
        wordToShow = new char[length + 1];
        wordToShow[length] = '\0';
        for (int i = 0; i < length; i += 2) {

            wordToShow[i] = '_';
            if (i + 1 != length)
                wordToShow[i + 1] = ' ';
        }
        this.word.set(String.valueOf(wordToShow));
    }

    private void initCells() {

        char letter;

        letter = 'a';
        for (int i = 0; i < 26; i++) {
            char currentLetter = (char)(letter + i);
            cells.put(String.valueOf(currentLetter), COLOR_NOT_PICKED);
        }
    }

    private void initGuesses() {

        guesses.set("0/6");
    }

    private void initVisibility() {

        alphabetVisibility.set(VISIBLE);
        messageVisibility.set(INVISIBLE);
    }

    public void onClickedCell(char letter) {

        Occurrence occurrence;

        occurrence= game.pickLetter(letter);
        if (game.guesses <= 6) {
            cells.remove(String.valueOf(letter));
            cells.put(String.valueOf(letter), COLOR_PICKED);
            if (occurrence != null) {
                showLetter(letter, occurrence);
            } else {
                guesses.set(game.guesses + "/" + Game.MAX_GUESSES);
                setImage(game.guesses);
            }
            if (game.guesses == 6) {
                game.guesses++;
            }
        }
    }

    private void setImage(int guesses) {
        image.set(IMAGE[guesses]);
    }

    private void showLetter(char letter, Occurrence occurrence) {

        ArrayList<Integer> occurList = occurrence.getOccurList();
        int numOccurrence = occurrence.getNumOccurrence();

        for (int i = 0; i < numOccurrence; i++) {
            int index = occurList.get(i) * 2;
            wordToShow[index] = letter;
        }
        word.set(String.valueOf(wordToShow));
    }

    public void showWord() {

        String word;
        word = game.getWord();
        for (int i = 0; i < word.length(); i++) {
           wordToShow[i * 2] = word.charAt(i);
        }
        this.word.set(String.valueOf(wordToShow));
    }

    public void gameOver(int winner) {

        if (winner == Game.PLAYER)
            message.set(MESSAGE_WIN);
        else
            message.set(MESSAGE_LOSE);
        alphabetVisibility.set(INVISIBLE);
        messageVisibility.set(VISIBLE);
    }

    public LiveData<Integer> getWinner() { return game.winner; }

    public void newGame() {

        game.newGame();
        initWord(game.getWord());
        initCells();
        initGuesses();
        setImage(0);
        initVisibility();
    }

    public void showError() {
        Log.i("SHOW ERROR", "I AM HERE");
        errorOccurred = true;
        message.set(MESSAGE_ERROR);
    }
}
