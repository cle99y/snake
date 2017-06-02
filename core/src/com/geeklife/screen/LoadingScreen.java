package com.geeklife.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.geeklife.SimpleSnake;
import com.geeklife.assets.AssetDescriptors;
import com.geeklife.config.GameConfig;
import com.geeklife.screen.menu.MenuScreen;
import com.geeklife.util.GdxUtils;

/**
 * Created by cle99 on 17/05/2017.
 */

public class LoadingScreen extends ScreenAdapter {

    public static final float PROGRESS_BAR_WIDTH = GameConfig.WIDTH;
    public static final float PROGRESS_BAR_HEIGHT = GameConfig.HEIGHT;

    // -- attributes --
    private final SimpleSnake game;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private float progress;
    private float waitTime;

    private boolean changeScreenEnabled;

    // -- constrictors --


    public LoadingScreen( SimpleSnake game ) {
        this.game = game;
        assetManager = game.getAssetManager();
        waitTime = 1f;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport( GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera );
        renderer = new ShapeRenderer();
        assetManager.load( AssetDescriptors.UI_FONT );
        assetManager.load( AssetDescriptors.GAME_PLAY );
        assetManager.load( AssetDescriptors.UI_SKIN );
        assetManager.load( AssetDescriptors.COIN_SOUND );
        assetManager.load( AssetDescriptors.LOSE_SOUND );
        assetManager.load( AssetDescriptors.LOSE_TAIL_SOUND );
        assetManager.load(AssetDescriptors.SPEED_UP_SOUND);
        assetManager.load( AssetDescriptors.SPEED_DOWN_SOUND );
    }

    @Override
    public void render( float delta ) {
        GdxUtils.clearScreen( Color.YELLOW );
        update( delta );
        draw();

        if ( changeScreenEnabled ) {
            game.setScreen( new MenuScreen( game ) );
        }
    }

    @Override
    public void resize( int width, int height ) {
        viewport.update( width, height, true );
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    private void update( float delta ) {
        progress = assetManager.getProgress();

        if ( assetManager.update() ) {
            waitTime -= delta;
        }

        if ( waitTime <= 0 ) {
            changeScreenEnabled = true;
        }
    }

    private void draw() {
        viewport.apply();
        renderer.setProjectionMatrix( camera.combined );
        renderer.begin( ShapeRenderer.ShapeType.Filled );
        renderer.setColor( Color.RED );

        renderer.rect( 0, 0, PROGRESS_BAR_WIDTH, progress * PROGRESS_BAR_HEIGHT );

        renderer.end();


    }
}
