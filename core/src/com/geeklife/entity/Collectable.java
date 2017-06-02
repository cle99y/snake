package com.geeklife.entity;

import com.geeklife.config.GameConfig;

/**
 * Created by cle99 on 16/05/2017.
 */

public class Collectable extends EntityTemplate {

    private boolean available;
    private TokenType tokenType;

    public Collectable() {
        setSize( GameConfig.COIN_SIZE, GameConfig.COIN_SIZE );

    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable( boolean available ) {
        this.available = available;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType( TokenType tokenType ) {
        this.tokenType = tokenType;
    }

    public boolean isCoin(){
        return (tokenType == TokenType.COIN || tokenType == TokenType.LOSE_TAIL);
    }
}
