package display;

import core.Position;
import core.Size;
import entity.GameObject;
import entity.Player;
import game.Game;
import map.Tile;
import state.State;

import java.util.Optional;

public class Camera {

    private Position position;
    private Size windowSize;
    private Optional<GameObject> player;

    public Camera(Size windowSize) {
        player = Optional.empty();
        position = new Position(0, 0);
        this.windowSize = windowSize;
    }

    public void update(State state) {
        if (player.isPresent()) {
            position.setX(player.get().getPosition().intX() - windowSize.getWidth() / 2);
            position.setY(player.get().getPosition().intY() - windowSize.getHeight() / 2);
            clampCameraToMap(state);
        }
    }

    private void clampCameraToMap(State state) {
        Tile[][] tiles = state.getGameMap().getTiles();
        if (position.intX() < 0) position.setX(0);
        if (position.intX() + windowSize.getWidth() > tiles[0].length * Game.SPRITE_SIZE) position.setX(tiles[0].length * Game.SPRITE_SIZE - windowSize.getWidth());
        if (position.intY() < 0) position.setY(0);
        if (position.intY() + windowSize.getHeight() > tiles.length * Game.SPRITE_SIZE) position.setY(tiles.length * Game.SPRITE_SIZE - windowSize.getHeight());
    }

    public Position getPosition() {
        return position;
    }

    public Size getWindowSize() {
        return windowSize;
    }

    public void setFocusOn(Player player) {
        this.player = Optional.of(player);
    }
}
