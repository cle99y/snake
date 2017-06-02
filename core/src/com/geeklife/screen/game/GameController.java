package com.geeklife.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;
import com.geeklife.collisions.CollisionListener;
import com.geeklife.common.GameManager;
import com.geeklife.common.GameState;
import com.geeklife.config.GameConfig;
import com.geeklife.entity.Collectable;
import com.geeklife.entity.Direction;
import com.geeklife.entity.Snake;
import com.geeklife.entity.SnakeBodySegment;
import com.geeklife.entity.SnakeHead;
import com.geeklife.entity.TokenType;
import com.geeklife.util.ViewportUtils;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by cle99 on 15/05/2017.
 */

public class GameController {

    private static final Logger log = new Logger( ViewportUtils.class.getName(), Logger.DEBUG );

    private final GameManager GM = GameManager.INSTANCE;
    private final CollisionListener listener;

    private Snake snake;
    private float timer;

    private Collectable collectable;
    private Collectable speedToken;
    private float coinTimer;
    private final TokenType[] speedTokens = {TokenType.SPEED_UP, TokenType.SPEED_DOWN};
    private boolean gameOver;

    private float snake_move_time;

    public GameController( CollisionListener listener ) {
        this.listener = listener;
        snake = new Snake();
        collectable = new Collectable();
        speedToken = new Collectable();

        // set default TYPES
        collectable.setTokenType( TokenType.COIN );
        speedToken.setTokenType( TokenType.SPEED_UP );

        snake_move_time = GameConfig.SNAKE_MOVE_TIME;
        coinTimer = 0;
    }

    public void update( float delta ) {

        GM.updateDisplayScore( delta );

        if ( GM.isPlaying() ) {
            pollInput();
            addDebugSegments();

            timer += delta;
            coinTimer += delta;
            if ( timer > snake_move_time ) {
                snake.move();
                timer = 0;

                keepInBounds();
                doesSnakeBiteItselfOrCoin();

            }

            spawnItem( collectable );

            // spawn speed token after set period
            log.debug( "coin time = " + coinTimer );
            if ( coinTimer >= GameConfig.SPEED_TOKEN_SPAWN_TIME ) {
                speedToken.setAvailable( false );
                // randomly choose 'speed up' or 'speed down' token type
                int speedSelector = MathUtils.random( 0, 1 );
                speedToken.setTokenType( speedTokens[speedSelector] );
                spawnItem( speedToken );
                // reset timer
                coinTimer = 0;
            }

        } else {
            restart();
        }
    }

