package gamestates;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIN(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(Gamestate state) {

        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().playSong(AudioPlayer.LEVEL_1);
        }

        Gamestate.state = state;
    }

}
