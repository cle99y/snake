package com.geeklife.entity;

import com.geeklife.config.GameConfig;

/**
 * Created by cle99 on 16/05/2017.
 */

public class SnakeBodySegment extends EntityTemplate {

    public SnakeBodySegment() {
        setSize( GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE );
    }
}
