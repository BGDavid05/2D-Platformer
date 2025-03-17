package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements Statemethods {

    private LevelManager levelManager;
    private Player player;
    private EnemyManager enemyManager;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;

    //map moving
    private int xLvlOffset = 0;
    private int leftBorder = (int) (0.3 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.7 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILE_SIZE;
    private boolean gameOver = false;


    public Playing(Game game) {
        super(game);
        initClasses();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, 64, 40, this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);


    }


    @Override
    public void update() {

        if (!gameOver) {
            if (!paused) {
                levelManager.update();
                player.update();
                enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
                checkCloseToBorder();
            } else {
                pauseOverlay.update();
            }
        }
        gameOverOverlay.update();

    }

    private void checkCloseToBorder() {

        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {

            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void draw(Graphics g) {

        if (gameOver) {
            gameOverOverlay.draw(g);
            return;
        }

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 215));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (paused) {
            pauseOverlay.mousePressed(e);
        }else if (gameOver) {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }else if (gameOver) {
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }else if (gameOver) {
            gameOverOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    player.setDir(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    player.setDir(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
            default:
                break;
        }
    }

    public void unpause() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        player.resetAll();
        enemyManager.resetAll();
    }

}
