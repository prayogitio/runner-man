package core;

import controller.Controller;

public class Motion {

    private Vector2D vector2D;
    private int speed;

    public Motion(int speed) {
        this.speed = speed;
        vector2D = new Vector2D(0, 0);
    }

    public void update(Controller controller) {
        int deltaX = 0;
        int deltaY = 0;
        if (controller.isRequestingUp()) deltaY--;
        if (controller.isRequestingRight()) deltaX++;
        if (controller.isRequestingDown()) deltaY++;
        if (controller.isRequestingLeft()) deltaX--;

        vector2D = new Vector2D(deltaX, deltaY);
        //vector2D.normalize();
        vector2D.multiply(speed);
    }

    public Vector2D getVector2D() {
        return vector2D;
    }

    public boolean isMoving() {
        return vector2D.length() > 0;
    }

    public void stopMoving(boolean collideX, boolean collideY) {
        vector2D = new Vector2D(collideX ? 0 : vector2D.getX(), collideY ? 0 : vector2D.getY());
    }
}
