package display;

import action.Handicap;
import core.Position;
import entity.Player;
import game.Game;
import map.GameMap;
import map.Tile;
import state.GameState;
import state.State;

import java.awt.Color;
import java.awt.Graphics;

public class Renderer {

    public void render(State state, Graphics graphics) {
        GameState gameState = (GameState) state;
        if (!gameState.isGameStarted()) {
            renderStartGameScreen(graphics);
        } else if (gameState.isGameOver()) {
            renderGameOverScreen(graphics);
        } else if (gameState.isPlayerSurvived()) {
            renderGameFinish(graphics);
        } else {
            renderGameMap(state, graphics);
            renderGameObjects(state, graphics);
            //renderCollisionBoxes(state, graphics);
            //renderPath(state, graphics);
            //renderPathTarget(state, graphics);
            renderEnemyCount(state, graphics);
            renderHandicapCoolDown(state, graphics);
        }
    }

    private void renderGameFinish(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawString("Congratulations! You have survived.", 50, 70);
        graphics.drawString("Press ( R ) to replay", 50, 85);
        graphics.drawString("Press ( ESC ) to exit", 50, 100);
    }

    private void renderHandicapCoolDown(State state, Graphics graphics) {
        GameState gameState = (GameState) state;
        gameState.getGameObjects().stream()
            .filter(gameObject -> gameObject instanceof Player)
            .forEach(gameObject -> {
                Player player = (Player) gameObject;
                Handicap handicap = player.getHandicap();
                graphics.setColor(Color.WHITE);
                graphics.drawString("Handicap left: " + handicap.getMaxUsage(), 15, 95);
                graphics.drawString("Handicap cooldown: " + String.format("%.2f", handicap.getCoolDownTime()), 15, 110);
                if (player.isHandicapped()) {
                    Camera camera = state.getCamera();
                    Position cameraPosition = camera.getPosition();
                    graphics.setColor(Color.WHITE);
                    graphics.drawString("Handicap duration: " + String.format("%.2f", handicap.getHandicapDuration()), 15,
                        125);
                    graphics.setColor(Color.YELLOW);
                    graphics.drawRect(
                        (int) player.getCollisionBox().getX() - cameraPosition.intX(),
                        (int) player.getCollisionBox().getY() - cameraPosition.intY(),
                        (int) player.getCollisionBox().getWidth(),
                        (int) player.getCollisionBox().getHeight()
                    );
                }
            });
    }

    private void renderStartGameScreen(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        int y = 70;
        graphics.drawString("Welcome to Runner Man!", 50, y); y += 20;
        graphics.drawString("Features:", 50, y); y += 15;
        graphics.drawString("- New enemy spawn every 5 seconds on random green tile", 50, y); y += 15;
        graphics.drawString("- Enemies will always find their way to your position", 50, y); y += 15;
        graphics.drawString("- You can\'t walk through gray tiles", 50, y); y += 15;
        graphics.drawString("- 3 new gray tiles will spawn every 7 seconds", 50, y); y += 15;
        graphics.drawString("- Handicap effect will last for 3 seconds", 50, y); y += 15;
        graphics.drawString("- Handicap cool down is 5 seconds", 50, y); y += 20;
        graphics.drawString("Win condition:", 50, y); y += 15;
        graphics.drawString("- Green tiles is 35% of total tiles and you survive", 50, y); y += 20;
        graphics.drawString("Lose condition:", 50, y); y += 15;
        graphics.drawString("- You collide with enemy", 50, y); y += 20;
        graphics.drawString("In game control:", 50, y); y += 15;
        graphics.drawString("- Arrow keys to go to destination", 50, y); y += 15;
        graphics.drawString("- Press space to gain HANDICAP (immune from collision and run a little bit faster)", 50,
            y); y += 30;
        graphics.drawString("Press ( P ) to play", 50, y); y += 15;
        graphics.drawString("Press ( ESC ) to exit", 50, y);
    }

    private void renderGameOverScreen(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawString("Game over!", 50, 70);
        graphics.drawString("Press ( R ) to replay", 50, 85);
        graphics.drawString("Press ( ESC ) to exit", 50, 100);
    }

