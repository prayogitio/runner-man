package state;

import core.Size;
import display.Camera;
import entity.GameObject;
import graphics.SpriteLibrary;
import input.Input;
import map.GameMap;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class State {

    List<GameObject> gameObjects;
    protected SpriteLibrary spriteLibrary;
    protected GameMap gameMap;
    protected Camera camera;
    protected Input input;

    public State(Size windowSize, Input input) {
        this.input = input;
        gameObjects = new ArrayList<>();
        spriteLibrary = new SpriteLibrary();
        gameMap = new GameMap(new Size(15, 15), spriteLibrary);
        camera = new Camera(windowSize);
    }

    public void update() {
        for (int i = 0; i < gameObjects.size(); ++i) {
            gameObjects.get(i).update(this);
        }
        camera.update(this);
        sortObjectsByPosition();
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Camera getCamera() {
        return camera;
    }

    public Input getInput() {
        return input;
    }

    private void sortObjectsByPosition() {
        gameObjects.sort(
            Comparator.comparing(GameObject::getRenderOrder).thenComparing(gameObject -> gameObject.getPosition().getY()));
    }

    public abstract void startGame();
}
