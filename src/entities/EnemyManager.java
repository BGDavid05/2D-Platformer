package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    Playing playing;
    private BufferedImage[][] crabbyArr;
    ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyimgs();
        addEnemys();
    }

    private void addEnemys() {
        crabbies = LoadSave.GetCrab();
    }


    public void update(int[][] lvlData, Player player) {
        for (Crabby crabby : crabbies) {
            crabby.update(lvlData, player);
        }
    }

    public void draw(Graphics g, int xlvlOffset) {

        for (Crabby crabby : crabbies) {
            g.drawImage(crabbyArr[crabby.getEnemyState()][crabby.getAniIndex()], (int) crabby.getHitbox().x - CRABBY_DRAWOFFSET_x - xlvlOffset, (int) crabby.getHitbox().y - CRABBY_DRAWOFFSET_y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }

    }

    private void loadEnemyimgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);

        for (int j = 0; j < crabbyArr.length; j++) {

            for (int i = 0; i < crabbyArr[j].length; i++) {
                crabbyArr[j][i] = tmp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAll() {
        for (Crabby crabby : crabbies) {

            crabby.resetAll();

        }
    }
}
