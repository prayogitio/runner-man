package graphics;

import game.Game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpriteLibrary {

    private Map<String, SpriteSet> spriteSets;
    private Map<String, Image> images;
    private Map<String, Image> tiles;

    public SpriteLibrary() {
        spriteSets = new HashMap<>();
        images = new HashMap<>();
        tiles = new HashMap<>();
        loadSpritesFromDisk();
        loadTiles();
    }

    private void loadTiles() {
        BufferedImage image = new BufferedImage(Game.SPRITE_SIZE, Game.SPRITE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(Color.WHITE);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f*3));
        graphics.fillRect(0, 0, Game.SPRITE_SIZE, Game.SPRITE_SIZE);
        tiles.put("unwalkable", image);
        graphics.dispose();

        image = new BufferedImage(Game.SPRITE_SIZE, Game.SPRITE_SIZE, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        graphics.setPaint(Color.GREEN);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f*3));
        graphics.fillRect(0, 0, Game.SPRITE_SIZE, Game.SPRITE_SIZE);
        tiles.put("walkable", image);
        graphics.dispose();
    }

    private void loadSpritesFromDisk() {
        loadSpriteSets("/sprites/units");
    }

    private void loadSpriteSets(String path) {
        String[] folderNames = getFolderNames(path);

        for(String folderName: folderNames) {
            SpriteSet spriteSet = new SpriteSet();
            String pathToFolder = path + "/" + folderName;
            String[] sheetsInFolder = getImagesInFolder(pathToFolder);

            for(String sheetName: sheetsInFolder) {
                spriteSet.addSheet(
                    sheetName.substring(0, sheetName.length() - 4),
                    ImageUtils.loadImage(pathToFolder + "/" + sheetName));
            }

            spriteSets.put(folderName, spriteSet);
        }
    }

    private String[] getImagesInFolder(String basePath) {
        URL resource = SpriteLibrary.class.getResource(basePath);
        File file = new File(resource.getFile());
        return file.list((current, name) -> new File(current, name).isFile());
    }

    private String[] getFolderNames(String basePath) {
        URL resource = SpriteLibrary.class.getResource(basePath);
        File file = new File(resource.getFile());
        return file.list((current, name) -> new File(current, name).isDirectory());
    }

    public Image getTile(String name) {
        return tiles.get(name);
    }

    public SpriteSet getSpriteSet(String name) {
        return spriteSets.get(name);
    }

    public Image getImage(String name) {
        return images.get(name);
    }
}
