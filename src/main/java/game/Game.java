package game;

import core.Size;
import display.Display;
import input.Input;
import state.GameState;
import state.State;

public class Game {

    public final static int SPRITE_SIZE = 64;

    private Display display;
    private State gameState;
    private Input input;

    public Game(int width, int height) {
        input = new Input();
        display = new Display(width, height, input);
        gameState = new GameState(input, new Size(width, height));
    }

    public void update() {
        GameState state = (GameState) gameState;
        if (!state.isGameStarted()) {
            if (input.isExitGameRequested()) {
                System.exit(1);
            } else if (input.isStartGameRequested()) {
                gameState.startGame();
            }
        } else if (!state.isGameOver() && !state.isPlayerSurvived()) {
            gameState.update();
        } else if (input.isRestartRequested()) {
            state.restartGameState();
        } else if (input.isExitGameRequested()) {
            System.exit(1);
        }
    }

    public void render() {
        display.render(gameState);
    }
}
