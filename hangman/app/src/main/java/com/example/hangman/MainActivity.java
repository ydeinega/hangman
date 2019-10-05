package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.util.Log;

import com.example.hangman.databinding.ActivityMainBinding;
import com.example.hangman.databinding.ActivityMainErrorBinding;
import com.example.hangman.model.Game;
import com.example.hangman.viewmodel.GameViewModel;


public class MainActivity extends AppCompatActivity  {

    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        gameViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(GameViewModel.class);
        gameViewModel.getWordData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    gameViewModel.init(s);
                    initDataBinding();
                    setUpOnGameEndListener();
                } else {
                    initDataBindingError();
                }
            }
        });
    }

    private void initDataBinding() {

        Log.i("INIT DATA BINDING", "I AM HERE");
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setGameViewModel(gameViewModel);
    }

    private void initDataBindingError() {
        Log.i("INIT DATA BINDING ERROR", "I AM HERE");
        ActivityMainErrorBinding activityMainErrorBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_error);
        activityMainErrorBinding.setGameViewModel(gameViewModel);
    }

    private void setUpOnGameEndListener() {

        gameViewModel.getWinner().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer winner) {
                if (winner == Game.SECRET_KEEPER || winner == Game.PLAYER) {
                    gameViewModel.gameOver(winner);
                    gameViewModel.showWord();
                }
            }
        });
    }

}