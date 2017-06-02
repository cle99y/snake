package com.geeklife.entity;

/**
 * Created by cle99 on 16/05/2017.
 */

public enum Direction {

    UP,
    RIGHT,
    DOWN,
    LEFT;

    public boolean isUP() {
        return this == UP;
    }

    public boolean isRIGHT() {
        return this == RIGHT;
    }

    public boolean isDOWN() {
        return this == DOWN;
    }

    public boolean isLEFT() {
        return this == LEFT;
    }

    public Direction getOpposite() {
        if ( isDOWN() ) {
            return UP;
        } else if ( isUP() ) {
            return DOWN;
        } else if ( isLEFT() ) {
            return RIGHT;
        } else if ( isRIGHT() ) {
            return LEFT;
        }

        throw new IllegalArgumentException( "Direction not valid " + this );
    }

    public boolean isOpposite( Direction direction ) {
        return getOpposite() == direction;
    }
}
