package entity;

import action.Handicap;
import controller.PlayerController;
import core.Position;
import graphics.AnimationManager;
import graphics.SpriteLibrary;
import state.State;

public class Player extends MovingEntity {

    private Handicap handicap;

    public Player(PlayerController controller, SpriteLibrary spriteLibrary) {
        super(controller);
        animationManager = new AnimationManager(spriteLibrary.getSpriteSet("matt"));
        animationManager.playAnimation("stand");
        setPosition(new Position(0, 0));
        handicap = new Handicap();
    }

    @Override
    public void update(State state) {
        super.update(state);
        PlayerController playerController = (PlayerController) controller;
        handlePlayerHandicap(playerController, state);
    }

    private void handlePlayerHandicap(PlayerController playerController, State state) {
        if (playerController.isHandicapRequested()) {
            if (!handicap.isInAction() && !handicap.isCoolingDown() && handicap.isAvailable()) {
                handicap.start(this);
            }
        }
        handicap.update(this);
    }

    public boolean isHandicapped() {
        return handicap.isInAction();
    }

    public Handicap getHandicap() {
        return handicap;
    }
}
