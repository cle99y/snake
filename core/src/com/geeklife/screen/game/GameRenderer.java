package com.geeklife.screen.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.geeklife.assets.AssetDescriptors;
import com.geeklife.assets.RegionNames;
import com.geeklife.common.GameManager;
import com.geeklife.config.GameConfig;
import com.geeklife.entity.Collectable;
import com.geeklife.entity.Snake;
import com.geeklife.entity.SnakeBodySegment;
import com.geeklife.entity.SnakeHead;
import com.geeklife.util.GdxUtils;
import com.geeklife.util.ViewportUtils;
import com.geeklife.util.debug.DebugCameraController;

/**
 * Created by cle99 on 15/05/2017.
 */

public class GameRenderer implements Disposable {

    // -- attributes --

    private GameController controller;
    private final GameManager GM = GameManager.INSTANCE;
    private final int PADDING = 20;

    private SpriteBatch sb;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;

    private BitmapFont hudFont = new BitmapFont();
    private final GlyphLayout layout = new GlyphLayout();

    private DebugCameraController debugCameraController;

    private TextureRegion headRegion, bodyRegion;     // snake TextureRegions
    private TextureRegion background;
    private TextureRegion coinRegion;
    private TextureRegion loseTailRegion;
    private TextureRegion speedUpRegion;
    private TextureRegion speedDownRegion;

    // -- constrictors --

    public GameRenderer( SpriteBatch sb, AssetManager assetManager, GameController controller ) {
        this.controller = controller;
        this.sb = sb;
        this.assetManager = assetManager;
        init();
    }

    private void init() {
        hudFont = assetManager.get( AssetDescriptors.UI_FONT );
        camera = new OrthographicCamera();
        viewport = new FitViewport( GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera );
        hudViewport = new FitViewport( GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT );
        renderer = new ShapeRenderer();

        BitmapFont font = assetManager.get( AssetDescriptors.UI_FONT );
        TextureAtlas gameplayAtlas = assetManager.get( AssetDescriptors.GAME_PLAY );

        headRegion = gameplayAtlas.findRegion( RegionNames.SNAKE_HEAD );
        bodyRegion = gameplayAtlas.findRegion( RegionNames.SNAKE_BODY );
        coinRegion = gameplayAtlas.findRegion( RegionNames.COLLECTABLE );
        loseTailRegion = gameplayAtlas.findRegion( RegionNames.LOSE_TAIL );
        speedUpRegion = gameplayAtlas.findRegion( RegionNames.SPEED_UP );
        speedDownRegion = gameplayAtlas.findRegion( RegionNames.SPEED_DOWN );
        background = gameplayAtlas.findRegion( RegionNames.BACKGROUND );

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition( GameConfig.WORLD_CENTER_X, GameConfig
                .WORLD_CENTER_Y );


    }

    public void render( float delta ) {

        debugCameraController.handleDebugInput( delta );
        debugCameraController.applyTo( camera );

        viewport.apply();
        renderer.setProjectionMatrix( camera.combined );
        renderer.begin( ShapeRenderer.ShapeType.Line );
        GdxUtils.clearScreen();

        renderer.end();

        renderDebug();
        renderGame();
        renderHUD();
    }

    public void renderDebug() {
        ViewportUtils.drawGrid( viewport, renderer );

        Color oldColor = new Color( renderer.getColor() );

        renderer.setProjectionMatrix( camera.combined );
        renderer.begin( ShapeRenderer.ShapeType.Line );

        drawDebug();

        renderer.end();
        renderer.setColor( oldColor );

    }

    private void drawDebug() {
        Snake snake = controller.getSnake();

        renderer.setColor( Color.RED );
        for ( SnakeBodySegment segment : snake.getSnakeBodySegments() ) {
            Rectangle segmentBounds = segment.getBounds();
            renderer.rect( segmentBounds.x, segmentBounds.y, segmentBounds.width, segmentBounds
                    .height );
        }

        renderer.setColor( Color.MAGENTA );
        SnakeHead head = snake.getSnakeHead();
        Rectangle headBounds = head.getBounds();
        renderer.rect( headBounds.x, headBounds.y, headBounds.width, headBounds.height );
        renderer.setColor( Color.YELLOW );
        Collectable collectable = controller.getCollectable();

        if ( GameManager.INSTANCE.isPlaying() ) {
            Rectangle coinBounds = collectable.getBounds();
            renderer.rect( coinBounds.x, coinBounds.y, coinBounds.width, coinBounds.height );
        }
    }

    private void renderGame() {
        viewport.apply();
        sb.setProjectionMatrix( camera.combined );
        sb.begin();

        drawGamePlay();

        sb.end();
    }

    private void drawGamePlay() {

        // background
        sb.draw( background,
                0, 0,
                GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT
        );

        // collectables
        Collectable collectable = controller.getCollectable();
        //getCollectableRegion();
        if ( collectable.isAvailable() ) {
            sb.draw( getCollectableRegion(collectable),
                    collectable.getX(), collectable.getY(),
                    collectable.getWidth(), collectable.getHeight()
            );
        }

        Collectable speedToken = controller.getSpeedToken();
        if ( speedToken.isAvailable() ) {
            sb.draw( getSpeedRegion(speedToken),
                    speedToken.getX(), speedToken.getY(),
                    speedToken.getWidth(), speedToken.getHeight()
            );
        }

        // snake
        Snake snake = controller.getSnake();
        SnakeHead head = snake.getSnakeHead();

        for ( SnakeBodySegment bodySegment : snake.getSnakeBodySegments() ) {
            sb.draw( bodyRegion,
                    bodySegment.getX(), bodySegment.getY(),
                    bodySegment.getWidth(), bodySegment.getHeight()
            );
        }

        sb.draw( headRegion,
                head.getX(), head.getY(),
                head.getWidth(), head.getHeight()
        );

    }

    private TextureRegion getCollectableRegion(Collectable collectable) {
        TextureRegion region;
        switch ( collectable.getTokenType() ) {
            case COIN:
                region = coinRegion;
                break;
            case LOSE_TAIL:
                region = loseTailRegion;
                break;
            default:
                region = coinRegion;    // question: how to avoid having default that will never be used?
                break;

        }
        return region;
    }

    private TextureRegion getSpeedRegion(Collectable collectable) {
        TextureRegion region;
        switch ( collectable.getTokenType() ) {

            case SPEED_UP:
                region = speedUpRegion;
                break;
            case SPEED_DOWN:
                region = speedDownRegion;
                break;
            default:
                region = speedUpRegion;  // question: how to avoid having default that will never be used?
                break;

        }
        return region;
    }



    private void renderHUD() {
        sb.setProjectionMatrix( hudViewport.getCamera().combined );
        sb.begin();

        drawHUD();

        sb.end();
    }

    private void drawHUD() {
        String highScore = "HIGH SCORE: " + GM.getDisplayHighScore();
        layout.setText( hudFont, highScore );
        hudFont.draw( sb, layout, PADDING, GameConfig.HUD_HEIGHT - PADDING );

        String score = "SCORE: " + GM.getDisplayScore();
        layout.setText( hudFont, score );
        hudFont.draw( sb, layout, GameConfig.HUD_WIDTH - layout.width - PADDING, GameConfig
                .HUD_HEIGHT - PADDING );


    }


    public void rezize( int width, int height ) {
        viewport.update( width, height, true );
        hudViewport.update( width, height, true );
        ViewportUtils.debugPixelsPerUnit( viewport );
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
