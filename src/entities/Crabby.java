package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;


public class Crabby extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, (int) 22 * Game.SCALE, (int) 19 * Game.SCALE);
        initAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateAnimationTick();
        updateMove(lvlData, player);
        updateAttackBox();
    }

    private void updateMove(int[][] lvlData, Player player) {

        if (firsUpdate) {
            firsUpdate = false;
            firsUpdateCheck(lvlData);
        }

        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerInRangeAttack(player)) {
                        newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 3 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }

                    break;
            }
        }
    }

}
