package entity;

import controller.EnemyController;
import core.Motion;
import core.Position;
import game.GameLoop;
import graphics.AnimationManager;
import graphics.SpriteLibrary;
import map.Pathfinder;
import state.GameState;
import state.State;

import java.awt.Rectangle;
import java.util.List;

public class Enemy extends MovingEntity {

    private Player player;
    private int timer;
    private int changePathCoolDown;

    public Enemy(EnemyController controller, SpriteLibrary spriteLibrary, Player player) {
        super(controller);
        this.player = player;
        animationManager = new AnimationManager(spriteLibrary.getSpriteSet("dave"));
        animationManager.playAnimation("stand");
        target = new Position(-1, -1);
        setMotion(new Motion(1));
        changePathCoolDown = GameLoop.UPDATES_PER_SECOND * 3;
    }

    @Override
    public void update(State state) {
        super.update(state);
        timer++;
        if (timer >= changePathCoolDown) {
            timer = 0;
            handleMovement(state, player);
        }

        EnemyController controller = (EnemyController) this.controller;
        if (arrived(0) || path.isEmpty()) {
            controller.stop();
            motion.stopMoving(true, true);
            target = new Position(-1, -1);
        }

        if (!path.isEmpty() && getPosition().isInRangeOf(path.get(0), 0)) {
            path.remove(0);
        }

        if (!path.isEmpty()) {
            controller.moveToTarget(path.get(0), getPosition(), 0);
        }

        if (isCollidingWithPlayer()) {
            GameState gameState = (GameState) state;
            gameState.setGameOver(true);
        }
    }

    public boolean isCollidingWithPlayer() {
        return !player.isHandicapped() && getCollisionBox().intersects(player.getCollisionBox());
    }

    private void handleMovement(State state, Player gameObject) {
        EnemyController controller = (EnemyController) this.controller;
        this.path.removeAll(this.path);
        controller.stop();
        motion.stopMoving(true, true);
        Rectangle playerCollisionBox = gameObject.getCollisionBox();
        target = new Position(playerCollisionBox.getX(), playerCollisionBox.getY());
        List<Position> paths = Pathfinder.findPath(getPosition(), target, state.getGameMap());
        if (!paths.isEmpty()) {
            this.path.addAll(paths);
        }
    }
}
