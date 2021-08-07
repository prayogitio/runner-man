package map;

import core.Position;
import core.Size;
import entity.Enemy;
import entity.GameObject;
import entity.Player;
import game.Game;
import graphics.SpriteLibrary;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private Tile[][] tiles;
    private Size size;
    private List<Rectangle> collisionBoxes;
    private int walkableTiles;
    private int unwalkableTiles;
    private int totalTiles;
    private SpriteLibrary spriteLibrary;

    public GameMap(Size size, SpriteLibrary spriteLibrary) {
        this.size = size;
        this.spriteLibrary = spriteLibrary;
    }

    public void restartGameMap() {
        collisionBoxes = new ArrayList<>();
        tiles = new Tile[size.getWidth()][size.getHeight()];
        walkableTiles = 0;
        unwalkableTiles = 0;
        totalTiles = 0;
        initializeTiles(spriteLibrary);
    }

    private void initializeTiles(SpriteLibrary spriteLibrary) {
        for (int i = 0; i < size.getWidth(); ++i) {
            for (int j = 0; j < size.getHeight(); ++j) {
                boolean walkable = true;
                if (i % 2 != 0 && j % 2 != 0) {
                    walkable = false;
                    unwalkableTiles++;
                } else {
                    walkableTiles++;
                }
                tiles[i][j] = new Tile(walkable, spriteLibrary);
                if (!walkable) {
                    Rectangle collisionBox = new Rectangle(
                        i * Game.SPRITE_SIZE,
                        j * Game.SPRITE_SIZE,
                        Game.SPRITE_SIZE,
                        Game.SPRITE_SIZE
                    );
                    collisionBoxes.add(collisionBox);
                }
                totalTiles++;
            }
        }
    }

    public boolean gridWithinBounds(int gridX, int gridY) {
        return gridX >= 0 && gridX < tiles.length
            && gridY >= 0 && gridY < tiles[0].length;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public List<Rectangle> getCollisionBoxes() {
        return collisionBoxes;
    }

    public boolean tileIsAvailable(int x, int y) {
        return getTile(x, y).isWalkable();
    }

    private Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWalkableTiles() {
        return walkableTiles;
    }

    public int getUnwalkableTiles() {
        return unwalkableTiles;
    }

    public int getTotalTiles() {
        return totalTiles;
    }

    public float getUnwalkableTilesRatio() {
        return ((float) getUnwalkableTiles() / (float) getTotalTiles()) * 100;
    }

    public boolean isSafeEnemySpawnPosition(int spawnX, int spawnY, Player player) {
        int playerSafeZone = 2;
        Position playerPosition = player.getPosition();
        if (tileIsAvailable(spawnX, spawnY)
            && Math.abs(playerPosition.gridX() - spawnX) >= playerSafeZone
            && Math.abs(playerPosition.gridY() - spawnY) >= playerSafeZone
        ) {
            return true;
        }
        return false;
    }

    public void increaseUnwalkableTiles() {
        unwalkableTiles++;
    }

    public void decreaseWalkableTiles() {
        walkableTiles--;
    }

    public void addCollisionBoxes(int i, int j) {
        Rectangle collisionBox = new Rectangle(
            i * Game.SPRITE_SIZE,
            j * Game.SPRITE_SIZE,
            Game.SPRITE_SIZE,
            Game.SPRITE_SIZE
        );
        collisionBoxes.add(collisionBox);
    }

    public boolean isSafeUnwalkableSpawnPosition(int spawnX, int spawnY, List<GameObject> gameObjects) {
        int enemySafeZone = 1;
        for (int i = 0; i < gameObjects.size(); ++i) {
            if (gameObjects.get(i) instanceof Enemy) {
                Position enemyPosition = gameObjects.get(i).getPosition();
                if (!tileIsAvailable(spawnX, spawnY)
                    || Math.abs(enemyPosition.gridX() - spawnX) < enemySafeZone
                    || Math.abs(enemyPosition.gridY() - spawnY) < enemySafeZone
                ) {
                    return false;
                }
            }
        }
        return true;
    }
}
