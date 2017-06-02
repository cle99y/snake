package com.geeklife.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by cle99 on 16/05/2017.
 */

public abstract class EntityTemplate {

    // -- attributes --
    protected float x;
    protected float y;

    protected float width = 1;
    protected float height = 1;

    protected Rectangle bounds;

    // -- constrictors --

    public EntityTemplate() {
        bounds = new Rectangle( x, y, width, height );
    }

    public void setPosition( float x, float y ) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void setSize( float width, float height ) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public float getX() {
        return x;
    }

    public void setX( float x ) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY( float y ) {
        this.y = y;
        updateBounds();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    protected void updateBounds() {
        bounds.setPosition( x, y );
        bounds.setSize( width, height );
    }
}
