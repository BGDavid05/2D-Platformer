package ui;

import gamestates.Gamestate;

import static utilz.Constants.UI.Buttons.*;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = BUTTON_WIDTH / 2;

    private boolean mouseOver = false;
    private boolean mousePressed = false;

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    private Rectangle bounds;

    private Gamestate gamestate;
    private BufferedImage[] imgs;


    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate gamestate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.gamestate = gamestate;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);

        for (int j = 0; j < imgs.length; j++) {
            imgs[j] = tmp.getSubimage(j * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void applyGamestate() {
        Gamestate.state = gamestate;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

}

