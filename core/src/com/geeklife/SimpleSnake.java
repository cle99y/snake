package com.geeklife;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.geeklife.screen.LoadingScreen;

public class SimpleSnake extends Game {

    // -- attributes --

    private AssetManager assetManager;
    private SpriteBatch sb;

    @Override
    public void create() {
        Gdx.app.setLogLevel( Application.LOG_DEBUG );

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel( Logger.DEBUG );

        sb = new SpriteBatch();

        setScreen( new LoadingScreen( this ) );

    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getSb() {
        return sb;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        sb.dispose();
    }
}