    public void addDebugSegments() {
        if ( Gdx.input.isKeyJustPressed( Input.Keys.EQUALS ) ) {
            snake.insertSnakeBodySegment();
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public Collectable getCollectable() {
        return collectable;
    }

    public Collectable getSpeedToken() {
        return speedToken;
    }

    public float getSnake_move_time() {
        return snake_move_time;
    }

    public void setSnake_move_time( float snake_move_time ) {
        this.snake_move_time = snake_move_time;
    }

    // -- private methods --

    private void pollInput() {

        boolean isRightPressed = Gdx.input.isKeyJustPressed( Input.Keys.RIGHT );
        boolean isLeftPressed = Gdx.input.isKeyJustPressed( Input.Keys.LEFT );
        boolean isUpPressed = Gdx.input.isKeyJustPressed( Input.Keys.UP );
        boolean isDownPressed = Gdx.input.isKeyJustPressed( Input.Keys.DOWN );

        if ( isRightPressed ) {
            snake.setDirection( Direction.RIGHT );
        } else if ( isLeftPressed ) {
            snake.setDirection( Direction.LEFT );
        } else if ( isDownPressed ) {
            snake.setDirection( Direction.DOWN );
        } else if ( isUpPressed ) {
            snake.setDirection( Direction.UP );
        }

    }

    private void keepInBounds() {

        SnakeHead snakeHead = snake.getSnakeHead();
        if ( snakeHead.getX() >= GameConfig.WORLD_WIDTH ) {
            snakeHead.setPosition( 0f, snakeHead.getY() );
        } else if ( snakeHead.getX() < 0f ) {
            snakeHead.setPosition( GameConfig.WORLD_WIDTH - GameConfig.SNAKE_SIZE, snakeHead.getY() );
        } else if ( snakeHead.getY() >= GameConfig.WORLD_HEIGHT - GameConfig.PROTECTED_HUD ) {
            snakeHead.setPosition( snakeHead.getX(), 0f );
        } else if ( snakeHead.getY() < 0f ) {
            snakeHead.setPosition( snakeHead.getX(), GameConfig.WORLD_HEIGHT -
                    GameConfig.PROTECTED_HUD - GameConfig.SNAKE_SIZE );
        }
    }

    private void spawnItem( Collectable collectable ) {

        if ( !collectable.isAvailable() ) {
            // set coin collectables TYPE: if coin and has length
            if ( collectable.isCoin() && snake.getSnakeBodySegments().size > 0) {
                collectable.setTokenType( getNewTokenType() );
            }

            do {
                float coinX = random( ( int ) ( GameConfig.WORLD_WIDTH - GameConfig.COIN_SIZE ) );
                float coinY = random( ( int ) ( GameConfig.WORLD_HEIGHT - GameConfig.COIN_SIZE
                        - GameConfig.PROTECTED_HUD
                ) );
                collectable.setAvailable( true );
                collectable.setPosition( coinX, coinY );
            } while ( collectablesDoNotOverlap() );  // todo: avoid other tokens as well
        }

    }

    private TokenType getNewTokenType() {
        float selector = random();
        log.debug( "random = " + selector );

        // only if snake has grown larger than three segments, select token type
        if ( snake.getSnakeBodySegments().size > 3 ) {
            if ( selector <= GameConfig.pCOIN ) {
                return TokenType.COIN;
            }

            return TokenType.LOSE_TAIL;

        } else {
            // until snake is at least three segments
            return TokenType.COIN;
        }

    }

    private boolean collectablesDoNotOverlap() {
        boolean result = false;

        // check collectable collision
        if ( collectable.getX() == snake.getSnakeHead().getX() &&
                collectable.getY() == snake.getSnakeHead().getY() ) {
            result = true;
        }

        // check snake collision with itself
        for ( SnakeBodySegment bodySegment : snake.getSnakeBodySegments() ) {
            if ( collectable.getX() == bodySegment.getX() && collectable.getY() == bodySegment.getY() ) {
                result = true;
            }
        }

        // check collectables do not overlap
        if (collectable.isAvailable() && speedToken.isAvailable()) {
            if ((collectable.getX() == speedToken.getX()) &&
                    collectable.getY() == speedToken.getY()) {
                result = true;
            }
        }

        return result;
    }

    private void doesSnakeBiteItselfOrCoin() {

        Collectable[] collectables = {collectable, speedToken};
        SnakeHead snakeHead = snake.getSnakeHead();
        Rectangle snakeHeadBounds = snakeHead.getBounds();

        for ( Collectable thisCollectable : collectables ) {
            Rectangle collectableBounds = thisCollectable.getBounds();

            boolean overlaps = Intersector.overlaps( snakeHeadBounds, collectableBounds );

            if ( thisCollectable.isAvailable() && overlaps ) {
                thisCollectable.setAvailable( false );

                if ( thisCollectable.getTokenType() == TokenType.COIN ) {
                    listener.hitCoin();
                } else if ( thisCollectable.getTokenType() == TokenType.LOSE_TAIL ) {
                    listener.loseTail();
                } else if ( thisCollectable.getTokenType() == TokenType.SPEED_UP ) {
                    listener.speedUp();
                } else if ( thisCollectable.getTokenType() == TokenType.SPEED_DOWN ) {
                    listener.speedDown();
                }
                log.debug( "type: " + thisCollectable.getTokenType() );

            }

        }


        for ( SnakeBodySegment segment : snake.getSnakeBodySegments() ) {
            Rectangle segmentBounds = segment.getBounds();
            boolean overlaps = Intersector.overlaps( segmentBounds, snakeHeadBounds );
            if ( overlaps ) {

                //  the following checks the index of the colliding segment as new segments are added to the head position
                int indexOfSegment = snake.getSnakeBodySegments().indexOf( segment, true );
                gameOver = indexOfSegment != 0;
            }

            if ( gameOver ) {
                listener.loseGame();
                GameManager.INSTANCE.updateHighScore();
                GM.setGameState( GameState.GAME_OVER );

            }
        }
    }

    private void restart() {
        timer = 0;
        snake.reset();
        GM.reset();
    }

}
