package com.geeklife.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Logger;
import com.geeklife.SimpleSnake;
import com.geeklife.assets.AssetDescriptors;
import com.geeklife.collisions.CollisionListener;
import com.geeklife.common.GameManager;
import com.geeklife.common.GameState;
import com.geeklife.config.GameConfig;
import com.geeklife.entity.Snake;
import com.geeklife.screen.menu.MenuScreen;
import com.geeklife.util.ViewportUtils;

/**
 * Created by cle99 on 15/05/2017.
 */

public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger( ViewportUtils.class.getName(), Logger.DEBUG );

    // -- attributes --

    private final SimpleSnake game;
    private final AssetManager assetManager;
    private Snake snake;


    private GameController controller;
    private GameRenderer renderer;
    private GameManager GM = GameManager.INSTANCE;

    private Sound coinSound, loseSound, loseTailSound, speedUpSound, speedDownSound;

    private CollisionListener listener;

    // -- constrictors --

    public GameScreen( SimpleSnake game ) {
        this.game = game;
        assetManager = game.getAssetManager();
        listener = new CollisionListener() {
            @Override
            public void hitCoin() {
                coinSound.play();
                snake.insertSnakeBodySegment();
                GM.incrementScore( GameConfig.COIN_SCORE_VALUE );
            }

            @Override
            public void loseTail() {
                if ( snake.getSnakeBodySegments().size > 0 ) {
                    loseTailSound.play();
                    snake.getSnakeBodySegments().removeIndex( 0 );
                    GM.incrementScore( GameConfig.LOSE_TAIL_SCORE_VALUE );
                }

            }

            @Override
            public void speedUp() {
                speedUpSound.play();
                float speed = controller.getSnake_move_time() - GameConfig.SNAKE_SPEED_DELTA;
                controller.setSnake_move_time( speed );
                GM.incrementScore( GameConfig.SPEED_DELTA_SCORE_VALUE );
            }

            @Override
            public void speedDown() {
                speedDownSound.play();
                float speed = controller.getSnake_move_time() + GameConfig.SNAKE_SPEED_DELTA;
                controller.setSnake_move_time( speed );
                GM.incrementScore( GameConfig.SPEED_DELTA_SCORE_VALUE );
            }

            @Override
            public void loseGame() {
                loseSound.play();
            }
        };
    }

    @Override
    public void show() {
        coinSound = assetManager.get( AssetDescriptors.COIN_SOUND );
        loseSound = assetManager.get( AssetDescriptors.LOSE_SOUND );
        loseTailSound = assetManager.get(AssetDescriptors.LOSE_TAIL_SOUND);
        speedUpSound = assetManager.get(AssetDescriptors.SPEED_UP_SOUND);
        speedDownSound = assetManager.get(AssetDescriptors.SPEED_DOWN_SOUND);

        controller = new GameController( listener );
        snake = controller.getSnake();
        renderer = new GameRenderer( game.getSb(), assetManager, controller );
    }

    @Override
    public void render( float delta ) {

        controller.update( delta );
        renderer.render( delta );

        if ( GameManager.INSTANCE.isGameOver() ) {
            GameManager.INSTANCE.setGameState( GameState.READY );
            game.setScreen( new MenuScreen( game ) );
        }
    }

    @Override
    public void resize( int width, int height ) {
        renderer.rezize( width, height );
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
