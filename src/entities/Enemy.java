package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;


public abstract class Enemy extends Entity {

    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firsUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed = 0;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.4f * Game.SCALE;
    protected int walkDir = LEFT;
    protected float attackDistance = (Game.TILE_SIZE / 2) * Game.SCALE;
    protected int tileY;
    protected boolean attackChecked = false;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        initHitBox(x, y, width, height);
        this.enemyType = enemyType;
    }


    protected boolean canSeePlayer(int[][] lvlData, Player player) {

        int playerTileY = (int) player.getHitbox().y / Game.TILE_SIZE;
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {

                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean isPlayerInRangeAttack(Player player) {
        int absValue = (int) Math.abs((player.hitbox.x + 10 * SCALE) - (hitbox.x + 11 * SCALE));
        return absValue <= attackDistance;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs((player.hitbox.x + 10 * SCALE) - (hitbox.x + 11 * SCALE));
        return absValue <= (attackDistance * 5);
    }


    protected void updateAnimationTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
                if (enemyState == ATTACK) {
                    enemyState = IDLE;
                }
            }

        }

    }

    protected void firsUpdateCheck(int[][] lvlData) {
        firsUpdate = false;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    protected void updateInAir(int[][] lvlData) {

        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {

            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) hitbox.y / Game.TILE_SIZE;
        }

    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFLoor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    protected void changeWalkDir() {
        if (walkDir == RIGHT) {
            walkDir = LEFT;
        } else {
            walkDir = RIGHT;
        }

    }

    protected void newState(int enemyState) {

        this.enemyState = enemyState;
        aniIndex = 0;
        aniTick = 0;


    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)) {
            System.out.println("hitbox intersects");
            player.changeHealth(-10);

        }

        attackChecked = true;

    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public void resetAll() {
        hitbox.x = x;
        hitbox.y = y;
        firsUpdate = true;
        newState(IDLE);
        fallSpeed = 0;
    }
}
