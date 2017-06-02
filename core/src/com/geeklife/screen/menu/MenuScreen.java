package com.geeklife.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.geeklife.SimpleSnake;
import com.geeklife.assets.AssetDescriptors;
import com.geeklife.assets.ButtonStyleNames;
import com.geeklife.assets.RegionNames;
import com.geeklife.config.GameConfig;
import com.geeklife.screen.game.GameScreen;
import com.geeklife.util.GdxUtils;

/**
 * Created by cle99 on 26/05/2017.
 */

public class MenuScreen extends ScreenAdapter {

    // -- attributes --
    private final SimpleSnake game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private Skin skin;
    private Stage stage;

    // -- constrictors --
    public MenuScreen( SimpleSnake game ) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    // -- private methods --

    private Actor createUI() {
        Table table = new Table( skin );
        table.defaults().pad( 10f );

        TextureRegion backgroundRegion = gameplayAtlas.findRegion( RegionNames.BACKGROUND );
        table.setBackground( new TextureRegionDrawable( backgroundRegion ) );

        Image titleImage = new Image( skin, RegionNames.TITLE );
        Button playButton = new Button( skin, ButtonStyleNames.PLAY );
        playButton.addListener( new ChangeListener() {
            @Override
            public void changed( ChangeEvent event, Actor actor ) {
                play();
            }
        } );

        Button quitButton = new Button( skin, ButtonStyleNames.QUIT );
        quitButton.addListener( new ChangeListener() {
            @Override
            public void changed( ChangeEvent event, Actor actor ) {
                quit();
            }
        } );

        table.add( titleImage ).row();
        table.add( playButton ).row();
        table.add( quitButton );

        table.center();
        table.setFillParent( true );
        table.pack();

        return table;

    }

    private void play() {
        game.setScreen( new GameScreen( game ) );
    }

    private void quit() {
        Gdx.app.exit();
    }


    // -- public methods --


    @Override
    public void show() {
        viewport = new FitViewport( GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT );
        stage = new Stage( viewport, game.getSb() );
        skin = assetManager.get( AssetDescriptors.UI_SKIN );
        gameplayAtlas = assetManager.get( AssetDescriptors.GAME_PLAY );

        Gdx.input.setInputProcessor( stage );
        stage.addActor( createUI() );
    }

    @Override
    public void resize( int width, int height ) {
        viewport.update( width, height, true );
    }

    @Override
    public void render( float delta ) {
        GdxUtils.clearScreen();

        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
