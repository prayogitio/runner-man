package map;

import graphics.SpriteLibrary;
import java.awt.Image;

public class Tile {

    private boolean walkable;
    private SpriteLibrary spriteLibrary;

    public Tile(boolean walkable, SpriteLibrary spriteLibrary) {
        this.walkable = walkable;
        this.spriteLibrary = spriteLibrary;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public Image getSprite() {
        return isWalkable() ? spriteLibrary.getTile("walkable") : spriteLibrary.getTile("unwalkable");
    }

    public void setUnwalkable() {
        walkable = false;
    }
}
