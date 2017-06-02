package com.geeklife.entity;

import com.geeklife.config.GameConfig;

/**
 * Created by cle99 on 16/05/2017.
 */

public class SnakeHead extends EntityTemplate {

    public SnakeHead() {
        setSize( GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE );
        setPosition( GameConfig.SNAKE_START_X, GameConfig.SNAKE_START_Y );
    }

    public void updateX( float amount ) {
        x += amount;
        updateBounds();
    }

    public void updateY( float amount ) {
        y += amount;
        updateBounds();
    }

}
