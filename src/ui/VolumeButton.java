package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.VolumeButtons.*;


public class VolumeButton extends PauseButton {

    BufferedImage[] imgs;
    BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonx;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;


    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);

        buttonx = x - VOLUME_WIDTH / 2;
        buttonx = x + width / 2;
        this.x = x;
        this.width = width;

        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);

        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = tmp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        slider = tmp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

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


    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    public void draw(Graphics g) {

        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonx - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);

    }

    public void changeX(int x) {
        if (x < minX) {
            buttonx = minX;
        } else if (x > maxX) {
            buttonx = maxX;
        } else {
            buttonx = x;
        }

        buttonX = x;
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public float getFloatValue() {
        return floatValue;
    }

}
