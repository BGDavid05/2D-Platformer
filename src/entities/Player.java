package entities;

import gamestates.Playing;
import utilz.LoadSave;

import static main.Game.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    //Animation
    private BufferedImage[][] animations;
    private Playing playing;
    private int aniTick, aniIndex, aniSpeed = (int) (15 * SCALE);
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private int[][] levelData;
    private float xDrawOffset = 21.4f * SCALE, yDrawOffset = 4 * SCALE;

    //Jumping Air
    private boolean inAir = false;
    private float playerSpeed = 1.0f * SCALE;
    private float airSpeed = 0f;
    private float gravity = 0.04f * SCALE;
    private float jumpSpeed = -2.25f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;
    private int health = 9;

    private boolean dir;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, (int) (SCALE * width), (int) (SCALE * height));
        this.playing = playing;
        loadAnimations();
        initHitBox(x, y, 20 * SCALE, 28 * SCALE);
    }

    public void update() {

        if (health <= 0) {
            playing.setGameOver(true);
            return;
        }
        updatePos();

        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, int lvlOffset) {

        if (!dir) {
            g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + width, (int) (hitbox.y - yDrawOffset), -width, height, null);
        } else {
            g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        }

    }

    private void updateAnimationTick() {

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
            }

        }

    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
        }
        if (attacking) {
            playerAction = ATTACK_1;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;
        if (jump) {
            jump_();
        }
        if (!left && !right && !inAir) {
            return;
        }
        float xSpeed = 0;

        if (left) {
            xSpeed = -playerSpeed;
        }
        if (right) {
            xSpeed = playerSpeed;
        }

        if (!inAir) {

            if (!IsEntityOnFloor(hitbox, levelData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }

        } else {
            updateXPos(xSpeed);
            moving = true;
        }


    }

    private void jump_() {
        if (inAir) {
            return;
        }

        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimations() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {

            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!IsEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void changeHealth(int i) {
        health += i;
        System.out.println(health);
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        health = 9;

        hitbox.x = x;
        hitbox.y = y;
    }

}