package controller;

import core.Position;
import input.Input;

public class EnemyController implements Controller {

    private Input input;
    private boolean up;
    private boolean right;
    private boolean down;
    private boolean left;

    public EnemyController(Input input) {
        this.input = input;
    }

    @Override public boolean isRequestingUp() {
        return up;
    }

    @Override public boolean isRequestingRight() {
        return right;
    }

    @Override public boolean isRequestingDown() {
        return down;
    }

    @Override public boolean isRequestingLeft() {
        return left;
    }

    public void moveToTarget(Position target, Position current, int proximityRange) {
        double deltaX = target.getX() - current.getX();
        double deltaY = target.getY() - current.getY();

        up = deltaY < 0 && Math.abs(deltaY) > proximityRange;
        right = deltaX > 0 && Math.abs(deltaX) > proximityRange;
        down = deltaY > 0 && Math.abs(deltaY) > proximityRange;
        left = deltaX < 0 && Math.abs(deltaX) > proximityRange;
    }

    public void stop() {
        up = false;
        right = false;
        down = false;
        left = false;
    }
}
