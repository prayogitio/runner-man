package entity;

import controller.Controller;
import core.Direction;
import core.Motion;
import core.Position;
import game.Game;
import graphics.AnimationManager;
import state.State;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MovingEntity extends GameObject {

    private Direction direction;
    protected Motion motion;
    protected Controller controller;
    protected AnimationManager animationManager;
    protected Position clickPosition;
    protected Position target;
    protected List<Position> path;

    public MovingEntity(Controller controller) {
        path = new ArrayList<>();
        motion = new Motion(3);
        this.controller = controller;
        direction = Direction.S;
        clickPosition = null;
        target = null;
    }

    @Override public void update(State state) {
        motion.update(controller);
        decideAnimation();
        animationManager.update(decideDirection());
        handleCollidingWithTiles(state);
        handleOutOfMap(state);

        position.apply(motion);
    }

    protected boolean arrived(int proximityRange) {
        return new Position(getCollisionBox().getX(), getCollisionBox().getY()).isInRangeOf(target, proximityRange);
    }

    private void handleOutOfMap(State state) {
        Position position = new Position(getCollisionBox().getX(), getCollisionBox().getY());
        position.apply(motion);
        if (position.intX() < 0 || position.intX() + Game.SPRITE_SIZE / 3 > state.getGameMap().getTiles()[0].length * Game.SPRITE_SIZE
            || position.intY() < 0 || position.intY() + Game.SPRITE_SIZE / 2 > state.getGameMap().getTiles().length * Game.SPRITE_SIZE
        ) {
            motion.stopMoving(true, true);
        }
    }

    private void handleCollidingWithTiles(State state) {
        state.getGameMap().getCollisionBoxes().forEach(rectangle -> {
            motion.stopMoving(willCollideX(rectangle), willCollideY(rectangle));
        });
    }

    private boolean willCollideY(Rectangle rectangle) {
        Position position = new Position(getCollisionBox().getX(), getCollisionBox().getY());
        position.applyY(motion);
        Rectangle temp = new Rectangle(
            position.intX(),
            position.intY(),
            (int) getCollisionBox().getWidth(),
            (int) getCollisionBox().getHeight()
        );
        return temp.intersects(rectangle);
    }

    private boolean willCollideX(Rectangle rectangle) {
        Position position = new Position(getCollisionBox().getX(), getCollisionBox().getY());
        position.applyX(motion);
        Rectangle temp = new Rectangle(
            position.intX(),
            position.intY(),
            (int) getCollisionBox().getWidth(),
            (int) getCollisionBox().getHeight()
        );
        return temp.intersects(rectangle);
    }

    private Direction decideDirection() {
        if (motion.isMoving()) {
            direction = Direction.fromMotion(motion);
        }
        return direction;
    }

    private void decideAnimation() {
        if (motion.isMoving()) {
            animationManager.playAnimation("walk");
        } else {
            animationManager.playAnimation("stand");
        }
    }

    @Override public Image getSprite() {
        return animationManager.getSprite();
    }

    @Override public Rectangle getCollisionBox() {
        return new Rectangle(
            position.intX() + Game.SPRITE_SIZE / 3,
            position.intY() + Game.SPRITE_SIZE / 3,
            size.getWidth() / 3,
            size.getHeight() / 2
        );
    }

    public List<Position> getPath() {
        return path;
    }

    public Position getTarget() {
        return target;
    }

    public void setMotion(Motion motion) {
        this.motion = motion;
    }
}
