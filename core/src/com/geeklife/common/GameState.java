package com.geeklife.common;

/**
 * Created by cle99 on 16/05/2017.
 */

public enum GameState {

    READY,
    PLAYING,
    GAME_OVER;

    public boolean isReady() {
        return this == READY;
    }

    public boolean isPlaying() {
        return this == PLAYING;
    }

    public boolean isGameOver() {
        return this == GAME_OVER;
    }
}
