package com.geeklife.config;

/**
 * Created by cle99 on 15/05/2017.
 */

public class GameConfig {

    public static final float WIDTH = 1200f;    // pixels
    public static final float HEIGHT = 720f;    // pixels

    public static final float HUD_WIDTH = 1200f;
    public static final float HUD_HEIGHT = 720f;

    public static final float WORLD_WIDTH = 27f;    // world units
    public static final float WORLD_HEIGHT = 15f;   // world units

    public static final float PROTECTED_HUD = 2f;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2f;    // world units
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2f;   // world units

    public static final float SNAKE_SIZE = 1;
    public static final float SNAKE_MOVE_SPEED = 1f;
    public static final float SNAKE_MOVE_TIME = 0.2f;
    public static final float SNAKE_SPEED_DELTA = SNAKE_MOVE_TIME / 5f;
    public static final int SNAKE_START_X = 1;
    public static final int SNAKE_START_Y = 1;

    public static final float COIN_SIZE = 1f;
    public static final int COIN_SCORE_VALUE = 20;
    public static final int LOSE_TAIL_SCORE_VALUE = 50;
    public static final int SPEED_DELTA_SCORE_VALUE = 10;


    // token type probability
    public static final float pCOIN = 0.7f;
    public static final float pSPEED_UP = 0.5f;



    public static final float SPEED_TOKEN_SPAWN_TIME = 4.0f;



    private GameConfig() {


    }
}
