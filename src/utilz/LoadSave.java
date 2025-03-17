package utilz;

import entities.Crabby;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static utilz.Constants.EnemyConstants.CRABBY;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long_01.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String OPTIONS_BUTTONS = "options_background.png";
    public static final String DEATH_SCREEN = "death_screen.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {

        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }


    public static ArrayList<Crabby> GetCrab() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> crab = new ArrayList<>();
        IntStream.range(0, img.getHeight()).forEach(j ->
                IntStream.range(0, img.getWidth()).forEach(i -> {
                    if (new Color(img.getRGB(i, j)).getGreen() == CRABBY) {
                        crab.add(new Crabby(i * Game.TILE_SIZE, j * Game.TILE_SIZE));
                    }
                })
        );
        return crab;
    }


    public static int[][] GetLevelData() {

        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] levelData = new int[img.getHeight()][img.getWidth()];

        IntStream.range(0, img.getHeight()).forEach(j ->
                IntStream.range(0, img.getWidth()).forEach(i -> {
                    int value = new Color(img.getRGB(i, j)).getRed();
                    levelData[j][i] = (value >= 48) ? 0 : value;
                })
        );
        return levelData;
    }
}
