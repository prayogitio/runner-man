package input;

import core.Position;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    private boolean[] pressed;
    private boolean[] currentlyPressed;
    private Position clickPosition;
    private boolean isRightMouseClicked;

    public Input() {
        resetAllKeyBinds();
    }

    public void resetAllKeyBinds() {
        pressed = new boolean[1000];
        currentlyPressed = new boolean[1000];
        clickPosition = new Position(-1, -1);
        isRightMouseClicked = false;
    }

    public boolean isCurrentlyPressed(int keyCode) {
        return currentlyPressed[keyCode];
    }

    public boolean isPressed(int keyCode) {
        if (!pressed[keyCode] && isCurrentlyPressed(keyCode)) {
            pressed[keyCode] = true;
            return true;
        }
        return false;
    }

    @Override public void keyTyped(KeyEvent e) {
    }

    @Override public void keyPressed(KeyEvent e) {
        currentlyPressed[e.getKeyCode()] = true;
        if (e.getKeyCode() != KeyEvent.VK_SPACE)
          resetIsRightMouseClicked();
    }

    @Override public void keyReleased(KeyEvent e) {
        currentlyPressed[e.getKeyCode()] = false;
        pressed[e.getKeyCode()] = false;
    }

    @Override public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            clickPosition = new Position(e.getPoint().getX(), e.getPoint().getY());
            isRightMouseClicked = true;
        }
    }

    @Override public void mousePressed(MouseEvent e) {
    }

    @Override public void mouseReleased(MouseEvent e) {
    }

    @Override public void mouseEntered(MouseEvent e) {

    }

    @Override public void mouseExited(MouseEvent e) {

    }

    @Override public void mouseDragged(MouseEvent e) {

    }

    @Override public void mouseMoved(MouseEvent e) {

    }

    public void resetIsRightMouseClicked() {
        isRightMouseClicked = false;
    }

    public Position getClickPosition() {
        return clickPosition;
    }

    public boolean isRightMouseClicked() {
        return isRightMouseClicked;
    }

    public boolean isRestartRequested() {
        return isPressed(KeyEvent.VK_R);
    }

    public boolean isExitGameRequested() {
        return isPressed(KeyEvent.VK_ESCAPE);
    }

    public boolean isStartGameRequested() {
        return isPressed(KeyEvent.VK_P);
    }
}
