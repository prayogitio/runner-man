package entity;

import core.Position;
import core.Size;
import game.Game;
import state.State;

import java.awt.Image;
import java.awt.Rectangle;

public abstract class GameObject {

    protected Position position;
    protected Size size;
    protected int renderOrder;

    public GameObject() {
        this.position = new Position(0, 0);
        this.size = new Size(Game.SPRITE_SIZE, Game.SPRITE_SIZE);
        this.renderOrder = 0;
    }

    public abstract void update(State state);
    public abstract Image getSprite();
    public abstract Rectangle getCollisionBox();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getRenderOrder() {
        return renderOrder;
    }
}
