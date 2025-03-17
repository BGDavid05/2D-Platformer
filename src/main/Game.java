package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;


public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioPlayer audioPlayer;
    private AudioOptions audioOptions;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;


    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.8f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = (int) (TILES_IN_WIDTH * TILE_SIZE);
    public final static int GAME_HEIGHT = (int) (TILES_IN_HEIGHT * TILE_SIZE);

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }


    public void render(Graphics g) {
        switch (Gamestate.state) {
            case PLAYING:
                playing.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;  //
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
            }


        }

    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.state.PLAYING) {
            playing.windowFocusLost();
        }

    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }
}