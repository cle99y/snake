package com.geeklife.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by cle99 on 16/05/2017.
 */

public class GameManager {

    public final static GameManager INSTANCE = new GameManager();

    private static final String HI_SCORE_KEY = "hiScore";

    private GameState state = GameState.READY;

    private int score;
    private int displayScore;
    private int highScore;
    private int displayHighScore;

    private Preferences prefs;

    public GameManager() {
        prefs = Gdx.app.getPreferences( "snakeGame" );
        highScore = prefs.getInteger( HI_SCORE_KEY, 0 );
        displayHighScore = highScore;
    }

    public void updateHighScore() {
        if ( score < displayHighScore ) {
            return;
        }

        highScore = score;
        prefs.putInteger( HI_SCORE_KEY, score );
        prefs.flush();
    }

    public boolean isReady() {
        return state.isReady();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public boolean isGameOver() {
        return state.isGameOver();
    }

    public void setGameState( GameState gameState ) {
        state = gameState;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public int getDisplayHighScore() {
        return displayHighScore;
    }

    public void incrementScore( int amount ) {
        score += amount;

        if ( score > highScore ) {
            highScore = score;
        }
    }

    public void updateDisplayScore( float delta ) {
        if ( displayScore < score ) {
            displayScore = Math.min( score, displayScore + ( int ) ( 100 * delta ) );

            if ( displayHighScore < highScore ) {
                displayHighScore = Math.min( highScore, displayHighScore + ( int ) ( 100 * delta ) );
            }
        }
    }

    public void reset() {
        setGameState( GameState.PLAYING );
        score = 0;
        displayScore = 0;
    }

}
