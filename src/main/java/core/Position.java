package core;

import game.Game;

public class Position {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position ofGridPosition(int gridX, int gridY) {
        return new Position(gridX * Game.SPRITE_SIZE, gridY * Game.SPRITE_SIZE);
    }

    public boolean isInRangeOf(Position position, int proximityRange) {
        return Math.abs(intX() - position.intX()) <= proximityRange && Math.abs(intY() - position.intY()) <= proximityRange;
    }

    public static boolean isEqual(Position a, Position b) {
        return a.intX() == b.intX() && a.intY() == b.intY();
    }

    public int gridX() {
        return (int) (x / Game.SPRITE_SIZE);
    }

    public int gridY() {
        return (int) (y / Game.SPRITE_SIZE);
    }

    public int intX() {
        return (int) Math.round(x);
    }

    public int intY() {
        return (int) Math.round(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void apply(Motion motion) {
        Vector2D vector2D = motion.getVector2D();
        x += vector2D.getX();
        y += vector2D.getY();
    }

    public void applyX(Motion motion) {
        Vector2D vector2D = motion.getVector2D();
        x += vector2D.getX();
    }

    public void applyY(Motion motion) {
        Vector2D vector2D = motion.getVector2D();
        y += vector2D.getY();
    }
}
