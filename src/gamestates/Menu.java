package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWIDTH, menuHEIGHT;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackGround();

    }

    private void loadBackGround() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWIDTH = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHEIGHT = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWIDTH / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton b : buttons) {
            b.update();
        }
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(107, 8, 8, 150));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(backgroundImg, menuX, menuY, menuWIDTH, menuHEIGHT, null);
        for (MenuButton b : buttons) {
            b.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton b : buttons) {
            if (isIN(e, b)) {
                b.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton b : buttons) {
            if (isIN(e, b)) {
                if (b.isMousePressed()) {
                    b.applyGamestate();
                    if (b.getGamestate() == Gamestate.PLAYING) {
                        game.getAudioPlayer().playSong(AudioPlayer.LEVEL_1);
                    }
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons() {

        for (MenuButton b : buttons) {
            b.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton b : buttons) {
            b.setMouseOver(false);
        }

        for (MenuButton b : buttons) {
            if (isIN(e, b)) {
                b.setMouseOver(true);
                break;
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
