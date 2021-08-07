package action;

import core.Motion;
import entity.Player;
import game.GameLoop;

public class Handicap {

    protected float lifeSpan;
    protected int coolDown;
    protected int timer;
    protected float castedTime;
    private boolean isInAction;
    private int maxUsage;

    public Handicap() {
        lifeSpan = GameLoop.UPDATES_PER_SECOND * 3;
        coolDown = GameLoop.UPDATES_PER_SECOND * 5;
        timer = 0;
        castedTime = 0;
        isInAction = false;
        maxUsage = 5;
    }

    public boolean isAvailable() {
        return maxUsage > 0;
    }

    public boolean isCoolingDown() {
        return castedTime > 0;
    }

    public void start(Player player) {
        timer = 0;
        castedTime = coolDown;
        isInAction = true;
        player.setMotion(new Motion(4));
        maxUsage--;
    }

    public boolean isInAction() {
        return isInAction;
    }

    public void update(Player player) {
        if (isCoolingDown()) {
            castedTime--;
        }
        if (isInAction) {
            timer++;
            if (timer >= lifeSpan) {
                timer = 0;
                isInAction = false;
                player.setMotion(new Motion(3));
            }
        }
    }

    public float getCoolDownTime() {
        return castedTime / GameLoop.UPDATES_PER_SECOND;
    }

    public float getHandicapDuration() {
        return (lifeSpan - timer) / GameLoop.UPDATES_PER_SECOND;
    }

    public int getMaxUsage() {
        return maxUsage;
    }
}
