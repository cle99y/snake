package com.geeklife.entity;

import com.badlogic.gdx.utils.Array;
import com.geeklife.config.GameConfig;

/**
 * Created by cle99 on 16/05/2017.
 */

public class Snake {

    private final Array<SnakeBodySegment> snakeBodySegments = new Array<SnakeBodySegment>();
    private SnakeHead snakeHead;
    private Direction direction = Direction.RIGHT;

    private float xBeforeUpdate;
    private float yBeforeUpdate;

    public Snake() {
        snakeHead = new SnakeHead();
    }

    public void move() {
        xBeforeUpdate = snakeHead.getX();
        yBeforeUpdate = snakeHead.getY();

        if ( direction.isRIGHT() ) {
            snakeHead.updateX( GameConfig.SNAKE_MOVE_SPEED );
        } else if ( direction.isLEFT() ) {
            snakeHead.updateX( -GameConfig.SNAKE_MOVE_SPEED );
        } else if ( direction.isUP() ) {
            snakeHead.updateY( GameConfig.SNAKE_MOVE_SPEED );
        } else if ( direction.isDOWN() ) {
            snakeHead.updateY( -GameConfig.SNAKE_MOVE_SPEED );
        }
        updateSnakeBodySegments();

    }

    public void setDirection( Direction newDirection ) {

        if ( !direction.isOpposite( newDirection ) || snakeBodySegments.size == 0 ) {
            direction = newDirection;
        }
    }

    public SnakeHead getSnakeHead() {
        return snakeHead;
    }

    private void updateSnakeBodySegments() {

        if ( snakeBodySegments.size > 0 ) {
            SnakeBodySegment tail = snakeBodySegments.removeIndex( 0 );
            tail.setPosition( xBeforeUpdate, yBeforeUpdate );
            snakeBodySegments.add( tail );
        }
    }

    public Array<SnakeBodySegment> getSnakeBodySegments() {
        return snakeBodySegments;
    }

    public void insertSnakeBodySegment() {
        SnakeBodySegment segment = new SnakeBodySegment();
        segment.setPosition( snakeHead.getX(), snakeHead.getY() );
        snakeBodySegments.insert( 0, segment );
    }

    public void reset() {
        snakeBodySegments.clear();
        direction = Direction.RIGHT;
        snakeHead.setPosition( 1, 1 );
    }
}
