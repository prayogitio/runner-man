package state;

import controller.EnemyController;
import controller.PlayerController;
import core.Position;
import core.Size;
import entity.Enemy;
import entity.Player;
import game.Game;
import game.GameLoop;
import input.Input;

import java.util.concurrent.ThreadLocalRandom;

public class GameState extends State {

    private Input input;
    private int spawnEnemyCoolDown;
    private int spawnUnwalkableTileCoolDown;
    private int spawnEnemyTimer;
    private int spawnTileTimer;
    private Player player;
    private int enemyCount;
    private boolean isGameOver;
    private boolean isGameStarted;
    private boolean isPlayerSurvived;
    private final int MAX_SPAWNED_ENEMY = 15;

    public GameState(Input input, Size windowSize) {
        super(windowSize, input);
        this.input = input;
        restartGameState();
        isGameStarted = false;
    }

    public void restartGameState() {
        getGameMap().restartGameMap();
        getGameObjects().clear();
        player = initializePlayer();
        initializeEnemies();
        spawnEnemyCoolDown = GameLoop.UPDATES_PER_SECOND * 5;
        spawnUnwalkableTileCoolDown = GameLoop.UPDATES_PER_SECOND * 7;
        enemyCount = 1;
        isGameOver = false;
        spawnEnemyTimer = 0;
        spawnTileTimer = 0;
        isPlayerSurvived = false;
    }

    private void initializeEnemies() {
        gameObjects.add(spawnEnemy());
    }

    private Enemy spawnEnemy() {
        int spawnX = ThreadLocalRandom.current().nextInt(0, gameMap.getTiles().length);
        int spawnY = ThreadLocalRandom.current().nextInt(0, gameMap.getTiles()[0].length);
        if (!getGameMap().isSafeEnemySpawnPosition(spawnX, spawnY, player)) {
            return spawnEnemy();
        }

        Enemy enemy = new Enemy(new EnemyController(input), spriteLibrary, player);
        enemy.setPosition(new Position(spawnX * Game.SPRITE_SIZE, spawnY * Game.SPRITE_SIZE));

        return enemy;
    }

    private Player initializePlayer() {
        Player player = new Player(new PlayerController(input), spriteLibrary);
        gameObjects.add(player);
        camera.setFocusOn(player);
        return player;
    }

    public void update() {
        super.update();
        if (getEnemyCount() <= MAX_SPAWNED_ENEMY) {
            spawnEnemyTimer++;
            if (spawnEnemyTimer >= spawnEnemyCoolDown) {
                spawnEnemyTimer = 0;
                gameObjects.add(spawnEnemy());
                enemyCount++;
            }
        }
        spawnTileTimer++;
        if (spawnTileTimer >= spawnUnwalkableTileCoolDown) {
            spawnTileTimer = 0;
            for (int i = 0; i < 4; ++i) {
                Position newUnwalkableTilePosition = getRandomWalkableTile();
                gameMap.getTiles()[newUnwalkableTilePosition.intX()][newUnwalkableTilePosition.intY()].setUnwalkable();
                gameMap.increaseUnwalkableTiles();
                gameMap.decreaseWalkableTiles();
                gameMap.addCollisionBoxes(newUnwalkableTilePosition.intX(), newUnwalkableTilePosition.intY());
            }
        }
    }

    private Position getRandomWalkableTile() {
        int spawnX = ThreadLocalRandom.current().nextInt(0, gameMap.getTiles().length);
        int spawnY = ThreadLocalRandom.current().nextInt(0, gameMap.getTiles()[0].length);
        if (!getGameMap().isSafeEnemySpawnPosition(spawnX, spawnY, player) ||
            !getGameMap().isSafeUnwalkableSpawnPosition(spawnX, spawnY, getGameObjects())
        ) {
            return getRandomWalkableTile();
        }

        return new Position(spawnX, spawnY);
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void startGame() {
        isGameStarted = true;
    }

    public boolean isPlayerSurvived() {
        return gameMap.getUnwalkableTilesRatio() >= 35;
    }
}