    private void renderEnemyCount(State state, Graphics graphics) {
        GameState gameState = (GameState) state;
        GameMap gameMap = gameState.getGameMap();
        graphics.setColor(Color.WHITE);
        graphics.drawString("Total tiles: " + gameMap.getTotalTiles(), 15, 20);
        graphics.drawString("Green tiles: " + gameMap.getWalkableTiles(), 15, 35);
        graphics.drawString("Gray tiles: " + gameMap.getUnwalkableTiles(), 15, 50);
        graphics.drawString("Unwalkable ratio: " + String.format("%.2f %%", gameMap.getUnwalkableTilesRatio()), 15,
            65);
        graphics.drawString("Enemies: " + gameState.getEnemyCount(), 15,
            80);
    }

    private void renderPathTarget(State state, Graphics graphics) {
        Camera camera = state.getCamera();
        Position cameraPosition = camera.getPosition();
        state.getGameObjects().stream()
            .filter(gameObject -> gameObject instanceof Player)
            .forEach(gameObject -> {
                Player player = (Player) gameObject;
                if (player.getTarget() != null) {
                    graphics.setColor(Color.BLUE);
                    graphics.drawRect(
                        player.getTarget().intX() - cameraPosition.intX(),
                        player.getTarget().intY() - cameraPosition.intY(),
                        Game.SPRITE_SIZE,
                        Game.SPRITE_SIZE
                    );
                }
            });
    }

    private void renderPath(State state, Graphics graphics) {
        Camera camera = state.getCamera();
        Position cameraPosition = camera.getPosition();
        state.getGameObjects().stream()
            .filter(gameObject -> gameObject instanceof Player)
            .forEach(gameObject -> {
                Player player = (Player) gameObject;
                player.getPath().forEach(position -> {
                    graphics.setColor(Color.ORANGE);
                    graphics.drawRect(
                        position.intX() + Game.SPRITE_SIZE / 3 - cameraPosition.intX(),
                        position.intY() + Game.SPRITE_SIZE / 3 - cameraPosition.intY(),
                        Game.SPRITE_SIZE / 3,
                        Game.SPRITE_SIZE / 2
                    );
                });
            });
    }

    private void renderCollisionBoxes(State state, Graphics graphics) {
        Camera camera = state.getCamera();
        Position cameraPosition = camera.getPosition();
        state.getGameObjects().stream()
            .filter(gameObject -> gameObject.getCollisionBox() != null)
            .forEach(gameObject -> {
                graphics.setColor(Color.RED);
                graphics.drawRect(
                (int) gameObject.getCollisionBox().getX() - cameraPosition.intX(),
                (int) gameObject.getCollisionBox().getY() - cameraPosition.intY(),
                (int) gameObject.getCollisionBox().getWidth(),
                (int) gameObject.getCollisionBox().getHeight()
            );
        });
        state.getGameMap().getCollisionBoxes().forEach(rectangle -> {
            graphics.setColor(Color.RED);
            graphics.drawRect(
                (int) rectangle.getX() - cameraPosition.intX(),
                (int) rectangle.getY() - cameraPosition.intY(),
                (int) rectangle.getWidth(),
                (int) rectangle.getHeight()
            );
        });
    }

    private void renderGameMap(State state, Graphics graphics) {
        Camera camera = state.getCamera();
        Position cameraPosition = camera.getPosition();
        Tile[][] tiles = state.getGameMap().getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                graphics.drawImage(
                    tiles[i][j].getSprite(),
                    i * Game.SPRITE_SIZE - cameraPosition.intX(),
                    j * Game.SPRITE_SIZE - cameraPosition.intY(),
                    null
                );
                graphics.drawRect(
                    i * Game.SPRITE_SIZE - cameraPosition.intX(),
                    j * Game.SPRITE_SIZE - cameraPosition.intY(),
                    Game.SPRITE_SIZE,
                    Game.SPRITE_SIZE
                );
            }
        }
    }

    private void renderGameObjects(State state, Graphics graphics) {
        Camera camera = state.getCamera();
        Position cameraPosition = camera.getPosition();
        state.getGameObjects().forEach(gameObject -> {
                graphics.drawImage(
                    gameObject.getSprite(),
                    gameObject.getPosition().intX() - cameraPosition.intX(),
                    gameObject.getPosition().intY() - cameraPosition.intY(),
                    null
                );
            });
    }
}
